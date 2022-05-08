package org.apache.lucene.analysis.ko.sp;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.ko.KoreanFilter;
import org.apache.lucene.analysis.ko.KoreanTokenizer;
import org.apache.lucene.analysis.ko.SpKoreanAnalyzer;
import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.MorphAnalyzer;
import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.morph.PatternConstants;
import org.apache.lucene.analysis.ko.morph.SpWordListComparator;
import org.apache.lucene.analysis.ko.morph.SpWordSegmentAnalyzer;
import org.apache.lucene.analysis.ko.morph.WordListCandidate;
import org.apache.lucene.analysis.ko.morph.WordSegmentAnalyzer;
import org.apache.lucene.analysis.ko.utils.SpConstants;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;


public class SpMorphAnalyzer {
	private final SpEmiMorphAnalyzer morphAnal = new SpEmiMorphAnalyzer();
	
    private static final int maxCandidate = 256;

    private static final int adjustNoOfCandidate = 40;
    //private static final int adjustNoOfCandidate = 100;
    
    private static final String possibleWordStartJosa = "의은가나며아야에엔여와요이";
    HashMap<String, String> map = new HashMap<String, String>();
    HashMap<String, String> pmap = new HashMap<String, String>();
	//단어 문법에 맞게 나눠주기
	//selectedId==1 자동나누기
	//selectedId==2 사용자나누기
	public Vector<String> selectToken(String source,String selectedId) {
		Vector<String> inputs = new Vector<String>();
		
		if(selectedId == null || selectedId.equals("1")||source.indexOf(" ")==-1) {//자동 //공백이 없을경우 자동띄어쓰기한다
			inputs = wordSegment2(source);
			//System.out.println("처음"+inputs.toString());
			//if(inputs.size()<2) {
			//	inputs = wordSegment(source);
			//	System.out.println("두번"+inputs.toString());
			//}
			//if(inputs.size()<2) {
			//	inputs = wordToken(source);
			//	System.out.println("세번"+inputs.toString());
			//}
		}else if(selectedId.equals("2")) {
			inputs = wordToken(source);
		}
		return inputs;
	}
	/*public Vector<String> wordSegment(String source) {
		Vector<String> inputs = new Vector<String>();
		TokenStream ts = null ;
		TokenStream tokenStream = null ;
	    //String szText="너오늘진짜예뻐";
		String output = "" ;
		//String s = source.replace(" ","");
		String s = source;
		try {
			KoreanAnalyzer analyzer = new KoreanAnalyzer() ;
			ts = analyzer.tokenStream(null,  new StringReader(s)) ;
			tokenStream = new KoreanFilter(ts);
		    CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class) ;
		    ts.reset() ;
			while(tokenStream.incrementToken()) {
				String token = termAtt.toString();
				inputs.add(token);
			}
			
			tokenStream.end() ;			
			if(s.equals(inputs.elementAt(0))&& inputs.size()>1) {
				inputs.remove(0);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if( tokenStream != null ) try { tokenStream.close() ; } catch(Exception ee){}
		}
		return inputs;
	}*/
	public Vector<String> wordSegment(String source) {
		Vector<String> inputs = new Vector<String>();
		TokenStream ts = null ;
	    //String szText="너오늘진짜예뻐";
		String output = "" ;
		//String s = source.replace(" ","");
		String s = source;
		KoreanTokenizer koreanTokenizer = new KoreanTokenizer();  // tokenizer 적용.
	    koreanTokenizer.setReader(new StringReader(source));
	    
		TokenStream tokenStream = new KoreanFilter(koreanTokenizer,false,false,false,false,false); 
		//TokenStream input, boolean bigram, boolean has, boolean exactMatch, 
		//  boolean cnoun, boolean isQuery
		
		try {
			
			CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
			TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);
			tokenStream.reset();
		      
			while (tokenStream.incrementToken()) {
			 System.out.println(termAtt.toString() + " [" + typeAttr.type() + "]");
				inputs.add(termAtt.toString());
			}
			tokenStream.end();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				tokenStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return inputs;
	}
	/*
	 * 
	 */
	//제외된 부분은 뒤에 붙인다. 안나오는경우는 원본유지한다.
	public void testKoreanAnalyzer() throws Exception {
		
		//String input = "오늘진짜예뻐";
		//String input = "학생이";
		//String input = "사랑스러웠어"; 제외
		String input = "오늘진짜예뻐";
		SpKoreanAnalyzer a = new SpKoreanAnalyzer();
		//a.setQueryMode(false);
		
		StringBuilder actual = new StringBuilder();
		
		TokenStream ts = a.tokenStream(null, input);
	    CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
	    ts.reset();
	    while (ts.incrementToken()) {
	      actual.append(termAtt.toString());
	      actual.append(' ');
	    }
	    System.out.println(actual);

	    ts.end();
	    ts.close();
	}
	public Vector<String> wordSegment2(String source) {

		Vector<String> inputs = new Vector<String>();
		try {
		SpWordSegmentAnalyzer ws = new SpWordSegmentAnalyzer();
		StringBuilder result = new StringBuilder();
		//String s = source.replace(" ","");
		String s = source;
		//System.out.println("s=="+s);
		List<String> outList = ws.analyzeNew(s); //이부분이 분석이다
		//System.out.println(outList.toString());
		/*for(List<AnalysisOutput> o: outList) {
			for(AnalysisOutput analysisOutput : o) {
				//result.append(analysisOutput.getSource()).append(" ");
				inputs.add(analysisOutput.getSource());
			}
		}*/
		for(String str:outList){
			inputs.add(str);
		}
		//System.out.println(result.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return inputs;
	}
	public Vector<String> wordToken(String source) {
		
		Vector<String> inputs = new Vector<String>();;
		try {
			StringBuilder result = new StringBuilder();
			StringTokenizer stok = new StringTokenizer(source," ");
			while(stok.hasMoreTokens()) {
				String token = stok.nextToken();
				inputs.add(token);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return inputs;
	}
	/*public List<AnalysisOutput> analyze(Vector<String> vec) {
		List<AnalysisOutput> totlist = new ArrayList<AnalysisOutput>();
		try {
				MorphAnalyzer analyzer = new MorphAnalyzer();
				 
				long start = 0;
				for(String input:vec) 
				{
					List<AnalysisOutput> list = analyzer.analyze(input);
					
					for(AnalysisOutput o:list) 
					{
						//System.out.print(o.toString()+" "+o.getPatn());
						totlist.add(o);
					}
					if(start==0) start = System.currentTimeMillis();
				}			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return totlist;
	}*/
	public List<AnalysisOutput> analyze(Vector<String> vec) {
		List<AnalysisOutput> totlist = new ArrayList<AnalysisOutput>();
		try {
				MorphAnalyzer analyzer = new MorphAnalyzer();
				 
				long start = 0;
				for(String input:vec) 
				{
					List<AnalysisOutput> list = analyzer.analyze(input);
					
					for(AnalysisOutput o:list) 
					{
						System.out.print(o.toString()+" "+o.getPatn());
						totlist.add(o);
					}
					if(start==0) start = System.currentTimeMillis();
				}			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return totlist;
	}
	
	public Vector<String> clearDuple(Vector<String> vec) {
		Vector<String> newVec = new Vector<String>();
		try {
		//System.out.println("test"+vec.toString());
		
		for(int i=0;i<vec.size()-1;i++) {
			if(!vec.get(i).equals(vec.get(i+1)))
			newVec.add(vec.get(i));
		}
		if(newVec.size()>0) {
			newVec.add(vec.get(vec.size()-1));
		}else {
			newVec = vec;
		}
		//첫번째와 두번째이후모든합이 같으면 첫번째 제거
		if(newVec.size()>1) {
			String fir = newVec.elementAt(0);
			String sum ="";
			for(int i=1;i<newVec.size();i++){
				sum+=newVec.elementAt(i);
			}
			if(fir.equals(sum)){
				newVec.remove(0);
			}
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return newVec;
	}
	
	//세그먼트
	//토크나이저
	//사용자
	
	public static void main(String[] args) {
		SpMorphAnalyzer any= new SpMorphAnalyzer();
		
		//any.basicTran("오늘진짜예뻐","1");
		//any.basicTran("어제치킨","1");//안됨
		//any.basicTran("사람별로","1");//안됨
		//any.basicTran("아까다","1");//안됨
		//any.basicTran("아니었음","1");
		//any.basicTran("정말사랑스러움","1");//도망감
		//any.basicTran("전혀사랑스럽지","1"); //해가 잘림
		
		//any.basicTran("연예인 봤어","2");
		String searchKeyword ="너 오늘 진짜 예뻐";
		//String searchKeyword ="금요일에 생일이었다";//ha,hy
		//String searchKeyword ="잘 지내고 있나";//ha,hy
		//String searchKeyword ="우리 언니 어렸을 때 예뻤음"; //3개
		//String searchKeyword ="오늘 좀 예쁨";
		//String searchKeyword ="연예인 봤나?"; //3개
		//String searchKeyword ="잘 지내고 있다";//3개
		//String searchKeyword ="금요일에 생일이었나?";//2개
		//String searchKeyword ="그 사람 별로 예쁘지 않음";
		//String searchKeyword ="어제 치킨 먹었어";
		//String searchKeyword ="치킨이 너무 먹고 싶네";
		//String searchKeyword ="그 사람 별로 예쁘지 않음";
		//String searchKeyword ="연예인 봤다";
		boolean inGative=false;
		String upKeyword = searchKeyword;
		if(searchKeyword.indexOf("?")!=-1 && searchKeyword.indexOf("?")==searchKeyword.length()-1){ //가장마지막에 ?표 표시확인
			inGative = true;
			upKeyword= searchKeyword.substring(0,searchKeyword.length()-1);
		}else{
			inGative=false;
		}
		
		any.basicTran(upKeyword,"2",inGative);
		//any.basicTran("금요일에 생일이었어","2");
		//any.basicTran("그사람별로예쁘지않음","1");
		
		/*try {
			any.testKoreanAnalyzer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	public HashMap<String, String> basicTran(String source,String type) {
		return basicTran( source, type,false);
	}
	
	public HashMap<String, String> basicTran(String source,String type,boolean inGative) {
		HashMap<String, String> reMap = new HashMap<String, String>();
		SpEmiMorphAnalyzer analyzer = new SpEmiMorphAnalyzer();
		String restr="";
		String inGativeMark="";
		if(inGative) inGativeMark="?";
		/*SpMorphAnalyzer any= new SpMorphAnalyzer();
		try {
			any.morphAnalyzer("새를 잡다");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
				
		try {
			//어미가져오는부분 별도 구축한다.
			 
			restr = basicAnaly(source,type);
			System.out.println("map"+map.toString());
			Vector<String> vec = selectToken(source,type);
			Vector<String> newvec = clearDuple(vec);
						
			//이부분에서 추가 수정해야한다.
			//restr+=any.emiTran(map);
			String emi = emiTran(map);
			System.out.println("emi"+emi);
			String bRestr = "";
			if(emi.equals("")) {
				bRestr = restr+vec.get(vec.size()-1)+inGativeMark;
			}else {
				bRestr = restr+emi+inGativeMark;
			}
			reMap.put("basic", bRestr);
			///end 기본형
			///시제 str
			String basicType="";
			if(!map.get("anynum").equals("11")&& !map.get("anynum").equals("12")&& !map.get("anynum").equals("3")&& !map.get("anynum").equals("4")){//일단, 용언+어미 형태가 아니면 basic을 그대로 사용한다.
				reMap.put("pBasic", bRestr+inGativeMark);
				basicType=bRestr;
			}else{
				String pemi = passEmiTran(map,newvec.get(newvec.size()-1)); //시제 기본형 어미
				reMap.put("pBasic", restr+pemi+inGativeMark);
				basicType=restr+pemi;
			}
			System.out.println("basicType"+basicType);
			///
			///여기서 다시 기본형부터 시작해라 
			///
			restr = basicPassAnaly(basicType,type);
			SpTransAnalyzer spt = new SpTransAnalyzer();
			//명사형 종결 음붙이기
			String nemi = spt.passUmEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.NOUN_EMI,inGative); //명사형종결 어미
			reMap.put("nBasic", restr+nemi+inGativeMark);
			
			//해체
			String hche = spt.passHaeEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.HAE_CHE,inGative); //해체
			reMap.put("hBasic", restr+hche+inGativeMark);
			
			//해요체
			String hyoc = spt.passHaeYoEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.HAE_YO,inGative); //해요
			reMap.put("hyBasic", restr+hyoc+inGativeMark);
			
			//합쇼체
			
			String hapsyo =spt.passHapSyoEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.HAP_SY,inGative); //합쇼체
			reMap.put("habBasic", restr+hapsyo+inGativeMark);
			
			
			System.out.println("==>"+reMap.toString());
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		//마지막이 3이면 기본형 추출한다.
		return reMap;
	}
	
	public String basicTranOne(String source,String type,String che, boolean inGative) {
		HashMap<String, String> reMap = new HashMap<String, String>();
		SpEmiMorphAnalyzer analyzer = new SpEmiMorphAnalyzer();
		String tranStr="";
		String restr="";
		String inGativeMark="";
		if(inGative) inGativeMark="?";
		
		try {
			//어미가져오는부분 별도 구축한다.
			 
			restr = basicAnaly(source,type);//2: 사용자
			
			Vector<String> vec = selectToken(source,type);
			Vector<String> newvec = clearDuple(vec);
			
			String emi = emiTran(map);
			
			String bRestr = "";
			if(emi.equals("")) {
				bRestr = restr+vec.get(vec.size()-1)+inGativeMark;
			}else {
				bRestr = restr+emi+inGativeMark;
			}
			reMap.put("basic", bRestr);
			///end 기본형
			///시제 str
			String basicType="";
			if(!map.get("anynum").equals("11")&& !map.get("anynum").equals("12")&& !map.get("anynum").equals("3")&& !map.get("anynum").equals("4")){//일단, 용언+어미 형태가 아니면 basic을 그대로 사용한다.
				reMap.put("pBasic", bRestr+inGativeMark);
				basicType=bRestr;
			}else{
				String pemi = passEmiTran(map,newvec.get(newvec.size()-1)); //시제 기본형 어미
				reMap.put("pBasic", restr+pemi+inGativeMark);
				basicType=restr+pemi;
			}
			if(che.equals("기본형")){
				tranStr = reMap.get("basic");
			}else if(che.equals("시제기본")){
				tranStr = reMap.get("pBasic");
			}else{
				///
				///여기서 다시 기본형부터 시작해라 
				///
				restr = basicPassAnaly(basicType,type);
				SpTransAnalyzer spt = new SpTransAnalyzer();
				if(che.equals("명사종결")){
					//명사형 종결 음붙이기
					String nemi = spt.passUmEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.NOUN_EMI,inGative); //명사형종결 어미
					reMap.put("nBasic", restr+nemi+inGativeMark);
					tranStr = reMap.get("nBasic");
				}else if(che.equals("해체")){
				//해체
					String hche = spt.passHaeEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.HAE_CHE,inGative); //해체
					reMap.put("hBasic", restr+hche+inGativeMark);
					tranStr = reMap.get("hBasic");
				}else if(che.equals("해요체")){
					//해요체
					String hyoc = spt.passHaeYoEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.HAE_YO,inGative); //해요
					reMap.put("hyBasic", restr+hyoc+inGativeMark);
					tranStr = reMap.get("hyBasic");
				}else if(che.equals("합쇼체")){
				//합쇼체
					String hapsyo =spt.passHapSyoEmiTran(pmap,newvec.get(newvec.size()-1),SpConstants.HAP_SY,inGative); //합쇼체
					reMap.put("habBasic", restr+hapsyo+inGativeMark);
					tranStr = reMap.get("habBasic");
				}
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		//마지막이 3이면 기본형 추출한다.
		//return tranStr+"|"+map.get("anycont");
		if(tranStr==null)tranStr="-";
		//System.out.println("anycont"+map.get("anycont"));
		//return tranStr;
		String emiType=map.get("anycont");
		System.out.println("리턴값"+tranStr+"|"+emiType);
		return tranStr+"|"+emiType;
	}
	
	public String basicAnaly(String source,String type) throws MorphException{
		String restr="";
		Vector<String> vec = selectToken(source, type);
		//Vector<String> vec = any.wordSegment(source);
		Vector<String> newvec = clearDuple(vec);
		System.out.println("newvec"+newvec.toString());
		List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
		for(String str:newvec){
			List<AnalysisOutput> totlist = analyzeNew(str);
			for(AnalysisOutput o:totlist) 
			{
				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("anycont", o.toString());
				hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
				hmap.put("anynum", String.valueOf(o.getPatn()));
				//System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn()));
				analyList.add(hmap);
			}
		}
		///기본형 구축
		for(int i=0;i<newvec.size()-1;i++) {
			System.out.print(newvec.get(i)+" ");
			restr+=newvec.get(i)+" ";
		}
	
		//map = analyList.get(analyList.size()-1);//무조건 마지막을 이용하니 문제다
		map = getChoiceV(analyList);
		return restr;
		
	}
	//기본형을 다시 형태소분석한다.
	public String basicPassAnaly(String source,String type) throws MorphException{
		String restr="";
		Vector<String> vec = selectToken(source, type);
		//Vector<String> vec = any.wordSegment(source);
		Vector<String> newvec = clearDuple(vec);
		System.out.println("newvec"+newvec.toString());
		List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
		for(String str:newvec){
			List<AnalysisOutput> totlist = analyzeNew(str);
			for(AnalysisOutput o:totlist) 
			{
				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("anycont", o.toString());
				hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
				hmap.put("anynum", String.valueOf(o.getPatn()));
				//System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn()));
				analyList.add(hmap);
			}
		}
		///기본형 구축
		for(int i=0;i<newvec.size()-1;i++) {
			System.out.print(newvec.get(i)+" ");
			restr+=newvec.get(i)+" ";
		}
		System.out.println("analyList"+analyList.toString());
		//pmap = analyList.get(analyList.size()-1);//무조건 마지막을 이용하니 문제다
		pmap = getChoiceV(analyList);
		return restr;
		
	}
	public HashMap<String, String> getChoiceV(List<HashMap<String, String>> analyList){
		HashMap<String, String> map=new HashMap<String, String>();
		if(analyList.size()>1){
			HashMap<String, String> map1 = analyList.get(analyList.size()-2);
			HashMap<String, String> map2 = analyList.get(analyList.size()-1);
			if(map1.get("anynum").equals("11")&&map2.get("anynum").equals("3")){
				map =analyList.get(analyList.size()-2);
			}else{
				map =analyList.get(analyList.size()-1);
			}
		}else{
			map =analyList.get(0); 
		}
		return map;
		
	}
	
	public List<AnalysisOutput> analyzeNew(String inputText) throws MorphException {
        //아랫부분은 뒤에서 처리한다
    	int[] nounPos = new int[inputText.length()];
        for(int i=0;i<inputText.length();i++) nounPos[i]=-1; // initialization

        int[] oneJosa = new int[inputText.length()];
       
        List<String> segList = new ArrayList<String>();
        segList.add(inputText);

        int offset = 0;
        List<String> result2 = new ArrayList<String>();
        List<AnalysisOutput> result = new ArrayList<AnalysisOutput>();
        for(int i=0;i<segList.size();i++) {
        	result  = new ArrayList<AnalysisOutput>();
        	int length = segList.get(i).length();
        	//if(length==1) return Collections.EMPTY_LIST; //하나인경우는 empty 리턴하고 있음 03.11 원본에 주석처리
        	        	
        	boolean containOneJosa = isContainOneJosa(offset, length,oneJosa);
        	//System.out.println("containOneJosa"+containOneJosa);
            analyzeLast(segList.get(i), result, containOneJosa);
            offset += length;
           
            
        }
        
        return result;
    }
		
	 private boolean isContainOneJosa(int offset, int length, int[] oneJosa) {
	    	for(int i=offset;i<length;i++) {
	    		if(oneJosa[i]==1) return true;
	    	}
	    	return false;
	 }
	 
	 public void analyzeLast(String inputText, List<AnalysisOutput> result, boolean containOneJosa) throws MorphException {

		    List<WordListCandidate> candiateList = new ArrayList<WordListCandidate>();

		    List<AnalysisOutput> aoList = morphAnal.analyze(inputText);
		    

		      int length = inputText.length();
		    // add last character as the first candidate
		    WordListCandidate listCandidate = new WordListCandidate(
		        morphAnal.analyze(inputText.substring(length-1,length)));
		    
		    candiateList.add(listCandidate);
		    //System.out.println("candiateList"+candiateList.size());
		    Map<String,List<AnalysisOutput>> analyzedSet = new HashMap<String,List<AnalysisOutput>>();
		    
		    // from last position, check whether if each position can be a dividing point.
		    for(int start=inputText.length()-2;start >=0 ; start--) {
		      
		      String thisChar = Character.toString(inputText.charAt(start));
		      List<WordListCandidate> newCandidates = null;
		      
		        // newly created candidates
		        newCandidates = new ArrayList<WordListCandidate>();
		        
		        for(WordListCandidate candidate : candiateList) {
		          
		          String fragment = thisChar + candidate.getFirstFragment();
		          // build the position key with the start position and the end position
		          String posKey = new StringBuffer()
		              .append(start)
		              .append(",")
		              .append(start+candidate.getFirstFragment().length())
		              .toString();
		                    
		          WordListCandidate newCandidate = candidate.newCopy();
		          List<AnalysisOutput> outputs = analyzedSet.get(posKey);

		          // check whether if already analyzed.
		          if(outputs == null) {
		            outputs = morphAnal.analyze(fragment);
		            newCandidate.replaceFirst(outputs);
		            analyzedSet.put(posKey, outputs);
		          } else {
		            newCandidate.replaceFirst(outputs);
		          }
		          
		          newCandidates.add(newCandidate);
		        }
		      //System.out.println("thisChar===>"+thisChar);
		      List<AnalysisOutput> outputs = morphAnal.analyze(thisChar);
		      
		      String posKey = new StringBuffer()
		          .append(start)
		          .append(",")
		          .append(start+1)
		          .toString();

		      analyzedSet.put(posKey, outputs);
		      /*if(thisChar.equals("카")) {
		    	  System.out.println("posKey===>"+posKey+analyzedSet.toString());
		      }*/
		      for(WordListCandidate candidate : candiateList) {
		        candidate.addWord(outputs);
		      }      
		      
		      if(newCandidates!=null) candiateList.addAll(newCandidates);
		      //SpUtilities.outLine("candiateList.size()==>",candiateList.size());
		      
		      if(candiateList.size()>=maxCandidate) {
		    	  Collections.sort(candiateList, new SpWordListComparator());
		          removeLast(candiateList,adjustNoOfCandidate);
		      }
		      
		      //System.out.println();
		    }
		    
		    Collections.sort(candiateList, new SpWordListComparator());
		   /* for(WordListCandidate candidate : candiateList) { //전체보여주기
		    	System.out.println("candidate sort after==>"+candidate.getWordList().toString()+candidate.getCorrectLength()+" "+candidate.getWordList().size()+"ptn "+candidate.getPtnCount()+" "+candidate.getVerbCount()+" "+candidate.getFirstFragment().length());
		    }*/
		    
		    for(WordListCandidate candidate : candiateList) {
		      //System.out.println("candidate==>"+candidate.getWordList().toString());
		     /* if(candiateList.indexOf(candidate)!=candiateList.size()-1 && 
		    	   hasConsecutiveOneWord(candidate))
		        continue;  //뭔지 모르지만 일단 주석처리 해보자
		      */
		      for(List<AnalysisOutput> outputs : candidate.getWordList()) {
		        for(int i=0;i<outputs.size();i++){
		        	result.add(outputs.get(i));
		        }
		      }
		      break;
		    }
		   
		  }
	 
	 private void removeLast(List<WordListCandidate> list, int start) {
		  	List<WordListCandidate> removed = new ArrayList<WordListCandidate>();
		  	for(int i=start;i<list.size();i++) {
		  		removed.add(list.get(i));
		  		//SpUtilities.outLine("remove",list.get(i).getWordList().toString());
		  	}
		  	
		  	for(Object o : removed) {
		  		list.remove(o);
		  	}
		  	
		  	removed=null;
		  }
	//기본형어미 바꾼다.
	public String emiTran(HashMap<String, String> map) {
		String anw="";
		String str=map.get("anycont");
		String anynum = map.get("anynum");
	    System.out.println("anytype"+map.get("anytype")+"anynum"+anynum);
	    if(anynum.equals("2")) { //Nj   체언+조사인 경우에는 다시한번 나눠볼까???
			List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
			
			StringTokenizer stk = new StringTokenizer(str,",");
			int cnt = stk.countTokens();
			for(int i=0;i<cnt;i++) {
				HashMap<String, String> hmap = new HashMap<String, String>();
				String strToken = stk.nextToken();
				String sourceChar = strToken.substring(0,strToken.indexOf("("));
				System.out.println("sourceChar===>"+sourceChar);
				
				String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
				System.out.println("sourceChar"+sourceChar+"stype"+stype);
				hmap.put("sourceChar", sourceChar);
				hmap.put("stype", stype);
				analyList.add(hmap);
			}
			System.out.println("emiTran"+analyList.toString());
			for(int i=0;i<analyList.size();i++) {
				HashMap<String, String> hmap = analyList.get(i);
				anw = anw+hmap.get("sourceChar");
				if(hmap.get("stype").equals("N")) {
					anw = anw+"다";
					break;
				}
			}
		}else if(anynum.equals("3")) { //Nte
			List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
			
			StringTokenizer stk = new StringTokenizer(str,",");
			int cnt = stk.countTokens();
			for(int i=0;i<cnt;i++) {
				HashMap<String, String> hmap = new HashMap<String, String>();
				String strToken = stk.nextToken();
				String sourceChar = strToken.substring(0,strToken.indexOf("("));
				String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
				System.out.println("sourceChar"+sourceChar+"stype"+stype);
				hmap.put("sourceChar", sourceChar);
				hmap.put("stype", stype);
				analyList.add(hmap);
			}
			System.out.println("emiTran"+analyList.toString());
			for(int i=0;i<analyList.size();i++) {
				HashMap<String, String> hmap = analyList.get(i);
				anw = anw+hmap.get("sourceChar");
				if(hmap.get("stype").equals("t")) {
					anw = anw+"다";
					break;
				}
			}
		}else if(anynum.equals("4")) { //Ntnj
			List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
			
			StringTokenizer stk = new StringTokenizer(str,",");
			int cnt = stk.countTokens();
			for(int i=0;i<cnt;i++) {
				HashMap<String, String> hmap = new HashMap<String, String>();
				String strToken = stk.nextToken();
				String sourceChar = strToken.substring(0,strToken.indexOf("("));
				String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
				System.out.println("sourceChar"+sourceChar+"stype"+stype);
				hmap.put("sourceChar", sourceChar);
				hmap.put("stype", stype);
				analyList.add(hmap);
			}
			System.out.println("emiTran"+analyList.toString());
			for(int i=0;i<analyList.size();i++) {
				HashMap<String, String> hmap = analyList.get(i);
				anw = anw+hmap.get("sourceChar");
				if(hmap.get("stype").equals("t")) {
					anw = anw+"다";
					break;
				}
			}
		}else if(anynum.equals("11")||anynum.equals("12")) {
			List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
			
			StringTokenizer stk = new StringTokenizer(str,",");
			int cnt = stk.countTokens();
			for(int i=0;i<cnt;i++) {
				HashMap<String, String> hmap = new HashMap<String, String>();
				String strToken = stk.nextToken();
				String sourceChar = strToken.substring(0,strToken.indexOf("("));
				String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
				System.out.println("sourceChar"+sourceChar+"stype"+stype);
				hmap.put("sourceChar", sourceChar);
				hmap.put("stype", stype);
				analyList.add(hmap);
			}
			
			for(int i=0;i<analyList.size();i++) {
				HashMap<String, String> hmap = analyList.get(i);
				anw = anw+hmap.get("sourceChar");
				if(hmap.get("stype").equals("V")) {
					anw = anw+"다";
					break;
				}
			}
		}
		return anw;
	}
	//과거형은 살린다. anynum이 11이고 V,f,e일 경우 e제거후 다를 붙인다.
	public String passEmiTran(HashMap<String, String> map,String pemi) {
		String anw="";
		anw = passEmiTran(map,pemi,"다");
		return anw;
	}
	//과거형은 살린다. anynum이 11이고 V,f,e일 경우 e제거후 다를 붙인다.
		public String passEmiTran(HashMap<String, String> map,String pemi,String type) {
			return passEmiTran(map,pemi,type,false);
		}
		public String passEmiTran(HashMap<String, String> map,String pemi,String type,boolean inGative) {
			String anw="";
			String str=map.get("anycont");
			String anynum = map.get("anynum");
		    System.out.println("passEmiTran anytype"+map.get("anytype"));
			if(anynum.equals("11")||anynum.equals("12")||anynum.equals("3")||anynum.equals("4")) {
				List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
				
				StringTokenizer stk = new StringTokenizer(str,",");
				int cnt = stk.countTokens();
				for(int i=0;i<cnt;i++) {
					HashMap<String, String> hmap = new HashMap<String, String>();
					String strToken = stk.nextToken();
					String sourceChar = strToken.substring(0,strToken.indexOf("("));
					String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
					System.out.println("sourceChar"+sourceChar+"stype"+stype);
					hmap.put("sourceChar", sourceChar);
					hmap.put("stype", stype);
					analyList.add(hmap);
				}
				
				/*if(chkVe(analyList)){
					String eChk = analyList.get(1).get("sourceChar");
					System.out.println("pemi"+pemi+"eChk"+eChk);
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}else*/ if(chkVfe(analyList)){
					//pemi에서 e를 떼어버리고 다를 붙인다.
					String eChk = analyList.get(2).get("sourceChar");
					System.out.println(eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}else if(chkVnj(analyList)){
					//pemi에서 nj를 떼어버리고 다를 붙인다.
					try{
					//n이 자음이면 기본형과 같다. 아니면 nj를 떼어버리고 다를 붙인다.
					String eChk = analyList.get(1).get("sourceChar")+analyList.get(2).get("sourceChar");
					System.out.println("pemi"+pemi+"Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));//무조건 떼버리면 오류 발생한다.
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(chkNtfe(analyList)){
					//pemi에서 e를 떼어버리고 다를 붙인다.
					String eChk = analyList.get(3).get("sourceChar");
					System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}else if(chkNtfnj(analyList)){
					//pemi에서 e를 떼어버리고 다를 붙인다.
					String eChk = analyList.get(3).get("sourceChar")+analyList.get(4).get("sourceChar");
					System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}else{ //기존 기본형과 같다.
					for(int i=0;i<analyList.size();i++) {
						HashMap<String, String> hmap = analyList.get(i);
						anw = anw+hmap.get("sourceChar");
						if(hmap.get("stype").equals("V")) {
							anw = anw+type;
							break;
						}else if(hmap.get("stype").equals("t")) {
							anw = anw+type;
							break;
						}
					}
				}
				
			}
			return anw;
		}
	
	//형태소가 2개이면서 Ve인지를 확인한다.
	public boolean chkVe(List<HashMap<String, String>> analyList){
		boolean chk = true;
		String[] ve = {"V","e"};
		if(analyList.size()==2&& ve.length==2){
			for(int i=0;i<ve.length;i++){
				if(!analyList.get(i).get("stype").equals(ve[i])){
					chk=false;
				}
			}
		}else{
			chk=false;
		}
		return chk;
	}
	//형태소가 3개이면서 Vfe인지를 확인한다.
	public boolean chkVfe(List<HashMap<String, String>> analyList){
		boolean chk = true;
		String[] vfe = {"V","f","e"};
		if(analyList.size()==3&& vfe.length==3){
			for(int i=0;i<vfe.length;i++){
				if(!analyList.get(i).get("stype").equals(vfe[i])){
					chk=false;
				}
			}
		}else{
			chk=false;
		}
		return chk;
	}
	//형태소가 3개이면서 Vnj인지를 확인한다.
	public boolean chkVnj(List<HashMap<String, String>> analyList){
		boolean chk = true;
		String[] vfe = {"V","n","j"};
		if(analyList.size()==3&& vfe.length==3){
			for(int i=0;i<vfe.length;i++){
				if(!analyList.get(i).get("stype").equals(vfe[i])){
					chk=false;
				}
				if(chkJaType(analyList.get(i).get("sourceChar"))){
					chk=false;
				}
			}
		}else{
			chk=false;
		}
		return chk;
	}
	//형태소가 3개이면서 Ntfe인지를 확인한다.
	public boolean chkNtfe(List<HashMap<String, String>> analyList){
		boolean chk = true;
		String[] vfe = {"N","t","f","e"};
		if(analyList.size()==4&& vfe.length==4){
			for(int i=0;i<vfe.length;i++){
				if(!analyList.get(i).get("stype").equals(vfe[i])){
					chk=false;
				}
			}
		}else{
			chk=false;
		}
		return chk;
	}
	//형태소가 5개이면서 Ntfnj인지를 확인한다.
	public boolean chkNtfnj(List<HashMap<String, String>> analyList){
		boolean chk = true;
		String[] vfe = {"N","t","f","n","j"};
		if(analyList.size()==5&& vfe.length==5){
			for(int i=0;i<vfe.length;i++){
				if(!analyList.get(i).get("stype").equals(vfe[i])){
					chk=false;
				}
			}
		}else{
			chk=false;
		}
		
		return chk;
	}
	//자음이 섞여 있는지 체크
	public boolean chkJaType(String str){
		boolean chk = false;
		String[] vfe = {"ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
		
		for(int i=0;i<vfe.length;i++){
			if(str.equals(vfe[i])){
				chk=true;
			}
		}	
		return chk;
	}
	
	

}

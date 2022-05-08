package org.apache.lucene.analysis.ko.sp;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanFilter;
import org.apache.lucene.analysis.ko.KoreanTokenizer;
import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.MorphAnalyzer;
import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.morph.PatternConstants;
import org.apache.lucene.analysis.ko.morph.SpWordSegmentAnalyzer;
import org.apache.lucene.analysis.ko.morph.WordSegmentAnalyzer;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.apache.lucene.analysis.ko.utils.Utilities;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static final String possibleWordStartJosa = "의은가나며아야에엔여와요이";
	private static Logger logger = LoggerFactory.getLogger(Test.class);
	
	//단어 문법에 맞게 나눠주기
	public Vector<String> wordSegment(String source) {
		
		Vector<String> inputs = new Vector<String>();;
		try {
		WordSegmentAnalyzer ws = new WordSegmentAnalyzer();
		StringBuilder result = new StringBuilder();
		String s = source.replace(" ","");
		List<List<AnalysisOutput>> outList = ws.analyze(s); //이부분이 분석이다
		for(List<AnalysisOutput> o: outList) {
			for(AnalysisOutput analysisOutput : o) {
				result.append(analysisOutput.getSource()).append(" ");
				inputs.add(analysisOutput.getSource());
			}
		}
		//System.out.println(result.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return inputs;
	}
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
		for(int i=0;i<vec.size()-1;i++) {
			if(!vec.get(i).equals(vec.get(i+1)))
			newVec.add(vec.get(i));
		}
		newVec.add(vec.get(vec.size()-1));
		return newVec;
	}
	
	public List<List<AnalysisOutput>> analyze(String inputText) throws MorphException {
        int[] nounPos = new int[inputText.length()];
        for(int i=0;i<inputText.length();i++) nounPos[i]=-1; // initialization

        StringBuffer sb = new StringBuffer();
        for(int i=0;i<inputText.length();i++) {
            int lastIndex = findLongestNoun(i, inputText);//단어를 찾는다
            //System.out.println("lastIndex"+lastIndex);
            if(lastIndex!=-1 && nounPos[lastIndex]==-1) {
                nounPos[lastIndex] = i; // store the start position of a noun at the iastIndex position to which the noun spans.
            }
        }
       /* for(int i=0;i<nounPos.length;i++) {
        	System.out.println("nounPos"+nounPos[i]);
        }*/
        int[] oneJosa = new int[inputText.length()];
        //1.System.out.println("inputText"+inputText+"nounPos"+nounPos+"oneJosa"+oneJosa);
        List<String> segList = splitByNoun(inputText);
        System.out.println("segList"+segList.toString());
        List<List<AnalysisOutput>> result = new ArrayList<List<AnalysisOutput>>();
        //2.System.out.println("inputText"+inputText);
        //3.System.out.println("segList"+segList.toString());
        
        //System.out.println("result"+result.toString());
        return result;
    }
	/**
     * find the longest noun with more than 2 length.
     * @param start
     * @param inputText
     * @return
     */
    public int findLongestNoun(int start, String inputText) throws MorphException {
        StringBuffer sb = new StringBuffer();
        sb.append(inputText.charAt(start));
        System.out.println("sb"+sb.toString());
        int lastIndex = -1;
        for(int i=start+1;i<inputText.length();i++) {
            sb.append(inputText.charAt(i));
            System.out.println("findLongestNoun"+sb.toString());
            if(!DictionaryUtil.findWithPrefix(sb.toString()).hasNext()) {
                return lastIndex;
            }
            if(DictionaryUtil.getAllNoun(sb.toString())!=null) {
                lastIndex = i;
            }
        }
        System.out.println("lastIndex"+lastIndex);
        return lastIndex;
    }

    public Vector<String> splitByNoun(String inputText) throws MorphException {
        List<Integer> positions = new LinkedList<Integer>();

        int[] nounPos = new int[inputText.length()];
        for(int i=0;i<inputText.length();i++) nounPos[i]=-1; // initialization

        StringBuffer sb = new StringBuffer();
        for(int i=0;i<inputText.length();i++) {
            int lastIndex = findLongestNoun(i, inputText);//단어를 찾는다
            //System.out.println("lastIndex"+lastIndex);
            if(lastIndex!=-1 && nounPos[lastIndex]==-1) {
                nounPos[lastIndex] = i; // store the start position of a noun at the iastIndex position to which the noun spans.
            }
        }
       /* for(int i=0;i<nounPos.length;i++) {
        	System.out.println("nounPos"+nounPos[i]);
        }*/
        int[] oneJosa = new int[inputText.length()];
        for(int i=1;i<nounPos.length;i++) {
        	System.out.println(i+"i==="+positions.toString()+"nounPos"+nounPos[i]);
        	if(nounPos[i]!=-1) continue;
            int endWithMe = -1;
            for(int j=i+1;j<nounPos.length;j++) {
                if(nounPos[j]==i) {endWithMe=j;break;}
                if(nounPos[j]>i) {break;}
            }
            if(endWithMe==-1) continue; // there is no word starting at i position

            if(possibleWordStartJosa.indexOf(inputText.charAt(i))!=-1)
            	oneJosa[i] = 1;
            
            boolean possibleWord = true;
            for(int j=endWithMe+1;j<nounPos.length;j++) {
            	System.out.println("j"+j);
                if(nounPos[j]!=-1&&(nounPos[j]<endWithMe ||
                        (nounPos[j]==endWithMe && (endWithMe-i)<=(j-nounPos[j]))) // [명사+명사]인 경우, 예)도서관어린이사서의
                        ) 
                {
                	possibleWord = false;
                }
                if(!possibleWord || nounPos[j]>endWithMe) {break;}
            }
            if(possibleWord) {
                positions.add(i);
            }
        }

        Vector<String> fragments = new Vector<String>();
        int start = 0;
        for(int i=0;i<positions.size();i++) {
        	System.out.println("start"+start+" "+positions.get(i));
            fragments.add(inputText.substring(start,positions.get(i)));
            start = positions.get(i);
        }
        fragments.add(inputText.substring(start));
        System.out.println("fragments"+fragments.toString());
        return fragments;
    }
    public static  void main(String args[]){
    	Test t = new Test();
    	SpMorphAnalyzer any= new SpMorphAnalyzer();
    	List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
    	try {
    		List<AnalysisOutput> totlist = any.analyzeNew("예뻐요");
			for(AnalysisOutput o:totlist) 
			{
				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("anycont", o.toString());
				hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
				hmap.put("anynum", String.valueOf(o.getPatn()));
				System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn()));
				analyList.add(hmap);
			}
			
			HashMap<String,String> reMap = any.basicTran("예뻐요","2",false);//기본형,명사형종결,해체
		} catch (MorphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main2(String args[]){
    	Test t = new Test();
    	String inputText ="생각보다";
    	SpWordSegmentAnalyzer an = new SpWordSegmentAnalyzer();
    	
        try{
        	List<String> list = an.splitDetail(inputText);
        	System.out.println(list.toString());
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}

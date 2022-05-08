package org.apache.lucene.analysis.ko.sp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.PatternConstants;

public class SelectKeyword {
	
	public Vector<String> getNoun(String input){
		Vector<String> vec=new Vector<String>();
		SpMorphAnalyzer any= new SpMorphAnalyzer();
		List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
		try {
			Vector<String> vec_f = any.selectToken(input,"2");
			Vector<String> newVec = any.clearDuple(vec_f);
			//System.out.println(newVec);
			for(String str:newVec){
				List<AnalysisOutput> totlist = any.analyzeNew(str);
				for(AnalysisOutput o:totlist) 
				{
					//System.out.println(o.getSource());
					HashMap<String, String> hmap = new HashMap<String, String>();
					hmap.put("anycont", o.toString());
					hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
					hmap.put("anynum", String.valueOf(o.getPatn()));
					//System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn())+String.valueOf(o.getPatn()));
					//analyList.add(hmap);
					
					if(hmap.get("anynum").equals("1")||hmap.get("anynum").equals("2")||hmap.get("anynum").equals("3")) {
						String source = o.toString();
						String chsource = source.substring(0,source.indexOf("(N)"));
						//System.out.println(source.substring(0,source.indexOf("(N)")));
						//if(chsource.length()>1) {
							vec.add(chsource);
						//}
					}
				}
			}
			//System.out.println(analyList.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return vec;
	}
	public Vector<String> getNounVerb(String input){
		Vector<String> vec=new Vector<String>();
		SpMorphAnalyzer any= new SpMorphAnalyzer();
		List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
		try {
			Vector<String> vec_f = any.selectToken(input,"2");
			Vector<String> newVec = any.clearDuple(vec_f);
			//System.out.println(newVec);
			boolean Nvalue=true;
			for(String str:newVec){
				List<AnalysisOutput> totlist = any.analyzeNew(str);
				for(AnalysisOutput o:totlist) 
				{
					//System.out.println(o.getSource());
					HashMap<String, String> hmap = new HashMap<String, String>();
					hmap.put("anycont", o.toString());
					hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
					hmap.put("anynum", String.valueOf(o.getPatn()));
					System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn())+String.valueOf(o.getPatn()));
					//analyList.add(hmap);
					
					if(hmap.get("anynum").equals("1")||hmap.get("anynum").equals("2")||hmap.get("anynum").equals("3")) {
						String source = o.toString();
						String chsource = source.substring(0,source.indexOf("(N)"));
						//System.out.println(source.substring(0,source.indexOf("(N)")));
						//if(chsource.length()>1) {
							vec.add(chsource);
							Nvalue=true;
						//}
					}
					if(Nvalue &&(hmap.get("anynum").equals("11"))) {
						String source = o.toString();
						String chsource = source.substring(0,source.indexOf("(V)"))+"다";
						//System.out.println(source.substring(0,source.indexOf("(N)")));
						vec.add(chsource);
						Nvalue=false;
					}
				}
			}
			//System.out.println(analyList.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return vec;
	}
	public static void main2(String[] args) {
		SelectKeyword word = new SelectKeyword();
		//word.getNoun("하루가 또 가네요.");
		File file = new File("C:\\project\\py_src\\result\\1618539157.3023088_언어.txt"); 
		if(file.exists()) { 
			BufferedReader inFile;
			try {
				inFile = new BufferedReader(new FileReader(file));
				String sLine = null; 
				while( (sLine = inFile.readLine()) != null ) {
					if(sLine!=null && sLine.length()>2)
					//System.out.println(sLine);
					word.getNounVerb(sLine); //읽어들인 문자열을 출력 합니다. }
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}

	}
	public String vecToStr(Vector<String> vec) {
		String reStr="";
		for(int i=0;i<vec.size();i++) {
			reStr+=vec.elementAt(i);
			if(i<vec.size()-1) {
				reStr+=",";
			}
		}
		return reStr;
	}
	public static void main(String[] args) {
		SelectKeyword word = new SelectKeyword();
		//Vector vec = word.getNoun("12시 땡!");
		String str = word.vecToStr(word.getNounVerb("다시 새로 사는 게 마음 편해요."));
		System.out.println(str);

	}

}

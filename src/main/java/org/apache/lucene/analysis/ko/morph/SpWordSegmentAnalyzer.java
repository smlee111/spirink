package org.apache.lucene.analysis.ko.morph;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanFilter;
import org.apache.lucene.analysis.ko.KoreanTokenizer;
import org.apache.lucene.analysis.ko.SpKoreanFilter;
import org.apache.lucene.analysis.ko.SpKoreanTokenizer;
import org.apache.lucene.analysis.ko.sp.SpUtilities;
import org.apache.lucene.analysis.ko.sp.Test;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.*;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class SpWordSegmentAnalyzer {
	private static Logger logger = LoggerFactory.getLogger(SpWordSegmentAnalyzer.class);
    private final SpMorphAnalyzer morphAnal = new SpMorphAnalyzer();

    //private static final int maxCandidate = 64;
    private static final int maxCandidate = 256;

    private static final int adjustNoOfCandidate = 40;
    //private static final int adjustNoOfCandidate = 100;
    
    private static final String possibleWordStartJosa = "의은가나며아야에엔여와요이";

    @SuppressWarnings("unused")
	public List<List<AnalysisOutput>> analyze(String inputText) throws MorphException {
        int[] nounPos = new int[inputText.length()];
        int[] oneJosa = new int[inputText.length()];
        //1.System.out.println("inputText"+inputText+"nounPos"+nounPos+"oneJosa"+oneJosa);
        List<String> segList = new ArrayList<String>();
        segList.add(inputText);
        //System.out.println("segList"+segList.toString());
        List<List<AnalysisOutput>> result = new ArrayList<List<AnalysisOutput>>();
        //2.System.out.println("inputText"+inputText);
        //3.System.out.println("segList"+segList.toString());
        
        int offset = 0;
        for(int i=0;i<segList.size();i++) {
        	//System.out.println("segList"+segList.toString());
        	int length = segList.get(i).length();
        	//if(length==1) return Collections.EMPTY_LIST; //하나인경우는 empty 리턴하고 있음 03.11 원본에 주석처리
        	        	
        	boolean containOneJosa = isContainOneJosa(offset, length,oneJosa);
        	//System.out.println("containOneJosa"+containOneJosa);
            analyze(segList.get(i), result, containOneJosa);
            offset += length;
        }
        //System.out.println("result"+result.toString());
        return result;
    }

    public List<String> analyzeNew(String inputText) throws MorphException {
        //아랫부분은 뒤에서 처리한다
    	int[] nounPos = new int[inputText.length()];
        for(int i=0;i<inputText.length();i++) nounPos[i]=-1; // initialization

               
       /* for(int i=0;i<nounPos.length;i++) {
        	System.out.println("nounPos"+nounPos[i]);
        }*/
        int[] oneJosa = new int[inputText.length()];
        //System.out.println("inputText"+inputText+"nounPos"+nounPos+"oneJosa"+oneJosa);
        //List<String> segList = splitByNoun(inputText, nounPos,oneJosa);
        List<String> segList = new ArrayList<String>();
        segList.add(inputText);
        //System.out.println("splitByNoun segList==>"+segList.toString());
        
        //System.out.println("inputText"+inputText);
        //System.out.println("segList"+segList.toString());
       /* for(int i=0;i<oneJosa.length;i++) {
        	logger.info("oneJosa"+i+" "+oneJosa[i]);
        }*/
        int offset = 0;
        List<String> result2 = new ArrayList<String>();
        List<String> result = new ArrayList<String>();
        for(int i=0;i<segList.size();i++) {
        	result  = new ArrayList<String>();
        	logger.info("segList"+segList.toString());//'이너' 명사 삭제
        	int length = segList.get(i).length();
        	//if(length==1) return Collections.EMPTY_LIST; //하나인경우는 empty 리턴하고 있음 03.11 원본에 주석처리
        	        	
        	boolean containOneJosa = isContainOneJosa(offset, length,oneJosa);
        	//System.out.println("containOneJosa"+containOneJosa);
            analyzeLast(segList.get(i), result, containOneJosa);
            offset += length;
           /* for(List<AnalysisOutput> o: result) {
        		for(AnalysisOutput analysisOutput : o) {
        			//result.append(analysisOutput.getSource()).append(" ");
        			result2.add(analysisOutput.getSource());
        		}
        	}*/
            for(String str:result){
            	System.out.println("str"+str);
            	List<String> resultNew = splitDetail(str);
            	//System.out.println("resultNew"+resultNew.toString());
            	result2.addAll(resultNew);
            }
            
            //명사+조사면 붙인다.
	        for(int k=0;k<result2.size()-1;k++){
	        	for(int j=k+1;j<result2.size();j++){
	        		//System.out.println(i+":"+segList.get(i) +"j:"+segList.get(j));
	        		//System.out.println("붙일까?"+chkNounJosa(segList.get(i),segList.get(j)));
	        		if(chkNounJosa(result2.get(k),result2.get(j))){
	        			///segList.get(i)에 두개를 붙여서넣고 segList.get(j)를 지운다.
	        			//System.out.println(segList.toString());
	        			String sumStr = result2.get(j-1)+result2.get(j);
	        			result2.remove(j);
	        			result2.remove(j-1);
	        			result2.add(j-1,sumStr);
	        			
	        			break;
	        		}
	        	}
	        }
	        //마지막 2개가 동사+어미이면 붙인다.
	        if(result2.size()>1){
	        	if(chkNounVerbEmi(result2.get(result2.size()-2),result2.get(result2.size()-1))){ //명사 or 동사+어미인지 체크
	        		String sumStr = result2.get(result2.size()-2)+result2.get(result2.size()-1);
        			result2.remove(result2.size()-1);
        			result2.remove(result2.size()-1);
        			result2.add(sumStr);
	        	}
	        }
            
        }
        
        return result2;
    }
    
    private boolean isContainOneJosa(int offset, int length, int[] oneJosa) {
    	for(int i=offset;i<length;i++) {
    		if(oneJosa[i]==1) return true;
    	}
    	return false;
    }
    //가장마지막에 다시 명사로 나눈다.
    public List<String> splitDetail(String inputText) throws MorphException{
    	//아랫부분은 뒤에서 처리한다
    	int[] nounPos = new int[inputText.length()];
        for(int i=0;i<inputText.length();i++) nounPos[i]=-1; // initialization

        int[] advPos = new int[inputText.length()];
        for(int i=0;i<inputText.length();i++) advPos[i]=-1; // initialization 부사추가한다
        
        StringBuffer sb = new StringBuffer();
        List<String> segList = new ArrayList<String>();
        //System.out.println("inputText"+inputText);
        if(DictionaryUtil.getBusa(inputText)==null){ //부사이면 자르지 마라
	        for(int i=0;i<inputText.length();i++) {
	            int lastIndex = findLongestNoun(i, inputText);//단어를 찾는다
	            
	            //System.out.println(i+inputText+"lastIndex"+lastIndex);
	            if(lastIndex!=-1 && nounPos[lastIndex]==-1) {
	                nounPos[lastIndex] = i; // store the start position of a noun at the iastIndex position to which the noun spans.
	            }
	           
	            //System.out.println(i+inputText+"nounPos[lastIndex]"+nounPos[i]);
	        }
	        int[] oneJosa = new int[inputText.length()];
	        segList = splitByNoun(inputText, nounPos,oneJosa);
	        if(segList==null||segList.size()<1){
	        	segList.add(inputText);
	        }
	        
	        //여기까지 명사+조사 붙이기
        }else{
        	segList.add(inputText);
        }

        return segList;
    }
    
    //명사+조사 인지 체크한다.
    public boolean chkNounJosa(String str1,String str2){
    	boolean chk=false;
    	boolean noun=false;
    	boolean josa=false;
    	try{
    	 if(DictionaryUtil.getAllNoun(str1)!=null) {
             noun=true;
         }
    	 if(DictionaryUtil.existJosa(str2)) {
    		 josa=true;
         }
    	 //System.out.println(noun+" "+josa);
    	 if(noun&&josa){
    		 chk = true;
    	 }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return chk;
    }
  //명사+조사 인지 체크한다.
    public boolean chkNounVerbEmi(String str1,String str2){
    	boolean chk=false;
    	boolean noun=false;
    	boolean verb=false;
    	boolean emi=false;
    	try{
    	 if(DictionaryUtil.getAllNoun(str1)!=null) {
             noun=true;
         }
    	 if(DictionaryUtil.getVerb(str1)!=null) {
    		 verb=true;
         }
    	 if(DictionaryUtil.existEomi(str2)) {
    		 emi=true;
         }
    	 //System.out.println(noun+" "+josa);
    	 if(noun&&emi){
    		 chk = true;
    	 }
    	 if(verb&&emi){
    		 chk = true;
    	 }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return chk;
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

        int lastIndex = -1;
        for(int i=start+1;i<inputText.length();i++) {
            sb.append(inputText.charAt(i));
            //logger.info("findLongestNoun"+sb.toString());
            //System.out.println("1sb.toString()"+sb.toString());
            if(!DictionaryUtil.findWithPrefix(sb.toString()).hasNext()) {
            	//System.out.println("2sb.toString()"+sb.toString());
                return lastIndex;
            }
            if(DictionaryUtil.getAllNoun(sb.toString())!=null) {
            	//System.out.println("3sb.toString()"+sb.toString());
                lastIndex = i;
            }
            //부사추가
            if(DictionaryUtil.getAdverb(sb.toString())!=null) {
            	//System.out.println("4sb.toString()"+sb.toString());
                lastIndex = i;
            }
                        
            //System.out.println("*****===>"+sb.toString()+"lastIndex"+lastIndex);
        }
        return lastIndex;
    }

   
    /*public List<String> splitByNoun(String inputText, int[] nounPos, int[] oneJosa) throws MorphException {
        List<Integer> positions = new LinkedList<Integer>();

        for(int i=1;i<nounPos.length;i++) {
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

        List<String> fragments = new ArrayList<String>();
        int start = 0;
        for(int i=0;i<positions.size();i++) {
            fragments.add(inputText.substring(start,positions.get(i)));
            start = positions.get(i);
        }
        fragments.add(inputText.substring(start));
        //System.out.println("fragments"+fragments.toString());
        return fragments;
    }*/

    public List<String> splitByNoun(String inputText, int[] nounPos, int[] oneJosa) throws MorphException {
    	List<String> fragments = new ArrayList<String>();
    	List<Integer> positions = new LinkedList<Integer>();
        logger.info("nounPos.length"+nounPos.length);
        for(int i=1;i<nounPos.length;i++) {
        	//System.out.println(i+"번째 "+nounPos[i]);
        	if(nounPos[i]!=-1) {
        		//System.out.println(i+"번째 continue"+nounPos[i]);
        		continue;  //명사면 자르는걸 포기해라
        	}
            int endWithMe = -1;           //끝으로 셋팅
            for(int j=i+1;j<nounPos.length;j++) {
            	//System.out.println("i"+i+"j"+j+"endWithMe"+endWithMe);
                if(nounPos[j]==i) {
                	endWithMe=j;
                	//System.out.println("endWithMe==>"+endWithMe);//진짜자른다
                	break;
                }
                if(nounPos[j]>i) {
                	break;
                }
            }
            if(endWithMe==-1) continue; // there is no word starting at i position

            if(possibleWordStartJosa.indexOf(inputText.charAt(i))!=-1)
            	oneJosa[i] = 1;
            
            boolean possibleWord = true;
            for(int j=endWithMe+1;j<nounPos.length;j++) {
            	//System.out.println("jj"+j);
                if(nounPos[j]!=-1&&(nounPos[j]<endWithMe ||
                        (nounPos[j]==endWithMe && (endWithMe-i)<=(j-nounPos[j]))) // [명사+명사]인 경우, 예)도서관어린이사서의
                        ) 
                {
                	//System.out.println("possibleWord"+possibleWord);
                	possibleWord = false;
                }
                //여기서 조건하나더 추가한다.
                
                //possibleWord = false;
                if(!possibleWord || nounPos[j]>endWithMe) {break;}
            }
            
            if(possibleWord) {
            	//System.out.println("add"+i);
                positions.add(i);
            }
            
            
            
        }
        
              
        
        int start = 0;
        
        for(int i=0;i<positions.size();i++) {
            fragments.add(inputText.substring(start,positions.get(i)));
            logger.info("fragments"+i+":"+fragments.toString());
            start = positions.get(i);
        }
        //이부분추가
        if(start+1<nounPos.length) {
        	fragments.add(inputText.substring(start));
        }
        
        //fragments.add(inputText);
        //System.out.println("fragments"+fragments.toString());
        return fragments;
    }
    
  /**
   * segment unsegmented sentence into words
   * a input text which has less than 3 characters or more than 10 character is not analyzed, 
   * because It seems to be no word segmentation error.
   * @param inputText unsegmented sentence
   * @return  segmented sentence into words
 * @throws MorphException 
   */
  public void analyze(String inputText, List<List<AnalysisOutput>> result, boolean containOneJosa) throws MorphException {

    List<WordListCandidate> candiateList = new ArrayList<WordListCandidate>();

    List<AnalysisOutput> aoList = morphAnal.analyze(inputText);
    logger.info("analyze inputText"+inputText);
    //System.out.println("aoList"+aoList.toString()+aoList.get(0).getScore()+!containOneJosa);
    /*if(aoList.get(0).getScore()==AnalysisOutput.SCORE_CORRECT && !containOneJosa) { // valid morpheme 원문주석
        result.add(aoList);
        return;
    }*/

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
    /*for(WordListCandidate candidate : candiateList) { //전체보여주기
    	System.out.println("candidate sort after==>"+candidate.getWordList().toString()+candidate.getCorrectLength()+" "+candidate.getWordList().size()+"ptn "+candidate.getPtnCount()+" "+candidate.getVerbCount()+" "+candidate.getFirstFragment().length());
    }*/
    
    for(WordListCandidate candidate : candiateList) {
      //System.out.println("candidate==>"+candidate.getWordList().toString());
     /* if(candiateList.indexOf(candidate)!=candiateList.size()-1 && 
    	   hasConsecutiveOneWord(candidate))
        continue;  //뭔지 모르지만 일단 주석처리 해보자
      */
      for(List<AnalysisOutput> outputs : candidate.getWordList()) {
        result.add(outputs);
      }      
      break;
    }
    
  }
  
  public void analyzeLast(String inputText, List<String> result, boolean containOneJosa) throws MorphException {

	    List<WordListCandidate> candiateList = new ArrayList<WordListCandidate>();

	    List<AnalysisOutput> aoList = morphAnal.analyze(inputText);
	    logger.info("analyze inputText"+inputText);
	    //System.out.println("aoList"+aoList.toString()+aoList.get(0).getScore()+!containOneJosa);
	    /*if(aoList.get(0).getScore()==AnalysisOutput.SCORE_CORRECT && !containOneJosa) { // valid morpheme 원문주석
	        result.add(aoList);
	        return;
	    }*/

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
	    /*for(WordListCandidate candidate : candiateList) { //전체보여주기
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
	        	result.add(outputs.get(i).getSource());
	        }
	      }
	      break;
	    }
	   
	  }
  
  private boolean hasConsecutiveOneWord(WordListCandidate candidate) {
	    
    int size = candidate.getWordList().size();
    for(int i=1;i<size;i++) {
      List<AnalysisOutput> outputs1 = candidate.getWordList().get(i-1);
      List<AnalysisOutput> outputs2 = candidate.getWordList().get(i);
      if(outputs1.get(0).getStem().length()==1 
          && outputs2.get(0).getStem().length()==1)
        return true;
    }
    
    return false;
  }
  
  private int validation(List<WordListCandidate> candiateList, String thisChar, 
      int start, String inputText) {
    
    int newStart = -1;
    AnalysisOutput dividedOutput = null;
    
    boolean lastPos = true;
    for(int i=candiateList.size()-1; i>=0;i--) {
      
      WordListCandidate candidate = candiateList.get(i);
      AnalysisOutput output = candidate.getWordList().get(0).get(0);
      
      int tempStart = validWord(output, start, inputText, lastPos);
      lastPos = false;
      
      if(tempStart<=start) {
        newStart = tempStart;
        dividedOutput = output;
        break;
      }     
    }
    
    // if here is a dividing point.
    if(newStart==start) 
      removeInvalidCandidate(candiateList, dividedOutput);
    
    return newStart;
  }
  
  
  /**
   * 
   * @param candiateList  all candidate list
   * @param dividedOutput the dividing analysis output
   */
  private void removeInvalidCandidate(List<WordListCandidate> candiateList, AnalysisOutput dividedOutput) {
    
    List<WordListCandidate> removes = new ArrayList<WordListCandidate>();
    for(int i=0;i<candiateList.size();i++) {
      
      WordListCandidate candidate = candiateList.get(i);
      AnalysisOutput output = candidate.getWordList().get(0).get(0);
 
      if(!output.getSource().equals(dividedOutput.getSource()) &&
          !includeNoun(candidate, dividedOutput, i)) 
        removes.add(candidate);        
    }
    
    candiateList.removeAll(removes);
  }
  
  /**
   * when the fragment can be analyzed as a verb, check whether if noun is included in the fragment.
   * prevent from being divided such as "전복사고==>전^복사고"
   * @param candidate all candidate list
   * @param dividedOutput  this analysis output
   * @return  check result
   */
  private boolean includeNoun(WordListCandidate candidate, AnalysisOutput dividedOutput, int pos) {
    
    if(candidate.getWordList().size()>1) {
      AnalysisOutput nextOutput = candidate.getWordList().get(1).get(0);
      if(nextOutput.getSource().length()>1 &&
          nextOutput.getPatn() == PatternConstants.PTN_N 
          && nextOutput.getScore()==AnalysisOutput.SCORE_CORRECT)
        return true;
    }
    
    return false;
  }
  
  /**
   * return the start position of the longest valid noun before the start position
   * @param output  analysis output
   * @param start start position
   * @param inputText input text
   * @param isLast  whether if this is the last word.
   * @return  the start position of the longest valid noun
   */
  private int validWord(AnalysisOutput output, int start, String inputText, boolean isLast) {
    
    int newStart = -1;
    if(output.getScore()!=AnalysisOutput.SCORE_CORRECT || 
        start==0 || output.getSource().length()<2) return newStart;
    
    if(!isLast && output.getJosa()==null && output.getEomi()==null) return newStart;
    
    if(output.getScore()==AnalysisOutput.SCORE_CORRECT)  newStart = start;
    
    // the word with greater than 6 length doesn't exist
    int minPos = start - 6;
    if(minPos<0) minPos = 0;
    
    for(int i=start-1; i>=minPos; i--) {
      String word = inputText.substring(i,start) + output.getStem();
      if(DictionaryUtil.getWord(word)!=null) {
        newStart = i;
      }
    }
    
    return newStart;
  }

  /**
   * calculate the score which is the worst score of the derived word scores
   * @param list  input
   * @return  calculated score
   */
  public int getOutputScore(List<AnalysisOutput> list) {
    int score = 100;
    for (AnalysisOutput o : list) {
      score = Math.min(score, o.getScore());
    }
    return score;
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
  
  public static void main(String args[]) {
   Vector<String> inputs = new Vector<String>();
	try {
		
		
	SpWordSegmentAnalyzer ws = new SpWordSegmentAnalyzer();
	StringBuilder result = new StringBuilder();
	//String s = source.replace(" ","");
	//String source = "세상에서제일사랑스러웠음요";
	//String source = "그친구는정말사랑스러움";
	//String source = "치킨이너무먹고싶네";
	//String source = "그사람별로예쁘지않음";
	//String source = "더이상학생이아님";
	//String source = "나지금공부해요";
	//String source = "평소에도예뻤어요";
	//String source = "너오늘진짜예뻐";
	//String source = "볼수록사랑스러워";
	//String source = "생각보다예쁘지않았어";
	//String source = "아까다먹음요";
	//String source = "걔는학생이아니야";
	String source = "생일이었어";
	
	String s=source.replace(" ","");
	//System.out.println("s=="+s);
	List<String> outList = ws.analyzeNew(s); //이부분이 분석이다
	
	System.out.println(outList.toString());
	logger.info(outList.toString());
	/*for(int j=0;j<inputs.size();j++) {
		//System.out.println("newvec.elementAt(j)"+newvec.elementAt(j));
		Vector<String> v= ws.wordSegmentOrigin(inputs.elementAt(j));
		for(int k=0;k<v.size();k++) {
			System.out.println(v.elementAt(k));
		}
	}*/
	}catch(Exception e) {
		e.printStackTrace();
	}
  }
  public Vector<String> wordSegmentOrigin(String inputText) throws MorphException {

	  List<Integer> positions = new LinkedList<Integer>();

      int[] nounPos = new int[inputText.length()];
      for(int i=0;i<inputText.length();i++) nounPos[i]=-1; // initialization

      StringBuffer sb = new StringBuffer();
	    //원본이 부사이면 자르지 마라
	if(DictionaryUtil.getBusa(inputText)==null){ //부사이면 자르지 마라
	  for(int i=0;i<inputText.length();i++) {
	      int lastIndex = findLongestNoun(i, inputText);//단어를 찾는다
	      //System.out.println("lastIndex"+lastIndex);
	      if(lastIndex!=-1 && nounPos[lastIndex]==-1) {
	          nounPos[lastIndex] = i; // store the start position of a noun at the iastIndex position to which the noun spans.
	      }
	  }
	}
	
      int[] oneJosa = new int[inputText.length()];
      for(int i=1;i<nounPos.length;i++) {
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
          fragments.add(inputText.substring(start,positions.get(i)));
          start = positions.get(i);
      }
      fragments.add(inputText.substring(start));
      //System.out.println("fragments"+fragments.toString());
      return fragments;
	}
  
  public static void main2(String args[]) {
	   
		try {
		SpWordSegmentAnalyzer ws = new SpWordSegmentAnalyzer();
		
		//String s = source.replace(" ","");
		Vector<String> vec = new Vector<String>();
		vec.add("너오늘진짜예뻐");
		vec.add("어제치킨먹었어");
		vec.add("오늘좀예쁨");
		vec.add("우리언니어렸을때예뻤음");  //복합어로 처리해야함
		vec.add("치킨이너무먹고싶네");
		vec.add("그사람별로예쁘지않음");
		vec.add("치킨은어제먹고싶었지");
		vec.add("생각보다예쁘지않았어");
		vec.add("족발이그렇게먹고싶었음");
		vec.add("많이먹어요");
		vec.add("평소에도예뻤어요");
		vec.add("아까다먹음요");
		vec.add("아까다먹었음요");
		vec.add("아무리봐도예쁘지않아요");
		vec.add("집가서먹고싶음요");
		vec.add("그다지예쁘지않았음요");
		vec.add("나는학생이야");
		vec.add("걔는학생이아니야");
		vec.add("사촌동생도학생임");
		vec.add("더이상학생이아님");
		vec.add("작년까지학생이었지");
		vec.add("알고보니학생이아니었어");
		vec.add("한때나도학생이었음");
		vec.add("우리학교학생이아니었음");
		vec.add("나지금공부해");
		vec.add("볼수록사랑스러워");
		vec.add("늦게까지공부함");
		vec.add("그친구는정말사랑스러움");
		vec.add("밤새공부했어");
		vec.add("연애할땐사랑스러웠어");
		vec.add("벼락치기로공부했음");
		vec.add("세상에서제일사랑스러웠음");
		vec.add("학생인듯싶어");
		vec.add("학생이아닌듯싶어");
		vec.add("학생인듯싶음");
		vec.add("학생이아닌듯싶음");
		vec.add("학생인듯싶었어");
		vec.add("학생이아닌듯싶었어");
		vec.add("학생인듯싶었음");
		vec.add("학생이아닌듯싶었음");
		vec.add("공부한듯싶어");
		vec.add("사랑스러운듯싶어");
		vec.add("공부한듯싶음");
		vec.add("사랑스러운듯싶음");
		vec.add("공부한듯싶었어");
		vec.add("사랑스러운듯싶었어");
		vec.add("공부한듯싶었음");
		vec.add("사랑스러운듯싶었음");
		vec.add("동생좀운동시키고싶어");
		vec.add("전혀사랑스럽지않아");
		vec.add("우리집강아지운동시키고싶음");
		vec.add("동생크니까사랑스럽지않음");
		vec.add("예전부터운동시키고싶었어");
		vec.add("실제로보니까사랑스럽지않았어");
		vec.add("소질이있어서운동시키고싶었음");
		vec.add("정떨어져서사랑스럽지않았음");
		vec.add("나는학생이에요");
		vec.add("걔는학생이아니에요");
		vec.add("사촌동생도학생임요");
		vec.add("더이상학생이아님요");
		vec.add("작년까지학생이었죠");
		vec.add("알고보니학생이아니었어요");
		vec.add("한때나도학생이었음요");
		vec.add("우리학교학생이아니었음요");
		vec.add("나지금공부해요");
		vec.add("볼수록사랑스러워요");
		vec.add("늦게까지공부함요");
		vec.add("그친구는정말사랑스러움요");
		vec.add("밤새공부했어요");
		vec.add("연애할땐사랑스러웠어요");
		vec.add("벼락치기로공부했음요");
		vec.add("세상에서제일사랑스러웠음요");
		vec.add("학생인듯싶어요");
		vec.add("학생이아닌듯싶어요");
		vec.add("학생인듯싶음요");
		vec.add("학생이아닌듯싶음요");
		vec.add("학생인듯싶었어요");
		vec.add("학생이아닌듯싶었어요");
		vec.add("학생인듯싶었음요");
		vec.add("학생이아닌듯싶었음요");
		vec.add("공부한듯싶어요");
		vec.add("사랑스러운듯싶어요");
		vec.add("공부한듯싶음요");
		vec.add("사랑스러운듯싶음요");
		vec.add("공부한듯싶었어요");
		vec.add("사랑스러운듯싶었어요");
		vec.add("공부한듯싶었음요");
		vec.add("사랑스러운듯싶었음요");
		vec.add("동생좀운동시키고싶어요");
		vec.add("전혀사랑스럽지않아요");
		vec.add("우리집강아지운동시키고싶음요");
		vec.add("동생크니까사랑스럽지않음요");
		vec.add("예전부터운동시키고싶었어요");
		vec.add("실제로보니까사랑스럽지않았어요");
		vec.add("소질이있어서운동시키고싶었음요");
		vec.add("정떨어져서사랑스럽지않았음요");
		Vector<String> vec2 = new Vector<String>();
		vec2.add("[너, 오늘, 진짜, 예뻐]");
		vec2.add("[어제, 치킨, 먹었어]");
		vec2.add("[오늘, 좀, 예쁨]");
		vec2.add("[우리언니, 어렸을, 때, 예뻤음]");
		vec2.add("[치킨이, 너무, 먹고, 싶네]");
		vec2.add("[그, 사람, 별로, 예쁘지, 않음]");
		vec2.add("[치킨은, 어제, 먹고, 싶었지]");
		vec2.add("[생각보다, 예쁘지, 않았어]");
		vec2.add("[족발이, 그렇게, 먹고, 싶었음]");
		vec2.add("[많이, 먹어요]");
		vec2.add("[평소에도, 예뻤어요]");
		vec2.add("[아까, 다, 먹음요]");
		vec2.add("[아까, 다, 먹었음요]");
		vec2.add("[아무리, 봐도, 예쁘지, 않아요]");
		vec2.add("[집, 가서, 먹고, 싶음요]");
		vec2.add("[그다지, 예쁘지, 않았음요]");
		vec2.add("[나는, 학생이야]");
		vec2.add("[걔는, 학생이, 아니야]");
		vec2.add("[사촌동생도, 학생임]");
		vec2.add("[더, 이상, 학생이, 아님]");
		vec2.add("[작년까지, 학생이었지]");
		vec2.add("[알고, 보니, 학생이, 아니었어]");
		vec2.add("[한때, 나도, 학생이었음]");
		vec2.add("[우리, 학교, 학생이, 아니었음]");
		vec2.add("[나, 지금, 공부해]");
		vec2.add("[볼수록, 사랑스러워]");
		vec2.add("[늦게까지, 공부함]");
		vec2.add("[그, 친구는, 정말, 사랑스러움]");
		vec2.add("[밤새, 공부했어]");
		vec2.add("[연애할, 땐, 사랑스러웠어]");
		vec2.add("[벼락치기로, 공부했음]");
		vec2.add("[세상에서, 제일, 사랑스러웠음]");
		vec2.add("[학생인, 듯싶어]");
		vec2.add("[학생이, 아닌, 듯싶어]");
		vec2.add("[학생인, 듯싶음]");
		vec2.add("[학생이, 아닌, 듯싶음]");
		vec2.add("[학생인, 듯싶었어]");
		vec2.add("[학생이, 아닌, 듯싶었어]");
		vec2.add("[학생인, 듯싶었음]");
		vec2.add("[학생이, 아닌듯, 싶었음]");
		vec2.add("[공부한, 듯싶어]");
		vec2.add("[사랑스러운, 듯싶어]");
		vec2.add("[공부한, 듯싶음]");
		vec2.add("[사랑스러운, 듯싶음]");
		vec2.add("[공부한, 듯싶었어]");
		vec2.add("[사랑스러운, 듯싶었어]");
		vec2.add("[공부한, 듯싶었음]");
		vec2.add("[사랑스러운, 듯싶었음]");
		vec2.add("[동생, 좀, 운동시키고, 싶어]");
		vec2.add("[전혀, 사랑스럽지, 않아]");
		vec2.add("[우리집, 강아지, 운동시키고, 싶음]");
		vec2.add("[동생, 크니까, 사랑스럽지, 않음]");
		vec2.add("[예전부터, 운동시키고, 싶었어]");
		vec2.add("[실제로, 보니까, 사랑스럽지, 않았어]");
		vec2.add("[소질이, 있어서, 운동시키고, 싶었음]");
		vec2.add("[정, 떨어져서, 사랑스럽지, 않았음]");
		vec2.add("[나는, 학생이에요]");
		vec2.add("[걔는, 학생이, 아니에요]");
		vec2.add("[사촌동생도, 학생임요]");
		vec2.add("[더, 이상, 학생이, 아님요]");
		vec2.add("[작년까지, 학생이었죠]");
		vec2.add("[알고, 보니, 학생이, 아니었어요]");
		vec2.add("[한때, 나도, 학생이었음요]");
		vec2.add("[우리학교, 학생이, 아니었음요]");
		vec2.add("[나, 지금, 공부해요]");
		vec2.add("[볼수록, 사랑스러워요]");
		vec2.add("[늦게까지, 공부함요]");
		vec2.add("[그, 친구는, 정말, 사랑스러움요]");
		vec2.add("[밤새, 공부했어요]");
		vec2.add("[연애할, 땐, 사랑스러웠어요]");
		vec2.add("[벼락치기로, 공부했음요]");
		vec2.add("[세상에서, 제일, 사랑스러웠음요]");
		vec2.add("[학생인, 듯싶어요]");
		vec2.add("[학생이, 아닌, 듯싶어요]");
		vec2.add("[학생인, 듯싶음요]");
		vec2.add("[학생이, 아닌, 듯싶음요]");
		vec2.add("[학생인, 듯싶었어요]");
		vec2.add("[학생이, 아닌, 듯싶었어요]");
		vec2.add("[학생인, 듯싶었음요]");
		vec2.add("[학생이, 아닌, 듯싶었음요]");
		vec2.add("[공부한, 듯싶어요]");
		vec2.add("[사랑스러운, 듯싶어요]");
		vec2.add("[공부한, 듯싶음요]");
		vec2.add("[사랑스러운, 듯싶음요]");
		vec2.add("[공부한, 듯싶었어요]");
		vec2.add("[사랑스러운, 듯싶었어요]");
		vec2.add("[공부한, 듯싶었음요]");
		vec2.add("[사랑스러운, 듯싶었음요]");
		vec2.add("[동생, 좀, 운동시키고, 싶어요]");
		vec2.add("[전혀, 사랑스럽지, 않아요]");
		vec2.add("[우리집, 강아지, 운동시키고, 싶음요]");
		vec2.add("[동생, 크니까, 사랑스럽지, 않음요]");
		vec2.add("[예전부터, 운동시키고, 싶었어요]");
		vec2.add("[실제로, 보니까, 사랑스럽지, 않았어요]");
		vec2.add("[소질이, 있어서, 운동시키고, 싶었음요]");
		vec2.add("[정, 떨어져서, 사랑스럽지, 않았음요]");
		
		for(int i=0;i<vec.size();i++) {
			Vector<String> inputs = new Vector<String>();
			StringBuilder result = new StringBuilder();
			String s = (String)vec.elementAt(i);
			//System.out.println("s=="+s);
			List<String> outList = ws.analyzeNew(s); //이부분이 분석이다
			//System.out.println(outList.toString());
			for(String str:outList){
				inputs.add(str);
			}
			
			Vector<String> newvec= ws.clearDuple(inputs);
			Vector<String> newvec2= new Vector<String>();
			//for(int j=0;j<newvec.size();j++) {
			//	//System.out.println("newvec.elementAt(j)"+newvec.elementAt(j));
			//	Vector<String> v= ws.wordSegmentOrigin(newvec.elementAt(j));
			//	for(int k=0;k<v.size();k++) {
			//		newvec2.add(v.elementAt(k));
			//	}
			//}
			
			
			System.out.print("정답"+vec2.elementAt(i)+" 자동변환"+newvec.toString());
			
			
			if(vec2.elementAt(i).equals(newvec.toString())) {
				System.out.println(" 결과:Y");
			}else {
				System.out.println(" 결과:N");
			}
			
			
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
  }
  public static List<AnalysisOutput> analyze(Vector<String> vec) {
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
	}
  public Vector<String> clearDuple(Vector<String> vec) {
		Vector<String> newVec = new Vector<String>();
		try {
				
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
  public static Vector<String> wordSegment(String source) {
		Vector<String> inputs = new Vector<String>();
		TokenStream ts = null ;
	    //String szText="너오늘진짜예뻐";
		String output = "" ;
		//String s = source.replace(" ","");
		String s = source;
		SpKoreanTokenizer koreanTokenizer = new SpKoreanTokenizer();  // tokenizer 적용.
	    koreanTokenizer.setReader(new StringReader(source));
	    
		TokenStream tokenStream = new SpKoreanFilter(koreanTokenizer,true,true,true,true,false); 
		//TokenStream input, boolean bigram, boolean has, boolean exactMatch, 
		//  boolean cnoun, boolean isQuery
		
		try {
			
			CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
			TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);
			tokenStream.reset();
		      
			while (tokenStream.incrementToken()) {
			    //System.out.print(termAtt.toString()+" ");
				inputs.add(termAtt.toString());
			}
			//System.out.println(inputs.toString());
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
}

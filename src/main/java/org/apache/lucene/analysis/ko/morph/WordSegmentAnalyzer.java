package org.apache.lucene.analysis.ko.morph;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanFilter;
import org.apache.lucene.analysis.ko.KoreanTokenizer;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

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

public class WordSegmentAnalyzer {

  private final MorphAnalyzer morphAnal = new MorphAnalyzer();

    //private static final int maxCandidate = 64;
    private static final int maxCandidate = 128;

    private static final int adjustNoOfCandidate = 40;
    //private static final int adjustNoOfCandidate = 100;
    
    private static final String possibleWordStartJosa = "의은가나며아야에엔여와요이";

    @SuppressWarnings("unused")
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
        List<String> segList = splitByNoun(inputText, nounPos,oneJosa);
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

    private boolean isContainOneJosa(int offset, int length, int[] oneJosa) {
    	for(int i=offset;i<length;i++) {
    		if(oneJosa[i]==1) return true;
    	}
    	return false;
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
            //System.out.println("findLongestNoun"+sb.toString());
            if(!DictionaryUtil.findWithPrefix(sb.toString()).hasNext()) {
                return lastIndex;
            }
            if(DictionaryUtil.getAllNoun(sb.toString())!=null) {
                lastIndex = i;
            }
        }
        return lastIndex;
    }

    public List<String> splitByNoun(String inputText, int[] nounPos, int[] oneJosa) throws MorphException {
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
    //System.out.println("analyze inputText"+inputText);
    //System.out.println("aoList"+aoList.toString());
    if(aoList.get(0).getScore()==AnalysisOutput.SCORE_CORRECT && !containOneJosa) { // valid morpheme
        result.add(aoList);
        return;
    }

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
      //System.out.println("candiateList.size()==>"+candiateList.size());
      
      if(candiateList.size()>=maxCandidate) {
    	  Collections.sort(candiateList, new WordListComparator());
          removeLast(candiateList,adjustNoOfCandidate);
      }
      /*for(WordListCandidate candidate : candiateList) {
    	  System.out.println("candidate==>"+candidate.getWordList().toString());
      }*/
      //System.out.println();
    }
    
    Collections.sort(candiateList, new WordListComparator());
    /*for(WordListCandidate candidate : candiateList) {
  	  System.out.println("candidate sort==>"+candidate.getWordList().toString());
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
  		//System.out.println("remove"+list.get(i).getWordList().toString());
  	}
  	
  	for(Object o : removed) {
  		list.remove(o);
  	}
  	
  	removed=null;
  }
  
  public static void main(String args[]) {
   Vector<String> inputs = new Vector<String>();
	try {
	WordSegmentAnalyzer ws = new WordSegmentAnalyzer();
	StringBuilder result = new StringBuilder();
	//String s = source.replace(" ","");
	//String s = "세상에서제일사랑스러웠음요";
	String s = "치킨이너무먹고싶네";
	//System.out.println("s=="+s);
	List<List<AnalysisOutput>> outList = ws.analyze(s); //이부분이 분석이다
	//System.out.println(outList.toString());
	for(List<AnalysisOutput> o: outList) {
		for(AnalysisOutput analysisOutput : o) {
			//result.append(analysisOutput.getSource()).append(" ");
			inputs.add(analysisOutput.getSource());
		}
	}
	System.out.println(inputs.toString());
	}catch(Exception e) {
		e.printStackTrace();
	}
  }
  /*public static void main(String args[]) {
	   
		try {
		WordSegmentAnalyzer ws = new WordSegmentAnalyzer();
		
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
		vec2.add("[사랑스러운듯싶어]");
		vec2.add("[공부한듯싶음]");
		vec2.add("[사랑스러운듯싶음]");
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
			List<List<AnalysisOutput>> outList = ws.analyze(s); //이부분이 분석이다
			//System.out.println(outList.toString());
			for(List<AnalysisOutput> o: outList) {
				for(AnalysisOutput analysisOutput : o) {
					//result.append(analysisOutput.getSource()).append(" ");
					inputs.add(analysisOutput.getSource());
				}
			}
			
			System.out.print("정답"+vec2.elementAt(i)+" 자동변환"+inputs.toString());
			if(vec2.elementAt(i).equals(inputs.toString())) {
				System.out.println(" 결과:Y");
			}else {
				System.out.println(" 결과:N");
			}
			
			if(inputs.size()==0) {
				wordSegment(s);
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	  }*/
  public static Vector<String> wordSegment(String source) {
		Vector<String> inputs = new Vector<String>();
		TokenStream ts = null ;
	    //String szText="너오늘진짜예뻐";
		String output = "" ;
		//String s = source.replace(" ","");
		String s = source;
		KoreanTokenizer koreanTokenizer = new KoreanTokenizer();  // tokenizer 적용.
	    koreanTokenizer.setReader(new StringReader(source));
	    
		TokenStream tokenStream = new KoreanFilter(koreanTokenizer,false,true,false,false,false); 
		//TokenStream input, boolean bigram, boolean has, boolean exactMatch, 
		//  boolean cnoun, boolean isQuery
		
		try {
			
			CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
			TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);
			tokenStream.reset();
		      
			while (tokenStream.incrementToken()) {
			 System.out.print(termAtt.toString()+" ");
				//inputs.add(termAtt.toString());
			}
			//System.out.println();
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

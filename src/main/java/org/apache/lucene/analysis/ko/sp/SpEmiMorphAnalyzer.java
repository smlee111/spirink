package org.apache.lucene.analysis.ko.sp;

import org.apache.lucene.analysis.ko.morph.AbbrevFinder;
import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.AnalysisOutputComparator;
import org.apache.lucene.analysis.ko.morph.CompoundEntry;
import org.apache.lucene.analysis.ko.morph.CompoundNounAnalyzer;
import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.morph.PatternConstants;
import org.apache.lucene.analysis.ko.morph.WordEntry;
import org.apache.lucene.analysis.ko.sp.SpUtilities;

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

import org.apache.lucene.analysis.ko.utils.*;

import antlr.collections.impl.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SpEmiMorphAnalyzer {

  /**
   * starting word of sentence.
   */
  public static final int POS_START = 1;
  
  /**
   * middle word of sentence
   */
  public static final int POS_MID = 2;
  
  /**
   * ending word of sentence.
   */
  public static final int POS_END = 3;  
  
  /**
   * determin whether one letter is divisible when to divide a compound noun.
   */
  private boolean divisibleOne = true;
  
  
  private CompoundNounAnalyzer cnAnalyzer = new CompoundNounAnalyzer();  
  
  private AbbrevFinder abbvFinder = new AbbrevFinder();
  private HashMap map = new HashMap();
  public SpEmiMorphAnalyzer() {
    cnAnalyzer.setExactMach(false);
  }
  
  public void setExactCompound(boolean is) {
    cnAnalyzer.setExactMach(is);
  }
  
  /**
   * set if one char can be divisible
   * @param is
   */
  public void setDivisibleOne(boolean is) {
	  cnAnalyzer.setDivisible(is);
	  divisibleOne = is;
  }
  
  public List<AnalysisOutput> analyze(String input) throws MorphException {  

	List<AnalysisOutput> outputs = abbvFinder.find(input);
	
	if(outputs!=null) return outputs;
	
    if(input.endsWith("."))  
      return analyze(input.substring(0,input.length()-1), POS_END);
    //System.out.println("analyze(String input)==>"+input+" "+POS_MID);
    return analyze(input, POS_MID);
  }
  

  
  /**
   * 
   * @param input input
   * @param pos pos
   * @return candidates
   * @throws MorphException exception
   */
  public List<AnalysisOutput> analyze(String input, int pos) throws MorphException {    
   
    List<AnalysisOutput> candidates = new ArrayList<AnalysisOutput>();        
    boolean isVerbOnly = MorphUtil.hasVerbSyllableOnly(input);
    AnalysisOutputComparator<AnalysisOutput> comparator = new AnalysisOutputComparator<AnalysisOutput>();
    
    analysisByRuleNew(input, candidates);    
    
    	//System.out.println("analysisByRule===>"+candidates.toString());
    
    //중복단어로 인하여 주석처리 20210222 oyh
     if((!isVerbOnly && onlyHangulWithinStem(candidates) && 
    		MorphUtil.isNotCorrect(candidates)) || 
    		DictionaryUtil.getWordExceptVerb(input)!=null) 
    {
    	 
    	 //System.out.println(input+"addSingleWord 1"+candidates.toString());
    	 
    	 if(candidates.size()==0) { //없을때만 add한다
    		 addSingleWord(input,candidates);
    	 }
    	 
    	 //System.out.println(input+"addSingleWord 2"+candidates.toString());
    	 
    }	
    
    // check if one letter exists in the compound noun entries
    checkOneLetterInCNoun(candidates);
    
    Collections.sort(candidates,comparator);
    
    // divide compound noun into unit noun
    decompoundNoun(candidates, isVerbOnly, comparator);
    
    List<AnalysisOutput> results = new ArrayList<AnalysisOutput>();  
    HashMap<String, AnalysisOutput> stems = new HashMap<String, AnalysisOutput>();
    
    AnalysisOutput compound = chooseValidResults(input, candidates, results, stems);      

    if(compound!=null) {
    	addResults(compound,results,stems);
    	Collections.sort(results,comparator);  
    }
    
    if(results.size()==0) {
      AnalysisOutput output = new AnalysisOutput(input, null, null, PatternConstants.PTN_N, AnalysisOutput.SCORE_ANALYSIS);
      output.setSource(input);
      output.setPos(PatternConstants.POS_NOUN);
      results.add(output);
    }
    //if(input.equals("치킨이")) {
    	//System.out.println("results===>"+results);
    //}
    return results;
  }

  private AnalysisOutput chooseValidResults(String input, List<AnalysisOutput> candidates, List<AnalysisOutput> results,
			HashMap<String, AnalysisOutput> stems) {
		boolean hasCorrect = false;
	    boolean hasCorrectNoun = false;
	    boolean correctCnoun = false;
	
	    AnalysisOutput noun = null;
	    
	    double ratio = 0;
	    AnalysisOutput compound = null;
	    
	    for(AnalysisOutput o:candidates) { 
	      
	      o.setSource(input);
	      
	      if(o.getScore()==AnalysisOutput.SCORE_FAIL) continue; // 분석에는 성공했으나, 제약조건에 실패
	      
	      if(o.getScore()==AnalysisOutput.SCORE_CORRECT && o.getPos()!=PatternConstants.POS_NOUN ) 
	      {
	        addResults(o,results,stems);
	        hasCorrect = true;
	      }
	      else if(o.getPos()==PatternConstants.POS_NOUN
	          &&o.getScore()>=AnalysisOutput.SCORE_SIM_CORRECT) 
	      {
	        
	        if((hasCorrect||correctCnoun)&&o.getCNounList().size()>0) continue;
	        
	        if(o.getPos()==PatternConstants.POS_NOUN) 
	        {
	          addResults(o,results,stems);
	        }
	        else if(noun==null) 
	        {
	          addResults(o,results,stems);
	          noun = o;
	        }
	        else if(o.getPatn()==PatternConstants.PTN_N
	            ||(o.getPatn()>noun.getPatn())||
	            (o.getPatn()==noun.getPatn()&&
	                o.getJosa()!=null&&noun.getJosa()!=null
	                &&o.getJosa().length()>noun.getJosa().length())) 
	        {
	          results.remove(noun);
	          addResults(o,results,stems);
	          noun = o;
	        }
	        hasCorrectNoun=true;
	
	      }
	      else if(o.getPos()==PatternConstants.POS_NOUN
	          &&o.getCNounList().size()>0&&!hasCorrect
	          &&!hasCorrectNoun) 
	      {
	        double curatio = NounUtil.countFoundNouns(o);
	        if(ratio<curatio&&(compound==null||(compound!=null&&compound.getJosa()==null))) {
	          ratio  = curatio;
	          compound = o;
	        }
	      }
	      else if(o.getPos()==PatternConstants.POS_NOUN
	          &&!hasCorrect
	          &&!hasCorrectNoun) 
	      {
	        addResults(o,results,stems);
	      }
	      else if(o.getPatn()==PatternConstants.PTN_NSM) 
	      {
	        addResults(o,results,stems);
	      } 
	    }
		return compound;
	}
	
	private void decompoundNoun(List<AnalysisOutput> candidates, boolean isVerbOnly, AnalysisOutputComparator<AnalysisOutput> comparator) throws MorphException {
		// 복합명사 분해여부 결정하여 분해
	    boolean changed = false;
	    boolean correct = false;
	    for(AnalysisOutput o:candidates) {
	    
	      if(o.getPatn()==PatternConstants.PTN_N) {
	    	  
	    	  if(o.getScore()==AnalysisOutput.SCORE_CORRECT) {
	    		  correct=true;
	    	  }
	    	  
	    	  continue;
	      }
	      
	      if(o.getScore()==AnalysisOutput.SCORE_CORRECT || isVerbOnly) {
	    	  
	    	if(o.getPatn()<=PatternConstants.PTN_NJ && !isVerbOnly) {
	    		confirmCNoun(o, true);
	    	}
	    	
	    	if(o.getScore()==AnalysisOutput.SCORE_CORRECT){
	    		correct=true;
	    	}
	    	
	        break;
	      }
	      
	      if(o.getPatn()<PatternConstants.PTN_VM&&o.getStem().length()>2) {
	    	  
	        if(!(correct&&o.getPatn()==PatternConstants.PTN_N) 
	        		&& !"내".equals(o.getVsfx())) {
	        	confirmCNoun(o);
	        }
	        
	        if(o.getScore()>=AnalysisOutput.SCORE_COMPOUNDS) {
	        	changed= changed || true;
	        	correct= correct || (o.getScore()>=AnalysisOutput.SCORE_CORRECT);
	        }
	      }
	    
	    }
	    
	    if(correct)
	      filterInCorrect(candidates);
	    
	    if(changed) {
	      Collections.sort(candidates,comparator);  
	    }
	}
  
  /**
   * removed the candidate items when one more candidates in correct is found
   * @param candidates  analysis candidates
   */
  private void filterInCorrect(List<AnalysisOutput> candidates) {
    List<AnalysisOutput> removeds = new ArrayList();
    
    for(AnalysisOutput o : candidates) {
      if(o.getScore()!=AnalysisOutput.SCORE_CORRECT)
        removeds.add(o);
    }
    
    for(AnalysisOutput o : removeds) {
      candidates.remove(o);
    }
    
  }
  
  private void analysisByRule(String input, List<AnalysisOutput> candidates) throws MorphException {
  
    boolean josaFlag = true;  
    boolean eomiFlag = true;
        
    int strlen = input.length();
    
    boolean isVerbOnly = false;
    analysisWithEomi(input,"",candidates);
    
    for(int i=strlen-1;i>0;i--) {
      
      String stem = input.substring(0,i);
      String eomi = input.substring(i);

      char[] feature =  SyllableUtil.getFeature(eomi.charAt(0));    
      if(!isVerbOnly&&josaFlag&&feature[SyllableUtil.IDX_JOSA1]=='1') {        
        analysisWithJosa(stem,eomi,candidates);
      }
      
      if(eomiFlag) {      
        analysisWithEomi(stem,eomi,candidates);
      }      
      
      if(josaFlag&&feature[SyllableUtil.IDX_JOSA2]=='0') josaFlag = false;
      if(eomiFlag&&feature[SyllableUtil.IDX_EOMI2]=='0') eomiFlag = false;
      //System.out.println("josaFlag==>"+josaFlag);
      if(!josaFlag&&!eomiFlag) break;
    }
  }
  
  private void analysisByRuleNew(String input, List<AnalysisOutput> candidates) throws MorphException {
	  
	    boolean josaFlag = true;  
	    boolean eomiFlag = true;
	        
	    int strlen = input.length();

	    boolean isVerbOnly = false;
	    String dupStr="";
	    analysisWithEomi(input,"",candidates);
	    
	    for(int i=strlen-1;i>0;i--) {
	      
	      String stem = input.substring(0,i);
	      String eomi = input.substring(i);

	      char[] feature =  SyllableUtil.getFeature(eomi.charAt(0));    
	      if(!isVerbOnly&&josaFlag&&feature[SyllableUtil.IDX_JOSA1]=='1') {        
	        analysisWithJosa(stem,eomi,candidates);
	      }
	      
	      if(eomiFlag) {      
	        analysisWithEomi(stem,eomi,candidates);
	      }      
	      
	      if(josaFlag&&feature[SyllableUtil.IDX_JOSA2]=='0') josaFlag = false;
	      if(eomiFlag&&feature[SyllableUtil.IDX_EOMI2]=='0') eomiFlag = false;
	      //System.out.println("josaFlag==>"+josaFlag);
	      if(!josaFlag&&!eomiFlag) break;
	    }
	  }
  
  private void addResults(AnalysisOutput o, List<AnalysisOutput> results, HashMap<String, AnalysisOutput> stems) {
    AnalysisOutput old = stems.get(o.getStem());
    if(old==null||old.getPos()!=o.getPos()) {
      results.add(o);
      stems.put(o.getStem(), o);
    }else if(old.getPatn()<o.getPatn()) {
      results.remove(old);
      results.add(o);
      stems.put(o.getStem(), o);
    }
  }
  
  private boolean onlyHangulWithinStem(List<AnalysisOutput> candidates) {	
	  boolean onlyHangul = true;
	  for(AnalysisOutput o : candidates) {
		  if(!MorphUtil.isHanSyllable(o.getStem().charAt(o.getStem().length()-1))) {
			  onlyHangul = false;
			  break;
		  }
	  }
	  
	  if(onlyHangul) return true;
	  
	  List<AnalysisOutput> removeList = new ArrayList<AnalysisOutput>();
	  for(AnalysisOutput o : candidates) {
		  if(MorphUtil.isHanSyllable(o.getStem().charAt(o.getStem().length()-1))) {
			  removeList.add(o);
		  }
	  }
	  
	  for(AnalysisOutput o : removeList) {
		  candidates.remove(o);
	  }
	  
	  return onlyHangul;
  }
  
  //단어하나
  private void addSingleWord(String word, List<AnalysisOutput> candidates) throws MorphException {
    
//    if(candidates.size()!=0&&candidates.get(0).getScore()==AnalysisOutput.SCORE_CORRECT) return;
	
    AnalysisOutput output = new AnalysisOutput(word, null, null, PatternConstants.PTN_N);
    output.setPos(PatternConstants.POS_NOUN);
    //SpUtilities.outLine("word===>",word);
    WordEntry entry;
    
    if(word.length()>1 && (entry=DictionaryUtil.getWord(word))!=null) {
    	if(word.equals("그친")||word.equals("구는")){
        	System.out.println("_BUSA)"+entry.getFeature(WordEntry.IDX_BUSA)+"_NOUN)"+entry.getFeature(WordEntry.IDX_NOUN));
        }	
      if(entry.getFeature(WordEntry.IDX_BUSA)=='1') {//부사이면
    	AnalysisOutput busa = new AnalysisOutput(word, null, null, PatternConstants.PTN_AID);
        busa.setPos(PatternConstants.POS_ETC);
        
        busa.setScore(AnalysisOutput.SCORE_CORRECT);
        candidates.add(0,busa);
        //SpUtilities.outLine("1 input candidates",candidates.toString());   
      }else if(entry.getFeature(WordEntry.IDX_NOUN)=='1' && entry.getWord().length()>1) {//명사이고 1보다크면
        output.setScore(AnalysisOutput.SCORE_CORRECT);
        //System.out.println("output"+output.toString()+AnalysisOutput.SCORE_CORRECT);
        candidates.add(0,output); //임시주석
        //SpUtilities.outLine("2 input candidates",candidates.toString()+output.getScore());
      }else if(entry.getFeature(WordEntry.IDX_NOUN)=='2' && entry.getWord().length()>1) {//명사이고 1보다크면
    	output.setScore(AnalysisOutput.SCORE_CORRECT);
    	output.addCNoun(entry.getCompounds());
        candidates.add(0,output);
        //SpUtilities.outLine("3 input candidates",candidates.toString());
      }
      //System.out.println("entry.getFeature(WordEntry.IDX_VERB)"+entry.getFeatu`re(WordEntry.IDX_VERB));
      
      if(entry.getFeature(WordEntry.IDX_VERB)!='1') return;
    } else if(candidates.size()==0||!NounUtil.endsWith2Josa(word)) {
      output.setScore(AnalysisOutput.SCORE_ANALYSIS);
      candidates.add(0,output);
    }
    
    if(output.getScore()!=AnalysisOutput.SCORE_CORRECT) {
    	boolean success = confirmCNoun(output);
    	if(success) candidates.add(0,output);
    }
    	
  }
  
  /**
   * 체언 + 조사 (PTN_NJ)
   * 체언 + 용언화접미사 + '음/기' + 조사 (PTN_NSMJ
   * 용언 + '음/기' + 조사 (PTN_VMJ)
   * 용언 + '아/어' + 보조용언 + '음/기' + 조사(PTN_VMXMJ)
   * 
   * @param stem  stem
   * @param end end
   * @param candidates  candidates
   * @throws MorphException exception
   */
  public void analysisWithJosa(String stem, String end, List<AnalysisOutput> candidates) throws MorphException {
  
    if(stem==null||stem.length()==0) return;  
    
    char[] chrs = MorphUtil.decompose(stem.charAt(stem.length()-1));
    if(!DictionaryUtil.existJosa(end)||
        (chrs.length==3&&ConstraintUtil.isTwoJosa(end))||
        (chrs.length==2&&(ConstraintUtil.isThreeJosa(end))||
        "".equals(end))) 
    	return; // 연결이 가능한 조사가 아니면...

    AnalysisOutput output = new AnalysisOutput(stem, end, null, PatternConstants.PTN_NJ);
    output.setPos(PatternConstants.POS_NOUN);
    
    boolean success = false;
    try {
      success = NounUtil.analysisMJ(output.clone(), candidates);
    } catch (CloneNotSupportedException e) {
      throw new MorphException(e.getMessage(),e);
    }

    WordEntry entry = DictionaryUtil.getWordExceptVerb(stem);
    if(entry!=null) {
      output.setScore(AnalysisOutput.SCORE_CORRECT);
      //output.setScore(AnalysisOutput.SCORE_SIM_CORRECT);
      if(entry.getFeature(WordEntry.IDX_NOUN)=='0'&&entry.getFeature(WordEntry.IDX_BUSA)=='1') {
        output.setPos(PatternConstants.POS_ETC);
        output.setPatn(PatternConstants.PTN_ADVJ);
      }
      if(entry.getCompounds().size()>1) output.addCNoun(entry.getCompounds());
    }else {
      if(success || MorphUtil.hasVerbSyllableOnly(stem)) return;
    }
    
//    NounUtil.confirmDNoun(output);
    
    candidates.add(output);

  }
  
  /**
   * 
   *  1. 사랑받다 : 체언 + 용언화접미사 + 어미 (PTN_NSM) <br>
   *  2. 사랑받아보다 : 체언 + 용언화접미사 + '아/어' + 보조용언 + 어미 (PTN_NSMXM) <br>
   *  3. 학교에서이다 : 체언 + '에서/부터/에서부터' + '이' + 어미 (PTN_NJCM) <br>
   *  4. 돕다 : 용언 + 어미 (PTN_VM) <br>
   *  5. 도움이다 : 용언 + '음/기' + '이' + 어미 (PTN_VMCM) <br>
   *  6. 도와주다 : 용언 + '아/어' + 보조용언 + 어미 (PTN_VMXM) <br>
   *  
   * @param stem  stem
   * @param end end
   * @param candidates  candidates
   * @throws MorphException exception 
   */
  public void analysisWithEomi(String stem, String end, List<AnalysisOutput> candidates) throws MorphException {
    
    String[] morphs = EomiUtil.splitEomi(stem, end);
    if(morphs[0]==null || ("어서의".equals(morphs[1]) && !"있".equals(morphs[0]))) return; // 어미가 사전에 등록되어 있지 않다면...., 어미(어서의)는 용어(있) 하고만 결합한다.

    String[] pomis = EomiUtil.splitPomi(morphs[0]);

    AnalysisOutput o = new AnalysisOutput(pomis[0],null,morphs[1],PatternConstants.PTN_VM);
    o.setPomi(pomis[1]);
  
    try {    

      WordEntry entry = DictionaryUtil.getVerb(o.getStem());  
      //System.out.println("end"+end+"o.getStem()"+o.getStem()+" ");
     
      //System.out.println();
      if(entry!=null&&!("을".equals(end)&&entry.getFeature(WordEntry.IDX_REGURA)==IrregularUtil.IRR_TYPE_LIUL)) {              
    	
    	AnalysisOutput output = o.clone();
    	
    	//if(!entry.getWord().equals("그치")){
    		output.setScore(AnalysisOutput.SCORE_CORRECT);//SCORE_SIM_CORRECT
    		//System.out.println(entry.getWord());
    	//}else{
    		//return;
    	//}
    	//map.put("dup", entry.getWord());
    	
        MorphUtil.buildPtnVM(output, candidates);
        
        char[] features = SyllableUtil.getFeature(stem.charAt(stem.length()-1)); // ㄹ불규칙일 경우
        if((features[SyllableUtil.IDX_YNPLN]=='0'||morphs[1].charAt(0)!='ㄴ')&&!"는".equals(end))   // "갈(V),는" 분석될 수 있도록
          return;
      }

      String[] irrs = IrregularUtil.restoreIrregularVerb(o.getStem(), o.getPomi()==null?o.getEomi():o.getPomi());

      if(irrs!=null) { // 불규칙동사인 경우
        AnalysisOutput output = o.clone();
        output.setStem(irrs[0]);
        
        if(output.getPomi()==null) {
          output.setEomi(irrs[1]);
        } else {
          output.setPomi(irrs[1]);
        }
        
        output.setScore(AnalysisOutput.SCORE_CORRECT);
        //output.setScore(AnalysisOutput.SCORE_SIM_CORRECT);
        MorphUtil.buildPtnVM(output, candidates);      
      }

      if(VerbUtil.ananlysisNSM(o.clone(), candidates)) return;
      
      if(VerbUtil.ananlysisNSMXM(o.clone(), candidates)) return;
      
      // [체언 + '에서/에서부터' + '이' +  어미]
      if(VerbUtil.ananlysisNJCM(o.clone(),candidates)) return;      
      
      if(VerbUtil.analysisVMCM(o.clone(),candidates)) return;  

      VerbUtil.analysisVMXM(o.clone(), candidates);
      
    } catch (CloneNotSupportedException e) {
      throw new MorphException(e.getMessage(),e);
    }
    
  }    
  
  public void analysisCNoun(List<AnalysisOutput> candidates) throws MorphException {
    
    boolean success = false;
    for(AnalysisOutput o: candidates) {
      if(o.getPos()!=PatternConstants.POS_NOUN) continue;
      if(o.getScore()==AnalysisOutput.SCORE_CORRECT) 
        success=true;
      else if(!success)
        confirmCNoun(o);
    }
  }
  
  /**
   * 복합명사인지 조사하고, 복합명사이면 단위명사들을 찾는다.
   * 복합명사인지 여부는 단위명사가 모두 사전에 있는지 여부로 판단한다.
   * 단위명사는 2글자 이상 단어에서만 찾는다.
   * @throws MorphException exception
   */
  public boolean confirmCNoun(AnalysisOutput o) throws MorphException  {
	  return confirmCNoun(o, false);
  }
  
  public boolean confirmCNoun(AnalysisOutput o, boolean existInDic) throws MorphException  {

    if(o.getScore()>=AnalysisOutput.SCORE_COMPOUNDS) 
    	return false;
        
    List<CompoundEntry> results = cnAnalyzer.analyze(o.getStem());
    
    int retSize = results.size();
    
    if(retSize>1 && NounUtil.DNouns.contains(results.get(retSize-1).getWord())) {
    	String dnoun = results.get(retSize-1).getWord();
    	
    	o.setStem(o.getStem().substring(0,o.getStem().length()-dnoun.length()));
    	o.setNsfx(dnoun);
    	
    	results.remove(retSize-1);
    	
    	if(retSize==2) {
    		o.setScore(AnalysisOutput.SCORE_CORRECT);
    		return true;
    	}
    }
    
    boolean hasOneWord = false;
    if(existInDic) {
        for(CompoundEntry ce : results) {
        	if(ce.getWord().length()==1) {
        		hasOneWord=true;
        		break;
        	}
        }
    }

    boolean success = false;
       
    if(results.size()>1 && !hasOneWord) {       
      o.setCNoun(results);
      success = true;
      int maxWordLen = 0;
      int dicWordLen = 0;
      for(CompoundEntry entry : results) {           
        if(!entry.isExist()) 
        {
          success = false;
        } 
        else 
        {
          if(entry.getWord().length()>maxWordLen) 
            maxWordLen = entry.getWord().length();
          dicWordLen += entry.getWord().length();
        }
      }
      o.setScore(AnalysisOutput.SCORE_COMPOUNDS);   
      o.setMaxWordLen(maxWordLen);
      o.setDicWordLen(dicWordLen);
    }
  
    if(success) {
      if(constraint(o)) {
        if(hasOneWord(o))
            o.setScore(AnalysisOutput.SCORE_SIM_CORRECT);
        else
            o.setScore(AnalysisOutput.SCORE_CORRECT);
      } else {
        o.setScore(AnalysisOutput.SCORE_FAIL);
        success = false;
      }
    } 
//    else {
//      if(NounUtil.confirmDNoun(o)&&o.getScore()!=AnalysisOutput.SCORE_CORRECT) {
//        confirmCNoun(o);
//      }
//      if(o.getScore()==AnalysisOutput.SCORE_CORRECT) success = true;
//      if(o.getCNounList().size()>0&&!constraint(o)) o.setScore(AnalysisOutput.SCORE_FAIL);
//    }

    return success;
       
  }

  private boolean hasOneWord(AnalysisOutput o) {
      List<CompoundEntry> entries = o.getCNounList();
      for(CompoundEntry entry : entries) {
          if(entry.getWord().length()==1 && !entry.isCompoundDic()) {
              return true;
          }
      }
      return false;
  }
     
  private boolean constraint(AnalysisOutput o) throws MorphException  {
       
    List<CompoundEntry> cnouns = o.getCNounList();
       
    if(o.getPatn()==PatternConstants.PTN_NSM) {         
      if("내".equals(o.getVsfx())&&cnouns.get(cnouns.size()-1).getWord().length()!=1) {
        WordEntry entry = DictionaryUtil.getWord(cnouns.get(cnouns.size()-1).getWord());
        if(entry!=null&&entry.getFeature(WordEntry.IDX_NE)=='0') return false;
      }         
    }
    
    return true;
  }
  
  /**
   * check if one letter exists in the compound entries
   * @param candidates
   */
  private void checkOneLetterInCNoun(List<AnalysisOutput> candidates) {
	  
	  if(divisibleOne) return;
	  
	  for(AnalysisOutput co : candidates) {
		  if(co.getCNounList().size()==0) continue;
		  
		  List<CompoundEntry> entries = co.getCNounList();
		  for(CompoundEntry ce : entries) {
			  if(ce.getWord().length()==1) {
				  co.getCNounList().clear();
				  break;
			  }
		  }
	  }
  }
  public static void main(String args[]) {
	   String term = "세상에서 제일 사랑스러웠음요";
		try {
			String[] inputs = new String[] {
					"금요일에"};
			
			/*String[] inputs = new String[] {
					"아버지가방에들어가신다"
				};*/
			
				SpEmiMorphAnalyzer analyzer = new SpEmiMorphAnalyzer();
				
				long start = 0;
				
				for(String input:inputs) 
				{
					List<AnalysisOutput> list = analyzer.analyze(input);
					
					for(AnalysisOutput o:list) 
					{
						System.out.print(o.toString()+" "+o.getPatn());
						System.out.print(",");
						//System.out.println("<"+o.getScore()+">");
					}
					
					if(start==0) start = System.currentTimeMillis();
					
				}
				
				//System.out.println((System.currentTimeMillis()-start)+"ms");
			/*MorphAnalyzer analyzer = new MorphAnalyzer();
			
			List<AnalysisOutput> outputs = analyzer.analyze(term);
			
			for(AnalysisOutput output : outputs) {
				System.out.println(output);
			}*/
				/*String text = "용수환경건물1층바닥거푸집설치";
				
				MorphAnalyzer analyzer = new MorphAnalyzer();
				
				List<AnalysisOutput> outputs = analyzer.analyze(text);
				
				for(AnalysisOutput o : outputs) {
					System.out.println(o);
				}*/
		}catch(Exception e) {
			e.printStackTrace();
		}
  }
}
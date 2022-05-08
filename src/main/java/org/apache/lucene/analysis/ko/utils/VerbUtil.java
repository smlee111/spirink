package org.apache.lucene.analysis.ko.utils;

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

import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.morph.PatternConstants;
import org.apache.lucene.analysis.ko.morph.WordEntry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class VerbUtil {

  public static final HashSet<String> verbSuffix = new HashSet<String>();
  
  public static final HashSet<String> XVerb = new HashSet<String>();
  
  public static final HashSet<String> wiAbbrevs = new HashSet<String>();
  
  static {
    String[] suffixs = {
        "이","하","되","내", "있","없","같","드리","받","주","짓","스럽","시키","당하","만하"};
    for(int i=0;i<suffixs.length;i++) verbSuffix.add(suffixs[i]);
    
    //어려워지다,어려워하다,주게되다,주게하다
    String[] xverbs = {"오","내","주","보","지","놓","가","오르","올리","두","되","하"};
    for(int i=0;i<xverbs.length;i++) XVerb.add(xverbs[i]);
    
    String[] eomis = {"고","거나", "다","란"}; // 이 축약이 일어나는 어미
    for(int i=0;i<eomis.length;i++) wiAbbrevs.add(eomis[i]);
  }
  
  /**
   * 어간이 용언화접미사로 끝나면 index 를 반환한다.  아니면 -1을 반환한다.
 * @throws MorphException 
   */
  public static int endsWithVerbSuffix(String stem) throws MorphException {
    int len = stem.length();
    if(len<2 || (len==2 && stem.charAt(0)=='수' && stem.charAt(1)=='있')) return -1;
    int start = 2;
    if(len==2) start = 1;      
    for(int i=start;i>0;i--) { // the most length of verb suffix is 2
      int idx = len-i;
      String suffix = stem.substring(idx);
      
      if(verbSuffix.contains(suffix)) {
    	  if((suffix.equals("당하") || suffix.equals("만하"))) {
    		  String noun = stem.substring(0, idx+1);
    		  if(DictionaryUtil.getAllNoun(noun)!=null) continue;
    	  }
    	  
    	  return idx;
      }
    }
    return -1;
  }
   
  /**
   * 어간부에 보조용언 [오,내,주,지]가 있는지 조사한다.
   */
  public static int endsWithXVerb(String stem) {
    int len = stem.length();
    if(len<2) return -1;
    int start = 2;
    if(len==2) start = 1;
    for(int i=start;i>0;i--) { //xverbs 의 가장 긴 글자수는 2이다.
      if(XVerb.contains(stem.substring(len-i))) return (len-i);
    }
    return -1;
  }
   
  public static boolean verbSuffix(String stem) {

    return verbSuffix.contains(stem);
     
  }
   
  public static boolean constraintVerb(String start, String end) {
     
    char[] schs = MorphUtil.decompose(start.charAt(start.length()-1));
    char[] echs = MorphUtil.decompose(end.charAt(0));
     
    if(schs.length==3&&schs[2]=='ㄹ'&&echs[0]=='ㄹ') return false;
     
    return true;
  }
   
  /**
   * 3. 학교에서이다 : 체언 + '에서/부터/에서부터' + '이' + 어미 (PTN_NJCM) <br>
   */
  public static boolean ananlysisNJCM(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {
 
    int strlen = o.getStem().length();
    boolean success = false;
     
    if(strlen>3&&(o.getStem().endsWith("에서이")||o.getStem().endsWith("부터이"))) {
      o.addElist(o.getStem().substring(strlen-1));
      o.setJosa(o.getStem().substring(strlen-3,strlen-1));
      o.setStem(o.getStem().substring(0,strlen-3));
      success = true;
    }else if(strlen>5&&(o.getStem().endsWith("에서부터이"))) {
      o.addElist(o.getStem().substring(strlen-1));
      o.setJosa(o.getStem().substring(strlen-5,strlen-1));
      o.setStem(o.getStem().substring(0,strlen-5));
      success = true;
    }
    if(!success) return false;
    
    WordEntry entry = null;
    if(success&&(entry=DictionaryUtil.getAllNoun(o.getStem()))!=null) { 
      if(entry.getFeature(WordEntry.IDX_NOUN)=='2') {
        o.setCNoun(entry.getCompounds());
      }
      o.setScore(AnalysisOutput.SCORE_CORRECT);
    }
     
    o.setPatn(PatternConstants.PTN_NJCM);
    o.setPos(PatternConstants.POS_NOUN);  
    candidates.add(o);
     
    return true;
  }
   
  /**
   * 어미부와 어간부가 분리된 상태에서 용언화접미사가 결합될 수 있는지 조사한다.
   * @param o  어미부와 어간부가 분리된 결과
   * @param candidates  candidates
   * @throws MorphException throw exception
   */
  public static boolean ananlysisNSM(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {

	if(o.getStem()==null || o.getStem().length()==0) return false;
	
    if(o.getStem().endsWith("스러우")) o.setStem(o.getStem().substring(0,o.getStem().length()-3)+"스럽");

    int idxVbSfix = VerbUtil.endsWithVerbSuffix(o.getStem());

    WordEntry entry =  DictionaryUtil.getWordExceptVerb(o.getStem());;
    
    char[] chrs = MorphUtil.decompose(o.getStem().charAt(o.getStem().length()-1));
    if(!wiAbbrevs.contains(o.getEomi())||entry==null) {
      if(idxVbSfix<1) return false;     
      o.setVsfx(o.getStem().substring(idxVbSfix));
      o.setStem(o.getStem().substring(0,idxVbSfix));
      
      if(o.getVsfx().equals("하"))
    	  entry = DictionaryUtil.getWordExceptVerb(o.getStem());
      else
    	  entry = DictionaryUtil.getWordExceptVerb(o.getStem());
    } else { // 이 축약인 경우
      if(entry==null || chrs.length==3) return false;
      o.setVsfx("이");
      o.setStem(o.getStem());
    }

    o.setPatn(PatternConstants.PTN_NSM);

    if(entry!=null && entry.getFeature(WordEntry.IDX_NOUN)=='0' && entry.getFeature(WordEntry.IDX_BUSA)=='1')
    	o.setPos(PatternConstants.POS_ETC);
    else
    	o.setPos(PatternConstants.POS_NOUN);
        
    if(entry!=null) {
      if(entry.getFeature(WordEntry.IDX_NOUN)=='0' && entry.getFeature(WordEntry.IDX_BUSA)=='0') return false;
      else if(o.getVsfx().equals("하")&&entry.getFeature(WordEntry.IDX_DOV)!='1') return false;
      else if(o.getVsfx().equals("되")&&entry.getFeature(WordEntry.IDX_BEV)!='1') return false;
      else if(o.getVsfx().equals("내")&&entry.getFeature(WordEntry.IDX_NE)!='1') return false;
      else if(o.getVsfx().equals("이")&&o.getEomi().equals("어")) return false;
      o.setScore(AnalysisOutput.SCORE_CORRECT); // '입니다'인 경우 인명 등 미등록어가 많이 발생되므로 분석성공으로 가정한다.      
    }else {
      o.setScore(AnalysisOutput.SCORE_ANALYSIS); // '입니다'인 경우 인명 등 미등록어가 많이 발생되므로 분석성공으로 가정한다.
    }
  
    candidates.add(o);

    return (o.getScore()==AnalysisOutput.SCORE_CORRECT);

  }
   
  public static boolean ananlysisNSMXM(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {
   
    int idxXVerb = VerbUtil.endsWithXVerb(o.getStem());
    if(idxXVerb==-1) return false;
    
    String eogan = o.getStem().substring(0,idxXVerb);
    String[] stomis = null;

    if((eogan.endsWith("아")||eogan.endsWith("어"))&&eogan.length()>1)
      stomis = EomiUtil.splitEomi(eogan.substring(0,eogan.length()-1),eogan.substring(eogan.length()-1));
    else
      stomis = EomiUtil.splitEomi(eogan,"");

    if(stomis[0]==null) return false;
    
    o.addElist(stomis[1]);
    int idxVbSfix = VerbUtil.endsWithVerbSuffix(stomis[0]);
    if(idxVbSfix==-1) return false;
    
    o.setXverb(o.getStem().substring(idxXVerb));
    o.setVsfx(stomis[0].substring(idxVbSfix));
    o.setStem(stomis[0].substring(0,idxVbSfix));
    o.setPatn(PatternConstants.PTN_NSMXM);
    o.setPos(PatternConstants.POS_NOUN);
    WordEntry entry = DictionaryUtil.getNoun(o.getStem());

    if(entry==null) return false;  
    
    if(o.getVsfx().equals("하")&&entry.getFeature(WordEntry.IDX_DOV)!='1') return false;
    if(o.getVsfx().equals("되")&&entry.getFeature(WordEntry.IDX_BEV)!='1') return false;        
    
    o.setScore(AnalysisOutput.SCORE_CORRECT);
    if(entry.getFeature(WordEntry.IDX_NOUN)=='2') {
      o.setCNoun(entry.getCompounds());
    }
    
    candidates.add(o);            
  
    return (o.getScore()==AnalysisOutput.SCORE_CORRECT);     
  }
   
  public static boolean analysisVMCM(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {
   
    int strlen = o.getStem().length();
     
    if(strlen<2) return false;
     
    if(!o.getStem().endsWith("이")) return false;
     
    char[] chrs = MorphUtil.decompose(o.getStem().charAt(strlen-2));
    boolean success = false;
  
    if(strlen>2&&o.getStem().endsWith("기이")) {
      o.setStem(o.getStem().substring(0,strlen-2));
      o.addElist("기");     
      success = true;       
    } else if(chrs.length>2&&chrs[2]=='ㅁ'){
      String[] eres = EomiUtil.splitEomi(o.getStem().substring(0,strlen-1), "");
      if(eres[0]==null) return false;
      
      o.addElist(eres[1]);       
      String[] irrs = IrregularUtil.restoreIrregularVerb(eres[0], eres[1]);
       
      if(irrs!=null) o.setStem(irrs[0]);
      else o.setStem(eres[0]);

      success = true;
    }
     
    if(success) {    
     
      o.addElist("이");
      if(DictionaryUtil.getVerb(o.getStem())!=null) {
        o.setPos(PatternConstants.POS_VERB);
        o.setPatn(PatternConstants.PTN_VMCM);
        o.setScore(AnalysisOutput.SCORE_CORRECT);
        candidates.add(o);
      }       
    }
     
    return (o.getScore()==AnalysisOutput.SCORE_CORRECT);
     
  }
   
  /**
   * 
   * 6. 도와주다 : 용언 + '아/어' + 보조용언 + 어미 (PTN_VMXM)
   * 
   */
  public static boolean analysisVMXM(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {

    int idxXVerb = VerbUtil.endsWithXVerb(o.getStem());

    if(idxXVerb==-1) return false;
      
    o.setXverb(o.getStem().substring(idxXVerb));
    
    String eogan = o.getStem().substring(0,idxXVerb);

    String[] stomis = analysisEomiByRule(eogan);
    if(stomis[0]==null) return false;
    
    o.setStem(stomis[0]);
    o.getElist().add(0, stomis[1]);
    
    o.setPos(PatternConstants.POS_VERB);
    o.setPatn(PatternConstants.PTN_VMXM);
    o.setScore(AnalysisOutput.SCORE_CORRECT);
    candidates.add(o);
    
    return true;  
  }
  
  private static String[] analysisEomiByRule(String eogan) throws MorphException {
	  
	    boolean eomiFlag = true;
	        
	    int strlen = eogan.length();
	    String[] irrs = null;
	    
	    String[] stomis =  EomiUtil.splitEomi(eogan, "");
	    
	    if(stomis[0]==null) 
	    	return new String[2];
	    else if(stomis[0]!=null && DictionaryUtil.getVerb(stomis[0])!=null) 
	    	return stomis;
	    
	    irrs = IrregularUtil.restoreIrregularVerb(stomis[0], stomis[1]);
	    if(irrs!=null) return irrs;
	    
	    for(int i=strlen-1;i>0;i--) {
	      
	      String stem = eogan.substring(0,i);
	      String eomi = eogan.substring(i); 
	      
	      stomis =  EomiUtil.splitEomi(stem, eomi);     
	      if(stomis[0]==null) 
	    	return new String[2];
	      else if(stomis[0]!=null && DictionaryUtil.getVerb(stomis[0])!=null) 
	    	return stomis;
	      
	      irrs = IrregularUtil.restoreIrregularVerb(stomis[0], stomis[1]);
	      if(irrs!=null) return irrs;
		    
	      char[] feature =  SyllableUtil.getFeature(eomi.charAt(0));   
	      if(eomiFlag&&feature[SyllableUtil.IDX_EOMI2]=='0') eomiFlag = false;
	      
	      if(!eomiFlag) break;
	    }
	    
	    return new String[2];
	  }
  
}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NounUtil {

  public static final Set<String> DNouns = new HashSet<String>();
    
  static {
    String[] strs = new String[]{"등", "들","상","간","뿐","별"};
    for(String str:strs) {
      DNouns.add(str);
    }
  };
    
  /**
   * 
   * 어간부가 음/기 로 끝나는 경우
   * 
   * @param o the analyzed output
   * @param candidates  candidates
   * @throws MorphException throw exception
   */
  public static boolean analysisMJ(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {

    int strlen = o.getStem().length();
       
    if(strlen<2 && !("함".equals(o.getStem()) || "됨".equals(o.getStem()))) return false;       

    char[] chrs = MorphUtil.decompose(o.getStem().charAt(strlen-1));
    boolean success = false;

    if(o.getStem().charAt(strlen-1)!='기'&&!(chrs.length==3&&chrs[2]=='ㅁ')) return false;

    String start = o.getStem();
    String end = "";
    if(o.getStem().charAt(strlen-1)=='기') {
      start = o.getStem().substring(0,strlen-1);
      end = "기";
    }else if(o.getStem().charAt(strlen-1)=='음') {
      start = o.getStem().substring(0,strlen-1);
      end = "음";
    }

    String[] eomis = EomiUtil.splitEomi(start, end);
    if(eomis[0]==null) return false;
    String[] pomis = EomiUtil.splitPomi(eomis[0]);
    o.setStem(pomis[0]);
    o.addElist(eomis[1]);       
    o.setPomi(pomis[1]);
     
    try {
      if(analysisVMJ(o.clone(),candidates)) return true;         
      if(analysisNSMJ(o.clone(),candidates)) return true;
      if(analysisVMXMJ(o.clone(),candidates)) return true;
    } catch (CloneNotSupportedException e) {
      throw new MorphException(e.getMessage(),e);
    }
              
    if(DictionaryUtil.getVerb(o.getStem())!=null) {
      o.setPos(PatternConstants.POS_VERB);
      o.setPatn(PatternConstants.PTN_VMJ);
      o.setScore(AnalysisOutput.SCORE_CORRECT);
      candidates.add(o);
      return true;
    }
     
    return false;
      
  }

  /**
   * 용언 + '음/기' + 조사(PTN_VMXMJ)
   * @param o the analyzed output
   * @param candidates  candidates
   * @throws MorphException throw exception
   */
  public static boolean analysisVMJ(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {

    String[] irrs =  IrregularUtil.restoreIrregularVerb(o.getStem(), o.getElist().get(0));
    if(irrs!=null) {
      o.setStem(irrs[0]);
      o.setElist(irrs[1],0);
    }
        
    if(DictionaryUtil.getVerb(o.getStem())!=null) {
      o.setPatn(PatternConstants.PTN_VMJ);
      o.setPos(PatternConstants.POS_VERB);
      o.setScore(AnalysisOutput.SCORE_CORRECT);
      candidates.add(o);
      return true;
    }
      
    return false;
  }
    
  /**
   * 용언 + '아/어' + 보조용언 + '음/기' + 조사(PTN_VMXMJ)
   * @param o the analyzed output
   * @param candidates  candidates
   * @throws MorphException throw exception
   */
  public static boolean analysisVMXMJ(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {
  
    int idxXVerb = VerbUtil.endsWithXVerb(o.getStem());

    if(idxXVerb!=-1) { // 2. 사랑받아보다
      String eogan = o.getStem().substring(0,idxXVerb);
      o.setXverb(o.getStem().substring(idxXVerb));

      String[] stomis = null;
      if(eogan.endsWith("아")||eogan.endsWith("어"))
        stomis = EomiUtil.splitEomi(eogan.substring(0,eogan.length()-1),eogan.substring(eogan.length()-1));
      else
        stomis = EomiUtil.splitEomi(eogan,"");
      if(stomis[0]==null) return false;
  
      String[] irrs =  IrregularUtil.restoreIrregularVerb(stomis[0], stomis[1]);
      if(irrs!=null) {
        o.setStem(irrs[0]);
        o.addElist(irrs[1]);
      }else {
        o.setStem(stomis[0]);
        o.addElist(stomis[1]);
      }
        
      if(DictionaryUtil.getVerb(o.getStem())!=null) {
        o.setPatn(PatternConstants.PTN_VMXMJ);
        o.setPos(PatternConstants.POS_VERB);
        o.setScore(AnalysisOutput.SCORE_CORRECT);
        candidates.add(o);
        return true;
      }else if(analysisNSMXMJ(o, candidates)){
        return true;          
      }

    }
      
    return false;
  }
    
  /**
   * 체언 + 용언화접미사 + '음/기' + 조사 (PTN_NSMJ)
   * @param o the analyzed output
   * @param candidates  candidates
   * @throws MorphException throw exception
   */
  public static boolean analysisNSMJ(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {

    int idxVbSfix = VerbUtil.endsWithVerbSuffix(o.getStem());        
    if(idxVbSfix==-1) return false;
    
    o.setVsfx(o.getStem().substring(idxVbSfix));
    o.setStem(o.getStem().substring(0,idxVbSfix));
    o.setPatn(PatternConstants.PTN_NSMJ);
    o.setPos(PatternConstants.POS_NOUN);
      
    WordEntry entry = DictionaryUtil.getWordExceptVerb(o.getStem());

    if(entry!=null) {
      if(entry.getFeature(WordEntry.IDX_NOUN)=='0') return false;
      else if(o.getVsfx().equals("하")&&entry.getFeature(WordEntry.IDX_DOV)!='1') return false;
      else if(o.getVsfx().equals("되")&&entry.getFeature(WordEntry.IDX_BEV)!='1') return false;
      else if(o.getVsfx().equals("내")&&entry.getFeature(WordEntry.IDX_NE)!='1') return false;
      o.setScore(AnalysisOutput.SCORE_CORRECT); // '입니다'인 경우 인명 등 미등록어가 많이 발생되므로 분석성공으로 가정한다.      
    }else {
      o.setScore(AnalysisOutput.SCORE_ANALYSIS); // '입니다'인 경우 인명 등 미등록어가 많이 발생되므로 분석성공으로 가정한다.
    }
    
    candidates.add(o);
      
    return true;
  }         
     
  public static boolean analysisNSMXMJ(AnalysisOutput o, List<AnalysisOutput> candidates) throws MorphException {

    int idxVbSfix = VerbUtil.endsWithVerbSuffix(o.getStem());        
    if(idxVbSfix==-1) return false;
    
    o.setVsfx(o.getStem().substring(idxVbSfix));
    o.setStem(o.getStem().substring(0,idxVbSfix));
    o.setPatn(PatternConstants.PTN_NSMXMJ);
    o.setPos(PatternConstants.POS_NOUN);
      
    WordEntry entry = DictionaryUtil.getWordExceptVerb(o.getStem());

    if(entry!=null) {
      if(entry.getFeature(WordEntry.IDX_NOUN)=='0') return false;
      else if(o.getVsfx().equals("하")&&entry.getFeature(WordEntry.IDX_DOV)!='1') return false;
      else if(o.getVsfx().equals("되")&&entry.getFeature(WordEntry.IDX_BEV)!='1') return false;
      else if(o.getVsfx().equals("내")&&entry.getFeature(WordEntry.IDX_NE)!='1') return false;
      o.setScore(AnalysisOutput.SCORE_CORRECT); // '입니다'인 경우 인명 등 미등록어가 많이 발생되므로 분석성공으로 가정한다.      
    }else {
      o.setScore(AnalysisOutput.SCORE_ANALYSIS); // '입니다'인 경우 인명 등 미등록어가 많이 발생되므로 분석성공으로 가정한다.
    }
    
    candidates.add(o);
      
    return true;
  }
 
    
  /*
     * 마지막 음절이 명사형 접미사(등,상..)인지 조사한다.
     */
  public static boolean confirmDNoun(AnalysisOutput output) throws MorphException {

    int strlen = output.getStem().length();
    if(strlen<2) return false;
    
    String d = Character.toString(output.getStem().charAt(strlen-1));      
    if(!DNouns.contains(d)) return false;

    String s = output.getStem().substring(0, strlen-1);
    output.setNsfx(d);
    output.setStem(s);
          
    WordEntry cnoun = DictionaryUtil.getWordExceptVerb(s);
    if(cnoun != null)  {
      if(cnoun.getFeature(WordEntry.IDX_NOUN)=='2') {
        output.setCNoun(cnoun.getCompounds());
      } else {
    	if(cnoun.getFeature(WordEntry.IDX_NOUN)!='1')
    		output.setPos(PatternConstants.POS_ETC);
    	
        output.setCNoun(Collections.EMPTY_LIST);
      }
      
      
      output.setScore(AnalysisOutput.SCORE_CORRECT);
    }
          
    return true;
  }
      
  public static boolean endsWith2Josa(String input) throws MorphException {

    boolean josaFlag = true;
    for(int i=input.length()-2;i>0;i--) {
        
      String josa = input.substring(i);

      char[] feature =  SyllableUtil.getFeature(josa.charAt(0));    
      if(josaFlag&&DictionaryUtil.existJosa(josa)) return true;
  
        
      if(josaFlag&&feature[SyllableUtil.IDX_JOSA2]=='0') josaFlag = false;        
      if(!josaFlag) break;
    }
      
    return false;
  }
      
  public static double countFoundNouns(AnalysisOutput o) {
    int count = 0;
    for(int i=0;i<o.getCNounList().size();i++) {
      if(o.getCNounList().get(i).isExist()) count++;
    }
    return (count*100)/o.getCNounList().size();
  }
}

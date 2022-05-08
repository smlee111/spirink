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

import org.apache.lucene.analysis.ko.morph.CompoundEntry;
import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.morph.WordEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DictionaryUtil {
  
  private static Trie<String,WordEntry> dictionary;
  
  private static HashMap<String, String> josas;
  
  private static HashMap<String, String> eomis;
  
  private static HashMap<String, String> prefixs;
  
  private static HashMap<String, String> suffixs;
  
  private static HashMap<String,WordEntry> uncompounds;
  
  private static HashMap<String, String> cjwords;
  
  private static HashMap<String, String> abbreviations;
  
  /**
   * 사전을 로드한다.
   */
  public synchronized static void loadDictionary() throws MorphException {
    
    dictionary = new Trie<String, WordEntry>(true);
    List<String> strList = null;
    List<String> compounds = null;
    List<String> abbrevs = null;
    try {
      strList = FileUtil.readLines(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_DICTIONARY),"UTF-8");
      strList.addAll(FileUtil.readLines(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_EXTENSION),"UTF-8"));
      compounds = FileUtil.readLines(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_COMPOUNDS),"UTF-8"); 
      abbrevs = FileUtil.readLines(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_ABBREV),"UTF-8"); 
    } catch (IOException e) {      
      new MorphException(e.getMessage(),e);
    } catch (Exception e) {
      new MorphException(e.getMessage(),e);
    }
    if(strList==null) throw new MorphException("dictionary is null");;
    
    for(String str:strList) {
      String[] infos = str.split("[,]+"); //기본사전과 확장사전 배열을 만든다.
      if(infos.length!=2) continue; //형식이 맞지않으면 건너띈다.
      infos[1] = infos[1].trim(); //공백제거
      if(infos[1].length()==6) infos[1] = infos[1].substring(0,5)+"000"+infos[1].substring(5); //형식에 맞지않으면 형식을 맞춘다. 예전버전인듯
      
      WordEntry entry = new WordEntry(infos[0].trim(),infos[1].trim().toCharArray());//하나씩 entry를 만든다
      dictionary.add(entry.getWord(), entry);//사전에 추가한다.
    }
    
    for(String compound: compounds) //복합어사전작업
    {    
      String[] infos = compound.split("[:]+");
      if(infos.length!=3&&infos.length!=2) continue;
      
      WordEntry entry = null;
      if(infos.length==2) 
        entry = new WordEntry(infos[0].trim(),"200000000X".toCharArray());
      else 
        entry = new WordEntry(infos[0].trim(),("200"+infos[2]+"00X").toCharArray());
      
      entry.setCompounds(compoundArrayToList(infos[1], infos[1].split("[,]+")));
      dictionary.add(entry.getWord(), entry);
    }
    
    abbreviations = new HashMap();
    
    for(String abbrev: abbrevs)
    {    
      String[] infos = abbrev.split("[:]+");
      if(infos.length!=2) continue;      
      abbreviations.put(infos[0].trim(), infos[1].trim());
    }
  }

  @SuppressWarnings({"rawtypes","unchecked"})
  public static Iterator<WordEntry> findWithPrefix(String prefix) throws MorphException {
    if(dictionary==null) loadDictionary();
    return dictionary.getPrefixedBy(prefix);
  }

  public static WordEntry getWord(String key)  {    
   
	try {
		if(dictionary==null) loadDictionary();
	    if(key.length()==0) return null;
	    
	    return (WordEntry)dictionary.get(key);
	} catch (MorphException e) {
		throw new RuntimeException(e);
	}

  }

  public static void addEntry(WordEntry entry) {
      try {
           if(dictionary==null) loadDictionary();
           dictionary.add(entry.getWord(), entry);
      } catch (MorphException e) {
          throw new RuntimeException(e);
      }
  }

  public static WordEntry getWordExceptVerb(String key) throws MorphException {    
    WordEntry entry = getWord(key);    
    if(entry==null) return null;
    
    if(entry.getFeature(WordEntry.IDX_NOUN)=='1'||
        entry.getFeature(WordEntry.IDX_NOUN)=='2'||
        entry.getFeature(WordEntry.IDX_BUSA)=='1')
      return entry;
    
    return null;
  }
  
  public static WordEntry getNoun(String key) throws MorphException {  

    WordEntry entry = getWord(key);
    if(entry==null) return null;
    
    if(entry.getFeature(WordEntry.IDX_NOUN)=='1') return entry;
    return null;
  }
  
  /**
   * 
   * return all noun including compound noun
   * @param key the lookup key text
   * @return  WordEntry
   * @throws MorphException throw exception
   */
  public static WordEntry getAllNoun(String key) throws MorphException {  

    WordEntry entry = getWord(key);
    if(entry==null) return null;

    if(entry.getFeature(WordEntry.IDX_NOUN)=='1' || entry.getFeature(WordEntry.IDX_NOUN)=='2') return entry;
    return null;
  }
  
  
  public static WordEntry getVerb(String key) throws MorphException {
    
    WordEntry entry = getWord(key);  
    if(entry==null) return null;

    if(entry.getFeature(WordEntry.IDX_VERB)=='1') {
      return entry;
    }
    return null;
  }
  
  public static WordEntry getAdverb(String key) throws MorphException {
    WordEntry entry = getWord(key);
    if(entry==null) return null;

    if(entry.getFeature(WordEntry.IDX_BUSA)=='1') return entry;
    return null;
  }
  
  public static WordEntry getBusa(String key) throws MorphException {
    WordEntry entry = getWord(key);
    if(entry==null) return null;

    if(entry.getFeature(WordEntry.IDX_BUSA)=='1') return entry;
    return null;
  }
  
  public static WordEntry getIrrVerb(String key, char irrType) throws MorphException {
    WordEntry entry = getWord(key);
    if(entry==null) return null;

    if(entry.getFeature(WordEntry.IDX_VERB)=='1'&&
        entry.getFeature(WordEntry.IDX_REGURA)==irrType) return entry;
    return null;
  }
  
  public static WordEntry getBeVerb(String key) throws MorphException {
    WordEntry entry = getWord(key);
    if(entry==null) return null;
    
    if(entry.getFeature(WordEntry.IDX_BEV)=='1') return entry;
    return null;
  }
  
  public static WordEntry getDoVerb(String key) throws MorphException {
    WordEntry entry = getWord(key);
    if(entry==null) return null;
    
    if(entry.getFeature(WordEntry.IDX_DOV)=='1') return entry;
    return null;
  }
  
  public static String getAbbrevMorph(String key) throws MorphException {
    if(abbreviations==null) loadDictionary();    
    return abbreviations.get(key);
  }
  
  public synchronized static WordEntry getUncompound(String key) throws MorphException {
    
    try {
      if(uncompounds==null) {
        uncompounds = new HashMap<String,WordEntry>();
        List<String> lines = FileUtil.readLines(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_UNCOMPOUNDS),"UTF-8");  
        for(String compound: lines) {    
          String[] infos = compound.split("[:]+");
          if(infos.length!=2) continue;
          WordEntry entry = new WordEntry(infos[0].trim(),"90000X".toCharArray());
          entry.setCompounds(compoundArrayToList(infos[1], infos[1].split("[,]+")));
          uncompounds.put(entry.getWord(), entry);
        }      
      }  
    }catch(Exception e) {
      throw new MorphException(e);
    }
    return uncompounds.get(key);
  }
  
  public synchronized static String getCJWord(String key) throws MorphException {
    
    try {
      if(cjwords==null) {
        cjwords = new HashMap<String, String>();
        List<String> lines = FileUtil.readLines(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_CJ),"UTF-8");  
        for(String cj: lines) {    
          String[] infos = cj.split("[:]+");
          if(infos.length!=2) continue;
          cjwords.put(infos[0], infos[1]);
        }      
      }  
    }catch(Exception e) {
      throw new MorphException(e);
    }
    return cjwords.get(key);
    
  }
  
  public static boolean existJosa(String str) throws MorphException {
    if(josas==null) {
      josas = new HashMap<String, String>();
      readFile(josas,KoreanEnv.FILE_JOSA);
    }
    //System.out.println("str"+str+"josas.get(str)"+josas.get(str));
    if(josas.get(str)==null) return false;
    else return true;
  }
  
  public static boolean existEomi(String str)  throws MorphException {
    if(eomis==null) {
      eomis = new HashMap<String, String>();
      readFile(eomis,KoreanEnv.FILE_EOMI);
    }

    if(eomis.get(str)==null) return false;
    else return true;
  }
  
  public static String getJosa(String str) throws MorphException {
    if(josas==null) {
      josas = new HashMap<String, String>();
      readFile(josas,KoreanEnv.FILE_JOSA);
    }  
    return josas.get(str);
  }
  
  public static String getEomi(String str)  throws MorphException {
    if(eomis==null) {
      eomis = new HashMap<String, String>();
      readFile(eomis,KoreanEnv.FILE_EOMI);
    }

    return eomis.get(str);
  }
	  
  public static boolean existPrefix(String str)  throws MorphException {
    if(prefixs==null) {
      prefixs = new HashMap<String, String>();
      readFile(prefixs,KoreanEnv.FILE_PREFIX);
    }
    
    if(prefixs.get(str)==null) return false;
    else return true;
  }
  
  public static boolean existSuffix(String str)  throws MorphException {
    if(suffixs==null) {
      suffixs = new HashMap<String, String>();
      readFile(suffixs,KoreanEnv.FILE_SUFFIX);
    }

    if(suffixs.get(str)!=null) return true;
    
    return false;
  }
  
  /**
   * ㄴ,ㄹ,ㅁ,ㅂ과 eomi 가 결합하여 어미가 될 수 있는지 점검한다.
   */
  public static String combineAndEomiCheck(char s, String eomi) throws MorphException {
  
    if(eomi==null) eomi="";

    if(s=='ㄴ') eomi = "은"+eomi;
    else if(s=='ㄹ') eomi = "을"+eomi;
    else if(s=='ㅁ') eomi = "음"+eomi;
    else if(s=='ㅂ') eomi = "습"+eomi;
    else eomi = s+eomi;

    if(existEomi(eomi)) return eomi;    

    return null;
    
  }
  
  /**
   * modified at 2017-09-19 by smlee
   * @param map map
   * @param dic  1: josa, 2: eomi
   * @throws MorphException excepton
   */
  private static synchronized void readFile(HashMap<String, String> map, String dic) throws MorphException {    
    
    String path = KoreanEnv.getInstance().getValue(dic);

    try{
      List<String> lines = FileUtil.readLines(path,"UTF-8");
      
      for(int i=1;i<lines.size();i++) {
    	String word_string = lines.get(i).trim();
    	String[] fields = word_string.split(",");

    	if(fields.length==2) {
    		map.put(fields[0].trim(), fields[1].trim());
    		/*if(fields[0].trim().equals("보다")){
    			System.out.println("fields[0].trim()"+fields[0].trim());
    		}*/
    	} else {
    		map.put(word_string, word_string);
    	}
      }
    }catch(IOException e) {
      throw new MorphException(e.getMessage(),e);
    } catch (Exception e) {
      throw new MorphException(e.getMessage(),e);
    }
  }
  
  
  private static List<CompoundEntry> compoundArrayToList(String source, String[] arr) {
    List<CompoundEntry> list = new ArrayList<CompoundEntry>();
    for(String str: arr) {
      CompoundEntry ce = new CompoundEntry(str);
      ce.setOffset(source.indexOf(str));
      ce.setCompoundDic(true);
      list.add(ce);
    }
    return list;
  }
  //사전테스트부터 잘해보자
  public static void main(String args[]){
	  try{
	  if(dictionary==null) loadDictionary(); //사전을 로드한다.
	  
	  }catch(Exception e){
		  e.printStackTrace();
	  }
  }
}

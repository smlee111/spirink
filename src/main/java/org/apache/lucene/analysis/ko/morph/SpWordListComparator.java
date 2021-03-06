package org.apache.lucene.analysis.ko.morph;

import java.util.Comparator;
import java.util.List;

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

public class SpWordListComparator implements Comparator<WordListCandidate> {
  
  public int compare(WordListCandidate o1, WordListCandidate o2) {
    
    int lenC1 = o1.getCorrectLength();
    int lenC2 = o2.getCorrectLength();
   
    int noV1 = o1.getVerbCount();
    int noV2 = o2.getVerbCount();
    
    int noUK1 = o1.getUnknownCount();
    int noUK2 = o2.getUnknownCount();
    
    int size1 = o1.getWordList().size();
    int size2 = o2.getWordList().size();
    
    int ptbn1 =o1.getPtnCount();
    int ptbn2 =o2.getPtnCount();
    
    int firstLength1 = o1.getFirstFragment().length();
    int firstLength2 = o2.getFirstFragment().length();
    //SpUtilities.outLine("lenC1:",lenC1+"lenC2:"+lenC2);
    //System.out.println("WordListCandidate o1"+o1.getFirstFragment()+lenC1);
    //System.out.println("WordListCandidate o2"+o2.getFirstFragment()+lenC2);
    
    if(lenC1 != lenC2) return lenC2-lenC1;
    
    /*if(size1 != size2) return size1 - size2;
    if(noV1!=noV2) return noV2-noV1; //원본
*/  
    //if(ptbn1 != ptbn2) return ptbn1 - ptbn2;//순서바꿈테스트
    if(size1 != size2) return size1 - size2;//순서바꿈테스트
    if(firstLength1 != firstLength2) return firstLength1 - firstLength2;//순서바꿈테스트
    //System.out.println("WordListCandidate o1"+o1.getFirstFragment()+"noV1"+noV1);
    //System.out.println("WordListCandidate o2"+o2.getFirstFragment()+"noV2"+noV2);
    //if(noV1!=noV2) return noV2-noV1; //순서바꿈테스트
    //if(size1 != size2) return size1 - size2;//순서바꿈테스트
    //if(noUK1!=noUK2) return noUK1-noUK2;
    //if(size1 != size2) return size1 - size2;//순서바꿈테스트
    return compareLength(o1.getWordList(), o2.getWordList());//원본
    //return 0;
  }
  
  private int compareLength(List<List<AnalysisOutput>> wordList1, 
      List<List<AnalysisOutput>> wordList2) 
  {
    
    for(int jj=0;jj<wordList1.size();jj++) {
      
      if(wordList2.size()<=jj) return -1;
      
      int length1 = wordList1.get(jj).get(0).getSource().length();
      int length2 = wordList2.get(jj).get(0).getSource().length();
      //SpUtilities.outLine("===>",wordList1.get(jj).get(0).getSource()+length1+wordList2.get(jj).get(0).getSource()+length2);
      if(length1 != length2) return length2-length1;//원본
      if(length1 != length2) return length1-length2;
    }
    
    return 0;
  }
}

package org.apache.lucene.analysis.ko;

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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.MorphAnalyzer;
import org.apache.lucene.analysis.ko.morph.PatternConstants;
import org.apache.lucene.analysis.ko.morph.WordSegmentAnalyzer;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

/**
 * A Korean Analyzer
 */
public class SpKoreanAnalyzer extends StopwordAnalyzerBase {
  
  /** An unmodifiable set containing some common English words that are not usually useful
  for searching.*/
  public static final CharArraySet ENGLISH_STOP_WORDS_SET;
  
  static {
    final List<String> stopWords = Arrays.asList(
      "a", "an", "and", "are", "as", "at", "be", "but", "by",
      "for", "if", "in", "into", "is", "it",
      "no", "not", "of", "on", "or", "such",
      "that", "the", "their", "then", "there", "these",
      "they", "this", "to", "was", "will", "with"
    );
    final CharArraySet stopSet = new CharArraySet(stopWords, false);
    ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet); 
  }

  /*private boolean bigrammable = false;
    
  private boolean hasOrigin = false;
    
  private boolean exactMatch = false;
  private boolean originCNoun = true;
  private boolean queryMode = false;
  private boolean wordSegment = false;*/
  

  private boolean bigrammable = false;
  
  private boolean hasOrigin = false;
    
  private boolean exactMatch = false;
  private boolean originCNoun = false;
  private boolean queryMode = false;
  private boolean wordSegment = true;
  
  @Override
  protected TokenStreamComponents createComponents(final String fieldName) {
    final SpKoreanTokenizer src = new SpKoreanTokenizer();
    TokenStream tok = new LowerCaseFilter(src);
    tok = new ClassicFilter(tok);
    tok = new SpKoreanFilter(tok, bigrammable, hasOrigin, exactMatch, originCNoun, queryMode);
    if(wordSegment) tok = new SpWordSegmentFilter(tok, hasOrigin);//이것만 원본이용한다.
    tok = new HanjaMappingFilter(tok);
    tok = new PunctuationDelimitFilter(tok);
    tok = new StopFilter(tok, stopwords);
    
    return new TokenStreamComponents(src, tok) {
      @Override
      protected void setReader(final Reader reader) {
        super.setReader(reader);
      }
    };
	    
  }
    
  /**
   * determine whether the bigram index term is returned or not if a input word is failed to analysis
   * If true is set, the bigram index term is returned. If false is set, the bigram index term is not returned.
   */
  public void setBigrammable(boolean is) {
    bigrammable = is;
  }
  
  /**
   * determin whether the original term is returned or not if a input word is analyzed morphically.
   */
  public void setHasOrigin(boolean has) {
    hasOrigin = has;
  }

  /**
   * determin whether the original compound noun is returned or not if a input word is analyzed morphically.
   */
  public void setOriginCNoun(boolean cnoun) {
    originCNoun = cnoun;
  }
  
  /**
   * determin whether the original compound noun is returned or not if a input word is analyzed morphically.
   */
  public void setExactMatch(boolean exact) {
    exactMatch = exact;
  }
  
  /**
   * determin whether the analyzer is running for a query processing
   */
  public void setQueryMode(boolean mode) {
    queryMode = mode;
  }

  /**
   * determin whether word segment analyzer is processing
   */
	public boolean isWordSegment() {
		return wordSegment;
	}
	
	public void setWordSegment(boolean wordSegment) {
		this.wordSegment = wordSegment;
	}
	
	public static void main(String args[]) {
		  /* String source="테스트 입니다";
		   MorphAnalyzer analyzer = new MorphAnalyzer();
		   StringBuilder result = new StringBuilder();
		   StringTokenizer stok = new StringTokenizer(source, " ");*/
		  TokenStream ts = null ;
		    //String szText="그 친구는 정말 사랑스러움요";
		    String szText="그 친구는 정말 사랑 스럽음요";
		    
			String output = "" ;
			try {
				KoreanAnalyzer analyzer = new KoreanAnalyzer() ;
				MorphAnalyzer morph = new MorphAnalyzer() ;
				ts = analyzer.tokenStream(null,  new StringReader(szText)) ;
			    CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class) ;
			    PositionIncrementAttribute posIncrAtt = ts.addAttribute(PositionIncrementAttribute.class);
			    PositionLengthAttribute posLenAtt = ts.addAttribute(PositionLengthAttribute.class);
			    TypeAttribute typeAtt = ts.addAttribute(TypeAttribute.class);
			    OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
			    MorphemeAttribute morphAtt = ts.addAttribute(MorphemeAttribute.class);																		
				
			    List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
			    
				ts.reset() ;
				
				while(ts.incrementToken()) {
					String token = termAtt.toString();
					
					int cnt=0;										
					List<AnalysisOutput> results = morph.analyze(token) ;
					System.out.println("=="+cnt+" "+results.toString());
					//for(AnalysisOutput o : results){
					for(int i=0;i<results.size();i++) {
						//if(i==results.size()-1) {
							AnalysisOutput o = results.get(i);
							if(offsetAtt.startOffset()==0) {
								analyList.clear();
								output = "[" + o.getPatn() + ":" + o.toString() +"]";
							}else {
								output += "[" + o.getPatn() + ":" + o.toString() +"]";
							}
							
							HashMap<String, String> hmap = new HashMap<String, String>();
							hmap.put("anycont", o.toString());
							hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
							hmap.put("anynum", String.valueOf(o.getPatn()));
							//System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn()));
							analyList.add(hmap);
						//}
						//cnt++;
					}
					
					
				}
				//System.out.println(szText) ;
				System.out.println(analyList.toString()) ;
				ts.end() ;			
			}
			catch(Exception e) {
				System.out.println(szText + ">> Exception : " + e.getMessage()) ;
			}
			finally {
				if( ts != null ) try { ts.close() ; } catch(Exception ee){}
			}
	  }
	public Vector<String> wordSegment2(String source) {

		Vector<String> inputs = new Vector<String>();
		try {
		WordSegmentAnalyzer ws = new WordSegmentAnalyzer();
		StringBuilder result = new StringBuilder();
		String s = source.replace(" ","");
		//System.out.println("s=="+s);
		List<List<AnalysisOutput>> outList = ws.analyze(s); //이부분이 분석이다
		//System.out.println(outList.toString());
		for(List<AnalysisOutput> o: outList) {
			for(AnalysisOutput analysisOutput : o) {
				//result.append(analysisOutput.getSource()).append(" ");
				inputs.add(analysisOutput.getSource());
			}
		}
		System.out.println(result.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return inputs;
	}
	public static void main2(String args[]) {
		    TokenStream ts = null ;
		    String szText="그 친구는 정말 사랑스러움요";
			String output = "" ;
			try {
				KoreanAnalyzer analyzer = new KoreanAnalyzer() ;
				ts = analyzer.tokenStream(null,  new StringReader(szText)) ;
			    CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class) ;
				ts.reset() ;
				while(ts.incrementToken()) {
					String token = termAtt.toString();
					System.out.println("token"+token);
				}
				
				ts.end() ;			
			}
			catch(Exception e) {
				System.out.println(szText + ">> Exception : " + e.getMessage()) ;
			}
			finally {
				if( ts != null ) try { ts.close() ; } catch(Exception ee){}
			}
	  }
	public static void main6(String args[]) {
		KoreanAnalyzer analyzer = new KoreanAnalyzer();
		String[] sources = new String[]{"그", "친구는", "정말", "사랑스러움요"};
		try {
	    for (String source : sources) {
	      TokenStream stream = analyzer.tokenStream(null, new StringReader(source));
	      
	      CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
	      PositionIncrementAttribute posIncrAtt = stream.addAttribute(PositionIncrementAttribute.class);
	      PositionLengthAttribute posLenAtt = stream.addAttribute(PositionLengthAttribute.class);
	      TypeAttribute typeAtt = stream.addAttribute(TypeAttribute.class);
	      OffsetAttribute offsetAtt = stream.addAttribute(OffsetAttribute.class);
	      MorphemeAttribute morphAtt = stream.addAttribute(MorphemeAttribute.class);
	      stream.reset();

	      while (stream.incrementToken()) {
	        System.out.println(termAtt.toString() + ":" + posIncrAtt.getPositionIncrement() + "(" + offsetAtt.startOffset() + "," + offsetAtt.endOffset() + ")");
	      }
	      stream.close();
	    }
	    }catch(Exception e) {
	    	
	    }
	  }
}

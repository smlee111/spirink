package org.apache.lucene.analysis.ko.morph;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.ko.sp.SpUtilities;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;

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

public class AbbrevFinder {
  
  public List<AnalysisOutput> find(String input) throws MorphException {
    
	  String definitions = DictionaryUtil.getAbbrevMorph(input);
	  //SpUtilities.outLine("definitions===>",definitions);
	  if(definitions==null) return null;
	  
	  List<AnalysisOutput> outputs = buildAnalysisOutputs(input, definitions);
	  
	  if(outputs.size()==0)
		  return null;
	  else
		  return outputs;
  }
  
  
  private List<AnalysisOutput> buildAnalysisOutputs(String input, String definitions) {
	  List<AnalysisOutput> outputs = new ArrayList<AnalysisOutput>();
	  
	  String[] definitionArr =  definitions.split(",");
	  
	  for(String definition : definitionArr) {
		  AnalysisOutput o = new AnalysisOutput();
		  o.setScore(AnalysisOutput.SCORE_CORRECT);
		  
		  String[] morphemes =  definition.split("\\-");
		  int end = morphemes.length-1;
		  
		  populateOutput(o, morphemes, end);
		  
		  o.setPatn(Integer.parseInt(morphemes[end]));  
		  o.setSource(input);
		  
		  outputs.add(o);
	  }
	  
	  return outputs;
  }


  /**
   * 개별분석 값을 세팅한다.
   * @param o
   * @param morphemes
   * @param end
   */
  private void populateOutput(AnalysisOutput o, String[] morphemes, int end) {
	for(int i=0;i<end;i++) {
		  String morpheme = morphemes[i];
		  
		  String[] morphDef = morpheme.split("/");
		  if(morphDef.length!=2 || morphDef[0].length()==0 
				  || morphDef[1].length()==0) 
			  continue;
		  
		  switch(morphDef[1].charAt(0)) 
		  {
		  	case PatternConstants.POS_VERB: //V
		  		o.setStem(morphDef[0].trim());
				o.setPos(PatternConstants.POS_VERB );
				continue;
		  	case PatternConstants.POS_NOUN: //N
		  		o.setStem(morphDef[0].trim());
				o.setPos(PatternConstants.POS_NOUN );
				continue;
		  	case PatternConstants.POS_ETC: //Z
		  		o.setStem(morphDef[0].trim());
				o.setPos(PatternConstants.POS_ETC );
				continue;
		  	case PatternConstants.POS_EOMI: //e
		  		o.setEomi(morphDef[0].trim());
		  	case PatternConstants.POS_JOSA: //j
		  		o.setJosa(morphDef[0].trim());
				continue;
		  	case PatternConstants.POS_SFX_N: //s
		  		o.setNsfx(morphDef[0].trim());
				continue;
		  	case PatternConstants.POS_SFX_V: //t
		  		o.setVsfx(morphDef[0].trim());
				continue;
		  	case PatternConstants.POS_PEOMI: //f
		  		o.setPomi(morphDef[0].trim());
				continue;
		  	case PatternConstants.POS_NEOMI: //n
		  		o.addElist(morphDef[0].trim());
				continue;
		  	case PatternConstants.POS_COPULA: //c
		  		o.setXverb(morphDef[0].trim());
				continue;
			default:
		  }
	  }
	}
}

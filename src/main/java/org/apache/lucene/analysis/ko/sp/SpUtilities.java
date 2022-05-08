package org.apache.lucene.analysis.ko.sp;

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

public class SpUtilities {

  public static String arrayToString(String[] strs) {
    StringBuffer sb = new StringBuffer();
    for(String str:strs) {
      sb.append(str);
    }
    return sb.toString();
  }
  
  public static AnalysisOutput cloneOutput(AnalysisOutput o) throws MorphException {
    try {
      return o.clone();
    } catch (CloneNotSupportedException e) {
      throw new MorphException(e.getMessage(),e);
    }
  }
  
  public static void outLine(String position,String str) {
	  System.out.println(position+"===>"+str);
  }
  public static void outLine(String position,int str) {
	  System.out.println(position+"===>"+str);
  }
  public static String getSystemProperty(String property) {
    try {
      return System.getProperty(property);
    } catch (SecurityException ex) {
      // we are not allowed to look at this property
      System.err.println("Caught a SecurityException reading the system property '" + property
          + "'; the SystemUtils property value will default to null.");
      return null;
    }
  }  
}

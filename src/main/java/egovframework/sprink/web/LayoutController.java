/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.sprink.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.WordAnalyService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


import javax.annotation.Resource;


import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.PatternConstants;
import org.apache.lucene.analysis.ko.sp.SpMorphAnalyzer;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.apache.lucene.analysis.ko.utils.FileUtil;
import org.apache.lucene.analysis.ko.utils.KoreanEnv;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
public class LayoutController {

	/** EgovSampleService */
	@Resource(name = "wordAnalyService")
	private WordAnalyService wordAnalyService;
	
	/** EgovSampleService */
	@Resource(name = "sampleService")
	private EgovSampleService sampleService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	/**
	 * 글 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/list.do")
	public String list(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {

		/** EgovPropertyService.sample */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> sampleList = sampleService.selectSampleList(searchVO);
		model.addAttribute("resultList", sampleList);

		int totCnt = sampleService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		System.out.println("======="+paginationInfo.toString());
		return "example/temp/list";
	}
	/**
	 * 테스트목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/test_list.do")
	public String test_list(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {

		/** EgovPropertyService.sample */
		searchVO.setPageUnit(100);
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		System.out.println("searchVO==>"+searchVO.getSearchKeyword());
		List<?> sampleList = sampleService.selectTestList(searchVO);
		
		model.addAttribute("resultList", sampleList); 

		int totCnt = sampleService.selectTestListTotCnt(searchVO);
		int ansCnt = sampleService.selectTestAnsTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		float sucRate = ((float)ansCnt/totCnt)*100;
		HashMap<String, String> staMap = new HashMap<String, String>();
		
		model.put("sucRate", String.valueOf(sucRate));
		return "example/temp/test_list";
	}
	
	/**
	 * 테스트를 시작 한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/exec_test.do")
	public String exec_test(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {

		SpMorphAnalyzer analy = new SpMorphAnalyzer();
		List<?> sampleList = sampleService.selectTestTotList();
		for(Object map:sampleList){
			//int sty_idx = Integer.valueOf((String)((HashMap)map).get("sty_idx"));
			HashMap<String,Object> hmap = (HashMap)map;
			Object obj = hmap.get("sty_idx");
			int sty_idx =  (int)obj;
			String sty_type = (String)((HashMap)map).get("sty_type");
			String sty_quest = (String)((HashMap)map).get("sty_quest");
			String sty_answer = (String)((HashMap)map).get("sty_answer");
			
			boolean inGative=false;
			String upKeyword = sty_quest;
			if(sty_quest.indexOf("?")!=-1 && sty_quest.indexOf("?")==sty_quest.length()-1){ //가장마지막에 ?표 표시확인
				inGative = true;
				upKeyword= sty_quest.substring(0,sty_quest.length()-1);
			}else{
				inGative=false;
			}
			//하나씩 꺼내서 변환한다.
			String tranStrSource = analy.basicTranOne(upKeyword,"2",sty_type, inGative);
			//System.out.println("받은값"+tranStrSource);
			StringTokenizer stk = new StringTokenizer(tranStrSource,"|");
			String tranStr=stk.nextToken();
			String sty_emi= stk.nextToken();
			//if(tranStr==null)tranStr="";
			System.out.println("분류값"+tranStr+":"+sty_emi);
			String sty_ans_tf="N";
			if(tranStr.equals(sty_answer)){
				sty_ans_tf="Y";
			}
			Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("sty_idx", sty_idx);
			hashData.put("sty_convert", tranStr);
			hashData.put("sty_ans_tf", sty_ans_tf);
			hashData.put("sty_emi", sty_emi);
			
			wordAnalyService.updateTestData(hashData);
			//하나씩 업데이트 한다
		}
		System.out.println("여기서 호출한다");
		return "redirect:/test_list.do";
	}
	
	/**
	 * 문체변환수행. 
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "wrtStyGen"
	 * @exception Exception
	 */
	@RequestMapping(value = "/wrtStyGen.do")
	public String wrtStyGen(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		
		String searchKeyword = requestParams.get("searchKeyword")==null?"":requestParams.get("searchKeyword");
		String selectedId = requestParams.get("selectedId")==null?"2":requestParams.get("selectedId");
		String firstStr="";
		boolean inGative=false; //의문문여부
		SpMorphAnalyzer any= new SpMorphAnalyzer();
		
		List<HashMap<String, String>> sampleList = new ArrayList<HashMap<String,String>>();
		List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
		searchKeyword = searchKeyword.trim();
		String upKeyword = searchKeyword;
		if(searchKeyword.indexOf("?")!=-1 && searchKeyword.indexOf("?")==searchKeyword.length()-1){ //가장마지막에 ?표 표시확인
			inGative = true;
			upKeyword= searchKeyword.substring(0,searchKeyword.length()-1);
		}else{
			inGative=false;
		}
		
		
		if(upKeyword!=null && !upKeyword.equals("")) {
			model.addAttribute("searchKeyword", searchKeyword);
			
			try {
				//기본형
				//?로 의문문인지 확인
				Vector<String> vec = any.selectToken(upKeyword,selectedId);
				Vector<String> newVec = any.clearDuple(vec);
				
				for(String s:newVec) {
					firstStr +=s+" ";
				}
				model.addAttribute("firstStr", firstStr);
				for(String str:newVec){
					List<AnalysisOutput> totlist = any.analyzeNew(str);
					for(AnalysisOutput o:totlist) 
					{
						HashMap<String, String> hmap = new HashMap<String, String>();
						hmap.put("anycont", o.toString());
						hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
						hmap.put("anynum", String.valueOf(o.getPatn()));
						//System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn()));
						analyList.add(hmap);
					}
				}
				HashMap<String,String> dataMap3 = new HashMap<String,String>();
				HashMap<String,String> reMap = any.basicTran(upKeyword,selectedId,inGative);//기본형,명사형종결,해체
				dataMap3.put("id", "기본형");
				dataMap3.put("gen", reMap.get("basic"));
				HashMap<String,String> dataMap4 = new HashMap<String,String>();
				dataMap4.put("id", "시제기본형");
				dataMap4.put("gen", reMap.get("pBasic"));
				HashMap<String,String> dataMap5 = new HashMap<String,String>();
				dataMap5.put("id", "명사형종결");
				dataMap5.put("gen", reMap.get("nBasic"));
				HashMap<String,String> dataMap6 = new HashMap<String,String>();
				dataMap6.put("id", "해체");
				dataMap6.put("gen", reMap.get("hBasic"));
				
				HashMap<String,String> dataMap7 = new HashMap<String,String>();
				dataMap7.put("id", "해요체");
				dataMap7.put("gen", reMap.get("hyBasic"));
				
				HashMap<String,String> dataMap8 = new HashMap<String,String>();
				dataMap8.put("id", "합쇼체");
				dataMap8.put("gen", reMap.get("habBasic"));
				
				sampleList.add(dataMap3);
				sampleList.add(dataMap4);
				sampleList.add(dataMap5);
				sampleList.add(dataMap6);
				sampleList.add(dataMap7);
				sampleList.add(dataMap8);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		//System.out.println("model"+model.get("searchKeyword"));
		model.addAttribute("resultList", sampleList);
		model.addAttribute("analyList", analyList);
		return "example/temp/wrtStyGen";
	}
	
	/**
	 * 글 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/total_list.do")
	public String total_list(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {

		/** EgovPropertyService.sample */
		searchVO.setPageUnit(100);
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		//System.out.println("검색어:"+searchVO.getSearchKeyword()+searchVO.getSearchCondition());
		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		if(searchVO.getPageIndex()==0){
			paginationInfo.setCurrentPageNo(1);
		}else{
			paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		}
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		//파일을 읽어서 수정일자를 보여준다.
/*		File file = FileUtil.getClassLoaderFile(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_DICTIONARY));
		long lastModified = file.lastModified();

		String pattern = "yyyy-MM-dd hh:mm aa";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		
		Date lastModifiedDate = new Date( lastModified );

		System.out.println( "The file " + file + " was last modified on " + simpleDateFormat.format( lastModifiedDate ) );*/
		List<?> sampleList = wordAnalyService.selectSampleList(searchVO);
		model.addAttribute("resultList", sampleList);
		//System.out.println("paginationInfo.getCurrentPageNo()"+paginationInfo.getCurrentPageNo());
		model.addAttribute("pageIndex", paginationInfo.getCurrentPageNo());
		model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
		//System.out.println("======================="+paginationInfo.getCurrentPageNo());
		//sampleList.get(0);
		int totCnt = wordAnalyService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		//System.out.println("======="+paginationInfo.toString());
		//색인도 같이 수행한다.
		/*if(searchVO!=null && searchVO.getSearchCondition().equals("3")) {
		    totalIndex(searchVO);
		}*/
		//System.out.println("문제없나~~~~");
		return "example/temp/total_list";
	}

	/**
	 * 글 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/extension_list.do")
	public String extension_list(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {

		/** EgovPropertyService.sample */
		searchVO.setPageUnit(100);
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		//System.out.println("검색어:"+searchVO.getSearchKeyword()+searchVO.getSearchCondition());
		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		if(searchVO.getPageIndex()==0){
			paginationInfo.setCurrentPageNo(1);
		}else{
			paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		}
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		//파일을 읽어서 수정일자를 보여준다.
/*		File file = FileUtil.getClassLoaderFile(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_DICTIONARY));
		long lastModified = file.lastModified();

		String pattern = "yyyy-MM-dd hh:mm aa";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		
		Date lastModifiedDate = new Date( lastModified );

		System.out.println( "The file " + file + " was last modified on " + simpleDateFormat.format( lastModifiedDate ) );*/
		List<?> sampleList = wordAnalyService.selectExtensionList(searchVO);
		model.addAttribute("resultList", sampleList);
		//System.out.println("paginationInfo.getCurrentPageNo()"+paginationInfo.getCurrentPageNo());
		model.addAttribute("pageIndex", paginationInfo.getCurrentPageNo());
		model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
		//System.out.println("======================="+paginationInfo.getCurrentPageNo());
		//sampleList.get(0);
		int totCnt = wordAnalyService.selectExtensionListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		//System.out.println("======="+paginationInfo.toString());
		//색인도 같이 수행한다.
		/*if(searchVO!=null && searchVO.getSearchCondition().equals("3")) {
		    totalIndex(searchVO);
		}*/
		//System.out.println("문제없나~~~~");
		return "example/temp/extension_list";
	}
	
	public boolean totalIndex(SampleDefaultVO searchVO) {
		boolean b = false;
		    String txt = "" ;
	        String fileName;
	        try{
	            // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
	        	File file = FileUtil.getClassLoaderFile(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_DICTIONARY));
	        	fileName = file.getAbsolutePath();
	            System.out.println("====>"+fileName);
	            List<?> sampleList = wordAnalyService.selectTotalList(searchVO);
	            BufferedWriter fw = new BufferedWriter(new FileWriter(fileName, false));
	            // 파일안에 문자열 쓰기
	        	for(Object map:sampleList) {
	        		HashMap hmap = (HashMap)map;
	        		fw.write(hmap.get("dic_word")+","+hmap.get("dic_nvzdbipscc")+"\n");
	        	}
	            
	            //fw.write("테스트2");
	            fw.flush();
	 
	            // 객체 닫기
	            fw.close();
	            DictionaryUtil.loadDictionary(); 
	             
	        }catch(Exception e){
	            e.printStackTrace();
	        }

		return b;
	}
	public boolean extensionIndex(SampleDefaultVO searchVO) {
		boolean b = false;
		    String txt = "" ;
	        String fileName;
	        try{
	            // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
	        	File file = FileUtil.getClassLoaderFile(KoreanEnv.getInstance().getValue(KoreanEnv.FILE_EXTENSION));
	        	fileName = file.getAbsolutePath();
	            System.out.println("fileName====>"+fileName);
	            List<?> sampleList = wordAnalyService.selectExtensionTotList(searchVO);
	            BufferedWriter fw = new BufferedWriter(new FileWriter(fileName, false));
	            // 파일안에 문자열 쓰기
	        	for(Object map:sampleList) {
	        		HashMap hmap = (HashMap)map;
	        		fw.write(hmap.get("dic_word")+","+hmap.get("dic_nvzdbipscc")+"\n");
	        	}
	            
	            //fw.write("테스트2");
	            fw.flush();
	 
	            // 객체 닫기
	            fw.close();
	            DictionaryUtil.loadDictionary(); 
	             
	        }catch(Exception e){
	            e.printStackTrace();
	        }

		return b;
	}
	
	/**
	 * 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping("/updateTotalView.do")
	public String updateTotalView(@RequestParam Map<String, String> requestParams, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		SampleVO sampleVO = new SampleVO();
		String id = requestParams.get("selectedId");
		String dic_idx = requestParams.get("dic_idx");
		String pageIndex = requestParams.get("pageIndex")==null?"1":requestParams.get("pageIndex");
		String agent = requestParams.get("agent");
		String searchKeyword = requestParams.get("searchKeyword");
		System.out.println("id"+id+"dic_idx"+dic_idx+"pageIndex"+pageIndex+"agent"+agent);
		sampleVO.setId(id);
		// 변수명은 CoC 에 따라 sampleVO
		//model.addAttribute(selectSample(sampleVO, searchVO));                                      
		Map<String,Object> duplMap = new HashMap<String,Object>();
		duplMap.put("dic_idx", dic_idx);
		Map<String, Object> totalData = wordAnalyService.selectTotalData(duplMap);
		totalData.put("pageIndex", pageIndex);
		System.out.println("===="+totalData.toString());
		model.addAttribute("totalData",totalData);
		model.addAttribute("agent",agent);
		model.addAttribute("searchKeyword",searchKeyword);
		return "example/temp/totalRegister";
	}
	/**
	 * 확장사전 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping("/updateExtensionView.do")
	public String updateExtensionView(@RequestParam Map<String, String> requestParams, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		SampleVO sampleVO = new SampleVO();
		String id = requestParams.get("selectedId");
		String dic_idx = requestParams.get("dic_idx");
		String pageIndex = requestParams.get("pageIndex")==null?"1":requestParams.get("pageIndex");
		String agent = requestParams.get("agent");
		String searchKeyword = requestParams.get("searchKeyword");
		System.out.println("id"+id+"dic_idx"+dic_idx+"pageIndex"+pageIndex+"agent"+agent);
		sampleVO.setId(id);
		// 변수명은 CoC 에 따라 sampleVO
		//model.addAttribute(selectSample(sampleVO, searchVO));                                      
		Map<String,Object> duplMap = new HashMap<String,Object>();
		duplMap.put("dic_idx", dic_idx);
		Map<String, Object> totalData = wordAnalyService.selectExtensionData(duplMap);
		totalData.put("pageIndex", pageIndex);
		System.out.println("===="+totalData.toString());
		model.addAttribute("totalData",totalData);
		model.addAttribute("agent",agent);
		model.addAttribute("searchKeyword",searchKeyword);
		return "example/temp/extensionRegister";
	}
	/**
	 * 확장사전 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping("/registerExtensionView.do")
	public String registerExtensionView(@RequestParam Map<String, String> requestParams, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		
		// 변수명은 CoC 에 따라 sampleVO
		//model.addAttribute(selectSample(sampleVO, searchVO));                                      
		
		return "example/temp/extensionNewRegister";
	}
	/**
	 * 글 수정 한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */         
	@RequestMapping("/updateTotalOk.do")
	public String updateTotalOk(@RequestParam Map<String, String> requestParams, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		
		String dic_idx = requestParams.get("dic_idx");
		String dic_use_yn = requestParams.get("dic_use_yn")==null?"N":requestParams.get("dic_use_yn");
		String dic_word = requestParams.get("dic_word");
		String dic_nvzdbipscc = requestParams.get("dic_nvzdbipscc");
		
		Map<String,Object> hashData = new HashMap<String,Object>();
		hashData.put("dic_idx", dic_idx);
		hashData.put("dic_word", dic_word);
		hashData.put("dic_nvzdbipscc", dic_nvzdbipscc);
		hashData.put("dic_use_yn", dic_use_yn);
		wordAnalyService.updateTotalData(hashData);
		
		totalIndex(searchVO);//인덱싱한다.
		/** EgovPropertyService.sample */
		searchVO.setPageUnit(100);
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		System.out.println("검색어:"+searchVO.getSearchKeyword()+searchVO.getSearchCondition());
		if(searchVO.getSearchKeyword()!=null && !searchVO.getSearchKeyword().equals("")){
			searchVO.setSearchCondition("1");
			model.addAttribute("searchKeyword",searchVO.getSearchKeyword());
		}
		String pageIndex = requestParams.get("pageIndex");
		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		System.out.println("test...");
		List<?> sampleList = wordAnalyService.selectSampleList(searchVO);
		model.addAttribute("resultList", sampleList);
		model.addAttribute("pageIndex", pageIndex);
		sampleList.get(0);
		int totCnt = wordAnalyService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		//System.out.println("======="+paginationInfo.toString());
		//색인도 같이 수행한다.
		if(searchVO!=null && searchVO.getSearchCondition().equals("3")) {
		    totalIndex(searchVO);
		}
		
		
		
		return "example/temp/total_list";
	}
	/**
	 * 글 수정 한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */         
	@RequestMapping("/updateExtensionOk.do")
	public String updateExtensionOk(@RequestParam Map<String, String> requestParams, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		
		String dic_idx = requestParams.get("dic_idx");
		String dic_use_yn = requestParams.get("dic_use_yn")==null?"N":requestParams.get("dic_use_yn");
		String dic_word = requestParams.get("dic_word");
		String dic_nvzdbipscc = requestParams.get("dic_nvzdbipscc");
		
		Map<String,Object> hashData = new HashMap<String,Object>();
		hashData.put("dic_idx", dic_idx);
		hashData.put("dic_word", dic_word);
		hashData.put("dic_nvzdbipscc", dic_nvzdbipscc);
		hashData.put("dic_use_yn", dic_use_yn);
		wordAnalyService.updateExtensionData(hashData);
		
		extensionIndex(searchVO);//인덱싱한다.
		/** EgovPropertyService.sample */
		searchVO.setPageUnit(100);
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		System.out.println("검색어:"+searchVO.getSearchKeyword()+searchVO.getSearchCondition());
		if(searchVO.getSearchKeyword()!=null && !searchVO.getSearchKeyword().equals("")){
			searchVO.setSearchCondition("1");
			model.addAttribute("searchKeyword",searchVO.getSearchKeyword());
		}
		String pageIndex = requestParams.get("pageIndex");
		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		System.out.println("test...");
		List<?> sampleList = wordAnalyService.selectExtensionList(searchVO);
		model.addAttribute("resultList", sampleList);
		model.addAttribute("pageIndex", pageIndex);
		sampleList.get(0);
		int totCnt = wordAnalyService.selectExtensionListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		//System.out.println("======="+paginationInfo.toString());
		//색인도 같이 수행한다.
		if(searchVO!=null && searchVO.getSearchCondition().equals("3")) {
		    extensionIndex(searchVO);
		}
		
		return "example/temp/extension_list";
	}
	/**
	 * 글 등록 한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */         
	@RequestMapping("/regExtensionOk.do")
	public String regExtensionOk(@RequestParam Map<String, String> requestParams, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) {
		try{
			
			String dic_use_yn = requestParams.get("dic_use_yn")==null?"N":requestParams.get("dic_use_yn");
			String dic_word = requestParams.get("dic_word");
			String dic_nvzdbipscc = requestParams.get("dic_nvzdbipscc");
			
			Map<String,Object> hashData = new HashMap<String,Object>();
			
			hashData.put("dic_word", dic_word);
			hashData.put("dic_nvzdbipscc", dic_nvzdbipscc);
			hashData.put("dic_use_yn", dic_use_yn);
			wordAnalyService.regExtensionData(hashData);
			
			//extensionIndex(searchVO);//인덱싱한다.
			/** EgovPropertyService.sample */
			searchVO.setPageUnit(100);
			searchVO.setPageSize(propertiesService.getInt("pageSize"));
			
			System.out.println("검색어:"+searchVO.getSearchKeyword()+searchVO.getSearchCondition());
			if(searchVO.getSearchKeyword()!=null && !searchVO.getSearchKeyword().equals("")){
				searchVO.setSearchCondition("1");
				model.addAttribute("searchKeyword",searchVO.getSearchKeyword());
			}
			String pageIndex = requestParams.get("pageIndex");
			/** pageing setting */
			PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
			paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
			paginationInfo.setPageSize(searchVO.getPageSize());
	
			searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
			searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
			searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
			System.out.println("test...");
			List<?> sampleList = wordAnalyService.selectExtensionList(searchVO);
			model.addAttribute("resultList", sampleList);
			model.addAttribute("pageIndex", pageIndex);
			sampleList.get(0);
			int totCnt = wordAnalyService.selectExtensionListTotCnt(searchVO);
			paginationInfo.setTotalRecordCount(totCnt);
			model.addAttribute("paginationInfo", paginationInfo);
			//System.out.println("======="+paginationInfo.toString());
			//색인도 같이 수행한다.
			//if(searchVO!=null && searchVO.getSearchCondition().equals("3")) {
			extensionIndex(searchVO);
			//}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "example/temp/extension_list";
	}
	/**
	 * 글을 조회한다.
	 * @param sampleVO - 조회할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return @ModelAttribute("sampleVO") - 조회한 정보
	 * @exception Exception
	 */
	public SampleVO selectSample(SampleVO sampleVO, @ModelAttribute("searchVO") SampleDefaultVO searchVO) throws Exception {
		return sampleService.selectSample(sampleVO);
	}
	
	public static void main(String args[]) {
		String searchKeyword="너 오늘 진짜 예뻐";
		String selectedId ="2";
		String firstStr="";
		List<HashMap<String, String>> sampleList = new ArrayList<HashMap<String,String>>();
		List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
		if(searchKeyword!=null && !searchKeyword.equals("")) {
			
			try {
				
				SpMorphAnalyzer any= new SpMorphAnalyzer();
				
				Vector<String> vec = any.selectToken(searchKeyword,selectedId);
				System.out.println("1차"+vec.toString());
				Vector<String> newVec = any.clearDuple(vec);
				System.out.println("1차"+newVec.toString());
				
				for(String s:newVec) {
					firstStr +=s+" ";
				}
				
				List<AnalysisOutput> totlist = any.analyze(newVec);
				for(AnalysisOutput o:totlist) 
				{
					HashMap<String, String> hmap = new HashMap<String, String>();
					hmap.put("anycont", o.toString());
					hmap.put("anytype", PatternConstants.getPTNStr(o.getPatn()));
					hmap.put("anynum", String.valueOf(o.getPatn()));
					//System.out.println(o.toString()+" "+PatternConstants.getPTNStr(o.getPatn()));
					analyList.add(hmap);
				}
				System.out.println(totlist.toString());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	

}

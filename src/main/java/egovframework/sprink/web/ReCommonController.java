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


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.WordAnalyService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import egovframework.sprink.dto.CommonFileVo;
import egovframework.sprink.dto.Member;
import egovframework.sprink.dto.UploadFileVo;
import egovframework.sprink.service.CommonService;
import egovframework.sprink.service.MemberService;
import egovframework.sprink.util.CommonUtils;
import egovframework.sprink.util.ConfigUtil;
import egovframework.sprink.util.RegexPattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.ko.sp.SelectKeyword;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.DefaultBeanValidator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @
 * @  ?????????      ?????????              ????????????
 * @ ---------   ---------   -------------------------------
 * @ 2022.05.09           ????????????
 *
 * @author ????????????????????? ???????????? ?????????
 * @since 2022. 05.09
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
@RequestMapping(value = "/reDesign/")
public class ReCommonController {
	
	/** EgovSampleService */
	@Resource(name = "wordAnalyService")
	private WordAnalyService wordAnalyService;
	
	/** EgovSampleService */
	@Resource(name = "sampleService")
	private EgovSampleService sampleService;

	/** MemberService */
	@Resource(name = "memberService")
	private MemberService memberService;
	
	/** CommonService */
	@Resource(name = "commonService")
	private CommonService commonService;
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	/**
	 * ???????????? ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/layout.do")
	public String layout() throws Exception {
		System.out.println("reDesign/layout");
		return "reDesign/layout";
	}
	
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/main.do")
	public String main(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		List<?> authList = (List<?>)session.getAttribute("authList");
		String agent = requestParams.get("agent")==null?"":requestParams.get("agent");
		Map<String,Object> map = new HashMap<String,Object>();
		
		try{
		if ( session.getAttribute("login") != null ){

			String proList = propertiesService.getString("projectNum");
			StringTokenizer stk = new StringTokenizer(proList,",");
			int cnt = stk.countTokens();
			System.out.println("cnt"+cnt);
			///?????? ??????????????? ???????????? ???????????? ??????????????? ???????????? ?????????
			///?????? ?????? ???????????? ????????? ????????? ????????????.
			String reAgent="";
			for(int i=0;i<cnt;i++){
				if(!reAgent.equals(""))break;
				String pro = stk.nextToken();
				for(Object menu :authList){
					HashMap<String,Object> authMap = (HashMap<String,Object>)menu;
					int mu_idx = (int)authMap.get("mu_idx");
					String au_list = (String)authMap.get("au_list");
					String mu_method = (String)authMap.get("mu_method");
					System.out.println("agent"+agent+"mu_idx"+mu_idx+"pro"+pro+"au_list"+au_list);
					if(agent.equals("")){//????????? ???????????? ????????? ????????????.
						if(mu_idx==Integer.parseInt(pro)&&au_list.equals("Y")){
							reAgent = (String)authMap.get("mu_method");
							break;
						}
					}else{//????????? ????????? ????????????
						if(agent.equals(mu_method)&&mu_idx==Integer.parseInt(pro)&&au_list.equals("Y")){
							reAgent = (String)authMap.get("mu_method");
							break;
						}
					}
				}
				
			}
			System.out.println("reAgent"+reAgent);
			map.put("agent", reAgent);
			List<?> workList = memberService.selectWorkList(map);
			System.out.println("authList=="+authList.toString());
			model.put("agent", reAgent);
			model.addAttribute("resultList", workList);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		/*System.out.println("requestParams"+requestParams.toString());
		*//** EgovPropertyService.sample *//*
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		*//** pageing setting *//*
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> sampleList = memberService.selectMemberList(searchVO);
		model.addAttribute("resultList", sampleList);

		int totCnt = memberService.selectMemberListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		System.out.println("======="+paginationInfo.toString());*/
		System.out.println("reDesign/main");
		return "reDesign/main";
	}
	
	/**
	 * ?????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/login.do")
	public String login(ModelMap model) throws Exception {
		
		return "reDesign/login";
	}
	
//	 // ????????? submit
//	  @RequestMapping(value = "/loginPost", method = RequestMethod.POST)
//	  public String loginPost(@RequestParam Map<String, String> requestParams, Model model, HttpSession session) throws Exception {
//		  String returnURL = "";
//		  try {
//		  
//	      if ( session.getAttribute("login") != null ){
//	          // ????????? login?????? ?????? ?????? ???????????????
//	          session.removeAttribute("login"); // ???????????? ????????? ??????.
//	      }
//	      
//	      // ???????????? ???????????? UsersVO ????????? ?????????.
//	      //UsersVO vo = service.getUser(UsersVO);
//	     
//	      Map<String, Object> hashData = new HashMap<String, Object>();
//	      hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
//	      hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
//	      Member mem = memberService.selectMember(hashData);
//	      //????????? ?????? ??????
//	      
//	      if ( mem != null ){ // ????????? ??????
//	          session.setAttribute("login", mem); // ????????? login????????? ???????????? UsersVO ????????? ????????? ???.
//	          String au_mem = mem.getTb_mem_no();//????????????
//	  		  SampleDefaultVO searchVO = new SampleDefaultVO();
//	  		  searchVO.setSearchKeyword(au_mem);
//	  		  List<?> authList = memberService.selectAuthList(searchVO);
//	  		    
//	  		    //????????? ????????? ????????????
//		  		Map<String, Object> logData = new HashMap<String, Object>();
//		  		logData.put("log_project", "common"); //??????????????? ??????????????????
//		  		logData.put("bd_idx", "0");           //????????? ???????????? ????????? ??????
//		  		logData.put("cd_group", "0002");      //????????????
//		  		logData.put("cd_code", "0001");		  //?????????
//		  		logData.put("mb_idx", mem.getTb_mem_no()); //????????????
//		  		logData.put("log_cont", mem.getTb_mem_nm()+"?????? ?????????????????????"); //????????????
//			    
//				commonService.insertLogData(logData);
//			  //System.out.println("????????????");
//	  		  session.setAttribute("authList", authList); // ????????? login????????? ???????????? UsersVO ????????? ????????? ?????????
//	  		  //System.out.println("????????????");
//	          returnURL = "redirect:/type_chk_main.do"; // ????????? ????????? ?????????????????? ????????????
//	          
//	      }else { // ???????????? ????????? ??????
//	          returnURL = "redirect:/relogin.do"; // ????????? ????????? ?????? ????????? ???
//	      }
//		  }catch(Exception e) {
//			  e.printStackTrace();
//		  }
//	      return returnURL; // ????????? ????????? returnURL ??? ???????????? ????????????
//	  }
	
	/**
	 * ????????? ??????
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */

	@RequestMapping(value = "/spell_remain.do")
	public String spell_list(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		List<?> authList = (List<?>)session.getAttribute("authList");
		String agent = requestParams.get("agent")==null?"":requestParams.get("agent");
		Map<String,Object> map = new HashMap<String,Object>();
		Member mem = (Member)session.getAttribute("login");
		String searchKeyword = requestParams.get("searchKeyword")==null?"":requestParams.get("searchKeyword");
		String sp_quest="";
		try{
		if ( session.getAttribute("login") != null ){

			String proList = propertiesService.getString("projectNum");
			StringTokenizer stk = new StringTokenizer(proList,",");
			int cnt = stk.countTokens();
			System.out.println("cnt"+cnt);
			///?????? ??????????????? ???????????? ???????????? ??????????????? ???????????? ?????????
			///?????? ?????? ???????????? ????????? ????????? ????????????.
			String reAgent="";
			for(int i=0;i<cnt;i++){
				if(!reAgent.equals(""))break;
				String pro = stk.nextToken();
				for(Object menu :authList){
					HashMap<String,Object> authMap = (HashMap<String,Object>)menu;
					int mu_idx = (int)authMap.get("mu_idx");
					String au_list = (String)authMap.get("au_list");
					String mu_method = (String)authMap.get("mu_method");
					System.out.println("agent"+agent+"mu_idx"+mu_idx+"pro"+pro+"au_list"+au_list);
					if(agent.equals("")){//????????? ???????????? ????????? ????????????.
						if(mu_idx==Integer.parseInt(pro)&&au_list.equals("Y")){
							reAgent = (String)authMap.get("mu_method");
							break;
						}
					}else{//????????? ????????? ????????????
						if(agent.equals(mu_method)&&mu_idx==Integer.parseInt(pro)&&au_list.equals("Y")){
							reAgent = (String)authMap.get("mu_method");
							break;
						}
					}
				}
				
			}
			System.out.println("reAgent"+reAgent);
			map.put("agent", reAgent);
			map.put("bd_writer", mem.getTb_mem_no());
			
			List<?> workList = memberService.selectSpellList(map);
			System.out.println("authList=="+authList.toString());
			model.put("agent", reAgent);
			model.addAttribute("resultList", workList);
			
			if(!searchKeyword.equals("")) {//?????? ????????? ????????? ????????????
				String[] command = new String[4];
		        command[0] = "python";
		        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
		        command[1] = ConfigUtil.PYTHON_EXEC+"/spellTest.py";
				try {
					        	command[2] = searchKeyword;
					            String str = CommonUtils.execPython(command,true);
					            String result = "[??????]";
					            String reStr = CommonUtils.getDecode(str);
					            if(!reStr.equals(searchKeyword)) {
					            	result = "[?????????]";
					            }
					            sp_quest = "????????? ???????????? "+result+" : "+reStr;
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
					}
			
			model.put("searchKeyword", searchKeyword);
			}
		    model.put("sp_quest", sp_quest);
		}catch(Exception e){
			e.printStackTrace();
		}
		/*System.out.println("requestParams"+requestParams.toString());
		*//** EgovPropertyService.sample *//*
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		*//** pageing setting *//*
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> sampleList = memberService.selectMemberList(searchVO);
		model.addAttribute("resultList", sampleList);

		int totCnt = memberService.selectMemberListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		System.out.println("======="+paginationInfo.toString());*/
		//System.out.println("main/main");
		return "reDesign/spell_main";
	}
	
	/**
	 * worklist
	 * ??????????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/work_relist.do")
	public String work_list(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {
		/** EgovPropertyService.sample */
		try {
		searchVO.setPageUnit(100);
		searchVO.setPageSize(20);
		System.out.println("searchVO.bd_idx"+searchVO.getBd_idx());
		searchVO.setTable_name("tb_work_"+searchVO.getBd_idx());
		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		System.out.println("searchVO==>"+searchVO.getSearchKeyword());
		List<?> sampleList = memberService.selectWorkDetailList(searchVO);
		
		model.addAttribute("resultList", sampleList); 

		int totCnt = memberService.selectWorkListTotCnt(searchVO);
		int ansCnt = memberService.selectWorkAnsTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		float sucRate = ((float)ansCnt/totCnt)*100;
		HashMap<String, String> staMap = new HashMap<String, String>();
		model.put("agent", searchVO.getAgent());
		model.put("bd_idx", searchVO.getBd_idx());
		model.put("searchKeyword", searchVO.getSearchKeyword());
		model.put("sucRate", String.valueOf(sucRate));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "reDesign/work_list";
	}
	
	/**
	 * work_insert
	 * ????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/work_reinsert.do")
	public String work_insert(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
        String agent = requestParams.get("agent");
        model.put("agent", agent);
        System.out.println("agent===>"+agent);
		return "reDesign/work_insert";
	}
	
	/**
	 * spell_list
	 * ????????? ??????????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/spell_relist.do")
	public String spell_list(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {
		/** EgovPropertyService.sample */
		try {
		searchVO.setPageUnit(100);
		searchVO.setPageSize(20);
		//System.out.println("searchVO.bd_idx"+searchVO.getBd_idx());
		
		String bd_state = memberService.selectSpellCompCode(searchVO);
		model.addAttribute("bd_state", bd_state); 
		searchVO.setTable_name("tb_spell_"+searchVO.getBd_idx());
		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		System.out.println("searchVO==>"+searchVO.getSearchKeyword());
		List<?> sampleList = memberService.selectSpellDetailList(searchVO);
		
		model.addAttribute("resultList", sampleList); 

		int totCnt = memberService.selectWorkListTotCnt(searchVO);
		int ansCnt = memberService.selectWorkAnsTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		float sucRate = ((float)ansCnt/totCnt)*100;
		HashMap<String, String> staMap = new HashMap<String, String>();
		model.put("agent", searchVO.getAgent());
		model.put("bd_idx", searchVO.getBd_idx());
		model.put("searchKeyword", searchVO.getSearchKeyword());
		model.put("sucRate", String.valueOf(sucRate));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "reDesign/spell_list";
	}
	
	/**
	 * spell_insert
	 * ????????? ????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/spell_reinsert.do")
	public String spell_insert(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
        String agent = requestParams.get("agent");
        model.put("agent", agent);
        System.out.println("agent===>"+agent);
		return "reDesign/spell_insert";
	}
}

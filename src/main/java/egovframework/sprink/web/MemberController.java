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
 * @ 2009.03.16           ????????????
 *
 * @author ????????????????????? ???????????? ?????????
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
public class MemberController {

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
	 * ?????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/new_main.do")
	public String new_main(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		///????????? ????????? ????????? ?????? ??????
		
		return "main/new_main";
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
		System.out.println("main/main");
		return "main/main";
	}
	
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/spell_main.do")
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
		return "main/spell_main";
	}
	
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_list.do")
	public String type_chk_list(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
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
			
			map.put("agent", agent);
			map.put("bd_writer", mem.getTb_mem_no());
			
			List<?> workList = memberService.selectTypeChkList(map);
			
			model.addAttribute("resultList", workList);
			model.addAttribute("agent", agent);
			}
		    model.put("sp_quest", sp_quest);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "main/type_chk_list";
	}
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_main.do")
	public String type_chk_main(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		List<?> authList = (List<?>)session.getAttribute("authList");
		String agent = requestParams.get("agent")==null?"":requestParams.get("agent");
		String typeSEL= requestParams.get("typeSEL")==null?"ES,DS,SD,PU,SW,CO":requestParams.get("typeSEL");
		System.out.println("typeSEL"+typeSEL);
		Map<String,Object> map = new HashMap<String,Object>();
		Member mem = (Member)session.getAttribute("login");
		String searchKeyword = requestParams.get("searchKeyword")==null?"":requestParams.get("searchKeyword");
		String orgSearchKeyword = searchKeyword;
		String sp_quest="";
		String typeInsert="";
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
			model.put("agent", reAgent);
			if(!searchKeyword.equals("")) {//?????? ????????? ????????? ????????????
				
				//??????????????????
				if(typeSEL.indexOf("ES")>-1){
					//???????????? ????????????
					boolean ep = RegexPattern.isEmpty(searchKeyword);
					if(ep){
						typeInsert="ES";
						searchKeyword=searchKeyword.trim();
					}
				}
				//??????????????????
				if(typeSEL.indexOf("DS")>-1){
					//???????????? ????????????
					boolean ep = RegexPattern.isDupEmpty(searchKeyword);
					if(ep){
						if(typeInsert.equals("")){
							typeInsert="DS";
						}else{
							typeInsert=typeInsert+",DS";
						}
						searchKeyword=RegexPattern.rvDupEmpty(searchKeyword);
					}
				}
				if(typeSEL.indexOf("SD")>-1){
					//???????????? ??????
					boolean ep = RegexPattern.isSpeChar(searchKeyword);
					if(!ep){
						if(typeInsert.equals("")){
							typeInsert="SD";
						}else{
							typeInsert=typeInsert+",SD";
						}
						searchKeyword=RegexPattern.rvSpeChar(searchKeyword);
					}
				}
				if(typeSEL.indexOf("PU")>-1){
					//????????? ??????
					boolean ep = RegexPattern.isEndChar(searchKeyword);
					if(!ep){
						if(typeInsert.equals("")){
							typeInsert="PU";
						}else{
							typeInsert=typeInsert+",PU";
						}
						searchKeyword=RegexPattern.addEndChar(searchKeyword);
					}
				}
				if(typeSEL.indexOf("SW")>-1 || typeSEL.indexOf("CO")>-1){//???????????? ?????????
					String pythonExec = propertiesService.getString("PYTHON_EXEC");
					String[] command = new String[4];
			        command[0] = "python";
			        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
			        command[1] = pythonExec+"/spellTest.py";
					try {
						        	command[2] = searchKeyword;
						            String str = CommonUtils.execPython(command,true);
						            
						            String reStr = CommonUtils.getDecode(str);
						            ///????????? ???????????? ????????? ???????????? ?????? ????????????
						            
						            //???????????? ??????????????? ?????? ?????? ??????
						            //??????????????? ????????????
						               //???????????? ????????? ????????? ??????
						               //???????????? ???????????? ???????????? ??????
						            //???????????? ????????????
						               //????????? ????????? ????????? ??????
						            if(!reStr.equals(searchKeyword)) {//????????? ????????????
						            	//???????????? ????????? ????????? ????????????
						            	String rStr = reStr.replaceAll(" ", "");
						            	String sStr = searchKeyword.replaceAll(" ","");
						            	//System.out.println("rStr"+rStr);		            	
						            	//System.out.println("sStr"+sStr);
						            	if(rStr.equals(sStr)) {//??????????????? ????????????
						            		//???????????? ?????????
						            		if(typeSEL.indexOf("SW")>-1) {
						            			if(typeInsert.equals("")){
													typeInsert="SW";
												}else{
													typeInsert=typeInsert+",SW";
												}
						            			searchKeyword=reStr; //??????
						            		}
						            	}else {//???????????? ????????????
						            		if(typeSEL.indexOf("CO")>-1) {
						            			if(typeInsert.equals("")){
													typeInsert="CO";
												}else{
													typeInsert=typeInsert+",CO";
												}
						            			searchKeyword=reStr; //??????
						            		}
						            	}
						            }
						            /*if(!reStr.equals(searchKeyword)) {
						            	if(typeInsert.equals("")){
											typeInsert="CO";
										}else{
											typeInsert=typeInsert+",CO";
										}
						            }*/
						            //searchKeyword=reStr;
						        } catch (Exception e) {
						            e.printStackTrace();
						        }
						}
				}
			if(!typeInsert.equals("")){
				typeInsert="["+typeInsert+"]";
			}
			model.put("searchKeyword", orgSearchKeyword);
			model.put("typeInsert",typeInsert);
			model.put("typeSEL",typeSEL);
			}
		    model.put("sp_quest", searchKeyword);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "main/type_chk_main";
	}
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_file.do")
	public String type_chk_file(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		
		String agent = requestParams.get("agent")==null?"":requestParams.get("agent");
		//String typeSEL= requestParams.get("typeSEL")==null?"ES,DS,SD,PU,CO":requestParams.get("typeSEL");
		String typeSEL= "ES,DS,SD,PU,SW,CO";	
		model.put("agent", agent);
		model.put("typeSEL",typeSEL);
		
		return "main/type_chk_file";
	}
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/json_main.do")
	public String json_main(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		List<?> authList = (List<?>)session.getAttribute("authList");
		String agent = requestParams.get("agent")==null?"first":requestParams.get("agent");
		String jsonPrj = requestParams.get("jsonPrj")==null?"hanbat":requestParams.get("jsonPrj");
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		Member mem = (Member)session.getAttribute("login");
		String searchKeyword = requestParams.get("searchKeyword")==null?"":requestParams.get("searchKeyword");
		String sp_quest="";
		try{
		if ( session.getAttribute("login") != null ){

			map.put("agent", jsonPrj);
			map.put("bd_writer", mem.getTb_mem_no());
			
			List<?> workList = memberService.selectJsonList(map);
			System.out.println("authList=="+authList.toString());
			model.put("agent", agent);
			model.addAttribute("resultList", workList);
			model.put("jsonPrj", jsonPrj);
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
		return "main/json_main";
	}
	
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/pdf_main.do")
	public String pdf_main(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		List<?> authList = (List<?>)session.getAttribute("authList");
		String agent = requestParams.get("agent")==null?"first":requestParams.get("agent");
		String jsonPrj = requestParams.get("jsonPrj")==null?"hanbat":requestParams.get("jsonPrj");
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		Member mem = (Member)session.getAttribute("login");
		String searchKeyword = requestParams.get("searchKeyword")==null?"":requestParams.get("searchKeyword");
		String sp_quest="";
		try{
		if ( session.getAttribute("login") != null ){

			map.put("agent", jsonPrj);
			map.put("bd_writer", mem.getTb_mem_no());
			
			List<?> workList = memberService.selectJsonList(map);
			System.out.println("authList=="+authList.toString());
			model.put("agent", agent);
			model.addAttribute("resultList", workList);
			model.put("jsonPrj", jsonPrj);
			}
		    model.put("sp_quest", sp_quest);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "main/pdf_main";
	}
	/**
	 * ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/json_del.do")
	public String json_del(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		System.out.println("requestParams"+requestParams);
		String[] arrIdx = requestParams.get("bd_chk").toString().split(",");
		for (int i=0; i<arrIdx.length; i++) {
		    //testMapper.delete(Integer.parseInt(arrIdx[i]));
			//????????? ????????????.
			
		}
		return "redirect:/json_main.do";
	}
	
	/**
	 * ??????????????? ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_del.do")
	public String type_chk_del(HttpSession session,@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		System.out.println("requestParams"+requestParams);
		String[] arrIdx = requestParams.get("bd_chk").toString().split(",");
		String agent = requestParams.get("agent");
		model.put("agent", agent);
		for (int i=0; i<arrIdx.length; i++) {
		    //testMapper.delete(Integer.parseInt(arrIdx[i]));
			//????????? ????????????.
			memberService.deleteTypeChkData(arrIdx[i]);
		}
		return "redirect:/type_chk_list.do";
	}
	
	/**
	 * ?????? ????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_list.do")
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

		List<?> sampleList = memberService.selectMemberList(searchVO);
		model.addAttribute("resultList", sampleList);

		int totCnt = memberService.selectMemberListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		System.out.println("======="+paginationInfo.toString());
		return "member/member_list";
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
		
		return "member/login";
	}
	
	/**
	 * ?????????????????? ?????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/twitter_sco.do")
	public String twitter_sco(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		String path = ConfigUtil.PYTHON_SEARCH;
		File dir = new File(path);
	    File files[] = dir.listFiles();
	    BasicFileAttributes attrs;
	    List<HashMap<String, String>> fileList = new ArrayList<HashMap<String,String>>();
	    for (int i = 0; i < files.length; i++) {
	        File file = files[i];
	        if (file.getName().endsWith(".txt")){
	        	 System.out.println("file: " + file.getName()+" "+file.length()+"bytes");
	            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			    FileTime time = attrs.creationTime();
			    String pattern = "yyyy-MM-dd HH:mm:ss";
			    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			    String formatted = simpleDateFormat.format( new Date( time.toMillis() ) );
			    System.out.println( "?????? ?????? ?????? ??? ????????? ????????? ????????????.: " + formatted );
	        	HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("fileName", file.getName());
				hmap.put("fileSize", String.valueOf(file.length()));
				hmap.put("fileDate", formatted);
				fileList.add(hmap);
	        }
	    }
	    model.addAttribute("fileList", fileList);
		return "crolling/twitter_sco";
	}
	
	/**
	 * ??????????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/twitter_search.do")
	public String twitter_search(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		String searchKeyword = requestParams.get("searchKeyword")==null?"":requestParams.get("searchKeyword");
		
		String[] command = new String[4];
        command[0] = "python";
        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
        command[1] = ConfigUtil.PYTHON_EXEC+"/twitter_search.py";
        command[2] = searchKeyword.trim();
        
        try {
            CommonUtils.execPython(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return "redirect:/twitter_sco.do";
	}
	/**
	 * ??????????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/twitter_content.do")
	public String twitter_content(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		String fName = requestParams.get("fName")==null?"":requestParams.get("fName");
		System.out.println("fName"+fName);
		try{
            //?????? ?????? ??????
            File file = new File(ConfigUtil.PYTHON_SEARCH+"/"+fName);
            //???????????? ?????? ??????
            Scanner scan = new Scanner(file);
            StringBuffer sbf = new StringBuffer();
            while(scan.hasNextLine()){
                sbf.append(scan.nextLine()+"\n");
            }
            model.put("fileContent", sbf.toString());
            System.out.println(sbf.toString());
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }
		
		return "crolling/twitter_content";
	}
	
	/**
	 * ???????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_insert.do")
	public String member_insert(ModelMap model) throws Exception {

		return "member/member_insert";
	}

	/**
	 * ????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/work_insert.do")
	public String work_insert(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
        String agent = requestParams.get("agent");
        model.put("agent", agent);
        System.out.println("agent===>"+agent);
		return "work/work_insert";
	}
	
	/**
	 * ????????? ????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/spell_insert.do")
	public String spell_insert(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
        String agent = requestParams.get("agent");
        model.put("agent", agent);
        System.out.println("agent===>"+agent);
		return "work/spell_insert";
	}
	
	/**
	 * JSON ????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/json_insert.do")
	public String json_insert(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
        String agent = requestParams.get("agent");
        String jsonPrj = requestParams.get("jsonPrj");
        System.out.println("agent"+agent+"jsonPrj"+jsonPrj);
        model.put("agent", agent);
        model.put("jsonPrj", jsonPrj);
		return "work/json_insert";
	}
	
	/**
	 * PDF ????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/pdf_insert.do")
	public String pdf_insert(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
        String agent = requestParams.get("agent");
        String pdfPrj = requestParams.get("pdfPrj");
        System.out.println("agent"+agent+"pdfPrj"+pdfPrj);
        model.put("agent", agent);
        model.put("pdfPrj", pdfPrj);
		return "work/pdf_insert";
	}
	
	/**
	 * ???????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_update.do")
	public String member_update(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
		//????????? ????????????.
		Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("tb_mem_no", requestParams.get("tb_mem_no"));
	      Member member = memberService.selectMemberNo(hashData);
	      System.out.println("-->"+member.getTb_mem_tel());
	      model.addAttribute("member", member);
		return "member/member_update";
	}
	
	/**
	 * ???????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/my_update.do")
	public String my_update(HttpSession session, @RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
		//????????? ????????????.
		  Member mem = (Member)session.getAttribute("login");
		  Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("tb_mem_no", mem.getTb_mem_no());
	      Member member = memberService.selectMemberNo(hashData);
	      model.addAttribute("member", member);
		return "member/my_update";
	}
	
	/**
	 * ???????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_update_ok.do")
	public String member_update_ok(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
				
		Map<String, Object> hashData = new HashMap<String, Object>();
		hashData.put("tb_mem_no", requestParams.get("tb_mem_no"));
		hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
	    hashData.put("tb_mem_nm", requestParams.get("tb_mem_nm"));
	    hashData.put("tb_mem_tel", requestParams.get("tb_mem_tel"));
	    hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
	    String tb_mem_yn = requestParams.get("tb_mem_yn")==null?"N":requestParams.get("tb_mem_yn");
	    hashData.put("tb_mem_yn", tb_mem_yn);
		memberService.updateMember(hashData);
		
		return "redirect:/member_list.do";
	}
	/**
	 * ???????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/my_update_ok.do")
	public String my_update_ok(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		try {		
		Map<String, Object> hashData = new HashMap<String, Object>();
		hashData.put("tb_mem_no", requestParams.get("tb_mem_no"));
		hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
	    hashData.put("tb_mem_nm", requestParams.get("tb_mem_nm"));
	    hashData.put("tb_mem_tel", requestParams.get("tb_mem_tel"));
	    hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
	    String tb_mem_yn = requestParams.get("tb_mem_yn")==null?"N":requestParams.get("tb_mem_yn");
	    hashData.put("tb_mem_yn", tb_mem_yn);
		memberService.updateMember(hashData);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/spell_main.do";
	}
	
	/**
	 * ??????????????? ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_insert_ok.do")
	public String member_insert_ok(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		System.out.println("????????? ????????? ??????.....");
		
		Map<String, Object> hashData = new HashMap<String, Object>();
	    hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
	    hashData.put("tb_mem_nm", requestParams.get("tb_mem_nm"));
	    hashData.put("tb_mem_tel", requestParams.get("tb_mem_tel"));
	    hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
	    
		memberService.insertMember(hashData);
		return "redirect:/member_list.do";
	}
	
	/**
	 * ????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/logOut.do")
	public String logOut(ModelMap model, HttpSession session) throws Exception {
		if ( session.getAttribute("login") != null ){
	          // ????????? login?????? ?????? ?????? ???????????????
			  Member mem = (Member)session.getAttribute("login");
	          session.removeAttribute("login"); // ???????????? ????????? ??????.
	          session.removeAttribute("authList");
	        //???????????? ????????? ????????????
	  		Map<String, Object> logData = new HashMap<String, Object>();
	  		logData.put("log_project", "common"); //??????????????? ??????????????????
	  		logData.put("bd_idx", "0");           //????????? ???????????? ????????? ??????
	  		logData.put("cd_group", "0002");      //????????????
	  		logData.put("cd_code", "0002");		  //????????????
	  		logData.put("mb_idx", mem.getTb_mem_no()); //????????????
	  		logData.put("log_cont", mem.getTb_mem_nm()+"?????? ???????????? ????????????"); //?????????
		    
			commonService.insertLogData(logData);
	    }
		return "redirect:/new_main.do";
	}
   // ????????? submit
  @RequestMapping(value = "/loginPost.do", method = RequestMethod.POST)
  public String loginPost(@RequestParam Map<String, String> requestParams, Model model, HttpSession session) throws Exception {
	  String returnURL = "";
	  try {
	  
      if ( session.getAttribute("login") != null ){
          // ????????? login?????? ?????? ?????? ???????????????
          session.removeAttribute("login"); // ???????????? ????????? ??????.
      }
      
      // ???????????? ???????????? UsersVO ????????? ?????????.
      //UsersVO vo = service.getUser(UsersVO);
     
      Map<String, Object> hashData = new HashMap<String, Object>();
      hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
      hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
      Member mem = memberService.selectMember(hashData);
      //????????? ?????? ??????
      
      if ( mem != null ){ // ????????? ??????
          session.setAttribute("login", mem); // ????????? login????????? ???????????? UsersVO ????????? ????????? ???.
          String au_mem = mem.getTb_mem_no();//????????????
  		  SampleDefaultVO searchVO = new SampleDefaultVO();
  		  searchVO.setSearchKeyword(au_mem);
  		  List<?> authList = memberService.selectAuthList(searchVO);
  		    
  		    //????????? ????????? ????????????
	  		Map<String, Object> logData = new HashMap<String, Object>();
	  		logData.put("log_project", "common"); //??????????????? ??????????????????
	  		logData.put("bd_idx", "0");           //????????? ???????????? ????????? ??????
	  		logData.put("cd_group", "0002");      //????????????
	  		logData.put("cd_code", "0001");		  //?????????
	  		logData.put("mb_idx", mem.getTb_mem_no()); //????????????
	  		logData.put("log_cont", mem.getTb_mem_nm()+"?????? ?????????????????????"); //????????????
		    
			commonService.insertLogData(logData);
		  //System.out.println("????????????");
  		  session.setAttribute("authList", authList); // ????????? login????????? ???????????? UsersVO ????????? ????????? ?????????
  		  //System.out.println("????????????");
          //returnURL = "redirect:/type_chk_main.do"; // ????????? ????????? ?????????????????? ????????????
  		  returnURL = "redirect:/new_main.do";
          
      }else { // ???????????? ????????? ??????
          returnURL = "redirect:/new_main.do"; // ????????? ????????? ?????? ????????? ???
      }
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
      return returnURL; // ????????? ????????? returnURL ??? ???????????? ????????????
  }
  
   
  /**
	 * ??????????????? Ajax Servlet
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/mem_ajax.do" , method=RequestMethod.POST)
	@ResponseBody
	public void mem_ajax(@RequestParam Map<String, String> requestParams, HttpServletResponse response) throws Exception {
		 Map<String, String> result = new HashMap<String, String>();
	      
	      ObjectMapper mapper = new ObjectMapper();
	      
	      Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
	      int cnt = memberService.selectChkMember(hashData);
	      System.out.println("cnt"+cnt+requestParams.get("tb_mem_email"));
	      if(cnt>0){
	    	  result.put("code", "1111");
	      }else{
	    	// ?????? ????????? ??????
		      result.put("code", "0000");
	      }
	      String json = mapper.writeValueAsString(result);
	      
	      response.getWriter().print(json);
	}
	/**
	 * ?????????,???????????? Ajax Servlet
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/mem_login_ajax.do" , method=RequestMethod.POST)
	@ResponseBody
	public void mem_login_ajax(@RequestParam Map<String, String> requestParams, HttpServletResponse response) throws Exception {
		 Map<String, String> result = new HashMap<String, String>();
	      
	      ObjectMapper mapper = new ObjectMapper();
	      
	      Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
	      hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
	      int cnt = memberService.selectLogChkMember(hashData);
	      
	      if(cnt>0){
	    	  result.put("code", "1111");
	      }else{
	    	// ?????? ????????? ??????
		      result.put("code", "0000");
	      }
	      String json = mapper.writeValueAsString(result);
	      System.out.println("json"+json);
	      response.getWriter().print(json);
	}
	/**
	 * ????????? ?????? Ajax Servlet
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_mod_ajax.do" , method=RequestMethod.POST)
	@ResponseBody
	public void type_chk_mod_ajax(@RequestParam Map<String, String> requestParams, HttpServletResponse response) throws Exception {
		 Map<String, String> result = new HashMap<String, String>();
	      
	      ObjectMapper mapper = new ObjectMapper();
	      
	      Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("table_name", requestParams.get("tb_name"));
	      hashData.put("wk_idx", requestParams.get("wk_idx"));
	      hashData.put("mod_yn", requestParams.get("mod_yn"));
	      //int cnt = memberService.selectLogChkMember(hashData);
	      memberService.updateTypeChkMod(hashData);
	      //if(cnt>0){
	    	  result.put("code", "1111");
	      //}else{
	    	// ?????? ????????? ??????
		    //  result.put("code", "0000");
	      //}
	      String json = mapper.writeValueAsString(result);
	      //System.out.println("json"+json);
	      response.getWriter().print(json);
	}
	/**
	 * ???????????? Ajax Servlet
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_state_ajax.do" , method=RequestMethod.POST)
	@ResponseBody
	public void type_chk_state_ajax(@RequestParam Map<String, String> requestParams, HttpServletResponse response) throws Exception {
		 Map<String, String> result = new HashMap<String, String>();
	      
	      ObjectMapper mapper = new ObjectMapper();
	      
	      Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("table_name", requestParams.get("tb_name"));
	      hashData.put("wk_idx", requestParams.get("wk_idx"));
	      hashData.put("noun_list", requestParams.get("noun_list"));
	      //int cnt = memberService.selectLogChkMember(hashData);
	      memberService.updateTypeChkState(hashData);
	      //if(cnt>0){
	    	  result.put("code", "1111");
	      //}else{
	    	// ?????? ????????? ??????
		    //  result.put("code", "0000");
	      //}
	      String json = mapper.writeValueAsString(result);
	      //System.out.println("json"+json);
	      response.getWriter().print(json);
	}
	/**
	 * ?????? ????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/auth_list.do")
	public String auth_list(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {

		//System.out.println("searchVO"+searchVO.getSearchKeyword());
		List<?> sampleList = memberService.selectAuthList(searchVO);
		Object obj = sampleList.get(0);
		model.addAttribute("resultList", sampleList);
		model.addAttribute("au_mem",searchVO.getSearchKeyword());
		//System.out.println("===>"+((HashMap)obj).get("tb_mem_nm"));
		model.addAttribute("mem_nm",((HashMap)obj).get("tb_mem_nm"));
		return "member/auth_list";
	}
	/**
	 * ????????? ?????????????????? (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/auth_update.do")
	public String auth_update(@RequestParam Map<String, String> requestParams,  ModelMap model) throws Exception {
		System.out.println("requestParams"+requestParams.toString());
		String au_mem = requestParams.get("au_mem");//????????????
		SampleDefaultVO searchVO = new SampleDefaultVO();
		searchVO.setSearchKeyword(au_mem);
		List<?> sampleList = memberService.selectAuthList(searchVO);
		for(Object map:sampleList) {
			HashMap hmap = (HashMap)map;
			int mu_idx = (int)hmap.get("mu_idx");
			String au_mod = requestParams.get("au_mod_"+mu_idx)==null?"N":requestParams.get("au_mod_"+mu_idx);
			String au_list = requestParams.get("au_list_"+mu_idx)==null?"N":requestParams.get("au_list_"+mu_idx);
			System.out.println("au_mem"+au_mem+"mu_idx"+mu_idx+"au_mod:"+au_mod+"au_list:"+au_list);
			//????????? ???????????? ????????? ??????.
			Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("au_mem", au_mem);
			hashData.put("au_menu", String.valueOf(mu_idx));
			hashData.put("au_list", au_list);
			hashData.put("au_mod", au_mod);
			memberService.updateAuthData(hashData);
		}
		return "redirect:/member_list.do";
	}
	
	@RequestMapping(value="/work_insert_ok.do") 
	public String work_insert_ok(MultipartHttpServletRequest req,HttpSession session) throws Exception {
		
		System.out.println("?????? ????????? ??????...");
		Iterator<String> itr = req.getFileNames();
		String agent = req.getParameter("agent");
		String bd_title = req.getParameter("bd_title");
		String bd_comment = req.getParameter("bd_comment");
		Member mem = (Member)session.getAttribute("login");
		boolean isLocal = false;
		CommonFileVo FileUploadVo = new CommonFileVo();
		String filePath=ConfigUtil.SERVER_FILE_UPLOAD_PATH;
		
		String requestUrl = new String(req.getRequestURL());
		System.out.println(requestUrl);		
		if (requestUrl.contains("localhost") || requestUrl.contains("127.0.0.1"))
		{
			isLocal = true;
			filePath=ConfigUtil.LOCAL_FILE_UPLOAD_PATH;
		}
		
		if(itr.hasNext()) {
			MultipartFile mpf = req.getFile(itr.next());
			FileUploadVo.setMpfile(mpf);
			CommonFileVo vo = CommonUtils.fileUpload(FileUploadVo, isLocal);
			///DB????????????.
			UploadFileVo inVo = new UploadFileVo();
			inVo.setBd_project(agent);
			inVo.setBd_title(bd_title);
			inVo.setBd_comment(bd_comment);
			inVo.setBd_file_name(vo.getFileName());
			inVo.setBd_org_name(vo.getOrgFileName());
			inVo.setBd_writer(mem.getTb_mem_no());
			/*Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("bd_project", agent);
			hashData.put("bd_title", bd_title);
			hashData.put("bd_comment", bd_comment);
			hashData.put("bd_file_name", vo.getFileName());
			hashData.put("bd_org_name", vo.getOrgFileName());
			hashData.put("bd_writer", mem.getTb_mem_no());*/
			
			memberService.insertWorkFile(inVo);
			System.out.println("inVo===="+inVo.getBd_idx());
			Map<String, Object> hashData = new HashMap<String, Object>();
		    hashData.put("table_name", "tb_work_"+inVo.getBd_idx());
			memberService.createTable(hashData);
			//????????? ????????????
			
			String excel_real = propertiesService.getString("EXCEL_READ"); 
			String[] command = new String[4];
	        command[0] = "python";
	        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
	        command[1] = ConfigUtil.PYTHON_EXEC+"/"+excel_real;
	        command[2] = filePath+"/"+vo.getFileName();
	        command[3] = inVo.getBd_idx();
	        try {
	            String str = CommonUtils.execPython(command,true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		//https://jays-lab.tistory.com/9
		return "redirect:/main.do";
	}
	
	@RequestMapping(value="/spell_insert_ok.do") 
	public String spell_insert_ok(MultipartHttpServletRequest req,HttpSession session) throws Exception {
		
		System.out.println("?????? ????????? ??????...");
		Iterator<String> itr = req.getFileNames();
		String agent = req.getParameter("agent");
		String bd_title = req.getParameter("bd_title");
		String bd_comment = req.getParameter("bd_comment");
		Member mem = (Member)session.getAttribute("login");
		boolean isLocal = false;
		CommonFileVo FileUploadVo = new CommonFileVo();
		String filePath=propertiesService.getString("SERVER_FILE_UPLOAD_PATH");//ConfigUtil.SERVER_FILE_UPLOAD_PATH;
		
		String requestUrl = new String(req.getRequestURL());
		System.out.println(requestUrl);		
		/*if (requestUrl.contains("localhost") || requestUrl.contains("127.0.0.1"))
		{
			isLocal = true;
			filePath=ConfigUtil.LOCAL_FILE_UPLOAD_PATH;
		}*/
		
		if(itr.hasNext()) {
			MultipartFile mpf = req.getFile(itr.next());
			FileUploadVo.setMpfile(mpf);
			CommonFileVo vo = CommonUtils.fileUpload(FileUploadVo, isLocal);
			///DB????????????.
			UploadFileVo inVo = new UploadFileVo();
			inVo.setBd_project(agent);
			inVo.setBd_title(bd_title);
			inVo.setBd_comment(bd_comment);
			inVo.setBd_file_name(vo.getFileName());
			inVo.setBd_org_name(vo.getOrgFileName());
			inVo.setBd_writer(mem.getTb_mem_no());
			inVo.setBd_type("spell"); //???????????????
			/*Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("bd_project", agent);
			hashData.put("bd_title", bd_title);
			hashData.put("bd_comment", bd_comment);
			hashData.put("bd_file_name", vo.getFileName());
			hashData.put("bd_org_name", vo.getOrgFileName());
			hashData.put("bd_writer", mem.getTb_mem_no());*/
			
			memberService.insertWorkFile(inVo);
			System.out.println("inVo===="+inVo.getBd_idx());
			Map<String, Object> hashData = new HashMap<String, Object>();
		    hashData.put("table_name", "tb_spell_"+inVo.getBd_idx());
			memberService.createTable(hashData);
			//????????? ????????????
			
			String excel_read = propertiesService.getString("SPELL_EXCEL_READ"); //??????????????? ????????? ?????????
			String py_path = propertiesService.getString("PYTHON_EXEC"); //????????? ??????
			String db_info = propertiesService.getString("DB_NAME"); //????????? ??????
			String[] command = new String[5];
	        command[0] = "python";
	        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
	        command[1] = py_path+"/"+excel_read;
	        command[2] = filePath+"/"+vo.getFileName();
	        command[3] = inVo.getBd_idx();
	        command[4] = db_info;
	        try {
	            String str = CommonUtils.execPython(command,true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		//https://jays-lab.tistory.com/9
		return "redirect:/spell_main.do";
	}
	
	@RequestMapping(value="/type_chk_insert_ok.do") 
	public String type_chk_insert_ok(MultipartHttpServletRequest req,HttpSession session) throws Exception {
		
		System.out.println("?????? ????????? ??????...");
		Iterator<String> itr = req.getFileNames();
		String agent = req.getParameter("agent");
		String bd_title = req.getParameter("bd_title");
		String bd_comment = req.getParameter("typeSEL"); //????????????
		Member mem = (Member)session.getAttribute("login");
		
		CommonFileVo FileUploadVo = new CommonFileVo();
		String filePath=propertiesService.getString("SERVER_FILE_UPLOAD_PATH");//ConfigUtil.SERVER_FILE_UPLOAD_PATH;
		
		String requestUrl = new String(req.getRequestURL());
		System.out.println(requestUrl);		
				
		if(itr.hasNext()) {
			MultipartFile mpf = req.getFile(itr.next());
			
			FileUploadVo.setMpfile(mpf);
			CommonFileVo vo = CommonUtils.fileUploadType(FileUploadVo, filePath);
			///DB????????????.
			UploadFileVo inVo = new UploadFileVo();
			inVo.setBd_project(agent);
			inVo.setBd_title(bd_title);
			inVo.setBd_comment(bd_comment);
			inVo.setBd_file_name(vo.getFileName());
			inVo.setBd_org_name(vo.getOrgFileName());
			inVo.setBd_writer(mem.getTb_mem_no());
			inVo.setBd_type("type_chk"); //???????????????
			/*Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("bd_project", agent);
			hashData.put("bd_title", bd_title);
			hashData.put("bd_comment", bd_comment);
			hashData.put("bd_file_name", vo.getFileName());
			hashData.put("bd_org_name", vo.getOrgFileName());
			hashData.put("bd_writer", mem.getTb_mem_no());*/
			
			memberService.insertWorkFile(inVo);
			System.out.println("inVo===="+inVo.getBd_idx());
			Map<String, Object> hashData = new HashMap<String, Object>();
		    hashData.put("table_name", "tb_type_chk_"+inVo.getBd_idx());
			memberService.createTable(hashData);
			//????????? ????????????
			
			String excel_read = propertiesService.getString("TYPE_EXCEL_READ"); //??????????????? ????????? ?????????
			String py_path = propertiesService.getString("PYTHON_EXEC"); //????????? ??????
			String db_info = propertiesService.getString("DB_NAME"); //????????? ??????
			String[] command = new String[5];
	        command[0] = "python";
	        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
	        command[1] = py_path+"/"+excel_read;
	        command[2] = filePath+"/"+vo.getFileName();
	        command[3] = inVo.getBd_idx();
	        command[4] = db_info;
	        try {
	            String str = CommonUtils.execPython(command,true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		//https://jays-lab.tistory.com/9
		return "redirect:/type_chk_list.do?agent="+agent;
	}
	@RequestMapping(value="/json_insert_ok.do") 
	public String json_insert_ok(MultipartHttpServletRequest req,HttpSession session) throws Exception {
		
		System.out.println("?????? ????????? ??????...");
		Iterator<String> itr = req.getFileNames();
		String agent = req.getParameter("agent");
		String bd_title = req.getParameter("bd_title");
		String bd_comment = req.getParameter("bd_comment")==null?"":req.getParameter("bd_comment");
		String jsonPrj = req.getParameter("jsonPrj")==null?"hanbat":req.getParameter("jsonPrj");
		Member mem = (Member)session.getAttribute("login");
		boolean isLocal = false;
		CommonFileVo FileUploadVo = new CommonFileVo();
		String filePath = propertiesService.getString("FILE_UPLOAD_PATH"); //??????????????? ??????
		
		String requestUrl = new String(req.getRequestURL());
		System.out.println(requestUrl);		
		if (requestUrl.contains("localhost") || requestUrl.contains("127.0.0.1"))
		{
			isLocal = true;
			filePath=ConfigUtil.LOCAL_FILE_UPLOAD_PATH;
		}
		
		if(itr.hasNext()) {
			MultipartFile mpf = req.getFile(itr.next());
			FileUploadVo.setMpfile(mpf);
			CommonFileVo vo = CommonUtils.fileUpload(FileUploadVo, isLocal);
			//????????????????????????.
			String[] command = new String[5];
			String excel_read = propertiesService.getString("JSON_EXCEL_READ"); //JSON ?????? ????????? ?????????
			String py_path = propertiesService.getString("PYTHON_EXEC"); //????????? ??????
			if(jsonPrj.equals("hanbat")){
				excel_read = propertiesService.getString("JSON_HANBAT_READ"); //?????????
				command[0] = "python";
		        command[1] = py_path+"/"+excel_read;
		        command[2] = filePath+"/"+vo.getFileName(); //???????????????
		        command[3] = filePath+"/"+vo.getFileName().substring(0,vo.getFileName().indexOf("."))+".json"; //???????????????
			}else{
				command[0] = "python";
		        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
		        command[1] = py_path+"/"+excel_read;
		        command[2] = filePath+"/"+vo.getFileName(); //???????????????
		        command[3] = filePath+"/"+"out_"+vo.getFileName(); //???????????????
			}
	        String bd_cnt="0";
	        try {
	        	bd_cnt = CommonUtils.execPython(command,true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			///DB????????????.
			UploadFileVo inVo = new UploadFileVo();
			inVo.setBd_project(jsonPrj);
			inVo.setBd_title(bd_title);
			inVo.setBd_comment(vo.getFileName().substring(0,vo.getFileName().indexOf("."))+".json");
			inVo.setBd_file_name(vo.getFileName());
			inVo.setBd_org_name(vo.getOrgFileName());
			inVo.setBd_writer(mem.getTb_mem_no());
			inVo.setBd_type("json"); //json??????
			inVo.setBd_cnt(bd_cnt);
			memberService.insertWorkFile(inVo);
			/*System.out.println("inVo===="+inVo.getBd_idx());
			Map<String, Object> hashData = new HashMap<String, Object>();
		    hashData.put("table_name", "tb_json_"+inVo.getBd_idx());
			memberService.createTable(hashData);*/
			//????????? ????????????
			
			
		}
		
		//https://jays-lab.tistory.com/9
		return "redirect:/json_main.do";
	}
	/**
	 * ??????????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/work_list.do")
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
		return "work/work_list";
	}
	/**
	 * ????????? ??????????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/spell_list.do")
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
		return "work/spell_list";
	}
	/**
	 * ??????????????? ??????????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_detail.do")
	public String type_chk_detail(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {
		/** EgovPropertyService.sample */
		try {
		searchVO.setPageUnit(100);
		searchVO.setPageSize(20);
		System.out.println("searchVO"+searchVO.toString());
		
		String bd_state = memberService.selectSpellCompCode(searchVO);
		model.addAttribute("bd_state", bd_state); 
		searchVO.setTable_name("tb_type_chk_"+searchVO.getBd_idx());
		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<?> sampleList = memberService.selectSpellDetailList(searchVO);
		
		model.addAttribute("resultList", sampleList); 

		int totCnt = memberService.selectWorkListTotCnt(searchVO);
		int ansCnt = memberService.selectTypeChkAnsTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		double sucRate = ((double)ansCnt/totCnt)*100;
		sucRate = Math.round(sucRate*100)/100.0;
		HashMap<String, String> staMap = new HashMap<String, String>();
		model.put("agent", searchVO.getAgent());
		model.put("bd_idx", searchVO.getBd_idx());
		model.put("searchKeyword", searchVO.getSearchKeyword());
		model.put("updYn", searchVO.getUpdYn());
		model.put("nounList", searchVO.getNounList());
		model.put("sucRate", String.valueOf(sucRate));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "main/type_chk_detail";
	}
	/**
	 * ???????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/spell_chk.do")
	public String spell_chk(HttpSession session,@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {
		try {
		model.put("agent", searchVO.getAgent());
		model.put("bd_idx", searchVO.getBd_idx());
		/*searchVO.setTable_name("tb_work_"+searchVO.getBd_idx());
		List<?> sampleList = memberService.selectWorkTotDetailList(searchVO);*/
		//SelectKeyword word = new SelectKeyword();
		Member mem = (Member)session.getAttribute("login");
		 
		String[] command = new String[5];
        command[0] = "python";
        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
        String file_name = propertiesService.getString("SPELL_CHECK_UPDATE");
        String path = propertiesService.getString("FILE_UPLOAD_PATH");
        String db_name = propertiesService.getString("DB_NAME");
        command[1] = ConfigUtil.PYTHON_EXEC+"/"+file_name;
        command[2] = searchVO.getBd_idx();
        command[3] = path;
        command[4] = db_name;
        String str = CommonUtils.execPython(command,true);

		Map<String, Object> logData = new HashMap<String, Object>();
  		logData.put("log_project", searchVO.getAgent()); //??????????????? ??????????????????
  		logData.put("bd_idx", searchVO.getBd_idx());     //????????? ???????????? ????????? ??????
  		logData.put("cd_group", "0003");      //????????????
  		logData.put("cd_code", "0003");		  //???????????????
  		logData.put("mb_idx", mem.getTb_mem_no()); //????????????
  		logData.put("log_cont", mem.getTb_mem_nm()+"?????? ["+searchVO.getBd_idx()+"] ????????? ?????????????????????"); //?????????
	    
		commonService.insertLogData(logData);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/spell_list.do";
	}
	/**
	 * ??????????????? ??????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/type_chk_process.do")
	public String type_chk_process(HttpSession session,@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {
		try {
		model.put("agent", searchVO.getAgent());
		model.put("bd_idx", searchVO.getBd_idx());
		String bd_chk = searchVO.getSearchKeyword();
		
		/*searchVO.setTable_name("tb_work_"+searchVO.getBd_idx());
		List<?> sampleList = memberService.selectWorkTotDetailList(searchVO);*/
		//SelectKeyword word = new SelectKeyword();
		Member mem = (Member)session.getAttribute("login");
		//1???????????? ??????????????? ????????????.
		searchVO.setTable_name("tb_type_chk_"+searchVO.getBd_idx());
		List<?> sampleList = memberService.selectWorkTotDetailList(searchVO);
		System.out.println("bd_chk===>"+bd_chk);
		for(Object obj:sampleList){
			HashMap map = (HashMap)obj;
			String quest = (String)map.get("quest");
			String typeInsert="";
			//??????????????????
			if(bd_chk.indexOf("ES")>-1){
				//???????????? ???????????? ??? ????????? ??????
				boolean ep = RegexPattern.isEmpty(quest);
				int cnt = quest.indexOf("\n"); //????????? ??????
				if(ep||cnt>-1){
					typeInsert="ES";
					quest=quest.replaceAll("\n", " ");
					quest=quest.trim();
				}
			}
			//??????????????????
			if(bd_chk.indexOf("DS")>-1){
				//???????????? ????????????
				boolean ep = RegexPattern.isDupEmpty(quest);
				if(ep){
					if(typeInsert.equals("")){
						typeInsert="DS";
					}else{
						typeInsert=typeInsert+",DS";
					}
					quest=RegexPattern.rvDupEmpty(quest);
				}
			}
			if(bd_chk.indexOf("SD")>-1){
				//???????????? ??????
				boolean ep = RegexPattern.isSpeChar(quest);
				if(!ep){
					if(typeInsert.equals("")){
						typeInsert="SD";
					}else{
						typeInsert=typeInsert+",SD";
					}
					quest=RegexPattern.rvSpeChar(quest);
				}
			}
			if(bd_chk.indexOf("PU")>-1){
				//????????? ??????
				boolean ep = RegexPattern.isEndChar(quest);
				if(!ep){
					if(typeInsert.equals("")){
						typeInsert="PU";
					}else{
						typeInsert=typeInsert+",PU";
					}
					quest=RegexPattern.addEndChar(quest);
				}
			}
			
			//????????? ???????????? ????????? ??????.
			Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("table_name", "tb_type_chk_"+searchVO.getBd_idx());
			hashData.put("wk_idx", String.valueOf(map.get("wk_idx")));
			hashData.put("answer", quest);
			hashData.put("sp_answer", typeInsert);
			memberService.updateTypeChkData(hashData);
			System.out.println("typeInsert"+typeInsert+"mod quest===>"+quest);
		}
		//????????? ????????? ???????????? ??????
		Map<String,Object> workData = new HashMap<String,Object>();
		workData.put("bd_idx", searchVO.getBd_idx());
		workData.put("bd_state", "0003");
		memberService.updateWorkState(workData);
		//System.out.println(sampleList.toString());
		//2???????????? ????????? ????????? ????????????.
		if(bd_chk.indexOf("SW")>-1||bd_chk.indexOf("CO")>-1){//???????????? ?????????				
			String[] command = new String[6];
	        command[0] = "python";
	        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
	        String file_name = propertiesService.getString("TYPE_CHK_SPELL");
	        String path = propertiesService.getString("FILE_UPLOAD_PATH");
	        String db_name = propertiesService.getString("DB_NAME");
	        String pythonExec = propertiesService.getString("PYTHON_EXEC");
	        command[1] = pythonExec+"/"+file_name;
	        command[2] = searchVO.getBd_idx();
	        command[3] = path;
	        command[4] = db_name;
	        command[5] = bd_chk;
	        String str = CommonUtils.execPython(command,true);
		}
		Map<String, Object> logData = new HashMap<String, Object>();
  		logData.put("log_project", searchVO.getAgent()); //??????????????? ??????????????????
  		logData.put("bd_idx", searchVO.getBd_idx());     //????????? ???????????? ????????? ??????
  		logData.put("cd_group", "0003");      //????????????
  		logData.put("cd_code", "0003");		  //???????????????
  		logData.put("mb_idx", mem.getTb_mem_no()); //????????????
  		logData.put("log_cont", mem.getTb_mem_nm()+"?????? ["+searchVO.getBd_idx()+"] ????????????????????? ???????????????"); //?????????
	    
		commonService.insertLogData(logData);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.put("searchVO", searchVO);
		return "redirect:/type_chk_detail.do";
	}
	/**
	 * ???????????????
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/make_keyword.do")
	public String make_keyword(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {
		
		model.put("agent", searchVO.getAgent());
		model.put("bd_idx", searchVO.getBd_idx());
		searchVO.setTable_name("tb_work_"+searchVO.getBd_idx());
		List<?> sampleList = memberService.selectWorkTotDetailList(searchVO);
		SelectKeyword word = new SelectKeyword();
		for(Object map:sampleList) {
			HashMap hmap = (HashMap)map;
			int wk_idx = (int)hmap.get("wk_idx");
			String noun_list=word.vecToStr(word.getNounVerb((String)hmap.get("quest")));
			String noun_list2=word.vecToStr(word.getNounVerb((String)hmap.get("answer")));
			//????????? ???????????? ????????? ??????.
			Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("table_name", "tb_work_"+searchVO.getBd_idx());
			hashData.put("wk_idx", String.valueOf(wk_idx));
			hashData.put("noun_list", noun_list);
			hashData.put("noun_list2", noun_list2);
			memberService.updateKeywordData(hashData);
		}
		return "redirect:/work_list.do";
	}
	/*
	 * ????????? ???????????? ??????
	 */
	@RequestMapping(value = "/download/downloadFile.do")
	public void downloadFile(
			@RequestParam(value = "requestedFile") String requestedFile,
			HttpServletResponse response) throws Exception {
 
		String uploadPath = propertiesService.getString("FILE_UPLOAD_PATH");
 
		File uFile = new File(uploadPath, requestedFile);
		int fSize = (int) uFile.length();
 
		if (fSize > 0) {
 
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(uFile));
			// String mimetype = servletContext.getMimeType(requestedFile);
			String mimetype = "text/html";
 
			response.setBufferSize(fSize);
			response.setContentType(mimetype);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ requestedFile + "\"");
			response.setContentLength(fSize);
 
			FileCopyUtils.copy(in, response.getOutputStream());
			in.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} else {
			//setContentType??? ???????????? ????????? ????????? ??????
			response.setContentType("application/x-msdownload");
			PrintWriter printwriter = response.getWriter();
			printwriter.println("<html>");
			printwriter.println("<br><br><br><h2>Could not get file name:<br>"
					+ requestedFile + "</h2>");
			printwriter
					.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
			printwriter.println("<br><br><br>&copy; webAccess");
			printwriter.println("</html>");
			printwriter.flush();
			printwriter.close();
		}
	}
	/*
	 * ????????? ???????????? ??????
	 */
	@RequestMapping(value = "/download/downloadTypeChk.do")
	public void downloadTypeChk(
			@RequestParam(value = "requestedFile") String requestedFile,
			HttpServletResponse response) throws Exception {
 
		String uploadPath = propertiesService.getString("FILE_UPLOAD_PATH");
		String fileName = "tb_type_chk_"+requestedFile+".xlsx";
		//????????? ????????? ????????????.
		try{
			String[] command = new String[5];
	        command[0] = "python";
	        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
	        String file_name = propertiesService.getString("TYPE_CHK_MAKE");
	        String path = propertiesService.getString("FILE_UPLOAD_PATH");
	        String db_name = propertiesService.getString("DB_NAME");
	        String pythonExec = propertiesService.getString("PYTHON_EXEC");
	        command[1] = pythonExec+"/"+file_name;
	        command[2] = requestedFile;
	        command[3] = path;
	        command[4] = db_name;
	        String str = CommonUtils.execPython(command,true);
		}catch(Exception e){
			e.printStackTrace();
		}
		File uFile = new File(uploadPath, fileName);
		int fSize = (int) uFile.length();
 
		if (fSize > 0) {
 
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(uFile));
			// String mimetype = servletContext.getMimeType(requestedFile);
			String mimetype = "text/html";
 
			response.setBufferSize(fSize);
			response.setContentType(mimetype);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			response.setContentLength(fSize);
 
			FileCopyUtils.copy(in, response.getOutputStream());
			in.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} else {
			//setContentType??? ???????????? ????????? ????????? ??????
			response.setContentType("application/x-msdownload");
			PrintWriter printwriter = response.getWriter();
			printwriter.println("<html>");
			printwriter.println("<br><br><br><h2>Could not get file name:<br>"
					+ requestedFile + "</h2>");
			printwriter
					.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
			printwriter.println("<br><br><br>&copy; webAccess");
			printwriter.println("</html>");
			printwriter.flush();
			printwriter.close();
		}
	}
	
	/*
	 * ????????? ?????? ???????????? ??????
	 */
	@RequestMapping(value = "/download/downloadMulTypeChk.do")
	public void downloadMulTypeChk(
			@RequestParam(value = "requestedFile") String requestedFile,
			HttpServletResponse response) throws Exception {
 
		String uploadPath = propertiesService.getString("FILE_UPLOAD_PATH");
		String fileName = "tb_type_chk_"+requestedFile+".xlsx";
		//????????? ????????? ????????????.
		try{
			String[] command = new String[5];
	        command[0] = "python";
	        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
	        String file_name = propertiesService.getString("TYPE_CHK_MAKE");
	        String path = propertiesService.getString("FILE_UPLOAD_PATH");
	        String db_name = propertiesService.getString("DB_NAME");
	        String pythonExec = propertiesService.getString("PYTHON_EXEC");
	        command[1] = pythonExec+"/"+file_name;
	        command[2] = requestedFile;
	        command[3] = path;
	        command[4] = db_name;
	        String str = CommonUtils.execPython(command,true);
		}catch(Exception e){
			e.printStackTrace();
		}
		File uFile = new File(uploadPath, fileName);
		int fSize = (int) uFile.length();
 
		if (fSize > 0) {
 
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(uFile));
			// String mimetype = servletContext.getMimeType(requestedFile);
			String mimetype = "text/html";
 
			response.setBufferSize(fSize);
			response.setContentType(mimetype);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			response.setContentLength(fSize);
 
			FileCopyUtils.copy(in, response.getOutputStream());
			in.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} else {
			//setContentType??? ???????????? ????????? ????????? ??????
			response.setContentType("application/x-msdownload");
			PrintWriter printwriter = response.getWriter();
			printwriter.println("<html>");
			printwriter.println("<br><br><br><h2>Could not get file name:<br>"
					+ requestedFile + "</h2>");
			printwriter
					.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
			printwriter.println("<br><br><br>&copy; webAccess");
			printwriter.println("</html>");
			printwriter.flush();
			printwriter.close();
		}
	}
}

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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			///현재 프로젝트와 내권한을 확인하여 프로젝트를 설정하는 메소드
			///내가 가진 권한중에 선택한 권한을 리턴한다.
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
					if(agent.equals("")){//무조건 내가가진 권한을 리턴한다.
						if(mu_idx==Integer.parseInt(pro)&&au_list.equals("Y")){
							reAgent = (String)authMap.get("mu_method");
							break;
						}
					}else{//선택한 권한을 리턴한다
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			///현재 프로젝트와 내권한을 확인하여 프로젝트를 설정하는 메소드
			///내가 가진 권한중에 선택한 권한을 리턴한다.
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
					if(agent.equals("")){//무조건 내가가진 권한을 리턴한다.
						if(mu_idx==Integer.parseInt(pro)&&au_list.equals("Y")){
							reAgent = (String)authMap.get("mu_method");
							break;
						}
					}else{//선택한 권한을 리턴한다
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
			
			if(!searchKeyword.equals("")) {//한건 맞춤법 검사를 진행한다
				String[] command = new String[4];
		        command[0] = "python";
		        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
		        command[1] = ConfigUtil.PYTHON_EXEC+"/spellTest.py";
				try {
					        	command[2] = searchKeyword;
					            String str = CommonUtils.execPython(command,true);
					            String result = "[일치]";
					            String reStr = CommonUtils.getDecode(str);
					            if(!reStr.equals(searchKeyword)) {
					            	result = "[불일치]";
					            }
					            sp_quest = "맞춤법 검사결과 "+result+" : "+reStr;
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			///현재 프로젝트와 내권한을 확인하여 프로젝트를 설정하는 메소드
			///내가 가진 권한중에 선택한 권한을 리턴한다.
			
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			///현재 프로젝트와 내권한을 확인하여 프로젝트를 설정하는 메소드
			///내가 가진 권한중에 선택한 권한을 리턴한다.
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
					if(agent.equals("")){//무조건 내가가진 권한을 리턴한다.
						if(mu_idx==Integer.parseInt(pro)&&au_list.equals("Y")){
							reAgent = (String)authMap.get("mu_method");
							break;
						}
					}else{//선택한 권한을 리턴한다
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
			if(!searchKeyword.equals("")) {//한건 맞춤법 검사를 진행한다
				
				//앞뒤공백검사
				if(typeSEL.indexOf("ES")>-1){
					//문장앞뒤 공백제거
					boolean ep = RegexPattern.isEmpty(searchKeyword);
					if(ep){
						typeInsert="ES";
						searchKeyword=searchKeyword.trim();
					}
				}
				//중복공백검사
				if(typeSEL.indexOf("DS")>-1){
					//문장앞뒤 공백제거
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
					//특수문자 제거
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
					//구두점 검사
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
				if(typeSEL.indexOf("SW")>-1 || typeSEL.indexOf("CO")>-1){//띄어쓰기 맞춤법
					String pythonExec = propertiesService.getString("PYTHON_EXEC");
					String[] command = new String[4];
			        command[0] = "python";
			        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
			        command[1] = pythonExec+"/spellTest.py";
					try {
						        	command[2] = searchKeyword;
						            String str = CommonUtils.execPython(command,true);
						            
						            String reStr = CommonUtils.getDecode(str);
						            ///둘중에 하나라도 있으면 체크해서 값을 가져온다
						            
						            //완벽하게 안바뀐경우 둘다 값이 없다
						            //띄어쓰기만 바뀐경우
						               //띄어쓰기 선택시 바뀐것 선택
						               //띄어쓰기 미선택시 안바뀐것 선택
						            //맞춤법이 바뀐경우
						               //맞춤법 선택시 맞춤법 선택
						            if(!reStr.equals(searchKeyword)) {//뭔가가 바뀌었다
						            	//띄어쓰기 없애고 두값을 비교한다
						            	String rStr = reStr.replaceAll(" ", "");
						            	String sStr = searchKeyword.replaceAll(" ","");
						            	//System.out.println("rStr"+rStr);		            	
						            	//System.out.println("sStr"+sStr);
						            	if(rStr.equals(sStr)) {//띄어쓰기만 바뀌었다
						            		//띄어쓰기 있으면
						            		if(typeSEL.indexOf("SW")>-1) {
						            			if(typeInsert.equals("")){
													typeInsert="SW";
												}else{
													typeInsert=typeInsert+",SW";
												}
						            			searchKeyword=reStr; //반영
						            		}
						            	}else {//맞춤법이 바뀌었다
						            		if(typeSEL.indexOf("CO")>-1) {
						            			if(typeInsert.equals("")){
													typeInsert="CO";
												}else{
													typeInsert=typeInsert+",CO";
												}
						            			searchKeyword=reStr; //반영
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			//하나씩 삭제한다.
			
		}
		return "redirect:/json_main.do";
	}
	
	/**
	 * 형태적합성 삭제
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			//하나씩 삭제한다.
			memberService.deleteTypeChkData(arrIdx[i]);
		}
		return "redirect:/type_chk_list.do";
	}
	
	/**
	 * 회원 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 로그인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/login.do")
	public String login(ModelMap model) throws Exception {
		
		return "member/login";
	}
	
	/**
	 * 트위터크롤링 리스트
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			    System.out.println( "파일 생성 날짜 및 시간은 다음과 같습니다.: " + formatted );
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
	 * 트위터크롤링
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 트위터크롤링
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/twitter_content.do")
	public String twitter_content(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		String fName = requestParams.get("fName")==null?"":requestParams.get("fName");
		System.out.println("fName"+fName);
		try{
            //파일 객체 생성
            File file = new File(ConfigUtil.PYTHON_SEARCH+"/"+fName);
            //스캐너로 파일 읽기
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
	 * 사용자등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_insert.do")
	public String member_insert(ModelMap model) throws Exception {

		return "member/member_insert";
	}

	/**
	 * 작업등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 맞춤법 작업등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * JSON 작업등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * PDF 작업등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 사용자수정
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_update.do")
	public String member_update(@RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
		//멤버를 불러온다.
		Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("tb_mem_no", requestParams.get("tb_mem_no"));
	      Member member = memberService.selectMemberNo(hashData);
	      System.out.println("-->"+member.getTb_mem_tel());
	      model.addAttribute("member", member);
		return "member/member_update";
	}
	
	/**
	 * 사용자수정
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/my_update.do")
	public String my_update(HttpSession session, @RequestParam Map<String, String> requestParams,ModelMap model) throws Exception {
		//멤버를 불러온다.
		  Member mem = (Member)session.getAttribute("login");
		  Map<String, Object> hashData = new HashMap<String, Object>();
	      hashData.put("tb_mem_no", mem.getTb_mem_no());
	      Member member = memberService.selectMemberNo(hashData);
	      model.addAttribute("member", member);
		return "member/my_update";
	}
	
	/**
	 * 사용자수정
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 사용자수정
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 사용자등록 등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/member_insert_ok.do")
	public String member_insert_ok(@RequestParam Map<String, String> requestParams, ModelMap model) throws Exception {
		System.out.println("여기서 인서트 한다.....");
		
		Map<String, Object> hashData = new HashMap<String, Object>();
	    hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
	    hashData.put("tb_mem_nm", requestParams.get("tb_mem_nm"));
	    hashData.put("tb_mem_tel", requestParams.get("tb_mem_tel"));
	    hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
	    
		memberService.insertMember(hashData);
		return "redirect:/member_list.do";
	}
	
	/**
	 * 로그아웃
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/logOut.do")
	public String logOut(ModelMap model, HttpSession session) throws Exception {
		if ( session.getAttribute("login") != null ){
	          // 기존에 login이란 세션 값이 존재한다면
			  Member mem = (Member)session.getAttribute("login");
	          session.removeAttribute("login"); // 기존값을 제거해 준다.
	          session.removeAttribute("authList");
	        //로그아웃 성공시 로그기록
	  		Map<String, Object> logData = new HashMap<String, Object>();
	  		logData.put("log_project", "common"); //프로젝트명 공통프로젝트
	  		logData.put("bd_idx", "0");           //작업이 결정되지 않았을 경우
	  		logData.put("cd_group", "0002");      //회원관련
	  		logData.put("cd_code", "0002");		  //로그아웃
	  		logData.put("mb_idx", mem.getTb_mem_no()); //회원번호
	  		logData.put("log_cont", mem.getTb_mem_nm()+"님이 로그아웃 했습니다"); //회원명
		    
			commonService.insertLogData(logData);
	    }
		return "member/login";
	}
   // 로그인 submit
  @RequestMapping(value = "/loginPost", method = RequestMethod.POST)
  public String loginPost(@RequestParam Map<String, String> requestParams, Model model, HttpSession session) throws Exception {
	  String returnURL = "";
	  try {
	  
      if ( session.getAttribute("login") != null ){
          // 기존에 login이란 세션 값이 존재한다면
          session.removeAttribute("login"); // 기존값을 제거해 준다.
      }
      
      // 로그인이 성공하면 UsersVO 객체를 반환함.
      //UsersVO vo = service.getUser(UsersVO);
     
      Map<String, Object> hashData = new HashMap<String, Object>();
      hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
      hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
      Member mem = memberService.selectMember(hashData);
      //세션에 권한 넣기
      
      if ( mem != null ){ // 로그인 성공
          session.setAttribute("login", mem); // 세션에 login인이란 이름으로 UsersVO 객체를 저장해 놈.
          String au_mem = mem.getTb_mem_no();//회원번호
  		  SampleDefaultVO searchVO = new SampleDefaultVO();
  		  searchVO.setSearchKeyword(au_mem);
  		  List<?> authList = memberService.selectAuthList(searchVO);
  		    
  		    //로그인 성공시 로그기록
	  		Map<String, Object> logData = new HashMap<String, Object>();
	  		logData.put("log_project", "common"); //프로젝트명 공통프로젝트
	  		logData.put("bd_idx", "0");           //작업이 결정되지 않았을 경우
	  		logData.put("cd_group", "0002");      //회원관련
	  		logData.put("cd_code", "0001");		  //로그인
	  		logData.put("mb_idx", mem.getTb_mem_no()); //회원번호
	  		logData.put("log_cont", mem.getTb_mem_nm()+"님이 로그인했습니다"); //회원번호
		    
			commonService.insertLogData(logData);
		  //System.out.println("로그저장");
  		  session.setAttribute("authList", authList); // 세션에 login인이란 이름으로 UsersVO 객체를 저장해 놓은다
  		  //System.out.println("세션적용");
          returnURL = "redirect:/type_chk_main.do"; // 로그인 성공시 메인페이지로 이동하고
          
      }else { // 로그인에 실패한 경우
          returnURL = "redirect:/login.do"; // 로그인 폼으로 다시 가도록 함
      }
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
      return returnURL; // 위에서 설정한 returnURL 을 반환해서 이동시킴
  }
  
   
  /**
	 * 이메일체크 Ajax Servlet
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	    	// 응답 데이터 셋팅
		      result.put("code", "0000");
	      }
	      String json = mapper.writeValueAsString(result);
	      
	      response.getWriter().print(json);
	}
	/**
	 * 이메일,비번체크 Ajax Servlet
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	    	// 응답 데이터 셋팅
		      result.put("code", "0000");
	      }
	      String json = mapper.writeValueAsString(result);
	      System.out.println("json"+json);
	      response.getWriter().print(json);
	}
	/**
	 * 수정안 채택 Ajax Servlet
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	    	// 응답 데이터 셋팅
		    //  result.put("code", "0000");
	      //}
	      String json = mapper.writeValueAsString(result);
	      //System.out.println("json"+json);
	      response.getWriter().print(json);
	}
	/**
	 * 검수상태 Ajax Servlet
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	    	// 응답 데이터 셋팅
		    //  result.put("code", "0000");
	      //}
	      String json = mapper.writeValueAsString(result);
	      //System.out.println("json"+json);
	      response.getWriter().print(json);
	}
	/**
	 * 메뉴 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 권한을 업데이트한다 (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/auth_update.do")
	public String auth_update(@RequestParam Map<String, String> requestParams,  ModelMap model) throws Exception {
		System.out.println("requestParams"+requestParams.toString());
		String au_mem = requestParams.get("au_mem");//회원번호
		SampleDefaultVO searchVO = new SampleDefaultVO();
		searchVO.setSearchKeyword(au_mem);
		List<?> sampleList = memberService.selectAuthList(searchVO);
		for(Object map:sampleList) {
			HashMap hmap = (HashMap)map;
			int mu_idx = (int)hmap.get("mu_idx");
			String au_mod = requestParams.get("au_mod_"+mu_idx)==null?"N":requestParams.get("au_mod_"+mu_idx);
			String au_list = requestParams.get("au_list_"+mu_idx)==null?"N":requestParams.get("au_list_"+mu_idx);
			System.out.println("au_mem"+au_mem+"mu_idx"+mu_idx+"au_mod:"+au_mod+"au_list:"+au_list);
			//하나씩 업데이트 인서트 한다.
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
		
		System.out.println("파일 업로드 시작...");
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
			///DB입력한다.
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
			//테이블 생성한다
			
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
		
		System.out.println("파일 업로드 시작...");
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
			///DB입력한다.
			UploadFileVo inVo = new UploadFileVo();
			inVo.setBd_project(agent);
			inVo.setBd_title(bd_title);
			inVo.setBd_comment(bd_comment);
			inVo.setBd_file_name(vo.getFileName());
			inVo.setBd_org_name(vo.getOrgFileName());
			inVo.setBd_writer(mem.getTb_mem_no());
			inVo.setBd_type("spell"); //맞춤법검사
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
			//테이블 생성한다
			
			String excel_read = propertiesService.getString("SPELL_EXCEL_READ"); //맞춤법엑셀 업로드 파이썬
			String py_path = propertiesService.getString("PYTHON_EXEC"); //파이썬 경로
			String db_info = propertiesService.getString("DB_NAME"); //파이썬 경로
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
		
		System.out.println("파일 업로드 시작...");
		Iterator<String> itr = req.getFileNames();
		String agent = req.getParameter("agent");
		String bd_title = req.getParameter("bd_title");
		String bd_comment = req.getParameter("typeSEL"); //선택항목
		Member mem = (Member)session.getAttribute("login");
		
		CommonFileVo FileUploadVo = new CommonFileVo();
		String filePath=propertiesService.getString("SERVER_FILE_UPLOAD_PATH");//ConfigUtil.SERVER_FILE_UPLOAD_PATH;
		
		String requestUrl = new String(req.getRequestURL());
		System.out.println(requestUrl);		
				
		if(itr.hasNext()) {
			MultipartFile mpf = req.getFile(itr.next());
			
			FileUploadVo.setMpfile(mpf);
			CommonFileVo vo = CommonUtils.fileUploadType(FileUploadVo, filePath);
			///DB입력한다.
			UploadFileVo inVo = new UploadFileVo();
			inVo.setBd_project(agent);
			inVo.setBd_title(bd_title);
			inVo.setBd_comment(bd_comment);
			inVo.setBd_file_name(vo.getFileName());
			inVo.setBd_org_name(vo.getOrgFileName());
			inVo.setBd_writer(mem.getTb_mem_no());
			inVo.setBd_type("type_chk"); //맞춤법검사
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
			//테이블 생성한다
			
			String excel_read = propertiesService.getString("TYPE_EXCEL_READ"); //맞춤법엑셀 업로드 파이썬
			String py_path = propertiesService.getString("PYTHON_EXEC"); //파이썬 경로
			String db_info = propertiesService.getString("DB_NAME"); //파이썬 경로
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
		
		System.out.println("파일 업로드 시작...");
		Iterator<String> itr = req.getFileNames();
		String agent = req.getParameter("agent");
		String bd_title = req.getParameter("bd_title");
		String bd_comment = req.getParameter("bd_comment")==null?"":req.getParameter("bd_comment");
		String jsonPrj = req.getParameter("jsonPrj")==null?"hanbat":req.getParameter("jsonPrj");
		Member mem = (Member)session.getAttribute("login");
		boolean isLocal = false;
		CommonFileVo FileUploadVo = new CommonFileVo();
		String filePath = propertiesService.getString("FILE_UPLOAD_PATH"); //파일업로드 패스
		
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
			//파일변환수행한다.
			String[] command = new String[5];
			String excel_read = propertiesService.getString("JSON_EXCEL_READ"); //JSON 엑셀 업로드 파이썬
			String py_path = propertiesService.getString("PYTHON_EXEC"); //파이썬 경로
			if(jsonPrj.equals("hanbat")){
				excel_read = propertiesService.getString("JSON_HANBAT_READ"); //한밭대
				command[0] = "python";
		        command[1] = py_path+"/"+excel_read;
		        command[2] = filePath+"/"+vo.getFileName(); //읽을파일명
		        command[3] = filePath+"/"+vo.getFileName().substring(0,vo.getFileName().indexOf("."))+".json"; //생성파일명
			}else{
				command[0] = "python";
		        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
		        command[1] = py_path+"/"+excel_read;
		        command[2] = filePath+"/"+vo.getFileName(); //읽을파일명
		        command[3] = filePath+"/"+"out_"+vo.getFileName(); //생성파일명
			}
	        String bd_cnt="0";
	        try {
	        	bd_cnt = CommonUtils.execPython(command,true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			///DB입력한다.
			UploadFileVo inVo = new UploadFileVo();
			inVo.setBd_project(jsonPrj);
			inVo.setBd_title(bd_title);
			inVo.setBd_comment(vo.getFileName().substring(0,vo.getFileName().indexOf("."))+".json");
			inVo.setBd_file_name(vo.getFileName());
			inVo.setBd_org_name(vo.getOrgFileName());
			inVo.setBd_writer(mem.getTb_mem_no());
			inVo.setBd_type("json"); //json변환
			inVo.setBd_cnt(bd_cnt);
			memberService.insertWorkFile(inVo);
			/*System.out.println("inVo===="+inVo.getBd_idx());
			Map<String, Object> hashData = new HashMap<String, Object>();
		    hashData.put("table_name", "tb_json_"+inVo.getBd_idx());
			memberService.createTable(hashData);*/
			//테이블 생성한다
			
			
		}
		
		//https://jays-lab.tistory.com/9
		return "redirect:/json_main.do";
	}
	/**
	 * 작업내용을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 맞춤법 작업내용을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 형태정합성 작업내용을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 맞춤법체크
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
  		logData.put("log_project", searchVO.getAgent()); //프로젝트명 공통프로젝트
  		logData.put("bd_idx", searchVO.getBd_idx());     //작업이 결정되지 않았을 경우
  		logData.put("cd_group", "0003");      //작업관련
  		logData.put("cd_code", "0003");		  //맞춤법체크
  		logData.put("mb_idx", mem.getTb_mem_no()); //회원번호
  		logData.put("log_cont", mem.getTb_mem_nm()+"님이 ["+searchVO.getBd_idx()+"] 맞춤법 체크하였습니다"); //회원명
	    
		commonService.insertLogData(logData);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/spell_list.do";
	}
	/**
	 * 형태적합성 체크
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
		//1차적으로 기본체크를 수행한다.
		searchVO.setTable_name("tb_type_chk_"+searchVO.getBd_idx());
		List<?> sampleList = memberService.selectWorkTotDetailList(searchVO);
		System.out.println("bd_chk===>"+bd_chk);
		for(Object obj:sampleList){
			HashMap map = (HashMap)obj;
			String quest = (String)map.get("quest");
			String typeInsert="";
			//앞뒤공백검사
			if(bd_chk.indexOf("ES")>-1){
				//문장앞뒤 공백제거 및 줄바꿈 제거
				boolean ep = RegexPattern.isEmpty(quest);
				int cnt = quest.indexOf("\n"); //줄바꿈 제거
				if(ep||cnt>-1){
					typeInsert="ES";
					quest=quest.replaceAll("\n", " ");
					quest=quest.trim();
				}
			}
			//중복공백검사
			if(bd_chk.indexOf("DS")>-1){
				//문장앞뒤 공백제거
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
				//특수문자 제거
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
				//구두점 검사
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
			
			//하나씩 업데이트 인서트 한다.
			Map<String,Object> hashData = new HashMap<String,Object>();
			hashData.put("table_name", "tb_type_chk_"+searchVO.getBd_idx());
			hashData.put("wk_idx", String.valueOf(map.get("wk_idx")));
			hashData.put("answer", quest);
			hashData.put("sp_answer", typeInsert);
			memberService.updateTypeChkData(hashData);
			System.out.println("typeInsert"+typeInsert+"mod quest===>"+quest);
		}
		//여기서 완료로 업데이트 한다
		Map<String,Object> workData = new HashMap<String,Object>();
		workData.put("bd_idx", searchVO.getBd_idx());
		workData.put("bd_state", "0003");
		memberService.updateWorkState(workData);
		//System.out.println(sampleList.toString());
		//2차적으로 맞춤법 체크를 수행한다.
		if(bd_chk.indexOf("SW")>-1||bd_chk.indexOf("CO")>-1){//띄어쓰기 맞춤법				
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
  		logData.put("log_project", searchVO.getAgent()); //프로젝트명 공통프로젝트
  		logData.put("bd_idx", searchVO.getBd_idx());     //작업이 결정되지 않았을 경우
  		logData.put("cd_group", "0003");      //작업관련
  		logData.put("cd_code", "0003");		  //맞춤법체크
  		logData.put("mb_idx", mem.getTb_mem_no()); //회원번호
  		logData.put("log_cont", mem.getTb_mem_nm()+"님이 ["+searchVO.getBd_idx()+"] 형태적합성검사 하였습니다"); //회원명
	    
		commonService.insertLogData(logData);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.put("searchVO", searchVO);
		return "redirect:/type_chk_detail.do";
	}
	/**
	 * 키워드추출
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
			//하나씩 업데이트 인서트 한다.
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
	 * 파일을 다운로드 한다
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
			//setContentType을 프로젝트 환경에 맞추어 변경
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
	 * 파일을 다운로드 한다
	 */
	@RequestMapping(value = "/download/downloadTypeChk.do")
	public void downloadTypeChk(
			@RequestParam(value = "requestedFile") String requestedFile,
			HttpServletResponse response) throws Exception {
 
		String uploadPath = propertiesService.getString("FILE_UPLOAD_PATH");
		String fileName = "tb_type_chk_"+requestedFile+".xlsx";
		//여기서 파일을 생성한다.
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
			//setContentType을 프로젝트 환경에 맞추어 변경
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
	 * 파일을 멀티 다운로드 한다
	 */
	@RequestMapping(value = "/download/downloadMulTypeChk.do")
	public void downloadMulTypeChk(
			@RequestParam(value = "requestedFile") String requestedFile,
			HttpServletResponse response) throws Exception {
 
		String uploadPath = propertiesService.getString("FILE_UPLOAD_PATH");
		String fileName = "tb_type_chk_"+requestedFile+".xlsx";
		//여기서 파일을 생성한다.
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
			//setContentType을 프로젝트 환경에 맞추어 변경
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

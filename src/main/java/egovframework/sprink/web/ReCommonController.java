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
 * @ 2022.05.09           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2022. 05.09
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
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
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/reMain.do")
	public String main() throws Exception {
		System.out.println("reDesign/main");
		return "reDesign/main";
	}
	
	/**
	 * 로그인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/reLogin.do")
	public String login(ModelMap model) throws Exception {
		
		return "reDesign/login";
	}
	
//	 // 로그인 submit
//	  @RequestMapping(value = "/loginPost", method = RequestMethod.POST)
//	  public String loginPost(@RequestParam Map<String, String> requestParams, Model model, HttpSession session) throws Exception {
//		  String returnURL = "";
//		  try {
//		  
//	      if ( session.getAttribute("login") != null ){
//	          // 기존에 login이란 세션 값이 존재한다면
//	          session.removeAttribute("login"); // 기존값을 제거해 준다.
//	      }
//	      
//	      // 로그인이 성공하면 UsersVO 객체를 반환함.
//	      //UsersVO vo = service.getUser(UsersVO);
//	     
//	      Map<String, Object> hashData = new HashMap<String, Object>();
//	      hashData.put("tb_mem_email", requestParams.get("tb_mem_email"));
//	      hashData.put("tb_mem_pwd", requestParams.get("tb_mem_pwd"));
//	      Member mem = memberService.selectMember(hashData);
//	      //세션에 권한 넣기
//	      
//	      if ( mem != null ){ // 로그인 성공
//	          session.setAttribute("login", mem); // 세션에 login인이란 이름으로 UsersVO 객체를 저장해 놈.
//	          String au_mem = mem.getTb_mem_no();//회원번호
//	  		  SampleDefaultVO searchVO = new SampleDefaultVO();
//	  		  searchVO.setSearchKeyword(au_mem);
//	  		  List<?> authList = memberService.selectAuthList(searchVO);
//	  		    
//	  		    //로그인 성공시 로그기록
//		  		Map<String, Object> logData = new HashMap<String, Object>();
//		  		logData.put("log_project", "common"); //프로젝트명 공통프로젝트
//		  		logData.put("bd_idx", "0");           //작업이 결정되지 않았을 경우
//		  		logData.put("cd_group", "0002");      //회원관련
//		  		logData.put("cd_code", "0001");		  //로그인
//		  		logData.put("mb_idx", mem.getTb_mem_no()); //회원번호
//		  		logData.put("log_cont", mem.getTb_mem_nm()+"님이 로그인했습니다"); //회원번호
//			    
//				commonService.insertLogData(logData);
//			  //System.out.println("로그저장");
//	  		  session.setAttribute("authList", authList); // 세션에 login인이란 이름으로 UsersVO 객체를 저장해 놓은다
//	  		  //System.out.println("세션적용");
//	          returnURL = "redirect:/type_chk_main.do"; // 로그인 성공시 메인페이지로 이동하고
//	          
//	      }else { // 로그인에 실패한 경우
//	          returnURL = "redirect:/relogin.do"; // 로그인 폼으로 다시 가도록 함
//	      }
//		  }catch(Exception e) {
//			  e.printStackTrace();
//		  }
//	      return returnURL; // 위에서 설정한 returnURL 을 반환해서 이동시킴
//	  }
	
	/**
	 * 맞춤법 검사
	 * 메인
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
		return "reDesign/spell_main";
	}
	
	/**
	 * worklist
	 * 작업내용을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 작업등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 맞춤법 작업내용을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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
	 * 맞춤법 작업등록
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
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

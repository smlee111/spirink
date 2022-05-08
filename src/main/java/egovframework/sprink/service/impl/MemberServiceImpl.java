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
package egovframework.sprink.service.impl;

import java.util.List;
import java.util.Map;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.impl.SampleDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.sprink.dto.Member;
import egovframework.sprink.dto.UploadFileVo;
import egovframework.sprink.service.MemberService;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



/**
 * @Class Name : EgovSampleServiceImpl.java
 * @Description : Sample Business Implement Class
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



@Service("memberService")
public class MemberServiceImpl extends EgovAbstractServiceImpl implements MemberService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);

	@Resource(name = "memberDAO")
	private MemberDAO memberDAO;
	
	@Override
	public Member selectMember(Map<String, Object> hashData) throws Exception {
		
		return memberDAO.selectMember(hashData);
	}

	@Override
	public Member selectMemberNo(Map<String, Object> hashData) throws Exception {
		
		return memberDAO.selectMemberNo(hashData);
	}
	
	@Override
	public int selectChkMember(Map<String, Object> hashData) throws Exception {
		
		return memberDAO.selectChkMember(hashData);
	}
	
	@Override
	public int selectLogChkMember(Map<String, Object> hashData) throws Exception {
		
		return memberDAO.selectLogChkMember(hashData);
	}
	
	@Override
	public List<?> selectMemberList(SampleDefaultVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectMemberList(searchVO);
	}

	@Override
	public List<?> selectWorkList(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectWorkList(hashData);
	}
	@Override
	public List<?> selectSpellList(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectSpellList(hashData);
	}
	@Override
	public List<?> selectJsonList(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectJsonList(hashData);
	}
	@Override
	public int selectMemberListTotCnt(SampleDefaultVO searchVO) {
		// TODO Auto-generated method stub
		return memberDAO.selectMemberListTotCnt(searchVO);
	}

	@Override
	public void insertMember(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.insertMember(hashData);
	}
	@Override
	public void insertWorkFile(UploadFileVo vo) throws Exception {
		// TODO Auto-generated method stub
		 memberDAO.insertWorkFile(vo);
	}
	@Override
	public void createTable(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		 memberDAO.createTable(hashData);
	}
	@Override
	public void updateMember(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateMember(hashData);
	}
	@Override
	public List<?> selectAuthList(SampleDefaultVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectAuthList(searchVO);
	}

	@Override
	public List<?> selectWorkDetailList(SampleDefaultVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectWorkDetailList(searchVO);
	}
	@Override
	public List<?> selectSpellDetailList(SampleDefaultVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectSpellDetailList(searchVO);
	}
	@Override
	public List<?> selectWorkTotDetailList(SampleDefaultVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectWorkTotDetailList(searchVO);
	}
	@Override
	public void updateAuthData(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateAuthData(hashData);
	}
	@Override
	public void updateTypeChkMod(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateTypeChkMod(hashData);
	}
	@Override
	public void updateTypeChkState(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateTypeChkState(hashData);
	}
	@Override
	public void updateWorkState(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateWorkState(hashData);
	}
	
	@Override
	public void updateKeywordData(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateKeywordData(hashData);
	}
	@Override
	public void updateTypeChkData(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateTypeChkData(hashData);
	}
	@Override
	public void updateSpellData(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		memberDAO.updateSpellData(hashData);
	}
	@Override
	public int selectWorkListTotCnt(SampleDefaultVO searchVO) {
		// TODO Auto-generated method stub
		return memberDAO.selectWorkListTotCnt(searchVO);
	}
	
	@Override
	public int selectWorkAnsTotCnt(SampleDefaultVO searchVO) {
		// TODO Auto-generated method stub
		return memberDAO.selectWorkAnsTotCnt(searchVO);
	}
	@Override
	public int selectTypeChkAnsTotCnt(SampleDefaultVO searchVO) {
		// TODO Auto-generated method stub
		return memberDAO.selectTypeChkAnsTotCnt(searchVO);
	}
	@Override
	public String selectSpellCompCode(SampleDefaultVO searchVO) {
		// TODO Auto-generated method stub
		return memberDAO.selectSpellCompCode(searchVO);
	}
	
	public void deleteJsonData(String bd_idx){
		memberDAO.deleteJsonData(bd_idx);
	}
	public void deleteTypeChkData(String bd_idx){
		memberDAO.deleteTypeChkData(bd_idx);
	}
	@Override
	public List<?> selectTypeChkList(Map<String, Object> hashData) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.selectTypeChkList(hashData);
	}
	
}

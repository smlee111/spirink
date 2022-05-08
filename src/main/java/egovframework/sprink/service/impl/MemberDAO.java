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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.sprink.dto.Member;
import egovframework.sprink.dto.UploadFileVo;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : SampleDAO.java
 * @Description : Sample DAO Class
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

@Repository("memberDAO")
public class MemberDAO extends EgovAbstractDAO {

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public String insertSample(SampleVO vo) throws Exception {
		return (String) insert("sampleDAO.insertSample", vo);
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateSample(SampleVO vo) throws Exception {
		update("sampleDAO.updateSample", vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void deleteSample(SampleVO vo) throws Exception {
		delete("sampleDAO.deleteSample", vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public Member selectMember(Map<String, Object> hashData) throws Exception {
		return (Member) select("memberDAO.selectMember", hashData);
	}
	
	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public Member selectMemberNo(Map<String, Object> hashData) throws Exception {
		return (Member) select("memberDAO.selectMemberNo", hashData);
	}
	
	
	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public int selectChkMember(Map<String, Object> hashData) throws Exception {
		return (Integer) select("memberDAO.selectChkMember", hashData);
	}
	
	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public int selectLogChkMember(Map<String, Object> hashData) throws Exception {
		return (Integer) select("memberDAO.selectLogChkMember", hashData);
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectSampleList(SampleDefaultVO searchVO) throws Exception {
		return list("sampleDAO.selectSampleList", searchVO);
	}
	
	/**
	 * 회원 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectMemberList(SampleDefaultVO searchVO) throws Exception {
		return list("memberDAO.selectMemberList", searchVO);
	}
	
	/**
	 * 회원 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectWorkList(Map<String, Object> hashData) throws Exception {
		return list("memberDAO.selectWorkList", hashData);
	}
	
	/**
	 * 맞춤법 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectSpellList(Map<String, Object> hashData) throws Exception {
		return list("memberDAO.selectSpellList", hashData);
	}
	
	/**
	 * 형태적합성검사 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectTypeChkList(Map<String, Object> hashData) throws Exception {
		return list("memberDAO.selectTypeChkList", hashData);
	}
	
	/**
	 * JSON 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectJsonList(Map<String, Object> hashData) throws Exception {
		return list("memberDAO.selectJsonList", hashData);
	}
	
	/**
	 * 테스트 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectTestList(SampleDefaultVO searchVO) throws Exception {
		return list("sampleDAO.selectTestList", searchVO);
	}
	/**
	 * 테스트 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectTestTotList() throws Exception {
		return list("sampleDAO.selectTestTotList");
	}
	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectTotalList(SampleDefaultVO searchVO) throws Exception {
		return list("sampleDAO.selectTotalList", searchVO);
	}
	
	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectExtensionTotList(SampleDefaultVO searchVO) throws Exception {
		return list("sampleDAO.selectExtensionTotList", searchVO);
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectExtensionList(SampleDefaultVO searchVO) throws Exception {
		return list("sampleDAO.selectExtensionList", searchVO);
	}
	
	public Map<String, Object> selectTotalData(Map<String, Object> hashData) throws Exception{
		return (Map<String, Object>) select("sampleDAO.selectTotalData", hashData);
	}
	public Map<String, Object> selectExtensionData(Map<String, Object> hashData) throws Exception{
		return (Map<String, Object>) select("sampleDAO.selectExtensionData", hashData);
	}
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectSampleListTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("sampleDAO.selectSampleListTotCnt", searchVO);
	}
	
	/**
	 * 회원총인원수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectMemberListTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("memberDAO.selectMemberListTotCnt", searchVO);
	}
	
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectTestListTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("sampleDAO.selectTestListTotCnt", searchVO);
	}
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectTestAnsTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("sampleDAO.selectTestAnsTotCnt", searchVO);
	}
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectExtensionListTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("sampleDAO.selectExtensionListTotCnt", searchVO);
	}
	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateTotalData(Map<String, Object> hashData) throws Exception {
		update("sampleDAO.updateTotalData", hashData);
	}
	/**
	 * 테스트글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateTestData(Map<String, Object> hashData) throws Exception {
		update("sampleDAO.updateTestData", hashData);
	}
	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateExtensionData(Map<String, Object> hashData) throws Exception {
		update("sampleDAO.updateExtensionData", hashData);
	}
	/**
	 * 글을 등록한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void regExtensionData(Map<String, Object> hashData) throws Exception {
		update("sampleDAO.regExtensionData", hashData);
	}
	/**
	 * 회원 등록한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void insertMember(Map<String, Object> hashData) throws Exception {
		update("memberDAO.insertMember", hashData);
	}
	/**
	 * 작업 등록한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void insertWorkFile(UploadFileVo vo) throws Exception {
		 insert("memberDAO.insertWorkFile", vo);
	}
	/**
	 * 작업 등록한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void createTable(Map<String, Object> hashData) throws Exception {
		 insert("memberDAO.createTable", hashData);
	}
	/**
	 * 회원 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateMember(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateMember", hashData);
	}
	/**
	 * 권한글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateAuthData(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateAuthData", hashData);
	}
	/**
	 * 형태 적합성 검사를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateTypeChkMod(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateTypeChkMod", hashData);
	}
	/**
	 * 형태 적합성 검사를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateTypeChkState(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateTypeChkState", hashData);
	}
	/**
	 * 상태를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateWorkState(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateWorkState", hashData);
	}
	
	
	/**
	 * 키워드를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateKeywordData(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateKeywordData", hashData);
	}
	/**
	 * 형태를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateTypeChkData(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateTypeChkData", hashData);
	}
	/**
	 * 키워드를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateSpellData(Map<String, Object> hashData) throws Exception {
		update("memberDAO.updateSpellData", hashData);
	}
	
	
	/**
	 * 메뉴 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectAuthList(SampleDefaultVO searchVO) throws Exception {
		return list("memberDAO.selectAuthList", searchVO);
	}
	/**
	 * 작업 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectWorkDetailList(SampleDefaultVO searchVO) throws Exception {
		return list("memberDAO.selectWorkDetailList", searchVO);
	}
	
	/**
	 * 맞춤법 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectSpellDetailList(SampleDefaultVO searchVO) throws Exception {
		return list("memberDAO.selectSpellDetailList", searchVO);
	}
	
	/**
	 * 작업 전체 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectWorkTotDetailList(SampleDefaultVO searchVO) throws Exception {
		return list("memberDAO.selectWorkTotDetailList", searchVO);
	}
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectWorkListTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("memberDAO.selectWorkListTotCnt", searchVO);
	}
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectWorkAnsTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("memberDAO.selectWorkAnsTotCnt", searchVO);
	}
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectTypeChkAnsTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("memberDAO.selectTypeChkAnsTotCnt", searchVO);
	}
	/**
	 * 검사여부를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public String selectSpellCompCode(SampleDefaultVO searchVO) {
		return (String) select("memberDAO.selectSpellCompCode", searchVO);
	}
	
	public void deleteJsonData(String bd_idx) {
		update("memberDAO.delJsonData", bd_idx);
	}
	public void deleteTypeChkData(String bd_idx) {
		try {
		update("memberDAO.delTypeChkData", bd_idx);
		String tb_name = "tb_type_chk_"+bd_idx;
		Map<String, String> hashData = new HashMap<String, String>();
		hashData.put("tb_name", tb_name);
		update("memberDAO.dropTableTypeChk",hashData);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

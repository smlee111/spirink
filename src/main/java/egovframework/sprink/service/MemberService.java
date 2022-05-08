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
package egovframework.sprink.service;

import java.util.List;
import java.util.Map;

import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.sprink.dto.Member;
import egovframework.sprink.dto.UploadFileVo;

/**
 * @Class Name : EgovSampleService.java
 * @Description : EgovSampleService Class
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
public interface MemberService {

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	//String insertSample(SampleVO vo) throws Exception;

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	//void updateSample(SampleVO vo) throws Exception;

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	//void deleteSample(SampleVO vo) throws Exception;

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	Member selectMember(Map<String, Object> hashData) throws Exception;

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	Member selectMemberNo(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 가입여부 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	int selectChkMember(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 로그인가능여부 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	int selectLogChkMember(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 가입등록 한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	void insertMember(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 파일정보등록 한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	void insertWorkFile(UploadFileVo vo) throws Exception;
	
	void deleteJsonData(String bd_idx) throws Exception;
	
	void deleteTypeChkData(String bd_idx) throws Exception;
	
	/**
	 * 테이블생성한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	void createTable(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 가입수정 한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	void updateMember(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 회원 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectMemberList(SampleDefaultVO searchVO) throws Exception;

	/**
	 * 작업 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectWorkList(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 맞춤법 작업 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectSpellList(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 형태적합성 검사를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectTypeChkList(Map<String, Object> hashData) throws Exception;
	
	/**
	 * JSON 작업 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectJsonList(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 테스트글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	//List<?> selectTestList(SampleDefaultVO searchVO) throws Exception;
	
	/**
	 * 테스트글 전체목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	//List<?> selectTestTotList() throws Exception;
	
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	int selectMemberListTotCnt(SampleDefaultVO searchVO);
	
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	//int selectTestListTotCnt(SampleDefaultVO searchVO);
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	//int selectTestAnsTotCnt(SampleDefaultVO searchVO);
	/**
	 * 회원 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectAuthList(SampleDefaultVO searchVO) throws Exception;
	/**
	 * 권한을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateAuthData(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 형태적합성수정안
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateTypeChkMod(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 형태적합성 검수상태
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateTypeChkState(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 키워드를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateKeywordData(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 키워드를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateTypeChkData(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 상태를 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateWorkState(Map<String, Object> hashData) throws Exception;
	
	
	
	
	/**
	 * 맞춤법을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateSpellData(Map<String, Object> hashData) throws Exception;
	
	/**
	 * 작업 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectWorkDetailList(SampleDefaultVO searchVO) throws Exception;
	
	/**
	 * 맞춤법 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectSpellDetailList(SampleDefaultVO searchVO) throws Exception;
	
	/**
	 * 작업 목록 전체를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectWorkTotDetailList(SampleDefaultVO searchVO) throws Exception;
	
	int selectWorkListTotCnt(SampleDefaultVO searchVO);
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	int selectWorkAnsTotCnt(SampleDefaultVO searchVO);
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	int selectTypeChkAnsTotCnt(SampleDefaultVO searchVO);
	
	
	/**
	 * 맞춤법 검사여부를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	String selectSpellCompCode(SampleDefaultVO searchVO) throws Exception;
}

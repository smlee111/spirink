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

@Repository("commonDAO")
public class CommonDAO extends EgovAbstractDAO {

	/**
	 * 로그 등록한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void insertLogData(Map<String, Object> hashData) throws Exception {
		update("commonDAO.insertLogData", hashData);
	}
	
	/**
	 * 로그 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectLogList(SampleDefaultVO searchVO) throws Exception {
		return list("commonDAO.selectLogList", searchVO);
	}
	
	/**
	 * 로그갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectLogListTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("commonDAO.selectLogListTotCnt", searchVO);
	}
	
	/**
	 * 엔티티 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectEntityList(SampleDefaultVO searchVO) throws Exception {
		return list("commonDAO.selectEntityList", searchVO);
	}
	
	/**
	 * 엔티티갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectEntityListTotCnt(SampleDefaultVO searchVO) {
		return (Integer) select("commonDAO.selectEntityListTotCnt", searchVO);
	}
}

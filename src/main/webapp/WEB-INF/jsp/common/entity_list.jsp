<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
	<title>Insert title here</title>
	<script type="text/javaScript" language="javascript" defer="defer">
	/* pagination 페이지 링크 function */
    function fn_egov_link_page(pageNo){
    	document.listForm.pageIndex.value = pageNo;
    	document.listForm.action = "<c:url value='/entity_list.do'/>";
       	document.listForm.submit();
    }
	</script>
	<link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet">
	<link rel='stylesheet' href='https://cdn-uicons.flaticon.com/uicons-regular-straight/css/uicons-regular-straight.css'>
	<link rel='stylesheet' href='https://cdn-uicons.flaticon.com/uicons-bold-straight/css/uicons-bold-straight.css'>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<style>
	  /* reset.css */
	  html, body, div, span, applet, object, iframe,
	  h1, h2, h3, h4, h5, h6, p, blockquote, pre,
	  a, abbr, acronym, address, big, cite, code,
	  del, dfn, em, img, ins, kbd, q, s, samp,
	  small, strike, strong, sub, sup, tt, var,
	  b, u, i, center,
	  dl, dt, dd, ol, ul, li,
	  fieldset, form, label, legend,
	  table, caption, tbody, tfoot, thead, tr, th, td,
	  article, aside, canvas, details, embed, 
	  figure, figcaption, footer, header, hgroup, 
	  menu, nav, output, ruby, section, summary,
	  time, mark, audio, video {
	      box-sizing: border-box;
	      margin: 0;
	      padding: 0;
	      border: 0;
	      font-family: 'Noto Sans KR', sans-serif;
	      font-size: 15px;
	      font: inherit;
	      vertical-align: baseline;
	  }
	  /* HTML5 display-role reset for older browsers */
	  article, aside, details, figcaption, figure, 
	  footer, header, hgroup, menu, nav, section {
	      display: block;
	  }
	  input:focus {outline: 0;}
	  input[type="text" i] {padding: 0;}
	  body {
	      line-height: 1;
	  }
	  ol, ul {
	      list-style: none;
	  }
	  blockquote, q {
	      quotes: none;
	  }
	  blockquote:before, blockquote:after,
	  q:before, q:after {
	      content: '';
	      content: none;
	  }
	  table {
	      border-collapse: collapse;
	      border-spacing: 0;
	  }
	
	  /* common */
	  /* nav_left */
	  #nav_left {width: 18%; height: 100vh; background: #fff; border-right: 1px solid rgba(0,0,0,.05); position: fixed;top: 0;left: 0; z-index: 1;}
	  #nav_left .logoWrap {overflow: hidden; border-bottom: 1px solid rgba(0,0,0,.07); padding: 0 18px;}
	  #nav_left .logoWrap .logo {float: left; height: 80px; background: url(/logo.png)no-repeat center left 15px; background-size: auto calc(100% - 38px); line-height: 20px; color:#000; font-size:16px; font-weight: 600; padding: 20px 0 20px 60px;}
	  #nav_left .menu_divider {width: calc(100% - 60px); height: 1px; background: rgba(255,255,255,.08); margin: 6px auto;}
	
	  #nav_left .user {margin: 6px 0 0;}
	  #nav_left .user .name {color:#000; cursor: pointer; padding: 15px 32px;}
	  #nav_left .user .name:hover {background: #eee;}
	  #nav_left .user .name i {float: right;}
	  #nav_left .user .lang {display: inline-block; background: #6700A9; border-radius: 15px; color:#fff; font-size:12px; font-weight: 900; padding: 6px 12px 7px; margin: 5px 0 8px 32px;}
	
	  #nav_left .menu li {overflow: hidden; color:#fff; padding: 15px 32px; position: relative;}
	  #nav_left .menu li:hover {background: #eee;}
	  #nav_left .menu li.on {background: #6700A9;}
	  #nav_left .menu li.on:hover {background: #6700A9;}
	  #nav_left .menu li a {width: 100%; height: 100%; position: absolute;top: 0;left: 0;}
	  #nav_left .menu li i {float: left; color:#555; margin: 0 8px 0 0;}
	  #nav_left .menu li.on i {color:#fff;}
	  #nav_left .menu li .txt {float: left; color:#444; font-size:15px; font-weight: 500;}
	  #nav_left .menu li.on .txt {color:#fff; font-weight: 600;}
	
	  /* container basic */
	  #container {width: 100%; min-height: 110vh; background: #efefef; padding: 0 18% 0;}
	  #container.w100 {padding: 0 0 0 18%;}
	
	  #container .top {width: 100%; height: 80px; background: #fff; border-bottom: 1px solid rgba(0,0,0,.07); padding: 17px 30px 16px 50px; margin: 0 0 10px; position: relative;}
	  #container .top.input {padding: 19px 250px 21px 60px;}
	  #container .top.input .priority {width: 10px; height: 10px; border-radius: 100%; cursor: pointer; position: absolute;top: 35px;left: 30px;}
	  #container .top.input .priority.highest {background: #f76949;}
	  #container .top.input input {width: 100%; height: 40px; background: transparent; border: 0; line-height: 36px; color:#000; font-size: 24px;}
	  #container .top.input input:focus {outline: 0; border-bottom:1.5px solid #6700A9;}
	  #container .top.input .btnWrap {overflow: hidden; position: absolute;top: 50%;right: 20px; transform: translateY(-50%);}
	  #container .top.input .btnWrap li {float: left; background: #6700A9; text-align: center; color:#222; font-size:20px; font-weight: 600; cursor: pointer; padding: 10px 12px 11px; margin: 0 0 0 5px;}
	  #container .top.input .btnWrap li:hover {background: #6700A9;}
	
	
	  /* container list */
	  #container .list-wrap {margin: 30px;}
	  #container .list-wrap .search {width: 100%; height: 100px; padding: 20px; background: #fff;}
	  #container .list-wrap .search ul {overflow: hidden;}
	  #container .list-wrap .search ul li {float: left; width: 16%; margin: 0 2% 0 0;}
	  #container .list-wrap .search ul li:nth-child(3) {width: 57%;}
	  #container .list-wrap .search ul li:nth-child(4) {margin: 22px 0 0; width: 60px; line-height: 32px; height: 32px; background: #6700A9; color: #fff; font-size: 12px; text-align: center;}
	  #container .list-wrap .search ul li .search-head {margin:0 0 10px 0; font-size: 12px; font-weight: bold;}
	  #container .list-wrap .search ul li .choice {width: 100%; height: 30px; text-indent: 10px;  border: 1px solid #a1a1a1; font-size: 10px;  color: #a1a1a1;}
	  #container .list-wrap .search ul li .sel {height: 32px;}
	
	  #container .list-wrap .result {margin:40px 0 0; width: 100%; height: 400px;}
	  #container .list-wrap .result .result-head {overflow: hidden; margin:0 0 10px 0;}
	  #container .list-wrap .result .result-head .result-tit {float: left; font-weight: bold; font-size:20px;}
	  #container .list-wrap .result .result-head .total-cnt {float: left; width: 40px; height: 20px; line-height: 20px; background: rgb(200, 157, 224); margin:0 0 0 10px; font-size:12px; text-align: center; border-radius: 5px;}
	  #container .list-wrap .result .result-head .file {overflow: hidden; float: right;}
	  #container .list-wrap .result .result-head .file li{float: left; background: #fff; margin:0 5px 0 0; padding: 5px; border-radius: 1px; border: 1px solid #cacaca; cursor: pointer;}
	
	  #container .list-wrap .result .result-list {border:2px solid #cacaca;}
	  #container .list-wrap .result .result-list > li {border-bottom:1px solid #cacaca ; background: #fff; text-align: center;}
	  #container .list-wrap .result .result-list > li:nth-child(1) .row {height: 60px; line-height: 60px; background: rgb(200, 157, 224); border-bottom:1px solid #cacaca ; text-align: center;}
	  #container .list-wrap .result .result-list > li:nth-child(1) .row li {text-align: center;}
	  #container .list-wrap .result .result-list .row {overflow: hidden; height: 50px; line-height: 50px;}
	  #container .list-wrap .result .result-list .row li {float: left;}
	  #container .list-wrap .result .result-list .row li:nth-child(1) {width: 3%; margin: 0 0 0 1%; text-align: center;}
	  #container .list-wrap .result .result-list .row li:nth-child(2) {width: 20%; text-align: left; text-indent: 20px;}
	  #container .list-wrap .result .result-list .row li:nth-child(3) {width: 32%; text-align: left; text-indent: 20px;}
	  #container .list-wrap .result .result-list .row li:nth-child(3) .word {font-weight: bold;}
	  #container .list-wrap .result .result-list .row li:nth-child(4) {width: 13%;}
	  #container .list-wrap .result .result-list .row li:nth-child(5) {width: 17%;}
	  #container .list-wrap .result .result-list .row li:nth-child(6) {width: 13%; margin: 0 1% 0 0;}
	
	  #container .list-wrap .result .result-foot {overflow: hidden; margin:10px 0 0;}
	  #container .list-wrap .result .result-foot .page-num {overflow: hidden; float: left; margin:0 0 0 40%;}
	  #container .list-wrap .result .result-foot .page-num li {float: left; margin:0 4px 0 0; width: 25px; height: 25px; line-height: 25px; text-align: center; font-size: 12px; background: #fff; border:1px solid #cacaca; cursor: pointer;}
	  #container .list-wrap .result .result-foot .btn {float: right; width: 100px; height: 40px; line-height: 40px; font-size: 14px; text-align: center; background-color: #373737; color:#fff; border-radius: 5px; cursor: pointer;}
	
	  /* container input */
	  #container .st {border-bottom: 1px solid rgba(255,255,255,.08); margin: 0 30px;}
	  #container .st .tit {color:#fff; font-size: 20px; font-weight: 400; cursor: pointer; padding: 25px 30px; position: relative;}
	  #container .st .tit:hover {background: #3f3f3f;}
	  #container .st .tit.on {background: #3f3f3f;}
	  #container .st .tit .fi {font-size:14px; position: absolute;top: 50%;right: 30px; transform: translateY(-50%);}
	  #container .st .tit .fi-bs-angle-down {display: block;}
	  #container .st .tit .fi-bs-angle-up {display: none;}
	  #container .st .tit.on .fi-bs-angle-down {display: none;}
	  #container .st .tit.on .fi-bs-angle-up {display: block;}
	  #container .st .st_depth {display: none; background: #272727; color:#fff; padding: 30px 40px 50px;}
	  #container .st .st_depth .inputWrap {padding: 0 0 0 30px; margin: 0 0 23px; position: relative;}
	  #container .st .st_depth .inputWrap i {color:rgb(169,234,19); position: absolute;top: 50%;left: 0; transform: translateY(-50%);}
	  #container .st .st_depth .inputWrap input {width: 100%; height: 50px; background: transparent; border: 0; border-bottom: 1px solid rgba(169,234,19,.25); text-indent: 10px; color:#fff; font-size:16px; transition: .2s;}
	  #container .st .st_depth .inputWrap input:hover {border-bottom: 1px solid rgb(169,234,19);}
	  #container .st .st_depth .inputWrap input:focus {border-bottom: 1px solid rgb(169,234,19);}
	  #container .st .st_depth #list_phrases li {background: rgba(255,255,255, 0.03); padding: 0 50px 0 50px; margin: 0 0 3px; position: relative;}
	  #container .st .st_depth #list_phrases li i.fi-bs-quote-right {font-size:11px; position: absolute;top: calc(50% + 1px);left: 25px; transform: translateY(-50%);}
	  #container .st .st_depth #list_phrases li i.fi-bs-cross {display: none; font-size:13px; position: absolute;top: calc(50% + 1px);right: 25px; transform: translateY(-50%);}
	  #container .st .st_depth #list_phrases li:hover i.fi-bs-cross {display: block; cursor: pointer;}
	  #container .st .st_depth #list_phrases li input {width: 100%; height: 52px; background: transparent; border: 0; color:#fff; font-size:16px;}
	
	  /* side_right */
	  #side_right {width: 18%; height: 100vh; background: #373737; position: fixed;top: 0;right: 0; z-index: 3;}
	
	  /* scrollbar */
	  body::-webkit-scrollbar {width: 5px;}
	  body::-webkit-scrollbar-thumb {min-height: 15%; background-color: #6700A9; border-radius: 10px;}
	  body::-webkit-scrollbar-track {background-color: #ccc;}
	</style>
</head>
<body>
	<div id="nav_left">
        <div class="logoWrap">
            <a href="#"><p class="logo">SPIRINK<br/>DialogFlow</p></a>
        </div>
        <div class="user">
            <p class="name">NewAgent<i class="fi-bs-caret-down"></i></p>
            <p class="lang">ko</p>
        </div>
        <p class="menu_divider"></p>
        <ul class="menu menu01">
            <li>
                <a href="./list_intent.html"></a>
                <i class="fi-bs-comment-alt"></i>
                <p class="txt">Intents</p>
            </li>
            <li class="on">
                <a href="./list_entity.html"></a>
                <i class="fi-bs-vector-alt"></i>
                <p class="txt">Entities</p>
            </li>
        </ul>
        <p class="menu_divider"></p>
        <ul class="menu">
            <li>
                <a href="#"></a>
                <i class="fi-bs-shield-interrogation"></i>
                <p class="txt">Support</p>
            </li>
            <li>
                <a href="#"></a>
                <i class="fi-bs-user"></i>
                <p class="txt">Account</p>
            </li>
            <li>
                <a href="#"></a>
                <i class="fi-bs-power"></i>
                <p class="txt">Logout</p>
            </li>
        </ul>
    </div>
    <!-- //nav_left end -->

    <div id="container" class="w100">
        <div class="top input">
            <span class="priority highest"></span>
            <input type="text" value="Default Welcome Intent">
            <ul class="btnWrap">
                <li title="저장"><i class="fi-bs-file-check"></i></li>
                <li title="삭제"><i class="fi-bs-file-delete"></i></li>
            </ul>
        </div>   
        
        <div class="list-wrap">
            <div class="search">
                <ul>
                    <li>
                        <p class="search-head">
                            수정일자
                        </p>
                        <input type="date" class="choice">
                    </li>
                    <li>
                        <p class="search-head">
                            카테고리
                        </p>
                        <select class="choice sel">
                            <option>전체</option>
                            <option>일상</option>
                            <option>금융</option>
                        </select>
                    </li>
                    <li>
                        <p class="search-head">
                            Entity명
                        </p>
                        <input type="text" class="choice" placeholder="검색어 입력">
                    </li>
                    <li>
                        <div class="btn">
                            <i class="fi fi-rr-search"></i>
                            검색
                        </div>
                    </li>
                </ul>
            </div>
            <!-- search End -->
            
            <div class="result">
                <div class="result-head">
                    <p class="result-tit">
                        검색결과
                    </p>
                    <div class="total-cnt">
                        <c:out value="${paginationInfo.totalRecordCount}"/>
                    </div>
                    <ul class="file">
                        <li>업로드</li>
                        <li>전체 다운로드</li>
                    </ul>
                </div>
                <!-- result-head End -->

                <ul class="result-list">
                    <li>
                        <ul class="row">
                            <li>
                                No.
                            </li>
                            <li>
                                Entity명
                            </li>
                            <li>
                                Entry
                            </li>
                            <li>
                                카테고리
                            </li>
                            <li>
                                등록일시
                            </li>
                            <li>
                                수정일시
                            </li>
                        </ul>
                    </li>
			<c:forEach var="result" items="${resultList}" varStatus="status">
                    <li>
                        <ul class="row">
                            <li>
                                <c:out value="${((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/>
                            </li>
                            <li>
                                <c:out value="${result.entity_name}"/>
                            </li>
                            <li>
                            	<div class="word">
                                <c:out value="${result.entity_entry}"/>
                                </div>
                            </li>
                            <li>
                                <c:out value="${result.entity_category}"/>
                            </li>
                            <li>
                                2022.04.29 15:28:05
                            </li>
                            <li>
                                2022.04.29 16:28:05
                            </li>
                        </ul>
                    </li>
			</c:forEach>
                    
                </ul>
                <!-- result-list End -->

                <div class="result-foot">
                <form:form commandName="searchVO" id="listForm" name="listForm" method="post">
        		<input type="hidden" name="selectedId" />
        		<input type="hidden" name="searchKeyword" />
        		<input type="hidden" name="tb_mem_no" />
                	<ul class="page-num">
                		<li>
	                  		<ui:pagination paginationInfo = "${paginationInfo}" type="text" jsFunction="fn_egov_link_page" />
	                  	</li>
					  <form:hidden path="pageIndex" />
                 	</ul>
                 </form:form>
                   <!--  <ul class="page-num">
                        <li>1</li>
                        <li>2</li>
                        <li>3</li>
                        <li>4</li>
                        <li>5</li>
                        <li>6</li>
                        <li>7</li>
                        <li>8</li>
                        <li>9</li>
                        <li>10</li>
                    </ul> -->

                    <div class="btn">
                        Entity 신규
                    </div>
                </div>
            </div>
            <!-- result End -->
        </div>

    </div>
    <!-- //container end -->

    <script>
        // nav_left addClass 'on' by url
        let nowUrl = window.location.pathname;
        if(nowUrl === "/list_intent.html" || nowUrl === "/input_intent.html") {
            $('.menu01 > li').removeClass('on');
            $('.menu01 > li').eq(0).addClass('on');
        } else if(nowUrl === "/list_entity.html" || nowUrl === "/input_entity.html") {
            $('.menu01 > li').removeClass('on');
            $('.menu01 > li').eq(1).addClass('on');
        }
    </script>
</body>
</html>
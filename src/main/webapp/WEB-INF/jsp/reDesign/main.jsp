<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/header.jsp" %>

<!-- body(wrapAll) -->
<div id="wrapAll">

	<div class="contents">
	    <section class="size">                
	        <div class="box">
	            <p class="tit">TOTAL PROJECT</p>
	            <p class="num"><span>123,456</span>개</p>
	        </div>
	        <div class="box">
	            <p class="tit">DONE PROJECT</p>
	            <p class="num"><span>123,000</span>개</p>
	        </div>
	        <div class="box">
	            <p class="tit">NOW PROJECT</p>
	            <p class="num"><span>456</span>개</p>
	        </div>                
	        <div class="box">
	            <p class="tit">ISSUE</p>
	            <p class="num"><span>123,456</span>개</p>
	        </div>
	        <div class="box">
	            <p class="tit">LAST CONNECTION</p>
	            <p class="num"><span>2021.12.22 17:00</span></p>
	        </div>
	    </section>
	    <!-- size end -->
	
	    <section class="graph">
	        <div class="head">
	            <p class="tit">MY STATUS</p>
	        </div>
	        <div class="chart chart1"><canvas id="myChart1"></canvas></div>
	        <div class="chart chart2"><canvas id="myChart2"></canvas></div>
	        <div class="chart chart3"><canvas id="myChart3"></canvas></div>
	    </section>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/myChart.js"></script>
	    <!-- graph end -->
	
	    <section class="active">
	        <div class="head">
	            <p class="tit">ACTIVE PROJECTS</p>
	            <select name="myProject">
	                <option hidden>filter</option>
	                <option value="">날짜순</option>
	                <option value="">이름순</option>
	            </select>
	        </div>
	        <ul class="list">
	            <li>
	                <p class="no">번호</p>
	                <p class="company">기업명</p>
	                <p class="manager">담당자</p>
	                <p class="managerph">담당자 연락처</p>
	                <p class="worker">작업자</p>
	                <p class="task">작업자 주업무</p>                        
	                <p class="workerph">작업자 연락처</p>                        
	                <p class="date">기한</p>
	            </li>
	            <li>
	                <p class="no">1</p>
	                <p class="company">한국전력</p>
	                <p class="manager">김전력</p>
	                <p class="managerph">010-1234-5678</p>
	                <p class="worker">이승민</p>
	                <p class="task">excel to yml</p>
	                <p class="workerph">010-1234-5678</p>
	                <p class="date">2021.12.22 - 2022.12.22</p>
	            </li>
	            <li>
	                <p class="no">1</p>
	                <p class="company">한국전력</p>
	                <p class="manager">김전력</p>
	                <p class="managerph">010-1234-5678</p>
	                <p class="worker">이승민</p>
	                <p class="task">excel to yml</p>
	                <p class="workerph">010-1234-5678</p>
	                <p class="date">2021.12.22 - 2022.12.22</p>
	            </li>
	            <li>
	                <p class="no">1</p>
	                <p class="company">한국전력</p>
	                <p class="manager">김전력</p>
	                <p class="managerph">010-1234-5678</p>
	                <p class="worker">이승민</p>
	                <p class="task">excel to yml</p>
	                <p class="workerph">010-1234-5678</p>
	                <p class="date">2021.12.22 - 2022.12.22</p>
	            </li>
	        </ul>
	    </section>
	    <!-- active end -->
	</div>
	<!-- contents end -->

</div>
<!-- //body(wrapAll) end -->

<script src="/js/nav.js"></script>

<%@ include file="/include/footer.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.sprink.dto.Member" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%!
private String chkAuth(HttpServletRequest request, String url) {
	 String auth="false";
	 //String url = request.getRequestURL().toString();
	 HttpSession session = request.getSession();
    // login처리를 담당하는 사용자 정보를 담고 있는 객체를 가져옴
	 List<?> authList = (List<?>)session.getAttribute("authList");
    if(authList!=null) {
   	 //System.out.println("authList size"+authList.size());
   	 for(Object obj:authList) {
   		 HashMap map =(HashMap)obj;
   		 String mu_method = map.get("mu_method")==null?"null":(String)map.get("mu_method");
   		 String au_list = map.get("au_list")==null?"N":(String)map.get("au_list");
   		 //System.out.println("url"+url+"mu_method===>"+mu_method);
   		 if(au_list.equals("Y")&&url.indexOf(mu_method)>-1) {
   			 auth="true";
   			 break;
   		 }
   	 }
    }
    //System.out.println("url"+url+"auth"+auth);
    return auth;
}
%>
<%
String name="";
Member mem = session.getAttribute("login")==null?null:(Member)session.getAttribute("login");
if(mem!=null){
	name=mem.getTb_mem_nm();
}
String agent=request.getParameter("agent")==null?"first":request.getParameter("agent");

String first_agent="first";
String second_agent="second";
String third_agent="third";
String test_agent="test";
String main_title="";
if(agent.equals("first")){
	first_agent="active";
	second_agent="";
	third_agent="";
	main_title="첫번째 프로젝트"; 
}else if(agent.equals("second")){
	first_agent="";
	second_agent="active";
	third_agent="";
	main_title="두번째 프로젝트";
}else if(agent.equals("third")){
	first_agent="";
	second_agent="";
	third_agent="active";
	main_title="두번째 프로젝트";
}
if(agent.equals("test")){
	test_agent="active";
}
String solr_chk = chkAuth(request, "solr");
String work_main = chkAuth(request, "main.do");
String wrt_sty_gen = chkAuth(request, "wrtStyGen.do");
String member_list = chkAuth(request, "member_list.do");
String twitter_sco = chkAuth(request, "twitter_sco.do");
String log_list = chkAuth(request, "log_list.do");
%>
<c:set var="solr_chk" value="<%=solr_chk %>"/>
<c:set var="work_main" value="<%=work_main %>"/>
<c:set var="wrt_sty_gen" value="<%=wrt_sty_gen %>"/>
<c:set var="member_list" value="<%=member_list %>"/>
<c:set var="twitter_sco" value="<%=twitter_sco %>"/>
<c:set var="log_list" value="<%=log_list %>"/>
<!-- Navbar -->
  <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
      </li>
      
      <li class="nav-item d-none d-sm-inline-block">
      <c:choose>
	    <c:when test="${solr_chk eq 'true'}">
	       <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	 </c:choose>
        
      </li>
      <li class="nav-item d-none d-sm-inline-block">
       <c:choose>
	    <c:when test="${work_main eq 'true'}">
	       <a href="${pageContext.request.contextPath}/main.do" class="nav-link">작업관리</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	   </c:choose>
        
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${wrt_sty_gen eq 'true'}">
	       <a href="${pageContext.request.contextPath}/wrtStyGen.do" class="nav-link">문체변환</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	   </c:choose>
        
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${twitter_sco eq 'true'}">
	       <a href="${pageContext.request.contextPath}/twitter_sco.do" class="nav-link">트위터크롤링</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	   </c:choose>
        
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${member_list eq 'true'}">
	       <a href="${pageContext.request.contextPath}/member_list.do" class="nav-link">사용자관리</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="/member_list.do" class="nav-link">사용자관리</a> -->
	    </c:otherwise>
	   </c:choose>
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${log_list eq 'true'}">
	       <a href="${pageContext.request.contextPath}/log_list.do" class="nav-link">로그관리</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="/member_list.do" class="nav-link">사용자관리</a> -->
	    </c:otherwise>
	   </c:choose>
      </li>
      <li class="nav-item d-none d-sm-inline-block">
       <c:choose>
	    <c:when test="${name eq ''}">
	       <a href="${pageContext.request.contextPath}/login.do" class="nav-link">로그인</a>
	    </c:when>
	    <c:otherwise>
	        <a href="${pageContext.request.contextPath}/logOut.do" class="nav-link">로그아웃</a>
	    </c:otherwise>
	   </c:choose>
      </li>
    </ul>

   

    <!-- Right navbar links -->
    <ul class="navbar-nav ml-auto">
      <!-- Messages Dropdown Menu -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="far fa-comments"></i>
          <!-- <span class="badge badge-danger navbar-badge">3</span> -->
        </a>
        
      </li>
      <!-- Notifications Dropdown Menu -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="far fa-bell"></i>
          <!-- <span class="badge badge-warning navbar-badge">15</span> -->
        </a>
        
      </li>
      <li class="nav-item">
        <a class="nav-link" data-widget="control-sidebar" data-slide="true" href="#" role="button"><i
            class="fas fa-th-large"></i></a>
      </li>
    </ul>
  </nav>
  <!-- /.navbar -->

  <!-- Main Sidebar Container -->
  <aside class="main-sidebar sidebar-dark-primary elevation-4">
    <!-- Brand Logo -->
    <a href="${pageContext.request.contextPath}/" class="brand-link">
      <img src="dist/img/AdminLTELogo1.png" alt="AdminLTE Logo" class="brand-image img-circle elevation-3"
           style="opacity: .8">
      
      <span class="brand-text font-weight-light">TextNet Admin</span>
       <%
          if(!name.equals("")){
        	  out.println("<br>"+name+"님");
          }
      %>
    </a>

    <!-- Sidebar -->
    <div class="sidebar">
      

      <!-- Sidebar Menu -->
      <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
          <!-- Add icons to the links using the .nav-icon class
               with font-awesome or any other icon font library -->
          <li class="nav-item has-treeview menu-open">
            <a href="#" class="nav-link active">
              <i class="nav-icon fas fa-tachometer-alt"></i>
              <p>
                	문체변환
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="${pageContext.request.contextPath}/wrtStyGen.do?agent=first" class="nav-link <%=first_agent%>">
                  <i class="far fa-circle nav-icon"></i>
                  <p>문체변환메인</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="${pageContext.request.contextPath}/total_list.do?agent=second" class="nav-link <%=second_agent%>">
                  <i class="far fa-circle nav-icon" ></i>
                  <p>기본사전관리</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="${pageContext.request.contextPath}/extension_list.do?agent=third" class="nav-link <%=third_agent%>">
                  <i class="far fa-circle nav-icon" ></i>
                  <p>확장사전관리</p>
                </a>
              </li>
            </ul>
          </li>
          <li class="nav-item has-treeview menu-open">
            <a href="#" class="nav-link active">
              <i class="nav-icon fas fa-tachometer-alt"></i>
              <p>
                                   문체변환테스트
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="${pageContext.request.contextPath}/test_list.do?agent=test" class="nav-link <%=test_agent%>">
                  <i class="far fa-circle nav-icon"></i>
                  <p>변환테스트</p>
                </a>
              </li>
            </ul>
          </li>
        </ul>
      </nav>
      <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
  </aside>
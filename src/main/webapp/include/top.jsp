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
String main_title="";
if(agent.equals("first")){
	first_agent="active";
	second_agent="";
	main_title="스피링크 프로젝트"; 
}else if(agent.equals("second")){
	first_agent="";
	second_agent="active";
	main_title="스피링크 프로젝트";
}
String solr_chk = chkAuth(request, "solr");
String work_main = chkAuth(request, "main.do");
String wrt_sty_gen = chkAuth(request, "wrtStyGen.do");
String member_list = chkAuth(request, "member_list.do");
String agent_first = chkAuth(request, "first");
String agent_second = chkAuth(request, "second");
String twitter_sco = chkAuth(request, "twitter_sco.do");
String log_list = chkAuth(request, "log_list.do");
String spell_main = chkAuth(request, "spell_main.do");
String my_update = chkAuth(request, "my_update.do");
String json_main = chkAuth(request, "json_main.do");
String type_chk_main = chkAuth(request, "type_chk_main.do");
String pdf_main = chkAuth(request, "pdf_main.do");
String word_main = chkAuth(request, "word_main.do");

//System.out.println("agent_second"+agent_second);
%>
<c:set var="name" value="<%=name %>" />
<c:set var="solr_chk" value="<%=solr_chk %>"/>
<c:set var="work_main" value="<%=work_main %>"/>
<c:set var="wrt_sty_gen" value="<%=wrt_sty_gen %>"/>
<c:set var="member_list" value="<%=member_list %>"/>
<c:set var="agent_first" value="<%=agent_first %>"/>
<c:set var="agent_second" value="<%=agent_second %>"/>
<c:set var="twitter_sco" value="<%=twitter_sco %>"/>
<c:set var="log_list" value="<%=log_list %>"/>
<c:set var="spell_main" value="<%=spell_main %>"/>
<c:set var="my_update" value="<%=my_update %>"/>
<c:set var="json_main" value="<%=json_main %>"/>
<c:set var="type_chk_main" value="<%=type_chk_main %>"/>
<c:set var="pdf_main" value="<%=pdf_main %>"/>
<c:set var="word_main" value="<%=word_main %>"/>
<!-- Navbar -->
  <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
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
	    <c:when test="${type_chk_main eq 'true'}">
	       <a href="${pageContext.request.contextPath}/type_chk_main.do" class="nav-link">형태적합성검사</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	  </c:choose>  
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${spell_main eq 'true'}">
	       <a href="${pageContext.request.contextPath}/spell_main.do" class="nav-link">맞춤법검사</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	  </c:choose>  
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${json_main eq 'true'}">
	       <a href="${pageContext.request.contextPath}/json_main.do" class="nav-link">JSON변환</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	   </c:choose>
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${pdf_main eq 'true'}">
	       <a href="${pageContext.request.contextPath}/pdf_main.do" class="nav-link">PDF텍스트추출</a>
	    </c:when>
	    <c:otherwise>
	        <!-- <a href="http://textnet.tk:8983/solr" class="nav-link" target="_blank">검색관리</a> -->
	    </c:otherwise>
	   </c:choose>
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <c:choose>
	    <c:when test="${word_main eq 'true'}">
	       <a href="${pageContext.request.contextPath}/word_main.do" class="nav-link">단어추출</a>
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
	    <c:when test="${my_update eq 'true'}">
	       <a href="${pageContext.request.contextPath}/my_update.do" class="nav-link">정보수정</a>
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
    <ul class="navbar-nav">
      <!-- Messages Dropdown Menu -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="far fa-comments"></i>
          <!-- <span class="badge badge-danger navbar-badge">3</span> -->
        </a>
        <!-- <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right"> -->
          <!-- <a href="#" class="dropdown-item">
            Message Start
            <div class="media">
              <img src="dist/img/user1-128x128.jpg" alt="User Avatar" class="img-size-50 mr-3 img-circle">
              <div class="media-body">
                <h3 class="dropdown-item-title">
                  Brad Diesel
                  <span class="float-right text-sm text-danger"><i class="fas fa-star"></i></span>
                </h3>
                <p class="text-sm">Call me whenever you can...</p>
                <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> 4 Hours Ago</p>
              </div>
            </div>
            Message End
          </a> -->
          <!-- <div class="dropdown-divider"></div> -->
          <!-- <a href="#" class="dropdown-item">
            Message Start
            <div class="media">
              <img src="dist/img/user8-128x128.jpg" alt="User Avatar" class="img-size-50 img-circle mr-3">
              <div class="media-body">
                <h3 class="dropdown-item-title">
                  John Pierce
                  <span class="float-right text-sm text-muted"><i class="fas fa-star"></i></span>
                </h3>
                <p class="text-sm">I got your message bro</p>
                <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> 4 Hours Ago</p>
              </div>
            </div>
            Message End
          </a> -->
         <!--  <div class="dropdown-divider"></div> -->
          <!-- <a href="#" class="dropdown-item">
            Message Start
            <div class="media">
              <img src="dist/img/user3-128x128.jpg" alt="User Avatar" class="img-size-50 img-circle mr-3">
              <div class="media-body">
                <h3 class="dropdown-item-title">
                  Nora Silvester
                  <span class="float-right text-sm text-warning"><i class="fas fa-star"></i></span>
                </h3>
                <p class="text-sm">The subject goes here</p>
                <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> 4 Hours Ago</p>
              </div>
            </div>
            Message End
          </a> -->
          <!-- <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item dropdown-footer">See All Messages</a> -->
       <!--  </div> -->
      </li>
      <!-- Notifications Dropdown Menu -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="far fa-bell"></i>
          <!-- <span class="badge badge-warning navbar-badge">15</span> -->
        </a>
        <!-- <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
          <span class="dropdown-header">15 Notifications</span>
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item">
            <i class="fas fa-envelope mr-2"></i> 4 new messages
            <span class="float-right text-muted text-sm">3 mins</span>
          </a>
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item">
            <i class="fas fa-users mr-2"></i> 8 friend requests
            <span class="float-right text-muted text-sm">12 hours</span>
          </a>
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item">
            <i class="fas fa-file mr-2"></i> 3 new reports
            <span class="float-right text-muted text-sm">2 days</span>
          </a>
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item dropdown-footer">See All Notifications</a>
        </div> -->
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
    <a href="${pageContext.request.contextPath}/type_chk_main.do" class="brand-link">
      <img src="dist/img/AdminLTELogo1.png" alt="AdminLTE Logo" class="brand-image img-circle elevation-3"
           style="opacity: .8">
      
      <span class="brand-text font-weight-light">TextNet Admin</span>
      <div class="row text-center"><h4><%
          if(!name.equals("")){
        	  out.println("<br> &nbsp;&nbsp;"+name+"님");
          }
      %></h4>
      </div>     
    </a>

    <!-- Sidebar -->
    <div class="sidebar">
      <!-- Sidebar user panel (optional) -->
      <!-- <div class="user-panel mt-3 pb-3 mb-3 d-flex">
        <div class="image">
          <img src="dist/img/user2-160x160.jpg" class="img-circle elevation-2" alt="User Image">
        </div>
        <div class="info">
          <a href="#" class="d-block">Alexander Pierce</a>
        </div>
      </div> -->

      <!-- Sidebar Menu -->
      <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
          <!-- Add icons to the links using the .nav-icon class
               with font-awesome or any other icon font library -->
          <li class="nav-item has-treeview menu-open">
            <a href="#" class="nav-link active">
              <i class="nav-icon fas fa-tachometer-alt"></i>
              <p>
                	프로젝트선택
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              
              <c:choose>
			    <c:when test="${agent_first eq 'true'}">
				    <li class="nav-item">
				    <c:choose>
			         <c:when test="${agent eq 'first'}">
				        <a href="${pageContext.request.contextPath}/type_chk_main.do?agent=first" class="nav-link active">
				     </c:when>
			    	 <c:otherwise>
			    	    <a href="${pageContext.request.contextPath}/type_chk_main.do?agent=first" class="nav-link ">
			    	 </c:otherwise>
			    	 </c:choose>
	                  <i class="far fa-circle nav-icon"></i>
	                  <p>스피링크 프로젝트</p>
	                </a>
	                </li>
			    </c:when>
			    <c:otherwise>
			        <!-- <a href="/member_list.do" class="nav-link">사용자관리</a> -->
			    </c:otherwise>
			  </c:choose>
               <c:choose>
			    <c:when test="${agent_second eq 'true'}">
				    <li class="nav-item">
				    <c:choose>
			         <c:when test="${agent eq 'second'}">
		                <a href="${pageContext.request.contextPath}/spell_main.do?agent=second" class="nav-link active">
		                </c:when>
			    	 <c:otherwise>
			    	    <a href="${pageContext.request.contextPath}/spell_main.do?agent=second" class="nav-link ">
			    	 </c:otherwise>
			    	 </c:choose>
                  <i class="far fa-circle nav-icon" ></i>
                  <p>두번째프로젝트</p>
                </a>
              </li>
			    </c:when>
			    <c:otherwise>
			        <!-- <a href="/member_list.do" class="nav-link">사용자관리</a> -->
			    </c:otherwise>
			  </c:choose>
            </ul>
          </li>
          <!-- <li class="nav-item">
            <a href="#" class="nav-link">
              <i class="nav-icon fas fa-th"></i>
              <p>
                Simple Link
                <span class="right badge badge-danger">New</span>
              </p>
            </a>
          </li> -->
        </ul>
      </nav>
      <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
  </aside>
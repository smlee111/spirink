<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<div id="nav">
    <a href="/SP_PRO/reDesign/main.do"><p class="logo">spirink.com</p></a>
    <ul class="user">
        <li class="thumbnail"></li>
        <li>
	        <span class="name"><%
	          if(!name.equals("")){
	        	  out.println(name);
	          }%></span>님 환영합니다
	    </li>
        <li class="btn"><a href="/SP_PRO/logOut.do"></a>LOGOUT</li>
    </ul>
    <ul class="menu">
        <li>HOME</li>
        <li>PROJECT</li>
        <li>TOOL
            <ul class="depth">
                <li>depth1</li>
                <li>depth2</li>
                <li>depth3</li>
            </ul>
        </li>
        <li>MYPAGE
            <ul class="depth">
                <li>depth1</li>
                <li>depth2</li>
                <li>depth3</li>
            </ul>
        </li>
        <li>ADMIN</li>                
    </ul>
</div>
<!-- nav end -->

<!-- JS  -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/nav.js"></script>
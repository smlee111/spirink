<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>TextNet Admin</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">

</head>
<body>
	<c:choose>
	    <c:when test="${sessionScope.login eq null}">
			<a href="/SP_PRO/reDesign/login.do">로그인</a>
	    </c:when>
	    <c:otherwise>
	    	<a href="/SP_PRO/logOut.do">로그아웃</a>
	    	<a href="/SP_PRO/logOut.do">로그기록</a>
	    	<a href="/SP_PRO/entity_list.do">엔티티목록</a>
	    </c:otherwise>
	</c:choose>
</body>
</html>
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
  <!-- reset css  -->
  <link rel="stylesheet" href="../..${pageContext.request.contextPath}/css/reset.css"> 
  <!-- main css  -->
<%--   <link rel="stylesheet" href="../..${pageContext.request.contextPath}/css/main.css"> --%> 
	<link rel="stylesheet" href="../..${pageContext.request.contextPath}/resources/css/main.css">
<!--   <link rel="stylesheet" href="../../SP_PRO/css/main.css">  -->
  <link rel="">
  <!-- Font Awesome -->
  <%-- <link rel="stylesheet" href="../..${pageContext.request.contextPath}/plugins/fontawesome-free/css/all.min.css"> --%>
  <!-- Ionicons -->
  <!-- <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"> -->
  <!-- icheck bootstrap -->
  <%-- <link rel="stylesheet" href="../..${pageContext.request.contextPath}/plugins/icheck-bootstrap/icheck-bootstrap.min.css"> --%>
  <!-- Theme style -->
  <%-- <link rel="stylesheet" href="../..${pageContext.request.contextPath}/dist/css/adminlte.min.css"> --%>
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
</head>
<body>
<div class="content">
        <div class="loginWrap">
			<c:out value="Hello World" />
			<c:out value="${pageContext.request.contextPath}"/>
            <div class="loginHead">
                <p class="logo">SPIRINK<span class="com">.com</span></p>
                <!-- <p class="welcome">Welcome back!</p> -->
            </div>
            <form name="loginForm" action="${pageContext.request.contextPath}/loginPost.do" method="post">
                <ul class="loginBox">  
                    <li>
                        <input type="email" id="tb_mem_email" name="tb_mem_email" onkeyup="mailEnterkey()" placeholder="이메일" value="">
                    </li>
                    <li>
                        <input type="password" id="tb_mem_pwd" name="tb_mem_pwd" onkeyup="enterkey()" placeholder="비밀번호" value="">
                    </li>
                    <!-- <div>
                        <input type="checkbox">
                    </div> -->
                    <li>
                        <div class="loginBtn" onClick="login_chk()">
                            <p>Login</p> 
                        </div>
                    </li>
                </ul>
            </form>
        </div>
    </div>

<!-- jQuery -->
<script src="../..${pageContext.request.contextPath}/plugins/jquery/jquery.min.js"></script>
<!-- AdminLTE App -->
<%-- <script src="../..${pageContext.request.contextPath}/dist/js/adminlte.min.js"></script> --%>
<script type="text/javascript">
 function mailEnterkey() { 
		 if (window.event.keyCode == 13) { 
			 if(loginForm.tb_mem_email.value!=''){
			 	loginForm.tb_mem_pwd.focus();
			 }
		 } 
 }
 
 function enterkey() { 
	 if (window.event.keyCode == 13) { 
		 login_chk() 
	 } 
 }
	
 function login_chk() {
        
        var params = {
        		 tb_mem_email: $("#tb_mem_email").val()
                , tb_mem_pwd  : $("#tb_mem_pwd").val()
        }
        
        // ajax 통신
        $.ajax({
            type : 'POST',            // HTTP method type(GET, POST) 형식이다.
            url : '${pageContext.request.contextPath}/mem_login_ajax.do',      // 컨트롤러에서 대기중인 URL 주소이다.
            data : params,          // Json 형식의 데이터이다.
            dataType:'json',
            success : function(res){ // 비동기통신의 성공일경우 success콜백으로 들어옵니다. 'res'는 응답받은 데이터이다.
                // 응답코드 > 0000
                if(res.code=='1111'){
                	loginForm.submit();
                	//alert("성공");
                }else{
                	alert("정확한 아이디 비밀번호를 입력하세요");
                	$("#tb_mem_email").focus();
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
                alert("통신 실패.")
            }
        });
  }
  
</script>
</body>
</html>
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
  <link rel="stylesheet" href="../..${pageContext.request.contextPath}/css/main.css"> 
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

            <div class="loginHead">
                <p class="logo">SPIRINK<span class="com">.com</span></p>
                <!-- <p class="welcome">Welcome back!</p> -->
            </div>
            <form name="loginForm" action="${pageContext.request.contextPath}/loginPost.do" method="post">
                <ul class="loginBox">  
                    <li>
                        <input type="email" id="tb_mem_email" name="tb_mem_email" onkeyup="mailEnterkey()" placeholder="?????????" value="">
                    </li>
                    <li>
                        <input type="password" id="tb_mem_pwd" name="tb_mem_pwd" onkeyup="enterkey()" placeholder="????????????" value="">
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
        
        // ajax ??????
        $.ajax({
            type : 'POST',            // HTTP method type(GET, POST) ????????????.
            url : '${pageContext.request.contextPath}/mem_login_ajax.do',      // ?????????????????? ???????????? URL ????????????.
            data : params,          // Json ????????? ???????????????.
            dataType:'json',
            success : function(res){ // ?????????????????? ??????????????? success???????????? ???????????????. 'res'??? ???????????? ???????????????.
                // ???????????? > 0000
                if(res.code=='1111'){
                	loginForm.submit();
                	//alert("??????");
                }else{
                	alert("????????? ????????? ??????????????? ???????????????");
                	$("#tb_mem_email").focus();
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){ // ????????? ????????? ??????????????? error ???????????? ???????????????.
                alert("?????? ??????.")
            }
        });
  }
  
</script>
</body>
</html>
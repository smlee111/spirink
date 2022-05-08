<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="x-ua-compatible" content="ie=edge">

  <title>TextNet Admin</title>
  
  <!-- Font Awesome -->
  <link rel="stylesheet" href="plugins/fontawesome-free/css/all.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!-- daterange picker -->
  <link rel="stylesheet" href="plugins/daterangepicker/daterangepicker.css">
  <!-- iCheck for checkboxes and radio inputs -->
  <link rel="stylesheet" href="plugins/icheck-bootstrap/icheck-bootstrap.min.css">
  <!-- Bootstrap Color Picker -->
  <link rel="stylesheet" href="plugins/bootstrap-colorpicker/css/bootstrap-colorpicker.min.css">
  <!-- Tempusdominus Bbootstrap 4 -->
  <link rel="stylesheet" href="plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
  <!-- Select2 -->
  <link rel="stylesheet" href="plugins/select2/css/select2.min.css">
  <link rel="stylesheet" href="plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
  <!-- Bootstrap4 Duallistbox -->
  <link rel="stylesheet" href="plugins/bootstrap4-duallistbox/bootstrap-duallistbox.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/adminlte.min.css">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
  
  <script type="text/javaScript" language="javascript" defer="defer">
        <!--
        /* 글 수정 화면 function */
        function fn_egov_select(id) {
        	document.listForm.selectedId.value = id;
           	document.listForm.action = "<c:url value='/updateSampleView.do'/>";
           	document.listForm.submit();
        }
        
        /* 글 등록 화면 function */
        function fn_egov_addView() {
           	document.listForm.action = "<c:url value='/addSample.do'/>";
           	document.listForm.submit();
        }
        
        /* 글 목록 화면 function */
        function fn_egov_selectList() {
        	document.listForm.action = "<c:url value='/egovSampleList.do'/>";
           	document.listForm.submit();
        }
        
        /* pagination 페이지 링크 function */
        function fn_egov_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	document.listForm.action = "<c:url value='/list.do'/>";
           	document.listForm.submit();
        }
        
        //-->
    </script>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  
  <%@ include file="/include/top.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">사용자관리</h1>
          </div><!-- /.col -->
          
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content -->
    <div class="content">
      <div class="container-fluid">
        <div class="row">
          <!-- left column -->
          <div class="col-md-12">
            <!-- jquery validation -->
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">사용자수정</h3>
              </div>
              <!-- /.card-header -->
              <!-- form start -->
              <form role="form" name="insForm" id="quickForm" method="post" action="${pageContext.request.contextPath}/my_update_ok.do">
              <input type="hidden" name="tb_mem_no" id="tb_mem_no" value="${member.tb_mem_no}">
                <div class="card-body">
                 <div class="form-group">
                    <label for="exampleInputEmail1">이메일</label>
                    <input type="email" name="tb_mem_email" class="form-control" id="tb_mem_email" readonly value="${member.tb_mem_email}" placeholder="이메일">
                  </div>
                  <div class="form-group">
                    <label for="exampleInputPassword1">비밀번호</label>
                    <input type="password" name="tb_mem_pwd" class="form-control" id="tb_mem_pwd" value="${member.tb_mem_pwd}" placeholder="비밀번호">
                  </div>
                  <div class="form-group">
                    <label for="exampleInputName">이름</label>
                    <input type="text" name="tb_mem_nm" class="form-control" id="tb_mem_nm" value="${member.tb_mem_nm}" placeholder="이름">
                  </div>
                 
                  <div class="form-group">
                    <label for="exampleInputTel">전화번호</label>
                    <input type="text" name="tb_mem_tel" class="form-control" id="tb_mem_tel" value="${member.tb_mem_tel}" placeholder="전화번호" maxLength='11'>
                  	
                  </div>
                  <div class="form-group">
                     <label for="exampleInputTel">사용여부</label>
                     <div class="card card-secondary">
		              <div class="card-body">
		              <c:choose>
								    <c:when test="${member.tb_mem_yn eq 'N'}">
								        <input type="checkbox" name="tb_mem_yn" value="Y" data-bootstrap-switch data-off-color="danger" data-on-color="success">
								    </c:when>
								    <c:otherwise>
								        <input type="checkbox" name="tb_mem_yn" value="Y" checked data-bootstrap-switch data-off-color="danger" data-on-color="success">
								    </c:otherwise>
						</c:choose>
		                
		              </div>
		            </div>
                  </div>
                  
                </div>
                <!-- /.card-body -->
                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">저장</button>
                </div>
              </form>
            </div>
            <!-- /.card -->
            </div>
          <!--/.col (left) -->
          <!-- right column -->
          <div class="col-md-6">

          </div>
          <!--/.col (right) -->
        </div>
        <!-- /.row -->
      </div><!-- /.container-fluid --> 
      
      
      
      
     
    </div>
    <!-- /.content -->
  </div>
   
  <!-- /.content-wrapper -->
	<%@ include file="/include/bottom.jsp" %>
  
</div>
<!-- ./wrapper -->

<!-- REQUIRED SCRIPTS -->

<!-- jQuery -->
<script src="plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- jquery-validation -->
<script src="plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="plugins/jquery-validation/additional-methods.min.js"></script>
<!-- Select2 -->
<script src="plugins/select2/js/select2.full.min.js"></script>
<!-- Bootstrap4 Duallistbox -->
<script src="plugins/bootstrap4-duallistbox/jquery.bootstrap-duallistbox.min.js"></script>
<!-- InputMask -->
<script src="plugins/moment/moment.min.js"></script>
<script src="plugins/inputmask/min/jquery.inputmask.bundle.min.js"></script>
<!-- date-range-picker -->
<script src="plugins/daterangepicker/daterangepicker.js"></script>
<!-- bootstrap color picker -->
<script src="plugins/bootstrap-colorpicker/js/bootstrap-colorpicker.min.js"></script>
<!-- Tempusdominus Bootstrap 4 -->
<script src="plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
<!-- Bootstrap Switch -->
<script src="plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>

<script type="text/javascript">
$(function () {
    //Initialize Select2 Elements
    $("input[data-bootstrap-switch]").each(function(){
        $(this).bootstrapSwitch('state', $(this).prop('checked'));
      });

})
$(document).ready(function () {
  $.validator.setDefaults({
    submitHandler: function () {
      //alert( "서밋 성공" );
      //insForm.submit();
    	
        var params = {
        		 tb_mem_email: $("#tb_mem_email").val()
                , tb_mem_nm  : $("#tb_mem_nm").val()
                , tb_mem_tel  : $("#tb_mem_tel").val()
                , tb_mem_pwd  : $("#tb_mem_pwd").val()
        }
        var chk = telValidator($("#tb_mem_tel").val());
        if(!chk){
        	return;
        }
        insForm.submit();
    }
  });
  $('#quickForm').validate({
    rules: {
    	tb_mem_nm: {
            required: true,
            minlength: 2
          },
    	tb_mem_email: {
        required: true,
        email: true,
      },
      tb_mem_tel: {
          required: true,
          number: true,
          minlength: 10
        },
      tb_mem_pwd: {
        required: true,
        minlength: 4
      },
      
    },
    messages: {
   	tb_mem_nm: {
           required: "이름을 입력해주세요",
           minlength: "이름은 2자이상입력해주세요"
         },
   	tb_mem_email: {
       required: "이메일주소를 입력해주세요",
       email: "정확한 이메일주소를 입력해주세요"
      },
      tb_mem_tel: {
          required: "전화번호를 입력해주세요",
          minlength: "전화번호는 10자이상입력해주세요",
          number: "전화번호는 숫자로 입력해주세요"
        },
    tb_mem_pwd: {
        required: "비밀번호를 입력해주세요",
        minlength: "비밀번호는 4자이상입력해주세요"
      },
      
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
      error.addClass('invalid-feedback');
      element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
      $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
      $(element).removeClass('is-invalid');
    }
  });
});
function telValidator(args) {
    const msg = '유효하지 않는 전화번호입니다.';
    // IE 브라우저에서는 당연히 var msg로 변경
    var regPhone = /^((01[1|6|7|8|9])[1-9]+[0-9]{6,7})|(010[1-9][0-9]{7})$/;

    if (regPhone.test(args)) {
        return true;
    }
    alert(msg);
    return false;
}
</script>
</body>
</html>
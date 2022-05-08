<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix = "fn"   uri = "http://java.sun.com/jsp/jstl/functions"%>

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
  
  
  <!-- Font Awesome Icons -->
  <link rel="stylesheet" href="plugins/fontawesome-free/css/all.min.css">
   <!-- DataTables -->
  <link rel="stylesheet" href="plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/adminlte.min.css">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
  
  <!-- Select2 -->
  <link rel="stylesheet" href="plugins/select2/css/select2.min.css">
  <link rel="stylesheet" href="plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
  <!-- Bootstrap4 Duallistbox -->
  <link rel="stylesheet" href="plugins/bootstrap4-duallistbox/bootstrap-duallistbox.min.css">
  
   <script type="text/javaScript" language="javascript" defer="defer">
	   /* 작업 목록 화면 function */
	   function fn_work_selectList(au_mem) {
		    document.listForm.bd_idx.value=au_mem;
	   		document.listForm.action = "<c:url value='/work_list.do'/>";
	      	document.listForm.submit();
	   }
	   function fn_type_chk_list() {
	   		document.insForm.action = "<c:url value='/type_chk_list.do'/>";
	      	document.insForm.submit();
	   }
	   function fn_work_spell_search() {
	   		document.insForm.action = "<c:url value='/type_chk_main.do'/>";
	      	document.insForm.submit();
	   }
	   function clearSearch() {
		    //alert("test");
	  		document.searchForm.searchKeyword.value = "";
	  		document.searchForm.searchKeyword.focus();
	  }
	  function getSelect() {
		   var gS = $("#type_select").val();
		   document.insForm.typeSEL.value =gS;
      }
	  function fn_chk_sentence() {
	   		document.insForm.action = "<c:url value='/type_chk_main.do'/>";
	      	document.insForm.submit();
	   }
	  function fn_down_excel(){
      	window.open(encodeURI('${pageContext.request.contextPath}/download/downloadFile.do?requestedFile=type_chk_templet.xlsx'))
      }
	  function fn_submit() {
		  	
         	document.insForm.submit();
         	LoadingWithMask();
      }
	  
	  function fileCheck( file )
	  {
	          // 사이즈체크
	          var maxSize  = 2 * 1024 * 1024    //5MB
	          //var maxSize  = 1 * 1024     //1kb
	          var fileSize = 0;

	  	// 브라우저 확인
	  	var browser=navigator.appName;
	  	
	  	// 익스플로러일 경우
	  	if (browser=="Microsoft Internet Explorer")
	  	{
	  		var oas = new ActiveXObject("Scripting.FileSystemObject");
	  		fileSize = oas.getFile( file.value ).size;
	  	}
	  	// 익스플로러가 아닐경우
	  	else
	  	{
	  		fileSize = file.files[0].size;
	  	}

	  	//alert("파일사이즈 : "+ fileSize +", 최대파일사이즈 : 5MB");

        if(fileSize > maxSize)
        {
            alert("첨부파일 사이즈는 2MB 이내로 등록 가능합니다.    ");
            return;
        }else{
        	fn_submit();
        }      
	  }
	  
	  function LoadingWithMask() {
          //화면의 높이와 너비를 구합니다.
          var maskHeight = $(document).height();
          var maskWidth  = window.document.body.clientWidth;
           
          //화면에 출력할 마스크를 설정해줍니다.
          var mask       ="<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
          var loadingImg ='';
            
          loadingImg +=" <img src='dist/img/Spinner.gif' style='position: absolute; display: block; margin: 0px auto;'/>";
       	
          //화면에 레이어 추가
          $('body')
              .append(mask)
          
          //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
          $('#mask').css({
                  'width' : maskWidth,
                  'height': maskHeight,
                  'opacity' :'0.3'
          });
        
          //마스크 표시
          $('#mask').show();
        
          //로딩중 이미지 표시
          $('#loadingImg').append(loadingImg);
          $('#loadingImg').show();
      }
	</script>
</head>
<body class="hold-transition sidebar-mini">
<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>
<div class="wrapper">
  
  <%@ include file="/include/top.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
  <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-4">
           		<h2>형태 적합성 검사</h2>
          </div><!-- /.col -->
          <div id='loadingImg' class="col-sm-4"></div>
          <div class="col-sm-4">
            <!-- <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">Dashboard v2</li>
            </ol> -->
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content -->
    <div class="content">
       
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <ul class="nav nav-tabs" id="custom-content-below-tab" role="tablist">
		              <li class="nav-item">
		                <a class="nav-link" onClick="fn_chk_sentence()" id="custom-content-below-home-tab" data-toggle="pill" href="#custom-content-below-home" role="tab" aria-controls="custom-content-below-home" aria-selected="true">문장검사</a>
		              </li>
		              <li class="nav-item">
		                <a class="nav-link active" id="custom-content-below-profile-tab" data-toggle="pill" href="#custom-content-below-profile" role="tab" aria-controls="custom-content-below-profile" aria-selected="false">파일검사</a>
		              </li>
		              <li class="nav-item">
		                <a class="nav-link" onClick="fn_type_chk_list()" id="custom-content-below-messages-tab" data-toggle="pill" href="#custom-content-below-messages" role="tab" aria-controls="custom-content-below-messages" aria-selected="false">검사내역</a>
		              </li>
		    </ul>  
            <div class="card">
            	<div class="card-header">
            	  <div class="container-fluid">
			        <div class="row mb-2">
			          <div class="col-sm-4">
			          </div>
			          <div id='loadingImg' class="col-sm-4"></div>
			          <div class="col-sm-4">
			              <button type="button" class="btn btn-block btn-outline-primary" onClick="fn_down_excel()">업로드 파일 규격 예시 내려받기</button>
			          </div>
			        </div>
			      </div><!-- /.container-fluid -->
			      </div>
			        <form class="form-horizontal" id="insForm" name="insForm" method="post" action="${pageContext.request.contextPath}/type_chk_insert_ok.do" enctype="multipart/form-data">
			        <div class="card-body">
		                    <input type=hidden id="bd_idx" name="bd_idx">
		                    <input type=hidden id="agent" name="agent" value="${agent}">
		                    <input type=hidden id="typeSEL" name="typeSEL" >
		                    <div class="form-group">
			                    <label for="exampleInputTel">파일업로드</label>
			                    <div class="input-group">
			                      <div class="custom-file">
			                        <input type="file" class="custom-file-input" id="bd_file_name" name="bd_file_name">
			                        <label class="custom-file-label" for="exampleInputFile">엑셀파일선택</label>
			                      </div>
			                    </div>
			                 </div>
		               
		              
	                                 검사항목
	                  <div class="select2-purple">
		                    <select id="type_select" name="type_select" onChange="getSelect()" class="select2" multiple="multiple" data-placeholder="검사항목 선택" data-dropdown-css-class="select2-purple" style="width: 100%;">
		                      <c:choose>
			                      <c:when test="${fn:contains(typeSEL,'ES')}">
			                      	<option selected value="ES">문장 앞뒤 공백제거(ES)</option>
			                      </c:when>
			                      <c:otherwise>
			                      	<option value="ES">문장 앞뒤 공백제거(ES)</option>
			                      </c:otherwise>
		                      </c:choose>
		                      <c:choose>
			                      <c:when test="${fn:contains(typeSEL,'DS')}">
			                      	<option selected value="DS">중복 공백 검사(DS)</option>
			                      </c:when>
			                      <c:otherwise>
			                      	<option value="DS">중복 공백 검사(DS)</option>
			                      </c:otherwise>
		                      </c:choose>
		                      <c:choose>
			                      <c:when test="${fn:contains(typeSEL,'SD')}">
			                      	<option selected value="SD">특수문자 제거(SD)</option>
			                      </c:when>
			                      <c:otherwise>
			                      	<option value="SD">특수문자 제거(SD)</option>
			                      </c:otherwise>
		                      </c:choose>
		                      <c:choose>
			                      <c:when test="${fn:contains(typeSEL,'PU')}">
			                      	<option selected value="PU">구두점(. ? !)검사(PU)</option>
			                      </c:when>
			                      <c:otherwise>
			                      	<option value="PU">구두점(. ? !)검사(PU)</option>
			                      </c:otherwise>
		                      </c:choose>
		                      <c:choose>
			                      <c:when test="${fn:contains(typeSEL,'SW')}">
			                      	<option selected value="SW">띄어쓰기(SW)</option>
			                      </c:when>
			                      <c:otherwise>
			                      	<option value="SW">띄어쓰기(SW)</option>
			                      </c:otherwise>
		                      </c:choose>
		                      <c:choose>
			                      <c:when test="${fn:contains(typeSEL,'CO')}">
			                      	<option selected value="CO">맞춤법(CO)</option>
			                      </c:when>
			                      <c:otherwise>
			                      	<option value="CO">맞춤법(CO)</option>
			                      </c:otherwise>
		                      </c:choose>
		                      <%-- 
		                      <c:choose>
			                      <c:when test="${fn:contains(typeSEL,'CO')}">
			                      	<option selected value="CO">띄어쓰기, 맞춤법(CO)</option>
			                      </c:when>
			                      <c:otherwise>
			                      	<option value="CO">띄어쓰기, 맞춤법(CO)</option>
			                      </c:otherwise>
		                      </c:choose> --%>
		                      
		                    </select>
		                  </div>
	                </div>
	                
	                
	                </form>
			      
			   
              
        	  
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
            <div class="card-footer">
				<div class="row mb-2">
						<div class="col-sm-3">
				        </div>
				        <div class="col-sm-6">
	                  		<button type="button" class="btn btn-block btn-primary" onClick="fileCheck(insForm.bd_file_name)" >저장</button>
	                  	</div>
	                  	<div class="col-sm-3">
				        </div>
	           </div>
           </div>              
          </div>
          <!-- /.col -->
        </div>
         
        <!-- /.row -->
      </div><!-- /.container-fluid -->
      <!-- Default box -->
     
    </div>
    <!-- /.content -->
   
  <!-- /.content-wrapper -->
	
  </div>
  <%@ include file="/include/bottom.jsp" %>
</div>
<!-- ./wrapper -->

<!-- REQUIRED SCRIPTS -->
<!-- jQuery -->
<script src="plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap -->
<script src="plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- overlayScrollbars -->
<script src="plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/adminlte.js"></script>

<!-- OPTIONAL SCRIPTS -->
<script src="dist/js/demo.js"></script>

<!-- PAGE PLUGINS -->
<!-- Select2 -->
<script src="plugins/select2/js/select2.full.min.js"></script>
<!-- Bootstrap4 Duallistbox -->
<script src="plugins/bootstrap4-duallistbox/jquery.bootstrap-duallistbox.min.js"></script>
<!-- jQuery Mapael -->
<script src="plugins/jquery-mousewheel/jquery.mousewheel.js"></script>
<script src="plugins/raphael/raphael.min.js"></script>
<script src="plugins/jquery-mapael/jquery.mapael.min.js"></script>
<script src="plugins/jquery-mapael/maps/usa_states.min.js"></script>
<!-- ChartJS -->
<script src="plugins/chart.js/Chart.min.js"></script>

<!-- PAGE SCRIPTS -->
<script src="dist/js/pages/dashboard2.js"></script>
<!-- bs-custom-file-input -->
<script src="plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script>
$(document).ready(function () {
	  bsCustomFileInput.init();
	});
$(function () {
    //Initialize Select2 Elements
    //$('.select2').select2()
	var $example = $(".select2").select2();
    //Initialize Select2 Elements
    $('.select2bs4').select2({
      theme: 'bootstrap4'
    })    
	getSelect();
  })
  
</script>
</body>
</html>
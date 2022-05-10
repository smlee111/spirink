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
	   function fn_work_spellList(bd_idx) {
	   		document.searchForm.action = "<c:url value='/spell_list.do'/>";
	   		document.searchForm.bd_idx.value=bd_idx;
	      	document.searchForm.submit();
	   }
	   function fn_work_spell_search() {
	   		document.searchForm.action = "<c:url value='/type_chk_main.do'/>";
	      	document.searchForm.submit();
	   }
	   function fn_chk_file() {
	   		document.searchForm.action = "<c:url value='/type_chk_file.do'/>";
	      	document.searchForm.submit();
	   }
	   function fn_type_chk_list() {
	   		document.searchForm.action = "<c:url value='/type_chk_list.do'/>";
	      	document.searchForm.submit();
	   }
	   function clearSearch() {
		    //alert("test");
	  		document.searchForm.searchKeyword.value = "";
	  		document.searchForm.searchKeyword.focus();
	  }
	  function getSelect() {
		   var gS = $("#type_select").val();
		   document.searchForm.typeSEL.value =gS;
      }

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
           		<h2>형태 적합성 검사</h2>
          </div><!-- /.col -->
          <div class="col-sm-6">
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
		                <a class="nav-link active" id="custom-content-below-home-tab" data-toggle="pill" href="#custom-content-below-home" role="tab" aria-controls="custom-content-below-home" aria-selected="true">문장검사</a>
		              </li>
		              <li class="nav-item">
		                <a class="nav-link" onClick="fn_chk_file()" id="custom-content-below-profile-tab" data-toggle="pill" href="#custom-content-below-profile" role="tab" aria-controls="custom-content-below-profile" aria-selected="false">파일검사</a>
		              </li>
		              <li class="nav-item">
		                <a class="nav-link" onClick="fn_type_chk_list()" id="custom-content-below-messages-tab" data-toggle="pill" href="#custom-content-below-messages" role="tab" aria-controls="custom-content-below-messages" aria-selected="false">검사내역</a>
		              </li>
		    </ul>  
            <div class="card">
              <section class="content-header">
			      <div class="container-fluid">
			        
			        <form class="form-horizontal" id="searchForm" name="searchForm" method="post">
	                <div class="card-body">
	                  <div class="form-group row">
	                    <input type=hidden id="bd_idx" name="bd_idx">
	                    <input type=hidden id="agent" name="agent" value="${agent}">
	                    <input type=hidden id="typeSEL" name="typeSEL" >
	                    <div class="col-sm-10">
	                      <input type="search" class="form-control form-control-navbar" aria-label="Search" name="searchKeyword" id="searchKeyword" placeholder="맞춤법검사할 문장을 입력하세요." value='<c:out value="${searchKeyword}"/>'>
	                    </div>
	                    <div class="btn-group float-right">
		                  <button type="button" class="btn btn-info" onClick="fn_work_spell_search()">검사시작</button>
		                  <!-- <button type="button" class="btn btn-info" onClick="fn_gen_select('1');">자동띄어쓰기</button> -->
		                  <!-- <button type="button" class="btn btn-default" onClick="clearSearch();">지우기</button> -->
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
		                      
		                    </select>
		                  </div>
	                </div>
	                
	                <!-- /.card-body -->
	                <div class="card-footer">
	                <div class="callout callout-success">
	                  <h5>${sp_quest}</h5>
	                  <p>${typeInsert}</p>
	                </div>
	                 
	                </div>
	                <!-- /.card-footer -->
	                </form>
			      </div><!-- /.container-fluid -->
			    </section>
              <!-- /.card-header -->
              
    			</div>
              </div>
              
        	  
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
			            
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
<script>

$(function () {
    //Initialize Select2 Elements
    $('.select2').select2()

    //Initialize Select2 Elements
    $('.select2bs4').select2({
      theme: 'bootstrap4'
    })    
	getSelect();
  })
  
</script>
</body>
</html>
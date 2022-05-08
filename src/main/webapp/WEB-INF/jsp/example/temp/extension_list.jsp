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
  
  
  <!-- Font Awesome Icons -->
  <link rel="stylesheet" href="plugins/fontawesome-free/css/all.min.css">
   <!-- DataTables -->
  <link rel="stylesheet" href="plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
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
        /* pagination 페이지 링크 function */
        function fn_egov_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	document.listForm.action = "<c:url value='/extension_list.do'/>";
           	document.listForm.submit();
        }
        //train
        function fn_egov_tran(id) {
        	document.listForm.searchCondition.value = id;
           	document.listForm.action = "<c:url value='/extension_list.do'/>";
           	document.listForm.submit();
           	LoadingWithMask();
        }
      //train
        function fn_egov_reg() {
        	document.listForm.selectedId.value = "register";
           	document.listForm.action = "<c:url value='/registerExtensionView.do'/>";
           	document.listForm.submit();
        }
        //modify
        function fn_egov_select(dic_idx) {
        	document.listForm.dic_idx.value = dic_idx;
        	document.listForm.selectedId.value = "modify";
        	
           	document.listForm.action = "<c:url value='/updateExtensionView.do'/>";
           	document.listForm.submit();
        }
                 
        function LoadingWithMask(gif) {
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
  
  <%@ include file="/include/genTop.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">확장사전관리</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <form class="form-inline ml-3">
			      <div class="input-group input-group-sm">
			        <input class="form-control form-control-navbar" type="search" id="searchKeyword" name="searchKeyword" placeholder="Search" aria-label="Search">
			        <input type="hidden" name="agent" id="agent" value="third">
			        <input type="hidden" name="searchCondition" id="searchCondition" value="1">
			        <input type="hidden" name="pageIndex" id="pageIndex" value="${pageIndex}">
			        <div class="input-group-append">
			          <button class="btn btn-navbar" type="submit">
			            <i class="fas fa-search"></i>
			          </button>
			        </div>
			      </div>
			    </form>
            </ol>
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
             
            <div class="card">
              <div class="card-header">
                 	총갯수: ${paginationInfo.totalRecordCount}<br>
                 	1-명사 2-동사 3-기타품사 4-하여동사 5-되어동사 6~9-예비 10-불규칙변형<br>
                    B:ㅂ불규칙 H:ㅎ불규칙 L:ㄹ불규칙 S:ㅅ불규칙 D:ㄷ불규칙 R:러 불규칙 X:규칙
                    <ol class="breadcrumb float-sm-right">
                     
		              <button type="button" class="btn btn-block btn-warning" onClick="fn_egov_reg()">신규등록</button>
		             
		            </ol>
              </div>
             
              <div class="row text-center" style="width: 100%">
 
                    <div style="width: 100%; float:none; margin:0 auto" >
                            <ui:pagination paginationInfo = "${paginationInfo}" type="text" jsFunction="fn_egov_link_page" />
                     <div>
                </div>  
              <!-- /.card-header -->
              <div class="card-body">
              
               
                <table id="example1" class="table table-bordered table-hover" summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<caption style="visibility:hidden">카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블</caption>
        			<thead>
        			<tr>
        				<td align="center">No</th>
        				<td align="center">단어명</th>
        				<td align="center">단어속성</th>
        				<td align="center">사용여부</th>
        				<td align="center">수정자</th>
        				<td align="center">수정일</th>
        				
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd">${result.dic_idx}</td>
            				<td align="center" class="listtd"><a href="javascript:fn_egov_select('<c:out value="${result.dic_idx}"/>')">${result.dic_word}</a></td>
            				<td align="center" class="listtd">${result.dic_nvzdbipscc}</td>
            				<td align="center" class="listtd">${result.dic_use_yn}</td>
            				<td align="center" class="listtd">${result.dic_user}</td>
            				<td align="center" class="listtd">${result.dic_upddate}</td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		<form:form commandName="searchVO" id="listForm" name="listForm" method="post">
        		<input type="hidden" name="selectedId" />
        		<input type="hidden" name="agent" value="third"/>
        		<input type="hidden" name="dic_idx" />
        		<input type="hidden" name="searchKeyword" id="searchKeyword"  value="${searchKeyword}"/>
                <div class="row text-center" style="width: 100%">
 
                    <div style="width: 30%; float:none; margin:0 auto" >
                            <ui:pagination paginationInfo = "${paginationInfo}" type="text" jsFunction="fn_egov_link_page" />
        					<input type="hidden" name="pageIndex" id="pageIndex" value="${pageIndex}">
        					<input type="hidden" name="agent" id="agent" value="second">
        					<input type="hidden" name="searchCondition" id="searchCondition" value="1">
                     <div>
                 </div>
                 </form:form>
                    </div>
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
<!-- DataTables -->
<script src="plugins/datatables/jquery.dataTables.min.js"></script>
<script src="plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<!-- page script -->
<script src="../../dist/js/demo.js"></script>
<!-- page script -->
<script>
  $(function () {
    
    $('#example1').DataTable({
      "paging": false,
      "lengthChange": false,
      "searching": false,
      "ordering": false,
      "info": false,
      "autoWidth": false,
      "responsive": true,
    });
  });
</script>
</body>
</html>
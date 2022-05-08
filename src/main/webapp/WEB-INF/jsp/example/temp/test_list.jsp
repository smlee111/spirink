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
        	document.listForm.action = "<c:url value='/test_list.do'/>";
           	document.listForm.submit();
        }
        //-->
         function fn_egov_test_ok() {
        	//alert("수정후 사전적용");
           	document.updateForm.action = "<c:url value='exec_test.do'/>";
           	document.updateForm.submit();
           	LoadingWithMask();
        }
        /* pagination 페이지 링크 function */
        function fn_egov_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	document.listForm.action = "<c:url value='/test_list.do'/>";
           	document.listForm.submit();
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
<div id='loadingImg'></div>
<div class="wrapper">
  
  <%@ include file="/include/top.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">테스트목록</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <form class="form-inline ml-3" method="post">
			      <div class="input-group input-group-sm">
			        <input class="form-control form-control-navbar" type="search" id="searchKeyword" name="searchKeyword" placeholder="Search" aria-label="Search">
			        <input type="hidden" name="agent" id="agent" value="test">
			        <input type="hidden" name="searchCondition" id="searchCondition" value="1">
			        
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
              
              <div class="row mb-2">
                 <div class="col-sm-2">
                 <form:form commandName="searchVO" id="updateForm" name="updateForm" method="post">
                 		<a href="javascript:fn_egov_test_ok()" class="btn btn-sm btn-info float-left">테스트시작</a>
                 		<!-- <button type="button" class="btn btn-block btn-warning" onClick="fn_egov_test_ok()">테스트</button> -->
                 </form:form>
                 </div>
                 <div class="col-sm-10">
                 <ol class="breadcrumb float-sm-right">
                 	
                 	총갯수:${paginationInfo.totalRecordCount} 정답률 : ${sucRate}%
                 	
                 </ol>
                 </div>
                 
                 </div>
              </div>
              <!-- /.card-header -->
              <div class="card-body">
                 
                <table id="example1" class="table table-bordered table-hover" summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<caption style="visibility:hidden">카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블</caption>
        			<thead>
        			<tr>
        				<td align="center">No</th>
        				<td align="center">변환전</th>
        				<td align="center">변환형태</th>
        				<td align="center">예상답변</th>
        				<td align="center">변환후</th>
        				<td align="center">결과</th>
        				<td align="center">어미분석</th>
        				<td align="center">테스트일시</th>
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><c:out value="${result.sty_idx}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.sty_quest}"/></a></td>
            				<td align="left" class="listtd"><c:out value="${result.sty_type}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.sty_answer}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.sty_convert}"/></td>
            				<td align="center" class="listtd">
            					<c:choose>
								    <c:when test="${result.sty_ans_tf eq 'N'}">
								        <span class="badge badge-danger">오류</span>
								    </c:when>
								    <c:otherwise>
								        <span class="badge badge-success">성공</span>
								    </c:otherwise>
								</c:choose>
            				</td>
            				<td align="center" class="listtd"><c:out value="${result.sty_emi}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.sty_upd_dt}"/></td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		<form:form commandName="searchVO" id="listForm" name="listForm" method="post">
        		<input type="hidden" name="selectedId" />
                <div class="row text-center" style="width: 100%">
 
                    <div style="width: 30%; float:none; margin:0 auto" >
                            <ui:pagination paginationInfo = "${paginationInfo}" type="text" jsFunction="fn_egov_link_page" />
        					<form:hidden path="pageIndex" />
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
      "autoWidth": true,
      "responsive": true,
    });
  });
</script>
</body>
</html>
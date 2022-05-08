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
	  function clearSearch() {
		    //alert("test");
	  		document.listForm.searchKeyword.value = "";
	  		document.listForm.searchKeyword.focus();
	  }
	  function fn_gen_select(id) {
      	    document.listForm.selectedId.value = id;
         	document.listForm.action = "<c:url value='/wrtStyGen.do'/>";
         	document.listForm.submit();
      }
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
            <h1 class="m-0 text-dark">문체변환</h1>
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
             <a href="${pageContext.request.contextPath}/test_list.do?agent=test">
                  <p>대량변환</p>
                </a>  
            <div class="card">
                <form:form class="form-horizontal" commandName="searchVO" id="listForm" name="listForm" method="post">
                <div class="card-body">
                  <div class="form-group row">
                    <label for="searchKeyword" class="col-sm-2 col-form-label">변환문장입력</label>
                    <div class="col-sm-10">
                      <input type="text" class="form-control" name="searchKeyword" id="searchKeyword" placeholder="변환할 문장을 입력하세요." value='<c:out value="${searchKeyword}"/>'>
                      <input type="hidden"  name="selectedId" id="selectedId" value="2">
                    </div>
                  </div>
                </div>
               
                <!-- /.card-body -->
                <div class="card-footer">
		        
                  <div class="btn-group float-right">
	                  <button type="button" class="btn btn-info" onClick="fn_gen_select('2');">변환</button>
	                  <!-- <button type="button" class="btn btn-info" onClick="fn_gen_select('1');">자동띄어쓰기</button> -->
	                  <button type="button" class="btn btn-default" onClick="clearSearch();">다시입력</button>
                  </div>
                </div>
                <!-- /.card-footer -->
              </form:form>
              <div class="card-header">
                <h3 class="card-title">형태소분석원문: <c:out value="${firstStr}"/> </h3>
              </div>
               <div class="card-body">
              
               <table id="example2" class="table table-bordered table-hover" summary="변환결과를 보는 테이블">
        			<thead>
        			<tr>
        				<td align="center">형태소</th>
        				<td align="center">형태소구분</th>
        				<td align="center">형태소패턴</th>
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${analyList}" varStatus="status">
            			<tr>
            				<td align="left" class="listtd"><c:out value="${result.anycont}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.anynum}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.anytype}"/></td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		
                    
              </div>
              <!-- /.card-body -->
              <div class="card-body">
              
               <table id="example1" class="table table-bordered table-hover" summary="변환결과를 보는 테이블">
        			<thead>
        			<tr>
        				<td align="center">구분</th>
        				<td align="center">문체변환결과</th>
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="left" class="listtd"><c:out value="${result.id}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.gen}"/></td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		
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
      "responsive": false,
    });
  });
</script>
</body>
</html>
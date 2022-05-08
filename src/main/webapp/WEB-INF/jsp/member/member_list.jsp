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
        
         function fn_egov_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	document.listForm.action = "<c:url value='/member_list.do'/>";
           	document.listForm.submit();
        }
        function fn_modify(tb_mem_no) {
        	document.listForm.tb_mem_no.value = tb_mem_no;
           	document.listForm.action = "<c:url value='/member_update.do'/>";
           	document.listForm.submit();
        }
        /* 권한 목록 화면 function */
        function fn_auth_selectList(au_mem) {
        	document.listForm.searchKeyword.value=au_mem;
        	document.listForm.action = "<c:url value='/auth_list.do'/>";
           	document.listForm.submit();
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
            <h1 class="m-0 text-dark">사용자관리</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <form class="form-inline ml-3" method="post" action="/member_list.do">
			      <div class="input-group input-group-sm">
			        <input class="form-control form-control-navbar" name="searchKeyword" type="search" placeholder="Search" aria-label="Search">
			        <input type="hidden" name="searchCondition" value="1" placeholder="Search" aria-label="Search">
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
              <section class="content-header">
			      <div class="container-fluid">
			        <div class="row mb-2">
			          <div class="col-sm-6">
			            <h1>사용자목록</h1>
			          </div>
			          <div class="col-sm-6">
			            <ol class="breadcrumb float-sm-right">
			              <li class="breadcrumb-item">
			              <a href="${pageContext.request.contextPath}/member_insert.do">
			              <button type="button" class="btn btn-block btn-outline-primary">신규등록</button>
			              </a>
			              </li>
			            </ol>
			          </div>
			        </div>
			      </div><!-- /.container-fluid -->
			    </section>
              <!-- /.card-header -->
              <div class="card-body">
              
                <table id="example1" class="table table-bordered table-hover" summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<caption style="visibility:hidden">카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블</caption>
        			<thead>
        			<tr>
        				<td align="center">No</th>
        				<td align="center">이름</th>
        				<td align="center">이메일</th>
        				<td align="center">전화번호</th>
        				<td align="center">수정</th>
        				<td align="center">권한관리</th>
        				<td align="center">사용여부</th>
        				<td align="center">등록일시</th>
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><c:out value="${((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.tb_mem_nm}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.tb_mem_email}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.tb_mem_tel}"/></td>
            				<td align="center" class="listtd"><button onClick="fn_modify(${result.tb_mem_no})" type="button" class="btn btn-block btn-outline-info btn-sm">수정</button></td>
            				<td align="center" class="listtd"><button type="button" onClick="fn_auth_selectList('${result.tb_mem_no}')" class="btn btn-block btn-outline-success btn-sm">권한관리</button></td>
            				<td align="center" class="listtd">
            				<c:choose>
								    <c:when test="${result.tb_mem_yn eq '미사용'}">
								        <span class="badge badge-danger">미사용</span>
								    </c:when>
								    <c:otherwise>
								        <span class="badge badge-success">사용</span>
								    </c:otherwise>
							</c:choose>
            				</td>
            				<td align="center" class="listtd"><c:out value="${result.tb_ins_dt}"/></td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		<form:form commandName="searchVO" id="listForm" name="listForm" method="post">
        		<input type="hidden" name="selectedId" />
        		<input type="hidden" name="searchKeyword" />
        		<input type="hidden" name="tb_mem_no" />
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
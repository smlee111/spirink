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
  <!-- iCheck for checkboxes and radio inputs -->
  <link rel="stylesheet" href="plugins/icheck-bootstrap/icheck-bootstrap.min.css">
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
        function fn_auth_update(){
        	document.authForm.submit();
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
            <h1 class="m-0 text-dark">권한관리</h1>
          </div><!-- /.col -->
          <%-- <div class="col-sm-6">
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
          </div> --%><!-- /.col -->
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
			            <h1><c:out value="${mem_nm}"/>님</h1>
			          </div>
			         <!--  <div class="col-sm-5">
			            <ol class="breadcrumb float-sm-right">
			              <li class="breadcrumb-item">
			              
			              전체 <input type="checkbox" name="au_mod" value="Y" checked data-bootstrap-switch data-off-color="danger" data-on-color="success">
			              </li>
			            </ol>
			          </div> -->
			          <div class="col-sm-6">
			            <ol class="breadcrumb float-sm-right">
			              <li class="breadcrumb-item">
			              
			              <button type="button" onClick="fn_auth_update()" class="btn btn-block btn-outline-primary">적용</button>
			              
			              </li>
			            </ol>
			          </div>
			        </div>
			      </div><!-- /.container-fluid -->
			    </section>
              <!-- /.card-header -->
              <form class="form-inline ml-3" name="authForm" method="post" action="/auth_update.do">
              <input type="hidden" name="au_mem" value="${au_mem}">
              <div class="card-body">
                <table id="example1" class="table table-bordered table-hover" summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<caption style="visibility:hidden">카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블</caption>
        			<thead>
        			<tr>
        				<td align="center">No</th>
        				<td align="center">대분류</th>
        				<td align="center">중분류</th>
        				<td align="center">조회</th>
        				<td align="center">수정</th>
        				<!-- <td align="center">권한관리</th>
        				<td align="center">사용여부</th>
        				<td align="center">등록일시</th> -->
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><c:out value="${((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.mu_title}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.mu_bigo}"/></td>
            				<td align="center" class="listtd"><%-- <c:out value="${result.au_list}"/> --%>
            				<c:choose>
							    <c:when test="${result.au_list eq 'Y'}">
							        <input type="checkbox" name="au_list_${result.mu_idx}" value="Y" checked data-bootstrap-switch data-off-color="danger" data-on-color="success">
							    </c:when>
							    <c:otherwise>
							        <input type="checkbox" name="au_list_${result.mu_idx}" value="Y"  data-bootstrap-switch data-off-color="danger" data-on-color="success">
							    </c:otherwise>
							</c:choose>
            				
            				</td>
            				<td align="center" class="listtd"><%-- <c:out value="${result.au_mod}"/> --%>
            				<c:choose>
            					<c:when test="${result.au_mod eq 'Y'}">
							        <input type="checkbox" name="au_mod_${result.mu_idx}" value="Y" checked data-bootstrap-switch data-off-color="danger" data-on-color="success">
							    </c:when>
							    <c:otherwise>
							        <input type="checkbox" name="au_mod_${result.mu_idx}" value="Y"  data-bootstrap-switch data-off-color="danger" data-on-color="success">
							    </c:otherwise>
							 </c:choose>
            				</td>
            				<%-- <td align="center" class="listtd"><button type="button" class="btn btn-block btn-outline-success btn-sm">권한관리</button></td>
            				<td align="center" class="listtd"><c:out value="${result.tb_mem_yn}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.tb_ins_dt}"/></td> --%>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		
                </div>
                </form>
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

<!-- Bootstrap Switch -->
<script src="plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>
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
    $("input[data-bootstrap-switch]").each(function(){
        $(this).bootstrapSwitch('state', $(this).prop('checked'));
      });
  });
</script>
</body>
</html>
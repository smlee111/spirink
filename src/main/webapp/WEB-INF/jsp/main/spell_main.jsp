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
	   		document.searchForm.action = "<c:url value='/spell_main.do'/>";
	      	document.searchForm.submit();
	   }
	   function clearSearch() {
		    //alert("test");
	  		document.searchForm.searchKeyword.value = "";
	  		document.searchForm.searchKeyword.focus();
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
            <c:choose>
			    <c:when test="${agent eq 'first'}">
	                  <h1 class="m-0 text-dark">스피링크 프로젝트</h1>
			    </c:when>
			    <c:when test="${agent eq 'second'}">
	                  <h1 class="m-0 text-dark">두번째프로젝트</h1>
			    </c:when>
			    <c:otherwise>
			        <h1 class="m-0 text-dark">로그인을하세요</h1>
			    </c:otherwise>
			  </c:choose>
            
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
              
            <div class="card">
              <section class="content-header">
			      <div class="container-fluid">
			        <div class="row mb-2">
			          <div class="col-sm-6">
			            <h1>맞춤법검사</h1>
			          </div>
			          <div class="col-sm-6">
			            <ol class="breadcrumb float-sm-right">
			              <li class="breadcrumb-item">
			              <a href="${pageContext.request.contextPath}/spell_insert.do?agent=<%=agent%>">
			              <button type="button" class="btn btn-block btn-outline-primary">여러건 엑셀등록</button>
			              </a>
			              </li>
			            </ol>
			          </div>
			        </div>
			        <form class="form-horizontal" id="searchForm" name="searchForm" method="post">
	                <div class="card-body">
	                  <div class="form-group row">
	                    <input type=hidden id="bd_idx" name="bd_idx">
	                    <input type=hidden id="agent" name="agent" value="${agent}">
	                    <label for="searchKeyword" class="col-sm-2 col-form-label">맞춤법 검사 문장입력</label>
	                    <div class="col-sm-10">
	                      <input type="text" class="form-control" name="searchKeyword" id="searchKeyword" placeholder="맞춤법검사할 문장을 입력하세요." value='<c:out value="${searchKeyword}"/>'>
	                    </div>
	                    
	                  </div>
	                </div>
	                
	                <!-- /.card-body -->
	                <div class="card-footer">
	                  ${sp_quest}
	                  <div class="btn-group float-right">
		                  <button type="button" class="btn btn-info" onClick="fn_work_spell_search()">맞춤법검사</button>
		                  <!-- <button type="button" class="btn btn-info" onClick="fn_gen_select('1');">자동띄어쓰기</button> -->
		                  <button type="button" class="btn btn-default" onClick="clearSearch();">지우기</button>
	                  </div>
	                </div>
	                <!-- /.card-footer -->
	                </form>
			      </div><!-- /.container-fluid -->
			    </section>
              <!-- /.card-header -->
              <div class="card-body">
              
                <table id="example1" class="table table-bordered table-hover" summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<caption style="visibility:hidden">카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블</caption>
        			<thead>
        			<tr>
        				<td align="center">No</th>
        				<td align="center">작업명</th>
        				<td align="center">작업내용</th>
        				<td align="center">맞춤법</th>
        				<td align="center">작업상태</th>
        				<td align="center">파일명</th>
        				<td align="center">갯수</th>
        				<td align="center">작업시간(초)</th>
        				<td align="center">등록일시</th>
        				<td align="center">변경일시</th>
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><c:out value="${((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.bd_title}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_comment}"/></td>
            				<td align="center" class="listtd"><button type="button" onClick="fn_work_spellList('${result.bd_idx}')" class="btn btn-block btn-outline-success btn-sm">맞춤법열기</button></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_state}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_org_name}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_cnt}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_time}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_ins_dt}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_upd_dt}"/></td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		<form:form commandName="searchVO" id="listForm" name="listForm" method="post">
        		<input type="hidden" name="selectedId" />
        		<input type="hidden" name="searchKeyword" />
        		<input type="hidden" name="tb_mem_no" />
                <input type="hidden" name="bd_idx" />
                <input type="hidden" name="agent" value="${agent}"/>
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
<!-- jQuery Mapael -->
<script src="plugins/jquery-mousewheel/jquery.mousewheel.js"></script>
<script src="plugins/raphael/raphael.min.js"></script>
<script src="plugins/jquery-mapael/jquery.mapael.min.js"></script>
<script src="plugins/jquery-mapael/maps/usa_states.min.js"></script>
<!-- ChartJS -->
<script src="plugins/chart.js/Chart.min.js"></script>

<!-- PAGE SCRIPTS -->
<script src="dist/js/pages/dashboard2.js"></script>

</body>
</html>
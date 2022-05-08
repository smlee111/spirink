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
		    if(listForm.searchKeyword.value==""){
		    	alert("검색할 단어를 입력하세요");
		    	listForm.searchKeyword.focus();
		    	return;
		    }
      	    document.listForm.selectedId.value = id;
         	document.listForm.action = "<c:url value='/twitter_search.do'/>";
         	document.listForm.submit();
         	LoadingWithMask();
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
<body class="hold-transition sidebar-mini" onLoad="listForm.searchKeyword.focus();">
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
            <h1 class="m-0 text-dark">트위터크롤링</h1>
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
                <form:form class="form-horizontal" commandName="searchVO" id="listForm" name="listForm" method="post">
                <div class="card-body">
                  <div class="form-group row">
                    <div class="col-sm-12">
                      <textarea class="form-control" rows="20" placeholder="Enter ..." disabled ><c:out value="${fileContent}"/></textarea>
                    </div>
                  </div>
                </div>
                <!-- /.card-body -->
                           <!-- /.card-footer -->
              </form:form>
              
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
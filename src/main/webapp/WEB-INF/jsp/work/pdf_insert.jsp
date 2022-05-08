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
        function fn_down_excel(){
        	<c:if test="${jsonPrj eq 'hancom'}">
        		window.open(encodeURI('${pageContext.request.contextPath}/download/downloadFile.do?requestedFile=hancom_json_templet.xlsx'))
        	</c:if>
        	<c:if test="${jsonPrj eq 'hanbat'}">
        		window.open(encodeURI('${pageContext.request.contextPath}/download/downloadFile.do?requestedFile=hanbat_json_templet.xlsx'))
        	</c:if>
        }
        function fn_submit() {
           	document.insForm.submit();
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
            <h1 class="m-0 text-dark">PDF 등록</h1>
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
                <div class="container-fluid">
			        <div class="row mb-2">
			          <div class="col-sm-10">
			                      	<h3>PDF 작업등록</h3>
			          </div>
			          <div class="col-sm-2">
			              <button type="button" class="btn btn-block btn-outline-primary" onClick="fn_down_excel()">샘플PDF 다운로드</button>
			          </div>
			        </div>
			      </div><!-- /.container-fluid -->
                
              </div>
              <!-- /.card-header -->
              <!-- form start -->
              <form role="form" name="insForm" id="insForm" method="post" action="${pageContext.request.contextPath}/json_insert_ok.do" enctype="multipart/form-data">
                <div class="card-body">
                   <div class="form-group">
                    <label for="exampleInputEmail1">작업명</label>
                    <input type="hidden" name="jsonPrj" class="form-control" id="jsonPrj" placeholder="프로젝트명" value='<c:out value="${jsonPrj}"/>'>
                    <input type="hidden" name="agent" class="form-control" id="bd_title" placeholder="작업명" value='<c:out value="${agent}"/>'>
                    <input type="text" name="bd_title" class="form-control" id="bd_title" placeholder="작업명">
                  </div>
                   
                  <!-- <div class="form-group">
                    <label for="exampleInputName">작업내용</label>
                    <input type="text" name="bd_comment" class="form-control" id="bd_comment" placeholder="작업내용">
                  </div> -->
                 
                  <div class="form-group">
                    <label for="exampleInputTel">파일업로드</label>
                    <div class="input-group">
                      <div class="custom-file">
                        <input type="file" class="custom-file-input" id="bd_file_name" name="bd_file_name">
                        <label class="custom-file-label" for="exampleInputFile">엑셀파일선택</label>
                      </div>
                    </div>
                  </div>
                  
                </div>
                <!-- /.card-body -->
                <div class="card-footer">
                  <button type="button" class="btn btn-primary" onClick="fn_submit();" >저장+변환</button>
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
<!-- AdminLTE App -->
<script src="dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<!-- bs-custom-file-input -->
<script src="plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<!-- page script -->
<script type="text/javascript">
$(document).ready(function () {
  bsCustomFileInput.init();
});
</script>
</body>
</html>
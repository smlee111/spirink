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
  
  
  
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  
  <!-- iCheck for checkboxes and radio inputs -->
  <link rel="stylesheet" href="plugins/icheck-bootstrap/icheck-bootstrap.min.css">
  <!-- Bootstrap Color Picker -->
  <link rel="stylesheet" href="plugins/bootstrap-colorpicker/css/bootstrap-colorpicker.min.css">
  
  <!-- Select2 -->
  <link rel="stylesheet" href="plugins/select2/css/select2.min.css">
  <link rel="stylesheet" href="plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
  
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/adminlte.min.css">
  
  <script type="text/javaScript" language="javascript" defer="defer">
        <!--
        /* ??? ?????? ?????? function */
        function fn_egov_select(id) {
        	document.listForm.selectedId.value = id;
           	document.listForm.action = "<c:url value='/updateSampleView.do'/>";
           	document.listForm.submit();
        }
        
        /* ??? ?????? ?????? function */
        function fn_egov_addView() {
           	document.listForm.action = "<c:url value='/addSample.do'/>";
           	document.listForm.submit();
        }
        
        /* ??? ?????? ?????? function */
        function fn_egov_selectList() {
        	document.listForm.action = "<c:url value='/egovSampleList.do'/>";
           	document.listForm.submit();
        }
        
        /* pagination ????????? ?????? function */
        function fn_egov_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	document.listForm.action = "<c:url value='/list.do'/>";
           	document.listForm.submit();
        }
        
        //-->
        /* pagination ????????? ?????? function */
        function fn_egov_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	document.listForm.action = "<c:url value='/total_list.do'/>";
           	document.listForm.submit();
        }
        //train
        function fn_egov_update_ok() {
        	//alert("????????? ????????????");
           	document.updateForm.action = "<c:url value='updateTotalOk.do'/>";
           	document.updateForm.submit();
           	LoadingWithMask();
        }
        
        function fn_egov_select(id) {
        	document.listForm.selectedId.value = "modify";
           	document.listForm.action = "<c:url value='/updateTotalView.do'/>";
           	document.listForm.submit();
        }
                 
        function LoadingWithMask(gif) {
            //????????? ????????? ????????? ????????????.
            var maskHeight = $(document).height();
            var maskWidth  = window.document.body.clientWidth;
             
            //????????? ????????? ???????????? ??????????????????.
            var mask       ="<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
            var loadingImg ='';
              
            loadingImg +=" <img src='dist/img/Spinner.gif' style='position: absolute; display: block; margin: 0px auto;'/>";
         	
            //????????? ????????? ??????
            $('body')
                .append(mask)
            
            //???????????? ????????? ????????? ?????? ????????? ????????? ?????? ????????? ????????????.
            $('#mask').css({
                    'width' : maskWidth,
                    'height': maskHeight,
                    'opacity' :'0.3'
            });
          
            //????????? ??????
            $('#mask').show();
          
            //????????? ????????? ??????
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
            <h1 class="m-0 text-dark">??????????????????</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <form class="form-inline ml-3">
			      <div class="input-group input-group-sm">
			        
			        <input type="hidden" name="agent" id="agent" value="second">
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
                 	No: ${totalData.dic_idx}<br>
                 	1-?????? 2-?????? 3-???????????? 4-???????????? 5-???????????? 6~9-?????? 10-???????????????<br>
                    B:???????????? H:???????????? L:???????????? S:???????????? D:???????????? R:??? ????????? X:??????
              </div>
              <!-- /.card-header -->
              <div class="card-body">
              <form:form commandName="searchVO" id="updateForm" name="updateForm" method="post">
               <input type="hidden" name="pageIndex" id="pageIndex" value="${totalData.pageIndex}">
               <input type="hidden" name="agent" id="agent" value="${agent}">
               <input type="hidden" name="dic_idx" id="dic_idx" value="${totalData.dic_idx}">
               <input type="hidden" id="searchKeyword" name="searchKeyword" value="${searchKeyword}">
                  <div class="row">
                    <div class="col-sm-6">
                      <!-- text input -->
                      <div class="form-group">
                        <label>????????????</label>
                        <input type="text" name="dic_word" id="dic_word" class="form-control" placeholder="Enter ..." value="${totalData.dic_word}">
                      </div>
                    </div>
                    <div class="col-sm-6">
                      <div class="form-group">
                        <label>????????????</label>
                        <input type="text"  class="form-control" placeholder="Enter ..." disabled value="${totalData.dic_word_b}">
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-sm-6">
                      <!-- textarea -->
                      <div class="form-group">
                        <label>????????????</label>
                        <input class="form-control" name="dic_nvzdbipscc" id="dic_nvzdbipscc" rows="3" placeholder="Enter ..." value="${totalData.dic_nvzdbipscc}">
                      </div>
                    </div>
                    <div class="col-sm-6">
                      <div class="form-group">
                        <label>????????????</label>
                        <input class="form-control" rows="3" placeholder="Enter ..." disabled value="${totalData.dic_nvzdbipscc_b}">
                      </div>
                    </div>
                  </div>
				  <div class="form-group">
                        <label>????????????</label>
                        <input type="checkbox" name="dic_use_yn" id="dic_use_yn" value="Y" <c:if test="${totalData.dic_use_yn == 'Y'}">checked</c:if>  data-bootstrap-switch>
                  </div>
		          <div class="card-header">
                    <ol class="breadcrumb float-sm-right">
		              <button type="button" class="btn btn-block btn-warning" onClick="fn_egov_update_ok()">??????</button>
		             </ol>
                  </div>      

                  
                </form:form>
             
                
              
        	  
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
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
	   		document.searchForm.action = "<c:url value='/type_chk_detail.do'/>";
	   		document.searchForm.bd_idx.value=bd_idx;
	      	document.searchForm.submit();
	   }
	   function fn_type_chk_process(bd_idx,bd_comment) {
	   		document.searchForm.action = "<c:url value='/type_chk_process.do'/>";
	   		document.searchForm.bd_idx.value=bd_idx;
	   		document.searchForm.searchKeyword.value=bd_comment;
	      	document.searchForm.submit();
	      	LoadingWithMask();
	   }
	   function fn_work_spell_search() {
	   		document.searchForm.action = "<c:url value='/type_chk_main.do'/>";
	      	document.searchForm.submit();
	   }
	   function fn_chk_file() {
	   		document.searchForm.action = "<c:url value='/type_chk_file.do'/>";
	      	document.searchForm.submit();
	   }
	   function fn_chk_sentence() {
	   		document.searchForm.action = "<c:url value='/type_chk_main.do'/>";
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
	  function fn_type_chk_del() {
		   var checkRow = "";
		   $($( "input[name='checkRow']:checked" ).get().reverse()).each (function (){
		     checkRow = checkRow + $(this).val()+"," ;
		   });
		   checkRow = checkRow.substring(0,checkRow.lastIndexOf( ",")); //맨끝 콤마 지우기
		   
		   if(checkRow == ''){
		     alert("삭제할 대상을 선택하세요.");
		     return false;
		   }else{
			   document.searchForm.bd_chk.value=checkRow;
			   //alert(document.searchForm.bd_chk.value);
			   document.searchForm.action = "<c:url value='/type_chk_del.do'/>";
			   if(confirm("선택한 내역을 삭제하시겠습니까?")){
		       		document.searchForm.submit();
			   }else{
				    return false;
			   }
		   }
	   }
	  function fn_down_excel() {
		   var checkRow = "";
		   var chk=true;
		   $($( "input[name='checkRow']:checked" ).get().reverse()).each (function (){
			 var sel = document.getElementById('bt_'+$(this).val());
			 if(sel.value=='준비'){
				alert("검사완료후 내려받기 해주시기 바랍니다.");
				chk=false;
				return false;
			 }else{
		     	checkRow = checkRow + $(this).val()+"_" ;
			 }
		   });
		   checkRow = checkRow.substring(0,checkRow.lastIndexOf( "_")); //맨끝 콤마 지우기
		  
		   if(chk && checkRow == ''){
		     alert("내려받을 대상을 선택하세요.");
		     return false;
		   }else if(chk){
			   document.searchForm.requestedFile.value=checkRow;
			   //alert(document.searchForm.bd_chk.value);
			   document.searchForm.action = "<c:url value='/download/downloadMulTypeChk.do'/>";
			   if(confirm("선택한 내역을 저장합니다")){
		       		document.searchForm.submit();
			   }else{
				    return false;
			   }
		   }
	   }
	  /* 체크박스 전체선택, 전체해제 */
	   function fn_work_chk_all() {
		   
		   if( $("#chkAll").is(':checked') ){
             $("input[name=checkRow]").prop("checked", true);
           }else{
             $("input[name=checkRow]").prop("checked", false);
           }
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
		                <a class="nav-link" onClick="fn_chk_sentence()" id="custom-content-below-home-tab" data-toggle="pill" href="#custom-content-below-home" role="tab" aria-controls="custom-content-below-home" aria-selected="true">문장검사</a>
		              </li>
		              <li class="nav-item">
		                <a class="nav-link" onClick="fn_chk_file()" id="custom-content-below-profile-tab" data-toggle="pill" href="#custom-content-below-profile" role="tab" aria-controls="custom-content-below-profile" aria-selected="false">파일검사</a>
		              </li>
		              <li class="nav-item">
		                <a class="nav-link active" id="custom-content-below-messages-tab" data-toggle="pill" href="#custom-content-below-messages" role="tab" aria-controls="custom-content-below-messages" aria-selected="false">검사내역</a>
		              </li>
		    </ul>  
            <div class="card">
              <section class="content-header">
			      <div class="container-fluid">
			        <div class="row mb-2">
			          <div class="col-sm-4"></div>
			          <div id='loadingImg' class="col-sm-4"></div>
			          <div class="col-sm-2">
			              <button type="button" class="btn btn-block btn-outline-primary" onClick="fn_down_excel()">내려받기</button>
			          </div>
			          <div class="col-sm-2">
			              <button type="button" class="btn btn-block btn-outline-primary" onClick="fn_type_chk_del()">삭제</button>
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
        				<td align="center"><input type="checkbox" id="chkAll" name="chkAll" value="option1" onClick="fn_work_chk_all()"></th>
        				<td align="center">No</th>
        				<td align="center">상태</th>
        				<!-- <td align="center">작업명</th> -->
        				<td align="center">파일명</th>
        				<td align="center">검사항목</th>
        				<td align="center">문장수</th>
        				<td align="center">등록일시</th>
        				
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><input type="checkbox" id="checkRow" name="checkRow" value="${result.bd_idx}"></td>
            				<td align="center" class="listtd"><c:out value="${((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
            				<c:choose>
							    <c:when test="${result.bd_state=='완료'}">
							    	<td align="center" class="listtd"><button type="button" id="bt_${result.bd_idx}" value="${result.bd_state}" onClick="fn_work_spellList('${result.bd_idx}')" class="btn btn-block btn-outline-success btn-sm"><c:out value="${result.bd_state}"/></button></td>
							    </c:when>
							    <c:otherwise>
							       <td align="center" class="listtd"><button type="button" id="bt_${result.bd_idx}" value="${result.bd_state}" onClick="fn_type_chk_process('${result.bd_idx}','${result.bd_comment}')" class="btn btn-block btn-outline-danger btn-sm"><c:out value="${result.bd_state}"/></button></td>
							    </c:otherwise>
							</c:choose>
            				
            				<%-- <td align="left" class="listtd"><c:out value="${result.bd_title}"/></td> --%>
            				<td align="center" class="listtd"><c:out value="${result.bd_org_name}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_comment}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_cnt}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.bd_ins_dt}"/></td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		<form  id="searchForm" name="searchForm" method="post">
                	<input type="hidden" name="agent" value="${agent}"/>
                	<input type="hidden" name="searchKeyword" />
                	<input type="hidden" name="bd_idx" />
                	<input type="hidden" name="bd_chk" />
                	<input type="hidden" name="requestedFile" />
                </form>
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
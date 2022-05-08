<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
  
  
  
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!-- daterange picker -->
  <link rel="stylesheet" href="plugins/daterangepicker/daterangepicker.css">
  
  <!-- Font Awesome Icons -->
  <link rel="stylesheet" href="plugins/fontawesome-free/css/all.min.css">
    <!-- Bootstrap Color Picker -->
  <link rel="stylesheet" href="plugins/bootstrap-colorpicker/css/bootstrap-colorpicker.min.css">
  <!-- Tempusdominus Bbootstrap 4 -->
  <link rel="stylesheet" href="plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
   <!-- DataTables -->
  <link rel="stylesheet" href="plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/adminlte.min.css">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
  <!-- iCheck for checkboxes and radio inputs -->
  <link rel="stylesheet" href="plugins/icheck-bootstrap/icheck-bootstrap.min.css">
    <!-- Bootstrap4 Duallistbox -->
  <link rel="stylesheet" href="plugins/bootstrap4-duallistbox/bootstrap-duallistbox.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/adminlte.min.css">
  <!-- summernote -->
  <link rel="stylesheet" href="plugins/summernote/summernote-bs4.css">
  
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  
<script type="text/javaScript" language="javascript" defer="defer">
  		//검수상태
		function state_chk(wk_idx) {
		      var sel = document.getElementById('noun_list_'+wk_idx); //select
		      var val = sel.options[sel.selectedIndex].value;
		      
			var params = {
	        		 wk_idx: wk_idx
	                , noun_list  : val
	                , tb_name : 'tb_type_chk_${bd_idx}'
	        }
	        
	        $.ajax({
	            type : 'POST',            // HTTP method type(GET, POST) 형식이다.
	            url : '${pageContext.request.contextPath}/type_chk_state_ajax.do',      // 컨트롤러에서 대기중인 URL 주소이다.
	            data : params,          // Json 형식의 데이터이다.
	            dataType:'json',
	            success : function(res){ // 비동기통신의 성공일경우 success콜백으로 들어옵니다. 'res'는 응답받은 데이터이다.
	                // 응답코드 > 0000
	                if(res.code=='1111'){
	                	//alert(val+" 수정완료");
	                }else{
	                	alert("통신중 오류가 발생하였습니다.");
	                }
	            },
	            error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
	                alert("통신중 오류가 발생하였습니다.")
	            }
	        });
		}
		//수정안채택
  		function mod_chk(wk_idx) {
		      var sel = document.getElementById('mod_yn_'+wk_idx); //select
		      var val = sel.options[sel.selectedIndex].value;
		      var tr_id = document.getElementById('tr_'+wk_idx); //tr
			var params = {
	        		 wk_idx: wk_idx
	                , mod_yn  : val
	                , tb_name : 'tb_type_chk_${bd_idx}'
	        }
	        
	        $.ajax({
	            type : 'POST',            // HTTP method type(GET, POST) 형식이다.
	            url : '${pageContext.request.contextPath}/type_chk_mod_ajax.do',      // 컨트롤러에서 대기중인 URL 주소이다.
	            data : params,          // Json 형식의 데이터이다.
	            dataType:'json',
	            success : function(res){ // 비동기통신의 성공일경우 success콜백으로 들어옵니다. 'res'는 응답받은 데이터이다.
	                // 응답코드 > 0000
	                if(res.code=='1111'){
	                	if(val=='Y'){
	                		tr_id.style.backgroundColor = "#FFFFFF";
	                	}else{
	                		tr_id.style.backgroundColor = "#FCE6E0";
	                	}
	                }else{
	                	alert("통신중 오류가 발생하였습니다.");
	                }
	            },
	            error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
	                alert("통신중 오류가 발생하였습니다.")
	            }
	        });
		}
        function fn_chk_file() {
	   		document.updateForm.action = "<c:url value='/type_chk_file.do'/>";
	      	document.updateForm.submit();
	    }
        function fn_type_chk_list() {
	   		document.updateForm.action = "<c:url value='/type_chk_list.do'/>";
	      	document.updateForm.submit();
	    }
        function fn_chk_sentence() {
	   		document.updateForm.action = "<c:url value='/type_chk_main.do'/>";
	      	document.updateForm.submit();
	    }
        function fn_egov_test_ok() {
        	//alert("수정후 사전적용");
           	//document.updateForm.action = "<c:url value='exec_test.do'/>";
           	document.updateForm.submit();
           	LoadingWithMask();
        }
         function fn_export_noun() {
         	//alert("수정후 사전적용");
            	document.searchForm.action = "<c:url value='spell_chk.do'/>";
            	document.searchForm.submit();
            	LoadingWithMask();
         }
        /* pagination 페이지 링크 function */
        function fn_egov_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	//document.listForm.searchKeyword.value = document.searchForm.searchKeyword.value;
        	document.listForm.action = "<c:url value='/type_chk_detail.do'/>";
           	document.listForm.submit();
        }
        function fn_down_excel(){
        	window.open(encodeURI('${pageContext.request.contextPath}/download/downloadTypeChk.do?requestedFile=${bd_idx}'))
        }
        
        function LoadingWithMask() {
            //화면의 높이와 너비를 구합니다.
            var maskHeight = $(document).height();
            var maskWidth  = window.document.body.clientWidth;
             
            //화면에 출력할 마스크를 설정해줍니다.
            var mask       ="<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
            var loadingImg ='';
              
            loadingImg +="<img src='dist/img/Spinner.gif' display: block; margin: 0px auto;'/>";
         	
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
<body class="hold-transition sidebar-mini sidebar-collapse">
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
		                <a class="nav-link active" onClick="fn_type_chk_list()" id="custom-content-below-messages-tab" data-toggle="pill" href="#custom-content-below-messages" role="tab" aria-controls="custom-content-below-messages" aria-selected="false">검사내역</a>
		              </li>
		    </ul>  
            <div class="card">
              <div class="card-header">
              
              <div class="row mb-2">
                 
                 <div class="col-sm-3">
                 <ol class="breadcrumb float-sm-left">
                 	
                 	검사 문장 : ${paginationInfo.totalRecordCount} 오류율 : <font color="red"><b> ${sucRate}%</b></font>
                 	
                 </ol>
                 </div>
                 <div class="col-sm-7">
                 <form  id="updateForm" name="updateForm" method="post">
                 		<input type="hidden" name="agent" id="agent" value="${agent}">
                 </form>
                 
                 </div>
                 <div class="col-sm-2">
				       <!-- <a href="javascript:fn_down_excel()" class="btn btn-sm btn-warning float-left">검사결과 엑셀다운</a> -->
				       <button type="button" class="btn btn-block btn-outline-primary" onClick="fn_down_excel()">결과내려받기</button>
                 </div>
                 
                 
                 </div>
                 <div class="row text-center" style="width: 100%">
 
                    <div style="width: 50%; float:none; margin:0 auto" >
                            <ui:pagination paginationInfo = "${paginationInfo}" type="text" jsFunction="fn_egov_link_page" />
                     <div>
                 </div>
              </div>
              
              <!-- /.card-header -->
              <div class="card-body">
                 
                <table id="example1" class="table table-bordered table-hover"   summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<caption style="visibility:hidden">카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블</caption>
        			<thead>
        			<tr style="background-color:#DCDCDC">
        				<th align="center" class='th3'>No</th>
        				<th align="center">수정안<br>채택</th>
        				<th align="center">검수상태</th>
        				<th align="center">ID</th>
        				<th align="center">원 문장</th>
        				<th align="center">오류유형 
        				<button type="button" id="tool" class="note-btn btn btn-light btn-sm" tabindex="-1"><i class="note-icon-question"></i></button>
        				</th>
        				<th align="center">수정위치</th>
        				<th align="center">수정문장</th>
        				<!-- <td align="center">검사결과</th> -->
        				<th align="center">수정일시</th>
        			</tr>
        			</thead>
        			<tbody>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
        				<c:choose>
							    <c:when test="${result.mod_yn=='N'}">
							    	<tr id="tr_${result.wk_idx}" style="background-color:#FCE6E0">
							    </c:when>
							    <c:otherwise>
							        <tr id="tr_${result.wk_idx}" style="background-color:#FFFFFF">
							    </c:otherwise>
						</c:choose>
            			
            				<td align="center" class="listtd"><c:out value="${(paginationInfo.totalRecordCount-((searchVO.pageIndex-1) * searchVO.pageUnit + status.count))+1}"/></td>
            				<td align="center" class="listtd">
            				<select name='mod_yn_${result.wk_idx}' id='mod_yn_${result.wk_idx}' class="form-control select2" style="width: 100%;" onchange="mod_chk('${result.wk_idx}')">
			                    <option <c:if test="${result.mod_yn=='Y'}">selected</c:if>>Y</option>
			                    <option <c:if test="${result.mod_yn=='N'}">selected</c:if>>N</option>
			                 </select>
							</td>
							<td align="center" class="listtd">
							<select name='noun_list_${result.wk_idx}' id='noun_list_${result.wk_idx}' class="form-control select2" style="width: 100%;" onchange="state_chk('${result.wk_idx}')">
			                    <option <c:if test="${result.noun_list=='검수완료'}">selected</c:if>>검수완료</option>
			                    <option <c:if test="${result.noun_list=='반려1회'}">selected</c:if>>반려1회</option>
			                    <option <c:if test="${result.noun_list=='재작업1회'}">selected</c:if>>재작업1회</option>
			                    <option <c:if test="${result.noun_list=='반려2회'}">selected</c:if>>반려2회</option>
			                    <option <c:if test="${result.noun_list=='재작업2회'}">selected</c:if>>재작업2회</option>
			                    <option <c:if test="${result.noun_list=='반려3회'}">selected</c:if>>반려3회</option>
			                 </select>
			                 </td>
            				<td align="center" class="listtd"><c:out value="${result.uni_num}"/></a></td>
            				<c:choose>
							    <c:when test="${fn:length(result.quest) gt 50}">
							    	<td align="left" class="listtd" style="white-space:pre;" title="${result.quest}"><c:out value="${fn:substring(result.quest,0,50)}"/>...</td>
							    </c:when>
							    <c:otherwise>
							       <td align="left" class="listtd" style="white-space:pre;" title="${result.quest}"><c:out value="${result.quest}"/></td>
							    </c:otherwise>
							</c:choose>
							<td align="left" class="listtd" style="white-space:pre;" title="${result.sp_answer}"><c:out value="${result.sp_answer}"/></td>
							<c:choose>
							    <c:when test="${fn:length(result.mod_contents) gt 100}">
							    	<td align="left" class="listtd" title="${result.mod_contents}"><c:out value="${fn:substring(result.mod_contents,0,100)}" escapeXml="false"/>...</td>
							    </c:when>
							    <c:otherwise>
							       <td align="left" class="listtd" title="${result.mod_contents}"><c:out value="${result.mod_contents}" escapeXml="false"/></td>
							    </c:otherwise>
							</c:choose>
							<c:choose>
							    <c:when test="${fn:length(result.sp_quest) gt 50}">
							    	<td align="left" class="listtd" style="white-space:pre;" title="${result.sp_quest}"><c:out value="${fn:substring(result.sp_quest,0,50)}"/>...</td>
							    </c:when>
							    <c:otherwise>
							       <td align="left" class="listtd" style="white-space:pre;" title="${result.sp_quest}"><c:out value="${result.sp_quest}"/></td>
							    </c:otherwise>
							</c:choose>
            				
            				<%-- <td align="center" class="listtd">
            					<c:choose>
								    <c:when test="${result.mod_yn eq 'N'}">
								        <span class="badge badge-success">정상</span>
								    </c:when>
								    <c:otherwise>
								        <span class="badge badge-danger">수정</span>
								    </c:otherwise>
								</c:choose>
            				</td> --%>
            				<td align="center" class="listtd"><c:out value="${result.upd_date}"/></td>
            			</tr>
        			</c:forEach>
        			</tbody>
        		</table>
        		<form:form commandName="searchVO" id="listForm" name="listForm" method="post">
        		<input type="hidden" name="selectedId" />
        		<input type="hidden" name="agent" id="agent" value="${agent}">
        		<input type="hidden" name="bd_idx" id="bd_idx" value="${bd_idx}">
        		<input type="hidden" id="searchKeyword" name="searchKeyword">
			    <input type="hidden" name="searchCondition" id="searchCondition" value="1">
                <div class="row text-center" style="width: 100%">
 
                    <div style="width: 50%; float:none; margin:0 auto" >
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
<!-- Bootstrap Switch -->
<script src="plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>
<!-- page script -->

<script>
	$('#tool').tooltip({
	    title: 'ES : 문장 앞뒤 공백제거' +'<br />'+ 'DS : 중복공백검사'+'<br />'+ 'SD : 특수문자 제거'+'<br />'+ 'PU : 구두점(. ? !) 검사'+'<br />'+ 'CO : 띄어쓰기, 맞춤법',
	    html: true
	});
    $(function () {
	    //Initialize Select2 Elements
	    $("input[data-bootstrap-switch]").each(function(){
	        $(this).bootstrapSwitch('state', $(this).prop('checked'));
	      });
	
	})
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
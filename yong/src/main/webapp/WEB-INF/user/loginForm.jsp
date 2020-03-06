<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>Bootstrap, from Twitter</title>
    <!-- bootstrap CDN , 반응형 CSS style -->
    <jsp:include page="/WEB-INF/layout/header.jsp"></jsp:include>
</head>
<body>
    <!-- 상단 메뉴바 -->
    <jsp:include page="/WEB-INF/layout/top.jsp"></jsp:include>

    <div class="container-fluid">
      <div class="row-fluid">
          <!-- 왼쪽 사이드 메뉴바 -->
          <div class="float_sidebar" id="ctgList"></div>
          
        <div class="span9">
        
        	<div class="span3"></div>
        	
        	<div class="span6">
        	<form class="form-horizontal">
			  <div class="errorForm control-group">
			    <label class="control-label" for="inputId">아이디</label>
			    <div class="controls">
			      <input type="text" id="inputId">
			    </div>
			  </div>
			  <div class="errorForm control-group">
			    <label class="control-label" for="inputPassword">비밀번호</label>
			    <div class="controls">
			      <input type="password" id="inputPassword">
			    <span class="help-block" id="helpCheck" style="color: red; display: none">아이디 또는 패스워드를 확인해 주세요</span>
			    </div>
			  </div>
			  <div class="control-group">
			    <div class="controls">
			      <button type="button" class="btn" onclick="login()">로그인</button>
			      <button type="button" class="btn" onclick="location.href='/user/userReg'">회원가입</button>
			    </div>
			  </div>
			</form>
			</div>
			<div class="span3"></div>


        </div><!--/span-->
      </div><!--/row-->

      <hr>

      <jsp:include page="/WEB-INF/layout/footer.jsp"></jsp:include>
    </div><!--/.fluid-container-->
    
<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="/resources/js/bootstrap.js"></script>
<!-- 사이드 메뉴 스크롤에 따라 움직이는 스크립트 -->
<script src="/resources/js/sideResponsive.js"></script>
<script>

function login() {
	let id = $('#inputId').val();
	let pw = $('#inputPassword').val();
	
	if (id == "" || pw == "") {
		$('#helpCheck').show();
		$('.errorForm').addClass('error');
		return false;
	}
	
	$.ajax({
		url : '/user/userLogin',
		type : 'post',
		dataType : 'json',
		data : {
			userId : id, 
			userPwd : pw
		},
		success : function(data, status) {
			if (data.resultCode == 100) {
				alert(data.message);
				self.location = '/';
			} else if (data.resultCode == 2) {
				$('#helpCheck').show();
				$('.errorForm').addClass('error');
				return false;
			} else {
				self.location = '/';
			}
		}
		
	});
}

$(function() {
	$('#ctgList').load("/layout/left", function() {
		
	});
	
});
</script>

</body>
</html>
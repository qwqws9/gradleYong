<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <!-- bootstrap CDN , 반응형 CSS style -->
    <jsp:include page="/WEB-INF/layout/header.jsp"></jsp:include>
</head>
<body>
    <!-- 상단 메뉴바 -->
    <jsp:include page="/WEB-INF/layout/top.jsp"></jsp:include>
    <input type="hidden" id="mode" value="${mode }"/>
    <div class="container-fluid">
      <div class="row-fluid">
          <!-- 왼쪽 사이드 메뉴바 -->
          <div class="float_sidebar" id="ctgList"></div>
          
        <div class="span9">
        
        	<div class="span3"></div>
        	
        	<div class="span6">
        	<form class="form-horizontal" id="userForm" method="post">
			  <div class="control-group">
			    <label class="control-label" for="inputId">아이디</label>
			    <div class="controls">
			      <input type="text" id="inputId" name="userId" <c:if test="${mode eq 'update' }">readonly</c:if> value="<c:out value='${user.userId }'/>"> 
			      <span class="help-block" id="helpCheckId" style="color: red; display: none">중복된 아이디 입니다.</span>
			    </div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="inputPassword">비밀번호</label>
			    <div class="controls">
			      <input type="password" id="inputPassword" name="userPwd">
			    </div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="inputPassword2">비밀번호 확인</label>
			    <div class="controls">
			      <input type="password" id="inputPassword2">
			    <span class="help-block" id="helpCheckPw" style="color: red; display: none">비밀번호가 일치하지 않습니다.</span>
			    </div>
			  </div>
			  
			  <div class="control-group">
			    <label class="control-label" for="nickname">닉네임</label>
			    <div class="controls">
			      <input type="text" id="nickname" name="userNick" value="<c:out value='${user.userNick }'/>">
			    </div>
			  </div>
			  <div class="control-group">
			    <div class="controls">
			      <c:if test="${mode eq 'update' }"><button type="button" class="btn" onclick="signup()">수정</button></c:if>
			      <c:if test="${mode ne 'update' }"><button type="button" class="btn" onclick="signup()">회원가입</button></c:if>
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

function signup() {
	let id = $('#inputId').val();
	let pw = $('#inputPassword').val();
	let pw2 = $('#inputPassword2').val();
	let nickname = $('#nickname').val();
	
	
	if (id == "") {
		$('#inputId').focus();
		return false;
	}
	if (pw == "" || pw2 == "") {
		$('#inputPassword').focus();
		return false;
	}
	if ($('#inputPassword').val() != $('#inputPassword2').val()) {
		$('#helpCheckPw').show();
		return false;
	}
	if (nickname == "") {
		$('#nickname').focus();
		return false;
	}
	
	var action = $('#mode').val() == 'update' ? "/user/userUpdate" : "/user/userSave"
	$('#userForm').attr('action', action).submit();
}

$(function() {
	$('#ctgList').load("/layout/left", function() {
		
	});
	
	$('#inputPassword').on('keyup', function() {
		if ($('#inputPassword').val() != $('#inputPassword2').val()) {
			$('#helpCheckPw').show();
		} else {
			$('#helpCheckPw').hide();
		}
	})
	$('#inputPassword2').on('keyup', function() {
		if ($('#inputPassword').val() != $('#inputPassword2').val()) {
			$('#helpCheckPw').show();
		} else {
			$('#helpCheckPw').hide();
		}
	})
	
});
</script>

</body>
</html>
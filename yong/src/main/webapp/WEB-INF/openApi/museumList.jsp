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
    
    <div class="container-fluid">
        <div class="row-fluid">
            <!-- 왼쪽 사이드 메뉴바 -->
            <div class="float_sidebar" id="ctgList"></div>
          
            <div class="span9">
                <div class="span12" id="museumList"></div>
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
var pageNo = 1;
var stop = true;

//게시판 무한스크롤..
function museumListAdd() {
	$.ajax({
	    url         : '/openApi/museumData',
	    type        : 'POST',
	    data        : {
	        pageNo : pageNo
	    },
	    dataType    : 'html',
	    success        : function(data, status) {
	        $('#museumList').append(data);
	        stop = true;
	    },
	    error        : function(request, status, error) {
	        console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
	    }
	});
}

$(function() {
    $('#ctgList').load("/layout/left", function() {
    	museumListAdd();
    });
    
    $(window).scroll(function(){
        let $window = $(this);
        let scrollTop = Math.ceil($window.scrollTop());
        let windowHeight = Math.ceil($window.height());
        let documentHeight = Math.ceil($(document).height());
        console.log(scrollTop);
        console.log(documentHeight - windowHeight);
        // scrollbar의 thumb가 바닥 전 700px까지 도달 하면 리스트를 가져온다.
        if (stop && (scrollTop + windowHeight + 700 > documentHeight) ) {
        	stop = false;
        	pageNo++;
        	console.log(pageNo);
            museumListAdd();
        }
    });
    
});
</script>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <%--           <jsp:include page="/WEB-INF/layout/left.jsp"></jsp:include> --%>
        <div class="float_sidebar" id="ctgList"></div>

        <div class="span9" id="boardList">
            <div class="hero-unit">
                <h1>Hi..</h1>
                <p>This is a template for a simple marketing or informational website. It includes a large callout called the hero unit and three supporting pieces of content. Use it as a starting point to create something more unique.</p>
                <p><a href="#" class="btn btn-primary btn-large">Learn more &raquo;</a></p>
            </div>

            <c:forEach items="${list }" var="board" varStatus="status">
                <div class="row-fluid">
                    <div class="span4">
                        <div class="card">
                        <a href="/board/getBoard/${board.boardSeq }"><img class="card-img-top" src="/resources/img/upload/${board.filePath }" onerror="this.src='/resources/img/unnamed.gif'"></a>
                        </div>
                    </div><!--/span-->
                    <div class="span8">
                        <div class="card-body">
                            <h4 class="card-title">
                            <a href="/board/getBoard/${board.boardSeq }"><c:out value="${board.boardTitle }"/></a>
                            </h4>
                            <p class="card-text">${board.textHtml }</p>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">${board.ctgMstName } &lt; ${board.ctgName }</small>
                        </div>
                    </div>
                </div>
                <br>
            </c:forEach>
        </div>
    </div>
    <input type="hidden" id="boardCount" value="${boardCount }"/>
    <input type="hidden" id="ctgSeq" value="${ctgSeq }"/>
    <input type="hidden" id="searchKeyword" value="${board.searchKeyword }"/>
    <hr>

    <jsp:include page="/WEB-INF/layout/footer.jsp"></jsp:include>
</div><!--/.fluid-container-->

<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="/resources/js/bootstrap.js"></script>
<!-- 사이드 메뉴 스크롤에 따라 움직이는 스크립트 -->
<script src="/resources/js/sideResponsive.js"></script>
<script>
var startNum = 0;

// 게시판 무한스크롤..
function boardListAdd() {
    
    
    $.ajax({
        url            : '/board/boardListAddForm',
        type        : 'POST',
        data        : {
            startNum : startNum,
            ctgSeq : $('#ctgSeq').val(),
            searchKeyword : $('#searchKeyword').val()
        },
        dataType    : 'html',
        success        : function(data, status) {
            $('#boardList').append(data);
        },
        error        : function(request, status, error) {
            console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
        }
    });
}

$(function() {
    $('#ctgList').load("/layout/left", function() {
    	if ($('#searchKeyword').val() != '') {
            $('#boardSearchKeyword').val($('#searchKeyword').val());
        }
    });
    
     $(window).scroll(function(){
         let $window = $(this);
         let scrollTop = $window.scrollTop();
         let windowHeight = $window.height();
         let documentHeight = $(document).height();
         
         // scrollbar의 thumb가 바닥 전 500px까지 도달 하면 리스트를 가져온다.
         if((startNum == 0 || (startNum * 2) <= $('#boardCount').val())  && scrollTop + windowHeight + 500 > documentHeight ) {
             startNum += 10;
             boardListAdd();
         }
     });
     
     
})
</script>
</body>
</html>
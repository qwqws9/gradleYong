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
                <div class="span12">
                    <input type="hidden" id="boardSeq" value="${board.boardSeq }"/>
                    <h3><c:out value="${board.boardTitle }"/></h3>
                    <small>
                        <fmt:parseDate value="${board.boardRegDt}" var="boardRegDt" pattern="yyyyMMddHHmmss"/>
                        <fmt:formatDate value="${boardRegDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </small>
                    <c:if test="${userInfo.userRole eq 'admin' }">
	                    <div class="control-group pull-right">
	                        <div class="controls">
	                            <button type="button" class="btn" onclick="location.href='/board/boardMod?boardSeq=${board.boardSeq}'">수정</button>
	                            <button type="button" class="btn" onclick="boardDelete()">삭제</button>
	                        </div>
	                    </div>
                	</c:if>
	                <hr>
	                    ${board.boardContent }
	                <hr>
	                <div id="commentList">
<%-- 	        			<jsp:include page="/WEB-INF/comment/commentList.jsp"></jsp:include> --%>
	        			<script src="https://utteranc.es/client.js"
						        repo="qwqws9/blog-comments"
						        issue-term="pathname"
						        theme="github-light"
						        crossorigin="anonymous"
						        async>
						</script>
	        		</div>
        		</div>
            </div><!--/span-->
      </div><!--/row-->

      <hr>

      <jsp:include page="/WEB-INF/layout/footer.jsp"></jsp:include>
    </div><!--/.fluid-container-->
    
<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="/resources/js/bootstrap.js"></script>
<!-- 사이드 메뉴 스크롤에 따라 움직이는 스크립트 -->
<script src="/resources/js/sideResponsive.js"></script>
<script src="/resources/js/lib/smarteditor/js/service/HuskyEZCreator.js"></script>
<script>

function parentFn(imgPathList) {
    console.log(imgPathList);
    var imgTag = '<img src="/resources/img/upload/';
    var html;
    $.each(imgPathList, function(index, item) {
        html = [];
        html.push(imgTag);
        html.push(item);
        html.push('"/>');
        oEditors.getById["boardContent"].exec("PASTE_HTML", [html.join('')]);
    })
}

function callPop() {
    var tempSeq = $('#tempSeq').val();
    window.open('/common/popup/uploadFilePopup?tempSeq='+tempSeq, 'uploader', 'width=500, height=400');
}

function boardWrite() {
    let title = $('#boardTitle').val();
    
    
    if (title == "") {
        $('#boardTitle').focus();
        return false;
    }
    
    oEditors.getById["boardContent"].exec("UPDATE_CONTENTS_FIELD", []);
    
    var data = $('#boardContent').val();
    var text = data.replace(/[<][^>]*[>]/gi, "");
    $('#textHtml').val(text);
    
    var action = $('#mode').val() == 'update' ? "/board/boardUpdate" : "/board/boardSave"
    $('#boardForm').attr('action', action).submit();
}

$(function() {
    $('#ctgList').load("/layout/left", function() {
        
    });
    
    $('.utterances').css('margin-left',0);
});
</script>

</body>
</html>
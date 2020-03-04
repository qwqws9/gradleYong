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
          <jsp:include page="/WEB-INF/layout/left.jsp"></jsp:include>
          
        <div class="span9">
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
                <p class="card-text"><c:out value="${board.textHtml }"/>
              </div>
              <div class="card-footer">
                <small class="text-muted">&#9733; &#9733; &#9733; &#9733; &#9733;</small>
              </div>
            </div>
          </div>
          <br>
          </c:forEach>
</div>
</div>
      <hr>

      <footer>
        <p>&copy; Company 2013</p>
      </footer>
    </div><!--/.fluid-container-->
    
<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="/resources/js/bootstrap.js"></script>
<!-- 사이드 메뉴 스크롤에 따라 움직이는 스크립트 -->
<script src="/resources/js/sideResponsive.js"></script>
<script>
$(function() {
	
})
</script>

</body>
</html>
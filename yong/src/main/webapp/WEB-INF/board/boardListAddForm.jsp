<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
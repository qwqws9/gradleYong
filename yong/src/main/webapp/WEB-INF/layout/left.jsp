<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="span3">
    <div class="well sidebar-nav">
        <ul class="nav nav-list">
                <li><a href="/">전체글보기 (${boardCount })</a></li>
                <c:forEach items="${ctgList }" var="ctgMst" varStatus="status">
                    <li class="nav-header" style="font-size: 16px;">${ctgMst.ctgName }</li>
                    <c:forEach items="${ctgMst.ctgDtoList }" var="ctg" varStatus="childStatus">
                        <li style="font-size: 13px;"><a href="/board/boardList?ctgSeq=${ctg.ctgSeq }">${ctg.ctgName } (${ctg.ctgCount })</a></li>
                    </c:forEach>
                </c:forEach>
        </ul>
    </div><!--/.well -->
    <input type="text" id="boardSearchKeyword" placeholder="검색..." value="${searchKeyword }"/>
</div>

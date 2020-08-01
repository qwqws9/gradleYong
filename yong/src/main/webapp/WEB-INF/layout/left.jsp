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
    <ul class="nav nav-list">
	    <li>
	    	<a href="/maskInfo2" class="btn btn-large btn-success">로컬 테스트용</a>
	        <hr>
	    </li>
	    <li>
	    	<a href="/maskAer" class="btn btn-large btn-warning">아에르 9:45</a>
	    	<hr>
	    </li>
	    <li>
	    	<a href="/mask5" class="btn btn-large btn-danger">아이리스 11:00</a>
	    	<hr>
	    </li>
	    <li>
	    	<a href="/mask" class="btn btn-large btn-success">금아..</a>
	        <hr>
	    </li>
<!-- 	    <li> -->
<!-- 	    	<a href="/mask4" class="btn btn-large btn-info">뉴네</a> -->
<!-- 	    	<hr> -->
<!-- 	    </li> -->
<!-- 	    <li> -->
<!-- 	    	<a href="/mask3" class="btn btn-large btn-info">걸리버 인쇄 13:00</a> -->
<!-- 	    	<hr> -->
<!-- 	    </li> -->
	    <li>
	    	<a href="/maskDrpuri1" class="btn btn-large btn-danger">닥터퓨리 번들</a>
	    	<hr>
	    </li>
	    <li>
	    	<a href="/maskDrpuri2" class="btn btn-large btn-danger">닥터퓨리 낱개</a>
	    </li>
    </ul>
    <div>
        <button type="button" style="border: 0; outline: 0; margin-top: 300px; margin-left: 250px;" id="goTop"><i class="icon-eject"></i>Top</button>
    </div>
</div>

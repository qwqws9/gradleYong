<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<div class="well sidebar-nav">
		<ul class="nav nav-list">
				<c:forEach items="${ctgAllList }" var="ctgMst" varStatus="status">
					<li class="nav-header"><a href="#" class="ctg" onclick="ctgInfo(${status.index})">${ctgMst.ctgName } [전시 : ${ctgMst.dispYn } , 순서 : ${ctgMst.dispNo }]</a></li>
					<c:forEach items="${ctgMst.ctgDtoList }" var="ctg" varStatus="childStatus">
						<li style="font-size: 11px;"><a href="#" onclick="ctgInfo(${status.index},${childStatus.index })">&nbsp;&nbsp;&nbsp;${ctg.ctgName } [전시 : ${ctg.dispYn } , 순서 : ${ctg.dispNo }]</a></li>
					</c:forEach>
				</c:forEach>
		</ul>
	</div><!--/.well -->

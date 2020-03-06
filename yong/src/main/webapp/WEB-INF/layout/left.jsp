<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="span3">
<div class="well sidebar-nav">
		<ul class="nav nav-list">
				<c:forEach items="${ctgList }" var="ctgMst" varStatus="status">
					<li class="nav-header">${ctgMst.ctgName }</li>
					<c:forEach items="${ctgMst.ctgDtoList }" var="ctg" varStatus="childStatus">
						<li><a href="#">${ctg.ctgName }</a></li>
					</c:forEach>
				</c:forEach>
		</ul>
	</div><!--/.well -->
	</div>

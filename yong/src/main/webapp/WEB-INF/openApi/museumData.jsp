<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:forEach items="${list }" var="museum" varStatus="status">
	${status.index % 3 == 0 ? '<div class="row-fluid">' : '' }
		<div class="span4">
			<h4>${museum.name }</h4>
			<a href="#"><img src="http://${museum.imgUri }" width="350px;" height="300px;" onerror="this.src='/resources/img/unnamed.gif'"></a>
			<br>
		</div><!--/span-->
	${status.count % 3 == 0 ? '</div>' : '' }
<%--                 		${museum.museumName } --%>
<%--                 		${museum.museumCode } --%>
<%--                 		${museum.id } --%>
<%--                 		${museum.name } --%>
</c:forEach>
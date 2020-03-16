<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${!empty mst }">
    <!-- ctgMst 카테고리 -->
    <select name="ctgMstSeq" id="ctgMstSeq">
        <option value="">선택</option>
        <c:forEach items="${ctgList }" var="ctg" varStatus="status">
            <option value="${ctg.ctgMstSeq }">${ctg.ctgName }</option>
        </c:forEach>
    </select>
</c:if>

<c:if test="${empty mst }">
    <!-- ctgMst 하위 카테고리 -->
    <select name="ctgSeq" id="ctgSeq">
        <option value="">선택</option>
        <c:forEach items="${ctgList }" var="ctg" varStatus="status">
            <option value="${ctg.ctgSeq }">${ctg.ctgName }</option>
        </c:forEach>
    </select>
</c:if>


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/">Yong</a>
            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    <c:if test="${empty userInfo }">
                        <a href="/user/userLoginForm" class="navbar-link">Login...</a>
                    </c:if>
                    <c:if test="${!empty userInfo }">
                        <a href="/user/userMod?userSeq=${userInfo.userSeq }" class="navbar-link">${userInfo.userNick}</a>
                    </c:if>
                </p>
                <ul class="nav" role="navigation">
                    <li><a href="#">Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#contact">Contact</a></li>
                    <li class="dropdown">
                        <a id="adminDropMenu"  role="button" class="dropdown-toggle" data-toggle="dropdown">관리자 메뉴 <b class="caret"></b></a>
                        <ul class="dropdown-menu" role="menu" aria-labelledby="adminDropMenu">
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="/board/boardReg">게시물 작성</a></li>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="/board/boardList">목록</a></li>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="/ctg/ctgInput">카테고리 관리</a></li>
                            <li role="presentation" class="divider"></li>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Separated link</a></li>
                        </ul>
                    </li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>
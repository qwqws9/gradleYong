<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:forEach items="${commentList }" var="comment" varStatus="status">
	<div class="well well-small">
	<p>
		<i class="icon-user"></i>
		${comment.writer }
		<small class="pull-right">
			<fmt:parseDate value="${comment.regDt}" var="commentRegDt" pattern="yyyyMMddHHmmss"/>
            <fmt:formatDate value="${commentRegDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</small>
	</p>
	
	<div class="btn-group pull-right">
                <button class="btn btn-mini btn-inverse dropdown-toggle" data-toggle="dropdown">Action <span class="caret"></span></button>
                <ul class="dropdown-menu">
                  <li><a href="#commentReply" role="button" data-toggle="modal">Reply</a></li>
                  <li class="divider"></li>
                  <li><a href="#">수정하기</a></li>
                  <li><a href="#">삭제하기</a></li>
                </ul>
              </div>
              
              <div class="modal hide fade" id="commentReply" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				  <div class="modal-header">
				    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				    <h3>Reply</h3>
				  </div>
				  <div class="modal-body">
				    <p>${comment.content }</p>
				    <form>
					    <input type="text" name="writer" placeholder="작성자.."/> &nbsp; 
						<input type="password" name="pwd" placeholder="비밀번호.."/>
						<textarea rows="5" name="content" style="width: 83%; resize: none;" placeholder="내용"></textarea>
						<input type="button" value="작성" onclick="addReply(${status.index});" class="btn btn-inverse" style="height: 110px; margin-bottom: 10px;"/>
					</form>
				  </div>
				  <div class="modal-footer">
				    <a href="#" class="btn">Close</a>
				    <a href="#" class="btn btn-primary" onclick="addReply(${status.index})">Save changes</a>
				  </div>
</div>

	<p>${comment.content }</p>
	
	<c:forEach items="${commentChildList }" var="child" varStatus="childStatus">
	<c:if test="${childStatus.index == 0 }"><hr></c:if>
	<c:if test="${comment.parentSeq == child.parentSeq}">
		<div style="padding-left: 20px;">
		<p><i class="icon-chevron-right"></i> ${child.writer }
		
		<small class="pull-right">
		    <fmt:parseDate value="${child.regDt}" var="commentChildRegDt" pattern="yyyyMMddHHmmss"/>
            <fmt:formatDate value="${commentChildRegDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</small> 수정/삭제</p>
		 ${child.content }
		 		</div>
		</c:if>
		</c:forEach>
	</div>
	
</c:forEach>
	<input type="text" id="writer" placeholder="작성자.."/> &nbsp; 
	<input type="password" id="pwd" placeholder="비밀번호.."/>
	<textarea rows="5" id="content" style="width: 50%; resize: none;" placeholder="내용"></textarea>
	<input type="button" value="작성" onclick="addComment();" class="btn btn-inverse" style="height: 110px; margin-bottom: 10px;"/>
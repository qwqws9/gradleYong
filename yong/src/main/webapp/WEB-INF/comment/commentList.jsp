<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<div class="span10" id="commentForm">
</div>
<div class="span2">
	
</div>

<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
var boardSeq = ${boardSeq}

// 댓글 조회
function getComment() {
	if (boardSeq == '') {
		alert('잘못된 접근입니다.')
		return false;
	}
	
	$('#commentForm').load('/comment/commentList',{"boardSeq" : boardSeq}, function() {
		
	});
// 	$.ajax({
//         url         : '/comment/commentList',
//         type        : 'POST',
//         data        : {
//             boardSeq : boardSeq
//         },
//         dataType    : 'json',
//         success        : function(data, status) {
//         	console.log(data);
//         },
//         error        : function(request, status, error) {
//             console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
//         }
//     });
}

function addReply(data) {
	console.log(data);
}

// 댓글 추가
function addComment() {
	var writer = $('#writer').val().trim();
	var pwd = $('#pwd').val().trim();
	var content = $('#content').val().trim();
	
	if (writer == '') {
		$('#writer').focus();
		return false;
	}
	if (pwd == '') {
		$('#pwd').focus();
		return false;
	}
	if (content == '') {
		$('#content').focus();
		return false;
	}
	if (boardSeq == '') {
		alert('잘못된 접근입니다.')
		return false;
	}
	
	$.ajax({
        url         : '/comment/commentSave',
        type        : 'POST',
        data        : {
            writer : writer,
            pwd : pwd,
            content : content,
            boardSeq : boardSeq
        },
        dataType    : 'json',
        success        : function(data, status) {
        	getComment();
        },
        error        : function(request, status, error) {
            console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
        }
    });
}

$(function() {
	getComment();
	
});
</script>

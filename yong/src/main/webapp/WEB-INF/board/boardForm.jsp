<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <!-- bootstrap CDN , 반응형 CSS style -->
    <jsp:include page="/WEB-INF/layout/header.jsp"></jsp:include>
</head>
<body>
    <!-- 상단 메뉴바 -->
    <jsp:include page="/WEB-INF/layout/top.jsp"></jsp:include>
    <input type="hidden" id="mode" value="${mode }"/>
    
    <div class="container-fluid">
        <div class="row-fluid">
            <!-- 왼쪽 사이드 메뉴바 -->
            <div class="float_sidebar" id="ctgList"></div>
            <div class="span9">
                <div class="span12">
                    <form class="form" id="boardForm" method="post">
                        <input type="hidden" id="tempSeq" name="tempSeq" value=""/>
                        <input type="hidden" id="textHtml" name="textHtml" value=""/>
                        <input type="hidden" id="boardSeq" name="boardSeq" value="${board.boardSeq }"/>
                        <label class="control-label">카테고리</label>
                        <div class="controls" id="ctgMst"></div>
                        <label class="control-label" for="boardTitle">제목</label>
                        <div class="controls">
                            <input type="text" id="boardTitle" name="boardTitle" value="<c:out value='${board.boardTitle }'/>"> 
                        </div>
                        <label class="control-label" for="boardContent">내용</label>
                        <a href="#" onclick="callPop()">이미지 업로드</a>
                        <div class="controls">
                            <textarea id="boardContent" name="boardContent" class="editor" style="min-width: 700px; min-height: 700px;">${board.boardContent }</textarea>
                        </div>
                    </form>
                    <div class="control-group">
                        <div class="controls">
                            <button type="button" class="btn" onclick="boardWrite()">작성</button>
                        </div>
                    </div>
                </div>
            </div><!--/span-->
        </div><!--/row-->
        <hr>
        <jsp:include page="/WEB-INF/layout/footer.jsp"></jsp:include>
    </div><!--/.fluid-container-->
    
<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="/resources/js/bootstrap.js"></script>
<!-- 사이드 메뉴 스크롤에 따라 움직이는 스크립트 -->
<script src="/resources/js/sideResponsive.js"></script>
<script src="/resources/js/lib/smarteditor/js/service/HuskyEZCreator.js"></script>
<script>


//Smart Editor 설정 정보
var oEditors = [];
nhn.husky.EZCreator.createInIFrame({
    oAppRef: oEditors,
    elPlaceHolder: "boardContent",    // textarea id
    sSkinURI: "/resources/js/lib/smarteditor/SmartEditor2Skin.html",
    fCreator: "createSEditor2",
    fOnAppLoad: function() {
        
    }
});

function parentFn(imgPathList) {
    console.log(imgPathList);
    var imgTag = '<img src="/resources/img/upload/';
    var html;
    $.each(imgPathList, function(index, item) {
        html = [];
        html.push(imgTag);
        html.push(item);
        html.push('"/>');
        console.log(html);
        oEditors.getById["boardContent"].exec("PASTE_HTML", [html.join('')]);
    })
}

function callPop() {
    var tempSeq = $('#tempSeq').val();
    window.open('/common/popup/uploadFilePopup?tempSeq='+tempSeq, 'uploader', 'width=500, height=400');
}

// 게시물 등록
function boardWrite() {
    let title = $('#boardTitle').val();
    let ctgMstSeq = $('#ctgMstSeq').val();
    let ctgSeq = $('#ctgSeq').val();
    
    if (ctgMstSeq == "") {
        alert('카테고리 선택');
        return false;
    }

    if (ctgSeq == "") {
        alert('하위 카테고리 선택');
        return false;
    }

    if (title == "") {
        $('#boardTitle').focus();
        return false;
    }
    
    oEditors.getById["boardContent"].exec("UPDATE_CONTENTS_FIELD", []);
    
    var data = $('#boardContent').val();
    var text = data.replace(/[<][^>]*[>]/gi, "");
    $('#textHtml').val(text);
    
    var action = $('#mode').val() == 'update' ? "/board/boardUpdate" : "/board/boardSave"
    $('#boardForm').attr('action', action).submit();
}

$(function() {
    $('#tempSeq').val(new Date().getTime());
    $('#ctgList').load("/layout/left", function() {
        
    });
    
    // 카테고리 항목 노출
    $('#ctgMst').load("/ctg/selectCtgMstList", function() {
        $('#ctgMstSeq').on('change', function() {
            console.log($(this).val());
            if ($(this).val() == '') { $('#ctg').remove(); return false; }
            $('#ctgMst').append('<div id="ctg"></div>')
            $('#ctg').load("/ctg/selectCtgList",{"ctgMstSeq" : $(this).val()}, function() {
                
            });
        });
    });
});
</script>

</body>
</html>
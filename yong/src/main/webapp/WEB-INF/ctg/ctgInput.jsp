<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bootstrap, from Twitter</title>
    <!-- bootstrap CDN , 반응형 CSS style -->
    <jsp:include page="/WEB-INF/layout/header.jsp"></jsp:include>
</head>
<body>
    <!-- 상단 메뉴바 -->
    <jsp:include page="/WEB-INF/layout/top.jsp"></jsp:include>
    <div class="container-fluid">
        <div class="row-fluid">
        <!-- 왼쪽 사이드 메뉴바 -->
            <div id="ctgList"></div>
            <div class="span9">
            <!-- 카테고리 업데이트 리스트 -->
                <div class="span4" id="ctgUpdateList"></div>
                <div class="span8">
                    <form class="form-horizontal">
                        <div class="control-group">
                            <label class="control-label" for="dispYn">생성 정보</label>
                            <div class="controls">
                                <label style="display: inline;"><input type="radio" name="loc" value="update">&nbsp;수정</label>&nbsp;&nbsp;&nbsp;
                                <label style="display: inline;"><input type="radio" name="loc" value="parent">&nbsp;새 부모 카테고리</label>&nbsp;&nbsp;&nbsp;
                                <label style="display:inline;"><input type="radio" name="loc" value="child">&nbsp;선택된 부모의 자식 생성</label>
                            </div>
                        </div>
                        <div class="control-group" id="parent">
                            <label class="control-label" for="ctgMstSeq">부모 카테고리 번호</label>
                            <div class="controls">
                                <input type="text" id="ctgMstSeq" name="ctgMstSeq" readonly>
                            </div>
                        </div>
                        <div class="control-group" id="child">
                            <label class="control-label" for="ctgSeq">자식 카테고리 번호</label>
                            <div class="controls">
                                <input type="text" id="ctgSeq" name="ctgSeq" readonly>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="ctgName">카테고리 명</label>
                            <div class="controls">
                                <input type="text" id="ctgName" name="ctgName">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="dispNo">전시 순서</label>
                            <div class="controls">
                                <input type="text" id="dispNo" name="dispNo">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="dispYn">전시 여부</label>
                            <div class="controls" id="loc">
                                <label style="display: inline;"><input type="radio" name="dispYn" value="Y" class="dispY">&nbsp;전시</label>&nbsp;&nbsp;&nbsp;
                                <label style="display:inline;"><input type="radio" name="dispYn" value="N" class="dispN">&nbsp;미전시</label>
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <button type="button" class="btn" onclick="save()">수정</button>
                                <button type="button" class="btn" onclick="location.href='/user/userReg'">삭제</button>
                            </div>
                        </div>
                    </form>
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
<script>
var ctgJson;

// 카테고리 선택시 form에 전달
function ctgInfo(parent,child) {
    var data = ctgJson[parent];
    
    if (typeof child == "undefined") {
        $('#ctgSeq').val("");
        $('#ctgMstSeq').val(data.ctgMstSeq);
        $('#ctgName').val(data.ctgName);
        $('#dispNo').val(data.dispNo);
        if (data.dispYn == 'Y') {
            $('.dispY').prop('checked', true);
            $('.dispN').prop('checked', false);
        } else {
            $('.dispN').prop('checked', true);
            $('.dispY').prop('checked', false);
        }
    } else {
        data = data.ctgDtoList[child];
        $('#ctgMstSeq').val(data.ctgMstSeq);
        $('#ctgSeq').val(data.ctgSeq);
        $('#ctgName').val(data.ctgName);
        $('#dispNo').val(data.dispNo);
        if (data.dispYn == 'Y') {
            $('.dispY').prop('checked', true);
            $('.dispN').prop('checked', false);
        } else {
            $('.dispN').prop('checked', true);
            $('.dispY').prop('checked', false);
        }
    }
}

function ctgJson(obj) {
    ctgJson = obj;
}

function save() {
    
    if (!$('input[name="loc"]').is(':checked')) {
        alert('생성 정보를 선택해 주세요.');
        return false;
    }
    
    if ($('#ctgName').val() == '') {
        $('#ctgName').focus();
        return false;
    }
    
    if ($('#dispNo').val() == '') {
        $('#dispNo').focus();
        return false;
    }
    
    if (!$('input[name="dispYn"]').is(':checked')) {
        alert('전시 여부를 선택해 주세요.');
        return false;
    }
    
    var formData = $($('form')[0]).serialize();
    
    $.ajax({
        url            : '/ctg/ctgSave',
        type        : 'POST',
        data        : formData,
        dataType    : 'json',
        success        : function(data, status){
            if (data.resultCode == 1) {
                $('#ctgList').children().remove();
                ctgListJson()
            }
        },
        error        : function(request, status, error){
            console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
        }
    });
}

function ctgListJson() {
    
    $.ajax({
        url            : '/ctg/ctgListJson',
        type        : 'POST',
        dataType    : 'json',
        success        : function(data, status){
            if (data.resultCode == 1) {
                ctgJson = JSON.parse(data.infoMap.ctgListJson);
                ctgList();
            }
        },
        error        : function(request, status, error){
            console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
        }
    });
}

function ctgList() {
    $('#ctgUpdateList').load("/ctg/ctgList", function(obj) {
    });
    $('#ctgList').load("/layout/left", function() {
    });
}

$(function() {
    
    ctgListJson();

});
</script>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="fileForm" name="fileForm" method="post" action="/common/uploadFile" enctype="multipart/form-data">
	<input type="file" id="files" name="files" multiple="multiple">
	<button type="button" onclick="fileUpload()">업로드</button>
</form>
<input type="hidden" id="tempSeq" value="0"/>
<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="/resources/js/bootstrap.js"></script>
<script type="text/javascript">
function getParam(sname) {
    var params = location.search.substr(location.search.indexOf("?") + 1);
    var sval = "";
    params = params.split("&");

    for (var i = 0; i < params.length; i++) {
        temp = params[i].split("=");
        if ([temp[0]] == sname) { sval = temp[1]; }
    }

    return sval;

}

function fileUpload() {
	var tempSeq = $('#tempSeq').val();
	
	if(tempSeq == '' || tempSeq == 0) {
		self.close();
		return false;
	}
	
	var formData = new FormData($('form')[0]);
	
	$.ajax({
		url			: '/common/uploadFile/'+tempSeq,
		type		: 'POST',
		enctype		: 'multipart/form-data',
		data		: formData,
		dataType	: 'json',
		contentType	: false,
		processData	: false,
		cache		: false,
		timeout		: 600000,
		success		: function(data, status){
			if(data.resultCode == 1 ) {
				opener.parent.parentFn(data.infoMap.list);
			} else {
				alert(data.message);
			}
			self.close();
		},
		error		: function(request, status, error){
			console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
	    }
	});
}

$(function() {
	console.log(getParam('tempSeq'));
	$('#tempSeq').val(getParam('tempSeq'));
})
</script>
</body>
</html>
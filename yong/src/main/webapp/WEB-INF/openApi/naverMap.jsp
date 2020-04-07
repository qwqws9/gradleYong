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
    <script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${clientId }"></script>
    <script type="text/javascript" src="/resources/js/MarkerClustering.js"></script>
</head>
<body>
    <!-- 상단 메뉴바 -->
    <jsp:include page="/WEB-INF/layout/top.jsp"></jsp:include>
    
    <div class="container-fluid">
        <div class="row-fluid">
            <!-- 왼쪽 사이드 메뉴바 -->
            <div class="float_sidebar" id="ctgList"></div>
          
            <div class="span9">
                <div class="span12">
                	<div id="map" style="width:100%;height:600px;"></div>
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
$(function() {
	$.ajax({
	    url         : '/openApi/chargeMap',
	    type        : 'POST',
	    dataType    : 'json',
	    success        : function(data, status) {
	        search(data.infoMap.list);
	    },
	    error        : function(request, status, error) {
	        console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
	    }
	});
})


var HOME_PATH = window.HOME_PATH || '.';

var markers = [];
var infoWindows = [];
var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.3595704, 127.105399),
    zoom: 12
});

function search(chargeList) {
	
	var bounds = map.getBounds(),
	    southWest = bounds.getSW(),
	    northEast = bounds.getNE(),
	    lngSpan = northEast.lng() - southWest.lng(),
	    latSpan = northEast.lat() - southWest.lat();
	
	markers = [];
	infoWindows = [];

	for (var i in chargeList) {
		var charge = chargeList[i];
		
		var content = ['<div>','/div>'].join('');
		
	    var marker = new naver.maps.Marker({
	        position: new naver.maps.LatLng(charge.lat, charge.lng),
	        map: map,
// 	        title: key,
	        icon: {
	        	url: HOME_PATH +'/img/example/sp_pins_spot_v3.png',
	            size: new naver.maps.Size(38, 58),
	            anchor: new naver.maps.Point(19, 58)
	        }
	    });

	    var infoWindow = new naver.maps.InfoWindow({
	        content: '<div style="width:150px;text-align:center;padding:10px;">충전소 명 : <b>"'+ charge.statNm +'"</b>.</div>'
	    });

	    markers.push(marker);
	    infoWindows.push(infoWindow);
	};
	
	naver.maps.Event.addListener(map, 'idle', function() {
	    updateMarkers(map, markers);
	});
// 	naver.maps.Event.addListener(map, 'zoom_changed', function() {
//         updateMarkers(map, markers);
//     });
//     naver.maps.Event.addListener(map, 'dragend', function() {
//         updateMarkers(map, markers);
//     });


	// 마커 클릭이벤트 등록..
	for (var i=0, ii=markers.length; i<ii; i++) {
		console.log(markers.length);
	    naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
	}
	
}


function updateMarkers(map, markers) {

    var mapBounds = map.getBounds();
    var marker, position;

    for (var i = 0; i < markers.length; i++) {

        marker = markers[i]
        position = marker.getPosition();

        if (mapBounds.hasLatLng(position)) {
            showMarker(map, marker);
        } else {
            hideMarker(map, marker);
        }
    }
}

function showMarker(map, marker) {

    if (marker.setMap()) return;
    marker.setMap(map);
}

function hideMarker(map, marker) {

    if (!marker.setMap()) return;
    marker.setMap(null);
}

// 해당 마커의 인덱스를 seq라는 클로저 변수로 저장하는 이벤트 핸들러를 반환합니다.
function getClickHandler(seq) {
    return function(e) {
        var marker = markers[seq],
            infoWindow = infoWindows[seq];

        if (infoWindow.getMap()) {
            infoWindow.close();
        } else {
            infoWindow.open(map, marker);
        }
    }
}
	
$(function() {
	 $('#ctgList').load("/layout/left", function() {
	        
	 });
})
</script>

</body>
</html>
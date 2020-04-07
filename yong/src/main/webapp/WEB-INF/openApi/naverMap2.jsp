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
	
})

// infoWindow 닫기
function closeInfoWindow(seq) {
	infoWindows[seq].close();
}

// 충전소 정보 가져오기(파라미터 : 주소)
function chargeData(addr) {
	if (!addr) {
		return false;
	}
	
	$.ajax({
	    url         : '/openApi/chargeMap',
	    type        : 'POST',
	    data        : {
	    	addr : addr
	    },
	    dataType    : 'json',
	    success        : function(data, status) {
	        search(data.infoMap.list);
	    },
	    error        : function(request, status, error) {
	        console.log('[ERROR]\nCODE : ' + request.status + '\nMESSAGE : ' + request.responseText + '\nERROR : ' + error);
	    }
	});
}

var markers = [];
var infoWindows = [];

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
		
		var content = [
			'<div style="padding:15px;">'
			 ,'<button type="button" onclick="closeInfoWindow(' + i + ');" style="border:0; outline:0;" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">', '<span aria-hidden="true">&times;</span></button>'
				,'<h4>',charge.csNm,'</h4>'
				,'<small>',charge.addr,'</small><hr>'
				,'충전기 ID &nbsp;&nbsp;&nbsp; : ', charge.cpId , '<br>'
				,'충전 방식 &nbsp;&nbsp;&nbsp; : ' , charge.cpTp , '<br>'
				,'충전기 타입 :',charge.chargeTp , '<br>'
// 				,'충전기 명칭 : ', charge.cpNm , '<br>'
				,'충전기 상태 :',charge.cpStat == "충전가능" ? '<span class="label label-success">' : '<span class="label label-important">' ,charge.cpStat ,'</span>',  '<br>'
				,'<small>업데이트 시간 : ', charge.statUpdateTime ,'</small>'
				,'<br>'
			,'</div>'].join('');
		
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
	        content: content
	    });

	    markers.push(marker);
	    infoWindows.push(infoWindow);
	    if (i == 0) {
	        contentEl.find('.totalCount').text(charge.totalCount + "개");
	    }
	    
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
		//console.log(markers.length);
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



var HOME_PATH = window.HOME_PATH || '.',
    urlPrefix = '/resources/json/region',
    urlSuffix = '.json',
    regionGeoJson = [],
    loadCount = 0;

var map = new naver.maps.Map(document.getElementById('map'), {
    zoom: 7,
    mapTypeId: 'normal',
    center: new naver.maps.LatLng(36.4203004, 128.317960)
});

// 맵박스를 이용해 행정구역 구분 맵 세팅
naver.maps.Event.once(map, 'init_stylemap', function () {
    for (var i = 1; i < 18; i++) {
        var keyword = i +'';

        if (keyword.length === 1) {
            keyword = '0'+ keyword;
        }

        $.ajax({
            url: urlPrefix + keyword + urlSuffix,
            success: function(idx) {
                return function(geojson) {
                    regionGeoJson[idx] = geojson;

                    loadCount++;

                    if (loadCount === 17) {
                        startDataLayer();
                    }
                }
            }(i - 1)
        });
    }
});

var contentEl = $('<div class="iw_inner" style="width:300px;position:absolute;top:0;right:0;z-index:1000;background-color:#fff;border:solid 1px #333;padding:15px;">'
		+ '<p style="font-size:14px;">선택된 행정구역 : <em class="choiceArea">없음</em></p>'
		+ '<p style="font-size:14px;">검색결과 수 : <em class="totalCount">0건</em></p>'
        + '<p style="font-size:14px;">zoom : <em class="zoom">'+ map.getZoom() +'</em></p>'
        + '<p style="font-size:14px;">centerPoint : <em class="center">'+ map.getCenterPoint() +'</em></p>'
        + '</div>');

    contentEl.appendTo(map.getElement());
    
    
    naver.maps.Event.addListener(map, 'zoom_changed', function(zoom) {
        contentEl.find('.zoom').text(zoom);
    });

    naver.maps.Event.addListener(map, 'bounds_changed', function(bounds) {
        contentEl.find('.center').text(map.getCenterPoint());
    });

var tooltip = $('<div style="position:absolute;z-index:1000;padding:5px 10px;background-color:#fff;border:solid 2px #000;font-size:14px;pointer-events:none;display:none;"></div>');

tooltip.appendTo(map.getPanes().floatPane);

function startDataLayer() {
    map.data.setStyle(function(feature) {
        var styleOptions = {
            fillColor: '#ff0000',
            fillOpacity: 0.0001,
            strokeColor: '#ff0000',
            strokeWeight: 2,
            strokeOpacity: 0.4
        };

        if (feature.getProperty('focus')) {
            styleOptions.fillOpacity = 0.6;
            styleOptions.fillColor = '#0f0';
            styleOptions.strokeColor = '#0f0';
            styleOptions.strokeWeight = 4;
            styleOptions.strokeOpacity = 1;
        }

        return styleOptions;
    });

    regionGeoJson.forEach(function(geojson) {
        map.data.addGeoJson(geojson);
    });

    // 행정구역 클릭 이벤트
    map.data.addListener('click', function(e) {
        var feature = e.feature;
        var area = feature.getProperty('area1');
        
        // 이미 선택된 행정구역은 return.
        if (contentEl.find('.choiceArea').text() == area) {
        	return false;
        }
        

        // 행정구역 상태 조건문인데 색을 안바꾸니 빠져도됨.
        if (feature.getProperty('focus') !== true) {
        	// 행정구역 클릭시 지도 색상 변경
            //feature.setProperty('focus', true);
        	
        	// 기존 맵에 그려진 마커 제거
            for(var i = 0; i < markers.length; i++){
            	markers[i].setMap(null);
            }
        	// infoWindow 닫기
            for(var i = 0; i < infoWindows.length; i++){
            	infoWindows[i].close();
            }
        	
        	// 선택한 행정구역 명
        	
        	
        	// KVO 변경
            contentEl.find('.choiceArea').text(area);
            
        	// 충전소 조회
            chargeData(area);
            
        } else {
            feature.setProperty('focus', false);
        }
    });

    // 행정구역 마우스 오버 이벤트
    map.data.addListener('mouseover', function(e) {
        var feature = e.feature,
            regionName = feature.getProperty('area1');
        
        // 이미 선택된 행정구역은 return.
        if (contentEl.find('.choiceArea').text() == regionName) {
        	return false;
        }

        tooltip.css({
            display: '',
            left: e.offset.x,
            top: e.offset.y
        }).text(regionName);

        map.data.overrideStyle(feature, {
            fillOpacity: 0.6,
            strokeWeight: 4,
            strokeOpacity: 1
        });
    });

    map.data.addListener('mouseout', function(e) {
        tooltip.hide().empty();
        map.data.revertStyle();
    });
}







$(function() {
	 $('#ctgList').load("/layout/left", function() {
	        
	 });
})
</script>

</body>
</html>
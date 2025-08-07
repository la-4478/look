<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>searchDetail</title>
</head>
<body>
<div id="map" style="width:500px; height:400px;"></div>

<script>
    var mapContainer = document.getElementById('map');
    var mapOption = { 
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3
    };
    var map = new kakao.maps.Map(mapContainer, mapOption);

    var markerPosition = new kakao.maps.LatLng(33.450701, 126.570667);
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });
    marker.setMap(map);

    // 전역 함수로 등록
    function moveMarker(lat, lng) {
        var newPos = new kakao.maps.LatLng(lat, lng);
        marker.setPosition(newPos);
        map.setCenter(newPos);
    }
</script>
</body>
</html>
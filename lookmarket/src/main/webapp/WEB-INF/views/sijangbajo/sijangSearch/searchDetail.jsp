<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!-- 지도 -->
<div id="map" style="width:83.5%;height:400px;"></div>

<script>
(function () {
  /** ********************************************************************
   * Kakao 지도: 초기엔 마커 0개. 검색/선택 시에만 렌더링.
   ********************************************************************* */

  // 전역 상태
 
	var _focusInfoWindow = null;
	window.map = window.map || null;
	window.clusterer = window.clusterer || null;
	window._pendingMoves = window._pendingMoves || [];
	var _focusMarker = null;
	var _nearbyMarkers = [];
	var _marketMarkers = [];
	var geocoder = null;

  // 서버에서 내려준(서울) 데이터는 “미리 적재”만 하고, 초기에는 그리지 않음
  var _preloadedMarkets = (function(){
    try { return ${seoulSijangListJson}; } catch(e) { return []; }
  })();

  // 지도 초기화
  function initMap() {
    var el = document.getElementById('map');
    if (!el) { console.error("#map element not found"); return; }

    // 지도 생성 (서울 시청 근처)
    window.map = new kakao.maps.Map(el, {
      center: new kakao.maps.LatLng(37.566826, 126.9786567),
      level: 7
    });

    // Geocoder
    if (kakao.maps.services && kakao.maps.services.Geocoder) {
      geocoder = new kakao.maps.services.Geocoder();
    }

    // 클러스터러
    if (kakao.maps.MarkerClusterer) {
      window.clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 8
      });
    }

    // ✅ 초기에는 마커를 그리지 않는다!
    // renderMarkets(_preloadedMarkets);  ← 호출하지 않음

    // 초기화 이후, 대기 중이던 moveMarker 요청 처리
    if (window._pendingMoves.length) {
      var q = window._pendingMoves.splice(0);
      q.forEach(function (it) { window.moveMarker(it.lat, it.lng, it.title); });
    }

    setTimeout(function(){ if (window.map) window.map.relayout(); }, 400);
  }

  // 좌표 → 주소 (역지오코딩)
  function reverseGeocode(lng, lat, cb) {
    if (!geocoder) { cb(null); return; }
    geocoder.coord2Address(Number(lng), Number(lat), function(result, status) {
      if (status !== kakao.maps.services.Status.OK || !result || !result.length) { cb(null); return; }
      var road  = result[0].road_address && result[0].road_address.address_name;
      var jibun = result[0].address && result[0].address.address_name;
      cb(road || jibun || null);
    });
  }

  // 전통시장 마커 모두 제거
  function clearMarketMarkers() {
    if (window.clusterer) clusterer.clear();
    _marketMarkers.forEach(function(m){ m.setMap && m.setMap(null); });
    _marketMarkers = [];
    if (_focusInfoWindow) _focusInfoWindow.close();
  }

  // 전달된 시장 리스트 렌더(수동 호출)
  function renderMarkets(markets) {
    if (!Array.isArray(markets)) markets = [];
    clearMarketMarkers();

    var markers = markets
      .filter(function(m){ return m.lat && m.lng; })
      .map(function(m){
        var lat = Number(m.lat), lng = Number(m.lng);
        var marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(lat, lng)
        });
        _marketMarkers.push(marker);

        kakao.maps.event.addListener(marker, 'click', function(){
          var title = ((m['시장명'] || '전통시장') + '')
            .replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
          reverseGeocode(lng, lat, function(address){
       		if (_focusInfoWindow) { _focusInfoWindow.close(); }
        	if (!_focusInfoWindow) {
        	  _focusInfoWindow = new kakao.maps.InfoWindow({ removable: true });
        	}
        	var html = '<div style="padding:6px 10px;"><b>'+title+'</b><br/>'+(address||'주소를 찾을 수 없습니다.')+'</div>';
        	_focusInfoWindow.setContent(html);
        	_focusInfoWindow.open(map, marker);
            map.panTo(marker.getPosition());
          });
        });
        return marker;
      });

    if (window.clusterer && markers.length) {
      clusterer.addMarkers(markers);
    } else {
      markers.forEach(function(mk){ mk.setMap(map); });
    }
  }

  // SDK 준비 보팅
  function boot() {
    if (window.kakao && kakao.maps) {
      if (typeof kakao.maps.load === "function") kakao.maps.load(initMap);
      else initMap();
    } else {
      setTimeout(boot, 120);
    }
  }
  boot();

  // 외부 공개 API들
  window.clearMarketMarkers = clearMarketMarkers;
  window.renderMarkets = renderMarkets;
  window.getPreloadedMarkets = function(){ return _preloadedMarkets; };

  // 리스트/검색에서 호출: 지도 이동 + 강조 마커 + 주소 인포윈도우
  window.moveMarker = function (lat, lng, title) {
    if (!(window.kakao && kakao.maps && window.map)) {
      window._pendingMoves.push({ lat: Number(lat), lng: Number(lng), title: title || '' });
      return;
    }
    lat = Number(lat); lng = Number(lng);
    var pos = new kakao.maps.LatLng(lat, lng);
    map.panTo(pos);

    if (!_focusMarker) _focusMarker = new kakao.maps.Marker({ map: map, position: pos });
    else _focusMarker.setPosition(pos);

    var safeTitle = (title || '선택한 시장').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
    reverseGeocode(lng, lat, function(address){
    // 기존 창이 있으면 닫고 하나만 사용
    if (_focusInfoWindow) { _focusInfoWindow.close(); }
    if (!_focusInfoWindow) {
      _focusInfoWindow = new kakao.maps.InfoWindow({ removable: true });
    }
    var html = '<div style="padding:6px 10px;"><b>'+safeTitle+'</b><br/>'+(address||'주소를 찾을 수 없습니다.')+'</div>';
    _focusInfoWindow.setContent(html);
    _focusInfoWindow.open(map, _focusMarker);
    });
  };

  // 주변 POI(카테고리) 열기
  window.openNearby = function (lng, lat, code) {
    if (!window.map) return;

    _nearbyMarkers.forEach(function(mk){ mk.setMap(null); });
    _nearbyMarkers = [];

    var url = "${contextPath}/sijangbajo/sijangSearch/nearby.json?x=" + lng + "&y=" + lat + "&code=" + code;

    fetch(encodeURI(url), { cache: "no-store" })
      .then(function(res){ return res.json(); })
      .then(function(data){
        (data || []).forEach(function(poi){
          var mk = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(Number(poi.y), Number(poi.x))
          });
          _nearbyMarkers.push(mk);

          var safeName = (poi.place_name || '').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
          var addr = (poi.road_address_name || poi.address_name || '');
          var iw = new kakao.maps.InfoWindow({
            content: '<div style="padding:6px 10px;">'+safeName+'<br/>'+addr+'<br/>거리: '+(poi.distance||'-')+'m</div>'
          });
          kakao.maps.event.addListener(mk, 'click', function(){ iw.open(map, mk); });
        });
      })
      .catch(console.error);
  };
  
  kakao.maps.event.addListener(map, 'click', function() {
	  if (_focusInfoWindow) _focusInfoWindow.close();
	});

  // 안전빵: 초기엔 마커 비우기(혹시 레이아웃 상에서 다른 스크립트가 먼저 추가했을 수도 있으니)
  window.addEventListener('DOMContentLoaded', function(){
    if (window.clearMarketMarkers) window.clearMarketMarkers();
  });
})();
</script>

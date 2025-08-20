<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<div id="map" style="width:100%;height:600px;"></div>

<script>
(function () {
  /** ********************************************************************
   *  Kakao SDK 로딩/지도 초기화 보장 & 디버깅 로그
   *  - SDK가 늦게 와도 boot()가 재시도
   *  - moveMarker가 초기화 전에 호출되면 큐에 쌓았다가 flush
   *  - JSP/EL 충돌 없도록 문자열 연결로만 작성
   ********************************************************************* */

  // SDK 로딩 상태 진단 로그
  var sdkScript = document.querySelector('script[src*="dapi.kakao.com/v2/maps/sdk.js"]');
  console.log("[Kakao SDK src]", sdkScript ? sdkScript.src : "NOT FOUND");
  console.log("[kakao defined?]", !!window.kakao, "maps?", !!(window.kakao && kakao.maps));

  // 전역 오브젝트(지도/클러스터러/펜딩 호출 큐)
  window.map = window.map || null;
  window.clusterer = window.clusterer || null;
  window._pendingMoves = window._pendingMoves || [];     // 초기화 전 moveMarker 호출 큐
  var _focusMarker = null;                               // 강조 마커
  var _nearbyMarkers = [];                               // 주변 상권 마커들

  // ───── 지도 초기화 ─────
  function initMap() {
    var el = document.getElementById('map');
    if (!el) {
      console.error("#map element not found");
      return;
    }

    // 컨테이너가 display:none으로 그려졌다면 레이아웃 계산이 꼬일 수 있어서 한번 더 relayout 예정
    if (el.offsetWidth === 0 || el.offsetHeight === 0) {
      console.warn("[Map] container size is 0. relayout scheduled…");
      setTimeout(function(){ if (window.map) window.map.relayout(); }, 250);
    }

    // 지도 생성
    window.map = new kakao.maps.Map(el, {
      center: new kakao.maps.LatLng(37.566826, 126.9786567),
      level: 7
    });
    console.log("[Map] initialized");

    // 클러스터러(라이브러리 로드 시)
    if (kakao.maps.MarkerClusterer) {
      window.clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 8
      });
    }

    // 서버에서 내려준 마켓 데이터 렌더
    renderMarketsSafe();

    // 초기화 끝났으니, 대기 중이던 moveMarker 요청 처리
    if (window._pendingMoves.length) {
      console.log("[Map] flushing pending moves:", window._pendingMoves.length);
      var q = window._pendingMoves.splice(0);
      q.forEach(function (it) { window.moveMarker(it.lat, it.lng, it.title); });
    }

    // 컨테이너가 탭 전환 등으로 뒤늦게 보이는 경우를 대비해 한 번 더 relayout
    setTimeout(function(){ if (window.map) window.map.relayout(); }, 400);
  }

  // ───── 마켓 데이터 렌더(예외 안전) ─────
  function renderMarketsSafe() {
    var markets;
    try {
      markets = ${seoulSijangListJson};
      console.log("[markets]", Array.isArray(markets) ? markets.length : markets);
    } catch (e) {
      console.error("seoulSijangListJson 파싱 오류:", e);
      markets = [];
    }

    var markers = (markets || [])
      .filter(function(m){ return m.lat && m.lng; })
      .map(function(m){
        var marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(m.lat, m.lng)
        });

        var content =
          '<div style="padding:6px 10px;">'
          + '<b>' + (m['시장명'] || '전통시장') + '</b><br/>'
          + (m['지번주소'] || '') + '<br/>'
          + '<button onclick="openNearby(' + m.lng + ',' + m.lat + ',\'FD6\')">주변 음식점</button> '
          + '<button onclick="openNearby(' + m.lng + ',' + m.lat + ',\'CE7\')">주변 카페</button>'
          + '</div>';

        var iw = new kakao.maps.InfoWindow({ content: content });
        kakao.maps.event.addListener(marker, 'click', function () {
          iw.open(map, marker);
          map.panTo(marker.getPosition());
        });
        return marker;
      });

    if (window.clusterer && markers.length) {
      clusterer.addMarkers(markers);
    } else {
      markers.forEach(function(mk){ mk.setMap(map); });
    }
  }

  // ───── SDK 로딩이 늦어도 안전 부팅 ─────
  function boot() {
    if (window.kakao && kakao.maps) {
      if (typeof kakao.maps.load === "function") {
        kakao.maps.load(initMap);        // autoload=true 환경에서도 안전
      } else {
        initMap();
      }
    } else {
      console.warn("[Kakao] 아직 준비 안 됨. 재시도…");
      setTimeout(boot, 150);
    }
  }
  boot();

  // ───── 외부에서 호출하는 함수들 ─────

  // 리스트에서 좌표 이동 요청
  window.moveMarker = function (lat, lng, title) {
    // 지도/SDK 준비 전이면 큐에 쌓고 리턴
    if (!(window.kakao && kakao.maps && window.map)) {
      console.warn("[moveMarker] map not ready → enqueue", lat, lng, title);
      window._pendingMoves.push({ lat: Number(lat), lng: Number(lng), title: title || '' });
      return;
    }

    var pos = new kakao.maps.LatLng(Number(lat), Number(lng));
    map.panTo(pos);

    if (!_focusMarker) _focusMarker = new kakao.maps.Marker({ map: map, position: pos });
    else _focusMarker.setPosition(pos);

    var safeTitle = (title || '선택한 시장')
      .replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');

    var iw = new kakao.maps.InfoWindow({
      content: '<div style="padding:6px 10px;"><b>' + safeTitle + '</b><br/>이 위치로 이동했습니다.</div>'
    });
    iw.open(map, _focusMarker);
  };

  // 주변 상권 조회(카테고리 코드: FD6=음식점, CE7=카페 등)
  window.openNearby = function (lng, lat, code) {
    if (!window.map) { console.error("map not ready"); return; }

    // 기존 주변 마커 제거
    _nearbyMarkers.forEach(function(mk){ mk.setMap(null); });
    _nearbyMarkers = [];

    var url = "${contextPath}/sijangbajo/sijangSearch/nearby.json?x=" + lng + "&y=" + lat + "&code=" + code;

    fetch(encodeURI(url), { cache: "no-store" })
      .then(function(res){ return res.json(); })
      .then(function(data){
        console.log("[nearby]", code, Array.isArray(data) ? data.length : data);
        (data || []).forEach(function(poi){
          var mk = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(poi.y, poi.x)
          });
          _nearbyMarkers.push(mk);

          var iw = new kakao.maps.InfoWindow({
            content:
              '<div style="padding:6px 10px;">'
              + (poi.place_name || '')
              + '<br/>' + (poi.road_address_name || poi.address_name || '')
              + '<br/>거리: ' + (poi.distance || '-') + 'm'
              + '</div>'
          });
          kakao.maps.event.addListener(mk, 'click', function(){ iw.open(map, mk); });
        });
      })
      .catch(function(e){
        console.error("openNearby error:", e);
      });
  };
})();
</script>

<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<div id="map" style="width:83.%;height:400px;"></div>

<script>
(function () {
  /** ********************************************************************
   *  Kakao SDK 로딩/지도 초기화 보장 + 역지오코딩 주소 표시
   *  - SDK가 늦게 와도 boot()가 재시도
   *  - moveMarker가 초기화 전에 호출되면 큐에 쌓았다가 flush
   *  - InfoWindow에는 "이 위치로 이동했습니다" 대신 좌표에 대한 주소를 표시
   ********************************************************************* */

  // ── 디버깅 로그: SDK 로딩 상태 ──
  var sdkScript = document.querySelector('script[src*="dapi.kakao.com/v2/maps/sdk.js"]');
  console.log("[Kakao SDK src]", sdkScript ? sdkScript.src : "NOT FOUND");
  console.log("[kakao defined?]", !!window.kakao, "maps?", !!(window.kakao && kakao.maps));

  // ── 전역 객체들 ──
  window.map = window.map || null;               // kakao.maps.Map 인스턴스
  window.clusterer = window.clusterer || null;   // MarkerClusterer
  window._pendingMoves = window._pendingMoves || []; // 초기화 전 moveMarker 호출 큐
  var _focusMarker = null;                       // 리스트 선택 시 강조 마커
  var _nearbyMarkers = [];                       // 주변 상권 마커들
  var geocoder = null;                           // 역지오코딩용 Geocoder

  // ────────────────────────────────────────────────────────────────────
  // 1) 지도 초기화
  // ────────────────────────────────────────────────────────────────────
  function initMap() {
    var el = document.getElementById('map');
    if (!el) {
      console.error("#map element not found");
      return;
    }

    // 컨테이너가 display:none이면 초기 레이아웃 계산이 꼬일 수 있음 → 약간 뒤에 relayout
    if (el.offsetWidth === 0 || el.offsetHeight === 0) {
      console.warn("[Map] container size is 0. relayout scheduled…");
      setTimeout(function(){ if (window.map) window.map.relayout(); }, 250);
    }

    // 지도 생성 (서울 시청 근처)
    window.map = new kakao.maps.Map(el, {
      center: new kakao.maps.LatLng(37.566826, 126.9786567),
      level: 7
    });
    console.log("[Map] initialized");

    // Geocoder 생성 (services 라이브러리 필요)
    if (kakao.maps.services && kakao.maps.services.Geocoder) {
      geocoder = new kakao.maps.services.Geocoder();
    } else {
      console.warn("[Kakao] services 라이브러리가 없습니다. 주소 표시 불가");
    }

    // 클러스터러 생성(라이브러리가 있으면)
    if (kakao.maps.MarkerClusterer) {
      window.clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 8
      });
    }

    // 서버에서 내려준 시장 데이터로 마커 렌더링
    renderMarketsSafe();

    // 초기화 이후, 대기 중이던 moveMarker 요청 처리
    if (window._pendingMoves.length) {
      console.log("[Map] flushing pending moves:", window._pendingMoves.length);
      var q = window._pendingMoves.splice(0);
      q.forEach(function (it) { window.moveMarker(it.lat, it.lng, it.title); });
    }

    // 탭 전환 등으로 뒤늦게 보이는 경우 대비해 relayout
    setTimeout(function(){ if (window.map) window.map.relayout(); }, 400);
  }

  // ────────────────────────────────────────────────────────────────────
  // 2) 좌표 → 주소 (역지오코딩) 헬퍼
  //    - lng, lat 순서 주의: Kakao는 x=lng, y=lat
  // ────────────────────────────────────────────────────────────────────
  function reverseGeocode(lng, lat, cb) {
    if (!geocoder) { cb(null); return; }
    geocoder.coord2Address(Number(lng), Number(lat), function(result, status) {
      if (status !== kakao.maps.services.Status.OK || !result || !result.length) {
        cb(null);
        return;
      }
      var road  = result[0].road_address && result[0].road_address.address_name;
      var jibun = result[0].address && result[0].address.address_name;
      cb(road || jibun || null);
    });
  }

  // ────────────────────────────────────────────────────────────────────
  // 3) 시장 데이터 렌더 (예외 안전 + 주소를 역지오코딩으로 표출)
  //    - 마커 클릭 시: 해당 좌표의 주소를 조회해서 인포윈도우에 표시
  //    - 기존 m['지번주소'] 고정값 대신 항상 실제 좌표 기준 주소 사용
  // ────────────────────────────────────────────────────────────────────
  function renderMarketsSafe() {
    var markets;
    try {
      // 컨트롤러에서 JSON 문자열로 내려준 걸 그대로 EL로 주입한다고 가정
      markets = ${seoulSijangListJson};
      console.log("[markets]", Array.isArray(markets) ? markets.length : markets);
    } catch (e) {
      console.error("seoulSijangListJson 파싱 오류:", e);
      markets = [];
    }

    var markers = (markets || [])
      .filter(function(m){ return m.lat && m.lng; })
      .map(function(m){
        var lat = Number(m.lat), lng = Number(m.lng);
        var marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(lat, lng)
        });

        // 마커 클릭 시, 역지오코딩해서 주소 표시
        kakao.maps.event.addListener(marker, 'click', function () {
          // 안전한 타이틀(시장명)
          var safeTitle = ((m['시장명'] || '전통시장') + '')
            .replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');

          reverseGeocode(lng, lat, function(address){
            var html =
              '<div style="padding:6px 10px;">'
              + '<b>' + safeTitle + '</b><br/>'
              + (address ? address : '주소를 찾을 수 없습니다.');
              
            var iw = new kakao.maps.InfoWindow({ content: html });
            iw.open(map, marker);
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

  // ────────────────────────────────────────────────────────────────────
  // 4) SDK 로딩이 늦어도 안전 부팅
  // ────────────────────────────────────────────────────────────────────
  function boot() {
    if (window.kakao && kakao.maps) {
      if (typeof kakao.maps.load === "function") {
        kakao.maps.load(initMap); // autoload=true 환경에서도 안전
      } else {
        initMap();
      }
    } else {
      console.warn("[Kakao] 아직 준비 안 됨. 재시도…");
      setTimeout(boot, 150);
    }
  }
  boot();

  // ────────────────────────────────────────────────────────────────────
  // 5) 외부에서 호출하는 API들
  // ────────────────────────────────────────────────────────────────────

  /**
   * 리스트에서 선택 시 지도 이동 + 강조 마커 + 주소 인포윈도우
   * @param {number|string} lat 위도
   * @param {number|string} lng 경도
   * @param {string} title  표시할 시장명(옵션)
   */
  window.moveMarker = function (lat, lng, title) {
    // 지도/SDK 준비 전이면 큐에 쌓고 리턴
    if (!(window.kakao && kakao.maps && window.map)) {
      console.warn("[moveMarker] map not ready → enqueue", lat, lng, title);
      window._pendingMoves.push({ lat: Number(lat), lng: Number(lng), title: title || '' });
      return;
    }

    lat = Number(lat); lng = Number(lng);
    var pos = new kakao.maps.LatLng(lat, lng);
    map.panTo(pos);

    if (!_focusMarker) _focusMarker = new kakao.maps.Marker({ map: map, position: pos });
    else _focusMarker.setPosition(pos);

    var safeTitle = (title || '선택한 시장')
      .replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');

    // ✅ 여기서 좌표 → 주소 변환 후 인포윈도우 표시
    reverseGeocode(lng, lat, function(address){
      var html =
        '<div style="padding:6px 10px;">'
        + '<b>' + safeTitle + '</b><br/>'
        + (address ? address : '주소를 찾을 수 없습니다.')
        + '</div>';

      var iw = new kakao.maps.InfoWindow({ content: html });
      iw.open(map, _focusMarker);
    });
  };

  /**
   * 주변 상권 조회(카테고리 코드: FD6=음식점, CE7=카페 등)
   * 서버의 프록시/REST 엔드포인트로 위경도, 코드 전달 → JSON으로 POI 목록 반환 가정
   */
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
            position: new kakao.maps.LatLng(Number(poi.y), Number(poi.x))
          });
          _nearbyMarkers.push(mk);

          var safeName = (poi.place_name || '').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
          var addr = (poi.road_address_name || poi.address_name || '');

          var iw = new kakao.maps.InfoWindow({
            content:
              '<div style="padding:6px 10px;">'
              + safeName
              + '<br/>' + addr
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
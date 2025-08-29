<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전통시장 주변 상권 검색</title>
<link rel="stylesheet" href="${contextPath}/resources/css/sijang.css" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
#map {
	width: 100%;
	height: 400px;
	margin-top: 20px;
}

table, th, td {
	border: 1px solid #ccc;
	padding: 8px;
	text-align: left;
	border-collapse: collapse;
}

th {
	background: #f5f5f5;
}

.loading {
	display: none;
	font-weight: bold;
	margin-top: 10px;
}

.result-row {
	cursor: pointer;
}
</style>
</head>
<body>
	<h2>전통시장 주변 상권 검색</h2>

	<form id="searchForm">
		<label>지역 <select id="region" name="key">
				<option value="11">서울특별시</option>
				<option value="26">부산광역시</option>
				<option value="27">대구광역시</option>
				<option value="28">인천광역시</option>
				<option value="29">광주광역시</option>
				<option value="30">대전광역시</option>
				<option value="31">울산광역시</option>
				<option value="36">세종특별자치시</option>
				<option value="41">경기도</option>
				<option value="42">강원도</option>
				<option value="43">충청북도</option>
				<option value="44">충청남도</option>
				<option value="45">전라북도</option>
				<option value="46">전라남도</option>
				<option value="47">경상북도</option>
				<option value="48">경상남도</option>
				<option value="50">제주특별자치도</option>
		</select>
		</label> <label>업종 대분류 <select id="categoryLcls" name="indsLclsCd">
				<option value="">-- 전체 --</option>
				<option value="G2">도소매</option>
				<option value="I1">숙박</option>
				<option value="I2">음식점</option>
				<option value="L1">부동산</option>
				<option value="M1">과학, 기술 서비스</option>
				<option value="N1">시설관리, 임대</option>
				<option value="P1">교육</option>
				<option value="Q1">보건의료</option>
				<option value="R1">예술, 스포츠, 여가</option>
				<option value="S2">수리, 개인 서비스</option>
		</select>

		</label> <label>업종 중분류 <select id="categoryMcls" name="indsMclsCd">
				<option value="">-- 전체 --</option>
		</select>
		</label> <input type="text" id="keyword" name="keyword" placeholder="키워드" />
		<button type="submit">검색</button>
	</form>

	<div class="loading" id="loadingMsg">검색 중...</div>

	<table id="resultTable" style="display: none;">
		<thead>
			<tr>
				<th>가게 이름</th>
				<th>업종</th>
				<th>주소</th>
			</tr>
		</thead>
		<tbody id="resultBody">
			<!-- Ajax로 동적 생성 -->
		</tbody>
	</table>

	<div id="map"></div>

	<script
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f3a5008edad1780fc2ddff81474f335f"></script>

	<script>
	let currentInfoWindow = null; // 목록 클릭 시 열린 인포윈도우를 관리
	
	function safeText(value) {
	    return (typeof value === 'string' || typeof value === 'number') && value !== null && value !== undefined
	        ? String(value).trim()
	        : '';
	}
var contextPath = '<c:out value="${contextPath}"/>';

const regionCoordMap = {
    "11": {lat:37.5665, lng:126.9780}, "26": {lat:35.1796, lng:129.0756}, "27": {lat:35.8714, lng:128.6014},
    "28": {lat:37.4563, lng:126.7052}, "29": {lat:35.1595, lng:126.8526}, "30": {lat:36.3504, lng:127.3845},
    "31": {lat:35.5396, lng:129.3114}, "36": {lat:36.4800, lng:127.2890}, "41": {lat:37.4138, lng:127.5183},
    "42": {lat:37.8228, lng:128.1555}, "43": {lat:36.6285, lng:127.9290}, "44": {lat:36.5184, lng:126.8000},
    "45": {lat:35.7175, lng:127.1530}, "46": {lat:34.8161, lng:126.4625}, "47": {lat:36.4919, lng:128.8889},
    "48": {lat:35.4606, lng:128.2132}, "50": {lat:33.4890, lng:126.4983}
};

$(function(){
    var mapContainer = document.getElementById('map');
    var mapOption = { center: new kakao.maps.LatLng(37.5665, 126.9780), level: 6 };
    var map = new kakao.maps.Map(mapContainer, mapOption);
//     var clusterer = new kakao.maps.MarkerClusterer({ map: map, averageCenter: true, minLevel: 4 });
    var markers = [];
    
 // 전역 인포윈도우 딱 한 번 생성
    const infowindow = new kakao.maps.InfoWindow({ removable: true });

    // 대분류 → 중분류
    $('#categoryLcls').change(function(){
    const lclsCode = $(this).val();
    console.log('대분류 선택됨:', lclsCode);
    const mclsSelect = $('#categoryMcls');
    mclsSelect.empty();
    mclsSelect.append(new Option('-- 전체 --', ''));

    if (lclsCode) {
        $.getJSON(contextPath + '/sijangbajo/nearby/getMiddleCategory.do', { indsLclsCd: lclsCode })
        .done(function(data){
            data.forEach(function(item){
                mclsSelect.append(new Option(item[1], item[0]));
            });
        });
    }
});


    // 지역 변경 시 지도 이동
    $('#region').change(function(){
        const coord = regionCoordMap[$(this).val()];
        if(coord){
            map.setCenter(new kakao.maps.LatLng(coord.lat, coord.lng));
            map.setLevel(6);
        }
    });

 // 검색 결과 클릭 시 지도 이동
    let clickMarker = null; // 클릭 시 표시할 마커

    $('#resultBody').on('click', '.result-row', function(){
        const lat = parseFloat($(this).data('lat'));
        const lng = parseFloat($(this).data('lng'));
        const name = $(this).find('td:first').text(); // 가게 이름
        if (!isNaN(lat) && !isNaN(lng)) {
            const position = new kakao.maps.LatLng(lat, lng);

            // 기존 클릭 마커 제거
            if (clickMarker) {
                clickMarker.setMap(null);
                clickMarker = null;
            }

            // 기존 열린 인포윈도우 닫기
            if (currentInfoWindow) {
                currentInfoWindow.close();
            }

            // 마커 배열에서 해당 좌표 마커 찾기
            let targetMarker = null;
            for(let i = 0; i < markers.length; i++) {
                const pos = markers[i].getPosition();
                if (pos.getLat() === lat && pos.getLng() === lng) {
                    targetMarker = markers[i];
                    break;
                }
            }

            if (targetMarker) {
                // 마커가 있으면 해당 마커를 사용해 인포윈도우 띄우기
                infowindow.setContent(targetMarker.content || `<div style="padding:5px; font-size:13px;">${name}</div>`);
                infowindow.open(map, targetMarker);
                currentInfoWindow = infowindow;

                // 지도 중앙 이동 및 확대
                map.setCenter(position);
                map.setLevel(3);
            } else {
                // 마커가 없으면 새로 생성 (필요하다면)
                clickMarker = new kakao.maps.Marker({
                    position: position,
                    map: map
                });
                infowindow.setContent(`<div style="padding:5px; font-size:13px;">${name}</div>`);
                infowindow.open(map, clickMarker);
                currentInfoWindow = infowindow;
                map.setCenter(position);
                map.setLevel(3);
            }
        }
    });
    
    // 검색
    $('#searchForm').submit(function(e){
        e.preventDefault(); // 기본 submit 막기
        $('#loadingMsg').show();
        $('#resultTable').hide();

        const divId = "ctprvnCd";
        const region = safeText($('#region').val());
        const indsLclsCd = safeText($('#categoryLcls').val());
        const indsMclsCd = safeText($('#categoryMcls').val());
        const indsSclsCd = ''; // 소분류 select 없으면 빈값
        const keyword = safeText($('#keyword').val());
        
        console.log("대분류 코드:", indsLclsCd);
        console.log("중분류 코드:", indsMclsCd);
        
        $.ajax({
            url: contextPath + '/sijangbajo/nearby/searchApi1.do',
            data: { divId, key: region, indsLclsCd, indsMclsCd, indsSclsCd, keyword },
            dataType: 'json',
            method: 'GET'
        })
        .done(function(storeList){
            const tbody = $('#resultBody').empty();            
//             clusterer.removeMarkers(markers);
			markers.forEach(m => m.setMap(null));
            markers = [];

            if(storeList.length === 0){
                alert('검색 결과가 없습니다.');
            } else {
            	console.log('받은 데이터:', storeList);
            	const tbody = $('#resultBody');
            	storeList.forEach(function(store){
            		const lat = parseFloat(store.latitude || store.lat || 'NaN');
            		const lng = parseFloat(store.longitude || store.lon || 'NaN');
            	    const isValidCoord = !isNaN(lat) && !isNaN(lng);          

            	    const $tr = $('<tr>')
            	        .addClass('result-row')
            	        .attr('data-lat', isValidCoord ? lat : '')
            	        .attr('data-lng', isValidCoord ? lng : '');

            	    $tr.append($('<td>').text(safeText(store.bizesNm) || ''));
            	    $tr.append($('<td>').text(
            	    		  safeText(store.indsLclsNm) + ' / ' +
            	    		  safeText(store.indsMclsNm) + ' / ' +
            	    		  safeText(store.indsSclsNm)
            	    		));
            	    $tr.append($('<td>').text(safeText(store.rdnmAdr) || ''));
            	    $tr.appendTo(tbody);

            	    if (isValidCoord) {
            	        const marker = new kakao.maps.Marker({
            	            position: new kakao.maps.LatLng(lat, lng),
            	            map: map
            	        });

            	        (function(s) {
            	            kakao.maps.event.addListener(marker, 'click', function() {
            	            	
            	                if (currentInfoWindow) {
            	                    currentInfoWindow.close();
            	                }

            	                const content = `
            	                    <div style="padding:10px; font-size:13px; line-height:1.6;">
            	                        <strong>\${safeText(s.bizesNm) || '이름없음'}</strong><br>
            	                        <span>\${safeText(s.indsLclsNm)} / \${safeText(s.indsMclsNm)} / \${safeText(s.indsSclsNm)}</span><br>
            	                        <span>\${safeText(s.rdnmAdr) || '주소없음'}</span>
            	                    </div>
            	                `;

            	                infowindow.setContent(content);
            	                infowindow.open(map, marker);
            	                currentInfoWindow = infowindow;
            	            });
            	        })(store);

            	        markers.push(marker);
            	    }
            	});
                $('#resultTable').show();
                }
        })
        .fail(function(xhr, status, error){
            console.error("검색 AJAX 실패:", status, error);
            alert('검색 중 오류 발생. 콘솔 확인');
        })
        .always(function(){ $('#loadingMsg').hide(); });
    });
});
</script>
</body>
</html>

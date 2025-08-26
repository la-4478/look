<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="pageType" value="sijangbajo" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전통시장 찾기</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="${contextPath}/resources/css/sijang.css" />
<!-- 카카오 지도 SDK (컨트롤러에서 kakaoJsKey 내려옴) — 실제 운영에선 레이아웃에 1회만 로드 권장 -->
<script
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f3a5008edad1780fc2ddff81474f335f&libraries=clusterer,services"></script>

<script>
	// ----- 공통 유틸: XSS-safe 문자열 escape -----
	function esc(s) {
		return (s == null ? '' : String(s)).replace(/&/g, '&amp;').replace(
				/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;')
				.replace(/'/g, '&#39;');
	}

	// ----- JSP와 동일한 마크업으로 1개 아이템 렌더 -----
	function renderMarketItem(name, addr) {
		return '' + '<div class="market-item"' + ' data-market-name="'
				+ esc(name) + '"' + ' data-market-addr="' + esc(addr) + '">'
				+ '<div class="market-name">'
				+ '<a href="#" class="js-market-link"' + ' data-name="'
				+ esc(name) + '"' + ' data-addr="' + esc(addr) + '">'
				+ esc(name) + '</a>' + '</div>'
				+ '<div class="market-address">' + esc(addr) + '</div>'
				+ '<div class="market-empty"></div>' + '</div>';
	}

	// ----- 시군구 셀렉트 채우기 -----
	function changeSigungu() {
		var sido = document.getElementById("sido").value;
		var sigunguSelect = document.getElementById("sigungu");
		sigunguSelect.innerHTML = ""; // 초기화

		var options = [];
		if (sido === "서울") {
			options = [ "전체", "종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구",
					"성북구", "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구",
					"강서구", "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구",
					"송파구", "강동구" ];
		} else if (sido === "부산") {
			options = [ "전체", "중구", "서구", "동구", "영도구", "부산광역시진구", "동래구", "남구",
					"북구", "해운대구", "사하구", "금정구", "강서구", "연제구", "수영구", "사상구" ];
		} else if (sido === "대구") {
			options = [ "전체", "중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군" ];
		} else if (sido === "인천") {
			options = [ "전체", "중구", "동구", "미추홀구", "연수구", "남동구", "부평구", "계양구",
					"서구", "강화군", "옹진군" ];
		} else if (sido === "광주") {
			options = [ "전체", "동구", "서구", "남구", "북구", "광산구" ];
		} else if (sido === "대전") {
			options = [ "전체", "동구", "중구", "서구", "유성구", "대덕구" ];
		} else if (sido === "울산광역시") {
			options = [ "전체", "중구", "남구", "동구", "북구", "울주군" ];
		} else if (sido === "세종") {
			options = [ "전체", "세종특별자치시" ];
		} else if (sido === "경기도") {
			options = [ "전체", "수원시 장안구", "수원시 권선구", "수원시 팔달구", "수원시 영통구",
					"성남시 수정구", "성남시 중원구", "성남시 분당구", "고양시 덕양구", "고양시 일산동구",
					"고양시 일산서구", "용인시 처인구", "용인시 기흥구", "용인시 수지구", "부천시",
					"안산시 상록구", "안산시 단원구", "남양주시", "화성시", "평택시", "의정부시" ];
		} else if (sido === "강원도") {
			options = [ "전체", "춘천시", "원주시", "강릉시", "동해시", "태백시", "속초시", "삼척시" ];
		} else if (sido === "충청북도") {
			options = [ "전체", "청주시 상당구", "청주시 서원구", "청주시 흥덕구", "청주시 청원구",
					"충주시", "제천시" ];
		} else if (sido === "충청남북") {
			options = [ "전체", "천안시 동남구", "천안시 서북구", "공주시", "보령시", "아산시", "서산시" ];
		} else if (sido === "전라북도") {
			options = [ "전체", "전주시 완산구", "전주시 덕진구", "군산시", "익산시", "정읍시" ];
		} else if (sido === "전라남도") {
			options = [ "전체", "목포시", "여수시", "순천시", "나주시" ];
		} else if (sido === "경상북도") {
			options = [ "전체", "포항시 남구", "포항시 북구", "경주시", "안동시", "김천시" ];
		} else if (sido === "경상남도") {
			options = [ "전체", "창원시 의창구", "창원시 성산구", "창원시 마산합포구", "창원시 마산회원구",
					"창원시 진해구", "진주시" ];
		} else if (sido === "제주특별자치도") {
			options = [ "전체", "제주시", "서귀포시" ];
		} else {
			options = [ "전체" ];
		}
		options.forEach(function(opt) {
			var optionElement = document.createElement("option");
			optionElement.value = opt;
			optionElement.text = opt;
			sigunguSelect.add(optionElement);
		});
	}

	$(document)
			.ready(
					function() {
						// ----- 검색 버튼 -----
						$("#searchBtn")
								.click(
										function() {
											const sido = $("#sido").val();
											const sigungu = $("#sigungu").val();
											const marketName = $("#marketName")
													.val();

											if (!sido) {
												alert("시도를 선택하세요.");
												return;
											}

											$
													.ajax({
														type : "get",
														async : true,
														url : "${contextPath}/sijangbajo/sijangSearch/searchDetail.do",
														dataType : "json",
														data : {
															sido : sido,
															sigungu : sigungu,
															marketName : marketName
														},
														success : function(data) {
															console.log(
																	"받은 데이터:",
																	data);

															var html = "";
															if (!data
																	|| data.length === 0) {
																html = "<p>검색 결과가 없습니다.</p>";
															} else {
																html = data
																		.map(
																				function(
																						item) {
																					var name = (item["시장명"] || "")
																							.trim()
																							|| "시장명 없음";
																					var addr = ((item["지번주소"] || "")
																							.trim())
																							|| ((item["도로명주소"] || "")
																									.trim())
																							|| ((item["소재지지번주소"] || "")
																									.trim())
																							|| ((item["소재지 주소"] || "")
																									.trim())
																							|| ((item["주소"] || "")
																									.trim())
																							|| ((item["소재지주소"] || "")
																									.trim())
																							|| ((item["소재지도로명주소"] || "")
																									.trim())
																							|| "주소 정보 없음";
																					return renderMarketItem(
																							name,
																							addr); // ☜ JSP와 동일 마크업
																				})
																		.join(
																				"");
															}

															$(
																	"#initialSeoulList")
																	.hide(); // 초기 리스트 숨김
															$("#resultArea")
																	.html(html)
																	.show(); // 결과 렌더
														},
														error : function(xhr,
																status, error) {
															console.error(
																	"에러:",
																	error);
															alert("검색 중 오류가 발생했습니다.");
														}
													});
										});

						// ----- 리스트/검색결과 공통 클릭(이벤트 위임) -----
						// .js-market-link 클릭 시 data-name & data-addr로 이동
						$(document).off('click', '.js-market-link').on(
								'click',
								'.js-market-link',
								function(e) {
									e.preventDefault();
									const $a = $(this);
									const name = $a.data('name')
											|| ($a.text() || '').trim();
									const addr = $a.data('addr')
											|| ($a.closest('.market-item')
													.find('.market-address')
													.text() || '').trim();
									viewMarketDetail(name, addr);
								});
					});

	// ----- 상세 이동 AJAX & 지도 이동 -----
	function viewMarketDetail(marketName, address) {
		console.groupCollapsed("%cviewMarketDetail ▶",
				"color:#6f42c1;font-weight:bold;");
		console.log("marketName:", marketName);
		console.log("address:", address);
		console.groupEnd();

		$
				.ajax({
					url : "${contextPath}/sijangbajo/sijangSearch/getMarketCoords.do",
					type : "GET",
					dataType : "json",
					data : {
						marketName : marketName,
						addr : address || ""
					},
					success : function(data, status, xhr) {
						console.groupCollapsed("%cgetMarketCoords ✓",
								"color:#198754;font-weight:bold;");
						console.log("HTTP", xhr.status, xhr.statusText);
						console.log("Response:", data);
						console.groupEnd();

						var lat = (data && data.latitude != null) ? Number(data.latitude)
								: NaN;
						var lng = (data && data.longitude != null) ? Number(data.longitude)
								: NaN;
						// x/y로 내려오는 서버를 대비한 폴백
						if (isNaN(lat) || isNaN(lng)) {
							lat = Number(data && (data.y || data.lat));
							lng = Number(data && (data.x || data.lng));
						}

						if (!isNaN(lat) && !isNaN(lng)) {
							if (typeof moveMarker === 'function') {
								moveMarker(lat, lng, marketName);
							} else {
								alert("지도 초기화가 아직 되지 않았습니다.");
							}
						} else {
							if (typeof window.debugNoCoords === 'function') {
								window.debugNoCoords({
									data : data,
									marketName : marketName,
									address : address,
									xhr : xhr
								});
							}
							alert("해당 시장의 위치 정보를 찾을 수 없습니다.");
						}
					},
					error : function(xhr) {
						console.groupCollapsed("%cgetMarketCoords ✗",
								"color:#dc3545;font-weight:bold;");
						console.log("HTTP", xhr.status, xhr.statusText);
						console.log("Response Text:", xhr.responseText);
						console.groupEnd();
						alert("시장 위치 정보를 가져오는 중 오류가 발생했습니다.");
					}
				});
	}
</script>
</head>

<body class="search">
	<form id="searchForm">
		<label for="sido">시도*</label> <select name="sido" id="sido"
			onchange="changeSigungu()" required>
			<option value="">선택</option>
			<option value="서울" selected>서울</option>
			<option value="부산">부산</option>
			<option value="대구">대구</option>
			<option value="인천">인천</option>
			<option value="광주">광주</option>
			<option value="대전">대전</option>
			<option value="울산광역시">울산</option>
			<option value="세종">세종</option>
			<option value="경기도">경기도</option>
			<option value="강원도">강원도</option>
			<option value="충청북도">충청북도</option>
			<option value="충청남도">충청남도</option>
			<option value="전라북도">전라북도</option>
			<option value="전라남도">전라남도</option>
			<option value="경상북도">경상북도</option>
			<option value="경상남도">경상남도</option>
			<option value="제주특별자치도">제주</option>
		</select> <label for="sigungu">시/군/구</label> <select name="sigungu"
			id="sigungu">
			<option value="전체">전체</option>
		</select> <label for="marketName">시장명</label> <input type="text"
			name="marketName" id="marketName" placeholder="시장명 입력">

		<button type="button" id="searchBtn">검색</button>
	</form>

	<!-- 초기 서울 리스트 (JSP 마크업) -->
	<div id="initialSeoulList"
		style="margin-top: 30px; max-height: 400px; overflow-y: auto; border: 1px solid #ccc; padding: 10px; width: 83.5%;">
		<c:forEach var="item" items="${seoulSijangList}">
			<div class="market-item"
				data-market-name="${fn:escapeXml(item['시장명'])}"
				data-market-addr="${fn:escapeXml(item['지번주소'])}">
				<div class="market-name">
					<!-- onclick 제거, 공통 위임 핸들러 사용 -->
					<a href="#" class="js-market-link"
						data-name="<c:out value='${item["시장명"]}' />"
						data-addr="<c:out value='${item["지번주소"]}' />"> <c:out
							value='${item["시장명"]}' />
					</a>
				</div>
				<div class="market-address">
					<c:out value='${item["지번주소"]}' />
				</div>
				<div class="market-empty"></div>
			</div>
		</c:forEach>
	</div>

	<!-- 검색 결과 리스트 (동일한 마크업으로 동적 렌더) -->
	<div id="resultArea"
		style="margin-top: 30px; max-height: 400px; overflow-y: auto; border: 1px solid #ccc; padding: 10px; display: none; width: 83.5%;"></div>

	<%@ include file="searchDetail.jsp"%>
</body>
</html>

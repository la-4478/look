<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<c:set var="pageType" value="sijangbajo" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>전통시장 찾기</title>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=c5158108310c9846cda26b7ac500c0a2c5158108310c9846cda26b7ac500c0a2"></script>
	<style>
	    body {font-family: Arial, sans-serif;padding: 20px;}
	    label {margin-right: 10px;}
	    select, input[type="text"] {padding: 5px; margin-right: 20px;}
	    body {font-family: Arial, sans-serif; padding: 20px; }
	    label {margin-right: 10px;}
	    select, input[type="text"] {padding: 5px; margin-right: 20px;}
	    .market-item {display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid #e0e0e0;}
	    .market-name {width: 33%; font-weight: bold;}
	    .market-name a {text-decoration: none; color: black;}
	    .market-name a:hover {color: #007bff;}
	    .market-address {width: 34%;}
	    .market-empty {width: 33%;}
	</style>
<script>
    function changeSigungu() {
        var sido = document.getElementById("sido").value;
        var sigunguSelect = document.getElementById("sigungu");
        sigunguSelect.innerHTML = ""; // 초기화
        
        var options = [];
        if (sido === "서울") {
            options = ["전체", "종로구", "중구", "용산구", "성동구", "광진구", "동대문구",
                       "중랑구", "성북구", "강북구", "도봉구", "노원구", "은평구", "서대문구",
                       "마포구", "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구",
                       "관악구", "서초구", "강남구", "송파구", "강동구"];
        } else if (sido === "부산") {
            options = ["전체", "중구", "서구", "동구", "영도구", "부산진구", "동래구",
                       "남구", "북구", "해운대구", "사하구", "금정구", "강서구", "연제구",
                       "수영구", "사상구"];
        } else if (sido === "대구") {
            options = ["전체", "중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"];
        } else if (sido === "인천") {
            options = ["전체", "중구", "동구", "미추홀구", "연수구", "남동구", "부평구", "계양구", "서구", "강화군", "옹진군"];
        } else if (sido === "광주") {
            options = ["전체", "동구", "서구", "남구", "북구", "광산구"];
        } else if (sido === "대전") {
            options = ["전체", "동구", "중구", "서구", "유성구", "대덕구"];
        } else if (sido === "울산") {
            options = ["전체", "중구", "남구", "동구", "북구", "울주군"];
        } else if (sido === "세종") {
            options = ["전체", "세종특별자치시"];
        } else if (sido === "경기") {
            options = ["전체", "수원시 장안구", "수원시 권선구", "수원시 팔달구", "수원시 영통구",
                       "성남시 수정구", "성남시 중원구", "성남시 분당구", "고양시 덕양구", "고양시 일산동구",
                       "고양시 일산서구", "용인시 처인구", "용인시 기흥구", "용인시 수지구", "부천시", 
                       "안산시 상록구", "안산시 단원구", "남양주시", "화성시", "평택시", "의정부시"];
        } else if (sido === "강원") {
            options = ["전체", "춘천시", "원주시", "강릉시", "동해시", "태백시", "속초시", "삼척시"];
        } else if (sido === "충북") {
            options = ["전체", "청주시 상당구", "청주시 서원구", "청주시 흥덕구", "청주시 청원구", "충주시", "제천시"];
        } else if (sido === "충남") {
            options = ["전체", "천안시 동남구", "천안시 서북구", "공주시", "보령시", "아산시", "서산시"];
        } else if (sido === "전북") {
            options = ["전체", "전주시 완산구", "전주시 덕진구", "군산시", "익산시", "정읍시"];
        } else if (sido === "전남") {
            options = ["전체", "목포시", "여수시", "순천시", "나주시"];
        } else if (sido === "경북") {
            options = ["전체", "포항시 남구", "포항시 북구", "경주시", "안동시", "김천시"];
        } else if (sido === "경남") {
            options = ["전체", "창원시 의창구", "창원시 성산구", "창원시 마산합포구", "창원시 마산회원구", "창원시 진해구", "진주시"];
        } else if (sido === "제주") {
            options = ["전체", "제주시", "서귀포시"];
        } else {
            options = ["전체"];
        }

        options.forEach(function(opt) {
            var optionElement = document.createElement("option");
            optionElement.value = opt;
            optionElement.text = opt;
            sigunguSelect.add(optionElement);
        });
    }
    
    $(document).ready(function () {
        $("#searchBtn").click(function () {
            const sido = $("#sido").val();
            const sigungu = $("#sigungu").val();
            const marketName = $("#marketName").val();

            if (!sido) {
                alert("시도를 선택하세요.");
                return;
            }

            $.ajax({
                type: "get",
                async: true,
                url: "${contextPath}/sijangbajo/sijangSearch/searchAjax.do",
                dataType: "json",
                data: {
                    sido: sido,
                    sigungu: sigungu,
                    marketName: marketName,
                },
                success: function (data) {
                    console.log("받은 데이터:", data);
                    
                    let html = "";

                    if (!data || data.length === 0) {
                        html = "<p>검색 결과가 없습니다.</p>";
                    } else {
                        data.forEach(function (item) {
                            const marketName = item["시장명"] ? item["시장명"].trim() : "시장명 없음";
                            const address = (item["지번주소"] && item["지번주소"].trim() !== "") ? item["지번주소"].trim() : "주소 정보 없음";
                            console.log(item["지번주소"]);
                            html += '<div style="padding: 8px 0; border-bottom: 1px solid #e0e0e0;">' +
                            '<div><strong>' + marketName + '</strong></div>' +
                            '<div>' + address + '</div>' +
                        '</div>';
                        });
                    }
                    $("#initialSeoulList").hide();
                    $("#resultArea").html(html).show();
                },
                error: function (xhr, status, error) {
                    console.error("에러:", error);
                    alert("검색 중 오류가 발생했습니다.");
                }
            });
        });
    });
	function viewMarketDetail(marketName) {
	    // marketName으로 좌표 조회 (예: 서버에서 좌표를 JSON으로 반환)
	    $.ajax({
	        url: "${contextPath}/sijangbajo/sijangSearch/getMarketCoords.do",  // 좌표를 반환하는 서버 주소
	        type: "GET",
	        data: { marketName: marketName },
	        dataType: "json",
	        success: function(data) {
	            if(data && data.latitude && data.longitude) {
	                // searchDetail.jsp 내에 정의된 moveMarker 함수 호출
	                if(typeof moveMarker === 'function') {
	                    moveMarker(data.latitude, data.longitude);
	                } else {
	                    alert("지도 초기화가 아직 되지 않았습니다.");
	                }
	            } else {
	                alert("해당 시장의 위치 정보를 찾을 수 없습니다.");
	            }
	        },
	        error: function() {
	            alert("시장 위치 정보를 가져오는 중 오류가 발생했습니다.");
	        }
	    });
	}
</script>
</head>
<body>
	<form id="searchForm">
    	<label for="sido">시도*</label>
    	<select name="sido" id="sido" onchange="changeSigungu()" required>
	        <option value="">선택</option>
	        <option value="서울" selected>서울</option>
	        <option value="부산">부산</option>
	        <option value="대구">대구</option>
	        <option value="인천">인천</option>
	        <option value="광주">광주</option>
	        <option value="대전">대전</option>
	        <option value="울산">울산</option>
	        <option value="세종">세종</option>
	        <option value="경기">경기</option>
	        <option value="강원">강원</option>
	        <option value="충북">충북</option>
	        <option value="충남">충남</option>
	        <option value="전북">전북</option>
	        <option value="전남">전남</option>
	        <option value="경북">경북</option>
	        <option value="경남">경남</option>
	        <option value="제주">제주</option>
	    </select>

	    <label for="sigungu">시/군/구</label>
	    <select name="sigungu" id="sigungu">
	        <option value="전체">전체</option>
	    </select>

	    <label for="marketName">시장명</label>
	    <input type="text" name="marketName" id="marketName" placeholder="시장명 입력">
	
	    <button type="button" id="searchBtn">검색</button>
	</form>
	<div id="initialSeoulList" style="margin-top: 30px; max-height: 400px; overflow-y: auto; border: 1px solid #ccc; padding: 10px; width: 900px;">
		<c:forEach var="item" items="${seoulSijangList}">
		    <div class="market-item">
		        <div class="market-name">
					<a href="javascript:void(0);" onclick="viewMarketDetail('<c:out value="${item['시장명']}" />')">
					    <c:out value="${item['시장명']}" />
					</a>
		        </div>
		        <div class="market-address">
		            <c:out value="${item['지번주소']}" />
		        </div>
		        <div class="market-empty"></div>
		    </div>
		</c:forEach>
	</div>
	<div id="resultArea" style="margin-top: 30px; max-height: 400px; overflow-y: auto; border: 1px solid #ccc; padding: 10px; display:none;"></div>
	<%@ include file="searchDetail.jsp" %>
</body>
</html>

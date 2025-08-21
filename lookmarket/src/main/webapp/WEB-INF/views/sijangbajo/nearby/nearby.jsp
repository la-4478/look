<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주변상권 정보 검색</title>
<link rel="stylesheet" href="${contextPath}/resources/css/sijang.css" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
    body.nearby { font-family: Arial, sans-serif; padding: 20px; }
    h2 { margin-bottom: 20px; }
    form label { margin-right: 15px; }
    table, th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    th { background-color: #f5f5f5; }
</style>
</head>
<body class="nearby">

<h2>주변상권 정보 검색</h2>

<form id="searchForm">
    <label>지역 
        <select id="region">
            <option value="">전체</option>
            <option value="서울특별시">서울특별시</option>
            <option value="부산광역시">부산광역시</option>
            <option value="대구광역시">대구광역시</option>
            <option value="인천광역시">인천광역시</option>
            <option value="광주광역시">광주광역시</option>
            <option value="대전광역시">대전광역시</option>
            <option value="울산광역시">울산광역시</option>
            <option value="세종특별자치시">세종특별자치시</option>
            <option value="경기도">경기도</option>
            <option value="강원도">강원도</option>
            <option value="충청북도">충청북도</option>
            <option value="충청남도">충청남도</option>
            <option value="전라북도">전라북도</option>
            <option value="전라남도">전라남도</option>
            <option value="경상북도">경상북도</option>
            <option value="경상남도">경상남도</option>
            <option value="제주특별자치도">제주특별자치도</option>
        </select>
    </label>
    <label>시/군/구</label>
    <select id="category">
    	<option value="">-- 선택 --</option>
        <option value="숙박">숙박시설</option>
        <option value="맛집">맛집</option>
        <option value="관광지">관광지</option>
    </select>
    <label>업종</label> 
    <select id="categoryLcls">
    	<option value="">-- 선택 --</option>
    </select>

    <label>키워드</label>
    	<input type="text" id="keyword" placeholder="예: 호텔, 한식, 놀이공원 등"/>
    <button type="submit">검색</button>
</form>

<div id="resultContainer">
    <table id="resultTable" style="display:none; width:100%; border-collapse:collapse; margin-top:20px;">
        <thead>
            <tr>
                <th>상호명</th>
                <th>업종</th>
                <th>도로명 주소</th>
                <th>위도/경도</th>
            </tr>
        </thead>
        <tbody id="resultBody"></tbody>
    </table>
</div>

<script>
$(function() {
    $('#searchForm').submit(function(e) {
        e.preventDefault();

        const divId = 'ctprvnCd';
        const key = $('#region').val();
        const indsLclsCd = $('#categoryLcls').val();
        const indsMclsCd = $('#categoryMcls').val();
        const indsSclsCd = $('#categoryScls').val();
        const keyword = $('#keyword').val();

        $.ajax({
            url: '${contextPath}/sijangbajo/nearby/searchApi.do',
            type: 'GET',
            data: { divId, key, indsLclsCd, indsMclsCd, indsSclsCd, keyword },
            dataType: 'json',
            success: function(data) {
                const tbody = $('#resultBody');
                tbody.empty();
                if (data.length === 0) {
                    tbody.append('<tr><td colspan="4">검색 결과가 없습니다.</td></tr>');
                } else {
                    data.forEach(function(item) {
                        tbody.append('<tr>' +
                            '<td>' + item.bizesNm + '</td>' +
                            '<td>' + item.indsMclsNm + '</td>' +
                            '<td>' + item.rdnmAdr + '</td>' +
                            '<td>' + item.latitude + ', ' + item.longitude + '</td>' +
                            '</tr>');
                    });
                }
                $('#resultTable').show();
            },
            error: function(xhr, status, err) {
                alert("검색 중 오류 발생: " + err);
            }
        });
    });
});

</script>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 상세 정보</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-4">

    <h2 class="mb-4">회원 상세 정보</h2>

    <table class="table table-bordered">
        <tr>
            <th>아이디</th>
            <td>${member.m_id}</td>
        </tr>
        <tr>
            <th>이름</th>
            <td>${member.m_name}</td>
        </tr>
        <tr>
            <th>성별</th>
            <td>
                <c:choose>
                    <c:when test="${member.m_gender == 1}">남성</c:when>
                    <c:when test="${member.m_gender == 2}">여성</c:when>
                    <c:otherwise>미정</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th>생년월일</th>
            <td><fmt:formatDate value="${member.m_birth}" pattern="yyyy-MM-dd"/></td>
        </tr>
        <tr>
            <th>휴대폰</th>
            <td>${member.m_phone}</td>
        </tr>
        <tr>
            <th>통신사</th>
            <td>
                <c:choose>
                    <c:when test="${member.m_phone_telecom == 1}">SKT</c:when>
                    <c:when test="${member.m_phone_telecom == 2}">KT</c:when>
                    <c:when test="${member.m_phone_telecom == 3}">LG U+</c:when>
                    <c:when test="${member.m_phone_telecom == 11}">알뜰폰(SKT)</c:when>
                    <c:when test="${member.m_phone_telecom == 12}">알뜰폰(KT)</c:when>
                    <c:when test="${member.m_phone_telecom == 13}">알뜰폰(LGU+)</c:when>
                    <c:otherwise>기타</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th>SMS 수신 동의</th>
            <td><c:out value="${member.m_phone_yn == 1 ? '동의' : '비동의'}" /></td>
        </tr>
        <tr>
            <th>이메일</th>
            <td>${member.m_email}</td>
        </tr>
        <tr>
            <th>이메일 수신 동의</th>
            <td><c:out value="${member.m_email_yn == 1 ? '동의' : '비동의'}" /></td>
        </tr>
        <tr>
            <th>주소</th>
            <td>
                [${member.m_zipcode}] ${member.m_road_address}<br>
                (지번: ${member.m_jibun_address})<br>
                나머지: ${member.m_namuji_address}
            </td>
        </tr>
        <tr>
            <th>회원 등급</th>
            <td>
                <c:choose>
                    <c:when test="${member.m_level == 1}">Welcome</c:when>
                    <c:when test="${member.m_level == 2}">Green</c:when>
                    <c:when test="${member.m_level == 3}">Gold</c:when>
                    <c:when test="${member.m_level == 4}">VIP</c:when>
                    <c:otherwise>미지정</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th>회원 구분</th>
            <td>
                <c:choose>
                    <c:when test="${member.m_role == 1}">사용자</c:when>
                    <c:when test="${member.m_role == 2}">관리자</c:when>
                    <c:when test="${member.m_role == 3}">사업자</c:when>
                    <c:otherwise>미지정</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th>가입일</th>
            <td><fmt:formatDate value="${member.m_joindate}" pattern="yyyy-MM-dd"/></td>
        </tr>
        <tr>
            <th>탈퇴일</th>
            <td>
                <c:if test="${not empty member.m_outdate}">
                    <fmt:formatDate value="${member.m_outdate}" pattern="yyyy-MM-dd"/>
                </c:if>
                <c:if test="${empty member.m_outdate}">
                    -
                </c:if>
            </td>
        </tr>
    </table>

    <a href="${contextPath}/views/member/memberlist" class="btn btn-secondary mt-3">목록으로 돌아가기</a>

</body>
</html>

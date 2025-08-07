<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<c:set var="pageType" value="sijangbajo" />
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8" />
	<title>시장봐조 메인화면</title>
  	<style>
    	#carouselExampleAutoplaying .carousel-inner {
      		height: 550px;
    	}
    	#carouselExampleAutoplaying .carousel-item img {
      		object-fit: cover;
      		height: 550px;
    	}
  	</style>
</head>
<body>
	<div class="container mt-4">
		<div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
	  		<div class="carousel-inner">
	    		<div class="carousel-item active">
	      			<img src="${contextPath}/resources/image/main1.jpg"class="d-block w-100" alt="mainimage1" />
	    		</div>
	    		<div class="carousel-item">
	      			<img src="${contextPath}/resources/image/main2.jpg" class="d-block w-100" alt="mainimage2" />
	    		</div>
	  		</div>
	  		<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
	    		<span class="carousel-control-prev-icon" aria-hidden="true"></span>
	    		<span class="visually-hidden">Previous</span>
	  		</button>
	  		<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
	    		<span class="carousel-control-next-icon" aria-hidden="true"></span>
	    		<span class="visually-hidden">Next</span>
	  		</button>
		</div>
	
		<div class="container mt-4">
			<div class="row row-cols-1 row-cols-md-3 g-4">
				<div class="col">
					<div class="card">
						<img src="${contextPath}/resources/image/banner_onnuri.jpg" class="card-img-top" alt="banner_onnuri1" />
					</div>
				</div>
				<div class="col">
					<div class="card">
						<img src="${contextPath}/resources/image/banner_onnuri2.jpg" class="card-img-top" alt="banner_onnuri2" />
					</div>
				</div>
				<div class="col">
					<div class="card">
						<img src="${contextPath}/resources/image/banner_gonggo.jpg" class="card-img-top" alt="banner_gonggo" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
   
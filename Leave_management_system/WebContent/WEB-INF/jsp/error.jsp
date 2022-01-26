<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<style>
.status {font-size: 25px; text-align: center;}
</style>
</head>
	<body>
	<div class="status">
	<c:if test="${message== 'Bye bye!!'}">
	 <div class="alert alert-info">
    <strong>${message}</strong> 
  </div>
	</c:if>
	<c:if test="${message== 'Sorry !!! Please enter valid details'}">
	 <div class="alert alert-danger">
    <strong>${message}</strong> 
  </div>
	</c:if>
	</div>
	<br /><br />
		<jsp:include page="/index.jsp"></jsp:include>
	</body>
</html>
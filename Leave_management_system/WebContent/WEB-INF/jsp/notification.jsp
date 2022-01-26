<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Notifications</title>
<style>
.status {font-size: 70px;}
</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="status">
<c:choose>
	<c:when test="${status== 'Your leave is still pending'}">
		<div class="alert alert-warning">
    <strong>${status}</strong>
  </div>
    </c:when> 
   <c:when test="${status== 'Your leave is approved'}">
		<div class="alert alert-success">
    <strong>${status}</strong>
  </div>
    </c:when> 
    <c:otherwise>
  <div class="alert alert-danger">
    <strong>${status}</strong>
  </div>
  </c:otherwise>
 </c:choose>
 </div>
<br />
<jsp:include page="/logout.jsp"></jsp:include>
</body>
</html>
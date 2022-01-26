<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<style>
.status {font-size: 25px;}
a {font-size: 20px;}
</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
	<body>
	<div class="status">
	<c:choose>
	<c:when test="${message== 'You have no leave requests' or message=='There is no employee without manager'}">
		<div class="alert alert-info">
    <strong>${message}</strong>
  </div>
    </c:when> 
    <c:otherwise>
  <div class="alert alert-success">
    <strong>${message}</strong>
  </div>
  </c:otherwise>
 </c:choose>
  </div><br /><br />
		<jsp:include page="/logout.jsp"></jsp:include>
		<a href="leave_request">Leave Requests</a><br /><br />
		<a href="add_employee">Add Employees</a><br /><br />
	</body>
</html>
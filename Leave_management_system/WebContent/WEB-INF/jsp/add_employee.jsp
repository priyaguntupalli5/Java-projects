<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Employees</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(document).ready(function() {
	$(".empl").click(function() {
		$(this).toggleClass("selected");
	});
	$("button").click(function() {
		var length=$('table').find(".selected").length;
		if(length>1)
			alert("You can move only 1 employee at a time!!");
		else
		{
			var id=$("table").find(".selected").children('td').first().html();
			$("table").find(".selected").remove();
			var input=$("<input>").attr("type","hidden").attr("name","employee").val(id);
			$("form").append(input);
			$("form").submit();
		}
	});
});
</script>
<style>
	body {margin: 10px;}
	* {box-sizing: border-box;}
	.column {float:left; width: 33.33%;padding: 25px;}
	table {border-collapse: collapse;}
	td,th {padding: 5px; font-size: 20px;}
	.empl:hover,button:hover {cursor: pointer;}
	.selected{background-color: lightblue;}
	.container { 
 		 height: 200px;
 		 position: relative;
		}
	.center {
	  margin: 0;
	  position: absolute;
	  top: 70%;
	  left: 50%;
	  -ms-transform: translate(-50%, -50%);
	  transform: translate(-50%, -50%);
	}
	button {font-size: 20px;}
</style>
</head>
<body>
${message} <br />
<jsp:include page="/logout.jsp"></jsp:include>
<div class="row">

	<div class="column">
	
	<table border="1">
	<caption><h1>List of Employees</h1></caption>
	<tr><th>SapId</th><th>First Name</th><th>Last Name</th></tr>
	<c:forEach var="emp" items="${list}">
	<tr class="empl">
	<td>${emp.sap_id}</td>
	<td>${emp.first_name}</td>
	<td>${emp.last_name}</td>
	</tr>	
	</c:forEach>
	</table>
	</div>
	
	<div class="column" id="button">
	<form action="add_employee" method="post">
	<div class="container">
	<div class="center"><button>Move</button></div></div>
	</form>
	</div>
	
	<div class="column" >
	<table border="1">
	<caption><h1>Your Employees</h1></caption>
	<tr><th>SapId</th><th>First Name</th><th>Last Name</th></tr>
	<c:forEach var="emp" items="${employees}">
	<tr>
	<td>${emp.sap_id}</td>
	<td>${emp.first_name}</td>
	<td>${emp.last_name}</td>
	</tr>	
	</c:forEach>
	</table>
	</div>
</div>
</body>
</html>
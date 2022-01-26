<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Leave Requests</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(".empl").click(function(event) {
		$(this).toggleClass("selected");
		var id=$(this).children('td').first().html();
		var input = $("<input>")
        .attr("type", "hidden")
        .attr("name", "leave").val(id);
		$('#form').append(input);
		$("#form").submit();
	});
	
	$('#approve').click(function(event) {
		var id=$("#status").children('input').val();
		var tableRow = $("td").filter(function() {
		    return $(this).text() == id; }).closest("tr").remove();
		var status=$("<input>").attr("type","hidden").attr("name","approve").val($(this).text());
		$('#status').append(status);
		$('#status').submit();
	});
	
	$('#reject').click(function(event) {
		var id=$("#status").children('input').val();
		var tableRow = $("td").filter(function() {
		    return $(this).text() == id; }).closest("tr").remove();
		var status=$("<input>").attr("type","hidden").attr("name","reject").val($(this).text());
		$('#status').append(status);
		$('#status').submit();
	});
	
});
</script>
<style>
	body {margin: 10px;}
	* {box-sizing: border-box;}
	.column {float:left; width: 50%; padding:20px; border: 1px solid black; height: 500px;}
	table {border-collapse: collapse;}
	td,th {padding: 5px;}
	.empl:hover {cursor: pointer;}
	.selected{background-color: lightblue;}
	#details{font-size: 20px;}
	a {cursor: pointer; color: blue; text-decoration: underline;}
</style>
</head>
<body>
${message}<br />
<jsp:include page="/logout.jsp"></jsp:include>
<h1>Leave Requests</h1>
<div class="row">
<div class="column">
<form action="leave_request" method="post" id="form">
	<table border="1">
	<tr><th>SapId</th><th>First Name</th><th>Last Name</th></tr>
	<c:forEach var="emp" items="${list}">	
	<tr class="empl">
	<td>${emp.sap_id}</td>
	<td>${emp.first_name}</td>
	<td>${emp.last_name}</td>
	</tr>
	</c:forEach>
	</table>
</form>
</div>
<div id="details" class="column">
	<c:forEach var="l" items="${leavedetails}">
	SAP ID: ${l.id}<br /><br />
	Leave Type: ${l.leave_type}<br /><br />
	Start Date: ${l.start_date}<br /><br />
	End Date: ${l.end_date}<br /><br />
	Reason: ${l.reason}<br /><br />
	<form id="status" action="leave_request" method="post"><input type="hidden" id="id" name="status" value="${l.id}"><a id="approve">Approve</a> <a id="reject">Reject</a></form>
	</c:forEach>

</div>
</div>
<script></script>
</body>
</html>
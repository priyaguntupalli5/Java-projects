<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
    <html>
<head>
<title>Apply Leave</title>
<style>
tr {height: 45px;}
form {display: inline-block;}
td {font-size: 20px;}
input, select {font-size: 16px; padding: 5px;}
</style>
</head>
<body>
<h1>Apply for a leave</h1> <br />
<jsp:include page="/logout.jsp"></jsp:include>
<form action="apply" method="post">
<table>
<tr>
<td>Leave Type:</td> <td><select name="leave_type">
							<option value="Annual Leave">Annual Leave</option>
							<option value="Restricted Leave">Restricted Leave</option>
							<option value="My Leave">My Leave</option>
											</select></td></tr>		
<tr><td>Start Date:</td><td><input type="date" name="start_date" /></td></tr>
	<tr><td>End Date:</td><td><input type="date" name="end_date" /></td></tr>
	<tr><td>Reason:</td><td><textarea rows="5" cols="25 " name="reason"></textarea></td></tr>
</table>
								
<br /><input type="submit" value="Apply">
</form>

</body>
</html>
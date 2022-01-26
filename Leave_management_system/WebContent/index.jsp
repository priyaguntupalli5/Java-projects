<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Login</title>
<style>
tr {height: 40px;}
h1 {text-align: center;}
.index {text-align: center;}
form {display: inline-block;}
.login {border: 1px solid black; width: 30%; margin: auto;}
</style>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
	<body style="background-color:lightgrey;">
		<h1> Leave Management System</h1><br />
		
		
		<div class="index">
		<div class="login">
		<h3>Existing User: </h3>
		<form action="login" method="post">
		<table>
		<tr><td>Sap_id: </td><td><input type="text" name="sap_id"></td></tr>
		<tr><td>Password: </td><td><input type="password" name="password"></td></tr>
		<tr><td>User Type:</td><td><select name="usertype">
						<option value="manager">Manager</option>
						<option value="employee">Employee</option>
					</select></td></tr>
		</table>
		<br /><input type="submit" value="Login">
		</form>
		</div>
		<br />
		<h3>Not an existing User:</h3>
		<button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Sign Up</button>
		<!-- Modal -->
		<div class="modal fade" id="myModal" role="dialog">
			<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">Register</h4>
				</div>
		<div class="modal-body">
		<form action="register" method="post">
		<table>
		<tr><td>Sap_id: </td><td><input type="text" name="sap_id"></td></tr>
		<tr><td>First Name: </td><td><input type="text" name="first_name"></td></tr>
		<tr><td>Last Name: </td><td><input type="text" name="last_name"></td></tr>
		<tr><td>Password: </td><td><input type="password" name="password"></td></tr>
		<tr><td>Re-enter Password:</td><td><input type="password" name="re-password"></td></tr>
		<tr><td>User Type:</td><td><select name="usertype">
						<option value="manager">Manager</option>
						<option value="employee">Employee</option>
					</select></td></tr>
		</table>
		<br /><input type="submit" value="Sign Up">
		</form>
		</div>
		<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		</div>
		</div>
		</div>
		</div>
		</div>
	</body>
</html>

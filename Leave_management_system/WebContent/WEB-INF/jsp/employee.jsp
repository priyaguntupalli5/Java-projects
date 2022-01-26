<html>
<head>
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
</head>
	<body>
	<div class="status">
		<div class="alert alert-success">
    	<strong>${message}</strong>
 	 	</div>
 	 	</div>
  <br /><br />
		<jsp:include page="/logout.jsp"></jsp:include>
		<a href="apply_leave">Apply for a leave</a><br /><br />
		<a href="notification">Go to notifications</a>
	</body>
</html>
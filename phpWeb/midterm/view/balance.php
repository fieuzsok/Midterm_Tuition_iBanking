<?php
session_start();
if(isset($_REQUEST['username'])) {
	
	$_SESSION['username'] = $_REQUEST['username'];
}
else {
	header('Location: index.php');
}	
?>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Thanh toán học phí</title>
	<link href="../css/bootstrap.min.css" rel="stylesheet">
	<link href="../css/font-awesome.min.css" rel="stylesheet">
	<link href="../css/datepicker3.css" rel="stylesheet">
	<link href="../css/styles.css" rel="stylesheet">
	
	<!--Custom Font-->
	<link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">
	<!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="../js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	
	<form role="form">
		<div class="form-group">
			<label>Mã số sinh viên:</label>
			<input class="form-control" name="id" type="text">
		</div>
		<div class="form-group">
			<button class="btn btn-success" onclick="">Số tiền cần nộp</button>
			<input type="number" class="form-control" name="">
		</div>
		
		<br>
	</form>				



	
<script src="../js/jquery-1.11.1.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
	<script src="../js/chart.min.js"></script>
	<script src="../js/chart-data.js"></script>
	<script src="../js/easypiechart.js"></script>
	<script src="../js/easypiechart-data.js"></script>
	<script src="../js/bootstrap-datepicker.js"></script>
	<script src="../js/custom.js"></script>
	
</body>
</html>


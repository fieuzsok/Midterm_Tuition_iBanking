<?php
session_start();
if(isset($_SESSION['username'])) {
    header('Location: homepage.php');
}  
?>


<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Đăng nhập</title>
	<link href="../css/bootstrap.min.css" rel="stylesheet">
	<link href="../css/datepicker3.css" rel="stylesheet">
	<link href="../css/styles.css" rel="stylesheet">

    <script src="../js/respond.min.js"></script>
    <script src="../js/jquery-1.11.1.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src = "../lib/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.8.3.js"> </script>
    <script src="../lib/jquery-3.3.1.min.js"> </script>


        <script>
        function showError(type) {
            $(type).fadeIn(300);
            setTimeout(function () {
                $(type).fadeOut(5000);
            },3000);
        }

    </script>
</head>
<body>

		<div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-4">
			<h2 class="text-center"><b>Welcome to IT TDT iBanking</b></h2>
			<div class="login-panel panel panel-default">
				<div class="panel-heading text-center"><b>Đăng nhập</b></div>
				<div class="panel-body">
					<form  id="Login" method = "post">
						<fieldset>
							<div class="form-group">
								<input class="form-control" name="username" placeholder="Tài khoản" type="text" autofocus>
							</div>
							<div class="form-group">
								<input class="form-control" name="password" placeholder="Mật khẩu" type="password">
							</div>
							<div class="form-group">
								<button type="submit" class="btn btn-primary">Đăng nhập</button>
							</div>
                            <div class="alert alert-danger expired" id="Err" style="text-align: center; display: none">
                                <span>Tài khoản hoặc mật khẩu không hợp lệ.</span>
                            </div>
						</fieldset>
					</form>
				</div>
			</div>
  	</div>



<?php


if (isset($_POST['username']) && isset($_POST['password']))
{
    // Send username/password to Tomcat server for authenticating
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/iBanking/rest/services/sign-in-secure-v2/");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded')); 
    // In Java: @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    $data = array('username'=>$_POST['username'],'password'=>$_POST['password']);
    curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));

    $output = curl_exec($ch);
    $info = curl_getinfo($ch);
    curl_close($ch);

    //If the server returns TRUE, then print something
    if($output == "true")
    {
        $username = $_POST["username"];
        $password = $_POST["password"];
        echo "Login successfully<br>";
        //tiến hành lưu tên đăng nhập vào session để tiện xử lý sau này

        $_SESSION['username'] = $username;
        // Thực thi hành động sau khi lưu thông tin vào session
        // ở đây mình tiến hành chuyển hướng trang web tới một trang gọi là index.php
        echo $_SESSION['username'];
        header('Location: homepage.php');
    }
    else
    {
        echo '<script>showError("#Err")</script>';
    }

}

?>
</body>
</html>

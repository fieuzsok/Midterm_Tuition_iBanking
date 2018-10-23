<?php
session_start();
if(!isset($_SESSION['username'])) {
    header('Location: index.php');
}

if(isset($_SESSION['haveOTP']) && $_SESSION['haveOTP'] == "true" ) {
    header('Location: verifyOTP.php');
}

if(!isset($_SESSION['student_id'])){
    $_SESSION['student_id'] = "";
    $_SESSION['tuition']  =  "";
}
?>

<?php
include('../Controller/homepageControl.php');
$account = loadUserInfo();


if(isset($_REQUEST['submit'])){
    if($_REQUEST['submit'] == "xacnhan"){
        if(isset($_REQUEST['studentid']) && isset($_REQUEST['tuitionFee'])){
            $studentID = $_REQUEST['studentid'];
            $username = $_SESSION['username'];
            checkInfoForm($account,$_REQUEST['tuitionFee'],$studentID );
        }

    }
}
$account = loadUserInfo();

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
    <script src="../js/jquery-1.11.1.min.js"></script>

	

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
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
		
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">THANH TOÁN HỌC PHÍ</h1>
			</div>
			<a class="btn btn-danger" href="sign-out.php">Sign-out</a>
		</div><!--/.row-->
		
		<div class="row">

			<div class="col-lg-9">
				<form method="POST" id = "Pay" >
					<div class="panel panel-default">
						<div class="panel-heading">Người nộp tiền</div>
						<div class="panel-body">
							<div class="col-md-2"></div>
							<div class="col-md-8">
								<div class="form-group">
									<label>Họ và tên:</label>
									<input class="form-control" value="<?= $account['name'] ?>" readonly>
								</div>
								<div class="form-group">
									<label>Số điện thoại:</label>
									<input class="form-control" value="0<?= $account['phone'] ?>" readonly>
								</div>
								<div class="form-group">
									<label>Email:</label>
									<input class="form-control" value="<?= $account['email'] ?>" readonly>
								</div>
							</div>
							<div class="col-md-2"></div>
						</div>
					</div><!-- /.panel-->
					
					<div class="panel panel-default">
						<div class="panel-heading">Thông tin học phí</div>
						<div class="panel-body">
							<div class="col-md-2"></div>
							<div class="col-md-8">

									<div class="form-group">
										<label>Mã số sinh viên:</label>
										<input class="form-control " id="student_id" name="studentid" type="text" value = "<?= $_SESSION['student_id']?>" required >
									</div>
                                    <div class="form-group" style="text-align: center; display: none">
                                         <input class="form-control " id="haveOTP" name="haveOTP" type="text" >
                                    </div>
									<div class="form-group">
										<label>Số tiền cần nộp</label>
										<input type="text" class="form-control currency" id = "mustPay" name="" value = "<?=$_SESSION['tuition'] ?>"readonly >
                                        <input id="tuitionFee" name="tuitionFee" type="text" value = "" style="text-align: center; display: none">

									</div>
									
									<br>

							</div>
							<div class="col-md-2"></div>
						</div>
					</div><!-- /.panel-->

					<div class="panel panel-default">
						<div class="panel-heading">Thông tin thanh toán</div>
						<div class="panel-body">
							<div class="col-md-2"></div>
							<div class="col-md-8">

									<div class="form-group">
										<label>Số dư khả dụng:</label>
										<input type="text" class="form-control currency" id="balance" readonly>
									</div>
									<div class="form-group">
										<label>Số tiền cần chuyển:</label>
										<input type="text" class="form-control currency" name="money" id = "tienCanChuyen" step="1000"  value = "<?=$_SESSION['tuition'] ?>" readonly>

                                        <div class="alert alert-danger" id="sodukhongdu" style="text-align: center; display: none" >
                                            <span >Số dư không đủ để thực hiện giao dịch. </span>
                                        </div>
                                        <div class="alert alert-danger" id = "tienkhonghople"style="text-align: center; display: none" >
                                            <span >So tien giao dich khong hop le. </span>
                                        </div>
                                        <div class="alert alert-danger" id = "dacoloixayra"style="text-align: center; display: none" >
                                            <span  >Da co loi xay ra </span>
                                        </div>
                                        <div class="alert alert-danger" id = "expired"style="text-align: center; display: none" >
                                            <span >Ma OTP cua ban da het han, vui long thuc hien lai giao dich.</span>
                                        </div>
                                        <div class="alert alert-danger expired" id="expired" style="text-align: center; display: none">
                                            <span>Mã OTP đã hết hạn.</span>
                                        </div>

									</div>

							</div>
							<div class="col-md-2"></div>
						</div>
					</div><!-- /.panel-->


					<div class="panel panel-default">
						<div class="panel-heading">Xác nhận giao dịch</div>
						<div class="panel-body">
							<div class="col-md-2"></div>
							<div class="col-md-8">
								<div class="form-group">
									<input type="hidden" value="<?= $_SESSION['username'] ?>">
									<button type="submit" class="btn btn-primary" name = "submit" id="submit" value="" onclick="requestSubmit()" ">Xác nhận</button>
								</div>
							</div>
							<div class="col-md-2"></div>
						</div>
					</div><!-- /.panel-->

				</form>

			</div><!-- /.col-->
		</div><!-- /.row -->
		

		<br>


	</div><!--/.main-->
    <?php

    if(isset($_SESSION['err1'])){
        checkError();
        unset($_SESSION['student_id']);
        unset($_SESSION['haveOTP']);
    }

    ?>
<script type="text/javascript">
        var fee = 0;
        var Student_id ;
        $("#student_id").on("keyup", function() {
            Student_id = $("#student_id").val();
        $.ajax({
            type: 'GET',
            url: "http://localhost:8080/iBanking/rest/services/pay/" + Student_id,
            success: function(response) {
                fee = response;
                if(fee > 0 || fee == 0) {
                    document.getElementById("mustPay").value = formatNumber(fee, '.', ',');
                    document.getElementById("tienCanChuyen").value = formatNumber(fee, '.', ',');
                    document.getElementById("tuitionFee").value = fee;

                } else if(fee < 0){
                    document.getElementById("mustPay").value = "Số tiền không hợp lệ";
                    document.getElementById("tienCanChuyen").value = "";
                }
                else if(fee == null){
                    document.getElementById("mustPay").value = "Mã số sinh vên không hợp lệ";
                    document.getElementById("tienCanChuyen").value = "";
                }
                console.log("Data type: " + (typeof response));
                console.log("Data: " + response);
               // console.log("Application name: " + response.name);
        }});
    });
</script>

    <script>
        function requestSubmit(){
            document.getElementById("submit").value = "xacnhan";
            jQuery.ajax({
                type: 'POST',
                url: "homepage.php"
            });
        }
    </script>

    <script>
        document.getElementById("balance").value = formatNumber(<?= $account['balance']?>, '.', ',');
        function formatNumber(nStr, decSeperate, groupSeperate) {
            nStr += '';
            x = nStr.split(decSeperate);
            x1 = x[0];
            x2 = x.length > 1 ? '.' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + groupSeperate + '$2');
            }
            return x1 + x2;
        }
    </script>


	
<script src="../js/jquery-1.11.1.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
    <script src = "../lib/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.8.3.js">

    </script>

</body>
</html>


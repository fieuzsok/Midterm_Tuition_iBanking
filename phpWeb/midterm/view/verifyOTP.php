<?php
session_start();
if(!isset($_SESSION['username'])) {
    header('Location: index.php');
}
include_once('../Controller/otpControl.php');
?>
<?php
if(isset($_POST['submit']) && isset($_POST['otpCode']) ){
    if($_POST['submit'] == "xacnhan"){
        $otpCode = $_POST['otpCode'];
        if($otpCode != ""){
            otpFormProcess($otpCode);
        }
    }
}
?>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>

    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/font-awesome.min.css" rel="stylesheet">
    <link href="../css/datepicker3.css" rel="stylesheet">
    <link href="../css/styles.css" rel="stylesheet">
    <script src="../js/jquery-1.11.1.min.js"></script>
    <style>
        .be-detail-header { border-bottom: 1px solid #edeff2; margin-bottom: 50px; }
</style>
</head>
<body>
<script>
    function showError(type) {
        $(type).fadeIn(500);
        setTimeout(function () {
            $(type).fadeOut(5000);
        },3000);
    }
</script>
<div class="container be-detail-container">
    <div class="row">
        <div class="col-lg-9">
            <form method="POST" id = "Pay" action="">
                <div class="panel panel-default">
                    <div class="panel-heading">Nhập mã OTP từ email</div>
                    <div class="panel-body">
                        <div class="col-md-2"></div>
                        <div class="col-md-8">
                                <div class="form-group">
                                    <label>OTP:</label>
                                    <input class="form-control " id="otpCode" name = "otpCode" type="number" value="" required >
                                </div>
                                <div class="form-group">
                                    <div class="alert alert-danger invalid" id="invalid"style="text-align: center; display: none">
                                        <span>Mã OTP không hợp lệ. Vui lòng thử lại.</span>
                                    </div>
                                    <div class="alert alert-danger expired" id="expired" style="text-align: center; display: none">
                                        <span>Mã OTP đã hết hạn. Vui lòng quay lại trang Chuyển tiền..</span>
                                    </div>
                                </div>
                                <br>
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
                                <button type="submit" class="btn btn-primary" id="submit" name ="submit" value="" onclick="requestSubmit()">Xác nhận</button>
                            </div>
                        </div>
                        <div class="col-md-2"></div>
                    </div>
                </div><!-- /.panel-->
            </form>

        </div><!-- /.col-->

    </div>
</div>
<?php
    checkError();
?>



<script>
    var studentID = <?=$_SESSION['student_id']?>;
    function paymentProcess(){
        jQuery.ajax({
            type: 'GET',
            url: "http://localhost:8080/iBanking/rest/services/paymentHandler/" + username + "/" + studentID,
            success: function(response) {
                var user = response["user"];
                var studID = response["Student ID"];
                var isUpdateBalance = response["isUpdateBalance"];
                var isUpdateTuitionFee = response["isUpdateTuitionFee"];

                if (user==username && studID == studentID && isUpdateBalance == true && isUpdateTuitionFee == true ) {
                    emailSucceed();
                } else {

                }
                console.log("Data type: " + (typeof  response));
                console.log("Application name: " + response.name);
            }
        });
    }

</script>
<script>

    function emailSucceed(){
        jQuery.ajax({
            type: 'GET',
            url: "http://localhost:8080/iBanking/rest/services/send-email-succeed/" + username + "/" + studentID,
            success: function(response) {
                isSent = response;
                if (isSent==true) {

                    top.location.href="homepage.php";//redirection"";
                } else {

                }
                console.log("Data type: " + (typeof  response));
                console.log("Application name: " + response.name);
            }
        });
    }

</script>

<script>
    function requestSubmit(){
        document.getElementById("submit").value = "xacnhan";
        jQuery.ajax({
            type: 'POST',
            url: "verifyOTP.php"
        });
    }
</script>
<script src="../js/jquery-1.11.1.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src = "../lib/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="//code.jquery.com/jquery-1.8.3.js"></script>

</body>
</html>
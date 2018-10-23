<?php
if(!isset($_SESSION['username']) ||  !isset($_SESSION['student_id']) || !isset($_SESSION['haveOTP'])) {
    header('Location: index.php');
}

function callService($url,$data){
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded'));
    // In Java: @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    //$data = array('username'=>$_GET['username'],'studentID'=>$_GET['studentID'],'fee'=>$_GET['fee']);
    curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));

    $output = curl_exec($ch);
    $info = curl_getinfo($ch);
    curl_close($ch);

    return $output;
}
function valid_otp($data){
    $url =  "http://localhost:8080/iBanking/rest/services/validOTP/";
    return callService($url,$data);
}
function callPaymentHandleFromService($data){
    $url=  "http://localhost:8080/iBanking/rest/services/paymentHandler/";
    return callService($url,$data);
}
function sendEmailAnnounce($data){
    $url=  "http://localhost:8080/iBanking/rest/services/send-email-succeed/";
    return callService($url,$data);
}
function paymentProcess($data){
    $output = callPaymentHandleFromService($data); // xu ly update balance va update no hoc phi
    $response =  json_decode($output,true);
    $_POST['username'] = $response["user"];
    $_POST['studentID'] = $response["Student ID"];
    $isUpdateBalance = $response["isUpdateBalance"];
    $isUpdateTuitionFee = $response["isUpdateTuitionFee"];
     if ($isUpdateBalance == true && $isUpdateTuitionFee == true ) {
         $data = array('username'=>$_POST['username'],'studentID'=>$_POST['studentID']);
         $responseEmail = sendEmailAnnounce($data);
         if($responseEmail == true){
             unset($_SESSION['erro']);
             unset($_SESSION['student_id']);
             unset( $_SESSION['haveOTP']);
             header('Location: homepage.php');
         }
         else{
             unset($_SESSION['student_id']);
             unset( $_SESSION['haveOTP']);
             header('Location: homepage.php');
         }
     }
    }

function otpFormProcess($otpCode){
    if(isset($_SESSION['username']) && isset($_SESSION['student_id'])){

        $user =$_SESSION['username'];
        $_POST['student_id'] = $_SESSION['student_id'];
        $_POST['otpCode'] = $otpCode;
        $_POST['username'] = $_SESSION['username'];
        $data = array('username'=>$_POST['username'],'otpCode'=>$_POST['otpCode']);
        $output = valid_otp($data);
        $validOTP = json_decode($output,true);

        $isExpired = $validOTP['isExpired'];
        $isValidOTP = $validOTP['isValidOTP'];
        if($isExpired == true && $isValidOTP  == true ){
            $dataPayment = array('username'=>$_POST['username'],'student_id'=> $_POST['student_id']);
            paymentProcess($dataPayment);
        }
        if ($isExpired == true && $isValidOTP  == false) {
                $_SESSION['erro'] = "#invalid";
            }
        if ($isExpired == false && $isValidOTP == false) {
                $_SESSION['erro1'] = "#expired";
                unset($_SESSION['otpCode']);
                unset($_SESSION['haveOTP']);
                header('Location: homepage.php');
            }

        }


}

function checkError(){
    if(isset($_SESSION['erro'])){
        $err = $_SESSION['erro'];
        echo '<script>showError("'.$err .'")</script>';
        unset($_SESSION['erro']);
    }
}
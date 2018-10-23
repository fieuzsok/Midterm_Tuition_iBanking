<?php
if(!isset($_SESSION['username'])) {
    header('Location: index.php');
}

function loadUserInfo() {
    $url = "http://localhost:8080/iBanking/rest/services/info/" . $_SESSION['username'];
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    $data = curl_exec($ch);
    curl_close($ch);
    $account = json_decode($data, true);
    return $account;
}
function validCheckBalance($balance, $mustPay) {
    if($mustPay == 0 || $mustPay < 0  ){

        $_SESSION['err1'] = "#tienkhonghople";
        return false;
    }
    else if ($balance > 0 && $balance >= $mustPay){

        return true;
    }
    else{
        $_SESSION['err1'] = "#sodukhongdu";
        return false;
    }
}

function loadTuitionPay($Student_id){
    $url = "http://localhost:8080/iBanking/rest/services/pay/" + $Student_id;
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    $data = curl_exec($ch);
    curl_close($ch);
    $tuitionFee = $data;
    return $tuitionFee;
}

function sendMailOTP($data){
    // Send username/password to Tomcat server for authenticating
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/iBanking/rest/services/send-email/");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded'));
    // In Java: @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));

    $output = curl_exec($ch);
    $info = curl_getinfo($ch);
    curl_close($ch);

    return $output;
    //If the server returns TRUE, then print something

}

function checkInfoForm($account,$fee,$studentid){
    if(isset($_SESSION['username'])){
        $username = $account['username'];
        $balance = $account['balance'];
        $isValidBalance =  validCheckBalance($balance,$fee);
        if($isValidBalance == false){
            $_SESSION['haveOTP'] = 'false1';
            $_SESSION['student_id'] = $studentid;
            $_SESSION['tuition'] = $fee;
        }
       if($isValidBalance == true){
            $_POST['studentID'] = $studentid;
            $_POST['username'] = $_SESSION['username'];
            $data = array('username'=>$_POST['username'],'studentID'=>$_POST['studentID']);
            $isSent = sendMailOTP($data);
            if($isSent == "true")
            {
                $_SESSION['haveOTP'] = "true";
                $_SESSION['student_id'] = $studentid;
                header('Location: verifyOTP.php');
            }
            else
            {
                $_SESSION['haveOTP'] = "false3";
                $_SESSION['student_id'] = $studentid;
                $_SESSION['err1'] = "#dacoloixayra";

            }
        }

    }
}

function checkError(){
    if(isset($_SESSION['err1'])){
        $err1 = $_SESSION['err1'];
        echo '<script>showError("'.$err1 .'")</script>';
        unset($_SESSION['err1']);
 }
}


?>
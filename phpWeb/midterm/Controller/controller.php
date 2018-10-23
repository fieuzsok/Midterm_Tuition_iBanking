<?php
session_start();
if(!isset($_SESSION['username'])) {
    header('Location: index.php');
}
?>

<?php
/**
 * Created by PhpStorm.
 * User: Asus
 * Date: 10/18/2018
 * Time: 10:14 AM
 */
header('Content-Type: application/json');

$aResult = array();

if( !isset($_POST['functionname']) ) { $aResult['error'] = 'No function name!'; }

if( !isset($_POST['arguments']) ) { $aResult['error'] = 'No function arguments!'; }

if( !isset($aResult['error']) ) {

    switch($_POST['functionname']) {
        case 'validCheckBalance':
            if( !is_array($_POST['arguments']) || (count($_POST['arguments']) != 2) ) {
                $aResult['error'] = 'Error in arguments!';
            }
            else {
                $aResult['result'] = validCheckBalance(intval($_POST['arguments'][0]), intval($_POST['arguments'][1]));
            }
            break;
        case 'checkOTP':
            if( !is_array($_POST['arguments']) || (count($_POST['arguments']) != 2) ) {
                $aResult['error'] = 'Error in arguments!';
            }
            else {
                $aResult['result'] = sendMail(intval($_POST['arguments'][0]), intval($_POST['arguments'][1]));
            }
            break;
        default:
            $aResult['error'] = 'Not found function '.$_POST['functionname'].'!';
            break;
    }

}

echo json_encode($aResult);
function validCheckBalance($balance, $mustPay) {
    //&& $balance > 0 && $balance >= $mustPay
    if($mustPay == 0 || $mustPay < 0  ){
        $_SESSION['error'] = "#tienkhonghople";
         return false;
    }
    else if ($balance > 0 && $balance >= $mustPay){
        $_SESSION['haveOTP'] = "true";
        return true;
    }
    else{
        $_SESSION['error'] = "#sodukhongdu";
        return false;
    }

}

function CheckOTP($otpInput , $otpDB){
    if(isset($_SESSION['username'])){
        $user = $_SESSION['username'];
        $this->session->set_tempdata($otpInput,$otpInput,15000);

    }
}


?>
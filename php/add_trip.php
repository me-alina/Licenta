<?php
  require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 //var_dump($_POST);
 

if( isset($_POST['uid'] ) && isset($_POST['time'])&& isset($_POST['fromplace']) && isset($_POST['toplace']) && isset($_POST['seats'])) {
    $uid=$_POST['uid'];
    $time=$_POST['time'];
	$fromplace=$_POST['fromplace'];
	$toplace=$_POST['toplace'];
	$seats=intval($_POST['seats']);
echo $seats;
   // $result = mysql_query("update users set name='$name', email='$email', phone='$phone', about='$about' where unique_id='$uid' ") or die(mysql_error());

    $response = $db->addTrip ($uid, $time, $fromplace, $toplace, $seats);

}
else {
	$response["error"] = TRUE;
    $response["message"] = "Failed To Update - parameters not set";  
}
echo json_encode($response);
?>
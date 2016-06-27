<?php
  require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 //var_dump($_POST);
 
  if( isset($_POST['uid'] ) && isset($_POST['rate']) ) {
    $driver_uid=$_POST['uid'];
    $rate=$_POST['rate'];
	
    $response = $db->setRating ($driver_uid, $rate);
	echo json_encode(array("response"=>$response));
  }
?>

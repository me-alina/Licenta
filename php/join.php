<?php
  require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 //var_dump($_POST);
 
  if( isset($_POST['driver_uid'] ) && isset($_POST['passenger_uid'])&& isset($_POST['trip_uid']) ) {
    $driver_uid=$_POST['driver_uid'];
    $passenger_uid=$_POST['passenger_uid'];
	$trip_uid=$_POST['trip_uid'];
	
    $response = $db->addCar ($driver_uid, $passenger_uid, $trip_uid);
	echo json_encode(array("response"=>$response));
  }
?>

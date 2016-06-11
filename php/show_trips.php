<?php
  require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 //var_dump($_POST);
 
  
    $response = $db->showTrips ();
	echo json_encode(array("response"=>$response));



?>
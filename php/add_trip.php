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
	$date;
	$time_offset=3600;
	$time_format = date("H:i", time() + $time_offset);     	
	
	if($time == "10 min")
		$time_offset=600;
	else 
		if ($time == "30 min")
			$time_offset=1800;
		else 
			if ($time == "1 h")
				$time_offset=3600;
			else $time_format = $time;
			
		//	var_dump($time) ;
	if(isset($_POST['date']))
		$date = $_POST['date'];
	else
		$date = date("Y-m-d");   
    $response = $db->addTrip ($uid, $time_format, $fromplace, $toplace, $seats, $date);

}
else {
	$response["error"] = TRUE;
    $response["message"] = "Failed To Update - parameters not set";  
}
echo json_encode($response);
?>
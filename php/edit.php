<?php
  require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 //var_dump($_POST);
 
if( isset($_POST['uid'] ) && isset($_POST['name'])&& isset($_POST['email']) && isset($_POST['phone']) && isset($_POST['about'])) {
    $uid=$_POST['uid'];
    $name=$_POST['name'];
	$email=$_POST['email'];
	$phone=$_POST['phone'];
	$about=$_POST['about'];

   // $result = mysql_query("update users set name='$name', email='$email', phone='$phone', about='$about' where unique_id='$uid' ") or die(mysql_error());

    $response = $db->updateUser ($uid, $name, $email, $phone, $about);

}
else {
	$response["error"] = TRUE;
    $response["message"] = "Failed To Update - parameters not set";  
}
echo json_encode($response);
?>
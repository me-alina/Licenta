<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 //var_dump($_POST);
 if (isset($_POST['uid'])) {
 
    // receiving the post params
    $uid = $_POST['uid'];
	//$gravatar = $db->get_gravatar($email);
 
    // get the user by email and password
    $user = $db->getUserProfileById($uid);
 
    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
		$response["user"]["phone"] = $user["phone"];
		$response["user"]["city"] = $user["city"];
		$response["user"]["about"] = $user["about"];
		$response["user"]["rating_cnt"] = $user["rating_cnt"];
		$response["user"]["rating_val"] = $user["rating_val"];
		$response["user"]["avatar"]=$user["gravatar"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Profile couldn't be displayed. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter email is missing!";
    echo json_encode($response);
}
?>
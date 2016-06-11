<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['uid'])) {
 
    // receiving the post params
    $uid = $_POST['uid'];
   
 
    // get the user by email and password
    $user = $db->getUserById($uid);
 
    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "User that posted trip is not found";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter uid is missing!";
    echo json_encode($response);
}
?>
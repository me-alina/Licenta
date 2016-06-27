<?php
  require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 

	$time_format = date("H:i", time() + 600);     		
			
	$stmt = $db->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at, phone, city, about, rating_cnt, rating_val) VALUES(?, ?, ?, ?, ?, NOW(),  '07', 'Timisoara', '', '0', '0')");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
   
echo json_encode($time_format);
?>
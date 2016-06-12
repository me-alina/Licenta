<?php
  require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 

	$time_format = date("H:i", time() + 600);     		
			
	
   
echo json_encode($time_format);
?>
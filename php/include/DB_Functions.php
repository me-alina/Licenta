<?php
 
 
class DB_Functions {
 
    private $conn;
     // public $conn;
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at, phone, city, about, rating_cnt, rating_val) VALUES(?, ?, ?, ?, ?, NOW(),  '07', 'Timisoara', '', '0', '0')");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
	  public function updateUser($uid, $name, $email, $phone, $about) {
        
        $stmt = $this->conn->prepare("UPDATE users SET name='$name', email='$email', phone='$phone', about='$about', updated_at= NOW() WHERE unique_id='$uid' ");
        //$stmt->bind_param("sssss", $uid, $name, $email, $encrypted_password, $salt);
		
        $result = $stmt->execute();
       	   
 
        // check for successful store
        $rows = $stmt->affected_rows;

    if($rows>=0){
         $response["error"] = FALSE;
         $response["message"] = "Updated Sucessfully.";
     }
    else{
        $response["error"] = TRUE;
        $response["message"] = "Failed To Update.";  
     }  
		$stmt->close();
        return $response;  
    }
 public function addTrip ($uid, $time, $fromplace, $toplace, $seats, $date){
	  
	   $stmt = $this->conn->prepare("INSERT INTO ruta (owner_id, time, date, fromplace, toplace, seats) VALUES(?, ?, ?, ?, ?, ?)");
	   $stmt->bind_param('sssssd', $uid, $time, $date, $fromplace, $toplace, $seats);       
	   $result = $stmt->execute();
       
 
        // check for successful store
        $rows = $stmt->affected_rows;

    if($rows>=0){
         $response["error"] = FALSE;
         $response["message"] = "Added Sucessfully.";
     }
    else{
        $response["error"] = TRUE;
        $response["message"] = "Failed To Add.";  
     }  
		$stmt->close();
        return $response;  
	
 }
 
 public function addCar ($driver_uid, $passenger_uid, $trip_uid){
	  // vezi daca exista solicitari pentru ruta respectiva deja
	  $rows = -1;
	  $stm = $this->conn->prepare("SELECT * FROM masina WHERE ruta_id = ?");
      $stm->bind_param("d", $trip_uid);
        if ($stm->execute()) {
            $trip = $stm->get_result()->fetch_assoc();
            $stm->close();
		}
		if ($trip==false){
		
			$stmt = $this->conn->prepare("INSERT INTO masina (owner_id, pass_id1, ruta_id, updated_at) VALUES(?, ?, ?, NOW())");
			
			$stmt->bind_param('sss', $driver_uid, $passenger_uid, $trip_uid);       
			$result = $stmt->execute();
			$rows = $stmt->affected_rows;
			// should decrease free seats in trip
			$stmt->close();
		}
		else 
		{
			
			if (is_null($trip['pass_id4']) && is_null($trip['pass_id3']) && is_null($trip['pass_id2'])) {
				
				$stmt = $this->conn->prepare("UPDATE masina SET pass_id2 = '$passenger_uid', updated_at= NOW() where ruta_id='$trip_uid' ");
				$result = $stmt->execute();
				$rows = $stmt->affected_rows;
				$stmt->close();
			}
				else if (is_null($trip['pass_id4'])&& is_null($trip['pass_id3'])) {
					
					$stmt = $this->conn->prepare("UPDATE masina SET pass_id3 = '$passenger_uid', updated_at= NOW() where ruta_id='$trip_uid' ");
					$result = $stmt->execute();
					$rows = $stmt->affected_rows;
					$stmt->close();
				}
			else if (is_null($trip['pass_id4'])) {
				
				$stmt = $this->conn->prepare("UPDATE masina SET pass_id4 = '$passenger_uid', updated_at= NOW() where ruta_id='$trip_uid' ");
				$result = $stmt->execute();
				$rows = $stmt->affected_rows;
				$stmt->close();
			}
			else 
			{$response["error"] = TRUE;
			$response["message"] = "Car is already full";
			return $response;  
			}
						
      //  $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
		}
    if($rows>=0){
         $response["error"] = FALSE;
         $response["message"] = "Added Sucessfully.";
     }
    else{
        $response["error"] = TRUE;
        $response["message"] = "Failed To Add.";  
     }  
		
        return $response;  
	
 }
 
 public function showTrips (){
	 
	$sql = "SELECT * from ruta";
 
	$res = mysqli_query($this->conn, $sql);
 
	$result = array();
 
	while($row = mysqli_fetch_array($res)){
		$name = self::getUserById($row[1]);
		if ( date('Y-m-d') <= $row[3] || (date('H:i', time() + 3600) < $row[2] && date('Y-m-d') == $row[3])  )
			array_push($result, array('id'=>$row[0], 'name'=>$name, 'uid'=>$row[1], 'depart'=>$row[2], 'date'=>$row[3], 'origin'=>$row[4], 'destination'=>$row[5], 'seats'=>$row[6] ));
	}

return $result;
	 
 }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
 
 public function getUserData($email) {
        $stmt = $this->conn->prepare("SELECT * from users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $result = $stmt->execute();
        
 
        // check for successful store
        if ($result) {
           
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
			$stmt->close();
            return false;
        }
    }
	
	public function getUserById($uid) {
        $stmt = $this->conn->prepare("SELECT * from users WHERE unique_id = ?");
        $stmt->bind_param("s", $uid);
        $result = $stmt->execute();
       
        // check for successful store
        if ($result) {
           
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user["name"];
        } else {
			$stmt->close();
            return false;
        }
    }
	
	
	public function getUserProfileById($uid) {
        $stmt = $this->conn->prepare("SELECT * from users WHERE unique_id = ?");
        $stmt->bind_param("s", $uid);
        $result = $stmt->execute();
       
        // check for successful store
        if ($result) {
           
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
			
			$user["gravatar"] = $this->get_gravatar($user["email"]);
 
            return $user;
        } else {
			$stmt->close();
            return false;
        }
    }
	
	public function setRating($uid, $rating) {
        $smt = $this->conn->prepare("SELECT * from users WHERE unique_id = ?");
        $smt->bind_param("s", $uid);
        $result = $smt->execute();
		
        $new_rating = (float)$rating;
        // check for successful store
        if ($result) {
           
            $user = $smt->get_result()->fetch_assoc();
          $rating_cnt= $user['rating_cnt'];
		  $rating_val= $user['rating_val'];
		  $rating_val= ($rating_val*$rating_cnt + $new_rating)/($rating_cnt+1);
		  $rating_cnt= $rating_cnt+1;
			  $stmt = $this->conn->prepare("UPDATE users SET rating_cnt='$rating_cnt', rating_val='$rating_val', updated_at= NOW() WHERE unique_id='$uid' ");
        $result = $stmt->execute();

   if ($stmt->num_rows > 0) {
            // rating updated
            $stmt->close();
            	$response["error"] = FALSE;
				$response["message"] = "Updated rating"; 
        } else {
            // rating NOT updated
            $stmt->close();
            	$response["error"] = TRUE;
				$response["message"] = "Failed To Update"; 
        }
    }
	}
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
	
	public function get_gravatar( $email, $s = 450, $d = 'mm', $r = 'g', $img = false, $atts = array() ) {
    $url = 'https://www.gravatar.com/avatar/';
    $url .= md5( strtolower( trim( $email ) ) );
    $url .= "?s=$s&d=$d&r=$r";
    if ( $img ) {
        $url = '<img src="' . $url . '"';
        foreach ( $atts as $key => $val )
            $url .= ' ' . $key . '="' . $val . '"';
        $url .= ' />';
    }
	return $url;	
	}
 
}
 
?>
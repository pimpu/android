<?php
 
	// include db connect class
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	
	// array for JSON response
	$response = array();
	 
	// check for required fields
	if (isset($_POST['loginId']) && isset($_POST['gcmId']) ) {
	 
		$loginId = $_POST['loginId'];
		$gcmId = $_POST['gcmId'];
		
		
		// mysql update row with matched userId
		$result = mysql_query("UPDATE userLogin SET gcmId = '$gcmId' WHERE userId = $loginId ");
	
	 	
		// check if row inserted or not
		if ($result) {
			// successfully updated
			$response["success"] = 1;
			$response["message"] = "gcm id successfully updated.";
	 
			// echoing JSON response
			echo json_encode($response);
		} else {
	 
		}
	} else {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	 
		// echoing JSON response
		echo json_encode($response);
	}
?>
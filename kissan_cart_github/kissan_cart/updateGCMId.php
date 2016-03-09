<?php
 
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	 
	// check for required fields
	if (isset($_POST['userId']) && isset($_POST['gcmId']) ) {
	 
		$userId = $_POST['userId'];
		$gcmId = $_POST['gcmId'];
		
		
		// mysql update row with matched userId
		$result = mysql_query("UPDATE userdetails SET gcmId = '".$gcmId."' WHERE uID = ".$userId);
	
	 	
		// check if row inserted or not
		if ($result) {
			// successfully updated
			$response["success"] = 1;
			$response["message"] = "gcm id successfully updated.";
	 
			// echoing JSON response
			echo json_encode($response);
		} 
	} else {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	 
		// echoing JSON response
		echo json_encode($response);
	}
?>
<?php
	// include db connect class
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// isset — Determine if a variable is set and is not NULL
	if( isset($_POST['userId']) ){
		$userId    = $_POST['userId'];
		
		// mysql update row with matched userId
		$result = mysql_query("UPDATE userLogin SET isLogin = 'false' WHERE userId=$userId ");
					
		if($result){
			$response["success"] = 0;
			$response["message"] = "You successfully logout";
		}
		else{
			$response["success"] = 1;
			$response["message"] = "some issue have come.";
		}
		echo json_encode($response);
	}
	else {
		// required field is missing
		$response["success"] = 2;
		$response["message"] = "Required field(s) is missing";
	 
		// echoing JSON response
		echo json_encode($response);
	}
?>
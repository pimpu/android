<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();

	if ( isset($_POST['serverId']) ) {
		// mysql update row with matched userId
		$updateSociety = mysql_query("UPDATE vendors SET status = 0 WHERE id = ".$_POST['serverId']);

		// required field is missing
		$response["success"] = 1;
		$response["message"] = "Society deleted successfully.";
	}
	else {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	}
	
	// echoing JSON response
	echo json_encode($response);
?>
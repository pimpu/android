<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	include 'GCM.php';
	error_reporting(0);
	$response = array();

	if ( isset($_POST['serverId']) ) {
		// mysql update row with matched userId
		$updateSociety = mysql_query("UPDATE userdetails SET status = 0 WHERE uID = ".$_POST['serverId']);

		$gcm = new GCM();
		$registration_ids = array();
		$gcmMessage = array(
								'message' => 'deletedOBPByAdmin'
							);


		// get gcmId from user data  when user is logging.
		$sql = "SELECT gcmId FROM userdetails WHERE uID = ".$_POST['serverId'];
		$getGCMResultData = mysql_query($sql);
		$idGCM=mysql_fetch_array($getGCMResultData);
		if( $idGCM['gcmId'] !== null ) {
			array_push($registration_ids, $idGCM['gcmId']);
			$gcm->send_notification($registration_ids, $gcmMessage);
		}

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
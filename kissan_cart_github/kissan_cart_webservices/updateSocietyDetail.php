<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	include 'GCM.php';
	error_reporting(0);
	$response = array();

	if (isset($_POST['id']) && isset($_POST['old_name']) ) {

		$id = $_POST['id'];
		$old_name = $_POST['old_name'];
		$name = $_POST['name'];
		$contact = $_POST['contact'];
		$email = $_POST['email'];
		$address = $_POST['address'];
		$status = $_POST['status'];

		// mysql update row with matched userId
		$updateSociety = mysql_query("UPDATE society SET soc_name = '".$name."',soc_contact = '".$contact."',soc_email = '".$email."',soc_adrs='".$address."', status=".$status." WHERE id = ".$id);

		$gcm = new GCM();
		$society = array();
		$registration_ids = array();

		$society["soc_id"] = $id;
		$society["old_name"] = $old_name;
		$society["soc_name"] = $name;
		$society["soc_contact"] = $contact;
		$society["soc_email"] = $email;
		$society["soc_adrs"] = $address;
		$society["status"] = $status;

		$gcmMessage = array(
								'message' => 'updateSocietyAtAdmin',
								'changedSocietyDetails' => $society
							);

		// get gcmId from user data  when user is logging.
		$sql = "SELECT gcmId FROM userdetails WHERE gID=1;";
		$getGCMResultData = mysql_query($sql);
		
		if( mysql_num_rows($getGCMResultData) > 0 ) {
			
			$idGCM=mysql_fetch_array($getGCMResultData);
			array_push($registration_ids, $idGCM['gcmId']);
			
		}

		$gcm->send_notification($registration_ids, $gcmMessage);

		// required field is missing
		$response["success"] = 1;
		// $response["message"] = "Records update successfully.";
		$response["message"] = $old_name;

	} else {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	 
	}

	// echoing JSON response
	echo json_encode($response);
?>
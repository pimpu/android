<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	include 'GCM.php';
	error_reporting(0);
	$response = array();

	if (isset($_POST['id'])  ) {

		$id = $_POST['id'];
		// $old_name = $_POST['old_name'];
		$name = $_POST['name'];
		$contact = $_POST['contact'];
		$email = $_POST['email'];
		$address = $_POST['address'];
		$status = $_POST['status'];

		// mysql update row with matched userId
		$updateSociety = mysql_query("UPDATE vendors SET name = '".$name."',contact = '".$contact."',email = '".$email."',address='".$address."', status=".$status." WHERE id = ".$id);

		// required field is missing
		$response["success"] = 1;
		$response["message"] = "Records update successfully.";

	} else {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	 
	}

	// echoing JSON response
	echo json_encode($response);
?>
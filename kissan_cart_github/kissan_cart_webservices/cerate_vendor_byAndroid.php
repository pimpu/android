<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST['name']) && 
		isset($_POST['contact']) &&
		isset($_POST['email']) &&
		isset($_POST['address']) ) {
			
		$name    	= $_POST['name'];
		$contact    = $_POST['contact'];
		$email    	= $_POST['email'];
		$address    = $_POST['address'];
		
		$result = mysql_query("INSERT INTO vendors(
											name,
											contact,
											address,
											email,
											status) VALUES(
											'".$name."',
											'".$contact."', 
											'".$address."',
											'".$email."', 
											1);");
	
		if($result) {
			$response["success"] = 2;
			$response["message"] = mysql_insert_id();

			echo json_encode($response);
		}
	}
	else{
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
		
		echo json_encode($response);
	}
?>
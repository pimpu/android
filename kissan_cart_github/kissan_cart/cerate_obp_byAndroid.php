<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST['name']) && 
		isset($_POST['contact']) &&
		isset($_POST['email']) &&
		isset($_POST['pwd']) &&
		isset($_POST['address']) &&
		isset($_POST['store_name']) &&
		isset($_POST['pin']) &&
		isset($_POST['city']) &&
		isset($_POST['state']) &&
		isset($_POST['country']) ) {
			
		$name    	= $_POST['name'];
		$contact    = $_POST['contact'];
		$email    	= $_POST['email'];
		$pwd    	= $_POST['pwd'];
		$address    = $_POST['address'];
		$store_name = $_POST['store_name'];
		$pin    	= $_POST['pin'];
		$city    	= $_POST['city'];
		$state    	= $_POST['state'];
		$country	= $_POST['country'];
		
		$checkMatchEmailQuery = mysql_query("SELECT * FROM userdetails WHERE uEmailID='".$email."';");
		
		// check email id is already exist.
		if(mysql_num_rows($checkMatchEmailQuery) > 0 ){
			$response["success"] = 1;
			$response["message"] = "This email-id is already exist.";
			
			echo json_encode($response);
		}
		else{
			$result = mysql_query("INSERT INTO userdetails(
														gID,
														uName,
														uStoreName,
														uEmailID,
														uPassword,
														uCont,
														uAddrs,
														uPin,
														uCity,
														uState,
														uCountry,
														status) VALUES(
														2,
														'".$name."',
														'".$store_name."',
														'".$email."', 
														'".$pwd."', 
														'".$contact."', 
														'".$address."',
														".$pin.",
														'".$city."',
														'".$state."',
														'".$country."',
														1);");
			
				if($result){
					$response["success"] = 2;
					$response["message"] = "OBP inserted successfully.";
		
					echo json_encode($response);
				}
		}

	}
	else{
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
		
		echo json_encode($response);
	}
?>
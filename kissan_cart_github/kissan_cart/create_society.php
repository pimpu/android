<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST['society_name']) && isset($_POST['society_contact']) && isset($_POST['society_email']) && isset($_POST['society_address']) && isset($_POST['userId']) ){
		$society_name    	= $_POST['society_name'];
		$society_contact	= $_POST['society_contact'];
		$society_email 		= $_POST['society_email'];
		$society_address	= $_POST['society_address'];
		$userId				= $_POST['userId'];
		
		$checkMatchOBPNameQuery = mysql_query("SELECT * FROM society WHERE soc_name='".$society_name."';");
		$checkMatchOBPEmailQuery = mysql_query("SELECT * FROM society WHERE soc_email='".$society_email."';");
		
		if(mysql_num_rows($checkMatchOBPNameQuery) > 0 ){
			$response["success"] = 1;
			$response["message"] = "This society name is already exist.";
			
			echo json_encode($response);
		}
		else if(mysql_num_rows($checkMatchOBPEmailQuery) > 0 ){
			$response["success"] = 3;
			$response["message"] = "This society email-id is already exist.";
			
			echo json_encode($response);
		}
		else{
			$result = mysql_query("INSERT INTO society(	uID,
														soc_name,
														soc_contact,
														soc_email,
														soc_adrs,
														status)VALUES(
														".$userId.",
														'".$society_name."',
														'".$society_contact."',
														'".$society_email."',
														'".$society_address."',
														1);");
			$getId = mysql_insert_id();
														
			if(isset($getId)){
				
				$response["success"] = 2;
				$response["message"] = $getId;
	
				echo json_encode($response);
			}
		}
		
	}
	else{
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
		
		echo json_encode($response);
	}
?>
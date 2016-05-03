<?php
	// include db connect class
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// isset — Determine if a variable is set and is not NULL
	if( isset($_POST['emailId']) && isset($_POST['loginId']) ) {
		
		$emailId    = $_POST['emailId'];
		$loginId	= $_POST['loginId'];
		
		$getUserData = mysql_query("SELECT * FROM userLogin WHERE emailId='".$emailId."' AND userId=".$loginId.";");
		
		if(mysql_num_rows($getUserData) > 0) {
			$arrayUserData = mysql_fetch_array($getUserData);
			
			$subject = "E-Carier ";
			$txt = "your password is: "+$arrayUserData['password'];

			mail($emailId,$subject,$txt);

			$response["message"] = "password successfully sent on your email address.";
			echo json_encode($response);
		}
		else{
			
			$response["message"] = "email id does not match";
			echo json_encode($response);
		}
	}
	
?>
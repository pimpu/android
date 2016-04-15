<?php
	// include db connect class
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// isset — Determine if a variable is set and is not NULL
	if( isset($_POST['emailId']) && isset($_POST['password']) ){
		$emailId    = $_POST['emailId'];
		$password	= $_POST['password'];
		
		$checkMatchEmailQuery = mysql_query("SELECT * FROM userLogin WHERE emailId='$emailId' ");
		$checkIsRegister      = mysql_query("SELECT * FROM userLogin WHERE emailId='$emailId' AND password='$password' ");
		if(mysql_num_rows($checkMatchEmailQuery) <= 0 )
		{
			$response["success"] = 0;
			$response["message"] = "Sorry,You are not register with this app.";
			
			echo json_encode($response);
		}
		else if(mysql_num_rows($checkIsRegister) > 0 )
		{
			$result = mysql_query("UPDATE userLogin SET isLogin = 'true' WHERE emailId='$emailId' AND password='$password' ");
			
			$userEach = array();
			$arrayRow = mysql_fetch_array($checkIsRegister);
			$userEach["loggedId"] = $arrayRow["userId"];
            $userEach["name"] = $arrayRow["fullName"];
			$userEach["who"] = $arrayRow["userType"];
			
			$response["success"] = 1;
			$response["message"] = array();
			array_push($response["message"], $userEach);
			
			
			echo json_encode($response);
		}
		else
		{
			$response["success"] = 2;
			$response["message"] = "Email-id and password doesn't match";
			
			echo json_encode($response);
		}
	}
?>
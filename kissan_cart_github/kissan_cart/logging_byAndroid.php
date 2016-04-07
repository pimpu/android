<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// isset — Determine if a variable is set and is not NULL
	if( isset($_POST['emailId']) && isset($_POST['password']) ){
		$emailId    = $_POST['emailId'];
		$password	= $_POST['password'];
		
		$checkMatchEmailQuery = mysql_query("SELECT * FROM userdetails WHERE uEmailID='".$emailId."';");
		$checkIsRegister      = mysql_query("SELECT * FROM userdetails WHERE uEmailID='".$emailId."' AND uPassword='".$password."';");
		if(mysql_num_rows($checkMatchEmailQuery) <= 0 )
		{
			$response["success"] = 0;
			$response["message"] = "Sorry,You are not register with this app.";
			
			echo json_encode($response);
		}
		else if(mysql_num_rows($checkIsRegister) > 0 )
		{
			// admin id requires for replyTo in offline mode in enquiry table when enquire first time created.
			$queryUIDForAdmin = mysql_query("SELECT uID FROM userdetails WHERE 	gID = 1;");
			$arrayUIDForAdmin = mysql_fetch_array($queryUIDForAdmin);

			$userEach = array();

			$arrayRow = mysql_fetch_array($checkIsRegister);
			$userEach["loggedId"] = $arrayRow["uID"];
            $userEach["name"] = $arrayRow["uName"];
            $userEach["store_name"] = $arrayRow["uStoreName"];
            $userEach["contact"] = $arrayRow["uCont"];
            $userEach["address"] = $arrayRow["uAddrs"];
            $userEach["pincode"] = $arrayRow["uPin"];
            $userEach["city"] = $arrayRow["uCity"];
            $userEach["state"] = $arrayRow["uState"];
            $userEach["country"] = $arrayRow["uCountry"];
            $userEach["status"] = $arrayRow["status"];
			
			$getUserGroup = mysql_query("SELECT gName FROM usergroup WHERE gId=".$arrayRow["gID"]);
			$arrayUG = mysql_fetch_array($getUserGroup);
			$userEach["who"] = $arrayUG["gName"];

			$userEach["adminUID"] = $arrayUIDForAdmin['uID'];

			
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
	else
	{
		$response["success"] = 3;
		$response["message"] = "Required field(s) is missing";
		
		echo json_encode($response);
	}
?>
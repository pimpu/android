<?php
	// include db connect class
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// isset — Determine if a variable is set and is not NULL
	if( isset($_POST['emailId']) && isset($_POST['password']) && isset($_POST['phoneNo']) && isset($_POST['fullName']) ){
		$emailId    = $_POST['emailId'];
		$password	= $_POST['password'];
		$phoneNo 	= $_POST['phoneNo'];
		$fullName 	= $_POST['fullName'];
		
		$checkMatchQuery = mysql_query("SELECT * FROM driverlogin WHERE emailId='$emailId' ");
		if(mysql_num_rows($checkMatchQuery) > 0 )
		{
			$response["success"] = 2;
			$response["message"] = "this email id is already exist";
			
			echo json_encode($response);
		}
		else
		{
			// mysql inserting a new row
			$result = mysql_query("INSERT INTO driverlogin(emailId,password,phoneNo,fullName) VALUES('$emailId', '$password', '$phoneNo', '$fullName')");
			// get driverId from inserted data
			$sql = mysql_query("SELECT driverId FROM driverlogin WHERE emailId='$emailId' AND password='$password' AND phoneNo='$phoneNo' AND fullName='$fullName' ");
							
			// check if row inserted or not
			if($result){
				$id = mysql_fetch_array($sql);
				
				$response["success"] = 1;
				$response["message"] = $id["driverId"];
				
				echo json_encode($response);
			}
			else {
				$response["success"] = 0;
				$response["message"] = "Required field(s) is missing";
				
				echo json_encode($response);
			}
		}
		
	}
?>
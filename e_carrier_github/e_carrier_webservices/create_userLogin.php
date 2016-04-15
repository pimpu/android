<?php
	// include db connect class
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// isset — Determine if a variable is set and is not NULL
	if( isset($_POST['emailId']) && isset($_POST['password']) && isset($_POST['phoneNo']) && isset($_POST['fullName']) && isset($_POST['userType']) ){
		$emailId    = $_POST['emailId'];
		$password	= $_POST['password'];
		$phoneNo 	= $_POST['phoneNo'];
		$fullName 	= $_POST['fullName'];
		$userType   = $_POST['userType'];
		
		
		$checkMatchEmailQuery = mysql_query("SELECT * FROM userLogin WHERE emailId='".$emailId,"';");
		$checkIsRegister      = mysql_query("SELECT * FROM userLogin WHERE emailId='".$emailId."' AND password='".$password."';");
		
		if(mysql_num_rows($checkMatchEmailQuery) > 0 ){
			if(mysql_num_rows($checkIsRegister) > 0 ){
				$response["success"] = 3;
				$response["message"] = "You already registered with this app.Please Log In";
			
				echo json_encode($response);
			}
			else{
				$response["success"] = 2;
				$response["message"] = "this email id is already exist";
			
				echo json_encode($response);
			}
		}
		else
		{
			// mysql inserting a new row
			$result = mysql_query("INSERT INTO userLogin(emailId,password,phoneNo,fullName,userType,isLogin) VALUES('".$emailId."', '".$password."', '".$phoneNo."', '".$fullName."', '".$userType."', 'true')");
			
			// check if row inserted or not
			if($result){
				$id = mysql_insert_id();
				$response["success"] = 1;
				$response["message"] = $id;
				
				// insert into driver vehicle type table for examine vehicle type of driver i.e close or open.
				if( $userType === 'driver'){
					$vehicleTypeEntry = mysql_query("INSERT INTO drivervehicletype(vehicle_type,vehicle,fk_driver_uesr_id) VALUES('open','MARUTI VAN (Cargo)',".$id.");");
				}
				
				echo json_encode($response);
			}
			else 
			{
				$response["success"] = 0;
				$response["message"] = "Required field(s) is missing";
				
				echo json_encode($response);
			}
		}
			
		
		
		
	}
?>
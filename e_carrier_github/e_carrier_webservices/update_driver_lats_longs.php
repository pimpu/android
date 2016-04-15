<?php
	
	// include db connect class
	include 'eCarrier_db_connect.php';
	include 'eCarrier_GCM.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST["driverId"]) && isset($_POST["lat"]) && isset($_POST["longs"]) && isset($_POST["postal_code"]) ){
		$driverId  	 = $_POST['driverId'];
		$lat 		 = $_POST['lat'];
		$longs 		 = $_POST['longs'];
		$postal_code = $_POST['postal_code'];
		
		$checkIsData = mysql_query("SELECT * FROM drivervehicletype WHERE fk_driver_uesr_id=$driverId");
		
		if(mysql_num_rows($checkIsData) > 0 ){
			$updateResult = mysql_query("UPDATE drivervehicletype SET lats = $lat,longs = $longs,postal_code=$postal_code WHERE fk_driver_uesr_id=$driverId;");
			
			// get driver profile data
			$getDriverData = mysql_query("SELECT * FROM userlogin WHERE userId =".$driverId.";");
			$arrayDriverData = mysql_fetch_array($getDriverData);
			
			// check current driver is aquired any enquiry or not
			$getAnyEnquiryAquired = mysql_query("SELECT * FROM bookedserviceinfo WHERE fk_driverId = ".$driverId." AND status = 'open'; ");
			
			// check driver is logged in if true then enter in if clause.
			if( strcmp($arrayDriverData["isLogin"],"true") == 0 && mysql_num_rows($getAnyEnquiryAquired) <= 0){
				
				
				// will find the closest enquired locations that are within a radius of 5 km to the current lats, longs coordinate of driver
				$getAnyBookedEnquiry = mysql_query("SELECT bookedId,fk_enquiryId,fk_driverId,( 6371 * acos( cos( radians(".$lat.") ) * cos( radians( enquired_lats ) ) * cos( radians( enquired_longs ) - radians(".$longs.") ) + sin( radians(".$lat.") ) * sin( radians( enquired_lats ) ) ) ) AS distance FROM bookedserviceinfo WHERE status='open' HAVING distance < 5 ORDER BY distance LIMIT 0 , 20;");
				
				if(mysql_num_rows($getAnyBookedEnquiry) > 0){
					
					$perEnquiryId = array();
				
					while($row = mysql_fetch_array($getAnyBookedEnquiry)){
						
						if( $row['fk_driverId'] == 0 ){
							
							$getRequestingDriverData = mysql_query("SELECT * FROM requestingalldriver WHERE fk_booked_id=".$row['bookedId']." AND requesting_driver=".$driverId." ;");
							
							if(mysql_num_rows($getRequestingDriverData) <= 0){
								
								mysql_query("INSERT INTO requestingalldriver(fk_booked_id,requesting_driver)VALUES(".$row['bookedId'].",".$driverId." ); ");
								array_push($perEnquiryId,$row['fk_enquiryId']);
							}
						}
					}
					
					$registration_ids = array( $arrayDriverData['gcmId'] );
					
					$gcm = new eCarrier_GCM();
					
					$gcmMessage = array(
										'message' 	 => 'comesInNearestEnquiryArea',
										'enquiryIds' => $perEnquiryId,
									);
					
					if(count($perEnquiryId)> 0){
						$getGCMresult = $gcm->send_notification($registration_ids, $gcmMessage);
					}
									
				}
				
			}
			
			$response["success"] = 1;
			$response["message"] = $lat." : ".$longs;
			echo json_encode($response);
				
		}
	}
	
?>


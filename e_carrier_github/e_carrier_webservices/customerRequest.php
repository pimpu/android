<?php
 
	// include db connect class
	include 'eCarrier_db_connect.php';
	include 'eCarrier_GCM.php';
	include 'update_enquired_km.php';
	error_reporting(0);
	
	// array for JSON response
	$response = array();
	
	if (	isset($_POST['fk_userId']) && isset($_POST['beginningArea']) && isset($_POST['destinationArea']) && isset($_POST['deliveryDateTime']) 
			&& isset($_POST['weight']) && isset($_POST['unit']) && isset($_POST['vehicleType']) && isset($_POST['vehicle']) &&
			isset($_POST['valueAddedServices']) && isset($_POST['pickupAddress']) && isset($_POST['deliveryAddress']) && isset($_POST['lats']) && isset($_POST['longs']) && isset($_POST['extraManPwr'])) {
		
		$fk_userId    		= $_POST['fk_userId'];
		$beginningArea    	= $_POST['beginningArea'];
		$destinationArea    = $_POST['destinationArea'];
		$deliveryDateTime  	= $_POST['deliveryDateTime'];
		$weight    			= $_POST['weight'];
		$unit    			= $_POST['unit'];
		$vehicleType    	= $_POST['vehicleType'];
		$vehicle    		= $_POST['vehicle'];
		$valueAddedServices = $_POST['valueAddedServices'];
		$pickupAddress    	= $_POST['pickupAddress'];
		$deliveryAddress    = $_POST['deliveryAddress'];
		$lats		    	= $_POST['lats'];
		$longs		    	= $_POST['longs'];
		$extraManPwr	    = $_POST['extraManPwr'];
		
						
		// $fk_userId    		= 17;
		// $beginningArea    	= "Sector 11, Sanpada";
		// $destinationArea    = "Dhobi Talao";
		// $deliveryDateTime  	= "1452685380000";
		// $weight    			= "600.00";
		// $unit    			= "Kg";
		// $vehicleType    	= "Open";
		// $vehicle    		= "MARUTI VAN (Cargo)";
		// $valueAddedServices = "[noVAS]";
		// $pickupAddress    	= "dsfd";
		// $deliveryAddress    = "gfhg";
		
		
		
		$getResultData = mysql_query("SELECT enquiryId FROM bookingenquiry WHERE fk_userId='$fk_userId' AND beginningArea='$beginningArea' AND destinationArea='$destinationArea' AND deliveryDateTime='$deliveryDateTime' ");
		
		if(mysql_num_rows($getResultData) <= 0){
			
			$result = mysql_query("INSERT INTO bookingenquiry(fk_userId,beginningArea,destinationArea,
															  deliveryDateTime,weight,unit,vehicleType,
															  vehicle,valueAddedServices,pickupAddress,deliveryAddress,kgForExtraManPwr) 
															  VALUES(
															  '".$fk_userId."','".$beginningArea."','".$destinationArea."',
															  '".$deliveryDateTime."','".$weight."','".$unit."','".$vehicleType."',
															  '".$vehicle."','".$valueAddedServices."','".$pickupAddress."','".$deliveryAddress."','".$extraManPwr."'
															  )");
			
			if(isset($result)){
		
				$strEnquiryId = mysql_insert_id();
				
				if(isset($strEnquiryId)){
					
					// insert data into bookedserviceinfo table for tracking customer-driver service.
					$insertBookedService = mysql_query("INSERT INTO bookedserviceinfo(fk_enquiryId,status,enquired_lats,enquired_longs) VALUES (".$strEnquiryId.",'open','".$lats."','".$longs."') ");
					
					// get booked data from currently inserted data in bookedserviceinfo
					$getBookedInsertedData = mysql_query("SELECT bookedId FROM bookedserviceinfo WHERE fk_enquiryId='".$strEnquiryId."' AND status='open' ");
					$arrayBookedId = mysql_fetch_array($getBookedInsertedData);
					$strBookeId = $arrayBookedId["bookedId"];
					
					// send notification to driver who has vehicle of vehicleType that customer wants.
					$gcm = new eCarrier_GCM();
					$updateEnquiryKmUsingGoogleApi = new updateEnquiryKm();
					
					$perEnquiry = array();
										
					$perEnquiry["enquiryId"] 		= (string)$strEnquiryId;
					$perEnquiry["userId"] 			= $fk_userId;
					$perEnquiry["beginningArea"] 	= $beginningArea;
					$perEnquiry["destinationArea"] 	= $destinationArea;
					$perEnquiry["deliveryDateTime"] = $deliveryDateTime;
					$perEnquiry["weight"] 			= $weight;
					$perEnquiry["unit"] 			= $unit;
					$perEnquiry["pickupAddress"] 	= $pickupAddress;
					$perEnquiry["deliveryAddress"] 	= $deliveryAddress;
					
					$gcmMessage = array(
											'message' => 'requestToNearestDriver',
											'enquiryDetails' => $perEnquiry
										);
					
					// will find the closest 20 locations that are within a radius of 5 km to the lats, longs coordinate and 
					// match vehicle and vehicle type from drivervehicletype table with customer enquiry vehicle and vehicle type then get fk_driver_uesr_id from that table
					// after getting fk_driver_uesr_id(user id) from drivervehicletype, then get  gcm id from user login table.
					
					$requestingDriver = "SELECT fk_driver_uesr_id, ( 6371 * acos( cos( radians(".$lats.") ) * cos( radians( lats ) ) * cos( radians( longs ) - radians(".$longs.") ) + sin( radians(".$lats.") ) * sin( radians( lats ) ) ) ) AS distance FROM drivervehicletype WHERE vehicle_type='".$vehicleType."' AND vehicle='".$vehicle."' HAVING distance < 5 ORDER BY distance LIMIT 0 , 20;";
					$getDriverResultQuery = mysql_query($requestingDriver);		
					$registration_ids = array();
					
					while($row = mysql_fetch_array($getDriverResultQuery)){
						
						// check current driver is aquired any enquiry or not
						$getAnyEnquiryAquired = mysql_query("SELECT * FROM bookedserviceinfo WHERE fk_driverId = ".$row['fk_driver_uesr_id']." AND status = 'open'; ");
						
						if(mysql_num_rows($getAnyEnquiryAquired) <= 0){
							
							// get gcmId from user data  when user is logging.
							$sql = "SELECT gcmId FROM userlogin WHERE userId=".$row['fk_driver_uesr_id']." AND isLogin='true';";
							$getGCMResultData = mysql_query($sql);
							
							if( mysql_num_rows($getGCMResultData) > 0 ){
								
								$idGCM=mysql_fetch_array($getGCMResultData);
								array_push($registration_ids, $idGCM['gcmId']);
								
								// creating data for all requesting driver which are in same area of customer requesting area of enquiry.
								$requestingDriverDataResult=mysql_query("INSERT INTO requestingalldriver(fk_booked_id,requesting_driver)VALUES(".$strBookeId.",".$row['fk_driver_uesr_id']." ); ");
								
							}
						}
						
					}
					
					$getGCMresult = $gcm->send_notification($registration_ids, $gcmMessage);
					
					$updateEnquiryKmUsingGoogleApi->update_enquiry_km($strEnquiryId);
					
					$response["success"] = 1;
					$response["message"] = $strEnquiryId;
					// echoing JSON response
					echo json_encode($response);
				}
				
			}
		}
		else {
			$response["success"] = 2;
			$response["message"] = "Entry already exist.";
			
			echo json_encode($response);
		}
	}
	else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
		
		echo json_encode($response);
	}
	
?>
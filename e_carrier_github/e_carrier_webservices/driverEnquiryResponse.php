<?php
	// steps -
	// 1)   get booked tracking data from bookedserviceinfo table
	// 2)   in that table, fk_driverId column is empty that mean enquiry is not full fill
	// 2.1) update fk_driverId column in bookedserviceinfo.
	// 2.2) remove row from requestingalldriver who has accepting enquiry.
	// 2.3) get data from requestingalldriver other than who has accepting enquiry.
	// 2.4) get gcmID from userlogin table using requesting_driver according to 2.3 data
	// 2.5) get enquiry data from bookingenquiry using enquiryId
	// 2.6) get gcmID from fk_userId which one got from 2.5 data (Cancelled coding)
	// 2.7) get driver profile data from userlogin. that data is use for user to show driver id and name.
	// 2.8) send notification to requesting driver except who has accepting enquiry and enquired user.
	// 2.9) remove row from requestingalldriver who has not accepting enquiry.
	// 3)   sent enquiry full fill notification to driver
	
 
	// include db connect class
	include 'eCarrier_db_connect.php';
	include 'eCarrier_GCM.php';
	$gcm = new eCarrier_GCM();
	
	error_reporting(0);
	
	// array for JSON response
	$response = array();
	
	if ( isset($_POST["enquiryId"]) && isset($_POST["driverId"]) ) {
					
		$enquiryId = $_POST['enquiryId'];
		$driverId  = $_POST['driverId'];
		
		// check current driver is aquired any enquiry or not
		$getAnyEnquiryAquired = mysql_query("SELECT * FROM bookedserviceinfo WHERE fk_driverId = ".$driverId." AND status = 'open'; ");
		
		if(mysql_num_rows($getAnyEnquiryAquired) <= 0){
			
			// get booked tracking data from bookedserviceinfo table
			$result 	 = mysql_query("SELECT * FROM bookedserviceinfo WHERE fk_enquiryId=".$enquiryId.";");
			$arrayResult = mysql_fetch_array($result);
			$strDriverId = $arrayResult["fk_driverId"];
			$bookedId	 = $arrayResult["bookedId"];
			
			// in that table, fk_driverId column is empty that mean enquiry is not full fill
			if( strcmp($strDriverId,"0") == 0){
				
				// update fk_driverId column in bookedserviceinfo.
				$updateResult = mysql_query("UPDATE bookedserviceinfo SET fk_driverId = $driverId WHERE fk_enquiryId=$enquiryId;");
				if( $updateResult ){
					
					// remove row from requestingalldriver who has accepting enquiry.
					$removedEnquiryAcceptDriverData=mysql_query("DELETE FROM requestingalldriver WHERE requesting_driver=".$driverId." AND fk_booked_id=".$bookedId.";");
					
					if($removedEnquiryAcceptDriverData){
													
						// get data from requestingalldriver except who has accepting enquiry.
						$gcmIDs=mysql_query("SELECT gcmId FROM userlogin WHERE userId=(SELECT requesting_driver FROM requestingalldriver WHERE fk_booked_id=".$bookedId.");");
											
						$registration_ids = array();
						
						// get gcmID from userlogin table using requesting_driver according to 2.3 data
						if( mysql_num_rows($gcmIDs) > 0 ){
							
							// make registration_ids array with gcm id
							while($row = mysql_fetch_array($gcmIDs) ){
								array_push($registration_ids, $row['gcmId']);
							}
						}
						
						// get enquiry data from bookingenquiry using enquiryId
						$getEnquiryData=mysql_query("SELECT * FROM bookingenquiry WHERE enquiryId=".$enquiryId.";");
						$arrayEnquiryData=mysql_fetch_array($getEnquiryData);
						$fk_userId_From_EnquiryTable=$arrayEnquiryData['fk_userId'];
						
						// get gcmID from fk_userId which one got from 2.5 data (Cancelled coding)
						$getUserDataForGCMOfEnquired=mysql_query("SELECT * FROM userlogin WHERE userId=$fk_userId_From_EnquiryTable; ");
						$arrayUserDataOfEnquired = mysql_fetch_array($getUserDataForGCMOfEnquired);
						array_push($registration_ids, $arrayUserDataOfEnquired['gcmId']);
						
						// get driver profile data from userlogin. that data is use for user to show driver id and name.
						$getUserDriverData=mysql_query("SELECT * FROM userlogin WHERE userId='".$driverId."' ");
						$arrayUserDriverData=mysql_fetch_array($getUserDriverData);
						array_push($registration_ids, $arrayUserDriverData['gcmId']);
						
						$driver_profile_data_array = array();
						array_push($driver_profile_data_array, $driverId );
						array_push($driver_profile_data_array, $arrayUserDriverData['fullName'] );
						
						$enquiry_data_array=array();
						array_push($enquiry_data_array,$enquiryId);
						array_push($enquiry_data_array,$arrayEnquiryData['beginningArea']);
						array_push($enquiry_data_array,$arrayEnquiryData['destinationArea']);
						
						// send notification to requesting driver except who has accepting enquiry and enquired user.
						$gcmMessage = array(
												'message' 		         => 'enquiryFullFill',
												'enquiryArray' 	         => $enquiry_data_array,
												'userId'    	         => $fk_userId_From_EnquiryTable,
												'acceptingDriverProfile' => $driver_profile_data_array
											);
											
						$getGCMresult = $gcm->send_notification($registration_ids, $gcmMessage);
						$gcmSuccess=$getGCMresult["success"];
						
						// remove row from requestingalldriver who has not accepting enquiry.
						// if($gcmSuccess > 0){
							mysql_query("DELETE FROM requestingalldriver WHERE fk_booked_id=".$bookedId." ;");
						// }
						
						
						$response["success"] = 0;
						$response["message"] = "enquiry fullfill.".$gcmMessage;
			 
						// echo no users JSON
						echo json_encode($response);
					
					}
					
				}
				
			}
			// sent enquiry full fill notification to driver
			else{
				$getBookedData=mysql_query("SELECT * FROM bookedserviceinfo WHERE fk_enquiryId='".$enquiryId."';");
				$arrayBookedData=mysql_fetch_array($getBookedData);
				$sqlDriverId=$arrayBookedData["fk_driverId"];
				
				if( $sqlDriverId == $driverId ){
					
					$gcmMessage = array(
										'message' 	=> 'afterEnquiryFullFill(ownerDriverEnquiry)',
										'enquiryId' => $enquiryId,
										'driverId'  => $driverId
									);
					$getUserData=mysql_query("SELECT gcmId FROM userlogin WHERE userId=$driverId ");
					$arrayUserData = mysql_fetch_array($getUserData);
					$registration_ids =array();
					array_push($registration_ids, $arrayUserData['gcmId']);
					
					$getGCMresult = $gcm->send_notification($registration_ids, $gcmMessage);
					
					$response["success"] = 1;
					$response["message"] = "enquiry already fullfill.sql id: ".$sqlDriverId." + driverId:".$driverId;
					echo json_encode($response);
					
					
				}
				else {
					$gcmMessage = array(
										'message' 	=> 'afterEnquiryFullFill',
										'enquiryId' => $enquiryId,
										'driverId'  => $driverId
									);
					$getUserData=mysql_query("SELECT gcmId FROM userlogin WHERE userId=$driverId ");
					$arrayUserData = mysql_fetch_array($getUserData);
					$registration_ids =array();
					array_push($registration_ids, $arrayUserData['gcmId']);
					
					$getGCMresult = $gcm->send_notification($registration_ids, $gcmMessage);
					
					$response["success"] = 2;
					$response["message"] = "enquiry already fullfill.sql id: ".$sqlDriverId." + driverId:".$driverId;
					echo json_encode($response);
					
					
				}
			}
		}
		else{
			$response["success"] = 3;
			$response["message"] = "you already booked enquiry";
			echo json_encode($response);
		}
						
		
		
					
			
	}
		
?>
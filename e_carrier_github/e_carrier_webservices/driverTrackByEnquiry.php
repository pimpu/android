<?php
	
	// include db connect class
	include 'eCarrier_db_connect.php';
	include 'eCarrier_GCM.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST["enquiryId"]) ){
		
		$enquiryId = (int)$_POST['enquiryId'];
		
		$bookedData = mysql_query("SELECT fk_driverId FROM bookedserviceinfo WHERE fk_enquiryId=".$enquiryId.";");
		
		if(mysql_num_rows($bookedData) > 0 ){
			
			$arrayBookedData = mysql_fetch_array($bookedData);
			$driverId 		 =  $arrayBookedData['fk_driverId'];
			
			$driverData =  mysql_query("SELECT * FROM drivervehicletype WHERE fk_driver_uesr_id=".$driverId.";");
			$arrayDriverData = mysql_fetch_array($driverData);
			
			$response["success"] = 0;
			$response["message"] = array();
			
			$perData = array();
			$perData["latitude"] 	= $arrayDriverData['lats'];
			$perData["longitude"] 	= $arrayDriverData['longs'];
			
			array_push($response["message"], $perData);
			echo json_encode($response);	
			
		}
		
	}
	
?>
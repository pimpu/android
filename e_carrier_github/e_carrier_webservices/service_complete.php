<?php
	
	// task 
	// make status as close in bookedserviceinfo table.
	// delete data enquiry data from both driver's and customer's mobile device.


	include 'eCarrier_db_connect.php';
	include 'eCarrier_GCM.php';
	error_reporting(0);
	
	// array for JSON response
	$response = array();
	
	// "[..,..]" then this function give the output in array seperated with .. value
	
	function multiexplode ($delimiters,$string) {
    
		$ready = str_replace($delimiters, $delimiters[0], $string);
		$launch = explode($delimiters[0], $ready);
		return  $launch;
	}
	
	function floor_dec($number,$separator = '.') {
		$numberpart=explode($separator,$number);
		
		$length = count($numberpart);
		$finalno=$numberpart[0];
		
		if($length > 1){
			$finalno+=1;
		}
			
		return $finalno;
	}
		
	if ( isset($_POST['enquiryId']) ){
		
		$enquiryId 			= $_POST['enquiryId'];
		$finalAmount		= null;
		$declaredAmt         = null;
		
		$result = mysql_query("SELECT * FROM bookingenquiry WHERE enquiryId=".$enquiryId.";");
		$serviceData = mysql_query("SELECT * FROM bookedserviceinfo WHERE fk_enquiryId=".$enquiryId.";");
		
		
		if( mysql_num_rows($result) > 0){
			$getEnquiryData 	= mysql_fetch_array($result);
			$getServiceData 	= mysql_fetch_array($serviceData);
			$distanceCovered 	= $getServiceData['enquired_meter'] / 1000;
			
			$perData = array();
			
			$response["message"] = array();
			
			$vehicle 			= $getEnquiryData['vehicle'];
			$valueAddedServices = $getEnquiryData['valueAddedServices'];
			$kgForExtraManPwr	= $getEnquiryData['kgForExtraManPwr'];
			
			
			
			$numbers = intval($kgForExtraManPwr)/30;
			$floarValue_30kgPerPerson = floor_dec($numbers);
			
			// get all valueAddedServices for per vehicle .
			$getResultData = mysql_query("SELECT * FROM valueaddedservices WHERE vehicle='".$vehicle."';");
			$getVASData = mysql_fetch_array($getResultData);
			
			$rsPerKm			= $getVASData['rsPerKm'];
			
			// calculate cost per distance covered by vehicle.
			$finalAmount		= $distanceCovered * $rsPerKm;
			$declaredAmt        = $distanceCovered * $rsPerKm;
			$perData["distanceCovered"] = $distanceCovered;
			$perData["declaredAmt"] = $declaredAmt;
			
			
			$response["success"] = 0;
			
			
			$exploded = multiexplode(array("[","]",","),$valueAddedServices);
			
			$count = count($exploded);
			for ($i = 0; $i < $count; $i++) {
				if( isset($getVASData[$exploded[$i]]) ){
					
					if( strcmp( $exploded[$i],"manpower" ) == 0 ){
						
						$perData[$exploded[$i]] = $getVASData[$exploded[$i]];
						$finalAmount += $getVASData[$exploded[$i]];
						
					}
					else if( strcmp( $exploded[$i],"insurance" ) == 0 ){
						
						// .2% of declaredAmt i.e. distance covered by vehicle per kilometer.
						// ( 0.2 / 100 ) * declaredAmt = ?;
						$perData[$exploded[$i]] = ( $getVASData[$exploded[$i]] / 100 ) * $declaredAmt;
						$finalAmount += ( (double)$getVASData[$exploded[$i]] / 100 ) * $declaredAmt;
						
					}
					else if( strcmp( $exploded[$i],"risk" ) == 0 ){
						
						// 2% of declaredAmt i.e. distance covered by vehicle per kilometer.
						// ( 2 / 100 ) * declaredAmt = ?;
						$perData[$exploded[$i]] = ( $getVASData[$exploded[$i]] / 100 ) * $declaredAmt;
						$finalAmount += ( (int)$getVASData[$exploded[$i]] / 100 ) * $declaredAmt;
						
					}
					else if( strcmp( $exploded[$i],"holiday_delivery" ) == 0 ){
						$perData[$exploded[$i]] = $getVASData[$exploded[$i]];
						$finalAmount += $getVASData[$exploded[$i]];
					}
					else if( strcmp( $exploded[$i],"labour_charges" ) == 0 ){
						$perData[$exploded[$i]] = $getVASData[$exploded[$i]];
						$finalAmount += $getVASData[$exploded[$i]];
					}
					
				}
			}
			
			$perData["vehicle"] = $getVASData["vehicle"];
			$perData["finalAmount"] = $finalAmount;
			
			array_push($response["message"], $perData);
			
			echo json_encode($response);
		}
	
	}
	else {
		$response["success"] = 1;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
?>
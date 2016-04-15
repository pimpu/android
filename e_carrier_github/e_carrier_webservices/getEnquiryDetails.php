<?php
 
	// include db connect class
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	
	// array for JSON response
	$response = array();
	
	// check for post data
	if ( isset($_POST["enquiryIds"]) ) {
		
		// enquiryIds comes like as ["1","2"]
		$enquiryIds = $_POST['enquiryIds'];
		preg_match_all('!\d+!', $enquiryIds, $matches);
		
		
		// user node
		$response["message"] = array();
		
		$matchesPerData=$matches[0];
		
		foreach($matchesPerData as $value) {
			$perEnquiry=array();
			
		  	$result = mysql_query("SELECT * FROM bookingenquiry WHERE enquiryId=".$value.";");
			$getEnquiryData = mysql_fetch_array($result);
			
			$perEnquiry["enquiryId"] 		= (string)$getEnquiryData['enquiryId'];
			$perEnquiry["userId"] 			= $getEnquiryData['fk_userId'];
			$perEnquiry["beginningArea"] 	= $getEnquiryData['beginningArea'];
			$perEnquiry["destinationArea"] 	= $getEnquiryData['destinationArea'];
			$perEnquiry["deliveryDateTime"] = $getEnquiryData['deliveryDateTime'];
			$perEnquiry["weight"] 			= $getEnquiryData['weight'];
			$perEnquiry["unit"] 			= $getEnquiryData['unit'];
			$perEnquiry["pickupAddress"] 	= $getEnquiryData['pickupAddress'];
			$perEnquiry["deliveryAddress"] 	= $getEnquiryData['deliveryAddress'];
			
			array_push($response["message"], $perEnquiry);
		}
				
		// get a enquiry from enquirys table
		
		
		if(count($response) > 0) {
			
			$response["success"] = 0;
			echo json_encode($response);
		} else {
			// no enquiry found
			$response["success"] = 1;
			$response["message"] = "enquiry not found.";
 
			// echo no users JSON
			echo json_encode($response);
		}
		
	}
	else {
		// required field is missing
		$response["success"] = 2;
		$response["message"] = "Required field(s) is missing";
	 
		// echoing JSON response
		echo json_encode($response);
	}
	
?>
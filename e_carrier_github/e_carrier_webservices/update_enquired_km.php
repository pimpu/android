<?php
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	$response = array();
	
	class updateEnquiryKm {
		
		function __construct() {
			
		}
		
		public function update_enquiry_km($enquiryId) {
			
			$serviceData = mysql_query("SELECT * FROM bookedserviceinfo WHERE fk_enquiryId=".$enquiryId.";");
			$enquiryData = mysql_query("SELECT * FROM bookingenquiry WHERE enquiryId=".$enquiryId.";");
			
			if(mysql_num_rows($serviceData) > 0 && mysql_num_rows($enquiryData) > 0 ){
				
				$arrayEnquiryData = mysql_fetch_array($enquiryData);
				$beginningArea 	= $arrayEnquiryData['beginningArea'];
				$destinatioArea = $arrayEnquiryData['destinationArea'];
				
				// getting km using google map direction api
				$origin = urlencode($beginningArea);
				$destination = urlencode($destinatioArea);
				// $via = urlencode("Palm Beach Rd");
				// $url = "http://maps.googleapis.com/maps/api/directions/json?origin=".$origin."&destination=".$destination."&waypoints=via:".$via;
				$url = "http://maps.googleapis.com/maps/api/directions/json?origin=".$origin."&destination=".$destination;
				
					
				// create curl resource
				$ch = curl_init();
				// set url
				curl_setopt($ch, CURLOPT_URL, $url);


				//return the transfer as a string
				curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

				// $output contains the output string
				$output = curl_exec($ch);

				// close curl resource to free up system resources
				curl_close($ch);

				$directions = json_decode($output, TRUE);
				
				$firstElementRoute = $directions['routes'][0];
				$legsData = $firstElementRoute['legs'][0];
				$strKm = $legsData['distance']['text']; // give result as ** or **.** km / m
				
				$km = 0;
				if (strpos($strKm, 'km') !== false) {
					$km = rtrim($strKm, " km"); // give result as ** or **.**
					$km = $km * 1000;
				}
				else{
					$km = rtrim($strKm, " m"); // give result as ** or **.**
				}
		
				$updateResult = mysql_query("UPDATE bookedserviceinfo SET enquired_meter=".$km." WHERE fk_enquiryId=".$enquiryId.";");
				
			}
		
		}
		
	}
?>
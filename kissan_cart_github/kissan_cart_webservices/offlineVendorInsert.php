<?php
include 'kissanCart_db_connect.php';
error_reporting(0);
// array for final json respone
$response = array();

if ( isset($_POST['jsonArrayVendorArr']) ) {
	$jsonArrayVendorArr = isset($_POST['jsonArrayVendorArr']) ? $_POST['jsonArrayVendorArr'] : '';


	$decodedItemArray = json_decode($jsonArrayVendorArr, true);
	
	$response['success'] = 1;
	$response['message'] = array();

	for ($i=0; $i < count($decodedItemArray); $i++) {
		switch ( $decodedItemArray[$i]["vendor_offline_action"] ) {
			case "Insert":

				$result = mysql_query("INSERT INTO vendors(
											name,
											contact,
											address,
											email,
											status) VALUES(
											'".$decodedItemArray[$i]["vendor_name"]."',
											'".$decodedItemArray[$i]["vendor_contact"]."',
											'".$decodedItemArray[$i]["vendor_Address"]."', 
											'".$decodedItemArray[$i]["vendor_email"]."', 
											1);") or die(mysql_error());
	
				if($result) {
					$getId = mysql_insert_id();

					$VendorArray = array();
					$VendorArray["serverId"] = $getId;
					$VendorArray["localVendorId"] = $decodedItemArray[$i]["id"];
					$VendorArray["action"] = $decodedItemArray[$i]["vendor_offline_action"];
					array_push( $response["message"] , $VendorArray);

				}

			break;

			case "Update":

				if( strcmp( $decodedItemArray[$i]["serverId"] , "0") === 0 ) {

					for ($j=0; $j < $i; $j++) {

						if( $decodedItemArray[$j]["vendor_offline_action"] === "Insert" && 
							$decodedItemArray[$j]["id"] ===  $decodedItemArray[$i]["id"]) {

					    	$getServerId = mysql_query("SELECT * FROM vendors WHERE email='".$decodedItemArray[$j]["vendor_email"]."';");
					    	$getServerIdArray = mysql_fetch_array($getServerId);

				    		$decodedItemArray[$i]["serverId"] = $getServerIdArray['id'];
				    		break;
						}
					}
		    	}

		    	// mysql update row with matched userId
				$updateVendor = mysql_query("UPDATE vendors SET 
						name = '".$decodedItemArray[$i]["vendor_name"]."', 
						contact = '".$decodedItemArray[$i]["vendor_contact"]."', 
						address = '".$decodedItemArray[$i]["vendor_Address"]."', 
						email ='".$decodedItemArray[$i]["vendor_email"]."', 
						status =".$decodedItemArray[$i]["vendor_status"].
						" WHERE id = ".$decodedItemArray[$i]["serverId"]);
				

				$vendorArray = array();
				$vendorArray["localVendorId"] = $decodedItemArray[$i]["id"];
				$vendorArray["action"] = $decodedItemArray[$i]["vendor_offline_action"];
				array_push( $response["message"] , $vendorArray);

			break;

			case "Delete":
			break;

		}
	}

	echo json_encode($response);
}

?>
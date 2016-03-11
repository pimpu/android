<?php
 
include 'kissanCart_db_connect.php';
include 'GCM.php';
error_reporting(0);
// array for final json respone
$response = array();

if ( isset($_POST['referenceNo']) ) {

	$referenceNo   	= $_POST['referenceNo'];
	$UTR			= $_POST['UTR'];
	$itemsJSONArray = $_POST['itemsJSONArray'];
	$userId			= $_POST['userId'];

	$decodedItemArray = json_decode($itemsJSONArray);
	// itemName
	$count = count($decodedItemArray);
	for($i = 0 ; $i < $count ; $i++){
		echo $decodedItemArray[$i];
	}
	

}
else{
	$response["success"] = 0;
	$response["message"] = "Required field(s) is missing";
	
}
echo json_encode($response);

?>
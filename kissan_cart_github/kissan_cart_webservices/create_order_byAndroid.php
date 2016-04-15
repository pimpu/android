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

	$decodedItemArray = json_decode($itemsJSONArray, true);
	$response["success"] = array();

	for ($i=0; $i < count($decodedItemArray); $i++) {

		$result = mysql_query("INSERT INTO orderdetail(	uID,
														enqRef,
														UTR,
														ordItem,
														ordQty,
														ordPrice,
														totamnt)VALUES(
														".$userId.",
														'".$referenceNo."',
														'".$UTR."',
														'".$decodedItemArray[$i]["itemName"]."',
														".$decodedItemArray[$i]["itemQuantity"].",
														'".$decodedItemArray[$i]["itemPrice"]."',
														'".$decodedItemArray[$i]["itemTotalAmount"]."');");
	}

	$id = mysql_insert_id();
	$getOrderData = mysql_query("SELECT ordDate FROM orderdetail WHERE ordID = ".$id);
	$getArrayOrderData = mysql_fetch_array($getOrderData);

	// send gcm notification to reply user
	$gcm = new GCM();

	$registration_ids = array();
	$gcmMessage = array(
							'message' => 'downloadOrderAtAdmin',
							'referenceNo' => $referenceNo, 	
							'utr' => $UTR,
							'items' => $itemsJSONArray,
							'userId' => $userId,
							'creationtime' => $getArrayOrderData["ordDate"]
						);

	$sql = "SELECT gcmId FROM userdetails WHERE gID=1;";
	$getGCMResultData = mysql_query($sql);
	$idGCM=mysql_fetch_array($getGCMResultData);
	array_push($registration_ids, $idGCM['gcmId']);

	// sent notification only for admin
	$gcm->send_notification($registration_ids, $gcmMessage);

	$response["success"] = 1;
	$response["message"] = "Order inserted successfully.";
	$response["creationtime"] = $getArrayOrderData["ordDate"];

}
else {
	$response["success"] = 0;
	$response["message"] = "Required field(s) is missing";
	
}
echo json_encode($response);

?>
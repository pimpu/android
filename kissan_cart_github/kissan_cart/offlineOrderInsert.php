<?php
include 'kissanCart_db_connect.php';
include 'GCM.php';
error_reporting(0);
// array for final json respone
$response = array();

if ( isset($_POST['jsonArrayOrderArr']) ) {

	$jsonArrayOrderArr = isset($_POST['jsonArrayOrderArr']) ? $_POST['jsonArrayOrderArr'] : '';

	$decodedItemArray = json_decode($jsonArrayOrderArr, true);
	
	$response['success'] = 1;
	$response['message'] = array();

	for ($i=0; $i < count($decodedItemArray); $i++) {

		switch ( $decodedItemArray[$i]["order_offline_action"] ) {

			case "Insert":

				$decodeItems = json_decode($decodedItemArray[$i]["order_items"], true);

				for ($j=0; $j < count($decodeItems); $j++) {

					$result = mysql_query("INSERT INTO orderdetail(	
														uID,
														enqRef,
														UTR,
														ordItem,
														ordQty,
														ordPrice,
														totamnt,
														ordDate,
														ordStatus)VALUES(
														".$decodedItemArray[$i]["userId"] .",
														'".$decodedItemArray[$i]["order_reference"] ."',
														'".$decodedItemArray[$i]["order_utr"] ."',
														'".$decodeItems[$j]["itemName"]."',
														".$decodeItems[$j]["itemQuantity"].",
														'".$decodeItems[$j]["itemPrice"]."',
														'".$decodeItems[$j]["itemTotalAmount"] ."',
														'".$decodedItemArray[$i]["order_creted_at"]."',
														".$decodedItemArray[$i]["order_status"].");");

				}

				$id = mysql_insert_id();
				$getOrderData = mysql_query("SELECT ordDate FROM orderdetail WHERE ordID = ".$id);
				$getArrayOrderData = mysql_fetch_array($getOrderData);

				// send gcm notification to reply user
				$gcm = new GCM();

				$registration_ids = array();
				$gcmMessage = array(
										'message' => 'downloadOrderAtAdmin',
										'referenceNo' => $decodedItemArray[$i]["order_reference"], 	
										'utr' => $decodedItemArray[$i]["order_utr"],
										'items' => json_encode($decodeItems),
										'userId' => $decodedItemArray[$i]["userId"],
										'creationtime' => $getArrayOrderData["ordDate"]
									);

				$sql = "SELECT gcmId FROM userdetails WHERE gID=1;";
				$getGCMResultData = mysql_query($sql);
				$idGCM=mysql_fetch_array($getGCMResultData);
				array_push($registration_ids, $idGCM['gcmId']);

				// sent notification only for admin
				$gcm->send_notification($registration_ids, $gcmMessage);


					/*if($result) {
						$societyArray = array();
						$societyArray["id"] = mysql_insert_id();
						$societyArray["action"] = $decodeItems[$j]["itemName"];
						array_push( $response["message"] , $societyArray);
					}*/

				break;

		    case "Update":
		    	break;

		    case "Delete":
		        // code to be executed if n=label3;
		        break;

		}

	}

	echo json_encode($response);

}
<?php
 
include 'kissanCart_db_connect.php';
include 'GCM.php';
error_reporting(0);
// array for final json respone
$response = array();

if ( isset($_POST['jsonArrayEnquiryArr']) ) {

	$jsonArrayEnquiryArr = isset($_POST['jsonArrayEnquiryArr']) ? $_POST['jsonArrayEnquiryArr'] : '';

	$decodedItemArray = json_decode($jsonArrayEnquiryArr, true);
	
	$response['success'] = 1;
	$response['message'] = array();

	for ($i=0; $i < count($decodedItemArray); $i++) {

		switch ( $decodedItemArray[$i]["enquiry_offline_action"] ) {
		    case "Insert":

		        $result = mysql_query("INSERT INTO enquiry(
				        	eRef,                                   
							uID,
							gID,
							repTo,
							replied,
							SocID,
							scid,
							prod_id,
							qty,
							eTime,
							status)VALUES(
							'".$decodedItemArray[$i]["enquiry_reference"]."',
							".$decodedItemArray[$i]["enquiry_userId"].",
							".$decodedItemArray[$i]["enquiry_groupId"].",
							".$decodedItemArray[$i]["enquiry_replyTo"].",
							".$decodedItemArray[$i]["enquiry_replied"].",
							".$decodedItemArray[$i]["enquiry_societyId"].",
							".$decodedItemArray[$i]["enquiry_subCategoryId"].",
							".$decodedItemArray[$i]["enquiry_productId"].",
							'".$decodedItemArray[$i]["enquiry_quantity"]."',
							'".$decodedItemArray[$i]["enquiry_creted_at"]."',
							1);");

		        if ($result) {

					// get id of inserted query
					$insertedId = mysql_insert_id();

					// query for time of creation
					$getInsertedEnquiryData = mysql_query("SELECT * FROM enquiry WHERE eID=".$insertedId.";");
					$arrayEnquiryData = mysql_fetch_array($getInsertedEnquiryData);

				
					$enquiruArray = array();
					$enquiruArray["serverId"] = $insertedId;
					$enquiruArray["enquiryId"] = $decodedItemArray[$i]["enquiry_id"];
					$enquiruArray["action"] = $decodedItemArray[$i]["enquiry_offline_action"];
					array_push( $response["message"] , $enquiruArray);
					

					// send gcm notification to reply user
					$gcm = new GCM();

					$enquiry = array();
					$registration_ids = array();

					$enquiry["id"] = $insertedId;
					$enquiry["eTime"] = $arrayEnquiryData["eTime"];
					$enquiry["eRef"] = $decodedItemArray[$i]["enquiry_reference"];
					$enquiry["uID"] = $decodedItemArray[$i]["enquiry_userId"];
					$enquiry["gID"] = $decodedItemArray[$i]["enquiry_groupId"];
					$enquiry["repTo"] = $decodedItemArray[$i]["enquiry_replyTo"];
					$enquiry["replied"] = $decodedItemArray[$i]["enquiry_replied"];
					$enquiry["SocID"] = $decodedItemArray[$i]["enquiry_societyId"];
					$enquiry["scid"] = $decodedItemArray[$i]["enquiry_subCategoryId"];
					$enquiry["prod_id"] = $decodedItemArray[$i]["enquiry_productId"];
					$enquiry["qty"] = $decodedItemArray[$i]["enquiry_quantity"];

					$gcmMessage = array(
											'message' => 'downloadOfflineInsertEnquiry',
											'enquiryDetails' => $enquiry
										);

					// get gcmId from user data  when user is logging.
					$sql = "SELECT gcmId FROM userdetails WHERE uID=".$decodedItemArray[$i]["enquiry_replyTo"].";";
					$getGCMResultData = mysql_query($sql);
					
					if( mysql_num_rows($getGCMResultData) > 0 ) {
						
						$idGCM=mysql_fetch_array($getGCMResultData);
						array_push($registration_ids, $idGCM['gcmId']);
						
					}

					$gcm->send_notification($registration_ids, $gcmMessage);

				}

		        break;

		    case "Update":

		        $query1 = mysql_query("UPDATE enquiry SET replied = '".$decodedItemArray[$i]["enquiry_replied"]."' where eID = ".$decodedItemArray[$i]["enquiry_server_id"].";");

		        if($query1) {

					$enquiruArray = array();
					$enquiruArray["enquiryId"] = $decodedItemArray[$i]["enquiry_id"];
					$enquiruArray["action"] = $decodedItemArray[$i]["enquiry_offline_action"];
					array_push( $response["message"] , $enquiruArray);


		        	// send gcm notification to reply user
					$gcm = new GCM();

					$enquiry = array();
					$registration_ids = array();

					$enquiry["enquiry_id"] = $decodedItemArray[$i]["enquiry_server_id"];
					$enquiry["replied"] = $decodedItemArray[$i]["enquiry_replied"];


					$gcmMessage = array(
											'message' => 'downloadOfflineUpdateEnquiry',
											'enquiryDetails' => $enquiry
										);

					// get gcmId from user data  when user is logging.
					$sql = "SELECT gcmId FROM userdetails WHERE uID=".$decodedItemArray[$i]["enquiry_replyTo"].";";
					$getGCMResultData = mysql_query($sql);
					
					if( mysql_num_rows($getGCMResultData) > 0 ) {
						
						$idGCM=mysql_fetch_array($getGCMResultData);
						array_push($registration_ids, $idGCM['gcmId']);
						
					}

					$gcm->send_notification($registration_ids, $gcmMessage);

		        }

		        break;
		    case "Delete":
		        // code to be executed if n=label3;
		        break;
		}


	}

	echo json_encode($response);
}

?>
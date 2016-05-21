<?php
 
	include 'kissanCart_db_connect.php';
	include 'GCM.php';
	error_reporting(0);
	// array for final json respone
	$response = array();
	 
	// final file url that is being uploaded


		/*$file_upload_url = "filename";
		$refNo = "refno";
		$name = "name";
		$contact = "contact";
		$email = "email@email.com";
		$address = "address";
		$message = "message";
		$userType = "obp";
		$uId = 4;
		$eId = 0;*/

	if ( isset($_POST['refNo']) ) {

		$refNo = isset($_POST['refNo']) ? $_POST['refNo'] : '';
		$uId = isset($_POST['uId']) ? $_POST['uId'] : '';
		$userType = isset($_POST['userType']) ? $_POST['userType'] : '';
		$eId = isset($_POST['eId']) ? $_POST['eId'] : '';
		$repToVal = isset($_POST['replyTo']) ? $_POST['replyTo'] : '';
		$groupId = isset($_POST['groupId']) ? $_POST['groupId'] : '';
		$societyId = isset($_POST['societyId']) ? $_POST['societyId'] : '';
		$subcategoryId = isset($_POST['subcategoryId']) ? $_POST['subcategoryId'] : '';
		$productId = isset($_POST['productId']) ? $_POST['productId'] : '';
		$productQty = isset($_POST['productQty']) ? $_POST['productQty'] : '';


		$result = mysql_query("INSERT INTO enquiry(	eRef,
													uID,
													gID,
													repTo,
													SocID,
													scid,
													prod_id,
													qty,
													status)VALUES(
													'".$refNo."',
													".$uId.",
													".$groupId.",
													".$repToVal.",
													".$societyId.",
													".$subcategoryId.",
													".$productId.",
													'".$productQty."',
													1);") or die(mysql_error());
		
		if ($result) {

			// get id of inserted query
			$insertedId = mysql_insert_id();

			// query for time of creation
			$getInsertedEnquiryData = mysql_query("SELECT * FROM enquiry WHERE eID=".$insertedId.";");
			$arrayEnquiryData = mysql_fetch_array($getInsertedEnquiryData);
			
			// This scenario come in new reply.
			// if enquiry is newly created, eId comes with 0 value
			// eId value is not 0 then set clicked enquiry replied as 1 value
			// i.e. it's not going to show in new reply view.
			if( strcmp($eId,"0") !== 0 ) {
				$query1 = mysql_query("UPDATE enquiry SET replied = '1' where eID = ".$eId.";") or die(mysql_error());
			}
			
		
			// File successfully uploaded
			$response['success'] = 0;
			$response["message"] = array();
			
			$enquiruArray = array();
			$enquiruArray["id"] = $insertedId;
			$enquiruArray["timestamp"] = $arrayEnquiryData["eTime"];
			
			array_push( $response["message"] , $enquiruArray);

			// send gcm notification to reply user
			$gcm = new GCM();

			$enquiry = array();
			$registration_ids = array();

			$enquiry["id"] = $insertedId;
			$enquiry["eTime"] = $arrayEnquiryData["eTime"];
			$enquiry["eRef"] = $refNo;
			$enquiry["uID"] = $uId;
			$enquiry["gID"] = $groupId;
			$enquiry["repTo"] = $repToVal;
			$enquiry["replied"] = 0;
			$enquiry["SocID"] = $societyId;
			$enquiry["scid"] = $subcategoryId;
			$enquiry["prod_id"] = $productId;
			$enquiry["qty"] = $productQty;
			$enquiry["oldEnquiryId"] = $eId;

			$gcmMessage = array(
									'message' => 'downloadEnquiry',
									'enquiryDetails' => $enquiry
								);

			// get gcmId from user data  when user is logging.
			$sql = "SELECT gcmId FROM userdetails WHERE uID=".$repToVal.";";
			$getGCMResultData = mysql_query($sql);
			
			if( mysql_num_rows($getGCMResultData) > 0 ) {
				
				$idGCM=mysql_fetch_array($getGCMResultData);
				array_push($registration_ids, $idGCM['gcmId']);
				
			}

			$gcm->send_notification($registration_ids, $gcmMessage);

		}
	} else {
	    // File parameter is missing
	    $response['success'] = 1;
	    $response['message'] = 'parameter missing';
	}
	 
	// Echo final json response to client
	echo json_encode($response);
?>
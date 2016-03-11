<?php
 
include 'kissanCart_db_connect.php';
include 'GCM.php';
error_reporting(0);
// array for final json respone
$response = array();
 
// final file url that is being uploaded
$file_upload_url = 'AndroidFileUpload/uploads/';


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

if (isset($_FILES['image']['name'])) {

    $file_upload_url = $file_upload_url . basename($_FILES['image']['name']);
	$refNo = isset($_POST['refNo']) ? $_POST['refNo'] : '';
	$name = isset($_POST['name']) ? $_POST['name'] : '';
	$contact = isset($_POST['contact']) ? $_POST['contact'] : '';
	$email = isset($_POST['email']) ? $_POST['email'] : '';
	$address = isset($_POST['address']) ? $_POST['address'] : '';
	$message = isset($_POST['message']) ? $_POST['message'] : '';
	$uId = isset($_POST['uId']) ? $_POST['uId'] : '';
	$userType = isset($_POST['userType']) ? $_POST['userType'] : '';
	$eId = isset($_POST['eId']) ? $_POST['eId'] : '';
 
    // reading other post parameters
	$checkMatchEnquiryQuery = mysql_query("SELECT * FROM enquiry WHERE uID=".$uId." && eMsg='".$message."' && eDoc = '".$_FILES['image']['name']."';");
	
	if(mysql_num_rows($checkMatchEnquiryQuery) > 0 ){
		$response["success"] = 1;
		$response["message"] = "This enquiry is already exist.";
	}
	else {
		try {
			// Throws exception incase file is not being moved
			if (!move_uploaded_file($_FILES['image']['tmp_name'], $file_upload_url)) {
				// make error flag true
				$response['success'] = 1;
				$response['message'] = 'Could not move the file!';
			}
			else{

				// query for to setting the replyTo var.
				// if result get null value with eID that means obp create fisrt time enquiry with reference.
				// then it goes to by default to admin
				// if result gets value with eID then it goes in reply scenario.
				// get user-id from currently clicked enqiry and store it into replyTo variable.
				$queryUID = mysql_query("SELECT uID FROM enquiry WHERE eID = ".$eId.";");
				$repToVal = null;
				if(mysql_num_rows($queryUID) > 0 ){
					$arrayUID = mysql_fetch_array($queryUID);
					$repToVal = $arrayUID['uID'];
				}
				else{
					$queryUIDForAdmin = mysql_query("SELECT uID FROM userdetails WHERE 	gID = 1;");
					$arrayUIDForAdmin = mysql_fetch_array($queryUIDForAdmin);
					$repToVal = $arrayUIDForAdmin['uID'];
				}
				
				// get group id
				$queryGID = mysql_query("SELECT gId FROM usergroup WHERE gName='".$userType	."';");
				$arrayGID = mysql_fetch_array($queryGID);
				$groupId = $arrayGID['gId'];

				$result = mysql_query("INSERT INTO enquiry(	eRef,
															uID,
															gID,
															repTo,
															eMsg,
															eSociety,
															eSocAdrs,
															eSocCont,
															eSocEmail,
															eDoc,
															eCost,
															status)VALUES(
															'".$refNo."',
															".$uId.",
															".$groupId.",
															".$repToVal.",
															'".$message."',
															'".$name."',
															'".$address."',
															'".$contact."',
															'".$email."',
															'".$file_upload_url."',
															'',
															1);");
				
				if ($result){

					// get id of inserted query
					$insertedId = mysql_insert_id();

					// query for time of creation
					$getInsertedEnquiryData = mysql_query("SELECT * FROM enquiry WHERE eID=".$insertedId.";");
					$arrayEnquiryData = mysql_fetch_array($getInsertedEnquiryData);
					
					// This scenario come in new reply.
					// if enquiry is newly created then eId comes with 0 value
					// eId value is not 0 then then set clicked enquiry replied as 1 value 
					// i.e. it's not going to show in new reply view.
					if( strcmp($eId,"0") !== 0 ){
						$query1 = mysql_query("UPDATE enquiry SET replied = '1' where eID = ".$eId.";");
					}
					
				
					// File successfully uploaded
					$response['success'] = 0;
					$response["message"] = array();
					
					$enquiruArray = array();
					$enquiruArray["id"] = $insertedId;
					$enquiruArray["timestamp"] = $arrayEnquiryData["eTime"];
					$enquiruArray["groupId"] = $groupId;
					$enquiruArray["repToVal"] = $repToVal;
					
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
					$enquiry["eMsg"] = $message;
					$enquiry["eSociety"] = $name;
					$enquiry["eSocAdrs"] = $address;
					$enquiry["eSocCont"] = $contact;
					$enquiry["eSocEmail"] = $email;
					$enquiry["eDoc"] = $_FILES['image']['name'];
					$enquiry["oldEnquiryId"] = $eId;

					$gcmMessage = array(
											'message' => 'downloadEnquiry',
											'enquiryDetails' => $enquiry
										);

					// get gcmId from user data  when user is logging.
					$sql = "SELECT gcmId FROM userdetails WHERE uID=".$repToVal.";";
					$getGCMResultData = mysql_query($sql);
					
					if( mysql_num_rows($getGCMResultData) > 0 ){
						
						$idGCM=mysql_fetch_array($getGCMResultData);
						array_push($registration_ids, $idGCM['gcmId']);
						
					}

					$gcm->send_notification($registration_ids, $gcmMessage);

				}
			}
		} catch (Exception $e) {
			// Exception occurred. Make error flag true
			$response['success'] = 1;
			$response['message'] = $e->getMessage();
		}
	}
} else {
    // File parameter is missing
    $response['success'] = 1;
    $response['message'] = 'Not received any file';
}
 
// Echo final json response to client
echo json_encode($response);
?>
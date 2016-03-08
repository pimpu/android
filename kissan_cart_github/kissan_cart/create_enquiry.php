<?php
 
include 'kissanCart_db_connect.php';
error_reporting(0);
// array for final json respone
$response = array();
 
// final file url that is being uploaded
$file_upload_url = 'AndroidFileUpload/uploads/';
 
 
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
	else{
		try {
			// Throws exception incase file is not being moved
			if (!move_uploaded_file($_FILES['image']['tmp_name'], $file_upload_url)) {
				// make error flag true
				$response['success'] = 1;
				$response['message'] = 'Could not move the file!';
			}
			else{

				$queryGID = mysql_query("SELECT gId FROM usergroup WHERE gName='".$userType	."';");
				$arrayGID = mysql_fetch_array($queryGID);
				$groupId = $arrayGID['gId'];
				
				$repToVal = null;
				if( $groupId === 1){
					$repToVal = 2;
				}
				else{
					$repToVal = 1;
				}
				
				
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
															'".$_FILES['image']['name']."',
															'',
															1);");
				
				if ($result){
					$insertedId = mysql_insert_id();
					$getInsertedEnquiryData = mysql_query("SELECT * FROM enquiry WHERE eID=".$insertedId.";");
					$arrayEnquiryData = mysql_fetch_array($getInsertedEnquiryData);
					
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
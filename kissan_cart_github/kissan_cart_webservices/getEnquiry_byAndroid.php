<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST['str_obpId']) ){
		
		$str_obpId = $_POST['str_obpId'];
		
		// get all enquirys from enquirys table
		$result = mysql_query("SELECT * FROM enquiry WHERE 	uID = ".$str_obpId);
		$getReplyEnquiry = mysql_query("SELECT * FROM enquiry WHERE repTo = ".$str_obpId." AND replied = 0;");
		 
		/*// check for empty result
		if (mysql_num_rows($result) > 0 ) {*/
			// looping through all results
			// societies node
			$response["message"] = array();
		 
			while ($row = mysql_fetch_array($result)) {

				// temp user array
				$enquiry = array();
				$enquiry["id"] = $row["eID"];
				$enquiry["creted_at"] = $row["eTime"];
				$enquiry["ref"] = $row["eRef"];
				$enquiry["eUid"] = $row["uID"];
				$enquiry["gId"] = $row["gID"];
				$enquiry["repToVal"] = $row["repTo"];
				$enquiry["replied"] = $row["replied"];
				$enquiry["message"] = $row["eMsg"];
				$enquiry["society_name"] = $row["eSociety"];
				$enquiry["society_address"] = $row["eSocAdrs"];
				$enquiry["society_contact"] = $row["eSocCont"];
				$enquiry["society_email"] = $row["eSocEmail"];
				$enquiry["document"] = $row["eDoc"];
				$enquiry["status"] = $row["status"];
		 
				// push single enquiry into final response array
				array_push($response["message"], $enquiry);
			}
			$response["success"] = 1;
		 
		/*} else {
			// no societies found
			$response["success"] = 0;
			$response["message"] = "No enquiry found";
		 
		}*/

		if (mysql_num_rows($getReplyEnquiry) > 0 ) {
			while ($row = mysql_fetch_array($getReplyEnquiry)) {

				// temp user array
				$enquiry = array();
				$enquiry["id"] = $row["eID"];
				$enquiry["creted_at"] = $row["eTime"];
				$enquiry["ref"] = $row["eRef"];
				$enquiry["eUid"] = $row["uID"];
				$enquiry["gId"] = $row["gID"];
				$enquiry["repToVal"] = $row["repTo"];
				$enquiry["replied"] = $row["replied"];
				$enquiry["message"] = $row["eMsg"];
				$enquiry["society_name"] = $row["eSociety"];
				$enquiry["society_address"] = $row["eSocAdrs"];
				$enquiry["society_contact"] = $row["eSocCont"];
				$enquiry["society_email"] = $row["eSocEmail"];
				$enquiry["document"] = $row["eDoc"];
				$enquiry["status"] = $row["status"];
		 
				// push single enquiry into final response array
				array_push($response["message"], $enquiry);
			}
		}

	}
	else{
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
		
	}	
	echo json_encode($response);

?>
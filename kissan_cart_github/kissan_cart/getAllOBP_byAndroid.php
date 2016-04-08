<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if ( isset($_POST['userId']) ) {

		// get all societys from societys table
		$result = mysql_query("SELECT * FROM userdetails WHERE uID <> ".$_POST['userId']." ;");
		 
		// check for empty result
		if (mysql_num_rows($result) > 0) {
			// looping through all results
			// societies node
			$response["message"] = array();
		 
			while ($row = mysql_fetch_array($result)) {

				$obp["userID_serverId"] = $row["uID"];
				$obp["obp_name"] = $row["uName"];
				$obp["obp_store_name"] = $row["uStoreName"];
				$obp["obp_email_id"] = $row["uEmailID"];
				$obp["obp_email_passowrd"] = $row["uPassword"];
				$obp["obp_contact_number"] = $row["uCont"];
				$obp["obp_address"] = $row["uAddrs"];
				$obp["obp_pincode"] = $row["uPin"];
				$obp["obp_city"] = $row["uCity"];
				$obp["obp_state"] = $row["uState"];
				$obp["obp_country"] = $row["uCountry"];
				$obp["obp_status"] = $row["status"];
		 
				// push single society into final response array
				array_push($response["message"], $obp);
			}
			// success
			$response["success"] = 1;
		 
		} else {
			// no societies found
			$response["success"] = 0;
			$response["message"] = "No obp found";
		 
		}

	}
	else {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	}

	// echo no users JSON
	echo json_encode($response);

?>
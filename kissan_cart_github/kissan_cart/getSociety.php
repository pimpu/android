<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST['str_obpId']) ){
		
		$str_obpId = $_POST['str_obpId'];
		
		// get all societys from societys table
		$result = mysql_query("SELECT * FROM society WHERE 	uID = ".$str_obpId);
		 
		// check for empty result
		if (mysql_num_rows($result) > 0) {
			// looping through all results
			// societies node
			$response["message"] = array();
		 
			while ($row = mysql_fetch_array($result)) {
				// temp user array
				$society = array();
				$society["soc_id"] = $row["id"];
				$society["soc_name"] = $row["soc_name"];
				$society["soc_contact"] = $row["soc_contact"];
				$society["soc_email"] = $row["soc_email"];
				$society["soc_adrs"] = $row["soc_adrs"];
		 
				// push single society into final response array
				array_push($response["message"], $society);
			}
			// success
			$response["success"] = 1;
		 
			// echoing JSON response
			echo json_encode($response);
		} else {
			// no societies found
			$response["success"] = 0;
			$response["message"] = "No society found";
		 
			// echo no users JSON
			echo json_encode($response);
		}
	}
	else{
		$response["success"] = 2;
		$response["message"] = "Required field(s) is missing";
		
		echo json_encode($response);
	}	

?>
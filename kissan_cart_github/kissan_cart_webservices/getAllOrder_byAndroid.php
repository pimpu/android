<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if( isset($_POST['userId']) && isset($_POST['userType']) ) {
		
		$result = null ;
		$userId = $_POST['userId'];
		$userType = $_POST['userType'];
		
		if( strcmp($userType, "obp") === 0 ) {
			$result = mysql_query("SELECT * FROM orderdetail WHERE uID = ".$userId.";");
		}
		else {
			$result = mysql_query("SELECT * FROM orderdetail;");
		}

		// check for empty result
		if (mysql_num_rows($result) > 0 ) {
			// looping through all results
			// societies node
			$response["message"] = array();
		 
			while ($row = mysql_fetch_array($result)) {

				// temp user array
				$order = array();
				$order["userID"] = $row["uID"];
				$order["enqRef"] = $row["enqRef"];
				$order["UTR"] = $row["UTR"];
				$order["ordItem"] = $row["ordItem"];
				$order["ordQty"] = $row["ordQty"];
				$order["ordPrice"] = $row["ordPrice"];
				$order["totamnt"] = $row["totamnt"];
				$order["ordDate"] = $row["ordDate"];
				$order["ordStatus"] = $row["ordStatus"];
		 
				// push single order into final response array
				array_push($response["message"], $order);
			}
			$response["success"] = 1;
		 
		} else {
			// no societies found
			$response["success"] = 0;
			$response["message"] = "No order found";
		 
		}

	}
	else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
		
	}	
	echo json_encode($response);

?>
<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// get all societys from societys table
	$result = mysql_query("SELECT * FROM product_group;");
	 
	// check for empty result
	if (mysql_num_rows($result) > 0) {
		// looping through all results
		// societies node
		$response["message"] = array();
	 
		while ($row = mysql_fetch_array($result)) {

			$group = array();
			$group["serverId"] = $row["pgid"];
			$group["group_name"] = $row["gname"];
			$group["status"] = $row["status"];

			// push single society into final response array
			array_push($response["message"], $group);
		}
		// success
		$response["success"] = 1;
	 
	} else {
		// no group found
		$response["success"] = 0;
		$response["message"] = "No group found.";
	 
	}

	// echo no users JSON
	echo json_encode($response);

?>
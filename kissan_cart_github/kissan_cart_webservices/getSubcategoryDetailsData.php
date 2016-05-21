<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// get all societys from societys table
	$result = mysql_query("SELECT * FROM product_subcategory;");
	 
	// check for empty result
	if (mysql_num_rows($result) > 0) {
		// looping through all results
		// societies node
		$response["message"] = array();
	 
		while ($row = mysql_fetch_array($result)) {

			$group = array();
			$group["serverId"] = $row["scid"];
			$group["category_id"] = $row["cid"];
			$group["product_group_id"] = $row["pgid"];
			$group["subcategory_name"] = $row["subcat_name"];
			$group["subcategory_image"] = $row["subcat_img"];
			$group["status"] = $row["status"];

			// push single society into final response array
			array_push($response["message"], $group);
		}
		// success
		$response["success"] = 1;
	 
	} else {
		// no group found
		$response["success"] = 0;
		$response["message"] = "No subcategory found.";
	 
	}

	// echo no users JSON
	echo json_encode($response);

?>
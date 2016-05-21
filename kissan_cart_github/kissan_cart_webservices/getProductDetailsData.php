<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	error_reporting(0);
	$response = array();
	
	// get all societys from societys table
	$result = mysql_query("SELECT * FROM product_details;");
	 
	// check for empty result
	if (mysql_num_rows($result) > 0) {
		// looping through all results
		// societies node
		$response["message"] = array();
	 
		while ($row = mysql_fetch_array($result)) {

			$group = array();
			$group["serverId"] = $row["prod_id"];
			$group["subcategory_id"] = $row["scid"];
			$group["category_id"] = $row["cid"];
			$group["product_group_id"] = $row["pgid"];
			$group["product_name"] = $row["prod_name"];
			$group["prod_path"] = $row["prod_path"];
			$group["prod_path1"] = $row["prod_path1"];
			$group["prod_path2"] = $row["prod_path2"];
			$group["prod_path3"] = $row["prod_path3"];
			$group["prod_path4"] = $row["prod_path4"];
			$group["prod_description"] = $row["prod_description"];
			$group["discount_type"] = $row["discount_type"];
			$group["status"] = $row["status"];

			// push single society into final response array
			array_push($response["message"], $group);
		}
		// success
		$response["success"] = 1;
	 
	} else {
		// no group found
		$response["success"] = 0;
		$response["message"] = "No product details found.";
	 
	}

	// echo no users JSON
	echo json_encode($response);

?>
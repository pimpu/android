<?php
 
include 'kissanCart_db_connect.php';
include 'GCM.php';
error_reporting(0);
// array for final json respone
$response = array();

if ( isset($_POST['str_obpId']) ) {
	$str_obpId = $_POST['str_obpId'];
		
	$result = mysql_query("SELECT * FROM userdetails WHERE uID = ".$str_obpId);

	if (mysql_num_rows($result) > 0 ) {
		$response["message"] = array();

		$row = mysql_fetch_array($result);

		// temp user array
		$user = array();
		$user["name"] = $row["uName"];
		$user["storeName"] = $row["uStoreName"];
		$user["contact"] = $row["uCont"];
		$user["email"] = $row["uEmailID"];
		$user["address"] = $row["uAddrs"];
 
		// push single enquiry into final response array
		array_push($response["message"], $user);
		$response["success"] = 1;
	}

}
else {
	$response["success"] = 0;
	$response["message"] = "Required field(s) is missing";
}

echo json_encode($response);

?>

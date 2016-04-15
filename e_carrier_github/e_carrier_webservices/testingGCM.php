<?php
 
	// include db connect class
	include 'eCarrier_db_connect.php';
	include 'eCarrier_GCM.php';
	error_reporting(0);
	
	// send notification to driver who has vehicle of vehicleType that customer wants.
	$gcm = new eCarrier_GCM();
	$gcmMessage = array('message' => 'requestToAllDriver');
	$registatoin_ids = array();
	
	$sql = "SELECT gcmId FROM userlogin";
	$getGCMResultQuery = mysql_query($sql);
	
	while($row = mysql_fetch_array($getGCMResultQuery)){
		array_push($registatoin_ids, $row['gcmId']);
	}
	
	$getGCMresult = $gcm->send_notification($registatoin_ids, $gcmMessage);
	echo $getGCMresult;
?>
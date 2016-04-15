<?php
	// include db connect class
	include 'kissanCart_db_connect.php';
	include 'GCM.php';
	error_reporting(0);
	$response = array();

	if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['pwd']) ) {
		// mysql update row with matched userId
		$updateSociety = mysql_query("UPDATE userdetails SET 
				uName = '".$_POST['name']."', 
				uStoreName = '".$_POST['store_name']."', 
				uEmailID = '".$_POST['email']."', 
				uPassword ='".$_POST['pwd']."', 
				uCont ='".$_POST['contact']."', 
				uAddrs ='".$_POST['address']."', 
				uPin =".$_POST['pin'].", 
				uCity ='".$_POST['city']."', 
				uState ='".$_POST['state']."', 
				uCountry ='".$_POST['country']."', 
				status =".$_POST['status'].
				" WHERE uID = ".$_POST['serverId']);

		$gcm = new GCM();
		$obp = array();
		$registration_ids = array();

		$obp["userID_serverId"] = $_POST['serverId'];
		$obp["obp_name"] = $_POST['name'];
		$obp["obp_store_name"] = $_POST['store_name'];
		$obp["obp_email_id"] = $_POST['email'];
		$obp["obp_email_passowrd"] = $_POST['pwd'];
		$obp["obp_contact_number"] = $_POST['contact'];
		$obp["obp_address"] = $_POST['address'];
		$obp["obp_pincode"] = $_POST['pin'];
		$obp["obp_city"] = $_POST['city'];
		$obp["obp_state"] = $_POST['state'];
		$obp["obp_country"] = $_POST['country'];
		$obp["obp_status"] = $_POST['status'];
		$obp["fromGid"] = $_POST['gId'];

		$gcmMessage = array(
								'message' => 'updateOBPAtAdmin',
								'changedOBPDetails' => $obp
							);

		if ( strcmp( $_POST['gId'], "2" ) === 0) {

			$sql = "SELECT * FROM userdetails WHERE gID = 1;";

			$getGCMResultData = mysql_query($sql);
			
			if( mysql_num_rows($getGCMResultData) > 0 ) {
				
				$idGCM=mysql_fetch_array($getGCMResultData);
				array_push($registration_ids, $idGCM['gcmId']);
				
			}

			$gcm->send_notification($registration_ids, $gcmMessage);
		}

	}

?>
<?php
include 'kissanCart_db_connect.php';
include 'GCM.php';
error_reporting(0);
// array for final json respone
$response = array();

if ( isset($_POST['jsonArrayObpArr']) ) {
	$jsonArrayObpArr = isset($_POST['jsonArrayObpArr']) ? $_POST['jsonArrayObpArr'] : '';

	/*$jsonArrayObpArr = "[{ \"obp_id\":\"7\", \"userID_serverId\":\"0\", \"obp_name\":\"offline1 obp\", \"obp_store_name\":\"hdhd\", \"obp_email_id\":\"offline1@obp.com\", \"obp_email_passowrd\":\"test\", \"obp_contact_number\":\"5568686685\", \"obp_address\":\"siohv\", \"obp_pincode\":\"768754\", \"obp_city\":\"dudififfi\", \"obp_state\":\"dydudufi\", \"obp_country\":\"ueuddufu\", \"obp_status\":\"1\", \"obp_offline_action\":\"Insert\"},{ \"obp_id\":\"8\", \"userID_serverId\":\"0\", \"obp_name\":\"offline2 obp\", \"obp_store_name\":\"duigigi\", \"obp_email_id\":\"offline2@obp.com\", \"obp_email_passowrd\":\"test\", \"obp_contact_number\":\"4355675555\", \"obp_address\":\"drfufh\", \"obp_pincode\":\"555652\", \"obp_city\":\"7rtifstdu\", \"obp_state\":\"r7titdysyd\", \"obp_country\":\"rufurfu\", \"obp_status\":\"0\", \"obp_offline_action\":\"Insert\"},{ \"obp_id\":\"8\", \"userID_serverId\":\"0\", \"obp_name\":\"offline2 obp\", \"obp_store_name\":\"duigigi\", \"obp_email_id\":\"offline2@obp.com\", \"obp_email_passowrd\":\"test\", \"obp_contact_number\":\"4355675555\", \"obp_address\":\"drfufh\", \"obp_pincode\":\"555652\", \"obp_city\":\"7rtifstdu\", \"obp_state\":\"r7titdysyd\", \"obp_country\":\"rufurfu\", \"obp_status\":\"0\", \"obp_offline_action\":\"Update\"}]";*/
	

	$decodedItemArray = json_decode($jsonArrayObpArr, true);
	
	$response['success'] = 1;
	$response['message'] = array();

	for ($i=0; $i < count($decodedItemArray); $i++) {
		switch ( $decodedItemArray[$i]["obp_offline_action"] ) {
			case "Insert":

				$result = mysql_query("INSERT INTO userdetails(
											gID,
											uName,
											uStoreName,
											uEmailID,
											uPassword,
											uCont,
											uAddrs,
											uPin,
											uCity,
											uState,
											uCountry,
											status) VALUES(
											2,
											'".$decodedItemArray[$i]["obp_name"]."',
											'".$decodedItemArray[$i]["obp_store_name"]."',
											'".$decodedItemArray[$i]["obp_email_id"]."', 
											'".$decodedItemArray[$i]["obp_email_passowrd"]."', 
											'".$decodedItemArray[$i]["obp_contact_number"]."', 
											'".$decodedItemArray[$i]["obp_address"]."',
											".$decodedItemArray[$i]["obp_pincode"].",
											'".$decodedItemArray[$i]["obp_city"]."',
											'".$decodedItemArray[$i]["obp_state"]."',
											'".$decodedItemArray[$i]["obp_country"]."',
											1);");
	
				if($result) {
					$getId = mysql_insert_id();

					$OBPArray = array();
					$OBPArray["serverId"] = $getId;
					$OBPArray["localOBPId"] = $decodedItemArray[$i]["obp_id"];
					$OBPArray["action"] = $decodedItemArray[$i]["obp_offline_action"];
					array_push( $response["message"] , $OBPArray);

				}

			break;

			case "Update":

				if( strcmp( $decodedItemArray[$i]["userID_serverId"] , "0") === 0 ) {

					for ($j=0; $j < $i; $j++) {

						if( $decodedItemArray[$j]["obp_offline_action"] === "Insert" && 
							$decodedItemArray[$j]["obp_id"] ===  $decodedItemArray[$i]["obp_id"]) {
					    	$getServerId = mysql_query("SELECT * FROM userdetails WHERE uEmailID='".$decodedItemArray[$j]["obp_email_id"]."' AND uName = '".$decodedItemArray[$j]["obp_name"]."' ;");
					    	$getServerIdArray = mysql_fetch_array($getServerId);

				    		$decodedItemArray[$i]["userID_serverId"] = $getServerIdArray['uID'];
				    		break;
						}
					}
		    	}


		    	// mysql update row with matched userId
				$updateSociety = mysql_query("UPDATE userdetails SET 
						uName = '".$decodedItemArray[$i]["obp_name"]."', 
						uStoreName = '".$decodedItemArray[$i]["obp_store_name"]."', 
						uEmailID = '".$decodedItemArray[$i]["obp_email_id"]."', 
						uPassword ='".$decodedItemArray[$i]["obp_email_passowrd"]."', 
						uCont ='".$decodedItemArray[$i]["obp_contact_number"]."', 
						uAddrs ='".$decodedItemArray[$i]["obp_address"]."', 
						uPin =".$decodedItemArray[$i]["obp_pincode"].", 
						uCity ='".$decodedItemArray[$i]["obp_city"]."', 
						uState ='".$decodedItemArray[$i]["obp_state"]."', 
						uCountry ='".$decodedItemArray[$i]["obp_country"]."', 
						status =".$decodedItemArray[$i]["obp_status"].
						" WHERE uID = ".$decodedItemArray[$i]["userID_serverId"]);

				$gcm = new GCM();
				$obp = array();
				$registration_ids = array();

				$obp["userID_serverId"] = $decodedItemArray[$i]["userID_serverId"];
				$obp["obp_name"] = $decodedItemArray[$i]["obp_name"];
				$obp["obp_store_name"] = $decodedItemArray[$i]["obp_store_name"];
				$obp["obp_email_id"] = $decodedItemArray[$i]["obp_email_id"];
				$obp["obp_email_passowrd"] = $decodedItemArray[$i]["obp_email_passowrd"];
				$obp["obp_contact_number"] = $decodedItemArray[$i]["obp_contact_number"];
				$obp["obp_address"] = $decodedItemArray[$i]["obp_address"];
				$obp["obp_pincode"] = $decodedItemArray[$i]["obp_pincode"];
				$obp["obp_city"] = $decodedItemArray[$i]["obp_city"];
				$obp["obp_state"] = $decodedItemArray[$i]["obp_state"];
				$obp["obp_country"] = $decodedItemArray[$i]["obp_country"];
				$obp["obp_status"] = $decodedItemArray[$i]["obp_status"];
				// this is used for blocking user
				$obp["fromGid"] = $_POST['gId'];

				$gcmMessage = array(
										'message' => 'updateOfflineOBP',
										'changedOBPDetails' => $obp
									);

				if ( strcmp( $_POST['gId'] , "1") === 0 ) {
					// get gcmId from user data  when user is logging.
					$sql = "SELECT * FROM userdetails WHERE uID = ".$decodedItemArray[$i]["userID_serverId"];
				}
				else {
					$sql = "SELECT * FROM userdetails WHERE gID = 1;";
				}

				$getGCMResultData = mysql_query($sql);
				
				if( mysql_num_rows($getGCMResultData) > 0 ) {
					
					$idGCM=mysql_fetch_array($getGCMResultData);
					array_push($registration_ids, $idGCM['gcmId']);
					
				}

				if( $idGCM['gcmId'] !== null ) {
					$gcm->send_notification($registration_ids, $gcmMessage);
				}

				$societyArray = array();
				$societyArray["localId"] = $decodedItemArray[$i]["obp_id"];
				$societyArray["action"] = $decodedItemArray[$i]["obp_offline_action"];
				array_push( $response["message"] , $societyArray);

			break;

			case "Delete":
			break;

		}
	}

	echo json_encode($response);
}

?>
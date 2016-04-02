<?php
include 'kissanCart_db_connect.php';
include 'GCM.php';
error_reporting(0);
// array for final json respone
$response = array();

if ( isset($_POST['jsonArraySocietyArr']) ) {

	$jsonArraySocietyArr = isset($_POST['jsonArraySocietyArr']) ? $_POST['jsonArraySocietyArr'] : '';

	$decodedItemArray = json_decode($jsonArraySocietyArr, true);
	
	$response['success'] = 1;
	$response['message'] = array();

	for ($i=0; $i < count($decodedItemArray); $i++) {

		switch ( $decodedItemArray[$i]["soc_offline_action"] ) {
			case "Insert":
				$checkMatchOBPNameQuery = mysql_query("SELECT * FROM society WHERE soc_name='".$decodedItemArray[$i]["soc_name"]."';");
				$checkMatchOBPEmailQuery = mysql_query("SELECT * FROM society WHERE soc_email='".$decodedItemArray[$i]["soc_email"]."';");

				if ( mysql_num_rows($checkMatchOBPNameQuery) > 0 && mysql_num_rows($checkMatchOBPEmailQuery) > 0) {

				}
				else {
					$result = mysql_query("INSERT INTO society(	
														uID,
														soc_name,
														soc_contact,
														soc_email,
														soc_adrs,
														status)VALUES(
														".$decodedItemArray[$i]["userId"].",
														'".$decodedItemArray[$i]["soc_name"]."',
														'".$decodedItemArray[$i]["soc_contact"]."',
														'".$decodedItemArray[$i]["soc_email"]."',
														'".$decodedItemArray[$i]["soc_adrs"]."',
														1);");

					if ($result) {
						$getId = mysql_insert_id();

						$societyArray = array();
						$societyArray["serverId"] = $getId;
						$societyArray["societyId"] = $decodedItemArray[$i]["id"];
						$societyArray["action"] = $decodedItemArray[$i]["soc_offline_action"];
						array_push( $response["message"] , $societyArray);

						$gcm = new GCM();

						$society = array();
						$registration_ids = array();

						$society["soc_id"] = $getId;
						$society["soc_name"] = $decodedItemArray[$i]["soc_name"];
						$society["soc_contact"] = $decodedItemArray[$i]["soc_contact"];
						$society["soc_email"] = $decodedItemArray[$i]["soc_email"];
						$society["soc_adrs"] = $decodedItemArray[$i]["soc_adrs"];

						$gcmMessage = array(
												'message' => 'downloadOfflineSocietyAtAdmin',
												'societyDetails' => $society
											);

						// get gcmId from user data  when user is logging.
						$sql = "SELECT gcmId FROM userdetails WHERE gID=1;";
						$getGCMResultData = mysql_query($sql);
						
						if( mysql_num_rows($getGCMResultData) > 0 ){
							
							$idGCM=mysql_fetch_array($getGCMResultData);
							array_push($registration_ids, $idGCM['gcmId']);
							
						}

						$gcm->send_notification($registration_ids, $gcmMessage);

					}

				}

				break;

		    case "Update":

		    	break;

		    case "Delete":
		        // code to be executed if n=label3;
		        break;

		}
	}

	echo json_encode($response);

}

?>
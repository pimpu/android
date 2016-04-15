<?php
	include 'eCarrier_db_connect.php';
	error_reporting(0);
	$response = array();
	
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		$image = $_POST['image'];

		$sql = mysql_query("INSERT INTO profilepic(image) VALUES('".$image."')");
		
		if( $sql ){
			
			$response["Message"] = "Image Uploaded Successfully";
			echo json_encode($response);
			
		}else{
			$response["Message"] = "Error Uploading Image";
			echo json_encode($response);
		}
		mysqli_close($selected);
	}else{
		$response["Message"] = "Error";
		echo json_encode($response);
	}
?>
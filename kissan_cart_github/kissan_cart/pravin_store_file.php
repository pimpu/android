<?php
include('config.php');
require_once('connection.php');
if(isset($_POST['ref']))
{
	if(isset($_FILES['doc'])){	
      $errors= array();
      $file_name = $_FILES['doc']['name'];
      $file_size =$_FILES['doc']['size'];
      $file_tmp =$_FILES['doc']['tmp_name'];
      $file_type=$_FILES['doc']['type'];
	  $array = explode('.',$_FILES['doc']['name']);
      $file_ext=strtolower(end($array));
      
     // $expensions= array("jpeg","jpg","png");
      
     // if(in_array($file_ext,$expensions)=== false){
     //    $errors[]="extension not allowed, please choose a JPEG or PNG file.";
     // }
	//$bbc =  $_POST['ref'].$_POST['uid'].$_POST['enqMsg'].$_POST['socName'].$_POST['socAdrss'].$_POST['socCont'].$_POST['socEmail'].$file_name;
	  
      // if($file_size > 2097152){
         // $errors[]='File size must be excately 2 MB';
      // }
      
      if(empty($errors)==true){
         move_uploaded_file($file_tmp,"../admin/Enquiry/".$file_name);
        // echo "Success";
		 $query = mysql_query("insert into enquiry(eRef,uID,gID,repTo,eMsg,eSociety,eSocAdrs,eSocCont,eSocEmail,eDoc,status) values('".$_POST['ref']."','".$_POST['uid']."','".$_POST['gid']."','".$_POST['repTo']."','".$_POST['enqMsg']."','".$_POST['socName']."','".$_POST['socAdrss']."','".$_POST['socCont']."','".$_POST['socEmail']."','".$file_name."','1')")or die(mysql_error());
		 if($query)
		 {
			
			 
			 if(isset($_POST['eid']))
			 {
				  $query1 = mysql_query("update enquiry Set replied = '1' where eID = '".$_POST['eid']."' ")or die(mysql_error());
				  if($query1)
				  {

				  }
			 }
			 if ($_SESSION["gID"] == 1)
			 {
				echo "<script>window.location.href = '../admin/new_enquiry.php';</script>";
			 }
			 else
			 {
				echo "<script>window.location.href = '../create_enquiry.php';</script>";
			 }

			 
			 
			
			 
		 }
		 
      }else{
         print_r($errors);
      }
   }
   else{
	   echo "<script>alert('Doc not found');</script>";
   }
}
else
{
	echo "<script>alert('not done');</script>";
}



 ?>
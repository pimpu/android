<?php
	
	$username = "root";
	$password = "";
	$hostname = "localhost";
	$db_name  = "e_carrier"	;
	
	error_reporting(0);
					
		
	//connection to the database
	$con = mysql_connect($hostname, $username, $password) or die("Unable to connect to MySQL");
	
	//select a database to work with
	$selected = mysql_select_db($db_name,$con) or die("Could not select e_carrier");
	
	return $con;
		
	
?>
<?php

error_reporting(0);

/**
 * Handling database connection
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class DbConnect {

    private $conn;

    function __construct() {        
    }

    /**
     * Establishing database connection
     * @return database connection handler
     */
    function connect() {
        include_once dirname(__FILE__) . '/Config.php';

        //connection to the database
        $con = mysql_connect(DB_HOST, DB_USERNAME, DB_PASSWORD) or die("Unable to connect to MySQL");
        
        //select a database to work with
        $selected = mysql_select_db(DB_NAME,$con) or die("Could not select e_carrier");
        
        return $con;
    }

}

?>

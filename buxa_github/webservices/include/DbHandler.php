<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class DbHandler {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    /* ------------- `bx_user` table method ------------------ */

    /**
     * Creating new user
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createUser($company, $uname, $mobile,
                                    $email, $password, $create_time) {
        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExists($email)) {

            // Generating API key
            $api_key = $this->generateApiKey();
            $result = mysql_query("INSERT INTO bx_user( api_key, 
                                                        company,
                                                        uname,
                                                        mobile,
                                                        email,
                                                        password,
                                                        create_time ) 
                                                        VALUES( 
                                                        '".$api_key."',
                                                        '".$company."',
                                                        '".$uname."',
                                                        ".$mobile.",
                                                        '".$email."',
                                                        '".$password."',
                                                        '".$create_time."');");


            // Check for successful insertion
            if ($result) {
                $response["message"] = USER_CREATED_SUCCESSFULLY;
                $response["id"] = mysql_insert_id();
                $response["api_key"] = $api_key;

            } else {
                // Failed to create user
                $response["message"] =  USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            $response["message"] =  USER_ALREADY_EXISTED;
        }

        return $response;
    }

    /**
     * Updating fcm registration id
     * @param String $regId Registration id
     * @param String $uId User login id
     */
    public function updateFcmID($regId, $uId) {
        $response = array();

        $result = mysql_query("UPDATE bx_user SET fcm_id = '".$regId."' WHERE uid = ".$uId);

        // Check for successful insertion
        if ($result) {
            $response["message"] = USER_CREATED_SUCCESSFULLY;

        } else {
            // Failed to create user
            $response["message"] =  USER_CREATE_FAILED;
        }

        return $response;
    }

    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLogin($email, $password) {
        $getPwd = mysql_query("SELECT password FROM bx_user WHERE email='".$email."' AND status=1;");
        $response = array();
        if (mysql_num_rows($getPwd) > 0) {
            $resultArray = mysql_fetch_array($getPwd);
            $sqlPassword = $resultArray["password"];

            if ( strcmp($password, $sqlPassword) == 0 ) {
                $response["result"]= TRUE;
            } else {
                $response["result"]= FALSE;
                $response["msg"] = "Password is incorrect.";
            }
        } else {
            $response["result"]= FALSE;
            $response["msg"] = "Please, Register with Buxa app.";
            // user not existed with the email
        }
        return $response;
    }

    /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */
    private function isUserExists($email) {
        $checkMatchEmailQuery = mysql_query("SELECT uid FROM bx_user WHERE email='".$email."';");
        return mysql_num_rows($checkMatchEmailQuery) > 0 ;
    }

    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByEmail($email) {
        $stmt = mysql_query("SELECT uname, email, api_key, uid, company FROM bx_user WHERE email='".$email."';");
        if ($stmt) {
            $stmtArray = mysql_fetch_array($stmt);
            $user = array();
            $user["name"] = $stmtArray["uname"];
            $user["email"] = $stmtArray["email"];
            $user["api_key"] = $stmtArray["api_key"];
            $user["id"] = $stmtArray["uid"];
            $user["companyName"] = $stmtArray["company"];
            return $user;
        } else {
            return NULL;
        }
    }

    /**
     * Validating user api key
     * If the api key is there in db, it is a valid key
     * @param String $api_key user api key
     * @return boolean
     */
    public function isValidApiKey($api_key) {
        $stmt = mysql_query("SELECT uid from bx_user WHERE api_key = '".$api_key."';");
        $data = mysql_num_rows($stmt);
        return $data > 0;
    }

    /**
     * Generating random Unique MD5 String for user Api key
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }

    /*---------------- 'bx_commodity' table method --------------*/

    /**
     * Fetching all commodity
     * 
     */
    public function getAllCommodity($start, $limit) {
        $stmt = mysql_query("SELECT * FROM bx_commodity WHERE status=1 LIMIT $start,$limit;");
        return $stmt;
    }

    /**
    * Count no of commodities row
    */
    public function getCommodityRowsCount() {
        $stmt = mysql_query("SELECT count(*) as total from bx_commodity;");
        $data = mysql_fetch_array($stmt);
        return $data["total"];
    }

    /*---------------- 'bx_icd' table method --------------*/

    /**
     * Fetching all custom loaction
     * 
     */
    public function getAllCustomLoaction() {
        $stmt = mysql_query("SELECT * FROM bx_icd WHERE status=1;");
        return $stmt;
    }

    /*---------------- 'bx_icdcategory' table method --------------*/

    /**
     * Fetching all custom clearance category
     * 
     */
    public function getAllCustomClearanceCategory() {
        $stmt = mysql_query("SELECT * FROM bx_icdcategory WHERE status=1;");
        return $stmt;
    }

    /*---------------- 'bx_ship_type' table method --------------*/

    /**
     * Fetching all type of shipment
     * 
     */
    public function getAllTypeOfShipment() {
        $stmt = mysql_query("SELECT * FROM bx_ship_type WHERE status=1;");
        return $stmt;
    }

    /*---------------- 'db_transport_type' table method --------------*/

    /**
     * Fetching all trasnport Type 
     * 
     */
    public function getAllTransportType() {
        $stmt = mysql_query("SELECT * FROM db_transport_type WHERE active_status=1;");
        return $stmt;
    }

    /*---------------- 'db_transport_service' table method --------------*/

    /**
     * Fetching all trasnport services
     * 
     */
    public function getAllTransportService() {
        $stmt = mysql_query("SELECT * FROM db_transport_service WHERE status=1;");
        return $stmt;
    }

    /*---------------- 'bx_packagetype' table method --------------*/

    /**
     * Fetching all packaging type data from table
     * 
     */
    public function getAllPackagingType() {
        $stmt = mysql_query("SELECT * FROM bx_packagetype WHERE status=1;");
        return $stmt;
    }

    /*---------------- 'bx_cfs_addresses' table method --------------*/

    /**
     * Fetching all container freight station from table
     * 
     */
    public function getCfsAddress() {
        $stmt = mysql_query("SELECT * FROM bx_cfs_addresses WHERE status=1;") or die(mysql_error());
        return $stmt;
    }

    /* ------------- `bx_transport` table method ------------------ */

    public function createTransportData($transportData) {
        $response = array();
        $decodedData = json_decode($transportData, true);

        if (!$this->isTransportBookingExists($decodedData["bookingId"])) {

            $result = mysql_query("INSERT INTO bx_transport(
                customer_code, 
                bookid,
                commodity_id,
                demlength,
                demwidth,
                demheight,
                shipment_type,
                mesurement,
                gross_weight,
                no_of_pack,
                pack_type,
                source,
                destination,
                avail_option,
                create_time ) 
                VALUES( 
                ".$decodedData["userId"].",
                '".$decodedData["bookingId"]."',
                ".$decodedData["commodityServerId"].",
                ".$decodedData["dimenLength"].",
                ".$decodedData["dimenWidth"].",
                ".$decodedData["dimenHeight"].",
                ".$decodedData["shipmentType"].",
                '".$decodedData["measurement"]."',
                ".$decodedData["grossWeight"].",
                ".$decodedData["noOfPack"].",
                ".$decodedData["packType"].",
                '".$decodedData["pickUp"]."',
                '".$decodedData["drop"]."',
                ".$decodedData["availOption"].",
                '".$decodedData["createdAt"]."');") or die(mysql_error());


            // Check for successful insertion
            if ($result) {

                mysql_query("INSERT INTO bx_shipment_confirmation(
                        bookid,
                        quotation,
                        finalrate,
                        serviceid )
                        VALUES(
                        '".$decodedData["bookingId"]."',
                        '',
                        '',
                        '1' )
                        ON DUPLICATE KEY UPDATE
                        serviceid = concat(serviceid ,',','1');") or die(mysql_error());

                $response["message"] = USER_CREATED_SUCCESSFULLY;
                $response["id"] = mysql_insert_id();

            } else {
                // Failed to create user
                $response["message"] =  USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            $response["message"] =  USER_ALREADY_EXISTED;
        } 

        return $response; 
    }

    /**
     * Checking for duplicate transport enquiry by booking id
     * @param String $bookingId to check in db
     * @return boolean
     */
    private function isTransportBookingExists($bookingId) {
        $checkMatchTransportQuery = mysql_query("SELECT trnsid FROM bx_transport WHERE bookid='".$bookingId."';");
        return mysql_num_rows($checkMatchTransportQuery) > 0 ;
    }


    /* ------------- `bx_custom_clearance` table method ------------------ */

    public function createCustomClearanceData($customClearancedata) {
        $response = array();
        $decodedData = json_decode($customClearancedata, true);

        if (!$this->isCustomClearanceBookingExists($decodedData["bookingId"])) {

            $result = mysql_query("INSERT INTO bx_custom_clearance(
                bookid,
                customer_code,
                cc_type,
                commodity_id,
                gross_weight,
                hscode,
                type_of_shipment,
                stuffing,
                address,
                avail_option,
                create_time ) 
                VALUES( 
                '".$decodedData["bookingId"]."',
                ".$decodedData["userId"].",
                '".$decodedData["CCType"]."',
                ".$decodedData["commodityServerId"].",
                ".$decodedData["grossWeight"].",
                ".$decodedData["HSCode"].",
                ".$decodedData["iShipmentType"].",
                '".$decodedData["stuffingType"]."',
                '".$decodedData["stuffingAddress"]."',
                ".$decodedData["availOption"].",
                '".$decodedData["createdAt"]."');") or die(mysql_error());

            // Check for successful insertion
            if ($result) {

                mysql_query("INSERT INTO bx_shipment_confirmation(
                        bookid,
                        quotation,
                        finalrate,
                        serviceid )
                        VALUES(
                        '".$decodedData["bookingId"]."',
                        '',
                        '',
                        '2' )
                        ON DUPLICATE KEY UPDATE
                        serviceid = concat(serviceid,',','2');") or die(mysql_error());

                $response["message"] = USER_CREATED_SUCCESSFULLY;
                $response["id"] = mysql_insert_id();

            } else {
                // Failed to create user
                $response["message"] =  USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            $response["message"] =  USER_ALREADY_EXISTED;
        } 

        return $response; 
    }

    /**
     * Checking for duplicate custom clearance enquiry by booking id
     * @param String $bookingId to check in db
     * @return boolean
     */
    private function isCustomClearanceBookingExists($bookingId) {
        $checkMatchTransportQuery = mysql_query("SELECT clearanceid FROM bx_custom_clearance WHERE bookid='".$bookingId."';");
        return mysql_num_rows($checkMatchTransportQuery) > 0 ;
    }


    /* ------------- `bx_freight_forwarding` table method ------------------ */

    public function createFreightForwardingData($freightForwardingdata) {
        $response = array();
        $decodedData = json_decode($freightForwardingdata, true);

        if (!$this->isFreightForwardingBookingExists($decodedData["bookingId"])) {

            $result = mysql_query("INSERT INTO bx_freight_forwarding(
                bookid,
                customer_code,
                type_of_shipment,
                pol,
                pod,
                poc,
                incoterm,
                destideliveryadr,
                measurement,
                grossweight,
                packtype,
                noofpack,
                commodity,
                avail_option,
                create_time ) 
                VALUES( 
                '".$decodedData["bookingId"]."',
                ".$decodedData["userId"].",
                ".$decodedData["shipmentType"].",
                '".$decodedData["portOfLoading"]."',
                '".$decodedData["portOfDestination"]."',
                '".$decodedData["portOfCountry"]."',
                '".$decodedData["strIncoterm"]."',
                '".$decodedData["strDestinatioDeliveryAdr"]."',
                '".$decodedData["measurement"]."',
                ".$decodedData["grossWeight"].",
                ".$decodedData["packType"].",
                ".$decodedData["noOfPack"].",
                ".$decodedData["commodityServerId"].",
                ".$decodedData["availOption"].",
                '".$decodedData["createdAt"]."');") or die(mysql_error());

            // Check for successful insertion
            if ($result) {

                mysql_query("INSERT INTO bx_shipment_confirmation(
                        bookid,
                        quotation,
                        finalrate,
                        serviceid )
                        VALUES(
                        '".$decodedData["bookingId"]."',
                        '',
                        '',
                        '3' )
                        ON DUPLICATE KEY UPDATE
                        serviceid = concat(serviceid,',','3');") or die(mysql_error());

                $response["message"] = USER_CREATED_SUCCESSFULLY;
                $response["id"] = mysql_insert_id();

            } else {
                // Failed to create user
                $response["message"] =  USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            $response["message"] =  USER_ALREADY_EXISTED;
        } 

        return $response; 
    }

    /**
     * Checking for duplicate freight forwarding enquiry by booking id
     * @param String $bookingId to check in db
     * @return boolean
     */
    private function isFreightForwardingBookingExists($bookingId) {
        $checkMatchTransportQuery = mysql_query("SELECT freightid FROM bx_freight_forwarding WHERE bookid='".$bookingId."';");
        return mysql_num_rows($checkMatchTransportQuery) > 0 ;
    }

    /**
     * Updating enquiry status
     * @param String $bookingId Booking id
     * @param String $status status (accept-4, cancel-5)
     */
    public function updateShipmentConformation($bookingId, $status) {
        $response = array();

        $result = mysql_query("UPDATE bx_shipment_confirmation SET enquiry_status = ".$status." WHERE bookid = '".$bookingId."';");

        // Check for successful insertion
        if ($result) {
            $response["message"] = USER_CREATED_SUCCESSFULLY;

        } else {
            // Failed to create user
            $response["message"] =  USER_CREATE_FAILED;
        }

        return $response;
    }


    /**
     * Deleting a task
     * @param String $task_id id of the task to delete
     */
    public function deleteTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("DELETE t FROM tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }
}

?>
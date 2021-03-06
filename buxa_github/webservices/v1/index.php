<?php

require_once '../include/DbHandler.php';
require_once '../include/firebase.php';
require_once '../include/push.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();


/**
 * Adding Middle Layer to authenticate every request
 * Checking if the request has valid api key in the 'Authorization' header
 */
function authenticate(\Slim\Route $route) {
    // Getting request headers
    // $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();
 
    // get the api key
    $api_key = $app->request->headers("X-Authorization");


    // Verifying Authorization Header
    if ($api_key != null ) {
        $db = new DbHandler();
 
        // validating api key
        if (!$db->isValidApiKey($api_key)) {
            // api key is not present in users table
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoRespnse(401, $response);
            $app->stop();
        }/* else {
            global $user_id;
            // get user primary key id
            $user = $db->getUserId($api_key);
            if ($user != NULL)
                $user_id = $user["id"];
        }*/
    } else {
        // api key is missing in header
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * ----------- METHODS WITHOUT AUTHENTICATION ---------------------------------
 */
 
 $app -> post('/verifyemail', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('otp', 'email', 'name'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $otp = $app->request->post('otp');
    $email = $app->request->post('email');
    $name = $app->request->post('name');

    $isExist = $db->isUserExists($email);
    if(!$isExist) {

        $to      = $email;
        $subject = 'One-time password for BUXA registering';
        $message = "Hello, ".$name."\r\n \r\n";
        $message .= "This is your one-time password \"".$otp."\".\r\n\r\n";
        $message .= "Thank you,\r\n";
        $message .= "Buxa Logistic\r\n";
        $headers .= 'From: info@buxa.tech' . "\r\n" .
            'Reply-To: info@buxa.tech' . "\r\n" .
            'X-Mailer: PHP/' . phpversion();

        mail($to, $subject, $message, $headers);
        $response["error"] = false;
        $response["message"] = "sent mail successfully";
    }
    else {
        $response["error"] = true;
        $response["message"] = "This email id already existed.Please, login with this email.";
    }

    echoRespnse(200, $response);

});

/**
 * User Registration
 * url - /register
 * method - POST
 * params - name, email, password
 */
$app->post('/register', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('uname', 'email', 'password'));

            $response = array();

            // reading post params
            $company = $app->request->post('company');
            $uname = $app->request->post('uname');
            $mobile = $app->request->post('mobile');
            $email = $app->request->post('email');
            $password = $app->request->post('password');
            $create_time = $app->request->post('create_time');

            $db = new DbHandler();
            $res = $db->createUser($company, $uname, $mobile,
                                    $email, $password, $create_time );

            if ($res["message"] == USER_CREATED_SUCCESSFULLY) {
                $response["error"] = false;
                $response["message"] = "You are successfully registered";
                $response["id"] = $res["id"];
                $response["api_key"] = $res["api_key"];
                $response["email"] = $email;
                $response["loginName"] = $uname;
                $response["companyName"] = $company;
            } else if ($res["message"] == USER_CREATE_FAILED) {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
            } else if ($res["message"] == USER_ALREADY_EXISTED) {
                $response["error"] = true;
                $response["message"] = "This email id already existed.Please, login with this email.";
            }
            // echo json response
            echoRespnse(201, $response);
        });

/**
 * User Login
 * url - /login
 * method - POST
 * params - email, password
 */
$app->post('/login', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email', 'password'));

            // reading post params
            $email = $app->request()->post('email');
            $password = $app->request()->post('password');
            $fcmId = $app->request()->post('fcmId');

            $response = array();

            $db = new DbHandler();
            // check for correct email and password
            $res = $db->checkLogin($email, $password);

            if ($res["result"]) {
                // get the user by email
                $user = $db->getUserByEmail($email);

                if ($user != NULL) {
                    $response["error"] = false;
                    $response['loginName'] = $user['name'];
                    $response['email'] = $user['email'];
                    $response['api_key'] = $user['api_key'];
                    $response['id'] = $user['id'];
                    $response['companyName'] = $user['companyName'];

                    $db->updateFcmID($fcmId, $user['id']);
                    
                } else {
                    // unknown error occurred
                    $response['error'] = true;
                    $response['message'] = "An error occurred. Please try again";
                }
            } else {
                // user credentials are wrong
                $response['error'] = true;
                $response['message'] = $res["msg"];
            }

            echoRespnse(200, $response);
        });

$app -> get('/commodities',function() use ($app) {
    $response = array();
    $db = new DbHandler();

    // check for required params
    verifyRequiredParams(array('start', 'limit'));

    // reading get params
    $start = $app->request()->get('start');
    $limit = $app->request()->get('limit');

    // fetching all commodities
    $allComodityResult = $db -> getAllCommodity($start, $limit);

    $response["error"] = false;
    $response["commodities"] = array();

    // looping through result and preparing commodities array
    while ($commodity = mysql_fetch_array($allComodityResult) ) {
        $tmp = array();
        $tmp["id"] = $commodity["comid"];
        $tmp["comodity"] = $commodity["comodity"];
        $tmp["status"] = $commodity["status"];
        array_push($response["commodities"], $tmp);
    }

    echoRespnse(200, $response);

});

$app -> get('/customlocation', function() {
    $response = array();
    $db =  new DbHandler();

    $allCustomLoaction = $db ->  getAllCustomLoaction();

    // fetching all custom location
    $response["error"] = false;
    $response["customLocation"] = array();

    while ($CL = mysql_fetch_array($allCustomLoaction)) {
        $temp = array();
        $temp["id"] = $CL["id"];
        $temp["CLCid"] = $CL["icdid"];
        $temp["name"] = $CL["icdname"];
        $temp["location"] = $CL["location"];
        $temp["state"] = $CL["state"];
        $temp["status"] = $CL["status"];

        array_push($response["customLocation"], $temp);
    }
    echoRespnse(200, $response);
});

$app -> get('/customclearancecategory', function(){
    $response = array();
    $db = new DbHandler();

    $allCustomClearanceCategory = $db -> getAllCustomClearanceCategory();

    // fetching all custom clearance category
    $response["error"] = false;
    $response["customClearanceCategory"] = array();

    while ($CCC = mysql_fetch_array($allCustomClearanceCategory)) {
        $temp = array();
        $temp["id"] = $CCC["icdid"];
        $temp["name"] = $CCC["icdname"];
        $temp["status"] = $CCC["status"];

        array_push($response["customClearanceCategory"], $temp);
    }
    echoRespnse(200, $response);

});


$app -> get('/typeofshipment', function(){
    $response = array();
    $db = new DbHandler();

    $allShipmentType = $db -> getAllTypeOfShipment();
    $noOfCommodityRows = $db -> getCommodityRowsCount();

    // fetching all term of shipment
    $response["error"] = false;
    $response["commodityRowCount"] = $noOfCommodityRows;
    $response["shipmentType"] = array();

    while ($type = mysql_fetch_array($allShipmentType)) {
        $temp = array();
        $temp["id"] = $type["tid"];
        $temp["name"] = $type["stypename"];
        $temp["status"] = $type["status"];

        array_push($response["shipmentType"], $temp);
    }
    echoRespnse(200, $response);

});

$app -> get('/transporttype', function(){
    $response = array();
    $db = new DbHandler();

    $allTransportType = $db -> getAllTransportType();

    // fetching all transportation type
    $response["error"] = false;
    $response["trasnportType"] = array();

    while ($tt = mysql_fetch_array($allTransportType)) {
        $temp = array();
        $temp["id"] = $tt["trid"];
        $temp["name"] = $tt["tname"];
        $temp["status"] = $tt["active_status"];

        array_push($response["trasnportType"], $temp);
    }
    echoRespnse(200, $response);

});

$app -> get('/transportservice', function(){
    $response = array();
    $db = new DbHandler();

    $allTransportService = $db -> getAllTransportService();

    // fetching all transportation service
    $response["error"] = false;
    $response["trasnportService"] = array();

    while ($ts = mysql_fetch_array($allTransportService)) {
        $temp = array();
        $temp["id"] = $ts["service_id"];
        $temp["name"] = $ts["servicename"];
        $temp["status"] = $ts["status"];

        array_push($response["trasnportService"], $temp);
    }
    echoRespnse(200, $response);

});

$app -> get('/packagingtype', function(){
    $response = array();
    $db = new DbHandler();

    $allPackageType = $db -> getAllPackagingType();

    // fetching all transportation service
    $response["error"] = false;
    $response["packageType"] = array();

    while ($ts = mysql_fetch_array($allPackageType)) {
        $temp = array();
        $temp["id"] = $ts["packagetypeid"];
        $temp["name"] = $ts["packagename"];
        $temp["status"] = $ts["status"];

        array_push($response["packageType"], $temp);
    }
    echoRespnse(200, $response);

});

$app -> get('/getcfsadresses', function(){
    $response = array();
    $db = new DbHandler();

    $allCfsAddress = $db -> getCfsAddress();

    // fetching all transportation service
    $response["error"] = false;
    $response["cfsAddress"] = array();

    while ($ts = mysql_fetch_array($allCfsAddress)) {
        $temp = array();
        $temp["id"] = $ts["cfs_id"];
        $temp["name"] = $ts["cfs_address"];
        $temp["status"] = $ts["status"];

        array_push($response["cfsAddress"], $temp);
    }
    echoRespnse(200, $response);

});

$app->put('/updatefcmid', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('registerId', 'userId'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $registerId = $app->request->put('registerId');
    $userId = $app->request->put('userId');

    $res = $db->updateFcmID($registerId, $userId);

    if ($res["message"] == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "successfully update FCM id";
    } else if ($res["message"] == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while updating";
    }

    echoRespnse(200, $response);

});

/*
 * ------------------------ METHODS WITH AUTHENTICATION ------------------------
 */

$app -> post('/inserttransport', 'authenticate', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('transportdata', 'shiparea'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $transportdata = $app->request->post('transportdata');
    $shiparea = $app->request->post('shiparea');

    $res = $db->createTransportData($transportdata, $shiparea);

    if ($res["message"] == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "Transportation enquiry created successfully";
        $response["id"] = $res["id"];
    } else if ($res["message"] == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while creating";
    } else if ($res["message"] == USER_ALREADY_EXISTED) {
        $response["error"] = true;
        $response["message"] = "This booking already existed.";
    }

    echoRespnse(200, $response);

});

$app -> post('/insertcustomclearance', 'authenticate', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('customClearancedata', 'shiparea'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $customClearancedata = $app->request->post('customClearancedata');
    $shiparea = $app->request->post('shiparea');

    $res = $db->createCustomClearanceData($customClearancedata, $shiparea);

    if ($res["message"] == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "Custom clearance enquiry created successfully";
        $response["id"] = $res["id"];
    } else if ($res["message"] == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while creating";
    } else if ($res["message"] == USER_ALREADY_EXISTED) {
        $response["error"] = true;
        $response["message"] = "This booking already existed.";
    }

    echoRespnse(200, $response);

});

$app -> post('/insertfreightforwarding', 'authenticate', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('freightForwardingdata', 'shiparea'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $freightForwardingdata = $app->request->post('freightForwardingdata');
    $shiparea = $app->request->post('shiparea');

    $res = $db->createFreightForwardingData($freightForwardingdata, $shiparea);

    if ($res["message"] == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "Freight forwarding enquiry created successfully";
        $response["id"] = $res["id"];
    } else if ($res["message"] == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while creating";
    } else if ($res["message"] == USER_ALREADY_EXISTED) {
        $response["error"] = true;
        $response["message"] = "This booking already existed.";
    }

    echoRespnse(200, $response);

});

$app -> post('/sendmail', 'authenticate', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('bookingId'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $bookingId = $app->request->post('bookingId');

    $to      = 'amruta@buxa.tech';
    // $to      = 'yogesh.blueoort@gmail.com';
    $subject = 'New Booking - '.$bookingId;
    $message = "Hello,\r\n \r\n";
    $message .= "You get the new enquiry of buxa no ".$bookingId.". Please check in admin.\r\n\r\n";
    $message .= "Thank you,\r\n";
    $message .= "Buxa Logistic\r\n";
    $headers .= "From: info@buxa.tech" . "\r\n" .
        "Reply-To: info@buxa.tech" . "\r\n" .
        "X-Mailer: PHP/" . phpversion();

    mail($to, $subject, $message, $headers);

    $response["error"] = false;
    $response["message"] = "sent mail successfully";
    echoRespnse(200, $response);

});

$app -> post('/acceptenquiry', 'authenticate', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('bookingId', 'status'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $bookingId = $app->request->post('bookingId');
    $status = $app->request->post('status');

    $res = $db->updateShipmentConformation($bookingId, $status);

    if ($res["message"] == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "shipment accept conformation update successfully";
    } else if ($res["message"] == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while upating shipment accept conformation";
    }

    echoRespnse(200, $response);

});

$app -> post('/cancelenquiry', 'authenticate', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('bookingId', 'status'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $bookingId = $app->request->post('bookingId');
    $status = $app->request->post('status');

    $res = $db->updateShipmentConformation($bookingId, $status);

    if ($res["message"] == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "shipment cancel conformation update successfully";
    } else if ($res["message"] == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while upating shipment cancel conformation";
    }

    echoRespnse(200, $response);

});

$app -> post('/forgotpassword', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('emailId'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $emailId = $app->request->post('emailId');

    $res = $db->forgotPwd($emailId);

    $response["error"] = false;
    $response["message"] = $res["message"];

    echoRespnse(200, $response);

});

$app -> post('/submitfeedback', 'authenticate', function() use ($app) {

    // check for required params
    verifyRequiredParams(array('loginId', 'feedback'));

    $db = new DbHandler();
    $response = array();

    // reading post params
    $loginId = $app->request->post('loginId');
    $feedback = $app->request->post('feedback');

    $res = $db->submitFeedback($loginId, $feedback);

    $response["error"] = false;
    $response["message"] = $res["message"];

    echoRespnse(200, $response);

});

/**
 * Deleting task. Users can delete only their tasks
 * method DELETE
 * url /tasks
 */
$app->delete('/tasks/:id', 'authenticate', function($task_id) use($app) {
            global $user_id;

            $db = new DbHandler();
            $response = array();
            $result = $db->deleteTask($user_id, $task_id);
            if ($result) {
                // task deleted successfully
                $response["error"] = false;
                $response["message"] = "Task deleted succesfully";
            } else {
                // task failed to delete
                $response["error"] = true;
                $response["message"] = "Task failed to delete. Please try again!";
            }
            echoRespnse(200, $response);
        });

/**
 * Verifying required params posted or not
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * Echoing json response to client
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response);
}

$app->run();
?>
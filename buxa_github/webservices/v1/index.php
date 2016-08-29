<?php

require_once '../include/DbHandler.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

// User id from db - Global Variable
$user_id = NULL;

/**
 * Adding Middle Layer to authenticate every request
 * Checking if the request has valid api key in the 'Authorization' header
 */
function authenticate(\Slim\Route $route) {
    // Getting request headers
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    // Verifying Authorization Header
    if (isset($headers['Authorization'])) {
        $db = new DbHandler();

        // get the api key
        $api_key = $headers['Authorization'];
        // validating api key
        if (!$db->isValidApiKey($api_key)) {
            // api key is not present in users table
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoRespnse(401, $response);
            $app->stop();
        } else {
            global $user_id;
            // get user primary key id
            $user_id = $db->getUserId($api_key);
        }
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
            $code = $app->request->post('code');
            $company = $app->request->post('company');
            $uname = $app->request->post('uname');
            $mobile = $app->request->post('mobile');
            $email = $app->request->post('email');
            $password = $app->request->post('password');
            $create_time = $app->request->post('create_time');

            $db = new DbHandler();
            $res = $db->createUser($code, $company, $uname, $mobile,
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


$app -> get('/commodities',function() {
    $response = array();
    $db = new DbHandler();

    // fetching all commodities
    $allComodityResult = $db -> getAllCommodity();

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


$app -> get('/termsofshipment', function(){
    $response = array();
    $db = new DbHandler();

    $allShipmentTerm = $db -> getAllTermOfShipment();

    // fetching all term of shipment
    $response["error"] = false;
    $response["shipmentTerm"] = array();

    while ($term = mysql_fetch_array($allShipmentTerm)) {
        $temp = array();
        $temp["id"] = $term["tid"];
        $temp["name"] = $term["stypename"];
        $temp["status"] = $term["status"];

        array_push($response["shipmentTerm"], $temp);
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



/*
 * ------------------------ METHODS WITH AUTHENTICATION ------------------------
 */

/**
 * Listing all tasks of particual user
 * method GET
 * url /tasks          
 */
$app->get('/tasks', 'authenticate', function() {
            global $user_id;
            $response = array();
            $db = new DbHandler();

            // fetching all user tasks
            $result = $db->getAllUserTasks($user_id);

            $response["error"] = false;
            $response["tasks"] = array();

            // looping through result and preparing tasks array
            while ($task = $result->fetch_assoc()) {
                $tmp = array();
                $tmp["id"] = $task["id"];
                $tmp["task"] = $task["task"];
                $tmp["status"] = $task["status"];
                $tmp["createdAt"] = $task["created_at"];
                array_push($response["tasks"], $tmp);
            }

            echoRespnse(200, $response);
        });

/**
 * Listing single task of particual user
 * method GET
 * url /tasks/:id
 * Will return 404 if the task doesn't belongs to user
 */
$app->get('/tasks/:id', 'authenticate', function($task_id) {
            global $user_id;
            $response = array();
            $db = new DbHandler();

            // fetch task
            $result = $db->getTask($task_id, $user_id);

            if ($result != NULL) {
                $response["error"] = false;
                $response["id"] = $result["id"];
                $response["task"] = $result["task"];
                $response["status"] = $result["status"];
                $response["createdAt"] = $result["created_at"];
                echoRespnse(200, $response);
            } else {
                $response["error"] = true;
                $response["message"] = "The requested resource doesn't exists";
                echoRespnse(404, $response);
            }
        });

/**
 * Creating new task in db
 * method POST
 * params - name
 * url - /tasks/
 */
$app->post('/tasks', 'authenticate', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('task'));

            $response = array();
            $task = $app->request->post('task');

            global $user_id;
            $db = new DbHandler();

            // creating new task
            $task_id = $db->createTask($user_id, $task);

            if ($task_id != NULL) {
                $response["error"] = false;
                $response["message"] = "Task created successfully";
                $response["task_id"] = $task_id;
                echoRespnse(201, $response);
            } else {
                $response["error"] = true;
                $response["message"] = "Failed to create task. Please try again";
                echoRespnse(200, $response);
            }            
        });

/**
 * Updating existing task
 * method PUT
 * params task, status
 * url - /tasks/:id
 */
$app->put('/tasks/:id', 'authenticate', function($task_id) use($app) {
            // check for required params
            verifyRequiredParams(array('task', 'status'));

            global $user_id;            
            $task = $app->request->put('task');
            $status = $app->request->put('status');

            $db = new DbHandler();
            $response = array();

            // updating task
            $result = $db->updateTask($user_id, $task_id, $task, $status);
            if ($result) {
                // task updated successfully
                $response["error"] = false;
                $response["message"] = "Task updated successfully";
            } else {
                // task failed to update
                $response["error"] = true;
                $response["message"] = "Task failed to update. Please try again!";
            }
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
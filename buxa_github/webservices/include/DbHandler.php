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

    /* ------------- `users` table method ------------------ */

    /**
     * Creating new user
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createUser($code, $company, $uname, $mobile,
                                    $email, $password, $create_time) {
        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExists($email)) {

            // Generating API key
            $api_key = $this->generateApiKey();
            $result = mysql_query("INSERT INTO bx_user( api_key, 
                                                        comp_ref_no, 
                                                        company,
                                                        uname,
                                                        mobile,
                                                        email,
                                                        password,
                                                        create_time ) 
                                                        VALUES( 
                                                        '".$api_key."',
                                                        '".$code."',
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
     * Fetching user api key
     * @param String $user_id user id primary key in user table
     */
    public function getApiKeyById($user_id) {
        $stmt = $this->conn->prepare("SELECT api_key FROM users WHERE id = ?");
        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            // $api_key = $stmt->get_result()->fetch_assoc();
            // TODO
            $stmt->bind_result($api_key);
            $stmt->close();
            return $api_key;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user id by api key
     * @param String $api_key user api key
     */
    public function getUserId($api_key) {
        $stmt = $this->conn->prepare("SELECT id FROM users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        if ($stmt->execute()) {
            $stmt->bind_result($user_id);
            $stmt->fetch();
            // TODO
            // $user_id = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user_id;
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
        $stmt = $this->conn->prepare("SELECT id from users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
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
    public function getAllCommodity() {
        $stmt = mysql_query("SELECT * FROM bx_commodity WHERE status=1;");
        return $stmt;
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


    /* ------------- `tasks` table method ------------------ */

    /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function createTask($user_id, $task) {
        $stmt = $this->conn->prepare("INSERT INTO tasks(task) VALUES(?)");
        $stmt->bind_param("s", $task);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            // task row created
            // now assign the task to user
            $new_task_id = $this->conn->insert_id;
            $res = $this->createUserTask($user_id, $new_task_id);
            if ($res) {
                // task created successfully
                return $new_task_id;
            } else {
                // task failed to create
                return NULL;
            }
        } else {
            // task failed to create
            return NULL;
        }
    }

    /**
     * Fetching single task
     * @param String $task_id id of the task
     */
    public function getTask($task_id, $user_id) {
        $stmt = $this->conn->prepare("SELECT t.id, t.task, t.status, t.created_at from tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        if ($stmt->execute()) {
            $res = array();
            $stmt->bind_result($id, $task, $status, $created_at);
            // TODO
            // $task = $stmt->get_result()->fetch_assoc();
            $stmt->fetch();
            $res["id"] = $id;
            $res["task"] = $task;
            $res["status"] = $status;
            $res["created_at"] = $created_at;
            $stmt->close();
            return $res;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllUserTasks($user_id) {
        $stmt = $this->conn->prepare("SELECT t.* FROM tasks t, user_tasks ut WHERE t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $tasks = $stmt->get_result();
        $stmt->close();
        return $tasks;
    }

    /**
     * Updating task
     * @param String $task_id id of the task
     * @param String $task task text
     * @param String $status task status
     */
    public function updateTask($user_id, $task_id, $task, $status) {
        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?, t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
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

    /* ------------- `user_tasks` table method ------------------ */

    /**
     * Function to assign a task to user
     * @param String $user_id id of the user
     * @param String $task_id id of the task
     */
    public function createUserTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("INSERT INTO user_tasks(user_id, task_id) values(?, ?)");
        $stmt->bind_param("ii", $user_id, $task_id);
        $result = $stmt->execute();

        if (false === $result) {
            die('execute() failed: ' . htmlspecialchars($stmt->error));
        }
        $stmt->close();
        return $result;
    }

}

?>

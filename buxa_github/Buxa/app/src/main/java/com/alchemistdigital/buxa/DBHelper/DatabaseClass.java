package com.alchemistdigital.buxa.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 8/26/2016.
 */
public class DatabaseClass extends SQLiteOpenHelper {
    private Context context;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Buxa.db";

    // Table Names
    public static final String TABLE_COMPANY = "CompanyDetailsModel";
    public static final String TABLE_COMMODITY = "Commodity";
    public static final String TABLE_CUSTOM_CLEARANCE_LOCATION = "CustomClearanceLocation";
    public static final String TABLE_CUSTOM_CLEARANCE_CATEGORY = "CustomClearanceCategory";
    public static final String TABLE_TERM_OF_SHIPMENT = "TermOfShipment";
    public static final String TABLE_TRANSPORTATION = "Transportation";
    public static final String TABLE_SHIPMENT_CONFORMATION = "ShipmentConformation";
    public static final String TABLE_TRANSPORT_TYPE = "TransportType";
    public static final String TABLE_TRANSPORT_SERVICE = "TransportService";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_STATUS = "status";

    // TABLE_COMPANY Table - column names
    private static final String COMPANY_SERVER_ID = "company_serverId";
    private static final String COMPANY_REFERENCE_NO = "company_reference_no";
    private static final String COMPANY_USER_DESIGNATION = "company_user_designation";
    private static final String COMPANY_NAME = "company_name";
    private static final String COMPANY_CONTACT = "company_contact";
    private static final String COMPANY_ADDRESS = "company_address";
    private static final String COMPANY_CITY = "company_city";
    private static final String COMPANY_LANDMARK = "company_landmark";
    private static final String COMPANY_STATE = "company_state";
    private static final String COMPANY_PAN = "company_pan";
    private static final String COMPANY_TIN = "company_tin";


    // TABLE_COMMODITY Table - column names
    private static final String COMMODITY_SERVER_ID = "commodity_serverId";
    private static final String COMMODITY_NAME = "commodity_name";

    // CUSTOM_CLEARANCE_LOCATION Table - column names
    private static final String CCL_SERVER_ID = "ccl_serverId";
    private static final String CC_CATEGORY_ID = "cc_category_id";
    private static final String CCL_NAME = "ccl_name";
    private static final String CCL_LOCATION = "ccl_location";
    private static final String CCL_STATE = "ccl_state";

    // TABLE_CUSTOM_CLEARANCE_CATEGORY Table - column names
    private static final String CCC_SERVER_ID = "ccc_serverId";
    private static final String CCC_NAME = "ccc_name";

    // TABLE_TERM_OF_SHIPMENT Table - column names
    private static final String TOS_SERVER_ID = "tos_serverId";
    private static final String TOS_name = "tos_name";


    // TABLE_SHIPMENT_CONFORMATION Table - column names
    private static final String SHIPMENT_CONFORM_SERVER_ID = "shipment_conform_serverId";

    // TABLE_TRANSPORTATION Table - column names
    private static final String  TRANSPORTATION_SERVER_ID = "trans_serverId";
    private static final String  COMMODITY_SERVERID = "commodity_serverId";
    private static final String  DIMEN_LENGTH = "dimen_length";
    private static final String  DIMEN_HEIGHT = "dimen_height";
    private static final String  DIMEN_WEIGHT = "dimen_weight";
    private static final String  SHIPMENT_TERM = "shipment_term";
    private static final String  NO_OF_PACK = "no_of_pack";
    private static final String  PACK_TYPE = "pack_type";
    private static final String  PICKUP = "pickup";
    private static final String  DROP = "drop";
    private static final String  LRCOPY = "lr_copy";
    private static final String  AVAIL_OPTION = "avail_option";

    // TABLE_TRANSPORT_TYPE Table - column names
    private static final String TRANSPORT_TYPE_SERVER_ID = "trans_type_serverId";
    private static final String TRANSPORT_TYPE_NAME = "trans_type_name";

    // TABLE_TRANSPORT_SERVICE Table - column names
    private static final String TRANSPORT_SERVICE_SERVER_ID = "trans_service_serverId";
    private static final String TRANSPORT_SERVICE_NAME = "trans_service_name";

    // Company Table Create Statements
    private static final String CREATE_TABLE_COMPANY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_COMPANY +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COMPANY_SERVER_ID+" INTEGER," +
                    COMPANY_REFERENCE_NO+" INTEGER," +
                    COMPANY_USER_DESIGNATION+" VARCHAR(100)," +
                    COMPANY_NAME+" VARCHAR(100), " +
                    COMPANY_CONTACT+" INTEGER," +
                    COMPANY_ADDRESS+" VARCHAR(200)," +
                    COMPANY_CITY+" VARCHAR(200)," +
                    COMPANY_LANDMARK+" VARCHAR(200)," +
                    COMPANY_STATE+" VARCHAR(200)," +
                    COMPANY_PAN+" VARCHAR(200)," +
                    COMPANY_TIN+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Commodity Table Create Statements
    private static final String CREATE_TABLE_COMMODITY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_COMMODITY +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COMMODITY_SERVER_ID+" INTEGER," +
                    COMMODITY_NAME+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";


    // Custom clearance location Table Create Statements
    private static final String CREATE_TABLE_CUSTOM_CLEARANCE_LOCATION =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_CUSTOM_CLEARANCE_LOCATION +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CCL_SERVER_ID+" INTEGER," +
                    CC_CATEGORY_ID +" INTEGER," +
                    CCL_NAME+" VARCHAR(200)," +
                    CCL_LOCATION+" VARCHAR(200)," +
                    CCL_STATE+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Custom clearance category Table Create Statements
    private static final String CREATE_TABLE_CUSTOM_CLEARANCE_CATEGORY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_CUSTOM_CLEARANCE_CATEGORY+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CCC_SERVER_ID+" INTEGER," +
                    CCC_NAME+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Term of shipment Table Create Statements
    private static final String CREATE_TABLE_TERM_OF_SHIPMENT =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TERM_OF_SHIPMENT+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TOS_SERVER_ID +" INTEGER," +
                    TOS_name+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Transportation Table Create Statements
    private static final String CREATE_TABLE_TRANSPORTATION =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSPORTATION+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TRANSPORTATION_SERVER_ID +" INTEGER," +
                    COMMODITY_SERVERID +" INTEGER," +
                    DIMEN_LENGTH +" INTEGER," +
                    DIMEN_HEIGHT +" INTEGER," +
                    DIMEN_WEIGHT +" INTEGER," +
                    SHIPMENT_TERM +" INTEGER," +
                    NO_OF_PACK +" INTEGER," +
                    PACK_TYPE +" INTEGER," +
                    PICKUP +" VARCHAR(200)," +
                    DROP +" VARCHAR(200)," +
                    LRCOPY +" VARCHAR(200)," +
                    AVAIL_OPTION +" TINYINT(4)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    /*// Shipment conformation Table Create Statements
    private static final String CREATE_TABLE_SHIPMENT_CONFORMATION =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_SHIPMENT_CONFORMATION+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TOS_SERVER_ID +" INTEGER," +
                    TOS_name+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";*/

    // Transport type Table Create Statements
    private static final String CREATE_TABLE_TRANSPORT_TYPE =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSPORT_TYPE+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TRANSPORT_TYPE_SERVER_ID +" INTEGER," +
                    TRANSPORT_TYPE_NAME +" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Transport service Table Create Statements
    private static final String CREATE_TABLE_TRANSPORT_SERVICE =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSPORT_SERVICE+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TRANSPORT_SERVICE_SERVER_ID +" INTEGER," +
                    TRANSPORT_SERVICE_NAME +" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMPANY);
        db.execSQL(CREATE_TABLE_COMMODITY);
        db.execSQL(CREATE_TABLE_CUSTOM_CLEARANCE_LOCATION);
        db.execSQL(CREATE_TABLE_CUSTOM_CLEARANCE_CATEGORY);
        db.execSQL(CREATE_TABLE_TERM_OF_SHIPMENT);
        db.execSQL(CREATE_TABLE_TRANSPORTATION);
        db.execSQL(CREATE_TABLE_TRANSPORT_TYPE);
        db.execSQL(CREATE_TABLE_TRANSPORT_SERVICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMODITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_CLEARANCE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_CLEARANCE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM_OF_SHIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORTATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORT_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORT_SERVICE);

        // create new tables
        onCreate(db);
    }
}

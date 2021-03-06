package com.alchemistdigital.kissan.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Category;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.Item;
import com.alchemistdigital.kissan.model.OBP;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Order;
import com.alchemistdigital.kissan.model.Product_Details;
import com.alchemistdigital.kissan.model.Product_Group;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.model.Subcategory;
import com.alchemistdigital.kissan.model.Vendor;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by user on 2/29/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "KissanCart.db";

    // Table Names
    public static final String TABLE_SOCIETY = "Society";
    public static final String TABLE_ENQUIRY = "Enquiry";
    public static final String TABLE_ORDER = "Orders";
    private static final String TABLE_ITEM = "Item";
    private static final String TABLE_OFFLINE = "Offline";
    public static final String TABLE_OBP = "OBP";
    public static final String TABLE_PRODUCT_GROUP = "ProductGroup";
    public static final String TABLE_PRODUCT_CATEGORY = "ProductCategory";
    public static final String TABLE_PRODUCT_SUBCATEGORY = "ProductSubCategory";
    public static final String TABLE_PRODUCT_DETAILS = "ProductDetails";
    public static final String TABLE_ENQUIRY_ADMIN = "EnquiryAdmin";
    public static final String TABLE_VENDORS = "Vendors";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_STATUS = "status";
//    private static final String KEY_OFFLINE_STATUS = "offline"; /*( 0 - online, 1 - offline )*/

    // SOCIETY Table - column names
    private static final String SOCIETY_SERVER_ID = "soc_serverId";
    private static final String SOCIETY_COLUMN_USERID = "uID";
    private static final String SOCIETY_COLUMN_NAME = "soc_name";
    private static final String SOCIETY_COLUMN_CONTACT = "soc_contact";
    private static final String SOCIETY_COLUMN_EMAIL = "soc_email";
    private static final String SOCIETY_COLUMN_ADDRESS = "soc_adrs";

    // ENQUIRY Table - column names
    private static final String ENQUIRY_SERVER_ID = "eServerId";
    private static final String ENQUIRY_REF = "eRef";
    private static final String ENQUIRY_USERID = "uID";
    private static final String ENQUIRY_GROUPID = "gID";
    private static final String ENQUIRY_REPLYTO = "repTo";
    private static final String ENQUIRY_REPLIED = "replied";
    private static final String ENQUIRY_SOCIETY_ID = "eSocietyId";
    private static final String ENQUIRY_SUBCATEGORY_ID = "eSubcategoryId";
    private static final String ENQUIRY_PRODUCT_ID = "eProductId";
    private static final String ENQUIRY_QUANTITY = "eQuantity";

    // ORDER Table - column names
    private static final String ORDER_USERID = "orderUserId";
    private static final String ORDER_REFERENCE = "enqRef";
    private static final String ORDER_UTR = "UTR";

    // ITEM Table - column names
    private static final String ITEM_REFERENCE = "referenceNo";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_QUANTITY = "quantity";
    private static final String ITEM_PRICE = "price";
    private static final String ITEM_TOTAL_AMOUNT = "total_amount";

    // OFFLINE Table - column names
    private static final String OFFLINE_TABLE_NAME = "table_name";
    private static final String OFFLINE_ROW_ID = "row_id";
    private static final String OFFLINE_ROW_ACTION = "row_action";

    // OBP Table - column names
    private static final String OBP_SERVER_ID = "userID_serverId"; // server id
    private static final String OBP_NAME = "obp_name";
    private static final String OBP_STORE_NAME = "obp_store_name";
    private static final String OBP_EMAIL_ID = "obp_email_id";
    private static final String OBP_PASSWORD = "obp_email_passowrd";
    private static final String OBP_CONTACT_NO = "obp_contact_number";
    private static final String OBP_ADDRESS = "obp_address";
    private static final String OBP_PINCODE = "obp_pincode";
    private static final String OBP_CITY = "obp_city";
    private static final String OBP_STATE = "obp_state";
    private static final String OBP_COUNTRY = "obp_country";

    // Product group table - column names
    private static final String PRODUCT_GROUP_NAME = "product_group_name";
    private static final String PRODUCT_GROUP_SERVER_ID = "product_group_server_id";

    // Product category table - column names
    private static final String PRODUCT_GROUP_ID = "product_group_id";
    private static final String CATEGORY_SERVER_ID = "category_server_id";
    private static final String CATEGORY_NAME = "category_name";
    private static final String CATEGORY_IMAGE = "category_image";

    // Product sub category table - column names
    private static final String SUBCATEGORY_SERVER_ID = "subcategory_server_id";
    private static final String CATEGORY_ID = "category_id";
    // present product_group_id column in this table
    /* private static final String PRODUCT_GROUP_ID = "product_group_id";*/
    private static final String SUB_CATEGORY_NAME = "subcategory_name";
    private static final String SUB_CATEGORY_IAMGE = "subcategory_image";

    // Product details table - column names
    private static final String PRODUCT_DETAILS_SERVER_ID = "productdetails_server_id";
    private static final String SUB_CATEGORY_ID = "subcategory_id";
    // present product_group_id and category_id column in this table
    /* private static final String PRODUCT_GROUP_ID = "product_group_id";
     private static final String CATEGORY_ID = "category_id";*/
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_PATH = "product_path";
    private static final String PRODUCT_PATH1 = "product_path1";
    private static final String PRODUCT_PATH2 = "product_path2";
    private static final String PRODUCT_PATH3 = "product_path3";
    private static final String PRODUCT_PATH4 = "product_path4";
    private static final String PRODUCT_DESCRIPTION = "product_description";
    private static final String PRODUCT_DISCOUNT_TYPE = "product_discount_type";

    // Enquiry Admin table - column names
    private static final String  ADMIN_ENQUIRY_SERVER_ID= "admin_enquiry_server_id";
    private static final String  ADMIN_ENQUIRY_USERID= "admin_enquiry_userid";
    private static final String  ENQUIRY_REFERENCE= "enquiry_reference";
    private static final String  VENDOR_ID= "vendor_id";
    // present subcategory_id column in this table
    /* private static final String SUB_CATEGORY_ID = "subcategory_id";*/
    private static final String PRODUCT_ID= "product_id";
    private static final String QUANTITY= "quantity";
    private static final String PRICE= "price";


    // Vendors Table - column names
    private static final String VENDOR_SERVER_ID= "vendor_server_id";
    private static final String VENDOR_NAME= "vendor_name";
    private static final String VENDOR_CONTACT= "vendor_contact";
    private static final String VENDOR_ADDRESS= "vendor_address";
    private static final String VENDOR_EMAIL= "vendor_email";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Society Table Create Statements
        private static final String CREATE_TABLE_SOCIETY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_SOCIETY +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SOCIETY_SERVER_ID+" INTEGER," +
                SOCIETY_COLUMN_USERID+" INTEGER," +
                SOCIETY_COLUMN_NAME+" VARCHAR(100)," +
                SOCIETY_COLUMN_CONTACT+" VARCHAR(100), " +
                SOCIETY_COLUMN_EMAIL+" VARCHAR(100)," +
                SOCIETY_COLUMN_ADDRESS+" VARCHAR(200)," +
                KEY_STATUS +" TINYINT(4)," +
                KEY_CREATED_AT + " DATETIME" + ")";

    // Enquiry Table Create Statements
    private static final String CREATE_TABLE_ENQUIRY =
        "CREATE TABLE IF NOT EXISTS "+ TABLE_ENQUIRY +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ENQUIRY_SERVER_ID +" INTEGER," +
            ENQUIRY_REF +" VARCHAR(20)," +
            ENQUIRY_USERID +" INTEGER," +
            ENQUIRY_GROUPID +" INTEGER," +
            ENQUIRY_REPLYTO +" INTEGER," +
            ENQUIRY_REPLIED +" TINYINT(4)," +
            ENQUIRY_SOCIETY_ID + " INTEGER,"+
            ENQUIRY_SUBCATEGORY_ID + " INTEGER, "+
            ENQUIRY_PRODUCT_ID + " INTEGER, "+
            ENQUIRY_QUANTITY + " VARCHAR(100), "+
            KEY_STATUS +" TINYINT(4)," +
            KEY_CREATED_AT + " DATETIME" + ")";

    // Item Table Create Statement
    private static final String CREATE_TABLE_ITEM =
        "CREATE TABLE IF NOT EXISTS "+ TABLE_ITEM +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ITEM_REFERENCE +" VARCHAR(50)," +
            ITEM_NAME +" VARCHAR(50)," +
            ITEM_QUANTITY +" INTEGER,"+
            ITEM_PRICE +" VARCHAR(50)," +
            ITEM_TOTAL_AMOUNT +" VARCHAR(50)," +
            KEY_CREATED_AT + " DATETIME" + ")";

    // Order Table Create Statement
    private static final String CREATE_TABLE_ORDER =
        "CREATE TABLE IF NOT EXISTS "+ TABLE_ORDER +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ORDER_USERID + " INTEGER,"+
            ORDER_REFERENCE +" VARCHAR(40),"+
            ORDER_UTR + " VARCHAR(40),"+
            KEY_STATUS +" TINYINT(4)," +
            KEY_CREATED_AT + " DATETIME" + ")";

    // Offline Table Cretate statement
    private static final String CREATE_TABLE_OFFLINE =
        "CREATE TABLE IF NOT EXISTS "+ TABLE_OFFLINE +
            "("+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            OFFLINE_TABLE_NAME +" VARCHAR(40)," +
            OFFLINE_ROW_ID +" INTEGER," +
            OFFLINE_ROW_ACTION +" VARCHAR(40)," +
            KEY_CREATED_AT + " DATETIME" + ")";

    // OBP Table create statement
    private static final String CREATE_TABLE_OBP =
        "CREATE TABLE IF NOT EXISTS "+ TABLE_OBP +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            OBP_SERVER_ID +" INTEGER," +
            OBP_NAME +" VARCHAR(50)," +
            OBP_STORE_NAME +" VARCHAR(50)," +
            OBP_EMAIL_ID +" VARCHAR(50)," +
            OBP_PASSWORD +" VARCHAR(50)," +
            OBP_CONTACT_NO +" VARCHAR(20)," +
            OBP_ADDRESS +" VARCHAR(300)," +
            OBP_PINCODE +" INTEGER," +
            OBP_CITY +" VARCHAR(50)," +
            OBP_STATE +" VARCHAR(50)," +
            OBP_COUNTRY +" VARCHAR(50)," +
            KEY_STATUS +" TINYINT(4)," +
            KEY_CREATED_AT + " DATETIME" + ")";

    // Product group Table create statement
    private static final String CREATE_TABLE_PRODUCT_GROUP =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_PRODUCT_GROUP +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PRODUCT_GROUP_SERVER_ID+" INTEGER," +
                    PRODUCT_GROUP_NAME+" INTEGER," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_CATEGORY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_PRODUCT_CATEGORY +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PRODUCT_GROUP_ID +" INTEGER," +
                    CATEGORY_SERVER_ID +" INTEGER," +
                    CATEGORY_NAME +" VARCHAR(50)," +
                    CATEGORY_IMAGE +" VARCHAR(50)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_SUBCATEGORY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_PRODUCT_SUBCATEGORY+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SUBCATEGORY_SERVER_ID +" INTEGER," +
                    CATEGORY_ID +" INTEGER," +
                    PRODUCT_GROUP_ID +" INTEGER," +
                    SUB_CATEGORY_NAME +" VARCHAR(50)," +
                    SUB_CATEGORY_IAMGE +" VARCHAR(50)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_PRODUCT_DETAILS =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_PRODUCT_DETAILS+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PRODUCT_DETAILS_SERVER_ID +" INTEGER," +
                    SUB_CATEGORY_ID +" INTEGER," +
                    CATEGORY_ID +" INTEGER," +
                    PRODUCT_GROUP_ID +" INTEGER," +
                    PRODUCT_NAME +" VARCHAR(50)," +
                    PRODUCT_PATH +" VARCHAR(50)," +
                    PRODUCT_PATH1 +" VARCHAR(50)," +
                    PRODUCT_PATH2 +" VARCHAR(50)," +
                    PRODUCT_PATH3 +" VARCHAR(50)," +
                    PRODUCT_PATH4 +" VARCHAR(50)," +
                    PRODUCT_DESCRIPTION +" VARCHAR(50)," +
                    PRODUCT_DISCOUNT_TYPE +" VARCHAR(50)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_ENQUIRY_ADMIN =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_ENQUIRY_ADMIN+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ADMIN_ENQUIRY_SERVER_ID +" INTEGER," +
                    ADMIN_ENQUIRY_USERID + " INTEGER,"+
                    ENQUIRY_REFERENCE +" INTEGER," +
                    VENDOR_ID +" INTEGER," +
                    SUB_CATEGORY_ID +" INTEGER," +
                    PRODUCT_ID +" INTEGER," +
                    QUANTITY +" INTEGER," +
                    PRICE +" INTEGER," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_VENDOR =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_VENDORS +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VENDOR_SERVER_ID +" INTEGER," +
                    VENDOR_NAME +" VARCHAR(50)," +
                    VENDOR_CONTACT +" VARCHAR(50)," +
                    VENDOR_ADDRESS +" VARCHAR(50)," +
                    VENDOR_EMAIL +" VARCHAR(50)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_SOCIETY);
        db.execSQL(CREATE_TABLE_ENQUIRY);
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_OFFLINE);
        db.execSQL(CREATE_TABLE_OBP);
        db.execSQL(CREATE_TABLE_PRODUCT_GROUP);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_SUBCATEGORY);
        db.execSQL(CREATE_TABLE_PRODUCT_DETAILS);
        db.execSQL(CREATE_TABLE_ENQUIRY_ADMIN);
        db.execSQL(CREATE_TABLE_VENDOR);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOCIETY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENQUIRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBP);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PRODUCT_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SUBCATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PRODUCT_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ENQUIRY_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VENDOR);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "Society" table methods ----------------//
    public long insertSociety(Society societyColumn) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SOCIETY_SERVER_ID, societyColumn.getServerId());
        contentValues.put(SOCIETY_COLUMN_USERID, societyColumn.getUserId());
        contentValues.put(SOCIETY_COLUMN_NAME, societyColumn.getSoc_name());
        contentValues.put(SOCIETY_COLUMN_CONTACT, societyColumn.getSoc_contact());
        contentValues.put(SOCIETY_COLUMN_EMAIL, societyColumn.getSoc_email());
        contentValues.put(SOCIETY_COLUMN_ADDRESS, societyColumn.getSoc_adrs());
        contentValues.put(KEY_STATUS, societyColumn.getSoc_status());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long society_id = 0;
        try {

            String whereClause = SOCIETY_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( societyColumn.getServerId() )} ;

            /*int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_SOCIETY,whereClause,whereArgs);
            if( numRows <= 0 ) {
            }*/
            society_id = db.insert(TABLE_SOCIETY, null, contentValues);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return society_id;
    }

    /**
     * get single society
     */
    public Society getSocietyById(int society_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT * FROM " + TABLE_SOCIETY + " WHERE "
                + KEY_ID + " = " + society_id + " AND " + SOCIETY_COLUMN_USERID + " = " + uId
                +" AND "+ KEY_STATUS + " = 1;" ;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Society society = new Society();
        assert c != null;
        society.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        society.setUserId(c.getInt(c.getColumnIndex(SOCIETY_COLUMN_USERID)));
        society.setSoc_name(c.getString(c.getColumnIndex(SOCIETY_COLUMN_NAME)));
        society.setSoc_contact(c.getString(c.getColumnIndex(SOCIETY_COLUMN_CONTACT)));
        society.setSoc_email(c.getString(c.getColumnIndex(SOCIETY_COLUMN_EMAIL)));
        society.setSoc_adrs(c.getString(c.getColumnIndex(SOCIETY_COLUMN_ADDRESS)));
        society.setCreted_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return society;
    }

    public List<Society> getOfflineSocietyById(int offline_row_id, String offline_row_action) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Society> array_list = new ArrayList<Society>();

        String selectQuery = "SELECT * FROM " + TABLE_SOCIETY + " WHERE "
                + KEY_ID + " = " + offline_row_id ;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Society society = new Society();
        assert c != null;
        society.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        society.setUserId(c.getInt(c.getColumnIndex(SOCIETY_COLUMN_USERID)));
        society.setSoc_name(c.getString(c.getColumnIndex(SOCIETY_COLUMN_NAME)));
        society.setSoc_contact(c.getString(c.getColumnIndex(SOCIETY_COLUMN_CONTACT)));
        society.setSoc_email(c.getString(c.getColumnIndex(SOCIETY_COLUMN_EMAIL)));
        society.setSoc_adrs(c.getString(c.getColumnIndex(SOCIETY_COLUMN_ADDRESS)));
        society.setCreted_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        society.setServerId(c.getInt(c.getColumnIndex(SOCIETY_SERVER_ID)));
        society.setSoc_status(c.getInt(c.getColumnIndex(KEY_STATUS)));
        society.setSoc_offline_action(offline_row_action);
        array_list.add(society);

        return array_list;

    }

    /**
     * getting society count
     */
    public int numberOfSocietyRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String whereClause = SOCIETY_COLUMN_USERID +" = ? ";
        String[] whereArgs = new String[]{ String.valueOf(uId) } ;

        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_SOCIETY,whereClause,whereArgs);
        return numRows;
    }

    public int numberOfSocietyRowsByStatus() {
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String whereClause = SOCIETY_COLUMN_USERID+" = ? AND "+KEY_STATUS+" = ?";
        String[] whereArgs = new String[]{ String.valueOf(uId), String.valueOf("1") } ;

        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_SOCIETY,whereClause,whereArgs);
        return numRows;
    }

    /**
     * Updating a society
     */
    public boolean updateSociety(Society society) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SOCIETY_COLUMN_NAME, society.getSoc_name());
        contentValues.put(SOCIETY_COLUMN_CONTACT, society.getSoc_contact());
        contentValues.put(SOCIETY_COLUMN_EMAIL, society.getSoc_email());
        contentValues.put(SOCIETY_COLUMN_ADDRESS, society.getSoc_adrs());
        contentValues.put(KEY_STATUS, society.getSoc_status());

        String whereClause = null;
        String[] whereArgs;

        // when offline society row updated
        // then server id will be o
        if ( society.getServerId() == 0 ) {
            whereClause = KEY_ID + " = ? ";
            whereArgs = new String[]{String.valueOf(society.getId())} ;
        }
        else {
            whereClause = SOCIETY_SERVER_ID + " = ? ";
            whereArgs = new String[]{String.valueOf(society.getServerId())} ;
        }

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_SOCIETY, contentValues, whereClause, whereArgs);
        return true;
    }

    /**
     * Deleting a society
     */
    public Integer deleteSociety (Society societyObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, "0");

        String whereClause = null ;
        String[] whereArgs;

        // when offline society row updated
        // then server id will be o
        if ( societyObj.getServerId() == 0 ) {
            whereClause = KEY_ID + " = ? ";
            whereArgs = new String[]{String.valueOf(societyObj.getId())} ;
        }
        else {
            whereClause = SOCIETY_SERVER_ID + " = ? ";
            whereArgs = new String[]{String.valueOf(societyObj.getServerId())} ;
        }

        return db.update(TABLE_SOCIETY, contentValues, whereClause, whereArgs);
    }

    /**
     * getting all societies
    **/
    public ArrayList<Society> getAllSocieties() {

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        ArrayList<Society> array_list = new ArrayList<Society>();
        String selectQuery = "SELECT * FROM "+TABLE_SOCIETY+" WHERE "+SOCIETY_COLUMN_USERID+" = "+uId
                            +" AND "+KEY_STATUS+" = 1 ORDER BY "+KEY_CREATED_AT;
        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Society society = new Society();

                society.setId(res.getInt(res.getColumnIndex(KEY_ID)));
                society.setUserId(res.getInt(res.getColumnIndex(SOCIETY_COLUMN_USERID)));
                society.setSoc_name(res.getString(res.getColumnIndex(SOCIETY_COLUMN_NAME)));
                society.setSoc_contact(res.getString(res.getColumnIndex(SOCIETY_COLUMN_CONTACT)));
                society.setSoc_email(res.getString(res.getColumnIndex(SOCIETY_COLUMN_EMAIL)));
                society.setSoc_adrs(res.getString(res.getColumnIndex(SOCIETY_COLUMN_ADDRESS)));
                society.setCreted_at(res.getString(res.getColumnIndex(KEY_CREATED_AT)));
                society.setServerId(res.getInt(res.getColumnIndex(SOCIETY_SERVER_ID)));
                society.setSoc_status(res.getInt(res.getColumnIndex(KEY_STATUS)));

//                adding to society list
                array_list.add(society);
            }while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * update server id when offline data inserted on server while internet is on.
     * function - InsertOfflineSocietyDataAsyncTask
     * @param societyId
     * @param serverId
     * @return
     */
    public boolean updateServerIdOfSociety(String societyId, int serverId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SOCIETY_SERVER_ID, serverId);

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_SOCIETY, contentValues, KEY_ID + " = ? ", new String[]{ societyId });
        return true;
    }

    // ------------------------ "Society" table methods ----------------//


    // ------------------------ "Enquiry" table methods ----------------//
    public long insertEnquiry(Enquiry enquiryColumn) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ENQUIRY_SERVER_ID, enquiryColumn.getEnquiry_server_id());
        contentValues.put(KEY_STATUS, enquiryColumn.getEnquiry_status());
        contentValues.put(KEY_CREATED_AT, enquiryColumn.getCreted_at());
        contentValues.put(ENQUIRY_REF, enquiryColumn.getEnquiry_reference());
        contentValues.put(ENQUIRY_USERID, enquiryColumn.getEnquiry_userId());
        contentValues.put(ENQUIRY_GROUPID, enquiryColumn.getEnquiry_groupId());
        contentValues.put(ENQUIRY_REPLYTO, enquiryColumn.getEnquiry_replyTo());
        contentValues.put(ENQUIRY_REPLIED, enquiryColumn.getEnquiry_replied());
        contentValues.put(ENQUIRY_SOCIETY_ID, enquiryColumn.getEnquiry_societyId());
        contentValues.put(ENQUIRY_SUBCATEGORY_ID, enquiryColumn.getEnquiry_subCategoryId());
        contentValues.put(ENQUIRY_PRODUCT_ID, enquiryColumn.getEnquiry_productId());
        contentValues.put(ENQUIRY_QUANTITY, enquiryColumn.getEnquiry_quantity());

        // insert row
        long enquiry_id = db.insert(TABLE_ENQUIRY, null, contentValues);
        return enquiry_id;
    }

    /**
     * Updating a society
     */
    public int updateEnquiryReplied(String eId,String repliedVal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ENQUIRY_REPLIED, repliedVal);

//      db.update(String table_name,String where_clause,String[] where_args);
        int update = db.update(TABLE_ENQUIRY, contentValues, ENQUIRY_SERVER_ID + " = ? ", new String[]{eId});
        return update;
    }

    /**
     * get enquiry id for storing in column_id in offline table
     * @param serverId
     * @return
     */
    public int getEnquiryIdByEnquiryServerId(String serverId) {
        SQLiteDatabase db = this.getReadableDatabase();

        int id = 0;
        String selectQuery = "SELECT * FROM " + TABLE_ENQUIRY + " WHERE "
                + ENQUIRY_SERVER_ID + " = " + serverId;

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{

                id = res.getInt(res.getColumnIndex(KEY_ID));

            }while (res.moveToNext());
        }
        return id;

    }

    /**
     * get all enquiry by user id
     */
    public List<Enquiry> getEnquiryByUid() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Enquiry> array_list = new ArrayList<Enquiry>();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT * FROM " + TABLE_ENQUIRY + " WHERE "
                + ENQUIRY_USERID + " = " + uId +" AND "+KEY_STATUS+" = 1 ORDER BY "+KEY_CREATED_AT;

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Enquiry enquiry = new Enquiry();
                String formatedDate = null;

                String date = res.getString(res.getColumnIndex(KEY_CREATED_AT));
                int abc = date.lastIndexOf(':');
                if (abc != -1) {
                    formatedDate = date.substring(0,abc);
                }

                enquiry.setEnquiry_id(res.getInt(res.getColumnIndex(KEY_ID)));
                enquiry.setEnquiry_reference(res.getString(res.getColumnIndex(ENQUIRY_REF)));
                enquiry.setEnquiry_userId(res.getInt(res.getColumnIndex(ENQUIRY_USERID)));
                enquiry.setEnquiry_groupId(res.getInt(res.getColumnIndex(ENQUIRY_GROUPID)));
                enquiry.setEnquiry_replyTo(res.getInt(res.getColumnIndex(ENQUIRY_REPLYTO)));
                enquiry.setEnquiry_replied(res.getInt(res.getColumnIndex(ENQUIRY_REPLIED)));
                enquiry.setEnquiry_societyId(res.getInt(res.getColumnIndex(ENQUIRY_SOCIETY_ID)));
                enquiry.setEnquiry_subCategoryId(res.getInt(res.getColumnIndex(ENQUIRY_SUBCATEGORY_ID)));
                enquiry.setEnquiry_productId(res.getInt(res.getColumnIndex(ENQUIRY_PRODUCT_ID)));
                enquiry.setEnquiry_quantity(res.getString(res.getColumnIndex(ENQUIRY_QUANTITY)));
                enquiry.setEnquiry_server_id(res.getInt(res.getColumnIndex(ENQUIRY_SERVER_ID)));
                enquiry.setCreted_at(formatedDate);

                array_list.add(enquiry);

            }while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * getting enquiry count using user id
     */
    public int numberOfEnquiryRowsByUid() {
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_ENQUIRY);
        return numRows;
    }

    /**
     * if result get null value with id that means obp create fisrt time enquiry with reference.
     * then it goes to by default to admin
     * if result gets value with eID then it goes in reply scenario.
     * get user-id from currently clicked enqiry and store it into replyTo variable.
     * @param id
     * @return
     */
    public int numberOfEnquiryRowsByServerID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = ENQUIRY_SERVER_ID + " = ? ";
        String[] whereArgs = new String[]{ id };

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_ENQUIRY, whereClause, whereArgs);
        return numRows;
    }

    public int getUserIdByServerIdInEnquired(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = 0;

        String selectQuery = "SELECT "+ENQUIRY_USERID+" FROM " + TABLE_ENQUIRY + " WHERE "
                + ENQUIRY_SERVER_ID + " = " + id ;

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                userId = res.getInt(res.getColumnIndex(ENQUIRY_USERID));
            }while (res.moveToNext());
        }
        return userId;
    }

    public int numberOfEnquiryRowsByUidAndStatus(){
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String whereClause = ENQUIRY_USERID + " = ? AND "+KEY_STATUS + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(uId), String.valueOf("1")};

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_ENQUIRY, whereClause, whereArgs);
        return numRows;
    }

    /**
     * getting enquiry count using replyTo with logged in user id and replied with 0 value.
     */
    public int numberOfEnquiryRowsByReplyto(){
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_ENQUIRY + " e WHERE " +
                "e."+ ENQUIRY_REPLYTO + " = " + uId+" AND " +
                "e."+ ENQUIRY_REPLIED+" = 0 " +"AND " +
                "e."+ KEY_STATUS+" = 1 AND " +
                "e."+ ENQUIRY_REF +" NOT IN (SELECT o."+ORDER_REFERENCE+" FROM "+TABLE_ORDER+" o GROUP BY o."
                +ORDER_REFERENCE+") ORDER BY e."+KEY_CREATED_AT;

        int numRows = (int) DatabaseUtils.longForQuery(db, selectQuery, null);

        return numRows;
    }

    public List<Enquiry> getEnquiryByReplytoAndReplied() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Enquiry> array_list = new ArrayList<Enquiry>();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT * FROM " + TABLE_ENQUIRY + " e WHERE " +
                "e."+ ENQUIRY_REPLYTO + " = " + uId+" AND " +
                "e."+ENQUIRY_REPLIED+" = 0 " +"AND " +
                "e."+KEY_STATUS +" = 1 AND " +
                "e."+ENQUIRY_REF +" NOT IN (SELECT o."+ORDER_REFERENCE+" FROM "+TABLE_ORDER+" o GROUP BY o."
                +ORDER_REFERENCE+") ORDER BY e."+KEY_CREATED_AT;

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Enquiry enquiry = new Enquiry();
                String formatedDate = null;

                String date = res.getString(res.getColumnIndex(KEY_CREATED_AT));
                int abc = date.lastIndexOf(':');
                if (abc != -1) {
                    formatedDate = date.substring(0,abc);
                }

                enquiry.setEnquiry_id(res.getInt(res.getColumnIndex(KEY_ID)));
                enquiry.setEnquiry_reference(res.getString(res.getColumnIndex(ENQUIRY_REF)));
                enquiry.setEnquiry_userId(res.getInt(res.getColumnIndex(ENQUIRY_USERID)));
                enquiry.setEnquiry_groupId(res.getInt(res.getColumnIndex(ENQUIRY_GROUPID)));
                enquiry.setEnquiry_replyTo(res.getInt(res.getColumnIndex(ENQUIRY_REPLYTO)));
                enquiry.setEnquiry_replied(res.getInt(res.getColumnIndex(ENQUIRY_REPLIED)));
                enquiry.setEnquiry_societyId(res.getInt(res.getColumnIndex(ENQUIRY_SOCIETY_ID)));
                enquiry.setEnquiry_subCategoryId(res.getInt(res.getColumnIndex(ENQUIRY_SUBCATEGORY_ID)));
                enquiry.setEnquiry_productId(res.getInt(res.getColumnIndex(ENQUIRY_PRODUCT_ID)));
                enquiry.setEnquiry_quantity(res.getString(res.getColumnIndex(ENQUIRY_QUANTITY)));
                enquiry.setEnquiry_server_id(res.getInt(res.getColumnIndex(ENQUIRY_SERVER_ID)));
                enquiry.setCreted_at(formatedDate);

                array_list.add(enquiry);

            }while (res.moveToNext());
        }
        return array_list;

    }

    public List<Enquiry> getReferenceNumberForOrder(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Enquiry> array_list = new ArrayList<Enquiry>();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        // gets reference number from enquiry table
        // Condition :-
        // group id from enquiry with value 2 (get only obp data)
        // AND
        // replied from enquiry with value 1 (get only admin replied enquiry from enquiry table)
        // AND
        // reference no which are not in order table. i.e. oreder not created of that reference number.
        String selectQuery = "SELECT DISTINCT e."+ENQUIRY_REF+" FROM " + TABLE_ENQUIRY + " e WHERE " +
                "e."+ENQUIRY_USERID+ " = " +uId+" AND " +
                "e."+ENQUIRY_GROUPID+" = 2 AND " +
                "e."+ENQUIRY_REPLIED+" = 1 AND " +
                "e."+KEY_STATUS+" = 1 AND " +
                " e."+ENQUIRY_REF+" NOT IN (SELECT o."+ORDER_REFERENCE+" FROM "+TABLE_ORDER+" o GROUP BY o."
                +ORDER_REFERENCE+") ORDER BY e."+KEY_CREATED_AT;
//        String selectQuery = "SELECT * FROM "+TABLE_ENQUIRY;
        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do {
                Enquiry enquiry = new Enquiry();
                enquiry.setEnquiry_reference(res.getString(res.getColumnIndex(ENQUIRY_REF)));
//                System.out.println("Ref no-" + res.getString(res.getColumnIndex(ENQUIRY_REF)) + ", USER id-" + res.getString(res.getColumnIndex(ENQUIRY_USERID)) + ", GROUP no-" + res.getString(res.getColumnIndex(ENQUIRY_GROUPID)) + ", Replied no-" + res.getString(res.getColumnIndex(ENQUIRY_REPLIED))+ ", Satus-"+res.getString(res.getColumnIndex(KEY_STATUS)));
                array_list.add(enquiry);
            }while (res.moveToNext());
        }
        return array_list;
    }

    public void deleteEnquiryTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENQUIRY);
    }

    /**
     * get enquiry by reference
     * purposely for showing society details in order details
     */
    public List<Enquiry> getEnquiryByReference(String reference) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Enquiry> array_list = new ArrayList<Enquiry>();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT * FROM " + TABLE_ENQUIRY + " WHERE "
                + ENQUIRY_REF + " = '"+reference+"' ORDER BY "+KEY_CREATED_AT + " LIMIT 1;";

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Enquiry enquiry = new Enquiry();
                String formatedDate = null;

                String date = res.getString(res.getColumnIndex(KEY_CREATED_AT));
                int abc = date.lastIndexOf(':');
                if (abc != -1) {
                    formatedDate = date.substring(0,abc);
                }

                enquiry.setEnquiry_id(res.getInt(res.getColumnIndex(KEY_ID)));
                enquiry.setEnquiry_reference(res.getString(res.getColumnIndex(ENQUIRY_REF)));
                enquiry.setEnquiry_userId(res.getInt(res.getColumnIndex(ENQUIRY_USERID)));
                enquiry.setEnquiry_groupId(res.getInt(res.getColumnIndex(ENQUIRY_GROUPID)));
                enquiry.setEnquiry_replyTo(res.getInt(res.getColumnIndex(ENQUIRY_REPLYTO)));
                enquiry.setEnquiry_replied(res.getInt(res.getColumnIndex(ENQUIRY_REPLIED)));
                enquiry.setEnquiry_societyId(res.getInt(res.getColumnIndex(ENQUIRY_SOCIETY_ID)));
                enquiry.setEnquiry_subCategoryId(res.getInt(res.getColumnIndex(ENQUIRY_SUBCATEGORY_ID)));
                enquiry.setEnquiry_productId(res.getInt(res.getColumnIndex(ENQUIRY_PRODUCT_ID)));
                enquiry.setEnquiry_quantity(res.getString(res.getColumnIndex(ENQUIRY_QUANTITY)));
                enquiry.setEnquiry_server_id(res.getInt(res.getColumnIndex(ENQUIRY_SERVER_ID)));
                enquiry.setCreted_at(formatedDate);

                array_list.add(enquiry);

            }while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * update society details in enquiry table when society details update
     * @param enquiry
     * @param oldSocietyName
     * @return
     */
   /* public boolean updateSocietyColumnInEnquiryTable(Enquiry enquiry, String oldSocietyName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ENQUIRY_SOCIETY, enquiry.getEnquiry_society());
        contentValues.put(ENQUIRY_SOCIETY_CONTACT, enquiry.getEnquiry_society_contact());
        contentValues.put(ENQUIRY_SOCIETY_EMAIL, enquiry.getEnquiry_society_email());
        contentValues.put(ENQUIRY_SOCIETY_ADDRESS, enquiry.getEnquiry_society_address());

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_ENQUIRY, contentValues, ENQUIRY_SOCIETY + " = ? ", new String[]{oldSocietyName});
        return true;
    }*/

    /**
     *  @param uId
     * @param selectedSubcategoryServerId
     * @param selectedProductServerId
     * @param str_ref_no
     * @return numRows
     */
    public int checkEnquiryEntryPresent(int uId,
                                        int selectedSocietyServerId,
                                        int selectedSubcategoryServerId,
                                        int selectedProductServerId,
                                        String str_ref_no) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = ENQUIRY_USERID + " = ? AND "
                + ENQUIRY_REF+ " = ? AND "
                + ENQUIRY_SOCIETY_ID+ " = ? AND "
                + ENQUIRY_SUBCATEGORY_ID+" = ? AND "
                + ENQUIRY_PRODUCT_ID;

        String[] whereArgs = new String[]{ String.valueOf(uId),
                str_ref_no,
                String.valueOf(selectedSocietyServerId),
                String.valueOf(selectedSubcategoryServerId),
                String.valueOf(selectedProductServerId) };

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_ENQUIRY, whereClause, whereArgs);

        return numRows;
    }

    /**
     * get all enquiry data which are store in offline mode.
     * @param id
     * @param action
     * @return
     */
    public List<Enquiry> getEnquiryByID(int id, String action) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Enquiry> array_list = new ArrayList<Enquiry>();

        String selectQuery = "SELECT * FROM " + TABLE_ENQUIRY + " WHERE "
                + KEY_ID + " = " + id;

//        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if (res.moveToFirst()){
            do{
                Enquiry enquiry = new Enquiry();

                enquiry.setEnquiry_id(res.getInt(res.getColumnIndex(KEY_ID)));
                enquiry.setEnquiry_reference(res.getString(res.getColumnIndex(ENQUIRY_REF)));
                enquiry.setEnquiry_userId(res.getInt(res.getColumnIndex(ENQUIRY_USERID)));
                enquiry.setEnquiry_groupId(res.getInt(res.getColumnIndex(ENQUIRY_GROUPID)));
                enquiry.setEnquiry_replyTo(res.getInt(res.getColumnIndex(ENQUIRY_REPLYTO)));
                enquiry.setEnquiry_replied(res.getInt(res.getColumnIndex(ENQUIRY_REPLIED)));
                enquiry.setEnquiry_societyId(res.getInt(res.getColumnIndex(ENQUIRY_SOCIETY_ID)));
                enquiry.setEnquiry_subCategoryId(res.getInt(res.getColumnIndex(ENQUIRY_SUBCATEGORY_ID)));
                enquiry.setEnquiry_productId(res.getInt(res.getColumnIndex(ENQUIRY_PRODUCT_ID)));
                enquiry.setEnquiry_quantity(res.getString(res.getColumnIndex(ENQUIRY_QUANTITY)));
                enquiry.setCreted_at( res.getString(res.getColumnIndex(KEY_CREATED_AT)));
                enquiry.setEnquiry_server_id(res.getInt(res.getColumnIndex(ENQUIRY_SERVER_ID)));
                enquiry.setEnquiry_offline_action( action );

                array_list.add(enquiry);

            }while (res.moveToNext());
        }
        return array_list;

    }

    /**
     * update server id when offline data inserted on server while internet is on.
     * function - InsertOfflineEnquiryDataAsyncTask
     * @param enquiryId
     * @param serverId
     * @return
     */
    public boolean updateServerIdOfEnquiry(String enquiryId, int serverId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ENQUIRY_SERVER_ID, serverId );

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_ENQUIRY, contentValues, KEY_ID + " = ? ", new String[]{ enquiryId });
        return true;
    }
    // ------------------------ "Enquiry" table methods ----------------//


    // ------------------------ "Orders" table methods ----------------//
    public long insertOrder(Order orderColumn) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, orderColumn.getOrder_status());
        contentValues.put(ORDER_USERID, orderColumn.getUserId());
        contentValues.put(ORDER_REFERENCE, orderColumn.getOrder_reference());
        contentValues.put(ORDER_UTR, orderColumn.getOrder_utr());
       /* contentValues.put(ORDER_ITEM, orderColumn.getOrder_item());
        contentValues.put(ORDER_QUANTITY, orderColumn.getOrder_quantity());
        contentValues.put(ORDER_PRICE, orderColumn.getOrder_price());
        contentValues.put(ORDER_TOTAL_AMOUNT, orderColumn.getOrder_total_amount());*/
        contentValues.put(KEY_CREATED_AT, orderColumn.getOrder_creted_at());

        // insert row
        long order_id = db.insert(TABLE_ORDER, null, contentValues);
        return order_id;
    }

    /**
     * getting order count
     */
    public int numberOfOrderRows(){
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = KEY_STATUS+" = ? ";
        String[] whereArgs = new String[]{ String.valueOf("1") } ;

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_ORDER, whereClause, whereArgs);
        return numRows;

    }

    /**
     *
     * @param referenceno
     * @return numRows
     */
    public int numberOfOrderByRefNo(String referenceno){
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = ORDER_REFERENCE+" = ? ";
        String[] whereArgs = new String[]{ String.valueOf(referenceno) } ;
        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_ORDER, whereClause, whereArgs);

        return  numRows;
    }

    /**
     * get count of order depending upon user type
     * if user is admin then take all order details irrespective of userid.
     * if uer is obp then take users data only using userId.
     *
     * @return numRows
     * @param userType
     */
    public int numberOfOrderRowsByUserType(String userType) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows;

        if( userType.equals("obp") ) {

            GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
            int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

            String whereClause = ORDER_USERID+" = ? ";
            String[] whereArgs = new String[]{ String.valueOf(uId) } ;
            numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_ORDER, whereClause, whereArgs);
        } else {
            numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_ORDER);
        }

        return numRows;
    }

    public List<Order> getAllOrder( ) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> array_list = new ArrayList<Order>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER + " WHERE "+KEY_STATUS+" = 1 ORDER BY "+KEY_CREATED_AT;

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Order order = new Order();

                order.setOrder_id(res.getInt(res.getColumnIndex(KEY_ID)));
                order.setOrder_reference(res.getString(res.getColumnIndex(ORDER_REFERENCE)));
                order.setOrder_utr(res.getString(res.getColumnIndex(ORDER_UTR)));
                order.setUserId(res.getInt(res.getColumnIndex(ORDER_USERID)));
                order.setOrder_creted_at(res.getString(res.getColumnIndex(KEY_CREATED_AT)));

                array_list.add(order);

            }while (res.moveToNext());
        }
        return array_list;
    }

    public List<Order> getOfflineOrderById(int offline_row_id, String offline_row_action) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> array_list = new ArrayList<Order>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER + " WHERE "
                + KEY_ID + " = " + offline_row_id ;

        Cursor res = db.rawQuery(selectQuery, null);

        if (res != null) {
            res.moveToFirst();
        }

        Order orderRow = new Order();

        orderRow.setOrder_id(res.getInt(res.getColumnIndex(KEY_ID)));
        orderRow.setUserId(res.getInt(res.getColumnIndex(ORDER_USERID)));
        orderRow.setOrder_reference(res.getString(res.getColumnIndex(ORDER_REFERENCE)));
        orderRow.setOrder_utr(res.getString(res.getColumnIndex(ORDER_UTR)));
        orderRow.setOrder_status(res.getInt(res.getColumnIndex(KEY_STATUS)));
        orderRow.setOrder_creted_at(res.getString(res.getColumnIndex(KEY_CREATED_AT)));
        orderRow.setOrder_offline_action(offline_row_action);

        // get items from item table using reference no.
        List<Item> itemByRefno = getItemByRefno(res.getString(res.getColumnIndex(ORDER_REFERENCE)));
        JSONArray jsonArr = new JSONArray();
        for (Item pn : itemByRefno ) {
            JSONObject pnObj = new JSONObject();
            try {
                pnObj.put("itemName", pn.getItemName());
                pnObj.put("itemQuantity", pn.getItemQuantity());
                pnObj.put("itemPrice", pn.getItemPrice());
                pnObj.put("itemTotalAmount", pn.getItemTotalAmount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArr.put(pnObj);
        }

        orderRow.setOrder_items( String.valueOf(jsonArr).replaceAll("\"", Matcher.quoteReplacement("\\\"")));
        array_list.add(orderRow);

        return array_list;
    }

    // ------------------------ "Orders" table methods ----------------//



    // ------------------------ "Item" table methods ----------------//
    public long insertItem(Item itemColumn){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CREATED_AT, getDateTime());
        contentValues.put(ITEM_REFERENCE, itemColumn.getReferenceNo());
        contentValues.put(ITEM_NAME, itemColumn.getItemName());
        contentValues.put(ITEM_QUANTITY, itemColumn.getItemQuantity());
        contentValues.put(ITEM_PRICE, itemColumn.getItemPrice());
        contentValues.put(ITEM_TOTAL_AMOUNT, itemColumn.getItemTotalAmount());

        String whereClause = ITEM_REFERENCE+" = ? AND "+ITEM_NAME+" = ? AND "+ITEM_QUANTITY+" = ? AND "
                +ITEM_PRICE+" = ?";
        String[] whereArgs = new String[]{ itemColumn.getReferenceNo(), itemColumn.getItemName(),
                String.valueOf(itemColumn.getItemQuantity()), itemColumn.getItemPrice()} ;
        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_ITEM, whereClause, whereArgs);

        long item_id = 0;
        // insert row
        if( numRows <= 0 ) {
            item_id = db.insert(TABLE_ITEM, null, contentValues);
        }
        return item_id;
    }

    public List<Item> getItemByRefno(String refno) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> array_list = new ArrayList<Item>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEM + " WHERE "
                + ITEM_REFERENCE + " = '" +refno+"' ORDER BY "+KEY_CREATED_AT;

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Item item = new Item();

                item.setItemId(res.getInt(res.getColumnIndex(KEY_ID)));
                item.setReferenceNo(res.getString(res.getColumnIndex(ITEM_REFERENCE)));
                item.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
                item.setItemQuantity(res.getInt(res.getColumnIndex(ITEM_QUANTITY)));
                item.setItemPrice(res.getString(res.getColumnIndex(ITEM_PRICE)));
                item.setItemTotalAmount(res.getString(res.getColumnIndex(ITEM_TOTAL_AMOUNT)));
                array_list.add(item);

            }while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * getting item count using reference value
     */
    public int numberOfItemRowsByRefNo(String refNo){
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = ITEM_REFERENCE+" = ? ";
        String[] whereArgs = new String[]{ String.valueOf(refNo)} ;
        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_ITEM, whereClause, whereArgs);
        return numRows;
    }

    // ------------------------ "Item" table methods ----------------//


    // ------------------------ "Offline" table methods ----------------//
    /**
     * inserting key of data whose row of table is inserted in offline mode.
     * @param offline
     * @return id of inserted row
     */
    public long insertOffline(Offline offline) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OFFLINE_TABLE_NAME, offline.getOffline_table_name());
        contentValues.put(OFFLINE_ROW_ID, offline.getOffline_row_id());
        contentValues.put(OFFLINE_ROW_ACTION, offline.getOffline_row_action());
        contentValues.put(KEY_CREATED_AT, offline.getOffline_row_creationTime());

        // insert row
        long offline_id = 0;
        try {
            offline_id = db.insert(TABLE_OFFLINE, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offline_id;
    }

    /**
     * delete data by row id which are inserted into online server
     * @param id
     * @return
     */
    public int deleteOfflineTableData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = OFFLINE_ROW_ID +" = ? ";
        String[] whereArgs = new String[]{ id } ;

        return db.delete(TABLE_OFFLINE, whereClause, whereArgs);
    }

    /**
     * get data with respect to table name
     * @param tableName
     * @return
     */
    public List<Offline> getOfflineDataByTableName(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Offline> array_list = new ArrayList<Offline>();

        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE + " WHERE "+
                                OFFLINE_TABLE_NAME+" = '"+tableName+"' ORDER BY "+KEY_CREATED_AT;

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Offline offline = new Offline();


                offline.setOffline_id(res.getInt(res.getColumnIndex(KEY_ID)));
                offline.setOffline_table_name(res.getString(res.getColumnIndex(OFFLINE_TABLE_NAME)));
                offline.setOffline_row_id(res.getInt(res.getColumnIndex(OFFLINE_ROW_ID)));
                offline.setOffline_row_action(res.getString(res.getColumnIndex(OFFLINE_ROW_ACTION)));

                array_list.add(offline);

            }while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * this function is used for showing uploaded image in enquiry details till the image
     * didn't uploaded on server.
     * @param id
     * @return
     */
    public int numberOfOfflineRowsByid( int id ) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = OFFLINE_ROW_ID +" = ? ";
        String[] whereArgs = new String[]{ String.valueOf(id)} ;

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_OFFLINE, whereClause, whereArgs);
        return numRows;
    }

    /**
     * it check whether data with same row id with update action in offline table.
     * if yes then it count rows
     * function - Edit_Society_Details
     * @param id
     * @return
     */
    public int numberOfOfflineRowsByRowIdAndUpdate( int id ) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = OFFLINE_ROW_ID + " = ? AND " + OFFLINE_ROW_ACTION + " = ? ";
        String[] whereArgs = new String[]{ String.valueOf(id), offlineActionModeEnum.UPDATE.toString() } ;

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_OFFLINE, whereClause, whereArgs);
        return numRows;
    }

    /**
     * it check whether data with same row id with update action in offline table.
     * if yes delete old one and create new entry in offline table.
     * @param id
     * @return
     */
    public int deleteOfflineTableDataByRowIdAndUpdate(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = OFFLINE_ROW_ID + " = ? AND " + OFFLINE_ROW_ACTION + " = ? ";
        String[] whereArgs = new String[]{ id, offlineActionModeEnum.UPDATE.toString() } ;

        return db.delete(TABLE_OFFLINE, whereClause, whereArgs);
    }

    // ------------------------ "Offline" table methods ----------------//


    // ------------------------ "OBP" table methods ----------------//

    public long insertOBPData(OBP obpObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OBP_SERVER_ID, obpObj.getUserID_serverId());
        contentValues.put(OBP_NAME, obpObj.getObp_name());
        contentValues.put(OBP_STORE_NAME, obpObj.getObp_store_name());
        contentValues.put(OBP_EMAIL_ID, obpObj.getObp_email_id());
        contentValues.put(OBP_PASSWORD, obpObj.getObp_email_passowrd());
        contentValues.put(OBP_CONTACT_NO, obpObj.getObp_contact_number());
        contentValues.put(OBP_ADDRESS, obpObj.getObp_address());
        contentValues.put(OBP_PINCODE, obpObj.getObp_pincode());
        contentValues.put(OBP_CITY, obpObj.getObp_city());
        contentValues.put(OBP_STATE, obpObj.getObp_state());
        contentValues.put(OBP_COUNTRY, obpObj.getObp_country());
        contentValues.put(KEY_STATUS, obpObj.getObp_status());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long obp_id = 0;
        try {
            obp_id = db.insert(TABLE_OBP, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obp_id;
    }

    public List<OBP> getOfflineOBPById(int offline_row_id, String offline_row_action) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<OBP> array_list = new ArrayList<OBP>();

        String selectQuery = "SELECT * FROM " + TABLE_OBP + " WHERE "
                + KEY_ID + " = " + offline_row_id ;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        OBP obp = new OBP();
        obp.setObp_id(c.getInt(c.getColumnIndex(KEY_ID)));
        obp.setUserID_serverId(c.getInt(c.getColumnIndex(OBP_SERVER_ID)));
        obp.setObp_name(c.getString(c.getColumnIndex(OBP_NAME)));
        obp.setObp_store_name(c.getString(c.getColumnIndex(OBP_STORE_NAME)));
        obp.setObp_email_id(c.getString(c.getColumnIndex(OBP_EMAIL_ID)));
        obp.setObp_email_passowrd(c.getString(c.getColumnIndex(OBP_PASSWORD)));
        obp.setObp_contact_number(c.getString(c.getColumnIndex(OBP_CONTACT_NO)));
        obp.setObp_address(c.getString(c.getColumnIndex(OBP_ADDRESS)));
        obp.setObp_pincode(c.getInt(c.getColumnIndex(OBP_PINCODE)));
        obp.setObp_city(c.getString(c.getColumnIndex(OBP_CITY)));
        obp.setObp_state(c.getString(c.getColumnIndex(OBP_STATE)));
        obp.setObp_country(c.getString(c.getColumnIndex(OBP_COUNTRY)));
        obp.setObp_status(c.getInt(c.getColumnIndex(KEY_STATUS)));
        obp.setObp_offline_action(offline_row_action);

        array_list.add(obp);

        return array_list;
    }

    /**
     * update server id when offline data inserted on server while internet is on.
     * function - InsertOfflineOBPDataAsyncTask
     * @param localOBPId
     * @param serverId
     * @return
     */
    public boolean updateServerIdOfOBP(String localOBPId, int serverId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OBP_SERVER_ID, serverId);

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_OBP, contentValues, KEY_ID + " = ? ", new String[]{ localOBPId });
        return true;
    }

    /**
     * check if obp email id exist
     */
    public int isOBPPresent(String obp_email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = OBP_EMAIL_ID +" = ? ";
        String[] whereArgs = new String[]{ obp_email } ;

        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_OBP,whereClause,whereArgs);
        return numRows;
    }

    /**
     * check if any obp entry exist
     */
    public int isAnyOBPPresent() {
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_OBP + " WHERE "+KEY_STATUS+" = 1 AND " +
                OBP_SERVER_ID+" <> "+uId+";";

        int numRows = (int) DatabaseUtils.longForQuery(db, selectQuery, null);

        return numRows;
    }

    public List<OBP> getAllOBPExcludeAdmin() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<OBP> array_list = new ArrayList<OBP>();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

//        '<>' = not equal to
        String selectQuery = "SELECT * FROM " + TABLE_OBP + " WHERE "+KEY_STATUS+" = 1 AND " +
                OBP_SERVER_ID +" <> "+uId+";";

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                OBP obpOjject = new OBP();

                obpOjject.setObp_id(res.getInt(res.getColumnIndex(KEY_ID)));
                obpOjject.setUserID_serverId(res.getInt(res.getColumnIndex(OBP_SERVER_ID)));
                obpOjject.setObp_name(res.getString(res.getColumnIndex(OBP_NAME)));
                obpOjject.setObp_store_name(res.getString(res.getColumnIndex(OBP_STORE_NAME)));
                obpOjject.setObp_email_id(res.getString(res.getColumnIndex(OBP_EMAIL_ID)));
                obpOjject.setObp_email_passowrd(res.getString(res.getColumnIndex(OBP_PASSWORD)));
                obpOjject.setObp_contact_number(res.getString(res.getColumnIndex(OBP_CONTACT_NO)));
                obpOjject.setObp_address(res.getString(res.getColumnIndex(OBP_ADDRESS)));
                obpOjject.setObp_pincode(res.getInt(res.getColumnIndex(OBP_PINCODE)));
                obpOjject.setObp_city(res.getString(res.getColumnIndex(OBP_CITY)));
                obpOjject.setObp_state(res.getString(res.getColumnIndex(OBP_STATE)));
                obpOjject.setObp_country(res.getString(res.getColumnIndex(OBP_COUNTRY)));

                array_list.add(obpOjject);

            } while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * Deleting a obp
     */
    public Integer deleteObp (OBP obpObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, "0");

        String whereClause = null ;
        String[] whereArgs;

        // when offline obp row updated
        // then server id will be o
        if ( obpObj.getUserID_serverId() == 0 ) {
            whereClause = KEY_ID + " = ? ";
            whereArgs = new String[]{String.valueOf(obpObj.getObp_id())} ;
        }
        else {
            whereClause = OBP_SERVER_ID + " = ? ";
            whereArgs = new String[]{String.valueOf(obpObj.getUserID_serverId())} ;
        }

        return db.update(TABLE_OBP, contentValues, whereClause, whereArgs);
    }

    public long updateObpData(OBP obpObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OBP_NAME, obpObj.getObp_name());
        contentValues.put(OBP_STORE_NAME, obpObj.getObp_store_name());
        contentValues.put(OBP_EMAIL_ID, obpObj.getObp_email_id());
        contentValues.put(OBP_PASSWORD, obpObj.getObp_email_passowrd());
        contentValues.put(OBP_CONTACT_NO, obpObj.getObp_contact_number());
        contentValues.put(OBP_ADDRESS, obpObj.getObp_address());
        contentValues.put(OBP_PINCODE, obpObj.getObp_pincode());
        contentValues.put(OBP_CITY, obpObj.getObp_city());
        contentValues.put(OBP_STATE, obpObj.getObp_state());
        contentValues.put(OBP_COUNTRY, obpObj.getObp_country());
        contentValues.put(KEY_STATUS, obpObj.getObp_status());

        String whereClause = OBP_SERVER_ID + " = ? ";
        String[] whereArgs = new String[]{ String.valueOf(obpObj.getUserID_serverId()) } ;

        return db.update(TABLE_OBP, contentValues, whereClause, whereArgs);
    }

    public OBP getOBPByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_OBP + " WHERE "+KEY_STATUS+" = 1 AND " +
                OBP_SERVER_ID +" = "+userId+";";

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        OBP obpOjject = new OBP();
        if(res.moveToFirst()) {
            do{

                obpOjject.setObp_id(res.getInt(res.getColumnIndex(KEY_ID)));
                obpOjject.setUserID_serverId(res.getInt(res.getColumnIndex(OBP_SERVER_ID)));
                obpOjject.setObp_name(res.getString(res.getColumnIndex(OBP_NAME)));
                obpOjject.setObp_store_name(res.getString(res.getColumnIndex(OBP_STORE_NAME)));
                obpOjject.setObp_email_id(res.getString(res.getColumnIndex(OBP_EMAIL_ID)));
                obpOjject.setObp_email_passowrd(res.getString(res.getColumnIndex(OBP_PASSWORD)));
                obpOjject.setObp_contact_number(res.getString(res.getColumnIndex(OBP_CONTACT_NO)));
                obpOjject.setObp_address(res.getString(res.getColumnIndex(OBP_ADDRESS)));
                obpOjject.setObp_pincode(res.getInt(res.getColumnIndex(OBP_PINCODE)));
                obpOjject.setObp_city(res.getString(res.getColumnIndex(OBP_CITY)));
                obpOjject.setObp_state(res.getString(res.getColumnIndex(OBP_STATE)));
                obpOjject.setObp_country(res.getString(res.getColumnIndex(OBP_COUNTRY)));
                obpOjject.setObp_status(res.getInt(res.getColumnIndex(KEY_STATUS)));

            } while (res.moveToNext());
        }
        return obpOjject;
    }

    /**
     * except admin user data
     * @return numRows
     */
    public int numberOfObpRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_OBP + " WHERE " +OBP_SERVER_ID+" <> "+uId+";";

        int numRows = (int) DatabaseUtils.longForQuery(db, selectQuery, null);

        return numRows;
    }

    // ------------------------ "OBP" table methods ----------------//


    // ------------------------ "Product Group" table methods ----------------//
    /**
     * getting product group count
     */
    public int numberOfGroupRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_PRODUCT_GROUP);
        return numRows;
    }

    public long insertProductGroup(Product_Group groupObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_GROUP_SERVER_ID, groupObj.getServerId());
        contentValues.put(PRODUCT_GROUP_NAME, groupObj.getGroup_name());
        contentValues.put(KEY_STATUS, groupObj.getGroup_status());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long group_id = 0;
        try {
            group_id = db.insert(TABLE_PRODUCT_GROUP, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return group_id;
    }

    /**
     * getting all Group
     **/
    public ArrayList<Product_Group> getAllGroup() {

        ArrayList<Product_Group> array_list = new ArrayList<Product_Group>();
        String selectQuery = "SELECT * FROM "+TABLE_PRODUCT_GROUP+" WHERE "+KEY_STATUS+" = 1 ORDER BY "+KEY_CREATED_AT;
        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Product_Group groups = new Product_Group();

                groups.setId(res.getInt(res.getColumnIndex(KEY_ID)));
                groups.setGroup_name(res.getString(res.getColumnIndex(PRODUCT_GROUP_NAME)));
                groups.setServerId(res.getInt(res.getColumnIndex(PRODUCT_GROUP_SERVER_ID)));
                array_list.add(groups);
            }while (res.moveToNext());
        }
        return array_list;
    }

    // ------------------------ "Product Group" table methods ----------------//


    // ------------------------ "Product Category" table methods ----------------//
    /**
     * getting category count
     */
    public int numberOfCategoryRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_PRODUCT_CATEGORY);
        return numRows;
    }

    public long insertCategoryGroup(Category categoryObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_GROUP_ID, categoryObj.getProduct_group_id_InCategory());
        contentValues.put(CATEGORY_SERVER_ID, categoryObj.getServerId());
        contentValues.put(CATEGORY_NAME, categoryObj.getCategory_name());
        contentValues.put(CATEGORY_IMAGE, categoryObj.getCategory_image());
        contentValues.put(KEY_STATUS, categoryObj.getCategory_status());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long category_id = 0;
        try {
            category_id = db.insert(TABLE_PRODUCT_CATEGORY, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category_id;
    }

    /**
     * getting all Category filter by group id
     *
     * @param groupId*/
    public ArrayList<Category> getAllCategory(int groupId) {

        ArrayList<Category> array_list = new ArrayList<Category>();
        String selectQuery = "SELECT * FROM "+TABLE_PRODUCT_CATEGORY+" WHERE "+KEY_STATUS+" = 1 AND "
                            +PRODUCT_GROUP_ID+" = "+groupId +" ORDER BY "+KEY_CREATED_AT;
        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Category category = new Category();

                category.setId(res.getInt(res.getColumnIndex(KEY_ID)));
                category.setProduct_group_id_InCategory(res.getInt(res.getColumnIndex(PRODUCT_GROUP_ID)));
                category.setServerId(res.getInt(res.getColumnIndex(CATEGORY_SERVER_ID)));
                category.setCategory_name(res.getString(res.getColumnIndex(CATEGORY_NAME)));
                category.setCategory_image(res.getString(res.getColumnIndex(CATEGORY_IMAGE)));
                array_list.add(category);

            }while (res.moveToNext());
        }
        return array_list;
    }

    // ------------------------ "Product Category" table methods ----------------//


    // ------------------------ "Product Subcategory" table methods ----------------//
    /**
     * getting Subcategory count
     */
    public int numberOfSubcategoryRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_PRODUCT_SUBCATEGORY);
        return numRows;
    }

    public long insertSubcategoryGroup(Subcategory subcategoryObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBCATEGORY_SERVER_ID, subcategoryObj.getServerId());
        contentValues.put(CATEGORY_ID, subcategoryObj.getCategory_id());
        contentValues.put(PRODUCT_GROUP_ID, subcategoryObj.getProduct_group_id_InSubcategory());
        contentValues.put(SUB_CATEGORY_NAME, subcategoryObj.getSubcategory_name());
        contentValues.put(SUB_CATEGORY_IAMGE, subcategoryObj.getSubcategory_image());
        contentValues.put(KEY_STATUS, subcategoryObj.getSubcategory_status());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long subcategory_id = 0;
        try {
            subcategory_id = db.insert(TABLE_PRODUCT_SUBCATEGORY, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subcategory_id;
    }

    /**
     * getting all SubCategory filter by group id and category id
     *
     * @param groupId
     * @param categoryId
     * @return
     */

    public ArrayList<Subcategory> getAllSubcategory(int groupId, int categoryId) {

        ArrayList<Subcategory> array_list = new ArrayList<Subcategory>();
        String selectQuery = "SELECT * FROM "+TABLE_PRODUCT_SUBCATEGORY+" WHERE "+KEY_STATUS+" = 1 AND "+
                PRODUCT_GROUP_ID+" = "+groupId+" AND "+
                CATEGORY_ID+" = "+categoryId+" ORDER BY "+KEY_CREATED_AT;
        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Subcategory subcategory = new Subcategory();

                subcategory.setId(res.getInt(res.getColumnIndex(KEY_ID)));
                subcategory.setServerId(res.getInt(res.getColumnIndex(SUBCATEGORY_SERVER_ID)));
                subcategory.setCategory_id(res.getInt(res.getColumnIndex(CATEGORY_ID)));
                subcategory.setProduct_group_id_InSubcategory(res.getInt(res.getColumnIndex(PRODUCT_GROUP_ID)));
                subcategory.setSubcategory_name(res.getString(res.getColumnIndex(SUB_CATEGORY_NAME)));
                subcategory.setSubcategory_image(res.getString(res.getColumnIndex(SUB_CATEGORY_IAMGE)));
                array_list.add(subcategory);

            } while (res.moveToNext());
        }
        return array_list;
    }

    // ------------------------ "Product Subcategory" table methods ----------------//

    // ------------------------ "Product Details" table methods ----------------//
    /**
     * getting product details count
     */
    public int numberOfProductdetailsRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_PRODUCT_DETAILS);
        return numRows;
    }

    public long insertProductDetailsGroup(Product_Details productDetailsObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(PRODUCT_DETAILS_SERVER_ID, productDetailsObj.getServerId());
        contentValues.put(SUB_CATEGORY_ID, productDetailsObj.getSubcategory_id_InProductDetails());
        contentValues.put(CATEGORY_ID, productDetailsObj.getCategory_id_InProductDetails());
        contentValues.put(PRODUCT_GROUP_ID, productDetailsObj.getProduct_group_id_InProductDetails());
        contentValues.put(PRODUCT_NAME, productDetailsObj.getProduct_name());
        contentValues.put(PRODUCT_PATH, productDetailsObj.getProd_path());
        contentValues.put(PRODUCT_PATH1, productDetailsObj.getProd_path1());
        contentValues.put(PRODUCT_PATH2, productDetailsObj.getProd_path2());
        contentValues.put(PRODUCT_PATH3, productDetailsObj.getProd_path3());
        contentValues.put(PRODUCT_PATH4, productDetailsObj.getProd_path4());
        contentValues.put(PRODUCT_DESCRIPTION, productDetailsObj.getProd_description());
        contentValues.put(PRODUCT_DISCOUNT_TYPE, productDetailsObj.getDiscount_type());
        contentValues.put(KEY_STATUS, productDetailsObj.getProduct_status());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long product_id = 0;
        try {
            product_id = db.insert(TABLE_PRODUCT_DETAILS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product_id;
    }

    /**
     * getting all product details filter by group id and category id and subcategory id
     *
     * @param groupId
     * @param categoryId
     * @param subcategoryId*/
    public ArrayList<Product_Details> getAllProductdetails(int groupId, int categoryId, int subcategoryId) {

        ArrayList<Product_Details> array_list = new ArrayList<Product_Details>();
        String selectQuery = "SELECT * FROM "+TABLE_PRODUCT_DETAILS+" WHERE "+KEY_STATUS+" = 1 AND "+
                PRODUCT_GROUP_ID +" = "+groupId+" AND "+
                CATEGORY_ID +" = "+categoryId+" AND "+
                SUB_CATEGORY_ID+" = "+subcategoryId+" ORDER BY "+KEY_CREATED_AT;
        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Product_Details product_details = new Product_Details();

                product_details.setId(res.getInt(res.getColumnIndex(KEY_ID)));
                product_details.setServerId(res.getInt(res.getColumnIndex(PRODUCT_DETAILS_SERVER_ID)));
                product_details.setSubcategory_id_InProductDetails(res.getInt(res.getColumnIndex(SUB_CATEGORY_ID)));
                product_details.setCategory_id_InProductDetails(res.getInt(res.getColumnIndex(CATEGORY_ID)));
                product_details.setProduct_group_id_InProductDetails(res.getInt(res.getColumnIndex(PRODUCT_GROUP_ID)));
                product_details.setProduct_name(res.getString(res.getColumnIndex(PRODUCT_NAME)));
                product_details.setProd_path(res.getString(res.getColumnIndex(PRODUCT_PATH)));
                product_details.setProd_description(res.getString(res.getColumnIndex(PRODUCT_DESCRIPTION)));
                product_details.setDiscount_type(res.getString(res.getColumnIndex(PRODUCT_DISCOUNT_TYPE)));
                array_list.add(product_details);

            } while (res.moveToNext());
        }
        return array_list;
    }

    // ------------------------ "Product Details" table methods ----------------//


    // ------------------------ "Vendor" table methods ----------------//

    /**
     * check if any vendor entry exist
     */
    public int isAnyVendorPresent() {
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_VENDORS + " WHERE "+KEY_STATUS+" = 1;";

        int numRows = (int) DatabaseUtils.longForQuery(db, selectQuery, null);

        return numRows;
    }

    public List<Vendor> getAllVendors() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Vendor> array_list = new ArrayList<Vendor>();

        String selectQuery = "SELECT * FROM " + TABLE_VENDORS + " WHERE "+KEY_STATUS+" = 1 ;";

        Log.d(LOG, selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                Vendor vendorObject = new Vendor();

                vendorObject.setId(res.getInt(res.getColumnIndex(KEY_ID)));
                vendorObject.setServerId(res.getInt(res.getColumnIndex(VENDOR_SERVER_ID)));
                vendorObject.setVendor_name(res.getString(res.getColumnIndex(VENDOR_NAME)));
                vendorObject.setVendor_contact(res.getString(res.getColumnIndex(VENDOR_CONTACT)));
                vendorObject.setVendor_Address(res.getString(res.getColumnIndex(VENDOR_ADDRESS)));
                vendorObject.setVendor_email(res.getString(res.getColumnIndex(VENDOR_EMAIL)));
                vendorObject.setVendor_status(res.getInt(res.getColumnIndex(KEY_STATUS)));

                array_list.add(vendorObject);

            } while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * check if vendor is exist
     * @param str_vendor_email
     * @return numRows
     */
    public int isVendorPresent(String str_vendor_email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = VENDOR_EMAIL + " = ? ";
        String[] whereArgs = new String[]{ str_vendor_email } ;

        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_VENDORS,whereClause,whereArgs);
        return numRows;
    }

    public long insertVendorData(Vendor vendorObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(VENDOR_SERVER_ID, vendorObj.getServerId());
        contentValues.put(VENDOR_NAME, vendorObj.getVendor_name());
        contentValues.put(VENDOR_CONTACT, vendorObj.getVendor_contact());
        contentValues.put(VENDOR_ADDRESS, vendorObj.getVendor_Address());
        contentValues.put(VENDOR_EMAIL, vendorObj.getVendor_email());
        contentValues.put(KEY_STATUS, vendorObj.getVendor_status());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long vendor_id = 0;
        try {
            vendor_id = db.insert(TABLE_VENDORS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendor_id;
    }

    public List<Vendor> getOfflineVendorById(int offline_row_id, String offline_row_action) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Vendor> array_list = new ArrayList<Vendor>();

        String selectQuery = "SELECT * FROM " + TABLE_VENDORS + " WHERE "
                + KEY_ID + " = " + offline_row_id ;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Vendor vendor = new Vendor();

        vendor.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        vendor.setServerId(c.getInt(c.getColumnIndex(VENDOR_SERVER_ID)));
        vendor.setVendor_name(c.getString(c.getColumnIndex(VENDOR_NAME)));
        vendor.setVendor_contact(c.getString(c.getColumnIndex(VENDOR_CONTACT)));
        vendor.setVendor_Address(c.getString(c.getColumnIndex(VENDOR_ADDRESS)));
        vendor.setVendor_email(c.getString(c.getColumnIndex(VENDOR_EMAIL)));
        vendor.setCreted_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        vendor.setVendor_status(c.getInt(c.getColumnIndex(KEY_STATUS)));
        vendor.setVendor_offline_action(offline_row_action);

        array_list.add(vendor);

        return array_list;
    }

    /**
     * update server id when offline data inserted on server while internet is on.
     * function - InsertOfflineVendorDataAsyncTask
     * @param localVendorId
     * @param serverId
     * @return
     */
    public boolean updateServerIdOfVendor(String localVendorId, int serverId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(VENDOR_SERVER_ID, serverId);

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_VENDORS, contentValues, KEY_ID + " = ? ", new String[]{ localVendorId });
        return true;
    }


    /**
     * Deleting a vendor
     */
    public Integer deleteVendor (Vendor vendorObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, "0");

        String whereClause = null ;
        String[] whereArgs;

        // when offline vendor row updated
        // then server id will be o
        whereClause = KEY_ID + " = ? ";
        whereArgs = new String[]{String.valueOf(vendorObj.getId())} ;

        return db.update(TABLE_VENDORS, contentValues, whereClause, whereArgs);
    }

    /**
     * Updating a Vendors
     */
    public boolean updateVendor(Vendor vendor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(VENDOR_NAME, vendor.getVendor_name());
        contentValues.put(VENDOR_CONTACT, vendor.getVendor_contact());
        contentValues.put(VENDOR_EMAIL, vendor.getVendor_email());
        contentValues.put(VENDOR_ADDRESS, vendor.getVendor_Address());
        contentValues.put(KEY_STATUS, vendor.getVendor_status());

        String whereClause = KEY_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(vendor.getId())} ;

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_VENDORS, contentValues, whereClause, whereArgs);
        return true;
    }

    // ------------------------ "Vendor" table methods ----------------//

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    /**
     * get datetime
     * */
    private String getDateTime() {
        Date date = new Date();
        return ""+date.getTime() ;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}

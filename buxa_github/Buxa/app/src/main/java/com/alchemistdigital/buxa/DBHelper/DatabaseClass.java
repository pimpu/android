package com.alchemistdigital.buxa.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alchemistdigital.buxa.model.CFSAddressModel;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.CustomClearanceCategoryModel;
import com.alchemistdigital.buxa.model.CustomClearanceLocation;
import com.alchemistdigital.buxa.model.CustomClearanceModel;
import com.alchemistdigital.buxa.model.FreightForwardingModel;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.model.ShipmentConformationModel;
import com.alchemistdigital.buxa.model.ShipmentTypeModel;
import com.alchemistdigital.buxa.model.TransportServiceModel;
import com.alchemistdigital.buxa.model.TransportTypeModel;
import com.alchemistdigital.buxa.model.TransportationModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 8/26/2016.
 */
public class DatabaseClass extends SQLiteOpenHelper {
    private int mOpenCounter;
    private DatabaseClass mDatabase;

    private Context context;

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Buxa.db";
    // Table Names
    public static final String TABLE_COMPANY = "CompanyDetails";
    public static final String TABLE_COMMODITY = "Commodity";
    public static final String TABLE_CUSTOM_CLEARANCE_LOCATION = "CustomClearanceLocation";
    public static final String TABLE_CUSTOM_CLEARANCE_CATEGORY = "CustomClearanceCategory";
    public static final String TABLE_TYPE_OF_SHIPMENT = "TypeOfShipment";
    public static final String TABLE_TRANSPORTATION = "Transportation";
    public static final String TABLE_CUSTOM_CLEARANCE = "CustomClearance";
    public static final String TABLE_FREIGHT_FORWARDING = "FreightForwarding";
    public static final String TABLE_SHIPMENT_CONFORMATION = "ShipmentConformation";
    public static final String TABLE_TRANSPORT_TYPE = "TransportType";
    public static final String TABLE_TRANSPORT_SERVICE = "TransportService";
    public static final String TABLE_TYPE_OF_PACKAGE = "TypeOfPackage";
    public static final String TABLE_CFS_ADDRESS = "CfsAddress";
    public static final String TABLE_INTERNATIONAL_DESTINATION_PORT = "InterDestiPorts";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_STATUS = "status";

    // TABLE_COMPANY Table - column names
    private static final String COMPANY_SERVER_ID = "company_serverId";
    private static final String USER_REFERENCE_NO = "company_reference_no";
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

    // TABLE_TYPE_OF_SHIPMENT Table - column names
    private static final String TOS_SERVER_ID = "tos_serverId";
    private static final String TOS_name = "tos_name";

    // TABLE_SHIPMENT_CONFORMATION Table - column names
    private static final String SHIPMENT_CONFORMATION_BOOKING_ID = "booking_id";
    private static final String SHIPMENT_CONFORMATION_ENQUIRY_STATUS = "enquiry_status";
    private static final String SHIPMENT_CONFORMATION_QUOTATION = "enquiry_quotation";
    private static final String RATES = "rates";
    private static final String SHIPMENT_CONFORMATION_IS_TRANS_SERVICE = "is_trans";
    private static final String SHIPMENT_CONFORMATION_IS_CC_SERVICE = "is_customclr";
    private static final String SHIPMENT_CONFORMATION_IS_FF_SERVICE = "is_freightfrd";

    // TABLE_TRANSPORTATION Table - column names
    private static final String  TRANSPORTATION_SERVER_ID = "trans_serverId";
    private static final String  BOOKING_ID = "booking_id";
    private static final String  PICKUP = "pickup";
    private static final String  DROP = "dropLocation";
    private static final String  SHIPMENT_TYPE = "shipment_type";
    private static final String  MEASUREMENT = "measurement";
    private static final String  GROSS_WEIGHT = "gross_weight";
    private static final String  PACK_TYPE = "pack_type";
    private static final String  NO_OF_PACK = "no_of_pack";
    private static final String  COMMODITY_SERVERID_IN_TRANSPORT = "commodity_serverId";
    private static final String  DIMEN_LENGTH = "dimen_length";
    private static final String  DIMEN_HEIGHT = "dimen_height";
    private static final String  DIMEN_WIDTH = "dimen_width";
    private static final String  AVAIL_OPTION = "avail_option";

    // TABLE_CUSTOM_CLEARANCE Table - column names
    private static final String CUSTOM_CLEARANCE_SERVER_ID = "CC_ServerId";
    // booking id
    private static final String CUSTOM_CLEARANCE_TYPE = "CC_type";
    private static final String CUSTOM_CLEARANCE_COMMODITY = "CC_commodity";
    private static final String CUSTOM_CLEARANCE_GROSS_WEIGHT = "CC_grossWeight";
    private static final String HARMONIZED_SYSTEM_CODE = "harmonized_system_code";
    // shipment type
    // avail option
    private static final String STUFFING_TYPE = "stuffing_type";
    private static final String STUFFING_ADDRESS = "stuffing_address";

    // TABLE_FREIGHT_FORWARDING_SERVICE Table - column names
    private static final String FREIGHT_FORWARDING_SERVER_ID = "FF_ServerId";
    // booking id
    // avail option
    private static final String PORT_OF_LOADING = "port_of_loading";
    private static final String PORT_OF_COUNTRY = "port_of_country";
    private static final String PORT_OF_DESTINATION = "port_of_destination";
    private static final String INCOTERM = "incoterm";
    private static final String DESTIANTION_DELIVERY_ADDRESS = "destination_delivery_address";
    private static final String FREIGHT_FORWARDING_SHIPMENT = "shipment_type";
    private static final String FREIGHT_FORWARDING_MEASURMETN = "measurment";
    private static final String FREIGHT_FORWARDING_GROSS_WEIGHT = "gross_weight";
    private static final String FREIGHT_FORWARDING_PACK_TYPE = "type_of_pack";
    private static final String FREIGHT_FORWARDING_NO_OF_PACK = "no_of_pack";
    private static final String FREIGHT_FORWARDING_COMMODITY = "commodity";


    // TABLE_CFS_ADDRESS Table - column names
    private static final String CFS_ADDRESS_SERVER_ID = "cfs_address_serverId";
    private static final String CFS_ADDRESS_NAME = "cfs_address_name";

    // TABLE_TRANSPORT_SERVICE Table - column names
    private static final String TRANSPORT_SERVICE_SERVER_ID = "trans_service_serverId";
    private static final String TRANSPORT_SERVICE_NAME = "trans_service_name";

    // TABLE_TYPE_OF_PACKAGE Table - column names
    private static final String PACKAGE_TYPE_SERVER_ID = "package_type_serverId";
    private static final String PACKAGE_TYPE_NAME = "package_type_name";

    // TABLE_TRANSPORT_TYPE Table - column names
    private static final String TRANSPORT_TYPE_SERVER_ID = "trans_type_serverId";
    private static final String TRANSPORT_TYPE_NAME = "trans_type_name";

    // TABLE_INTERNATIONAL_DESTIANTION_PORT Table - column names
    private static final String INTER_DESTI_PORT_NAME = "inter_desti_ports_name";
    private static final String INTER_DESTI_PORT_COUNTRY = "inter_desti_ports_country";

    // Company Table Create Statements
    private static final String CREATE_TABLE_COMPANY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_COMPANY +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COMPANY_SERVER_ID+" INTEGER," +
                    USER_REFERENCE_NO+" INTEGER," +
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
    private static final String CREATE_TABLE_TYPE_OF_SHIPMENT =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TYPE_OF_SHIPMENT +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TOS_SERVER_ID +" INTEGER," +
                    TOS_name+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Transportation Table Create Statements
    private static final String CREATE_TABLE_TRANSPORTATION =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSPORTATION +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TRANSPORTATION_SERVER_ID +" INTEGER," +
                    BOOKING_ID +" VARCHAR(50)," +
                    PICKUP +" VARCHAR(200)," +
                    DROP +" VARCHAR(200)," +
                    SHIPMENT_TYPE +" INTEGER," +
                    MEASUREMENT +" VARCHAR(10)," +
                    GROSS_WEIGHT +" REAL," +
                    PACK_TYPE +" INTEGER," +
                    NO_OF_PACK +" INTEGER," +
                    COMMODITY_SERVERID_IN_TRANSPORT +" INTEGER," +
                    DIMEN_LENGTH +" INTEGER," +
                    DIMEN_HEIGHT +" INTEGER," +
                    DIMEN_WIDTH +" INTEGER," +
                    AVAIL_OPTION +" TINYINT(4)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Custom Clearance Table Create Statements
    private static final String CREATE_TABLE_CUSTOM_CLEARANCE =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_CUSTOM_CLEARANCE+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CUSTOM_CLEARANCE_SERVER_ID +" INTEGER," +
                    BOOKING_ID +" VARCHAR(50)," +
                    CUSTOM_CLEARANCE_TYPE + " VARCHAR(10),"+
                    CUSTOM_CLEARANCE_COMMODITY + " INTEGER,"+
                    CUSTOM_CLEARANCE_GROSS_WEIGHT + " REAL,"+
                    HARMONIZED_SYSTEM_CODE + " INTEGER,"+
                    SHIPMENT_TYPE +" INTEGER," +
                    STUFFING_TYPE + "  VARCHAR(50),"+
                    STUFFING_ADDRESS + "  VARCHAR(50),"+
                    AVAIL_OPTION +" TINYINT(4)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";


    // Freight forwarding Table Create Statement
    private static final String CREATE_TABLE_FREIGHT_FORWARDING =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_FREIGHT_FORWARDING+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FREIGHT_FORWARDING_SERVER_ID +" INTEGER," +
                    BOOKING_ID +" VARCHAR(50)," +
                    PORT_OF_LOADING + " VARCHAR(100),"+
                    PORT_OF_COUNTRY + " VARCHAR(100),"+
                    PORT_OF_DESTINATION + " VARCHAR(100),"+
                    INCOTERM + " VARCHAR(50),"+
                    DESTIANTION_DELIVERY_ADDRESS + " VARCHAR(100),"+
                    FREIGHT_FORWARDING_SHIPMENT +" INTEGER," +
                    FREIGHT_FORWARDING_MEASURMETN + " VARCHAR(10),"+
                    FREIGHT_FORWARDING_GROSS_WEIGHT + " REAL,"+
                    FREIGHT_FORWARDING_PACK_TYPE +" INTEGER," +
                    FREIGHT_FORWARDING_NO_OF_PACK +" INTEGER," +
                    FREIGHT_FORWARDING_COMMODITY +" INTEGER," +
                    AVAIL_OPTION +" TINYINT(4)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Shipment conformation Table Create Statements
    private static final String CREATE_TABLE_SHIPMENT_CONFORMATION =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_SHIPMENT_CONFORMATION+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SHIPMENT_CONFORMATION_BOOKING_ID+" VARCHAR(200)," +
                    SHIPMENT_CONFORMATION_ENQUIRY_STATUS +" TINYINT(4) DEFAULT 1," +
                    SHIPMENT_CONFORMATION_QUOTATION+" VARCHAR(100)," +
                    RATES+" INTEGER," +
                    SHIPMENT_CONFORMATION_IS_TRANS_SERVICE+" TINYINT(4) DEFAULT 0," +
                    SHIPMENT_CONFORMATION_IS_CC_SERVICE+" TINYINT(4) DEFAULT 0," +
                    SHIPMENT_CONFORMATION_IS_FF_SERVICE+" TINYINT(4) DEFAULT 0," +
                    KEY_STATUS +" TINYINT(4) DEFAULT 1," +
                    KEY_CREATED_AT + " DATETIME" + ")";

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

    // Package type Table Create Statements
    private static final String CREATE_TABLE_PACKAGE_TYPE =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TYPE_OF_PACKAGE +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PACKAGE_TYPE_SERVER_ID+" INTEGER," +
                    PACKAGE_TYPE_NAME+" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Container freight station(cfs) Table Create Statements
    private static final String CREATE_TABLE_CFS_ADDRESS =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_CFS_ADDRESS +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CFS_ADDRESS_SERVER_ID + " INTEGER," +
                    CFS_ADDRESS_NAME +" VARCHAR(200)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // international destination port Table Create Statements
    private static final String CREATE_TABLE_INTERNATIONAL_DESTINATION_PORT =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_INTERNATIONAL_DESTINATION_PORT +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    INTER_DESTI_PORT_COUNTRY +" VARCHAR(50)," +
                    INTER_DESTI_PORT_NAME +" VARCHAR(50)," +
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
        db.execSQL(CREATE_TABLE_TYPE_OF_SHIPMENT);
        db.execSQL(CREATE_TABLE_TRANSPORTATION);
        db.execSQL(CREATE_TABLE_TRANSPORT_TYPE);
        db.execSQL(CREATE_TABLE_TRANSPORT_SERVICE);
        db.execSQL(CREATE_TABLE_CUSTOM_CLEARANCE);
        db.execSQL(CREATE_TABLE_FREIGHT_FORWARDING);
        db.execSQL(CREATE_TABLE_SHIPMENT_CONFORMATION);
        db.execSQL(CREATE_TABLE_PACKAGE_TYPE);
        db.execSQL(CREATE_TABLE_CFS_ADDRESS);
        db.execSQL(CREATE_TABLE_INTERNATIONAL_DESTINATION_PORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMODITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_CLEARANCE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_CLEARANCE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE_OF_SHIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORTATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORT_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORT_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_CLEARANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FREIGHT_FORWARDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPMENT_CONFORMATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE_OF_PACKAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CFS_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNATIONAL_DESTINATION_PORT);

        // create new tables
        onCreate(db);
    }

    public synchronized DatabaseClass openDatabase() {
        mOpenCounter++;

        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = this;
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();

        }
    }

    // closing database
    public void closeDB() {
        /*SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();*/

        closeDatabase();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        Date date = new Date();
        return ""+date.getTime() ;
    }

    /**
     *
     * @param Query
     * @return
     */
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

    // ------------------------ "Commodity" table methods ----------------//

    public long insertCommodity(CommodityModel commodityModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COMMODITY_SERVER_ID, commodityModel.getServerId());
        contentValues.put(COMMODITY_NAME, commodityModel.getName());
        contentValues.put(KEY_STATUS, commodityModel.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long commodity_id = 0;
        try {

            String whereClause = COMMODITY_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( commodityModel.getServerId() )} ;

            int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_COMMODITY,whereClause,whereArgs);
            if( numRows <= 0 ) {
                commodity_id = db.insert(TABLE_COMMODITY, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return commodity_id;
    }

    /**
     * getting commodity count
     */
    public int numberOfComodityRows(){
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_COMMODITY);
        return numRows;

    }

    /**
     * get all commodity to show on service parameter activity
     */
    public List<CommodityModel> getCommodityData() {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        List<CommodityModel> array_list = new ArrayList<CommodityModel>();

        String selectQuery = "SELECT * FROM " + TABLE_COMMODITY + " WHERE "
                + KEY_STATUS + " = 1; ";

        Log.d("getCommodityData: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                CommodityModel commodity = new CommodityModel();

                commodity.setServerId(res.getInt(res.getColumnIndex(COMMODITY_SERVER_ID)));
                commodity.setName(res.getString(res.getColumnIndex(COMMODITY_NAME)));

                array_list.add(commodity);

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return array_list;
    }

    public int getCommodityServerID(String name) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_COMMODITY + " WHERE "
                + KEY_STATUS + " = 1 AND "+ COMMODITY_NAME+" = '"+ name +"'; ";

        System.out.println("getCommodityServerID: "+selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        int serverId = 0;
        if(res.moveToFirst()) {
            do{
                serverId = res.getInt(res.getColumnIndex(COMMODITY_SERVER_ID));
            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return serverId;
    }

    public String getCommodityName(int serverId) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_COMMODITY + " WHERE "
                + KEY_STATUS + " = 1 AND "+ COMMODITY_SERVER_ID+" = '"+ serverId +"'; ";

        System.out.println("getCommodityName: "+selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        String name = null;
        if(res.moveToFirst()) {
            do{
                name = res.getString(res.getColumnIndex(COMMODITY_NAME));
            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return name;
    }

    // ------------------------ "CustomClearanceLocation" table methods ----------------//

    public long insertCustomLoaction(CustomClearanceLocation customClearanceLocation) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CCL_SERVER_ID, customClearanceLocation.getServerId());
        contentValues.put(CC_CATEGORY_ID, customClearanceLocation.getCcCategoryId());
        contentValues.put(CCL_NAME, customClearanceLocation.getName());
        contentValues.put(CCL_LOCATION, customClearanceLocation.getLocation());
        contentValues.put(CCL_STATE, customClearanceLocation.getState());
        contentValues.put(KEY_STATUS, customClearanceLocation.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = 0;
        try {

            String whereClause = CCL_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( customClearanceLocation.getServerId() )} ;

            int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_CUSTOM_CLEARANCE_LOCATION,whereClause,whereArgs);
            if( numRows <= 0 ) {
                id = db.insert(TABLE_CUSTOM_CLEARANCE_LOCATION, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * get all custom clearance location to show on service parameter activity
     */
    public List<CustomClearanceLocation> getCustomClearanceLocationData() {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        List<CustomClearanceLocation> array_list = new ArrayList<CustomClearanceLocation>();

        String selectQuery = "SELECT * FROM " + TABLE_CUSTOM_CLEARANCE_LOCATION + " WHERE "
                + KEY_STATUS + " = 1; ";

        Log.d("getCommodityData: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                CustomClearanceLocation CCL = new CustomClearanceLocation();

                CCL.setServerId(res.getInt(res.getColumnIndex(CCL_SERVER_ID)));
                CCL.setCcCategoryId(res.getInt(res.getColumnIndex(CC_CATEGORY_ID)));
                CCL.setName(res.getString(res.getColumnIndex(CCL_NAME)));
                CCL.setLocation(res.getString(res.getColumnIndex(CCL_LOCATION)));
                CCL.setState(res.getString(res.getColumnIndex(CCL_STATE)));

                array_list.add(CCL);

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return array_list;
    }

    // ------------------------ "CustomClearanceCategory" table methods ----------------//
    public long insertCustomClearanceCategory(CustomClearanceCategoryModel customClearanceCategoryModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CCC_SERVER_ID, customClearanceCategoryModel.getServerId());
        contentValues.put(CCC_NAME, customClearanceCategoryModel.getName());
        contentValues.put(KEY_STATUS, customClearanceCategoryModel.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = 0;
        try {

            String whereClause = CCC_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( customClearanceCategoryModel.getServerId() )} ;

            int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_CUSTOM_CLEARANCE_CATEGORY,whereClause,whereArgs);
            if( numRows <= 0 ) {
                id = db.insert(TABLE_CUSTOM_CLEARANCE_CATEGORY, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    // ------------------------ "TypeOfShipment" table methods ----------------//
    public long insertShipmentType(ShipmentTypeModel shipmentTypeModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TOS_SERVER_ID, shipmentTypeModel.getServerId());
        contentValues.put(TOS_name, shipmentTypeModel.getName());
        contentValues.put(KEY_STATUS, shipmentTypeModel.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = 0;
        try {

            String whereClause = TOS_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( shipmentTypeModel.getServerId() )} ;

            int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_TYPE_OF_SHIPMENT,whereClause,whereArgs);
            if( numRows <= 0 ) {
                id = db.insert(TABLE_TYPE_OF_SHIPMENT, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getServerIdByName(String serviceName) {
        String name = null;
        if( serviceName.equals("LCL") ) {
            name = "Less than Container Load";
        }
        else {
            name = "Full Container Load";
        }

        DatabaseClass sqLiteDatabase = openDatabase();
        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TYPE_OF_SHIPMENT + " WHERE "
                + KEY_STATUS + " = 1 AND "+TOS_name+" = '"+name + "';";

//        Log.d("getTransportService: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        int anInt = 0;
        if(res.moveToFirst()) {
            do{
                anInt = res.getInt(res.getColumnIndex(TOS_SERVER_ID));
            }while (res.moveToNext());
        }

        sqLiteDatabase.closeDatabase();

        return anInt;
    }

    public String getShipmentNameByServerId(int serverId) {
        String name = null;

        DatabaseClass sqLiteDatabase = openDatabase();
        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TYPE_OF_SHIPMENT + " WHERE "
                + KEY_STATUS + " = 1 AND "+TOS_SERVER_ID+" = "+serverId + ";";

//        Log.d("getTransportService: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                name = res.getString(res.getColumnIndex(TOS_name));
            }while (res.moveToNext());
        }

        if( name.equals("Less than Container Load")  ) {
            name ="LCL";
        }
        else {
            name = "FCL";
        }

        sqLiteDatabase.closeDatabase();

        return name;
    }

    // ------------------------ "TransportType" table methods ----------------//
    public long insertTransportType(TransportTypeModel transportTypeModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSPORT_TYPE_SERVER_ID, transportTypeModel.getServerId());
        contentValues.put(TRANSPORT_TYPE_NAME, transportTypeModel.getName());
        contentValues.put(KEY_STATUS, transportTypeModel.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = 0;
        try {

            String whereClause = TRANSPORT_TYPE_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( transportTypeModel.getServerId() )} ;

            int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_TRANSPORT_TYPE,whereClause,whereArgs);
            if( numRows <= 0 ) {
                id = db.insert(TABLE_TRANSPORT_TYPE, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    // ------------------------ "TransportService" table methods ----------------//

    public long insertTransportService(TransportServiceModel transportServiceModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSPORT_SERVICE_SERVER_ID, transportServiceModel.getServerId());
        contentValues.put(TRANSPORT_SERVICE_NAME, transportServiceModel.getName());
        contentValues.put(KEY_STATUS, transportServiceModel.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = 0;
        try {

            String whereClause = TRANSPORT_SERVICE_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( transportServiceModel.getServerId() )} ;

            int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_TRANSPORT_SERVICE,whereClause,whereArgs);
            if( numRows <= 0 ) {
                id = db.insert(TABLE_TRANSPORT_SERVICE, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * get transport service to show on service activity
     */
    public List<TransportServiceModel> getTransportServiceData() {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        List<TransportServiceModel> array_list = new ArrayList<TransportServiceModel>();

        String selectQuery = "SELECT * FROM " + TABLE_TRANSPORT_SERVICE + " WHERE "
                + KEY_STATUS + " = 1; ";

        Log.d("getTransportService: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                TransportServiceModel service = new TransportServiceModel();

                service.setServerId(res.getInt(res.getColumnIndex(TRANSPORT_SERVICE_SERVER_ID)));
                service.setName(res.getString(res.getColumnIndex(TRANSPORT_SERVICE_NAME)));

                array_list.add(service);

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return array_list;
    }

    public int getTransportServiceServerId(String serviceName) {
        DatabaseClass sqLiteDatabase = openDatabase();
        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TRANSPORT_SERVICE + " WHERE "
                + KEY_STATUS + " = 1 AND "+TRANSPORT_SERVICE_NAME+" = '"+serviceName + "';";

        Log.d("getTransportService: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        int anInt = 0;
        if(res.moveToFirst()) {
            do{
                TransportServiceModel service = new TransportServiceModel();

                anInt = res.getInt(res.getColumnIndex(TRANSPORT_SERVICE_SERVER_ID));

            }while (res.moveToNext());
        }

        sqLiteDatabase.closeDatabase();

        return anInt;
    }

    // ------------------------ "TypeOfPackage" table methods ----------------//

    public long insertPackageType(PackageTypeModel packageTypeModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PACKAGE_TYPE_SERVER_ID, packageTypeModel.getServerId());
        contentValues.put(PACKAGE_TYPE_NAME, packageTypeModel.getName());
        contentValues.put(KEY_STATUS, packageTypeModel.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = 0;
        try {

            String whereClause = PACKAGE_TYPE_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( packageTypeModel.getServerId() )} ;

            int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_TYPE_OF_PACKAGE, whereClause, whereArgs);
            if( numRows <= 0 ) {
                id = db.insert(TABLE_TYPE_OF_PACKAGE, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<PackageTypeModel> getPackagingTypeData() {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        List<PackageTypeModel> array_list = new ArrayList<PackageTypeModel>();

        String selectQuery = "SELECT * FROM " + TABLE_TYPE_OF_PACKAGE + " WHERE "
                + KEY_STATUS + " = 1; ";

        Log.d("getPackagingType: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                PackageTypeModel types = new PackageTypeModel();

                types.setServerId(res.getInt(res.getColumnIndex(PACKAGE_TYPE_SERVER_ID)));
                types.setName(res.getString(res.getColumnIndex(PACKAGE_TYPE_NAME)));

                array_list.add(types);

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return array_list;
    }

    public int getServerIdByPackagingType(String strPackName) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TYPE_OF_PACKAGE + " WHERE "
                + KEY_STATUS + " = 1 AND "+PACKAGE_TYPE_NAME+" = '"+ strPackName +"'; ";

        System.out.println("getServerIdByPackagingType: "+selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        int name = 0;
        if(res.moveToFirst()) {
            do{
                name = res.getInt(res.getColumnIndex(PACKAGE_TYPE_SERVER_ID));
            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return name;
    }

    public String getPackagingTypeByServerId(int serverId) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TYPE_OF_PACKAGE + " WHERE "
                + KEY_STATUS + " = 1 AND "+PACKAGE_TYPE_SERVER_ID+" = "+ serverId +"; ";

        System.out.println("getPackagingTypeDataByServerId: "+selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        String name = null;
        if(res.moveToFirst()) {
            do{
                name = res.getString(res.getColumnIndex(PACKAGE_TYPE_NAME));
            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return name;
    }

    // ------------------------ "CfsAddress" table methods ----------------//

    public long insertCFSAddress(CFSAddressModel cfsAddressModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CFS_ADDRESS_SERVER_ID, cfsAddressModel.getServerId());
        contentValues.put(CFS_ADDRESS_NAME, cfsAddressModel.getName());
        contentValues.put(KEY_STATUS, cfsAddressModel.getStatus());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = 0;
        try {

            String whereClause = CFS_ADDRESS_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( cfsAddressModel.getServerId() )} ;

            int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_CFS_ADDRESS, whereClause, whereArgs);
            if( numRows <= 0 ) {
                id = db.insert(TABLE_CFS_ADDRESS, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<CFSAddressModel> getCfsData() {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        List<CFSAddressModel> array_list = new ArrayList<CFSAddressModel>();

        String selectQuery = "SELECT * FROM " + TABLE_CFS_ADDRESS + " WHERE "
                + KEY_STATUS + " = 1; ";

        Log.d("getCfsData: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                CFSAddressModel types = new CFSAddressModel();

                types.setServerId(res.getInt(res.getColumnIndex(CFS_ADDRESS_SERVER_ID)));
                types.setName(res.getString(res.getColumnIndex(CFS_ADDRESS_NAME)));

                array_list.add(types);

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return array_list;
    }

    // ------------------------ "InterDestiPorts" table methods ----------------//
    public int insertInterDestiPorts(String name, String country) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(INTER_DESTI_PORT_COUNTRY, country);
        contentValues.put(INTER_DESTI_PORT_NAME, name);
        contentValues.put(KEY_STATUS, 1);
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int id = 0;
        try {
            String whereClause = INTER_DESTI_PORT_COUNTRY+" = ? AND "+INTER_DESTI_PORT_NAME+" = ? ";
            String[] whereArgs = new String[]{ country, name } ;

            int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_INTERNATIONAL_DESTINATION_PORT, whereClause, whereArgs);
            if( numRows <= 0 ) {
                id = (int) db.insert(TABLE_INTERNATIONAL_DESTINATION_PORT, null, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public ArrayList getPortOfCountry(){
        ArrayList<String> poc = new ArrayList<String>();

        DatabaseClass sqliteDb = openDatabase();
        SQLiteDatabase db = sqliteDb.getReadableDatabase();

        String selectQuery = "SELECT DISTINCT " +INTER_DESTI_PORT_COUNTRY+" FROM " +
                TABLE_INTERNATIONAL_DESTINATION_PORT + " WHERE "+ KEY_STATUS + " = 1 ORDER BY "+
                INTER_DESTI_PORT_COUNTRY+";";

        Log.d("getPortOfCountry: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                poc.add(res.getString(res.getColumnIndex(INTER_DESTI_PORT_COUNTRY)));

            }while (res.moveToNext());
        }

        sqliteDb.closeDatabase();

        return poc;
    }

    public ArrayList getPortOfDestination(String countryName) {
        ArrayList<String> pod = new ArrayList<String>();

        DatabaseClass sqliteDb = openDatabase();
        SQLiteDatabase db = sqliteDb.getReadableDatabase();

        String selectQuery = "SELECT " +INTER_DESTI_PORT_NAME+" FROM " +
                TABLE_INTERNATIONAL_DESTINATION_PORT + " WHERE "+ KEY_STATUS + " = 1 AND " +
                INTER_DESTI_PORT_COUNTRY+" = '"+countryName+"' ORDER BY "+INTER_DESTI_PORT_NAME+" ;";

        Log.d("getPortOfCountry: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                pod.add(res.getString(res.getColumnIndex(INTER_DESTI_PORT_NAME)));

            }while (res.moveToNext());
        }

        sqliteDb.closeDatabase();

        return pod;
    }
    // ------------------------ "Transportation" table methods ----------------//

    public int insertTransportation(TransportationModel transportationModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSPORTATION_SERVER_ID, transportationModel.getServerId());
        contentValues.put(BOOKING_ID, transportationModel.getBookingId());
        contentValues.put(PICKUP, transportationModel.getPickUp());
        contentValues.put(DROP, transportationModel.getDrop());
        contentValues.put(SHIPMENT_TYPE, transportationModel.getShipmentType());
        contentValues.put(MEASUREMENT, transportationModel.getMeasurement());
        contentValues.put(GROSS_WEIGHT, transportationModel.getGrossWeight());
        contentValues.put(PACK_TYPE, transportationModel.getPackType());
        contentValues.put(NO_OF_PACK, transportationModel.getNoOfPack());
        contentValues.put(COMMODITY_SERVERID_IN_TRANSPORT, transportationModel.getCommodityServerId());
        contentValues.put(DIMEN_LENGTH, transportationModel.getDimenLength());
        contentValues.put(DIMEN_HEIGHT, transportationModel.getDimenHeight());
        contentValues.put(DIMEN_WIDTH, transportationModel.getDimenWidth());
        contentValues.put(AVAIL_OPTION, transportationModel.getAvailOption());
        contentValues.put(KEY_STATUS, transportationModel.getStatus());
        contentValues.put(KEY_CREATED_AT, transportationModel.getCreatedAt());

        // insert row
        int id = 0;
        try {
            String whereClause = TRANSPORTATION_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( transportationModel.getServerId() )} ;

            int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_TRANSPORTATION, whereClause, whereArgs);
            if( numRows <= 0 ) {
                id = (int) db.insert(TABLE_TRANSPORTATION, null, contentValues);

                int checkEntry = (int) DatabaseUtils.queryNumEntries(db, TABLE_SHIPMENT_CONFORMATION, SHIPMENT_CONFORMATION_BOOKING_ID+"=?", new String[]{ String.valueOf( transportationModel.getBookingId() )} );

                if(checkEntry <= 0) {

                    String createshipmentConform = "INSERT INTO "+TABLE_SHIPMENT_CONFORMATION+" ("+
                            SHIPMENT_CONFORMATION_BOOKING_ID+", "+
                            RATES+", "+
                            SHIPMENT_CONFORMATION_IS_TRANS_SERVICE+", "+
                            KEY_CREATED_AT+
                            ") VALUES ( '"+
                            transportationModel.getBookingId()+"', 0, 1, '"+transportationModel.getCreatedAt()+"' );";

                    System.out.println("createshipmentConform(Trans): "+createshipmentConform);

                    db.execSQL(createshipmentConform);
                }
                else {
                    String updateshipmentConform = "UPDATE "+TABLE_SHIPMENT_CONFORMATION+" SET "+
                            SHIPMENT_CONFORMATION_IS_TRANS_SERVICE+" = 1 WHERE "+
                            SHIPMENT_CONFORMATION_BOOKING_ID+" = '"+transportationModel.getBookingId()+"';";

                    System.out.println("createshipmentConform(Trans): "+updateshipmentConform);

                    db.execSQL(updateshipmentConform);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sqLiteDatabase.closeDatabase();
        return id;
    }

    public TransportationModel getTransportstionData(String bookingId) {
        DatabaseClass sqLiteDatabase = openDatabase();
        TransportationModel transportationModel = new TransportationModel();
        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TRANSPORTATION + " WHERE "
                + KEY_STATUS + " = 1 AND "+BOOKING_ID+" = '"+bookingId+"'; ";

        Log.d("getTransportstionData: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{
                transportationModel.setServerId(res.getInt(res.getColumnIndex(TRANSPORTATION_SERVER_ID)));
                transportationModel.setBookingId(res.getString(res.getColumnIndex(BOOKING_ID)));
                transportationModel.setPickUp(res.getString(res.getColumnIndex(PICKUP)));
                transportationModel.setDrop(res.getString(res.getColumnIndex(DROP)));
                transportationModel.setShipmentType(res.getInt(res.getColumnIndex(SHIPMENT_TYPE)));
                transportationModel.setMeasurement(res.getString(res.getColumnIndex(MEASUREMENT)));
                transportationModel.setGrossWeight(res.getFloat(res.getColumnIndex(GROSS_WEIGHT)));
                transportationModel.setPackType(res.getInt(res.getColumnIndex(PACK_TYPE)));
                transportationModel.setNoOfPack(res.getInt(res.getColumnIndex(NO_OF_PACK)));
                transportationModel.setCommodityServerId(res.getInt(res.getColumnIndex(COMMODITY_SERVERID_IN_TRANSPORT)));
                transportationModel.setDimenLength(res.getInt(res.getColumnIndex(DIMEN_LENGTH)));
                transportationModel.setDimenHeight(res.getInt(res.getColumnIndex(DIMEN_HEIGHT)));
                transportationModel.setDimenWidth(res.getInt(res.getColumnIndex(DIMEN_WIDTH)));
                transportationModel.setCreatedAt(res.getString(res.getColumnIndex(KEY_CREATED_AT)));

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return transportationModel;

    }

    // ------------------------ "CustomClearance" table methods ----------------//
    public int insertCustomClearance(CustomClearanceModel customClearanceModel) {

        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOM_CLEARANCE_SERVER_ID, customClearanceModel.getServerId());
        contentValues.put(BOOKING_ID, customClearanceModel.getBookingId());
        contentValues.put(CUSTOM_CLEARANCE_TYPE, customClearanceModel.getCCType());
        contentValues.put(CUSTOM_CLEARANCE_COMMODITY, customClearanceModel.getCommodityServerId());
        contentValues.put(CUSTOM_CLEARANCE_GROSS_WEIGHT, customClearanceModel.getGrossWeight());
        contentValues.put(HARMONIZED_SYSTEM_CODE, customClearanceModel.getHSCode());
        contentValues.put(SHIPMENT_TYPE, customClearanceModel.getiShipmentType());
        contentValues.put(STUFFING_TYPE, customClearanceModel.getStuffingType());
        contentValues.put(STUFFING_ADDRESS, customClearanceModel.getStuffingAddress());
        contentValues.put(AVAIL_OPTION, customClearanceModel.getAvailOption());
        contentValues.put(KEY_STATUS, customClearanceModel.getStatus());
        contentValues.put(KEY_CREATED_AT, customClearanceModel.getCreatedAt());

        // insert row
        int id = 0;
        try {
            String whereClause = CUSTOM_CLEARANCE_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( customClearanceModel.getServerId() )} ;

            int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_CUSTOM_CLEARANCE, whereClause, whereArgs);
            if( numRows <= 0 ) {
                id = (int) db.insert(TABLE_CUSTOM_CLEARANCE, null, contentValues);

                int checkEntry = (int) DatabaseUtils.queryNumEntries(db, TABLE_SHIPMENT_CONFORMATION, SHIPMENT_CONFORMATION_BOOKING_ID+"=?", new String[]{ String.valueOf( customClearanceModel.getBookingId() )} );

                if(checkEntry <= 0) {

                    String createshipmentConform = "INSERT INTO "+TABLE_SHIPMENT_CONFORMATION+" ("+
                            SHIPMENT_CONFORMATION_BOOKING_ID+", "+
                            RATES+", "+
                            SHIPMENT_CONFORMATION_IS_CC_SERVICE+", "+
                            KEY_CREATED_AT+
                            ") VALUES ( '"+
                            customClearanceModel.getBookingId()+"', 0, 1, '"+customClearanceModel.getCreatedAt()+"' );";

                    System.out.println("createshipmentConform(CC): "+createshipmentConform);

                    db.execSQL(createshipmentConform);
                }
                else {
                    String updateshipmentConform = "UPDATE "+TABLE_SHIPMENT_CONFORMATION+" SET "+
                            SHIPMENT_CONFORMATION_IS_CC_SERVICE+" = 1 WHERE "+
                            SHIPMENT_CONFORMATION_BOOKING_ID+" = '"+customClearanceModel.getBookingId()+"';";

                    System.out.println("createshipmentConform(CC): "+updateshipmentConform);

                    db.execSQL(updateshipmentConform);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sqLiteDatabase.closeDatabase();
        return id;

    }

    public CustomClearanceModel getCustomClrData(String bookingId) {
        DatabaseClass sqLiteDatabase = openDatabase();
        CustomClearanceModel customClearanceModel = new CustomClearanceModel();
        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CUSTOM_CLEARANCE + " WHERE "
                + KEY_STATUS + " = 1 AND "+BOOKING_ID+" = '"+bookingId+"'; ";

        Log.d("getCustomClrData: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{

                customClearanceModel.setServerId(res.getInt(res.getColumnIndex(CUSTOM_CLEARANCE_SERVER_ID)));
                customClearanceModel.setBookingId(res.getString(res.getColumnIndex(BOOKING_ID)));
                customClearanceModel.setCCType(res.getString(res.getColumnIndex(CUSTOM_CLEARANCE_TYPE)));
                customClearanceModel.setCommodityServerId(res.getInt(res.getColumnIndex(CUSTOM_CLEARANCE_COMMODITY)));
                customClearanceModel.setGrossWeight(res.getFloat(res.getColumnIndex(CUSTOM_CLEARANCE_GROSS_WEIGHT)));
                customClearanceModel.setHSCode(res.getInt(res.getColumnIndex(HARMONIZED_SYSTEM_CODE)));
                customClearanceModel.setiShipmentType(res.getInt(res.getColumnIndex(SHIPMENT_TYPE)));
                customClearanceModel.setStuffingType(res.getString(res.getColumnIndex(STUFFING_TYPE)));
                customClearanceModel.setStuffingAddress(res.getString(res.getColumnIndex(STUFFING_ADDRESS)));
                customClearanceModel.setAvailOption(res.getInt(res.getColumnIndex(AVAIL_OPTION)));
                customClearanceModel.setCreatedAt(res.getString(res.getColumnIndex(KEY_CREATED_AT)));

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return customClearanceModel;
    }
    // ------------------------ "FreightForwarding" table methods ----------------//
    public int insertFreightForwarding(FreightForwardingModel freightForwardingModel) {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = sqLiteDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FREIGHT_FORWARDING_SERVER_ID, freightForwardingModel.getServerId());
        contentValues.put(BOOKING_ID, freightForwardingModel.getBookingId());
        contentValues.put(PORT_OF_LOADING, freightForwardingModel.getPortOfLoading());
        contentValues.put(PORT_OF_COUNTRY, freightForwardingModel.getPortOfCountry());
        contentValues.put(PORT_OF_DESTINATION, freightForwardingModel.getPortOfDestination());
        contentValues.put(INCOTERM, freightForwardingModel.getStrIncoterm());
        contentValues.put(DESTIANTION_DELIVERY_ADDRESS, freightForwardingModel.getStrDestinatioDeliveryAdr());
        contentValues.put(FREIGHT_FORWARDING_SHIPMENT, freightForwardingModel.getShipmentType());
        contentValues.put(FREIGHT_FORWARDING_MEASURMETN, freightForwardingModel.getMeasurement());
        contentValues.put(FREIGHT_FORWARDING_GROSS_WEIGHT, freightForwardingModel.getGrossWeight());
        contentValues.put(FREIGHT_FORWARDING_PACK_TYPE, freightForwardingModel.getPackType());
        contentValues.put(FREIGHT_FORWARDING_NO_OF_PACK, freightForwardingModel.getNoOfPack());
        contentValues.put(FREIGHT_FORWARDING_COMMODITY, freightForwardingModel.getCommodityServerId());
        contentValues.put(AVAIL_OPTION, freightForwardingModel.getAvailOption());
        contentValues.put(KEY_STATUS, freightForwardingModel.getStatus());
        contentValues.put(KEY_CREATED_AT, freightForwardingModel.getCreatedAt());

        // insert row
        int id = 0;
        try {
            String whereClause = FREIGHT_FORWARDING_SERVER_ID+" = ?";
            String[] whereArgs = new String[]{ String.valueOf( freightForwardingModel.getServerId() )} ;

            int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_FREIGHT_FORWARDING, whereClause, whereArgs);
            if( numRows <= 0 ) {
                id = (int) db.insert(TABLE_FREIGHT_FORWARDING, null, contentValues);

                int checkEntry = (int) DatabaseUtils.queryNumEntries(db, TABLE_SHIPMENT_CONFORMATION, SHIPMENT_CONFORMATION_BOOKING_ID+"=?", new String[]{ String.valueOf( freightForwardingModel.getBookingId() )} );

                if(checkEntry <= 0) {

                    String createshipmentConform = "INSERT INTO "+TABLE_SHIPMENT_CONFORMATION+" ("+
                            SHIPMENT_CONFORMATION_BOOKING_ID+", "+
                            RATES+", "+
                            SHIPMENT_CONFORMATION_IS_FF_SERVICE+", "+
                            KEY_CREATED_AT+
                            ") VALUES ( '"+
                            freightForwardingModel.getBookingId()+"', 0, 1, '"+freightForwardingModel.getCreatedAt()+"' );";

                    System.out.println("createshipmentConform(CC): "+createshipmentConform);

                    db.execSQL(createshipmentConform);
                }
                else {
                    String updateshipmentConform = "UPDATE "+TABLE_SHIPMENT_CONFORMATION+" SET "+
                            SHIPMENT_CONFORMATION_IS_FF_SERVICE+" = 1 WHERE "+
                            SHIPMENT_CONFORMATION_BOOKING_ID+" = '"+freightForwardingModel.getBookingId()+"';";

                    System.out.println("createshipmentConform(CC): "+updateshipmentConform);

                    db.execSQL(updateshipmentConform);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sqLiteDatabase.closeDatabase();

        return id;
    }

    public FreightForwardingModel getFreightFwdData(String bookingId) {
        DatabaseClass sqLiteDatabase = openDatabase();
        FreightForwardingModel freightForwardingModel = new FreightForwardingModel();
        SQLiteDatabase db = sqLiteDatabase.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_FREIGHT_FORWARDING + " WHERE "
                + KEY_STATUS + " = 1 AND "+BOOKING_ID+" = '"+bookingId+"'; ";

        Log.d("getFreightFwdData: ", selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()) {
            do{

                freightForwardingModel.setServerId(res.getInt(res.getColumnIndex(FREIGHT_FORWARDING_SERVER_ID)));
                freightForwardingModel.setBookingId(res.getString(res.getColumnIndex(BOOKING_ID)));
                freightForwardingModel.setPortOfLoading(res.getString(res.getColumnIndex(PORT_OF_LOADING)));
                freightForwardingModel.setPortOfCountry(res.getString(res.getColumnIndex(PORT_OF_COUNTRY)));
                freightForwardingModel.setPortOfDestination(res.getString(res.getColumnIndex(PORT_OF_DESTINATION)));
                freightForwardingModel.setStrIncoterm(res.getString(res.getColumnIndex(INCOTERM)));
                freightForwardingModel.setStrDestinatioDeliveryAdr(res.getString(res.getColumnIndex(DESTIANTION_DELIVERY_ADDRESS)));
                freightForwardingModel.setShipmentType(res.getInt(res.getColumnIndex(FREIGHT_FORWARDING_SHIPMENT)));
                freightForwardingModel.setMeasurement(res.getString(res.getColumnIndex(FREIGHT_FORWARDING_MEASURMETN)));
                freightForwardingModel.setGrossWeight(res.getFloat(res.getColumnIndex(FREIGHT_FORWARDING_GROSS_WEIGHT)));
                freightForwardingModel.setPackType(res.getInt(res.getColumnIndex(FREIGHT_FORWARDING_PACK_TYPE)));
                freightForwardingModel.setNoOfPack(res.getInt(res.getColumnIndex(FREIGHT_FORWARDING_NO_OF_PACK)));
                freightForwardingModel.setCommodityServerId(res.getInt(res.getColumnIndex(FREIGHT_FORWARDING_COMMODITY)));
                freightForwardingModel.setAvailOption(res.getInt(res.getColumnIndex(AVAIL_OPTION)));
                freightForwardingModel.setCreatedAt(res.getString(res.getColumnIndex(KEY_CREATED_AT)));

            }while (res.moveToNext());
        }

        // closing database
        sqLiteDatabase.closeDatabase();

        return freightForwardingModel;
    }

    // ------------------------ "ShipmentConformation" table methods ----------------//
    public int numberOfEnquiryRowsByStatus() {
        DatabaseClass sqLiteDatabase = openDatabase();

        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = KEY_STATUS + " = ? ";
        String[] whereArgs = new String[]{String.valueOf("1")};

        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_SHIPMENT_CONFORMATION, whereClause, whereArgs);
        sqLiteDatabase.closeDatabase();
        return numRows;
    }

    public List<ShipmentConformationModel> getShipmentConformationData() {
        DatabaseClass sqLiteDatabase = openDatabase();
        SQLiteDatabase db = this.getReadableDatabase();
        List<ShipmentConformationModel> array_list = new ArrayList<ShipmentConformationModel>();

        String selectQuery = "SELECT * FROM " + TABLE_SHIPMENT_CONFORMATION + " WHERE "
                + KEY_STATUS+" = 1 ORDER BY "+KEY_CREATED_AT+" DESC;";

        System.out.println("getShipmentConformationData: "+ selectQuery);

        Cursor res =  db.rawQuery(selectQuery, null);

        if(res.moveToFirst()){
            do{
                ShipmentConformationModel shipmentConformationModel = new ShipmentConformationModel();

                shipmentConformationModel.setKeyId(res.getInt(res.getColumnIndex(KEY_ID)));
                shipmentConformationModel.setBookingId(res.getString(res.getColumnIndex(SHIPMENT_CONFORMATION_BOOKING_ID)));
                shipmentConformationModel.setEnquiryStatus(res.getInt(res.getColumnIndex(SHIPMENT_CONFORMATION_ENQUIRY_STATUS)));
                shipmentConformationModel.setQuotaion(res.getString(res.getColumnIndex(SHIPMENT_CONFORMATION_QUOTATION)));
                shipmentConformationModel.setRates(res.getInt(res.getColumnIndex(RATES)));
                shipmentConformationModel.setIsTrans(res.getInt(res.getColumnIndex(SHIPMENT_CONFORMATION_IS_TRANS_SERVICE)));
                shipmentConformationModel.setIsCC(res.getInt(res.getColumnIndex(SHIPMENT_CONFORMATION_IS_CC_SERVICE)));
                shipmentConformationModel.setIsFF(res.getInt(res.getColumnIndex(SHIPMENT_CONFORMATION_IS_FF_SERVICE)));
                shipmentConformationModel.setStatus(res.getInt(res.getColumnIndex(KEY_STATUS)));
                shipmentConformationModel.setCreatedAt(res.getString(res.getColumnIndex(KEY_CREATED_AT)));

                array_list.add(shipmentConformationModel);

            }while (res.moveToNext());
        }

        sqLiteDatabase.closeDatabase();
        return array_list;
    }

    public boolean updateEnquiryStatus(int status, String bookingId, String quotation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPMENT_CONFORMATION_ENQUIRY_STATUS, status);
        contentValues.put(SHIPMENT_CONFORMATION_QUOTATION, quotation);

        String whereClause;
        String[] whereArgs;

        whereClause = SHIPMENT_CONFORMATION_BOOKING_ID + " = ? ";
        whereArgs = new String[]{String.valueOf(bookingId)};

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_SHIPMENT_CONFORMATION, contentValues, whereClause, whereArgs);
        return true;
    }

    // when client accept or cancel quotation, db will update accordingly
    // if client accept quotation then status will be 4
    // if client cancel quotation then status will be 5
    public boolean updateShipmentConformTable(String bookingId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPMENT_CONFORMATION_ENQUIRY_STATUS, status);

        String whereClause;
        String[] whereArgs;

        whereClause = SHIPMENT_CONFORMATION_BOOKING_ID + " = ? ";
        whereArgs = new String[]{String.valueOf(bookingId)};

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_SHIPMENT_CONFORMATION, contentValues, whereClause, whereArgs);
        return true;
    }
}

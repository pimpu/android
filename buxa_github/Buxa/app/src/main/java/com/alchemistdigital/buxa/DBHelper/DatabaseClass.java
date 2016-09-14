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

import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.CustomClearanceCategoryModel;
import com.alchemistdigital.buxa.model.CustomClearanceLocation;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.model.ShipmentTypeModel;
import com.alchemistdigital.buxa.model.TransportServiceModel;
import com.alchemistdigital.buxa.model.TransportTypeModel;

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
    public static final String TABLE_SHIPMENT_CONFORMATION = "ShipmentConformation";
    public static final String TABLE_TRANSPORT_TYPE = "TransportType";
    public static final String TABLE_TRANSPORT_SERVICE = "TransportService";
    public static final String TABLE_TYPE_OF_PACKAGE = "TypeOfPackage";

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

    // TABLE_TYPE_OF_SHIPMENT Table - column names
    private static final String TOS_SERVER_ID = "tos_serverId";
    private static final String TOS_name = "tos_name";

    // TABLE_SHIPMENT_CONFORMATION Table - column names
    private static final String SHIPMENT_CONFORMATION_SERVER_ID = "shipment_conformation_serverId";
    private static final String SHIPMENT_CONFORMATION_BOOKING_ID = "booking_id";
    private static final String RATES = "rates";
    private static final String SERVICE_ID = "service_id";

    // TABLE_TRANSPORTATION Table - column names
    private static final String  TRANSPORTATION_SERVER_ID = "trans_serverId";
    private static final String  BOOKING_ID = "booking_id";
    private static final String  COMMODITY_SERVERID_IN_TRANSPORT = "commodity_serverId";
    private static final String  DIMEN_LENGTH = "dimen_length";
    private static final String  DIMEN_HEIGHT = "dimen_height";
    private static final String  DIMEN_WEIGHT = "dimen_weight";
    private static final String  SHIPMENT_TERM = "shipment_term";
    private static final String  NO_OF_PACK = "no_of_pack";
    private static final String  PACK_TYPE = "pack_type";
    private static final String  PICKUP = "pickup";
    private static final String  DROP = "dropLocation";
    private static final String  LRCOPY = "lr_copy";
    private static final String  AVAIL_OPTION = "avail_option";

    // TABLE_TRANSPORT_TYPE Table - column names
    private static final String TRANSPORT_TYPE_SERVER_ID = "trans_type_serverId";
    private static final String TRANSPORT_TYPE_NAME = "trans_type_name";

    // TABLE_TRANSPORT_SERVICE Table - column names
    private static final String TRANSPORT_SERVICE_SERVER_ID = "trans_service_serverId";
    private static final String TRANSPORT_SERVICE_NAME = "trans_service_name";

    // TABLE_TYPE_OF_PACKAGE Table - column names
    private static final String PACKAGE_TYPE_SERVER_ID = "package_type_serverId";
    private static final String PACKAGE_TYPE_NAME = "package_type_name";

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
                    COMMODITY_SERVERID_IN_TRANSPORT +" INTEGER," +
                    DIMEN_LENGTH +" INTEGER," +
                    DIMEN_HEIGHT +" INTEGER," +
                    DIMEN_WEIGHT +" INTEGER," +
                    SHIPMENT_TERM +" INTEGER," +
                    NO_OF_PACK +" INTEGER," +
                    PACK_TYPE +" INTEGER," +
                    BOOKING_ID +" VARCHAR(200)," +
                    PICKUP +" VARCHAR(200)," +
                    DROP +" VARCHAR(200)," +
                    LRCOPY +" VARCHAR(200)," +
                    AVAIL_OPTION +" TINYINT(4)," +
                    KEY_STATUS +" TINYINT(4)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Shipment conformation Table Create Statements
    private static final String CREATE_TABLE_SHIPMENT_CONFORMATION =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_SHIPMENT_CONFORMATION+
                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SHIPMENT_CONFORMATION_SERVER_ID +" INTEGER," +
                    SHIPMENT_CONFORMATION_BOOKING_ID+" VARCHAR(200)," +
                    RATES+" INTEGER," +
                    SERVICE_ID+" INTEGER," +
                    KEY_STATUS +" TINYINT(4)," +
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
        db.execSQL(CREATE_TABLE_SHIPMENT_CONFORMATION);
        db.execSQL(CREATE_TABLE_PACKAGE_TYPE);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPMENT_CONFORMATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE_OF_PACKAGE);

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
}

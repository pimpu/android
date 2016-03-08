package com.alchemistdigital.kissan.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private static final String TABLE_SOCIETY = "Society";
    private static final String TABLE_ENQUIRY = "Enquiry";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // SOCIETY Table - column names
    private static final String SOCIETY_COLUMN_USERID = "uID";
    private static final String SOCIETY_COLUMN_NAME = "soc_name";
    private static final String SOCIETY_COLUMN_CONTACT = "soc_contact";
    private static final String SOCIETY_COLUMN_EMAIL = "soc_email";
    private static final String SOCIETY_COLUMN_ADDRESS = "soc_adrs";

    // ENQUIRY Table - column names
    private static final String ENQUIRY_REF = "eRef";
    private static final String ENQUIRY_USERID = "uID";
    private static final String ENQUIRY_GROUPID = "gID";
    private static final String ENQUIRY_REPLYTO = "repTo";
    private static final String ENQUIRY_REPLIED = "replied";
    private static final String ENQUIRY_MESSAGE = "eMsg";
    private static final String ENQUIRY_SOCIETY = "eSociety";
    private static final String ENQUIRY_SOCIETY_ADDRESS = "eSocAdrs";
    private static final String ENQUIRY_SOCIETY_CONTACT = "eSocCont";
    private static final String ENQUIRY_SOCIETY_EMAIL = "eSocEmail";
    private static final String ENQUIRY_DOCUMENT = "eDoc";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }



    // Society Table Create Statements
    private static final String CREATE_TABLE_SOCIETY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_SOCIETY +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                    SOCIETY_COLUMN_USERID+" INTEGER," +
                    SOCIETY_COLUMN_NAME+" VARCHAR(100)," +
                    SOCIETY_COLUMN_CONTACT+" VARCHAR(100), " +
                    SOCIETY_COLUMN_EMAIL+" VARCHAR(100)," +
                    SOCIETY_COLUMN_ADDRESS+" VARCHAR(200)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    // Enquiry Table Create Statements
    private static final String CREATE_TABLE_ENQUIRY =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_ENQUIRY +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                    ENQUIRY_REF +" VARCHAR(20)," +
                    ENQUIRY_USERID +" INTEGER," +
                    ENQUIRY_GROUPID +" INTEGER," +
                    ENQUIRY_REPLYTO +" INTEGER," +
                    ENQUIRY_REPLIED +" TINYINT(4)," +
                    ENQUIRY_MESSAGE +" TEXT,"+
                    ENQUIRY_SOCIETY +" VARCHAR(50)," +
                    ENQUIRY_SOCIETY_ADDRESS +" VARCHAR(50)," +
                    ENQUIRY_SOCIETY_CONTACT +" VARCHAR(20)," +
                    ENQUIRY_SOCIETY_EMAIL +" VARCHAR(20)," +
                    ENQUIRY_DOCUMENT +" VARCHAR(50)," +
                    KEY_CREATED_AT + " DATETIME" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_SOCIETY);
        db.execSQL(CREATE_TABLE_ENQUIRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOCIETY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENQUIRY);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "Society" table methods ----------------//
    public long insertSociety(Society societyColumn){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, societyColumn.getId());
        contentValues.put(SOCIETY_COLUMN_USERID, societyColumn.getUserId());
        contentValues.put(SOCIETY_COLUMN_NAME, societyColumn.getSoc_name());
        contentValues.put(SOCIETY_COLUMN_CONTACT, societyColumn.getSoc_contact());
        contentValues.put(SOCIETY_COLUMN_EMAIL, societyColumn.getSoc_email());
        contentValues.put(SOCIETY_COLUMN_ADDRESS, societyColumn.getSoc_adrs());
        contentValues.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long society_id = db.insert(TABLE_SOCIETY, null, contentValues);
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
                + KEY_ID + " = " + society_id + " AND " + SOCIETY_COLUMN_USERID + " = " + uId;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Society society = new Society();
        society.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        society.setUserId(c.getInt(c.getColumnIndex(SOCIETY_COLUMN_USERID)));
        society.setSoc_name(c.getString(c.getColumnIndex(SOCIETY_COLUMN_NAME)));
        society.setSoc_contact(c.getString(c.getColumnIndex(SOCIETY_COLUMN_CONTACT)));
        society.setSoc_email(c.getString(c.getColumnIndex(SOCIETY_COLUMN_EMAIL)));
        society.setSoc_adrs(c.getString(c.getColumnIndex(SOCIETY_COLUMN_ADDRESS)));
        society.setCreted_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return society;
    }

    /**
     * getting society count
     */
    public int numberOfSocietyRows(){
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_SOCIETY,SOCIETY_COLUMN_USERID+" = ?",new String[]{ String.valueOf(uId)});
        return numRows;
    }

    /**
     * Updating a society
     */
    public boolean updateContact(Society society){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SOCIETY_COLUMN_NAME, society.getSoc_name());
        contentValues.put(SOCIETY_COLUMN_CONTACT, society.getSoc_contact());
        contentValues.put(SOCIETY_COLUMN_EMAIL, society.getSoc_email());
        contentValues.put(SOCIETY_COLUMN_ADDRESS, society.getSoc_adrs());

//      db.update(String table_name,String where_clause,String[] where_args);
        db.update(TABLE_SOCIETY, contentValues, KEY_ID+" = ? ", new String[] { String.valueOf(society.getId()) } );
        return true;
    }

    /**
     * Deleting a society
     */
    public Integer deleteContact (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

//      db.delete(String table_name,String where_clause,String[] where_args);
        return db.delete(TABLE_SOCIETY,
                KEY_ID+" = ? ",
                new String[] { String.valueOf(id) });
    }

    /**
     * getting all societies
    **/
    public List<Society> getAllSocieties(){

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        List<Society> array_list = new ArrayList<Society>();
        String selectQuery = "SELECT * FROM "+TABLE_SOCIETY+" WHERE "+SOCIETY_COLUMN_USERID+" = "+uId+" ORDER BY "+KEY_CREATED_AT;
        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);

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

//                adding to society list
                array_list.add(society);
            }while (res.moveToNext());
        }
        return array_list;
    }
    // ------------------------ "Society" table methods ----------------//


    // ------------------------ "Enquiry" table methods ----------------//
    public long insertEnquiry(Enquiry enquiryColumn){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, enquiryColumn.getEnquiry_id());
        contentValues.put(KEY_CREATED_AT, enquiryColumn.getCreted_at());
        contentValues.put(ENQUIRY_REF, enquiryColumn.getEnquiry_reference());
        contentValues.put(ENQUIRY_USERID, enquiryColumn.getEnquiry_userId());
        contentValues.put(ENQUIRY_GROUPID, enquiryColumn.getEnquiry_groupId());
        contentValues.put(ENQUIRY_REPLYTO, enquiryColumn.getEnquiry_replyTo());
        contentValues.put(ENQUIRY_REPLIED, enquiryColumn.getEnquiry_replied());
        contentValues.put(ENQUIRY_MESSAGE, enquiryColumn.getEnquiry_message());
        contentValues.put(ENQUIRY_SOCIETY, enquiryColumn.getEnquiry_society());
        contentValues.put(ENQUIRY_SOCIETY_ADDRESS, enquiryColumn.getEnquiry_society_address());
        contentValues.put(ENQUIRY_SOCIETY_CONTACT, enquiryColumn.getEnquiry_society_contact());
        contentValues.put(ENQUIRY_SOCIETY_EMAIL, enquiryColumn.getEnquiry_society_email());
        contentValues.put(ENQUIRY_DOCUMENT, enquiryColumn.getEnquiry_document());

        // insert row
        long enquiry_id = db.insert(TABLE_ENQUIRY, null, contentValues);
        return enquiry_id;
    }

    /**
     * Updating a society
     */
    public int updateEnquiryReplied(String eId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ENQUIRY_REPLIED, 1);

//      db.update(String table_name,String where_clause,String[] where_args);
        int update = db.update(TABLE_ENQUIRY, contentValues, KEY_ID + " = ? ", new String[]{eId});
        return update;
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
                + ENQUIRY_USERID + " = " + uId+" ORDER BY "+KEY_CREATED_AT;

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
                enquiry.setEnquiry_message(res.getString(res.getColumnIndex(ENQUIRY_MESSAGE)));
                enquiry.setEnquiry_society(res.getString(res.getColumnIndex(ENQUIRY_SOCIETY)));
                enquiry.setEnquiry_society_address(res.getString(res.getColumnIndex(ENQUIRY_SOCIETY_ADDRESS)));
                enquiry.setEnquiry_society_contact(res.getString(res.getColumnIndex(ENQUIRY_SOCIETY_CONTACT)));
                enquiry.setEnquiry_society_email(res.getString(res.getColumnIndex(ENQUIRY_SOCIETY_EMAIL)));
                enquiry.setEnquiry_document(res.getString(res.getColumnIndex(ENQUIRY_DOCUMENT)));
                enquiry.setCreted_at(formatedDate);

                array_list.add(enquiry);

            }while (res.moveToNext());
        }
        return array_list;
    }

    /**
     * getting enquiry count using user id
     */
    public int numberOfEnquiryRowsByUid(){
        SQLiteDatabase db = this.getReadableDatabase();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

        int numRows = (int)DatabaseUtils.queryNumEntries(db,TABLE_ENQUIRY, ENQUIRY_USERID+" = ?",new String[]{ String.valueOf(uId)});
        return numRows;
    }
    // ------------------------ "Enquiry" table methods ----------------//

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
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}

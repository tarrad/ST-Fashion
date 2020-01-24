package com.example.st_fashion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.st_fashion.ui.create.CreateFragment;

import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "STDatabase";

    private static final String TABLE_USERS = "users";
    private static final String TABLE_USERS_USERNAME = "username";
    private static final String TABLE_USERS_EMAIL = "email";
    private static final String TABLE_USERS_PASSWORD = "password";

    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_ITEMS_USERNAME = "username";
    private static final String TABLE_ITEMS_NAME = "name";
    private static final String TABLE_ITEMS_TYPE = "type";
    private static final String TABLE_ITEMS_COLOR = "color";
    private static final String TABLE_ITEMS_SEASON = "season";
    private static final String TABLE_ITEMS_IMAGE = "image";

    private static final String TABLE_UNIFORMS = "uniforms";
    private static final String TABLE_UNIFORMS_USERNAME = "username";
    private static final String TABLE_UNIFORMS_TOP = "top";
    private static final String TABLE_UNIFORMS_BOTTOM = "bottom";
    private static final String TABLE_UNIFORMS_SHOES = "shoes";
    private static final String TABLE_UNIFORMS_SEASON = "season";
    private static final String TABLE_UNIFORMS_COLOR = "color";

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //=======================================================================================
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + TABLE_USERS_USERNAME + " TEXT PRIMARY KEY," + TABLE_USERS_EMAIL + " TEXT,"
                + TABLE_USERS_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
        //=======================================================================================
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + TABLE_ITEMS_USERNAME + " TEXT," + TABLE_ITEMS_NAME + " TEXT,"
                + TABLE_ITEMS_TYPE + " TEXT," + TABLE_ITEMS_COLOR + " TEXT,"
                + TABLE_ITEMS_SEASON + " TEXT," + TABLE_ITEMS_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
        //=======================================================================================
        String CREATE_UNIFORMS_TABLE = "CREATE TABLE " + TABLE_UNIFORMS + "("
                + "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + TABLE_UNIFORMS_USERNAME + " TEXT," + TABLE_UNIFORMS_TOP + " BLOB,"
                + TABLE_UNIFORMS_BOTTOM + " BLOB," + TABLE_UNIFORMS_SHOES + " BLOB,"
                + TABLE_UNIFORMS_SEASON + " TEXT," + TABLE_UNIFORMS_COLOR + " TEXT)";
        db.execSQL(CREATE_UNIFORMS_TABLE);
        //=======================================================================================

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_UNIFORMS);
        onCreate(db);

    }

    //==========================================================================================================================================
    public int addUser(UsersDb user)
    {
        //check if we have user with same username
        String selectQuery = "SELECT " + TABLE_USERS_USERNAME + " FROM " + TABLE_USERS + " WHERE " + TABLE_USERS_USERNAME + " = ?";
        SQLiteDatabase dbRead = getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(selectQuery,new String[]{user.username});

        if(cursor.getCount() > 0)
        {
            //same username
            cursor.close();
            dbRead.close();
            return 0;
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(TABLE_USERS_USERNAME,user.username);
            values.put(TABLE_USERS_EMAIL,user.email);
            values.put(TABLE_USERS_PASSWORD,user.password);

            db.insert(TABLE_USERS,null,values);
            cursor.close();
            db.close();
            return 1;
        }
    }

    public String checkRegisteredUser(UsersDb user)
    {
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS_USERNAME + " = ?";
        SQLiteDatabase dbWr = getWritableDatabase();
        Cursor cursor = dbWr.rawQuery(selectQuery,new String[]{user.username});

        if(cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            int col = cursor.getColumnIndex(TABLE_USERS_PASSWORD);
            String password = cursor.getString(col);
            if(user.password.equals(password))
            {
                col = cursor.getColumnIndex(TABLE_USERS_EMAIL);
                String email = cursor.getString(col);
                cursor.close();
                dbWr.close();
                return email;
            }
            else
            {
                cursor.close();
                dbWr.close();
                return "0";
            }
        }
        cursor.close();
        dbWr.close();
        return "0";
    }
    //==========================================================================================================================================

    public int addItem(ItemsDb item)
    {
        SQLiteDatabase dbWr = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TABLE_ITEMS_USERNAME,item.username);
        cv.put(TABLE_ITEMS_NAME,item.name);
        cv.put(TABLE_ITEMS_TYPE,item.type);
        cv.put(TABLE_ITEMS_COLOR,item.color);
        cv.put(TABLE_ITEMS_SEASON,item.season);
        cv.put(TABLE_ITEMS_IMAGE,item.image);

        long result = dbWr.insert(TABLE_ITEMS,null,cv);
        dbWr.close();
        if(result > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public Cursor getData(String sql)
    {
        SQLiteDatabase dbWr = getWritableDatabase();
        Cursor cursor = dbWr.rawQuery(sql,new String[]{Menu.getUsername(), CreateFragment.getItemClicked()});

        return cursor;
    }
    //==========================================================================================================================================

    public int addUniform(UniformsDb ud)
    {
        SQLiteDatabase dbWr = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TABLE_UNIFORMS_USERNAME,ud.username);
        cv.put(TABLE_UNIFORMS_TOP,ud.imageTop);
        cv.put(TABLE_UNIFORMS_BOTTOM,ud.imageBottom);
        cv.put(TABLE_UNIFORMS_SHOES,ud.imageShoes);
        cv.put(TABLE_UNIFORMS_SEASON,ud.season);
        cv.put(TABLE_UNIFORMS_COLOR,ud.color);

        dbWr.insert(TABLE_UNIFORMS,null,cv);
        dbWr.close();
        return 1;
    }

    public Cursor getUniforms(String sql)
    {
        SQLiteDatabase dbWr = getWritableDatabase();
        Cursor cursor = dbWr.rawQuery(sql,new String[]{Menu.getUsername()});

        return cursor;
    }
    //==========================================================================================================================================

    public byte[] getRandomData(String sql,String type)
    {
        SQLiteDatabase dbWr = getWritableDatabase();
        Cursor cursor = dbWr.rawQuery(sql,new String[]{Menu.getUsername(),type});

        if(cursor.getCount() > 0)
        {
            Random random = new Random();
            int randomNum = random.nextInt(cursor.getCount());
            cursor.moveToPosition(randomNum);
            int col = cursor.getColumnIndex(TABLE_ITEMS_IMAGE);
            byte[] itemImage = cursor.getBlob(col);
            cursor.close();
            dbWr.close();
            return itemImage;
        }
        else
        {
            cursor.close();
            dbWr.close();
            return null;
        }
    }
    //==========================================================================================================================================

    public Cursor getParamUniforms(String sql,String season,String color)
    {
        SQLiteDatabase dbWr = getWritableDatabase();
        Cursor cursor = dbWr.rawQuery(sql,new String[]{Menu.getUsername(),season,color});

        return cursor;
    }

}

package br.embrapa.embrapashare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "db.db";
    public static final String TABLE = "records";
    public static final int VERSION = 1;

    public static final String ID = "_id";
    public static final String STATUS = "status";
    public static final String LAST_UPDATE = "last_update";
    public static final String CULTURE = "culture";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_NAME = "image_name";
    public static final String LOCAL = "local";
    public static final String EXIF_LAT = "exif_lat";
    public static final String EXIF_LONG = "exif_long";
    public static final String EXIF_DATE = "exif_date";


    public DBHelper(Context context){
        super(context, DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+" ("
                + ID + " integer primary key autoincrement,"
                + STATUS + " integer,"
                + LAST_UPDATE + " text,"
                + CULTURE + " integer,"
                + DATE + " text,"
                + DESCRIPTION + " text,"
                + IMAGE_NAME + " text,"
                + LOCAL + " text,"
                + EXIF_LAT + " text,"
                + EXIF_LONG + " text,"
                + EXIF_DATE + " text"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE);
        onCreate(db);
    }
}

package br.embrapa.embrapashare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DBNAME = "db.db";

    public static final String TABLE_RECORDS = "records";
    public static final String TABLE_IMAGES = "images";
    public static final String TABLE_CULTURES = "cultures";

    public static final String ID = "_id";
    public static final String STATUS = "status";
    public static final String LAST_UPDATE = "last_update";
    public static final String CULTURE = "culture";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String LOCAL = "local";

    public static final String IMAGE_ID = "_image_id";
    public static final String IMAGE_REC_ID = "rec_id";
    public static final String IMAGE_NAME = "image_name";

    public static final String CULTURE_ID = "culture_id";
    public static final String CULTURE_NAME = "culture_name";

    private final Context context;


    public DBHelper(Context context){
        super(context, DBNAME,null,VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE_RECORDS+" ("
                + ID + " integer primary key autoincrement,"
                + STATUS + " integer,"
                + LAST_UPDATE + " text,"
                + CULTURE + " text,"
                + DATE + " text,"
                + DESCRIPTION + " text,"
                + LOCAL + " text"
                +")";
        db.execSQL(sql);

        sql = "CREATE TABLE "+TABLE_IMAGES+" ("
                + IMAGE_ID + " integer primary key autoincrement,"
                + IMAGE_REC_ID + " integer,"
                + IMAGE_NAME + " text"
                +")";
        db.execSQL(sql);

        sql = "CREATE TABLE "+TABLE_CULTURES+" ("
                + CULTURE_ID + " integer primary key autoincrement,"
                + CULTURE_NAME + " text"
                +")";
        db.execSQL(sql);

        String[] cultures_array = context.getResources().getStringArray(R.array.cultures_array);
        sql = "INSERT INTO "+TABLE_CULTURES+" ("+CULTURE_NAME+") VALUES ";
        for(int i = 0; i < cultures_array.length; i++){
            sql += "(\""+cultures_array[i]+"\")";
            if(i < cultures_array.length - 1)
                sql += ", ";
        }
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CULTURES);
        onCreate(db);
    }//TODO dropa mesmo?
}

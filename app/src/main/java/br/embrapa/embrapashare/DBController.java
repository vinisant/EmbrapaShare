package br.embrapa.embrapashare;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;

public class DBController {

    private SQLiteDatabase db;
    private DBHelper helper;

    public DBController(Context context){
        helper = new DBHelper(context);
    }

    public long insertData(int status, String last_update, int culture, String date, String description, String image_name, String local, String exif_lat, String exif_long, String exif_date){
        ContentValues values;
        long result;
//TODO testar com entradas nulas
        db = helper.getWritableDatabase();
        values = new ContentValues();
        values.put(DBHelper.STATUS, status);
        values.put(DBHelper.LAST_UPDATE, last_update);
        values.put(DBHelper.CULTURE, culture);
        values.put(DBHelper.DATE, date);
        values.put(DBHelper.DESCRIPTION, description);
        values.put(DBHelper.IMAGE_NAME, image_name);
        values.put(DBHelper.LOCAL, local);
        values.put(DBHelper.EXIF_LAT, exif_lat);
        values.put(DBHelper.EXIF_LONG, exif_long);
        values.put(DBHelper.EXIF_DATE, exif_date);

        result = db.insert(DBHelper.TABLE, null, values);
        db.close();

        return result; //TODO ver se esse retorno rowid eh confiavel
    }

    public Cursor loadAllData(){
        Cursor cursor;
        String[] fields =  {DBHelper.ID,DBHelper.DATE, DBHelper.CULTURE, DBHelper.STATUS, DBHelper.DESCRIPTION, DBHelper.IMAGE_NAME};
        db = helper.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE, fields, null, null, null, null, DBHelper.LAST_UPDATE+" DESC", null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }


    public Cursor loadDataByID(long id){
        Cursor cursor;
        String[] fields =  {DBHelper.ID,DBHelper.DATE, DBHelper.CULTURE, DBHelper.STATUS, DBHelper.DESCRIPTION, DBHelper.IMAGE_NAME};
        db = helper.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE,fields, DBHelper.ID + "=" + id, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }



    public LinkedList<RegisteredItem> loadDataForList(){
        Cursor cursor = loadAllData();
        LinkedList<RegisteredItem> itens = new LinkedList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            itens.add(new RegisteredItem(
                    cursor.getLong(cursor.getColumnIndex(DBHelper.ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.STATUS)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.DATE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.CULTURE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION))
            ));
        }
        return itens;
    }


    public RegisteredItem loadDataForEdit(long id){
        Cursor cursor = loadDataByID(id);
        cursor.moveToFirst();
        RegisteredItem data = new RegisteredItem();
        if(!cursor.isAfterLast()){
            data.setRegisterID(cursor.getLong(cursor.getColumnIndex(DBHelper.ID)));
            data.setStatus(cursor.getInt(cursor.getColumnIndex(DBHelper.STATUS)));
            data.setCulture(cursor.getInt(cursor.getColumnIndex(DBHelper.CULTURE)));
            data.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.DATE)));
            data.setImageName(cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_NAME)));
            data.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION)));
        }
        else return null;
        return data;
    }


    public int updateDataByID(long id, int status, String last_update, int culture, String date, String description, String local){
        ContentValues values;
        String where;

        db = helper.getWritableDatabase();

        where = DBHelper.ID + "=" + id;
        values = new ContentValues();
        values.put(DBHelper.STATUS, status);
        values.put(DBHelper.LAST_UPDATE, last_update);
        values.put(DBHelper.CULTURE, culture);
        values.put(DBHelper.DATE, date);
        values.put(DBHelper.DESCRIPTION, description);
        //values.put(DBHelper.IMAGE_NAME, image_name);
        values.put(DBHelper.LOCAL, local);
        //values.put(DBHelper.EXIF_LAT, exif_lat);
        //values.put(DBHelper.EXIF_LONG, exif_long);
        //values.put(DBHelper.EXIF_DATE, exif_date);

        int r = db.update(DBHelper.TABLE,values,where,null);
        db.close();
        return r;
    }


    public void deleteDataByID(String id){
        String where = DBHelper.ID + "=" + id;
        db = helper.getReadableDatabase();
        db.delete(DBHelper.TABLE,where,null);
        db.close();
    }
}

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
//mudar esse retorno
    public String insertData(String date, String culture, String status, String last_update, String description, String image_name, String local){
        ContentValues values;
        long result;

        db = helper.getWritableDatabase();
        values = new ContentValues();
        values.put(DBHelper.CULTURE, culture);
        values.put(DBHelper.STATUS, status);
        values.put(DBHelper.DATE, date);
        values.put(DBHelper.LAST_UPDATE, last_update);
        values.put(DBHelper.DESCRIPTION, description);
        values.put(DBHelper.IMAGE_NAME, image_name);
        values.put(DBHelper.LOCAL, local);

        result = db.insert(DBHelper.TABLE, null, values);
        db.close();

        if (result ==-1)
            return "Error when insert";
        else
            return "Success";

    }

    public Cursor loadData(){
        Cursor cursor;
        String[] fields =  {helper.ID,helper.DATE, helper.CULTURE, helper.STATUS, helper.DESCRIPTION, helper.IMAGE_NAME};
        db = helper.getReadableDatabase();
        cursor = db.query(helper.TABLE, fields, null, null, null, null, helper.LAST_UPDATE+" DESC", null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }


    public Cursor loadDataByID(String id){
        Cursor cursor;
        String[] fields =  {helper.ID,helper.DATE, helper.CULTURE, helper.STATUS, helper.DESCRIPTION, helper.IMAGE_NAME};
        db = helper.getReadableDatabase();
        cursor = db.query(helper.TABLE,fields, helper.ID + "=" + id, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }



    public LinkedList<RegisteredItem> loadDataForList(){
        Cursor cursor = loadData();
        LinkedList<RegisteredItem> itens = new LinkedList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            itens.add(new RegisteredItem(
                    cursor.getString(cursor.getColumnIndex(helper.ID)),
                    cursor.getString(cursor.getColumnIndex(helper.STATUS)),
                    cursor.getString(cursor.getColumnIndex(helper.DATE)),
                    cursor.getString(cursor.getColumnIndex(helper.IMAGE_NAME)),
                    cursor.getString(cursor.getColumnIndex(helper.CULTURE)),
                    cursor.getString(cursor.getColumnIndex(helper.DESCRIPTION))
            ));
        }
        return itens;
    }


    public String[] loadDataForEdit(String id){
        Cursor cursor = loadDataByID(id);
        cursor.moveToFirst();
        String[] data = new String[6];
        if(!cursor.isAfterLast()){
            data[0] = cursor.getString(cursor.getColumnIndex(helper.ID)); //deixar vindo da classe estatica
            data[1] = cursor.getString(cursor.getColumnIndex(helper.STATUS));
            data[2] = cursor.getString(cursor.getColumnIndex(helper.DATE));
            data[3] = cursor.getString(cursor.getColumnIndex(helper.IMAGE_NAME));
            data[4] = cursor.getString(cursor.getColumnIndex(helper.CULTURE));
            data[5] = cursor.getString(cursor.getColumnIndex(helper.DESCRIPTION));
        }
        else return null;
        return data;
    }


    public void alterDataByID(String id, String date, String culture, String status, String last_update, String description, String local){
        ContentValues values;
        String where;

        db = helper.getWritableDatabase();

        where = helper.ID + "=" + id;
        values = new ContentValues();
        values.put(DBHelper.CULTURE, culture);
        values.put(DBHelper.STATUS, status);
        values.put(DBHelper.DATE, date);
        values.put(DBHelper.LAST_UPDATE, last_update);
        values.put(DBHelper.DESCRIPTION, description);
        values.put(DBHelper.LOCAL, local);

        db.update(helper.TABLE,values,where,null);
        db.close();
    }


    public void deleteDataByID(String id){
        String where = DBHelper.ID + "=" + id;
        db = helper.getReadableDatabase();
        db.delete(DBHelper.TABLE,where,null);
        db.close();
    }
}

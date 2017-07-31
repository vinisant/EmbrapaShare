package br.embrapa.embrapashare;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;

public class DBController {

    private SQLiteDatabase db;
    private DBHelper helper;

    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_PENDING = 1;


    public DBController(Context context){
        helper = new DBHelper(context);
    }
//TODO sigleton

/**CREATE*/

    public long insertData(int status, String last_update, String culture, String date, String description, String local, String image_name){
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
        values.put(DBHelper.LOCAL, local);

        result = db.insert(DBHelper.TABLE_RECORDS, null, values);
        db.close();

        if(result >= 0)
            insertImage(result, image_name);

        return result; //TODO ver se esse retorno rowid eh confiavel
    }

    public void insertImages(long record_id, String[] images_names){
        ContentValues values;
        db = helper.getWritableDatabase();

        for(int i=0; i < images_names.length;i++){
            values = new ContentValues();
            values.put(DBHelper.IMAGE_REC_ID, record_id);
            values.put(DBHelper.IMAGE_NAME, images_names[i]);
            /*result = TODO tratar falha*/ db.insert(DBHelper.TABLE_IMAGES, null, values);
        }
        db.close();
    }
    public void insertImage(long record_id, String image_name){
        ContentValues values;
        db = helper.getWritableDatabase();
        values = new ContentValues();
        values.put(DBHelper.IMAGE_REC_ID, record_id);
        values.put(DBHelper.IMAGE_NAME, image_name);
        /*result = TODO tratar falha*/ db.insert(DBHelper.TABLE_IMAGES, null, values);

        db.close();
    }


/**READ*/

    public Cursor select(String where){
        Cursor cursor;
        String[] fields =  {DBHelper.ID,DBHelper.DATE, DBHelper.CULTURE, DBHelper.STATUS, DBHelper.DESCRIPTION, DBHelper.LOCAL, DBHelper.LAST_UPDATE};
        db = helper.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE_RECORDS,fields, where, null, null, null, DBHelper.LAST_UPDATE+" DESC", null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor selectImages(String where, String count){
        Cursor cursor;
        String[] fields =  {DBHelper.IMAGE_ID,DBHelper.IMAGE_REC_ID, DBHelper.IMAGE_NAME};
        db = helper.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE_IMAGES,fields, where, null, null, null, null, count);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor loadDataByID(long id){
        return select(DBHelper.ID + "=" + id);
    }

    public LinkedList<RegisteredItem> loadDataForList(){
        Cursor cursor = select(null);
        LinkedList<RegisteredItem> itens = new LinkedList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            itens.add(new RegisteredItem(
                    cursor.getLong(cursor.getColumnIndex(DBHelper.ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.STATUS)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.LAST_UPDATE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.CULTURE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.DATE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.LOCAL)),
                    loadFirstImage(cursor.getLong(cursor.getColumnIndex(DBHelper.ID)))
            ));
        }
        return itens;
    }

    public String loadFirstImage(long id){
        Cursor cursor = selectImages(DBHelper.IMAGE_REC_ID + "=" + id, "1");
        return cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_NAME));
    }

    public LinkedList<ImageItem> loadImagesForList(long id) {
        Cursor cursor = selectImages(DBHelper.IMAGE_REC_ID + "=" + id,null);
        LinkedList<ImageItem> itens = new LinkedList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            itens.add(new ImageItem(
                    cursor.getLong(cursor.getColumnIndex(DBHelper.IMAGE_ID)),
                    cursor.getLong(cursor.getColumnIndex(DBHelper.IMAGE_REC_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_NAME))
            ));
        }
        return itens;
    }

    public RegisteredItem loadDataForEdit(long id){
        Cursor cursor = loadDataByID(id);
        cursor.moveToFirst();
        RegisteredItem data = new RegisteredItem();
        if(!cursor.isAfterLast()){
            data.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.ID)));
            data.setStatus(cursor.getInt(cursor.getColumnIndex(DBHelper.STATUS)));
            data.setLastUpdate(cursor.getString(cursor.getColumnIndex(DBHelper.LAST_UPDATE)));
            data.setCulture(cursor.getString(cursor.getColumnIndex(DBHelper.CULTURE)));
            data.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.DATE)));
            data.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION)));
            data.setLocal(cursor.getString(cursor.getColumnIndex(DBHelper.LOCAL)));
        }
        else return null;
        return data;
    }

    public LinkedList<String> loadCultures(){
        Cursor cursor;
        String[] fields =  {DBHelper.CULTURE_ID,DBHelper.CULTURE_NAME};
        db = helper.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE_CULTURES,fields, null, null, null, null, DBHelper.CULTURE_NAME, null);


        LinkedList<String> itens = new LinkedList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            itens.add(cursor.getString(cursor.getColumnIndex(DBHelper.CULTURE_NAME)));
        }
        //db.close(); //TODO tirar esses close
        return itens;
    }

/**UPDATE*/

    public int updateDataByID(long id, int status, String last_update, String culture, String date, String description, String local){
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
        values.put(DBHelper.LOCAL, local);

        int r = db.update(DBHelper.TABLE_RECORDS,values,where,null);
        db.close();
        return r;
    }

    public int updateImagesByRecID(long record_id, String[] images_names) {
        deleteImagesByRecID(record_id+"");//TODO padronizar se id eh long ou string
        insertImages(record_id, images_names);
        return 0;//TODO
    }

/**DELETE*/

    public void deleteDataByID(String id){
        String where = DBHelper.ID + "=" + id;
        db = helper.getReadableDatabase();
        db.delete(DBHelper.TABLE_RECORDS,where,null);
        db.close();

        deleteImagesByRecID(id);
    }

    public void deleteImagesByRecID(String record_id){
        String where = DBHelper.IMAGE_REC_ID+ "=" + record_id;
        db = helper.getReadableDatabase();
        db.delete(DBHelper.TABLE_IMAGES,where,null);
        db.close();
    }

    public void deleteImagesByID(String image_id){
        String where = DBHelper.IMAGE_ID+ "=" + image_id;
        db = helper.getReadableDatabase();
        db.delete(DBHelper.TABLE_IMAGES,where,null);
        db.close();
    }

}

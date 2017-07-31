package br.embrapa.embrapashare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean listAsGrid = false;
    private LinkedList<RegisteredItem> itens;
    private DBController crud;

    private SharedPreferences sharedPref;
    private boolean permissionStorage;

    public Boolean getListAsGrid() {
        return listAsGrid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(Context.MODE_PRIVATE);


        permissionStorage = sharedPref.getBoolean("embrapaStorePermission", false);
        permissionStorageCheck();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPost(CAPTURE_PHOTO);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPost(LOAD_PHOTO);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        crud = new DBController(getBaseContext());

        itens = crud.loadDataForList();
        //itens.add(new RegisteredItem());
        //itens.add(new RegisteredItem());
        applyLayout();

        //ulugiguiuploadImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            listAsGrid = !listAsGrid;
            applyLayout();

        }

        return super.onOptionsItemSelected(item);
    }

    static final int CAPTURE_PHOTO = 1;
    static final int LOAD_PHOTO = 2;
    static final int EDIT_PHOTO = 3;
    private void newPost(int requestCode){
        permissionStorageCheck();
        Intent intent = new Intent(this, EditActivity.class);
        if(permissionStorage) {
            if (requestCode == CAPTURE_PHOTO) {
                intent.putExtra("requestCode", requestCode);
                startActivityForResult(intent, requestCode);
            } else if (requestCode == LOAD_PHOTO) {
                intent.putExtra("requestCode", requestCode);
                startActivityForResult(intent, requestCode);
            }
        }
    }
    //event from layouts registered_item
    public void editPost(View v) {
        //Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("requestCode", EDIT_PHOTO);
        intent.putExtra("registerID", v.getTag().toString());
        startActivityForResult(intent, EDIT_PHOTO);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_PHOTO && resultCode == RESULT_OK) {
            itens = crud.loadDataForList();
            applyLayout();
        }
        else if (requestCode == LOAD_PHOTO && resultCode == RESULT_OK) {
            itens = crud.loadDataForList();
            applyLayout();
        }
        else if (requestCode == EDIT_PHOTO && resultCode == RESULT_OK) {
            itens = crud.loadDataForList();
            applyLayout();
        }
        else if (resultCode == RESULT_FIRST_USER){
            itens = crud.loadDataForList();
            applyLayout();
            Snackbar.make(findViewById(R.id.fab), getString(R.string.delete_confirmation), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }


    }

    public void applyLayout(){
        if(itens.size() <= 0) {
            ((TextView)findViewById(R.id.txt_empty1)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.txt_empty2)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.txt_empty3)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.txt_empty4)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.txt_empty5)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.txt_empty1)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.txt_empty2)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.txt_empty3)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.txt_empty4)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.txt_empty5)).setVisibility(View.GONE);
        }

        LayoutManager layout;// = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        if(listAsGrid){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                layout = new GridLayoutManager(this, 2);
            else
                layout = new GridLayoutManager(this, 3);
        }
        else {
            layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
        recyclerView.setAdapter(new RegisteredItemAdapter(itens, this, listAsGrid));
        recyclerView.setLayoutManager(layout);
    }

    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 23;

    public void permissionStorageCheck(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(findViewById(R.id.fab), "É preciso acessar os arquivos para guardar as imagens", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                permissionStorage = false;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("embrapaStorePermission", false);
                editor.commit();
            } else {
                Log.e(">>>>","aaaaaa");
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else{
            permissionStorage = true;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("embrapaStorePermission", true);
            editor.commit();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        Log.e(">>>>","chamou");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    permissionStorage = true;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("embrapaStorePermission", true);
                    editor.commit();

                    Log.e(">>>>","deixou");

                } else {
                    Log.e(">>>>","NAO deixou");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}

package br.embrapa.embrapashare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity{


    //private String imagePath;
    //private String last_update;
    //private File image;
    DBController crud;
    boolean edited;
    long id;
    int culture;
    File imageFile;
    ImageView imageView;
    EditText dateView;
    EditText descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        crud = new DBController(getBaseContext());
        culture = 0;
        id = -1;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imageView = (ImageView)findViewById(R.id.edit_item_image);

        dateView = (EditText)findViewById(R.id.edit_item_date);
        descriptionView = (EditText)findViewById(R.id.edit_item_description);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int r = crud.updateDataByID(id, 1, getCurrentDateTime(), culture, dateView.getText().toString(), descriptionView.getText().toString(), null);
                Log.e(">>>"+id, " |"+r);
                setResult(RESULT_OK);
                finish();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.edit_item_culture);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                culture = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        if(getIntent().getIntExtra("requestCode",0) == REQUEST_CAPTURE_PHOTO){
            dispatchTakePictureIntent();
        }
        else if(getIntent().getIntExtra("requestCode",0) == REQUEST_LOAD_PHOTO){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.image_chooser)), REQUEST_LOAD_PHOTO);
        }
        else if(getIntent().getIntExtra("requestCode",0) == REQUEST_EDIT_PHOTO){
            id = Long.valueOf(getIntent().getStringExtra("registerID"));
            RegisteredItem data = crud.loadDataForEdit(id);

            imageFile = FileUtils.loadImageFile(data.getImageName());

            Glide.with(this).load(imageFile).override(150,150).centerCrop().into(imageView);
            dateView.setText(data.getDate());
            descriptionView.setText(data.getDescription());
            spinner.setSelection(data.getCulture());
        }
    }


    static final int REQUEST_CAPTURE_PHOTO = 1;
    static final int REQUEST_LOAD_PHOTO = 2;
    static final int REQUEST_EDIT_PHOTO = 3;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //camera
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            imageFile = FileUtils.createImageFile();

            if (imageFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "br.embrapa.embrapashare.fileprovider", imageFile);

                //permission to grant access to the file by cameras apps (android < 7)
                List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    this.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAPTURE_PHOTO);
            }
            else{
                //error
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //TODO flag pra ver se mudou mesmo
        int r = crud.updateDataByID(id, 0, getCurrentDateTime(), culture, dateView.getText().toString(), descriptionView.getText().toString(), null);
        Log.e(">>>"+id, " |"+r);
        setResult(RESULT_OK);
        finish();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //if (requestCode == REQUEST_CAPTURE_PHOTO)
            //  imageFile has already been instantiated

            if(requestCode == REQUEST_LOAD_PHOTO){
                try {
                    imageFile = FileUtils.copyInputStreamToFile(getContentResolver().openInputStream(data.getData()));
                } catch (IOException e) {
                    //erro nao copiou // TODO
                    e.printStackTrace();
                }
            }

            String[] exif = FileUtils.getExif(imageFile);
            id = crud.insertData(0, null, 0, exif[2], null, imageFile.getName(), null, exif[0], exif[1], exif[2]);
            if(id<0){
                //TODO tratar caso nao tenha adicionado no bd, deleta a imagem?
            }

            Glide.with(this).load(imageFile).override(150,150).centerCrop().into(imageView);
            if(exif[2] == "")
                dateView.setText(getCurrentDateTime());
            else dateView.setText(exif[2]);
        }
        else {
            //mensagem de erro //TODO
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            //deletar imagem
            //verificar se foi salvo antes
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setTitle(getString(R.string.delete_title))
                    .setMessage(getString(R.string.delete_message))
                    .setPositiveButton(getString(R.string.delete_accept), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            crud.deleteDataByID(getIntent().getStringExtra("registerID"));
                            setResult(RESULT_FIRST_USER);
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.delete_cancel), null)
                    .show();
        }
        else if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    public String getCurrentDateTime(){ //TODO colocar em um utils
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}

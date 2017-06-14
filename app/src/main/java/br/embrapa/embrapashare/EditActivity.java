package br.embrapa.embrapashare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private String imagePath;
    private String last_update;
    private File image;
    DBController crud;
    private int culture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        crud = new DBController(getBaseContext());

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DBController crud = new DBController(getBaseContext());

                String date = ((EditText)findViewById(R.id.edit_item_date)).getText().toString();
                //MUDAR PARA EVENTO String culture = ((Spinner)findViewById(R.id.edit_item_culture)).getSelectedItem().toString();
                String status = "Rascunho";
                String description = ((EditText)findViewById(R.id.edit_item_description)).getText().toString();
                String local = "????";


                if(getIntent().getIntExtra("requestCode",0) == REQUEST_EDIT_PHOTO) {
                    last_update = date;
                    crud.alterDataByID(getIntent().getStringExtra("registerID"), date, "" + culture, status, last_update, description, local);
                }
                else{
                    String result;
                    result = crud.insertData(date, "" + culture, status, last_update, description, imagePath, local);
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }




                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                setResult(RESULT_OK);
                finish();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.edit_item_culture);
        spinner.setOnItemSelectedListener(this);


        if(getIntent().getIntExtra("requestCode",0) == REQUEST_CAPTURE_PHOTO){
            dispatchTakePictureIntent();
        }
        else if(getIntent().getIntExtra("requestCode",0) == REQUEST_LOAD_PHOTO){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_LOAD_PHOTO);
        }
        else if(getIntent().getIntExtra("requestCode",0) == REQUEST_EDIT_PHOTO){
            String[] data = crud.loadDataForEdit(getIntent().getStringExtra("registerID"));

            ImageView imageView = (ImageView)findViewById(R.id.edit_item_image);
            EditText editText = (EditText)findViewById(R.id.edit_item_date);
            EditText editText1 = (EditText)findViewById(R.id.edit_item_description);
            Spinner spinner1 = (Spinner)findViewById(R.id.edit_item_culture);


            Glide.with(this).load(new File(Environment.getExternalStorageDirectory() + File.separator + "EmbrapaShare" + File.separator + data[3])).override(150,150).centerCrop().into(imageView);
            editText1.setText(data[5]);
            editText.setText(data[2]);
            spinner1.setSelection(Integer.parseInt(data[4]));
        }



        Log.e("end of create======","****************");
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }


    static final int REQUEST_CAPTURE_PHOTO = 1;
    static final int REQUEST_LOAD_PHOTO = 2;
    static final int REQUEST_EDIT_PHOTO = 3;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            Log.e("1======","****************");
            try {
                Log.e("2======","****************");
                photoFile = createImageFile();
                Log.e("3======","****************");
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("2E======","****************");

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.e("4======","****************");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "br.embrapa.embrapashare.fileprovider",
                        photoFile);


                List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    this.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAPTURE_PHOTO);
                Log.e("5======","****************");
            }
            else{
                //erro
                finish();
            }
        }
    }

    //String mCurrentPhotoPath;


    public void copyImageFile(String src) throws IOException {//ver se arquivo existe
        FileInputStream inStream = new FileInputStream(new File(src));

        ExifInterface exif = new ExifInterface(src);
        String datetime =  exif.getAttribute(ExifInterface.TAG_DATETIME);

        String timeStamp[];
        if(datetime != null) {
            timeStamp = datetime.split(":| ");
            if(timeStamp.length != 6)
                timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()).split("_");
        }
        else
            timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()).split("_");

//ver se a pasta existe

        last_update = timeStamp[0]+timeStamp[1]+timeStamp[2]+timeStamp[3]+timeStamp[4]+timeStamp[5];
        EditText editText = (EditText)findViewById(R.id.edit_item_date);
        editText.setText(timeStamp[2]+"/"+timeStamp[1]+"/"+timeStamp[0]+" "+timeStamp[3]+":"+timeStamp[4], TextView.BufferType.NORMAL);


        image = new File(Environment.getExternalStorageDirectory() + File.separator + "EmbrapaShare" + File.separator +
                last_update + "_" + (int)(Math.random() * 899999999) + ".jpg");

        FileOutputStream outStream = new FileOutputStream(image);



        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp[] = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()).split("_");
        last_update = timeStamp[0]+timeStamp[1]+timeStamp[2]+timeStamp[3]+timeStamp[4]+timeStamp[5];
        EditText editText = (EditText)findViewById(R.id.edit_item_date);
        editText.setText(timeStamp[2]+"/"+timeStamp[1]+"/"+timeStamp[0]+" "+timeStamp[3]+":"+timeStamp[4], TextView.BufferType.NORMAL);
        String imageFileName = last_update + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "EmbrapaShare");//getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
//padronizar e colocar numa constante o endereço das imagens
        /*File*/ image = null;
        if (success) {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            imagePath = image.getName();
        }

        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String copyImageFile(Uri uri){
return "";
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = (ImageView)findViewById(R.id.edit_item_image);
        if (requestCode == REQUEST_CAPTURE_PHOTO && resultCode == RESULT_OK) {
            Log.e("6======","****************");
            //Bundle extras = data.getExtras();
            Log.e("7======","****************");
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.e("8======","****************");

            /*if(imageBitmap.getHeight() >= imageBitmap.getWidth())
                imageView.setImageBitmap(imageBitmap.createBitmap(imageBitmap, 0, (imageBitmap.getHeight()-imageBitmap.getWidth())/2, imageBitmap.getWidth(), imageBitmap.getWidth()));
            else
                imageView.setImageBitmap(imageBitmap.createBitmap(imageBitmap, (imageBitmap.getWidth()-imageBitmap.getHeight())/2, 0, imageBitmap.getHeight(), imageBitmap.getHeight()));
            */
            Glide.with(this).load(image).override(150,150).centerCrop().into(imageView);


//guardar thumbnails
        }
        else if(requestCode == REQUEST_LOAD_PHOTO && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Log.e(">>>>>>>>path",getPathFromUri(imageUri));
            try {
                copyImageFile(getPathFromUri(imageUri));
                imagePath = image.getName();
                Glide.with(this).load(image).override(150,150).centerCrop().into(imageView);
            } catch (IOException e) {
                Log.e("NAO COPIOU","------------");
                e.printStackTrace();
            }
        }

        else
            finish();
    }


    private String getPathFromUri(Uri uri){//isso precisa de um try
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       this.culture = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

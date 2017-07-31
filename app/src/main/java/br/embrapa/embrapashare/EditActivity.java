package br.embrapa.embrapashare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EditActivity extends AppCompatActivity{


    //private String imagePath;
    //private String last_update;
    //private File image;
    DBController crud;
    //boolean edited_record;
    boolean edited_images;
    long id;
    String culture;
    String[] images_names;
    File imageFile;
    //ImageView imageView;
    TextView dateView;
    EditText descriptionView;
    InstantAutoComplete cultureView;
    Context context;

    private RecyclerView recyclerView;
    private LinkedList<ImageItem> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        context = this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_image_list);
        applyEmpty();

        crud = new DBController(getBaseContext());
        culture = "";
        id = -1;
        /*edited_record = */edited_images = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = (LayoutInflater) this
                    .getSystemService(this.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.autocomplete, null);

            actionBar.setCustomView(v);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, crud.loadCultures());

        cultureView = (InstantAutoComplete) findViewById(R.id.actv_culture);
        cultureView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        cultureView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                culture = cultureView.getText().toString();
            }
        });

        cultureView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                culture = cultureView.getText().toString();
            }
        });

        //cultureView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);



        //imageView = (ImageView)findViewById(R.id.edit_item_image);

        dateView = (TextView)findViewById(R.id.text_item_date);
        descriptionView = (EditText)findViewById(R.id.edit_item_description);

        /*descriptionView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Log.e("after"," ");
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("before>>>>"," "+after);
                if(count != 0)
                    edited_record = true;
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("on","  "+count);
            }
        });*/



        FloatingActionButton fabSend = (FloatingActionButton) findViewById(R.id.fab_send);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(culture.length() > 0){
                    //if(edited_images)
                    //    crud.updateImagesByRecID(id, images_names);
                    crud.updateDataByID(id, crud.STATUS_PENDING, getCurrentDateTime(), culture, dateView.getText().toString(), descriptionView.getText().toString(), null);
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    //cultureView.requestFocus();
                    cultureView.showDropDown();
                    showKeyboard();
                }

            }
        });

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence opts[] = new CharSequence[] {getString(R.string.add_camera), getString(R.string.add_chooser)};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getString(R.string.add_title));
                builder.setItems(opts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                dispatchTakeChooserIntent();
                                break;
                            default:
                        }
                    }
                });
                builder.show();
            }
        });


        if(getIntent().getIntExtra("requestCode",0) == REQUEST_CAPTURE_PHOTO){
            dispatchTakePictureIntent();
        }
        else if(getIntent().getIntExtra("requestCode",0) == REQUEST_LOAD_PHOTO){
            dispatchTakeChooserIntent();
        }
        else if(getIntent().getIntExtra("requestCode",0) == REQUEST_EDIT_PHOTO){
            id = Long.valueOf(getIntent().getStringExtra("registerID"));
            RegisteredItem data = crud.loadDataForEdit(id);

            //TODO imageFile = FileUtils.loadImageFile(data.getImageName());

            //Glide.with(this).load(imageFile).override(150,150).centerCrop().into(imageView);
            dateView.setText(data.getDate());
            descriptionView.setText(data.getDescription());
            cultureView.setText(data.getCulture());

            applyImages();



            //if(cultureView.getText().length() != 0)
                descriptionView.requestFocus();
            //else
            //    showKeyboard();
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

    private void dispatchTakeChooserIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.image_chooser)), REQUEST_LOAD_PHOTO);
    }

    @Override
    public void onBackPressed() {
        //if(edited_record || edited_images)
            crud.updateDataByID(id, crud.STATUS_DRAFT, getCurrentDateTime(), culture, dateView.getText().toString(), descriptionView.getText().toString(), null);
        setResult(RESULT_OK);
        finish();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //if (requestCode == REQUEST_CAPTURE_PHOTO)
            //  imageFile has already been instantiated

            if(requestCode == REQUEST_CAPTURE_PHOTO)
                FileUtils.deleteLastCapturedImage(this);

            if(requestCode == REQUEST_LOAD_PHOTO){
                try {
                    imageFile = FileUtils.copyInputStreamToFile(getContentResolver().openInputStream(data.getData()));
                } catch (IOException e) {
                    //erro nao copiou // TODO
                    e.printStackTrace();
                }
                cultureView.showDropDown();
            }

            dateView.setText(getCurrentDateTime());
            //String[] exif = FileUtils.getExif(imageFile);

            if(id<0) {
                id = crud.insertData(0, dateView.getText().toString(), "", dateView.getText().toString(), null, null, imageFile.getName());
                showKeyboard();
            }
            else {
                crud.insertImage(id, imageFile.getName());
                cultureView.dismissDropDown();
                descriptionView.requestFocus();
            }

            if(id<0){
                //TODO tratar caso nao tenha adicionado no bd, deleta a imagem?
            }

            //Glide.with(this).load(imageFile).override(150,150).centerCrop().into(imageView);
            applyImages();


        }
        else {
            //mensagem de erro //TODO
            if(id<0)
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

    public void showKeyboard(){
        //cultureView.requestFocus();
        //cultureView.showDropDown();

        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(cultureView, InputMethodManager.SHOW_IMPLICIT);
        imm.showSoftInput(cultureView, InputMethodManager.SHOW_FORCED);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void applyImages(){
        itens = crud.loadImagesForList(id);
        recyclerView.setAdapter(new ImageItemAdapter(itens, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void applyEmpty(){
        recyclerView.setAdapter(new ImageItemAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    //event from layout images_list
    public void deleteImage(final String selected_image_id) {
        if(itens.size() <= 1)
            return;
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setTitle(getString(R.string.delete_image_title))
                .setMessage(getString(R.string.delete_image_message))
                .setPositiveButton(getString(R.string.delete_accept),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            crud.deleteImagesByID(selected_image_id);
                            applyImages();
                        }
                    }
                )
                .setNegativeButton(getString(R.string.delete_cancel), null)
                .show();

    }
}

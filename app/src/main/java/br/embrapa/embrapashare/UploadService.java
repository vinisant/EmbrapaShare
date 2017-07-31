package br.embrapa.embrapashare;


import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Base64;
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
import java.util.Map;

public class UploadService extends IntentService {

    public UploadService() {
        super("UploadService");
    }

    public UploadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String dataString = intent.getDataString();

    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        //Toast.makeText(MainActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = null;
                try {
                    image = getStringImage(FileUtils.loadImageFile("20170724204151_1692923275.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Getting Image Name
                String name = "asdasd";//editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }


    public String getStringImage(File photoFile) throws IOException {
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = FileUtils.fullyReadFileToBytes(photoFile);
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String UPLOAD_URL ="http://ec2-18-231-76-79.sa-east-1.compute.amazonaws.com/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

}

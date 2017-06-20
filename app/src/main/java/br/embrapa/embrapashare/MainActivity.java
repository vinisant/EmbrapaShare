package br.embrapa.embrapashare;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean listAsGrid = false;
    private LinkedList<RegisteredItem> itens;
    private DBController crud;

    public Boolean getListAsGrid() {
        return listAsGrid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

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
        Intent intent = new Intent(this, EditActivity.class);
        if(requestCode == CAPTURE_PHOTO){
            intent.putExtra("requestCode", requestCode);
            startActivityForResult(intent, requestCode);
        }
        else if(requestCode == LOAD_PHOTO){
            intent.putExtra("requestCode", requestCode);
            startActivityForResult(intent, requestCode);
        }
    }
//TODO mudar para um listener
    public void editPost(View v) {
        Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();

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
            Snackbar.make(findViewById(R.id.fab), "Registro excluído", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

}

package br.embrapa.embrapashare;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;


public class RegisteredItemAdapter extends RecyclerView.Adapter {

    private List<RegisteredItem> itens;
    private Context context;
    private boolean grid;

    SimpleCursorAdapter mCursorAdapter;

    public RegisteredItemAdapter(List<RegisteredItem> itens, Context context, boolean isgrid) {
        this.itens = itens;
        this.context = context;
        this.grid = isgrid;
    }

    public RegisteredItemAdapter(Context context, Cursor c) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(grid){
            view = LayoutInflater.from(this.context).inflate(R.layout.registered_item_grid, parent, false);
        }
        else{
            view = LayoutInflater.from(this.context).inflate(R.layout.registered_item_linear, parent, false);
        }
        return new RegisteredItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RegisteredItemViewHolder holder = (RegisteredItemViewHolder) viewHolder;
        holder.description.setText(itens.get(position).getDescription());
        holder.status.setText(context.getResources().getStringArray(R.array.register_status)[itens.get(position).getStatus()]);
        holder.culture.setText(context.getResources().getStringArray(R.array.cultures_array)[itens.get(position).getCulture()]);
        holder.date.setText(itens.get(position).getDate());

        holder.content_box.setTag(itens.get(position).getRegisterIDString()); //colocar onclick aqui

        File imgFile = new File(Environment.getExternalStorageDirectory() + File.separator + "EmbrapaShare" + File.separator + itens.get(position).getImageName());

        if(imgFile.exists()){
            /*
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            if(myBitmap.getHeight() >= myBitmap.getWidth())
                holder.image.setImageBitmap(myBitmap.createBitmap(myBitmap, 0, (myBitmap.getHeight()-myBitmap.getWidth())/2, myBitmap.getWidth(), myBitmap.getWidth()));
            else
                holder.image.setImageBitmap(myBitmap.createBitmap(myBitmap, (myBitmap.getWidth()-myBitmap.getHeight())/2, 0, myBitmap.getHeight(), myBitmap.getHeight()));
            */

            Glide.with(context).load(imgFile).override(150,150).centerCrop().into(holder.image);


            //holder.image.setImageBitmap(myBitmap);

        }


    }

    @Override
    public int getItemCount() {
        return this.itens.size();
    }
}

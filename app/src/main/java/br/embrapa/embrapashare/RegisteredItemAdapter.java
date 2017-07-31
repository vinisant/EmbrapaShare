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
        holder.getDescription().setText(itens.get(position).getDescription());
        holder.getStatus().setText(context.getResources().getStringArray(R.array.register_status)[itens.get(position).getStatus()]);
        holder.getCulture().setText(itens.get(position).getCulture());
        holder.getDate().setText(itens.get(position).getDate());

        holder.getContentBox().setTag(itens.get(position).getIdString()); //colocar onclick aqui


        File imgFile = new File(Environment.getExternalStorageDirectory() + File.separator + "EmbrapaShare" + File.separator + itens.get(position).getFirstImage());

        if(imgFile.exists())
            Glide.with(context).load(imgFile).override(150,150).centerCrop().into(holder.getFirstImage());

    }

    @Override
    public int getItemCount() {
        return this.itens.size();
    }
}

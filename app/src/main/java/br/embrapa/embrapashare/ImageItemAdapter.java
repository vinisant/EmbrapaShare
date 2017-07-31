package br.embrapa.embrapashare;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;


public class ImageItemAdapter extends RecyclerView.Adapter {

    private List<ImageItem> itens;
    private Context context;


    public ImageItemAdapter(List<ImageItem> itens, Context context) {
        this.itens = itens;
        this.context = context;
    }

    public ImageItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.images_list, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ImageViewHolder holder = (ImageViewHolder) viewHolder;

        //holder.getImage().setTag(itens.get(position).getRecIdString());

        final String image_id = itens.get(position).getIdString();

        holder.getImage().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((EditActivity)context).deleteImage(image_id);
                return false;
            }
        });

        File imgFile = new File(Environment.getExternalStorageDirectory() + File.separator + "EmbrapaShare" + File.separator + itens.get(position).getImageName());
        Glide.with(context).load(imgFile).override(150,150).centerCrop().into(holder.getImage());
    }

    @Override
    public int getItemCount() {
        if(itens == null)
            return 0;
        return this.itens.size();
    }
}

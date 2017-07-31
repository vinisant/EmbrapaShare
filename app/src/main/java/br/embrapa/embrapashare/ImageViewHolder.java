package br.embrapa.embrapashare;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


public class ImageViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;

    public ImageViewHolder(View itemView) {
        super(itemView);
        this.image = (ImageView) itemView.findViewById(R.id.image_list_item);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}

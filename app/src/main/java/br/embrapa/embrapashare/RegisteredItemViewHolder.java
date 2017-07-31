package br.embrapa.embrapashare;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




public class RegisteredItemViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout content_box;
    private TextView description;
    private TextView status;
    private TextView date;
    private TextView culture;
    private ImageView first_image;

    public RegisteredItemViewHolder(View itemView) {
        super(itemView);
        this.content_box = (LinearLayout) itemView.findViewById(R.id.registered_item_content_box);
        this.description = (TextView) itemView.findViewById(R.id.registered_item_description);
        this.status = (TextView) itemView.findViewById(R.id.registered_item_status);
        this.date = (TextView) itemView.findViewById(R.id.registered_item_date);
        this.culture = (TextView) itemView.findViewById(R.id.registered_item_culture);
        this.first_image = (ImageView) itemView.findViewById(R.id.registered_item_image);
    }

    public LinearLayout getContentBox() {
        return content_box;
    }

    public void setContentBox(LinearLayout content_box) {
        this.content_box = content_box;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public TextView getStatus() {
        return status;
    }

    public void setStatus(TextView status) {
        this.status = status;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public TextView getCulture() {
        return culture;
    }

    public void setCulture(TextView culture) {
        this.culture = culture;
    }

    public ImageView getFirstImage() {
        return first_image;
    }

    public void setFirstImage(ImageView first_image) {
        this.first_image = first_image;
    }
}

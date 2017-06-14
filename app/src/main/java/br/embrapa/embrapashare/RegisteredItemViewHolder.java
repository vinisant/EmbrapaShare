package br.embrapa.embrapashare;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




public class RegisteredItemViewHolder extends RecyclerView.ViewHolder {

    LinearLayout content_box;
    TextView description;
    TextView status;
    TextView date;
    TextView culture;
    ImageView image;
    //mudar isso pra private

    public RegisteredItemViewHolder(View itemView) {
        super(itemView);
        this.content_box = (LinearLayout) itemView.findViewById(R.id.registered_item_content_box);
        this.description = (TextView) itemView.findViewById(R.id.registered_item_description);
        this.status = (TextView) itemView.findViewById(R.id.registered_item_status);
        this.date = (TextView) itemView.findViewById(R.id.registered_item_date);
        this.culture = (TextView) itemView.findViewById(R.id.registered_item_culture);
        this.image = (ImageView) itemView.findViewById(R.id.registered_item_image);
    }
}

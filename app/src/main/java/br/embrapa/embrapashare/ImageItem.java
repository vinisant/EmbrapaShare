package br.embrapa.embrapashare;


public class ImageItem {
    private long image_id;
    private long rec_id;
    private String image_name;


    public ImageItem(long image_id, long rec_id, String image_name) {
        this.image_id = image_id;
        this.rec_id = rec_id;
        this.image_name = image_name;
    }

    public long getRecId() {
        return rec_id;
    }



    public void setRecId(long rec_id) {
        this.rec_id = rec_id;
    }

    public long getImageId() {
        return image_id;
    }

    public String getIdString() {
        return Long.toString(image_id);
    }

    public void setImageId(long image_id) {
        this.image_id = image_id;
    }

    public String getImageName() {
        return image_name;
    }

    public void setImageName(String image_name) {
        this.image_name = image_name;
    }
}

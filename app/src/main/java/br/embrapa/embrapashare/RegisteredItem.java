package br.embrapa.embrapashare;


public class RegisteredItem {
    private long id;
    private int status;
    private String lastUpdate;
    private String culture;
    private String date;
    private String description;
    private String local;
    private String first_image;

    public RegisteredItem() {
        this.status = 0;
    }

    public RegisteredItem(long id, int status, String lastUpdate, String culture, String date, String description, String local, String first_image) {
        this.id = id;
        this.status = status;
        this.lastUpdate = lastUpdate;
        this.culture = culture;
        this.date = date;
        this.description = description;
        this.local = local;
        this.first_image = first_image;
    }

    public long getId() {
        return id;
    }

    public String getIdString() { return Long.toString(id); }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String last_update) {
        this.lastUpdate = last_update;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFirstImage() {
        return first_image;
    }

    public void setFirstImage(String first_image) {
        this.first_image = first_image;
    }


}


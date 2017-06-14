package br.embrapa.embrapashare;


public class RegisteredItem {

    private String registerID;
    private String status;
    private String date;
    private String imageName;
    private String culture;
    private String description;
    private String local;

    public RegisteredItem(String registerID, String status, String date, String imageName, String culture, String description) {
        this.registerID = registerID;
        this.status = status;
        this.date = date;
        this.imageName = imageName;
        this.culture = culture;
        this.description = description;
    }

    /*public RegisteredItem() {
        this.status = "status";
        this.date = "27/03/2017 16:55";
        this.imageName = "";
        this.culture = "Café";
        this.description = "aaaaaa aaa aaaaaaaaa aaa";
    }*/

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegisterID(String registerID) { this.registerID = registerID; }



    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getImageName() {
        return imageName;
    }

    public String getCulture() {
        return culture;
    }

    public String getDescription() {
        return description;
    }

    public String getRegisterID() { return registerID; }
}


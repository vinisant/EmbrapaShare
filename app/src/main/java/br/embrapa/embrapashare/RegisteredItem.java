package br.embrapa.embrapashare;


public class RegisteredItem {
//TODO mudar propriedades
    private long registerID;
    private int status;
    private String date;
    private String imageName;
    private int culture;
    private String description;
    private String local;

    public RegisteredItem(long registerID, int status, String date, String imageName, int culture, String description) {
        this.registerID = registerID;
        this.status = status;
        this.date = date;
        this.imageName = imageName;
        this.culture = culture;
        this.description = description;
    }

  public RegisteredItem() {}
     /*     this.status = "status";
        this.date = "27/03/2017 16:55";
        this.imageName = "";
        this.culture = "Café";
        this.description = "aaaaaa aaa aaaaaaaaa aaa";
    }*/

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setCulture(int culture) {
        this.culture = culture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegisterID(long registerID) { this.registerID = registerID; }



    public int getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getImageName() {
        return imageName;
    }

    public int getCulture() {
        return culture;
    }

    public String getDescription() {
        return description;
    }

    public long getRegisterID() { return registerID; }

    public String getRegisterIDString() { return Long.toString(registerID); }
}


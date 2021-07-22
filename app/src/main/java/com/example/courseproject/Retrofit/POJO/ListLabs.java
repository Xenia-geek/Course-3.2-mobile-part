package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListLabs {
    @SerializedName("IDLab")
    @Expose
    private int IDLab;
    @SerializedName("NameLab")
    @Expose
    private String NameLab;
    @SerializedName("Quantity")
    @Expose
    private int Quantity;
    @SerializedName("IDCource")
    @Expose
    private int IDCource;
    @SerializedName("IDSpeciality")
    @Expose
    private String IDSpeciality;
    @SerializedName("IDSem")
    @Expose
    private int IDSem;


    public int getIDLab() {
        return IDLab;
    }

    public void setIDLab(int IDLab) {
        this.IDLab = IDLab;
    }

    public String getNameLab() {
        return NameLab;
    }

    public void setNameLab(String NameLab) {

        this.NameLab = NameLab;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {

        this.Quantity = Quantity;
    }

    public int getIDCource() {
        return IDCource;
    }

    public void setIDCource(int IDCource) {

        this.IDCource = IDCource;
    }

    public String getIDSpeciality() {
        return IDSpeciality;
    }

    public void setIDSpeciality(String IDSpeciality) {
        this.IDSpeciality = IDSpeciality;
    }

    public int getIDSem() {
        return IDSem;
    }

    public void setIDSem(int IDSem) {

        this.IDSem = IDSem;
    }
}

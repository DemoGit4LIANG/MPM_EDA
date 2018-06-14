package com.liangtee.mpmeda.vo;

public class MetaData {

    private int ID = -1;

    private String classTag = null;

    private double[] properties = null;

    private boolean isClustered = false;

    public MetaData(int ID, double[] properties, String classTag) {
        this.ID = ID;
        this.properties = properties;
        this.classTag = classTag;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int iD) {
        this.ID = iD;
    }

    public String getClassTag() {
        return this.classTag;
    }

    public void setClassTag(String classTag) {
        this.classTag = classTag;
    }

    public double[] getProperties() {
        return this.properties;
    }

    public void setProperties(double[] properties) {
        this.properties = properties;
    }

    public boolean isClustered() {
        return this.isClustered;
    }

    public void setClustered(boolean isClustered) {
        this.isClustered = isClustered;
    }

    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) return false;
        return this.ID == ((MetaData) obj).ID;
    }

    public int hashCode() {
        return this.ID;
    }
}
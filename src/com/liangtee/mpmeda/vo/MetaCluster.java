package com.liangtee.mpmeda.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetaCluster implements Comparable<MetaCluster> {

    private int CID = -1;

    private int layer = -1;

    private Set<MetaData> elements = null;

    private List<Integer> subCluserIDs = null;

    private double stepLen = -1.0D;

    public MetaCluster(int layer) {
        this.layer = layer;
        this.elements = new HashSet();
        this.subCluserIDs = new ArrayList();
    }

    private double euclidDistance(MetaData d1, MetaData d2) {
        double[] properties_d1 = d1.getProperties();
        double[] properties_d2 = d2.getProperties();

        double tmpTot = 0.0D;

        for (int i = 0; i < properties_d1.length; i++) {
            tmpTot += Math.pow(properties_d1[i] - properties_d2[i], 2.0D);
        }

        return Math.sqrt(tmpTot);
    }

    public double getMax() {
        double max = -1.0D;
        for (MetaData data : this.elements) {
            double value = 0.0D;
            for (double property : data.getProperties()) {
                value += Math.pow(property, 2.0D);
            }
            value = Math.sqrt(value);
            if (value <= max) continue;
            max = value;
        }

        return max;
    }

    public double getMin() {
        double min = Double.MAX_VALUE;
        for (MetaData data : this.elements) {
            double value = 0.0D;
            for (double property : data.getProperties()) {
                value += Math.pow(property, 2.0D);
            }
            value = Math.sqrt(value);
            if (value >= min) continue;
            min = value;
        }

        return min;
    }

    public double ComputeStepLen() {
        double stepLen = Double.MAX_VALUE;
        for (MetaData data1 : this.elements) {
            for (MetaData data2 : this.elements) {
                if (!data1.equals(data2)) {
                    double dist = euclidDistance(data1, data2);
                    if (dist >= stepLen) continue;
                    stepLen = dist;
                }
            }

        }

        return stepLen;
    }

    public boolean isBelonging(MetaData data, double init_dist) {
        int n = 0;
        for (MetaData d : this.elements) {
            double dist = euclidDistance(d, data);
            if (dist > init_dist) continue;
            n++;
        }

        return n == this.elements.size();
    }

    public void addElements(Set<MetaData> elements) {
        this.elements.addAll(elements);
    }

    public int getCID() {
        return this.CID;
    }

    public void setCID(int cID) {
        this.CID = cID;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public Set<MetaData> getElements() {
        return this.elements;
    }

    public void setElements(Set<MetaData> elements) {
        this.elements = elements;
    }

    public List<Integer> getSubCluserIDs() {
        return this.subCluserIDs;
    }

    public void setSubCluserIDs(List<Integer> subCluserIDs) {
        this.subCluserIDs = subCluserIDs;
    }

    public double getStepLen() {
        return this.stepLen;
    }

    public void setStepLen(double stepLen) {
        this.stepLen = stepLen;
    }

    public boolean equals(Object obj) {
        if (getClass() != getClass()) return false;
        return this.CID == ((MetaCluster) obj).getCID();
    }

    public int hashCode() {
        return this.CID;
    }

    public int compareTo(MetaCluster metaCluster) {
        if (this.elements.size() > metaCluster.elements.size()) return -1;
        if (this.elements.size() < metaCluster.elements.size()) return 1;
        return -1;
    }
}
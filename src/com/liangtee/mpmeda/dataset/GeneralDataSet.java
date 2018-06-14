package com.liangtee.mpmeda.dataset;

import com.liangtee.mpmeda.vo.MetaData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class GeneralDataSet {

    private Map<Integer, MetaData> dataSet = null;

    private int classTagSeq = -1;

    public GeneralDataSet(String filePath, int classTagSeq) {
        this.classTagSeq = classTagSeq;
        this.dataSet = loadData(filePath, classTagSeq);
    }

    private Map<Integer, MetaData> loadData(String filePath, int classTagSeq) {
        BufferedReader in = null;
        Map dataSet = new HashMap();
        try {
            in = new BufferedReader(new FileReader(filePath));
            String line = null;
            int idx = 1;
            while ((line = in.readLine()) != null) {
                String[] det = line.split(",");
                if (classTagSeq != -1) {
                    double[] properties = new double[det.length - 1];
                    for (int i = 0; i < properties.length; i++) {
                        properties[i] = Double.parseDouble(det[i]);
                    }
                    MetaData data = new MetaData(idx, properties, det[classTagSeq]);
                    dataSet.put(Integer.valueOf(idx++), data);
                } else {
                    double[] properties = new double[det.length];
                    for (int i = 0; i < properties.length; i++) {
                        properties[i] = Double.parseDouble(det[i]);
                    }
                    MetaData data = new MetaData(idx, properties, null);
                    dataSet.put(Integer.valueOf(idx++), data);
                }
            }

            in.close();
            System.out.println("###### load data " + (idx - 1) + " lines...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataSet;
    }

    public Map<Integer, MetaData> getDataSet() {
        return this.dataSet;
    }

    public void setDataSet(Map<Integer, MetaData> dataSet) {
        this.dataSet = dataSet;
    }

    public int getClassTagSeq() {
        return this.classTagSeq;
    }

    public void setClassTagSeq(int classTagSeq) {
        this.classTagSeq = classTagSeq;
    }
}
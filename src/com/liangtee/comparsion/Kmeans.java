package com.liangtee.comparsion;

import com.liangtee.mpmeda.dataset.GeneralDataSet;
import com.liangtee.mpmeda.vo.MetaData;

import java.util.*;
import java.util.Map.Entry;

public class Kmeans {

    private GeneralDataSet g_dataSet = null;

    private int Kvalue = 0;

    public Kmeans(GeneralDataSet dataSet, int Kvalue) {
        this.g_dataSet = dataSet;
        this.Kvalue = Kvalue;
    }

    private double computeDistByCuclid(MetaData data1, MetaData data2) {
        double[] properties_data1 = data1.getProperties();
        double[] properties_data2 = data2.getProperties();
        double dist = 0.0D;

        for (int i = 0; i < properties_data1.length; i++) {
            dist += Math.pow(properties_data1[i] - properties_data2[i], 2.0D);
        }

        return Math.sqrt(dist);
    }

    private MetaData computeNewCentroid(List<MetaData> cluster) {
        double[] new_properties = new double[((MetaData) cluster.get(0)).getProperties().length];

        for (int i = 0; i < new_properties.length; i++) {
            for (MetaData data : cluster) {
                new_properties[i] += data.getProperties()[i];
            }
        }

        for (int i = 0; i < new_properties.length; i++) {
            new_properties[i] /= cluster.size();
        }

        return new MetaData(0, new_properties, "centroid");
    }

    public Map<Integer, List<MetaData>> launch_pro(int iter) {
        Map<Integer, List<MetaData>> results = new HashMap<Integer, List<MetaData>>();
        for (int i = 1; i <= this.Kvalue; i++) {
            results.put(i, new ArrayList<MetaData>());
        }

        List<MetaData> init_centroids = new ArrayList<MetaData>();
        List<MetaData> centroids = null;

        Random random = new Random();
        int centroid_elem_index;
        for (int i = 0; i < this.Kvalue; ) {
            centroid_elem_index = random.nextInt(this.g_dataSet.getDataSet().size());
            if (!init_centroids.contains(this.g_dataSet.getDataSet().get(centroid_elem_index))) {
                init_centroids.add(this.g_dataSet.getDataSet().get(centroid_elem_index));
                results.get(i + 1).add(this.g_dataSet.getDataSet().get(centroid_elem_index));
                i++;
            }

        }

        System.out.println("Initial centroids : ");
        for (MetaData i : init_centroids) {
            System.out.println("MetaData ID : " + i.getID());
        }

        for (Entry<Integer, MetaData> entry : this.g_dataSet.getDataSet().entrySet()) {
            if (!init_centroids.contains(entry.getValue())) {
                double minDist = Double.MAX_VALUE;
                int bestCentroidID = -1;
                for (int centroid_id = 0; centroid_id < init_centroids.size(); centroid_id++) {
                    MetaData centroid = init_centroids.get(centroid_id);
                    double dist = computeDistByCuclid(centroid, (MetaData) entry.getValue());
                    if (dist < minDist) {
                        bestCentroidID = centroid_id + 1;
                    }
                }
                results.get(bestCentroidID).add(entry.getValue());
            }

        }

        double E1 = 0.0D;
        double E2 = 0.0D;
        for (int i = 0; i < iter; i++) {
            centroids = new ArrayList<MetaData>();
            for (int idx = 1; idx <= this.Kvalue; idx++) {
                List<MetaData> cluster = results.get(idx);
                MetaData newCentroid = computeNewCentroid(cluster);
                centroids.add(newCentroid);
            }

            results = new HashMap<Integer, List<MetaData>>();
            for (int k = 1; k <= this.Kvalue; k++) {
                results.put(k, new ArrayList<MetaData>());
            }

            for (Iterator<Entry<Integer, MetaData>> iterator = this.g_dataSet.getDataSet().entrySet().iterator(); iterator.hasNext(); ) {
                Entry<Integer, MetaData> entry = iterator.next();
                double minDist = Double.MAX_VALUE;
                int bestCentroidID = -1;
                for (int j = 0; j < centroids.size(); j++) {
                    MetaData centroid = centroids.get(j);
                    double dist = computeDistByCuclid(centroid, entry.getValue());
                    if (dist < minDist) {
                        minDist = dist;
                        bestCentroidID = j + 1;
                    }
                }

                results.get(bestCentroidID).add(entry.getValue());
                E1 += minDist;
            }

        }

        for (Entry<Integer, List<MetaData>> entry : results.entrySet()) {
            for (MetaData metaData : entry.getValue()) {
                System.out.println("Cluster ID : " + entry.getKey() + " element ID : " + metaData.getID() + " classTag :" + metaData.getClassTag());
            }
        }

        return results;
    }
}
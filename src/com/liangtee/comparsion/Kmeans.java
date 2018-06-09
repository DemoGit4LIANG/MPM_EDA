package com.liangtee.comparsion;

import com.liangtee.mpmeda.dataset.GeneralDataSet;
import com.liangtee.mpmeda.vo.MetaData;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Kmeans
{
  private GeneralDataSet g_dataSet = null;

  private int Kvalue = 0;

  public Kmeans(GeneralDataSet dataSet, int Kvalue) {
    this.g_dataSet = dataSet;
    this.Kvalue = Kvalue;
  }

  private double computeDistByCuclid(MetaData data1, MetaData data2)
  {
    double[] properties_data1 = data1.getProperties();
    double[] properties_data2 = data2.getProperties();
    double dist = 0.0D;

    for (int i = 0; i < properties_data1.length; i++) {
      dist += Math.pow(properties_data1[i] - properties_data2[i], 2.0D);
    }

    return Math.sqrt(dist);
  }

  private MetaData computeNewCentroid(List<MetaData> cluster) {
    double[] new_properties = new double[((MetaData)cluster.get(0)).getProperties().length];

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

  public Map<Integer, List<MetaData>> launch_pro(int iter)
  {
    Map results = new HashMap();
    for (int i = 1; i <= this.Kvalue; i++) {
      results.put(Integer.valueOf(i), new ArrayList());
    }

    List init_centroids = new ArrayList();
    List centroids = null;

    Random random = new Random();
    int centroid_elem_index;
    for (int i = 0; i < this.Kvalue; ) {
      centroid_elem_index = random.nextInt(this.g_dataSet.getDataSet().size());
      if (!init_centroids.contains(this.g_dataSet.getDataSet().get(Integer.valueOf(centroid_elem_index)))) {
        init_centroids.add((MetaData)this.g_dataSet.getDataSet().get(Integer.valueOf(centroid_elem_index)));
        ((List)results.get(Integer.valueOf(i + 1))).add((MetaData)this.g_dataSet.getDataSet().get(Integer.valueOf(centroid_elem_index)));
        i++;
      }

    }

    System.out.println("Initial centroids : ");
    for (MetaData i : init_centroids) {
      System.out.println("MetaData ID : " + i.getID());
    }

    for (Map.Entry entry : this.g_dataSet.getDataSet().entrySet()) {
      if (!init_centroids.contains(entry.getValue())) {
        double minDist = 1.7976931348623157E+308D;
        int bestCentroidID = -1;
        for (int centroid_id = 0; centroid_id < init_centroids.size(); centroid_id++) {
          MetaData centroid = (MetaData)init_centroids.get(centroid_id);
          double dist = computeDistByCuclid(centroid, (MetaData)entry.getValue());
          if (dist < minDist) {
            bestCentroidID = centroid_id + 1;
          }
        }
        ((List)results.get(Integer.valueOf(bestCentroidID))).add((MetaData)entry.getValue());
      }

    }

    double E1 = 0.0D; double E2 = 0.0D;
    Map.Entry entry;
    double minDist;
    for (int i = 0; i < iter; i++)
    {
      centroids = new ArrayList();
      for (int idx = 1; idx <= this.Kvalue; idx++) {
        cluster = (List)results.get(Integer.valueOf(idx));
        MetaData newCentroid = computeNewCentroid(cluster);
        centroids.add(newCentroid);
      }

      results = new HashMap();
      for (int k = 1; k <= this.Kvalue; k++) {
        results.put(Integer.valueOf(k), new ArrayList());
      }

      for (List cluster = this.g_dataSet.getDataSet().entrySet().iterator(); cluster.hasNext(); ) { entry = (Map.Entry)cluster.next();
        minDist = 1.7976931348623157E+308D;
        int bestCentroidID = -1;
        for (int j = 0; j < centroids.size(); j++) {
          MetaData centroid = (MetaData)centroids.get(j);
          double dist = computeDistByCuclid(centroid, (MetaData)entry.getValue());
          if (dist < minDist) {
            minDist = dist;
            bestCentroidID = j + 1;
          }
        }

        ((List)results.get(Integer.valueOf(bestCentroidID))).add((MetaData)entry.getValue());
        E1 += minDist;
      }

    }

    for (Map.Entry entry : results.entrySet()) {
      for (MetaData i : (List)entry.getValue()) {
        System.out.println("Cluster ID : " + entry.getKey() + " element ID : " + i.getID() + " classTag :" + i.getClassTag());
      }
    }

    return results;
  }
}
package com.liangtee.mpmeda;

import com.liangtee.mpmeda.dataset.GeneralDataSet;
import com.liangtee.mpmeda.vo.MetaCluster;
import com.liangtee.mpmeda.vo.MetaData;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MPMEDA
{
  private GeneralDataSet dataSet = null;
  private double init_dist;
  private int lamda;
  private Map<Integer, List<MetaCluster>> mapTree = new HashMap();

  private int layer = 1;

  public MPMEDA(GeneralDataSet dataSet, double init_dist, int lamda) {
    this.dataSet = dataSet;
    this.init_dist = init_dist;
    this.lamda = lamda;
  }

  private double computeDistBetweenVectors(double[] v1, double[] v2)
  {
    double dist = 0.0D;
    for (int i = 0; (i < v1.length) && (i < v2.length); i++) {
      dist += Math.pow(v1[i] - v2[i], 2.0D);
    }

    return Math.sqrt(dist);
  }

  private double[] getME(MetaCluster cluster, int dim) {
    double[] ME = new double[dim];
    for (int i = 0; i < ME.length; i++) {
      for (MetaData data : cluster.getElements()) {
        ME[i] += data.getProperties()[i];
      }
      ME[i] /= cluster.getElements().size();
    }

    return ME;
  }

  public Map<Integer, List<MetaCluster>> launch_pro2(double dist_threshold, int data_dim)
  {
    List floorLayer = new ArrayList();

    for (Map.Entry entry : this.dataSet.getDataSet().entrySet()) {
      MetaData data = (MetaData)entry.getValue();
      if (data.isClustered()) {
        continue;
      }
      if (floorLayer.size() > 0) {
        for (int i = 0; i < floorLayer.size(); i++) {
          MetaCluster cluster = (MetaCluster)floorLayer.get(i);
          if (cluster.isBelonging(data, this.init_dist)) {
            cluster.getElements().add(data);
            data.setClustered(true);
            break;
          }
        }
        if (!data.isClustered()) {
          MetaCluster newCluster = new MetaCluster(this.layer);
          newCluster.setCID(floorLayer.size() + 1);
          newCluster.getElements().add(data);
          data.setClustered(true);
          floorLayer.add(newCluster);
        }
      } else {
        MetaCluster newCluster = new MetaCluster(this.layer);
        newCluster.setCID(1);
        newCluster.getElements().add(data);
        data.setClustered(true);
        floorLayer.add(newCluster);
      }
    }
    try
    {
      Collections.sort(floorLayer);
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.mapTree.put(Integer.valueOf(this.layer), floorLayer);

    System.out.println("### inital clustering finished ... ###");

    Set passedClusterIDs = new HashSet();
    int clusterNum = floorLayer.size();

    int preCurrentLayerClusterNum = 0;
    while (true)
    {
      List newLayerClusters = new ArrayList();
      List currentLayerClusters = (List)this.mapTree.get(Integer.valueOf(this.layer));

      if (currentLayerClusters.size() < 2) {
        break;
      }
      if (preCurrentLayerClusterNum == currentLayerClusters.size())
      {
        break;
      }
      preCurrentLayerClusterNum = currentLayerClusters.size();

      for (int i = 0; i < currentLayerClusters.size(); i++) {
        if (!passedClusterIDs.contains(Integer.valueOf(((MetaCluster)currentLayerClusters.get(i)).getCID()))) {
          MetaCluster cluster = (MetaCluster)currentLayerClusters.get(i);

          double[] ME = getME(cluster, data_dim);

          double min_dist = 1.7976931348623157E+308D;
          int mostSimilarClusterID = 0;

          for (int j = 0; j < currentLayerClusters.size(); j++) {
            if ((i != j) && (!passedClusterIDs.contains(Integer.valueOf(((MetaCluster)currentLayerClusters.get(j)).getCID())))) {
              double[] ME2 = getME((MetaCluster)currentLayerClusters.get(j), data_dim);

              double dist = computeDistBetweenVectors(ME, ME2);

              if (dist < min_dist) {
                min_dist = dist;
                mostSimilarClusterID = j;
              }
            }

          }

          if (min_dist <= dist_threshold) {
            MetaCluster mostSimilarCluster = (MetaCluster)currentLayerClusters.get(mostSimilarClusterID);
            clusterNum++;

            MetaCluster newCluster = new MetaCluster(this.layer + 1);
            newCluster.setCID(clusterNum);
            newCluster.addElements(cluster.getElements());
            newCluster.addElements(mostSimilarCluster.getElements());
            newCluster.getSubCluserIDs().add(Integer.valueOf(cluster.getCID()));
            newCluster.getSubCluserIDs().add(Integer.valueOf(mostSimilarCluster.getCID()));
            newLayerClusters.add(newCluster);
            passedClusterIDs.add(Integer.valueOf(cluster.getCID()));
            passedClusterIDs.add(Integer.valueOf(mostSimilarCluster.getCID()));
          } else {
            newLayerClusters.add(cluster);
          }
        }
      }
      try
      {
        Collections.sort(newLayerClusters);
      } catch (Exception e) {
        e.printStackTrace();
      }

      this.mapTree.put(Integer.valueOf(++this.layer), newLayerClusters);
    }

    System.out.println("###### finished ######");
    return this.mapTree;
  }
}
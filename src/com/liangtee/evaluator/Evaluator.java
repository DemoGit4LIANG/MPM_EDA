package com.liangtee.evaluator;

import com.liangtee.mpmeda.vo.MetaCluster;
import com.liangtee.mpmeda.vo.MetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Evaluator
{
  public static List<MetaCluster> switcher(Map<Integer, List<MetaData>> clusters)
  {
    List clusterList = new ArrayList();
    int idx = 1;
    for (Map.Entry entry : clusters.entrySet()) {
      List data = (List)entry.getValue();
      Set set = new HashSet();
      for (MetaData d : data) {
        set.add(d);
      }
      MetaCluster cluster = new MetaCluster(0);
      cluster.setCID(idx++);
      cluster.addElements(set);
      clusterList.add(cluster);
    }

    return clusterList;
  }

  public static double evaluateResu(List<MetaCluster> clusters) {
    int totElementNum = 0;
    double totErrorNum = 0.0D;
    Map correctClassTags = new HashMap();

    for (MetaCluster cluster : clusters) {
      Map classMap = new HashMap();
      int clusterElementNum = 0;
      for (MetaData data : cluster.getElements()) {
        totElementNum++;
        clusterElementNum++;
        if (classMap.containsKey(data.getClassTag()))
          classMap.put(data.getClassTag(), Integer.valueOf(((Integer)classMap.get(data.getClassTag())).intValue() + 1));
        else {
          classMap.put(data.getClassTag(), Integer.valueOf(1));
        }
      }

      String correctClassTag = null;
      int correctNum = 0;
      for (Map.Entry entry : classMap.entrySet()) {
        if (((Integer)entry.getValue()).intValue() > correctNum) {
          correctNum = ((Integer)entry.getValue()).intValue();
          correctClassTag = (String)entry.getKey();
        }
      }
      correctClassTags.put(Integer.valueOf(cluster.getCID()), correctClassTag);

      totErrorNum += clusterElementNum - correctNum;
    }

    return totErrorNum / totElementNum;
  }
}
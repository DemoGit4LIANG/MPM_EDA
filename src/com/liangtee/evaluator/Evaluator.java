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

public class Evaluator {

    public static List<MetaCluster> switcher(Map<Integer, List<MetaData>> clusters) {
        List<MetaCluster> clusterList = new ArrayList<MetaCluster>();
        int idx = 1;
        for (Entry<Integer, List<MetaData>> entry : clusters.entrySet()) {
            List<MetaData> data = entry.getValue();
            Set<MetaData> set = new HashSet<MetaData>();
            for (MetaData metaData : data) {
                set.add(metaData);
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
//        Map<String, Integer> correctClassTags = new HashMap<String, Integer>();

        for (MetaCluster cluster : clusters) {
            Map<String, Integer> classMap = new HashMap<String, Integer>();
            int clusterElementNum = 0;
            for (MetaData data : cluster.getElements()) {
                totElementNum++;
                clusterElementNum++;
                if (classMap.containsKey(data.getClassTag()))
                    classMap.put(data.getClassTag(), classMap.get(data.getClassTag()) + 1);
                else {
                    classMap.put(data.getClassTag(), 1);
                }
            }

            String correctClassTag = null;
            int correctNum = 0;
            for (Entry<String, Integer> entry : classMap.entrySet()) {
                if (entry.getValue() > correctNum) {
                    correctNum = entry.getValue();
                    correctClassTag = entry.getKey();
                }
            }
//            correctClassTags.put(cluster.getCID(), correctClassTag);

            totErrorNum += clusterElementNum - correctNum;
        }

        return totErrorNum / totElementNum;
    }
}
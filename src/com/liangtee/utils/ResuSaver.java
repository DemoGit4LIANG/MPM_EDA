package com.liangtee.utils;

import com.liangtee.mpmeda.vo.MetaCluster;
import com.liangtee.mpmeda.vo.MetaData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class ResuSaver {

    public static void translateFile(String filePath) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            bw = new BufferedWriter(new FileWriter("d:\\yeast2.data"));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] det = line.split("  ");
                StringBuffer sb = new StringBuffer("");
                sb.append(det[1].trim()).append(",").append(det[2]).append(",").append(det[3]).append(",").append(det[4]).append(",").append(det[5]).append(",").append(det[6]).append(",").append(det[7]).append(",").append(det[8]).append(",").append(det[9]);
                bw.write(sb.toString());
                bw.newLine();
            }

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAsCSVFile(List<MetaCluster> clusterList, String savePath) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(savePath));
            for (MetaCluster cluster : clusterList) {
                for (MetaData data : cluster.getElements()) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Cluster ID : ").append(cluster.getCID()).append(", Element ID : ").append(data.getID()).append(", classTag : ").append(data.getClassTag());
                    out.write(sb.toString());
                    out.newLine();
                }
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.gmail.markushygedombrowski.utils;

import com.gmail.markushygedombrowski.HLvagt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    File generalReports;
    File vagtdeathlog;
    File vagtcellelog;
    private HLvagt plugin;
    public Logger(HLvagt plugin) {
        this.plugin = plugin;
        System.out.println(plugin.getDataFolder()+"");
        generalReports = new File(plugin.getDataFolder() + "/reports.txt");
        vagtdeathlog = new File(plugin.getDataFolder() + "/vagtdeathlog.txt");
        vagtcellelog = new File(plugin.getDataFolder() + "/vagtcellelog.txt");
    }

    public void addMessage(String message,String fileName){
        if(fileName.equalsIgnoreCase("generalReports")){
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(generalReports, true));
                bw.append(message);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(fileName.equalsIgnoreCase("vagtdeathlog")){
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(vagtdeathlog, true));
                bw.append(message);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(fileName.equalsIgnoreCase("vagtcellelog")){
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(vagtcellelog, true));
                bw.append(message);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void setup(){
        if(!generalReports.exists()){
            try {
                generalReports.createNewFile();
                vagtdeathlog.createNewFile();
                vagtcellelog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void formatMessage(String msg, String name, String fileName){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
        String date = dateFormat.format(new Date());
        String finalMessage = date + " - "+ name + ": " + msg + "\n";
        addMessage(finalMessage,fileName);
    }
}

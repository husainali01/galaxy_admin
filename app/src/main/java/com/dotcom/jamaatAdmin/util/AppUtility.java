package com.dotcom.jamaatAdmin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by anjanik on 12/5/16.
 */
public class AppUtility{
    public static final String decode(final String in)
    {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while(index > -1)
        {
            int length = working.length();
            if(index > (length-6))break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring,16);
            String stringStart = working.substring(0, index);
            String stringEnd   = working.substring(numFinish);
            working = stringStart + ((char)number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }
    public static final String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getDateTime(long timeStamp)
    {
        Timestamp timestamp = new Timestamp(timeStamp*1000L);
        Date date = new Date(timestamp.getTime());
        //Date date = new Date(timestamp.getTime());
        // S is the millisecond
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        simpleDateFormat.setTimeZone(tz);
        return simpleDateFormat.format(date);
    }
    public static String getDate(long timeStamp)
    {
        Timestamp timestamp = new Timestamp(timeStamp*1000L);
        Date date = new Date(timestamp.getTime());
        // S is the millisecond
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        simpleDateFormat.setTimeZone(tz);
        return simpleDateFormat.format(date);
    }
    public static Map<String,String> getFilterMap(){
        Map<String,String> stringStringMap =  new HashMap<String,String>();
        String ventureName = SharedPreferencesManager.getStringPreference("venture",null);
        String leadInvestor = SharedPreferencesManager.getStringPreference("leadInvestor",null);
        String entrepreneur = SharedPreferencesManager.getStringPreference("entrepreneur",null);
        String assigneeName = SharedPreferencesManager.getStringPreference("assigneeName",null);
        String sponsored = SharedPreferencesManager.getStringPreference("sponsored",null);
        String cityName = SharedPreferencesManager.getStringPreference("city",null);
        String sector = SharedPreferencesManager.getStringPreference("sector", null);
        String workFlowStep = SharedPreferencesManager.getStringPreference("workFlowStep", null);
        String currency = SharedPreferencesManager.getStringPreference("currency",null);

        if(ventureName!= null && ventureName.length()>0){
            stringStringMap.put("ventureName",ventureName);
        }
        if(entrepreneur!= null && entrepreneur.length()>0){
            stringStringMap.put("entrepreneur",entrepreneur);
        }
        if(leadInvestor!=null && leadInvestor.length()>0){
            stringStringMap.put("leadInvestor",leadInvestor);
        }
        if(sponsored!=null && sponsored.length()>0){
            stringStringMap.put("isSponsered",sponsored);
        }
        if (currency != null && currency.length() > 0) {
            stringStringMap.put("currency", currency);
            SharedPreferencesManager.setPreference("currency", currency);
        }
        if(assigneeName!=null && assigneeName.length()>0){
            stringStringMap.put("assignee",assigneeName);
        }
        if(sector!= null && sector.length()>0){
            stringStringMap.put("sector",sector);
        }
        if(workFlowStep!= null && workFlowStep.length()>0){
            stringStringMap.put("workFlowStep",workFlowStep);
        }
        if(cityName!= null && cityName.length()>0) {
            stringStringMap.put("city", cityName);
        }
        return stringStringMap;
    }
    public static Map<String,String> getCommittedFilterMap(){
        Map<String,String> stringStringMap =  new HashMap<String,String>();
        String investor = SharedPreferencesManager.getStringPreference("committedInvestor",null);
        String lead = SharedPreferencesManager.getStringPreference("lead",null);

        if(investor!= null && investor.length()>0){
            stringStringMap.put("investor",investor);
        }
        if(lead!= null && lead.length()>0){
            stringStringMap.put("isLead",lead);
        }
        return stringStringMap;
    }
    public static Map<String,String> getCommitmentFilterMap(){
        Map<String,String> stringStringMap =  new HashMap<String,String>();
        String investor = SharedPreferencesManager.getStringPreference("investor_name",null);
        String filterBy = SharedPreferencesManager.getStringPreference("filterBy",null);

        if(investor!= null && investor.length()>0){
            stringStringMap.put("investorName",investor);
        }
        if(filterBy!= null && filterBy.length()>0){
            stringStringMap.put("filterBy",filterBy);
        }
        return stringStringMap;
    }
    public static String getUrl(String mUrl,Map<String,String> formattedJson) {
        StringBuilder stringBuilder = new StringBuilder(mUrl);
        int i = 1;
        for (Map.Entry<String,String> entry: formattedJson.entrySet()) {
            String key;
            String value;
            try {
                key = URLEncoder.encode(entry.getKey(), "UTF-8");
                value = URLEncoder.encode(entry.getValue(), "UTF-8");
//                if(i == 1) {
//                    stringBuilder.append( key + "=" + value);
//                } else {
                    stringBuilder.append("&" + key + "=" + value);
//                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;

        }
        String url = stringBuilder.toString();

        return url;
    }
    public static String getUrlForFilter(String mUrl,Map<String,String> formattedJson) {
        StringBuilder stringBuilder = new StringBuilder(mUrl);
        int i = 1;
        for (Map.Entry<String,String> entry: formattedJson.entrySet()) {
            String key;
            String value;
            try {
                key = URLEncoder.encode(entry.getKey(), "UTF-8");
                value = URLEncoder.encode(entry.getValue(), "UTF-8");
                stringBuilder.append("&" + key + "=" + value);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;

        }
        String url = stringBuilder.toString();

        return url;
    }
    public static String getArabicNumbers(String englishNumber){
        String arabicNumbers = englishNumber.replaceAll("1","١").replaceAll("2","٢").replaceAll("3","٣").replaceAll("4","٤").replaceAll("5","٥").replaceAll("6","٦").replaceAll("7","٧").replaceAll("8","٨").replaceAll("9","٩");
        return arabicNumbers;
    }
}

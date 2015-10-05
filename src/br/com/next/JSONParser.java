package br.com.next;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
  
public class JSONParser{
  
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
     
    static InputStream iStream = null;
    static JSONArray jarray = null;
    //static String json = "";
  
    // constructor
    public JSONParser() {
  
    }
    
    
    public static String makePOST(String url_string, String method, String data){
        String resposta = "";
        
        HttpURLConnection con = null;
        try {
        	 URL  url = new URL(url_string);
           con = (HttpURLConnection) url.openConnection();
           con.setRequestMethod(method);
           con.setRequestProperty("Content-Type", "application/json");
           if(data!=null){
        	   con.setRequestProperty("Content-Length", new StringBuilder().append(data.length()).toString());
           }
        
           con.setRequestProperty("Connection", "Keep-alive");
           con.setRequestProperty("Accept-Language", "UTF-8");
           con.setRequestProperty("User-Agent", "Android Client");
           con.setRequestProperty("accept", "*/*");
           
            con.setDoOutput(true);   
            con.setDoInput(true);
           con.setRequestProperty("Content-Type", "application/json");
           con.setUseCaches(false);  
         
            con.setRequestProperty("Accept", "application/json");
            con.connect();
            //Write
            OutputStream os = con.getOutputStream();
            if(data!=null){
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();
            }
       
            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            con.getResponseCode();
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            resposta = sb.toString();
      //      Log.i(TAG, "resposta sb,toString () " + resposta);
            int responseCode = con.getResponseCode();
         //   Log.i(TAG, method + " :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
            //    Log.i(TAG, response.toString());
                resposta = response.toString();
            } else {
                resposta ="";
             //   Log.i(TAG, "URL =" + url_string + " mehtod = " +   method + " nao foi encontrado!!!");
            }
        } catch (IOException e) {
            if(con != null)  con.disconnect();

          //  Log.i(TAG, "ERRO " + e.getMessage());
            e.printStackTrace();
        }
        if(con != null)  con.disconnect();
        return(resposta);
    }



}

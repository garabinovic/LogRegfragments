package fragments.android.exemple.com.logregfragments;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Korisnik on 22.11.2016..
 */

public class FetchingJsonData {

    private String address;

    public FetchingJsonData(String address) {
        this.address = address;
    }

    public String postRequest(String postDataBody){
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", postDataBody);

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            if (postDataBody!=null){
                wr.writeBytes(postDataBody);
                wr.flush();
                wr.close();
            }

            InputStream inputStream = null;
            try{
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                inputStream = connection.getErrorStream();
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String input;
            while ((input = bufferedReader.readLine()) != null){
                responseStrBuilder.append(input);
            }
            bufferedReader.close();
            String fjd = responseStrBuilder.toString();

            return fjd;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String putRequest(String postDataBody){
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            if (postDataBody!=null){
                wr.writeBytes(postDataBody);
                wr.flush();
                wr.close();
            }

            InputStream inputStream = null;
            try{
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                inputStream = connection.getErrorStream();
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String input;
            while ((input = bufferedReader.readLine()) != null){
                responseStrBuilder.append(input);
            }
            bufferedReader.close();
            String fjd = responseStrBuilder.toString();

            return fjd;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRequest(){
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = null;
            try{
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                inputStream = connection.getErrorStream();
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String input;
            while ((input = bufferedReader.readLine()) != null){
                responseStrBuilder.append(input);
            }
            bufferedReader.close();
            String fjd = responseStrBuilder.toString();

            return fjd;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

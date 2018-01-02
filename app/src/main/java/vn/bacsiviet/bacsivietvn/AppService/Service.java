package vn.bacsiviet.bacsivietvn.AppService;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 14/12/2017.
 */

public class Service {
    private static int timeout = 30000;

    private boolean isSuccess = false;
    private String message = "";
    private int statusCode;
    private String data;
    private HttpURLConnection conn;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getData() {
        return data;
    }

    public Service(){}

    public Service(String url) {
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Authorization", "bearer ");
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            conn.setDoInput(true);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("log_url", url);
    }

    public Service(String url, String token) {
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Authorization", "bearer " + token);
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            conn.setDoInput(true);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("log_url", url);
    }

    public void get() {
        try {
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();

                is.close();
                data = builder.toString();
                isSuccess = true;
            }
            message = conn.getResponseMessage();
            Log.d("log_da", message);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("log_da", e.getMessage().toString());
        }
    }

    public void get(String url, String token) {
        Log.d("log_url", url);
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Authorization", "bearer " + token);
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");


            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();

                is.close();
                data = builder.toString();
                isSuccess = true;
            }
            message = conn.getResponseMessage();
            Log.d("log_da", message + " - "+ conn.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("log_er", e.getMessage().toString());
        }
    }

    public void post(String req) {
        try {
            conn.setDoOutput(true); // set true để write request
            conn.setRequestMethod("POST"); // Phải được gọi trước khi write request
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(req);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                isSuccess = true;

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();
                is.close();

                data = builder.toString();
            }
            message = conn.getResponseMessage();
            statusCode = conn.getResponseCode();
            Log.e("log_ms", statusCode + " - " + message + " - " + req);
            Log.e("log_conten", conn.getContent().toString() + " data:" + data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }
}

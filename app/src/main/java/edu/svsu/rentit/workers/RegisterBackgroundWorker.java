package edu.svsu.rentit.workers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

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
import java.net.URLEncoder;

import edu.svsu.rentit.activities.LoginActivity;

public class RegisterBackgroundWorker extends AsyncTask<String, String, String> {

    Context context;


    public RegisterBackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        URL url = null;
        HttpURLConnection httpURLConnection;
        String HttpURL = "http://18.224.109.190/rentit/scripts/register.php";

        try {
            url = new URL(HttpURL);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }

        try {

            String firstname = params[0];
            String lastname = params[1];
            String username = params[2];
            String password = params[3];

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8") + "&"
                    + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(lastname, "UTF-8") + "&"
                    + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                    + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            httpURLConnection.connect();
        }catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.toString();
        }

        try {
            int response_code = httpURLConnection.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();

                return result;

            }
            else {
                return("Connection error");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            httpURLConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            JSONObject response = new JSONObject(result);

            int success = response.getInt("success");
            String message = response.getString("message");


            if (success == 1) {
                context.startActivity(new Intent(context, LoginActivity.class));
            } else {
                Toast toast = Toast.makeText(context,
                        message,
                        Toast.LENGTH_SHORT);

                toast.show();
            }

        } catch (Exception e) {

        }


    }
}
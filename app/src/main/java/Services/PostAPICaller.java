package Services;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.function.Function;

public class PostAPICaller extends AsyncTask<String, String, String> {

    String result = "";


    public PostAPICaller()
    {
    }

    public String getResult() {
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params){
        String urlString = params[0]; // URL to call
        String data = params[1]; //data to post
        String responseMessage = "not set";
        HttpURLConnection httpURLConnection =null;
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("body", data);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("AppUser-Agent", "Mozilla/5.0");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;");

            byte[] outputInBytes = data.getBytes("UTF-8");
            OutputStream os = httpURLConnection.getOutputStream();
            os.write( outputInBytes );
            os.close();

            httpURLConnection.connect();
            responseMessage = getOutputUrlConnection(httpURLConnection);

        } catch (Exception e) {
            responseMessage = e.getMessage();
        }

        finally {
            return responseMessage;
        }
    }



    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        this.result = result;
    }

    private String getOutputUrlConnection(HttpURLConnection httpURLConnection)
    {
        String result = null;
        try{
            int responseCode = httpURLConnection.getResponseCode();


            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                result = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    result += inputLine;
                }
                in.close();

            }
        }

        catch (IOException e)
        {

        }

        return result;
    }


    private String getRequestErrorBody(HttpURLConnection httpURLConnection)
    {
        InputStream errorstream = httpURLConnection.getErrorStream();

        String response = "";

        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(errorstream));

        try{
            while ((line = br.readLine()) != null) {
                response += line;
            }
        }

        catch(IOException e) {

    }
        return response;
    }
}

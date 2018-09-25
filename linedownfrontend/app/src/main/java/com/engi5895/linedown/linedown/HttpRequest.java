package com.engi5895.linedown.linedown;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonathan on 12/03/18.
 */

public class HttpRequest extends AsyncTask <Object, Void, String> {

    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    HttpRequester httpRequester;

    @Override
    protected String doInBackground(Object... params){
        Log.d("HttpRequest", (String) params[0]);
        String stringUrl = (String) params[0];
        String requestMethod = (String) params[1];
        httpRequester = (HttpRequester) params[2];
        String result;
        String inputLine;


        try {
            URL myUrl = new URL(stringUrl);

            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            connection.setRequestMethod(requestMethod);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();

            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            result = stringBuilder.toString();
        }
        catch(IOException e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if (result != null) {
            if (!result.isEmpty()) {
                httpRequester.callback(result);
            }
        }
    }
}

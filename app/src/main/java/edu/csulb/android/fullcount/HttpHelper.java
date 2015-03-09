package edu.csulb.android.fullcount;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.os.Handler;

/**
 * Created by james_000 on 2/23/2015.
 */
public class HttpHelper {
    //final Handler mHandler = new Handler();
    public HttpHelper(){
    }

    /**
     *
     * @param url
     * @param data
     * @param authorization
     */

    public HttpResponse post(String url, JSONObject data, String authorization, String type){
        HttpResponse respose = null;
        try {
            respose = new PostAsyncTask()
                    .execute(url, data.toString(), authorization, type)
                    .get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return respose;
    }

    /**
     *
     * @param url
     * @param authorization
     */

    public HttpResponse get(String url, String authorization){
        HttpResponse respose = null;
        try {
            respose = new GetAsyncTask()
                    .execute(url, authorization)
                    .get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return respose;
    }

    /**
     *
     * @param url
     * @param data
     * @param authorization
     */

    public HttpResponse put(String url, JSONObject data, String authorization){
        HttpResponse respose = null;
        try {
            respose = new PutAsyncTask()
                            .execute(url, data.toString(), authorization)
                            .get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return respose;
    }

    private abstract class HttpAsyncTask extends AsyncTask<String, Integer, HttpResponse> {
        protected static final String server = "http://fullcount.azurewebsites.net";
        protected HttpClient client;

        public HttpAsyncTask(){
            HttpParams httpParameters = new BasicHttpParams();
            //HttpConnectionParams.setConnectionTimeout(httpParameters, this.timeoutConnection);
            //HttpConnectionParams.setSoTimeout(httpParameters, this.timeoutSocket);
            this.client = new DefaultHttpClient(httpParameters);
        }
        @Override
        protected abstract HttpResponse doInBackground(String... params);
    }

    private class PostAsyncTask extends HttpAsyncTask {
        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpPost post = new HttpPost(server + params[0]);
            HttpResponse response = null;
            try {
                StringEntity dataStringEntity = new StringEntity(params[1]);
                dataStringEntity.setContentType("application/json;charset=UTF-8");
                dataStringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                post.setEntity(dataStringEntity);
                if (!(params[2]).matches(""))
                    post.addHeader("Authorization", params[3] + " " + params[2]);
                //Execute HTTP Post Request
                response = client.execute(post);
                //Log.e("Response: ", EntityUtils.toString(response.getEntity()));
                //Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
            } catch (final ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class GetAsyncTask extends HttpAsyncTask {
        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpGet get = new HttpGet(server + params[0]);
            HttpResponse response = null;
            try {
                get.addHeader("Content-type", "application/json");

                if(!(params[1].matches("")))
                    get.addHeader("Authorization", "Basic "+ params[1]);
                Log.e("GetURL: ", server + params[0]);
                //Execute HTTP Post Request
                response = client.execute(get);
                //Log.e("Response: ", EntityUtils.toString(response.getEntity()));
                //Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
            }catch (final ClientProtocolException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class PutAsyncTask extends HttpAsyncTask{
        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpPut put = new HttpPut(server + params[0]);
            HttpResponse response = null;
            try{
                StringEntity dataStringEntity = new StringEntity(params[1]);
                dataStringEntity.setContentType("application/json;charset=UTF-8");
                dataStringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                put.setEntity(dataStringEntity);

                if(!(params[2].matches("")))
                    put.addHeader("Authorization", "Basic "+ params[2] /*Base64.encodeToString((authorization[0] + ":" + authorization[1]).getBytes("UTF-8"), Base64.DEFAULT)*/);
                //Execute HTTP Post Request
                response = client.execute(put);
                //Log.e("Response: ", EntityUtils.toString(response.getEntity()));
                //Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
            }catch (final ClientProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        /*protected void HttpResponse(Double result){
            pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Integer... progress){
            pb.setProgress(progress[0]);
        }*/
    }
}

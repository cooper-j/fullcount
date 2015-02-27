package android.csulb.edu.fullcount1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
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
import java.util.concurrent.ExecutionException;

import android.os.Handler;

/**
 * Created by james_000 on 2/23/2015.
 */
public class HttpHelper extends Activity {
    //final Handler mHandler = new Handler();
    public HttpHelper(){
    }

    /**
     *
     * @param url
     * @param data
     * @param authorization
     */

    public void post(Activity me, String url, JSONObject data, String authorization, Handler handler){
        new Thread(new HttpRunnablePost(me, url, data, authorization, handler)).start();
        /*HttpResponse respose = null;
        try {
            new PostAsyncTask()
                    .execute(url, data.toString(), authorization)
                    .get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return respose;*/
    }

    /**
     *
     * @param url
     * @param data
     * @param authorization
     */

    public void get(Activity me, String url, JSONObject data, String authorization, Handler handler){
        new Thread(new HttpRunnableGet(me, url, data, authorization, handler)).start();
    }

    /**
     *
     * @param url
     * @param data
     * @param authorization
     */

    public HttpResponse put(String url, JSONObject data, String authorization){
        //new Thread(new HttpRunnablePut(me, url, data, authorization, handler)).start();
        HttpResponse respose = null;
        try {
             new PutAsyncTask()
                            .execute(url, data.toString(), authorization)
                            .get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return respose;
    }

    private abstract class HttpRunnable implements Runnable{
        protected String url;
        protected JSONObject data;
        protected String authorization;

        protected static final String server = "http://fullcount.azurewebsites.net";
        protected HttpClient client;
        protected Activity me;

        protected Handler handler;

        private int timeoutConnection = 3000;
        private int timeoutSocket = 5000;

        public HttpRunnable(Activity me, String url, JSONObject data, String authorization, Handler handler){
            this.me = me;
            this.url = url;
            this.data = data;
            this.authorization = authorization;
            this.handler = handler;


            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, this.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, this.timeoutSocket);
            this.client = new DefaultHttpClient(httpParameters);
        }

        @Override
        public abstract void run();

        protected void threadMsg(String msg) {

            if (!msg.equals(null) && !msg.equals("")) {
                Message msgObj = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("message", msg);
                msgObj.setData(b);
                handler.sendMessage(msgObj);
            }
        }

    }

    private class HttpRunnablePost extends HttpRunnable{

        public HttpRunnablePost(Activity me, String url, JSONObject data, String authorization, Handler handler){
            super(me, url, data, authorization, handler);
        }

        @Override
        public void run() {
            HttpPost post = new HttpPost(server + url);
            try {
                StringEntity dataStringEntity = new StringEntity(data.toString());
                dataStringEntity.setContentType("application/json;charset=UTF-8");
                dataStringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                post.setEntity(dataStringEntity);
                if (authorization != null)
                    post.addHeader("Authorization", "Basic " + authorization /*Base64.encodeToString((authorization[0] + ":" + authorization[1]).getBytes("UTF-8"), Base64.DEFAULT)*/);
                //Execute HTTP Post Request
                HttpResponse response = client.execute(post);
                Log.e("Response: ", EntityUtils.toString(response.getEntity()));
                Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
                threadMsg(String.valueOf(response.getStatusLine().getStatusCode()));
                /*runOnUiThread(new Runnable() {
                    public void run() {
                        // toasts 200 on success
                        Toast.makeText(me, String.valueOf(response.getStatusLine().getStatusCode()), Toast.LENGTH_LONG).show();
                    }
                });*/

                }catch (final ClientProtocolException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(me, String.valueOf("Error #101: Please try again later or report"), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(me, String.valueOf("Error #102: Please try again later or report"), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class HttpRunnableGet extends HttpRunnable {

        public HttpRunnableGet(Activity me, String url, JSONObject data, String authorization, Handler handler) {
            super(me, url, data, authorization, handler);
        }

        @Override
        public void run(){
            HttpGet get = new HttpGet(server + url);
            try {
                if(authorization != null)
                    get.addHeader("Authorization", "Basic "+ authorization/*Base64.encodeToString((authorization[0] + ":" + authorization[1]).getBytes("UTF-8"), Base64.DEFAULT)*/);

                //Execute HTTP Post Request
                HttpResponse response = client.execute(get);
                Log.e("Response: ", EntityUtils.toString(response.getEntity()));
                Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
            }catch (final ClientProtocolException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(me, String.valueOf("Error #101: Please try again later or report"), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(me, String.valueOf("Error #102: Please try again later or report"), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class HttpRunnablePut extends HttpRunnable{

        public HttpRunnablePut(Activity me, String url, JSONObject data, String authorization, Handler handler){
            super(me, url, data, authorization, handler);
        }

        @Override
        public void run() {
            HttpPut put = new HttpPut(server + url);

            try{
                StringEntity dataStringEntity = new StringEntity(data.toString());
                dataStringEntity.setContentType("application/json;charset=UTF-8");
                dataStringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                put.setEntity(dataStringEntity);

                if(authorization != null)
                    put.addHeader("Authorization", "Basic "+ authorization /*Base64.encodeToString((authorization[0] + ":" + authorization[1]).getBytes("UTF-8"), Base64.DEFAULT)*/);
                //Execute HTTP Post Request
                HttpResponse response = client.execute(put);
                Log.e("Response: ", EntityUtils.toString(response.getEntity()));
                Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
                threadMsg(String.valueOf(response.getStatusLine().getStatusCode()));
            }catch (final ClientProtocolException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(me, String.valueOf("Error #101: Please try again later or report"), Toast.LENGTH_LONG).show();
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(me, String.valueOf("Error #102: Please try again later or report"), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private abstract class HttpAsyncTask extends AsyncTask<String, Integer, HttpResponse> {
        protected static final String server = "http://fullcount.azurewebsites.net";
        protected HttpClient client;

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
                if (params[2] != null)
                    post.addHeader("Authorization", "Basic " + params[2] /*Base64.encodeToString((authorization[0] + ":" + authorization[1]).getBytes("UTF-8"), Base64.DEFAULT)*/);
                //Execute HTTP Post Request
                response = client.execute(post);
                Log.e("Response: ", EntityUtils.toString(response.getEntity()));
                Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
            } catch (final ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

                if(params[2] != null)
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

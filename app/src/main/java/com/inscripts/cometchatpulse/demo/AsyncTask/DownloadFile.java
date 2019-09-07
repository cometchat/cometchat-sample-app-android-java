package com.inscripts.cometchatpulse.demo.AsyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.FileUtils;
import com.inscripts.cometchatpulse.demo.ViewHolders.LeftAudioViewHolder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFile extends AsyncTask<String, Integer, String> {

    private String mediaUrl;

    private String type;

    private LeftAudioViewHolder leftViewHolder;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public DownloadFile(Context context,String type, String mediaUrl, LeftAudioViewHolder leftViewHolder){
        this.type=type;
        this.context=context;
        this.mediaUrl=mediaUrl;
        this.leftViewHolder=leftViewHolder;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream=null;
        OutputStream outputStream=null;
        HttpURLConnection connection=null;
        File file;
        try{
            URL url=new URL(mediaUrl);
            connection= (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                return "server "+connection.getResponseCode() +" "+connection.getResponseMessage();
            }

            int fileLength=connection.getContentLength();

            inputStream=connection.getInputStream();

            file= new File(FileUtils.getPath(context,type)+FileUtils.getFileName(mediaUrl));

            file.setReadable(true,false);


            outputStream= new  FileOutputStream(file);

            byte []data=new byte[4096];

            long total=0;

            int count=0;

            while ((count = inputStream.read(data))!=-1){

                if (isCancelled()) {
                    inputStream.close();
                    return null;

                }
                total += count;

                if (fileLength > 0) {
                    publishProgress((int) (total * 100 / fileLength));
                    outputStream.write(data, 0, count);
                }
            }

        }catch (Exception e){
          e.printStackTrace();
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException io) {

                io.printStackTrace();
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        try {
             leftViewHolder.fileLoadingProgressBar.setVisibility(View.VISIBLE);
             leftViewHolder.fileLoadingProgressBar.setMax(100);
             leftViewHolder.fileLoadingProgressBar.setIndeterminate(false);
             leftViewHolder.download.setImageResource(R.drawable.ic_close_24dp);
             leftViewHolder.fileLoadingProgressBar.setProgress(values[0]);
             leftViewHolder.playAudio.setVisibility(View.GONE);

        }catch (Exception e){
           e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            leftViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
             leftViewHolder.download.setVisibility(View.GONE);
             leftViewHolder.playAudio.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

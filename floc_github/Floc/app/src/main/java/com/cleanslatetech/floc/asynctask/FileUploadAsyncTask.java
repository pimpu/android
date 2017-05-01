package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.cleanslatetech.floc.activities.CreateFlocActivity;
import com.cleanslatetech.floc.activities.CreatePlatformActivity;
import com.cleanslatetech.floc.activities.MyProfileActivity;
import com.cleanslatetech.floc.models.ChannelModel;
import com.cleanslatetech.floc.models.EventsModel;
import com.cleanslatetech.floc.models.MyProfileModel;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by pimpu on 2/24/2017.
 */

public class FileUploadAsyncTask extends AsyncTask<String, String, String>{
    private ProgressDialog prgDialog;
    private String filepath, postImageServerUrl;
    private Context context;
    private EventsModel eventsModel;
    private MyProfileModel myProfileModel;
    private ChannelModel channelModel;

    public FileUploadAsyncTask(Context context, EventsModel eventsModel, String filepath, String postImageServerUrl) {
        this.filepath = filepath;
        this.context = context;
        this.postImageServerUrl = postImageServerUrl;
        this.eventsModel = eventsModel;
    }

    public FileUploadAsyncTask(Context context, MyProfileModel myProfileModel, String filepath, String postImageServerUrl) {
        this.filepath = filepath;
        this.context = context;
        this.postImageServerUrl = postImageServerUrl;
        this.myProfileModel = myProfileModel;
    }

    public FileUploadAsyncTask(Context context, ChannelModel channelModel, String filepath, String postImageServerUrl) {
        this.filepath = filepath;
        this.context = context;
        this.postImageServerUrl = postImageServerUrl;
        this.channelModel = channelModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Floc ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        prgDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {

        String result = null;
        try {
            result = multipartRequest(postImageServerUrl, filepath, "image", "image/"+ MimeTypeMap.getFileExtensionFromUrl(filepath));
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        prgDialog.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(s);
            Boolean error = jsonObject.getBoolean(CommonVariables.TAG_ERROR);
            JSONArray jsonArray = jsonObject.getJSONArray(CommonVariables.TAG_MESSAGE);

            if (error) {
                for( int i = 0 ; i < jsonArray.length(); i++) {
                    String msg = jsonArray.getJSONObject(i).getString(CommonVariables.TAG_MESSAGE_OBJ);
                    System.out.println(msg);
                    CommonUtilities.customToast(context, msg);
                }
            } else {
                String msg = jsonArray.getJSONObject(0).getString(CommonVariables.TAG_MESSAGE_OBJ);
                System.out.println("file uploaded successfully.");

                if (eventsModel != null) {
                    eventsModel.setEventPicture(msg);
                    new CreateFlocAsyncTask(context, eventsModel, prgDialog).postData();

                } else if (myProfileModel != null) {
                    myProfileModel.setProfilePic(msg);
                    new InsertMyProfileAsyncTask(context, myProfileModel, prgDialog).postData();

                } else if (channelModel != null) {
                    channelModel.setChannelImage(msg);
                    new CreatePlatformAsyncTask(context, channelModel, prgDialog).postData();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String multipartRequest(String urlTo, String filepath, String filefield, String fileMimeType) throws CustomException{
        HttpURLConnection connection;
        DataOutputStream outputStream;
        InputStream inputStream;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = decodeFile(new File(filepath));
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                throw new CustomException("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private class CustomException extends Exception {
        public CustomException(Exception s) {
            System.out.println("Ex: "+s.getMessage());
        }

        public CustomException(String s) {
            System.out.println("String: "+s);
        }
    }

    private File decodeFile(File f) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap convertesBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);

        double xFactor = 0;
        double width = (double) convertesBitmap.getWidth();
        double height = (double) convertesBitmap.getHeight();
        if(width>height){
            xFactor = 470/width;
        }
        else{
            xFactor = 250/width;
        }
        int Nheight = (int) ((xFactor*height));
        int NWidth =(int) (xFactor * width) ;

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(convertesBitmap, NWidth, Nheight, true);

        File imagePath = new File(context.getCacheDir(), "images");
        if(!imagePath.exists()) {
            imagePath.mkdir();
        }

        File newFile = new File(imagePath, "image.png");

        try {
            if( newFile.exists() ) {
                newFile.delete();
            }

            newFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(newFile);
            // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // Flush and close the output stream
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFile;

    }

}

package com.alchemistdigital.ziko.activities;

import android.app.ProgressDialog;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

import com.alchemistdigital.ziko.R;
import com.alchemistdigital.ziko.adapters.ImageLoaderAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.alchemistdigital.ziko.utilities.CommonMethods.getExternalSdCardPath;

public class CameraActivity extends AppCompatActivity implements FileFilter, AdapterView.OnItemSelectedListener {
    private ArrayList<String> directorySpinnerItem = new ArrayList<String>();
    private Spinner spinner_nav;
    VideoView video_player_view;
    MediaController media_Controller;
    Map<String, List<String>> folder_mp4_filename = new HashMap<String, List<String>>();
    List<String> mp4FileNames = new ArrayList<String>();
    HashMap<String, String> videoDuration = new HashMap<String,String>();
    File parentFileName=null;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide notification bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);

        // toolbar set.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // hide title of activity from toolbar.
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        new DirectorySpinnerAsynTask().execute();


        gridView = (GridView) findViewById(R.id.gridview);


        video_player_view =(VideoView)findViewById(R.id.video_player_view);
        media_Controller = new MediaController(this);
        media_Controller.setAnchorView(video_player_view);
    }

    public void playVideoInVideoView(Uri uri) {
        video_player_view.setVideoURI(uri);
        video_player_view.requestFocus();
        video_player_view.start();
    }

    private class DirectorySpinnerAsynTask extends AsyncTask<String, String, ArrayList<String>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            pDialog = new ProgressDialog(CameraActivity.this);
            pDialog.setMessage("loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            // create directory spinner data
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            // create directory from internal memory
            createDirecotyArrayList(dir);

            // create directory from external sdcard memory.
            createDirecotyArrayList(getExternalSdCardPath());


            // this 3 lines are part of createDirecotyArrayList() method
            // below 3 lines of code doesn't work for last data of parent directory of mp4 file,
            folder_mp4_filename.put(parentFileName.getPath(), mp4FileNames);
            mp4FileNames=new ArrayList<String>();
            parentFileName = null;
            return directorySpinnerItem;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            pDialog.dismiss();

//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(CameraActivity.this,
                    android.R.layout.simple_spinner_item, directorySpinnerItem);
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_nav.setAdapter(spinAdapter);
            spinner_nav.setOnItemSelectedListener(CameraActivity.this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
        // On selecting a spinner item
        String item = adapter.getItemAtPosition(position).toString();
        List<String> values = null ;
        String key = null;

        // spinner item are file name
        // folder_mp4_filename are collection of path name as key and path name's mp4 files are string array
        // eg. spinner item = Camera and
        // folder_mp4_filename = ["/storage/emulated/0/DCIM/Camera" : " xyz.mp4,abc.mp4,pqr.mp4"]
        // for loop working -
        //    - key with full path get substring for getting file name
        //    - match with spinner selected item
        //    - if match, then it will get all value of that key from folder_mp4_filename.
        for (Map.Entry<String, List<String>> entry : folder_mp4_filename.entrySet()) {
            // key = /storage/emulated/0/DCIM/Camera
            key = entry.getKey();
            // getFileName = Camera;
            String getFileName = key.substring(key.lastIndexOf("/")+1, key.length()).trim();

            if ( getFileName.equals(item) ) {
                values = entry.getValue();
                break;
            }
        }

        // play first video of of spinner selected item directory.
        String videoFilePath = key + "/" + values.get(0);
        File newFile = new File(videoFilePath);
        playVideoInVideoView(Uri.fromFile(newFile));

        ImageLoaderAdapter ImageLoadAdapter = null;
        if (values != null) {
            ImageLoadAdapter = new ImageLoaderAdapter(this, values, key, videoDuration, getApplicationContext());
            gridView.setAdapter(ImageLoadAdapter);
        }

        final String finalKey = key;
        final List<String> finalValues = values;

        // video play when video thumbnail get click.
        final ImageLoaderAdapter finalImageLoadAdapter = ImageLoadAdapter;

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

                String videoFilePath = finalKey + "/" + finalValues.get(position);
                File newFile = new File(videoFilePath);
                playVideoInVideoView(Uri.fromFile(newFile));

                finalImageLoadAdapter.selectedPosition = position;
                finalImageLoadAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private ArrayList<String> createDirecotyArrayList(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if( !accept(listFile[i]) ) {
                    continue;
                }

                if (listFile[i].isDirectory()) {
                    createDirecotyArrayList(listFile[i]);
                }
                else {
                    if(listFile[i].getPath().contains(".mp4") == true ) {
                        if( parentFileName == null ) {
                            parentFileName = listFile[i].getParentFile();
                        }
                        directorySpinnerItem.add(listFile[i].getParentFile().getName());

                        if( ! parentFileName.getName().equals( listFile[i].getParentFile().getName() ) ) {
                            folder_mp4_filename.put(parentFileName.getPath(), mp4FileNames);
                            mp4FileNames=new ArrayList<String>();
                            parentFileName = null;

                        }
                        mp4FileNames.add(listFile[i].getName());

                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(this,Uri.fromFile(new File(listFile[i].getPath())));
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long duration = Long.parseLong(time);
                        String format = String.format("%d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(duration),
                                TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                        );
                        videoDuration.put(listFile[i].getName(),format);
                    }
                }
            }
        }
        HashSet hs = new HashSet();
        hs.addAll(directorySpinnerItem);
        directorySpinnerItem.clear();
        directorySpinnerItem.addAll(hs);

        return directorySpinnerItem;
    }

    Set<String> systemFileNames = new HashSet<String>(Arrays.asList("sys", "etc"));
    @Override
    public boolean accept(File pathname) {
        // in my scenario: each hidden file starting with a dot is a "system file"
        if (pathname.getName().startsWith(".") && pathname.isHidden()) {
            return false;
        }

        // exclude known system files
        if (systemFileNames.contains(pathname.getName())) {
            return false;
        }

        // more rules / other rules

        // no rule matched, so this is not a system file
        return true;
    }
}

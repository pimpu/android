package com.alchemistdigital.ziko.activities;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

import com.alchemistdigital.ziko.R;
import com.alchemistdigital.ziko.adapters.ImageLoaderAdapter;
import com.alchemistdigital.ziko.models.DirectorySpinnerItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CameraActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner_nav;
    ImageLoaderAdapter ImageLoadAdapter;
    VideoView video_player_view;
    MediaController media_Controller;
    GridView gridView;
    ArrayAdapter<DirectorySpinnerItem> spinAdapter;

    ArrayList<DirectorySpinnerItem> fileDirectoryNames = new ArrayList<DirectorySpinnerItem>();

    // used for to generate spinner item
    // {parentFileName=[ ##.mp4, ##mp4, ##mp4], parentFileName=[##.mp4]}
    Map<String, ArrayList<String>> filename_path_mp4Files = new HashMap<>();

    // used for video duration which is used in video thumbnail
    HashMap<String, String> videoDuration = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide notification bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);

        gridView = (GridView) findViewById(R.id.gridview);
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        // create directory from external sdcard memory.
        getMediaMp4Cursor();

        getContentResolver().registerContentObserver(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        getMediaMp4Cursor();
                        System.out.println("test: " + fileDirectoryNames);
                        spinAdapter.notifyDataSetChanged();
                        ImageLoadAdapter.notifyDataSetChanged();

                        super.onChange(selfChange);
                    }
                }
        );

        // toolbar set.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // hide title of activity from toolbar.
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        spinAdapter = new ArrayAdapter<DirectorySpinnerItem>(CameraActivity.this,
                android.R.layout.simple_spinner_item, fileDirectoryNames);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nav.setAdapter(spinAdapter);
        spinner_nav.setOnItemSelectedListener(CameraActivity.this);



        video_player_view = (VideoView) findViewById(R.id.video_player_view);
        media_Controller = new MediaController(this);
        media_Controller.setAnchorView(video_player_view);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {

//        ((TextView) adapter.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));

        // On selecting a spinner item
        final DirectorySpinnerItem spinnerItem = (DirectorySpinnerItem) adapter.getSelectedItem();

        final ArrayList<String> selectedmp4FilesList = filename_path_mp4Files.get(spinnerItem.getDirectoryPath());

        // play first video of of spinner selected item directory.
        String videoFilePath = spinnerItem.getDirectoryPath() + selectedmp4FilesList.get(0);
        File newFile = new File(videoFilePath);
        playVideoInVideoView(Uri.fromFile(newFile));

        // creating grid view
        ImageLoadAdapter = new ImageLoaderAdapter(this, selectedmp4FilesList, spinnerItem.getDirectoryPath(), videoDuration);
        gridView.setAdapter(ImageLoadAdapter);

        // video play when video thumbnail get click.
        final ImageLoaderAdapter finalImageLoadAdapter = ImageLoadAdapter;

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String videoFilePath = spinnerItem.getDirectoryPath() + selectedmp4FilesList.get(position);
                File newFile = new File(videoFilePath);
                playVideoInVideoView(Uri.fromFile(newFile));

                // used for change background resources i.e. border to selected item
                finalImageLoadAdapter.selectedPosition = position;
                finalImageLoadAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getMediaMp4Cursor() {
        fileDirectoryNames.clear();
        filename_path_mp4Files.clear();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION
        };
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp4");
        String[] selectionArgsPdf = new String[]{mimeType};
        String sortOrder = MediaStore.Video.Media.BUCKET_DISPLAY_NAME;
        Cursor query = getContentResolver().query(uri, projection, selectionMimeType, selectionArgsPdf, sortOrder);


        createDirecotyArrayList(query);
    }

    /**
     * get cursor of all mp4 file of mobile
     * function did two operation
     * 1 - create model of spinner item of parent file path and parent file name
     * 2 - create hash map of {parentFileName=[ ##.mp4, ##mp4, ##mp4], parentFileName=[##.mp4]}
     * % cursor comes with full path od mp4 file
     * % for loop did operation of grouping of all mp4 file according to parent file name.
     *
     * @param cursorMp4
     * @return
     */
    private void createDirecotyArrayList(Cursor cursorMp4) {
        ArrayList<String> innerList = new ArrayList<String>();
        String refFilePath = null;
        String refFolderName = null;

        for (int i = 0; i < cursorMp4.getCount(); i++) {
            cursorMp4.moveToPosition(i);

            // get parent(bucket name)
            int bucketColumnIndex = cursorMp4.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);
            String folderName = cursorMp4.getString(bucketColumnIndex);

            // get file path
            int dataColumnIndex = cursorMp4.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            String filePath = cursorMp4.getString(dataColumnIndex);
            String parentfilePath = filePath.substring(0, filePath.lastIndexOf("/") + 1).trim();

            if (refFilePath == null) {
                refFilePath = parentfilePath;
                refFolderName = folderName;
            }

            if (!refFilePath.equals(parentfilePath)) {
                DirectorySpinnerItem spinnerItemModel = new DirectorySpinnerItem();
                // fileDirectoryName used for to generate spinner item
                spinnerItemModel.setDirectoryName(refFolderName);
                spinnerItemModel.setDirectoryPath(refFilePath);

                fileDirectoryNames.add(spinnerItemModel);

                // below code is used for grid view
                filename_path_mp4Files.put(refFilePath, innerList);
                refFilePath = parentfilePath;
                refFolderName = folderName;
                innerList = new ArrayList<String>();
            }
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()).trim();
            innerList.add(fileName);

            // get video duration
            int durationColumnIndex = cursorMp4.getColumnIndex(MediaStore.Video.VideoColumns.DURATION);
            long duration = Long.parseLong(cursorMp4.getString(durationColumnIndex));
            String format = String.format("%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
            videoDuration.put(filePath, format);
        }


        filename_path_mp4Files.put(refFilePath, innerList);

        DirectorySpinnerItem spinnerItemModel = new DirectorySpinnerItem();
        spinnerItemModel.setDirectoryName(refFolderName);
        spinnerItemModel.setDirectoryPath(refFilePath);
        fileDirectoryNames.add(spinnerItemModel);
    }

    public void playVideoInVideoView(Uri uri) {
        video_player_view.setVideoURI(uri);
        video_player_view.requestFocus();
        video_player_view.start();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

}

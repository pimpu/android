package com.alchemistdigital.ziko.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alchemistdigital.ziko.utilities.CommonVariable;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 5/11/2016.
 */
public class TrimmVideo extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String mediaPath;
    private double startTime;
    private double endTime;
    private int length;
    private ProgressDialog progressDialog;

    public TrimmVideo(Context context, String selectedVideoFilePath, int startTime, int length) {
        this.context = context;
        this.mediaPath = selectedVideoFilePath;
        this.startTime = startTime / 1000.0;
        this.endTime = length / 1000.0;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context,
                "Trimming videos", "Please wait...", true);
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        trimVideo();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        super.onPostExecute(aVoid);
    }

    private void trimVideo() {
        try {
            File file = new File(mediaPath);
            FileInputStream fis = new FileInputStream(file);
            FileChannel in = fis.getChannel();
            Movie movie = MovieCreator.build(in);

            List<Track> tracks = movie.getTracks();
            movie.setTracks(new LinkedList<Track>());

            boolean timeCorrected = false;

            // Here we try to find a track that has sync samples. Since we can only start decoding
            // at such a sample we SHOULD make sure that the start of the new fragment is exactly
            // such a frame
            for (Track track : tracks) {
                if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                    if (timeCorrected) {
                        // This exception here could be a false positive in case we have multiple tracks
                        // with sync samples at exactly the same positions. E.g. a single movie containing
                        // multiple qualities of the same video (Microsoft Smooth Streaming file)

                        //throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                    } else {
                        startTime = correctTimeToNextSyncSample(track, startTime);
                        timeCorrected = true;
                    }
                }
            }

            for (Track track : tracks) {
                long currentSample = 0;
                double currentTime = 0;
                long startSample = -1;
                long endSample = -1;

                for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
                    TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
                    for (int j = 0; j < entry.getCount(); j++) {
                        // entry.getDelta() is the amount of time the current sample covers.

                        if (currentTime <= startTime) {
                            // current sample is still before the new starttime
                            startSample = currentSample;
                        } else if (currentTime <= endTime) {
                            // current sample is after the new start time and still before the new endtime
                            endSample = currentSample;
                        } else {
                            // current sample is after the end of the cropped video
                            break;
                        }
                        currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                        currentSample++;
                    }
                }
                movie.addTrack(new CroppedTrack(track, startSample, endSample));
            }
            //if(startTime==length)
            //throw new Exception("times are equal, something went bad in the conversion");

            IsoFile out = new DefaultMp4Builder().build(movie);

            File storagePath = new File(CommonVariable.CROP_VIDEO_FILE_PATH);
            storagePath.mkdirs();

            long timestamp=new Date().getTime();
            String timestampS="" + timestamp;

            File myMovie = new File(storagePath, String.format("output-%s-%f-%d.mp4", timestampS, startTime*1000, length*1000));

            FileOutputStream fos = new FileOutputStream(myMovie);
            FileChannel fc = fos.getChannel();
            out.getBox(fc);

            fc.close();
            fos.close();
            fis.close();
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private double correctTimeToNextSyncSample(Track track, double cutHere) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
            TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
            for (int j = 0; j < entry.getCount(); j++) {
                if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                    // samples always start with 1 but we start with zero therefore +1
                    timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
                }
                currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
        }
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                return timeOfSyncSample;
            }
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}

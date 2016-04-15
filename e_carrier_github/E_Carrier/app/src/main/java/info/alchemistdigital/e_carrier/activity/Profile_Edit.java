package info.alchemistdigital.e_carrier.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import info.alchemistdigital.e_carrier.R;

public class Profile_Edit extends AppCompatActivity implements View.OnClickListener {
    EditText txtEditName;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;
    ImageView imageView;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);

        txtEditName = (EditText) findViewById(R.id.idEditName);
        imageView = (ImageView) findViewById(R.id.id_edit_profile_image);
        btnSave = (Button) findViewById(R.id.idSaveProfile);
        btnSave.setOnClickListener(this);

        SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
        String name = sharedPreferenceLogin.getString(getResources().getString(R.string.loginName), "");
        String strProfilePic = sharedPreferenceLogin.getString(getResources().getString(R.string.profilePic), "");

        txtEditName.setText(name);

        if (!strProfilePic.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(strProfilePic, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setBackground(getResources().getDrawable(R.drawable.ic_profile));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.idEditProfilePic);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {

                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = getResizedBitmap(selectedBitmap,500,filePath);
                selectedBitmap.recycle();
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idSaveProfile:

                String stringImage = getStringImage(bitmap);
                String name = txtEditName.getText().toString().trim();

                new uploadPic(stringImage, name).execute();

                break;
        }

    }

    class uploadPic extends AsyncTask<String,String,String>{
        String stringImage,name;

        public uploadPic(String stringImage, String name) {
            this.stringImage = stringImage;
            this.name        = name;
        }

        @Override
        protected String doInBackground(String... params) {

            SharedPreferences shre = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
            SharedPreferences.Editor edit = shre.edit();
            edit.putString(getResources().getString(R.string.profilePic), stringImage);
            edit.putString(getResources().getString(R.string.loginName), name);
            edit.commit();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            startActivity(new Intent(Profile_Edit.this, MainActivity.class));
            finish();
        }
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @param filePath
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap selectedBitmap, int maxSize, Uri filePath) {
        int width = selectedBitmap.getWidth();
        int height = selectedBitmap.getHeight();
        Matrix matrix = new Matrix();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        try {

            ExifInterface exif = new ExifInterface(getRealPathFromURI(Profile_Edit.this,filePath));
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }
        }
        catch (Exception e) {

        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedBitmap, width, height, true);
        return Bitmap.createBitmap(scaledBitmap, 0, 0, width, height, matrix, true);// rotating bitmap
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile_Edit.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

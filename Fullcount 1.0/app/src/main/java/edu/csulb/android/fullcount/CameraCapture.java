package edu.csulb.android.fullcount;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AbsListView;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ListAdapter;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.io.File;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Tryndal on 4/2/2015.
 */
public class CameraCapture implements Button.OnClickListener
{
    // Activity result key for camera
    static final int REQUEST_TAKE_PHOTO = 11111;
    // Image view for showing our image.
    private ImageView mImageView;
    private ImageView mThumbnailImageView;
    /**
     * Default empty constructor.
     */
    public CameraCapture(){
        super();
    }
    /**
     * Static factory method
     * @param sectionNumber
     * @return
     */
    public static SimpleCameraIntentFragment newInstance(int sectionNumber) {
        SimpleCameraIntentFragment fragment = new SimpleCameraIntentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * OnCreateView fragment override
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_simple_camera_intent, container, false);
// Set the image view
        mImageView = (ImageView)view.findViewById(R.id.imageViewFullSized);
        mThumbnailImageView = (ImageView)view.findViewById(R.id.imageViewThumbnail);
        Button takePictureButton = (Button)view.findViewById(R.id.button);
// Set OnItemClickListener so we can be notified on button clicks
        takePictureButton.setOnClickListener(this);
        return view;
    }

    protected void dispatchTakePictureIntent()
    {
        //check to see if camera is available
        Context context = (Context)getActivity();
        PackageManager packManager = context.getPackageManager();
        if(packManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)==false)
        {
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Camera exists... proceed
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Ensure that there's a camera activity to handle the intent
        CameraActivity activity = (CameraActivity)getActivity();
        if(takePictureIntent.resolveActivity(activity.getPackageManager()) != null)
        {
            //create file where photo goes
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch(IOException ex)
            {
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...");
                toast.show();
            }
            if(photoFile != null)
            {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //returns with the photo
    @Override
    public void onActivityRequest(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK)
        {
            addPhotoToGallery();
            CameraActivity activity = (CameraActivity)getActivity();

            //show full sized image
            setFullImageFromFilePath(activity.getCurrentPhotoPath(), mImageView);
            setFullImageFromFilePath(activity.getCurrentPhotoPath(), mThumbnailImageView);
        }
        else
        {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT).show();
        }
    }

    protected File createImageFile() throws IOException {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
// Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity)getActivity();
        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }
    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        CameraActivity activity = (CameraActivity)getActivity();
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }
    /**
     * Deal with button clicks.
     * @param v
     */
    @Override
    public void onClick(View v) {
        dispatchTakePictureIntent();
    }
    /**
     * Scale the photo down and fit it to our image views.
     *
     * "Drastically increases performance" to set images using this technique.
     * Read more:http://developer.android.com/training/camera/photobasics.html
     */
    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
// Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
// Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
// Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
// Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
}

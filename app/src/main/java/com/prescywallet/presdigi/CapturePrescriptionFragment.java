package com.prescywallet.presdigi;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.gotev.uploadservice.UploadService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class CapturePrescriptionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.camera_bar_main, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        cameraCaptureView(view);

        return view;
    }

    Camera camera;
    File tempFile = null;


    private File folder = null;



    public void cameraCaptureView(final View view){
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.prescywallet.presdigi";



        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            initControls(view);


            //create a folder to get image
            /*folder = new File(Environment.getDataDirectory() + "/PrescriptionCamera");*/
            folder = getActivity().getDir("PrescriptionCamera", Context.MODE_PRIVATE);
            if (!folder.exists()) {
                folder.mkdirs();
            }

        }

    }



    private CapturePrescriptionFragment.SavePicTask savePicTask;

    private class SavePicTask extends AsyncTask<Void, Void, String> {
        private byte[] data;
        private int degree;

        public SavePicTask(byte[] data, int degree) {
            this.data = data;
            this.degree = degree;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                return saveToSDCard(data, degree);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {



            tempFile = new File(result);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            mDialog.cancel();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            PrescriptionUploadFragment fragment = new PrescriptionUploadFragment();
            Bundle args = new Bundle();
            args.putString("pathImage", tempFile.toString());
            fragment.setArguments(args);
            loadFragment(fragment);

        }
    }
    private void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("CapturePrescriptionFragment")
                    .commit();
        }
    }

    public String saveToSDCard(byte[] data, int rotation) throws IOException {
        String imagePath = "";
        Bitmap scaledBitmap = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            Log.e("Height&Width","Actual Height = " + actualHeight + " Actual Width = " + actualWidth);

            float maxHeight = 1080.0f;
            float maxWidth = 720.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            //      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }
            Log.e("Height&Width","Actual Height = " + actualHeight + " Actual Width = " + actualWidth);

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;

            //      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];


            try {
            //          load the bitmap from its path
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            Bitmap bmGrayScale = getGrayscale(scaledBitmap);
            Bitmap bmGrayContrastAdj = adjustedContrast(bmGrayScale, 30);
            if (rotation != 0) {
                Matrix mat = new Matrix();
                mat.postRotate(rotation);
                Bitmap bitmap1 = Bitmap.createBitmap(bmGrayContrastAdj, 0, 0, bmGrayContrastAdj.getWidth(), bmGrayContrastAdj.getHeight(), mat, true);
                if (bmGrayContrastAdj != bitmap1) {
                    bmGrayContrastAdj.recycle();
                }
                imagePath = getSavePhotoLocal(bitmap1);
                if (bitmap1 != null) {
                    bitmap1.recycle();
                }
            } else {
                imagePath = getSavePhotoLocal(bmGrayContrastAdj);
                if (bmGrayContrastAdj != null) {
                    bmGrayContrastAdj.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    private Bitmap adjustedContrast(Bitmap src, double value)
    {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.green(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.blue(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    private Bitmap getGrayscale(Bitmap src){

        //Custom color matrix to convert to GrayScale
        float[] matrix = new float[]{
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0, 0, 0, 1, 0,};

        Bitmap dest = Bitmap.createBitmap(
                src.getWidth(),
                src.getHeight(),
                src.getConfig());

        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(src, 0, 0, paint);

        return dest;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private String getSavePhotoLocal(Bitmap bitmap) {
        String path = "";
        try {
            OutputStream output;
            File file = new File(folder.getAbsolutePath(), "wc" + System.currentTimeMillis() + ".png");
            try {
                output = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 60, output);
                output.flush();
                output.close();
                path = file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 2000;
    CardView galleryCard, cameraCard;
    ProgressDialog mDialog;
    private Uri mHighQualityImageUri = null;

    private void initControls(View view) {



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        cameraCard = view.findViewById(R.id.cameraCardView);
        cameraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mHighQualityImageUri = getCaptureImageOutputUri("IMG_" + System.currentTimeMillis() + ".png");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });

        galleryCard = view.findViewById(R.id.galleryCardView);
        galleryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // 2. pick image only
                intent.setType("image/*");
                // 3. start activity
                startActivityForResult(intent, GALLERY_REQUEST);

            }
        });

    }
    private Uri getCaptureImageOutputUri(String filename) {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), filename));
        }
        return outputFileUri;
    }


    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        assert data.getData() != null;
        if(reqCode == GALLERY_REQUEST && resCode == Activity.RESULT_OK && getActivity() != null){
            try {
                InputStream iStream =   getActivity().getContentResolver().openInputStream(data.getData());
                byte[] inputData = getBytes(iStream);
                mDialog = new ProgressDialog(getContext());
                mDialog.setMessage("Retrieving data...");
                mDialog.show();
                savePicTask = new CapturePrescriptionFragment.SavePicTask(inputData, 0);
                savePicTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (reqCode == CAMERA_REQUEST && resCode == Activity.RESULT_OK && getActivity() != null){
            assert getContext() != null;
            try {
                InputStream iStream =   getActivity().getContentResolver().openInputStream(mHighQualityImageUri);
                byte[] inputData = getBytes(iStream);
                mDialog = new ProgressDialog(getContext());
                mDialog.setMessage("Loading Prescription...");
                mDialog.show();
                savePicTask = new CapturePrescriptionFragment.SavePicTask(inputData, 0);
                savePicTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }




    //--------------------------CHECK FOR MEMORY -----------------------------//

    public int getFreeSpacePercantage() {
        int percantage = (int) (freeMemory() * 100 / totalMemory());
        int modValue = percantage % 5;
        return percantage - modValue;
    }

    public double totalMemory() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        double sdAvailSize = (double) stat.getBlockCount() * (double) stat.getBlockSize();
        return sdAvailSize / 1073741824;
    }

    public double freeMemory() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        double sdAvailSize = (double) stat.getAvailableBlocks() * (double) stat.getBlockSize();
        return sdAvailSize / 1073741824;
    }

    //-------------------END METHODS OF CHECK MEMORY--------------------------//




}

package com.creatifsoftware.filonova.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.R;
import com.emrhmrc.cameraxlib.CameraXActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static android.widget.Toast.LENGTH_LONG;

public class ImageUtil {
    public static final String KILOMETER_FUEL_CAPTURE_IMAGE = "kilometer_fuel_image";
    public static final String DAMAGE_IMAGE = "damage_image";
    public static final String FRONT_LICENSE_IMAGE = "front_license_image";
    public static final String REAR_LICENSE_IMAGE = "rear_license_image";
    public static final ImageUtil instance = new ImageUtil();
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";
    private String imageFilePath;

    public Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public Intent dispatchTakePictureIntent(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile(context);
            } catch (IOException e) {
                Toast.makeText(context, e.getLocalizedMessage(), LENGTH_LONG).show();
                e.printStackTrace();
            }

            if (imageFile != null) {
                imageFilePath = imageFile.getAbsolutePath();

                Uri imageUri = FileProvider.getUriForFile(context, AUTHORITY, imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //grantUriPermissions(context,intent, imageUri);
            }
        } else {
            Toast.makeText(context, R.string.camera_not_found, LENGTH_LONG).show();
        }

        return intent;
    }

    private void grantUriPermissions(Context context, Intent intent, Uri imageUri) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public File getImageFile() {
        return new File(imageFilePath);
    }

    public void removeImageFile() {
        if (imageFilePath != null) {
            imageFilePath = null;
        }
    }

    public Bitmap convertFiletoBitmap(File file) {
        String filePath = file.getPath();
        return BitmapFactory.decodeFile(filePath);
    }

    public File compressImage(Context context, File imageFile) {
        try {
            return new Compressor(context)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return imageFile;
        }
    }

//    public void compressImageFile(Context context,File imageFile) {
//        Compressor compressor = initCompressor(context);
//        compressor.compressToFileAsFlowable(imageFile).
//                subscribeOn(Schedulers.io()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe(file ->
//                        onCompressImageSuccess(file), throwable -> onCompressImageFailed(imageFile, throwable));
//    }
//
//    public Compressor initCompressor(Context context){
//        return new Compressor(context).setQuality(240).setCompressFormat(Bitmap.CompressFormat.JPEG);
//    }
//
//    private void onCompressImageSuccess( File file) {
//
//    }
//
//    private void onCompressImageFailed(File file,Throwable throwable) {
//
//    }
}
package com.lx.camera2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.openjdk.jol.info.ClassLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SuppressLint("newApi")

public class MainActivity extends AppCompatActivity {
    private TextureView textureView;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private Surface textureViewSurface;
    private Surface imageReaderSurface;
    private CameraCharacteristics cameraCharacteristics;
    private CameraCaptureSession cameraCaptureSession;
    private CameraCaptureSession previewSession;
    private Button btnPhoto;
    private ImageReader imageReader;
    private ImageView imageView;
    private MediaRecorder recorder;
    private CaptureRequest.Builder previewBuilder;
    private Button stopRecorder;
    private String mNextVideoAbsolutePath;
    private Size mVideoSize;
    private static final long NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 4L;

    CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            cameraCaptureSession = session;
            try {
                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
                builder.addTarget(textureViewSurface);
                cameraCaptureSession.setRepeatingRequest(builder.build(), null,null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = findViewById(R.id.textureView);
        btnPhoto = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        stopRecorder = findViewById(R.id.button3);
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},1);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();

            }
        });
        stopRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recorder!=null) {
                    recorder.stop();
                    recorder.release();
                    galleryAddVideo(MainActivity.this);
                }
            }

        });
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                textureViewSurface = new Surface(surface);
//                try {
//                    openCamera();
//                } catch (CameraAccessException e) {
//                    e.printStackTrace();
//                }
                StorageManager storageManager = (StorageManager) getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
                try {
                    UUID uuid = storageManager.getUuidForPath(getFilesDir());
                    //获取设备可供分配的内存空间大小
                    long allocatableBytes = storageManager.getAllocatableBytes(uuid);
                    Log.e("TAG", "galleryAddVideo: "+allocatableBytes/1024/1024/1024+"GB" );
                    if (allocatableBytes>=NUM_BYTES_NEEDED_FOR_MY_APP){
                        //声明应用所需空间
                        storageManager.allocateBytes(uuid,NUM_BYTES_NEEDED_FOR_MY_APP);
                    }else {
                        Intent intent = new Intent(Intent.ACTION_MANAGE_PACKAGE_STORAGE);
                        sendBroadcast(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }

        });
    }
    public boolean checkSelfPermissions(){

        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    public void switchCamera(){
        if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_FRONT){
            try {
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[1]);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }else {
            try {
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[0]);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("MissingPermission")
    public void openCamera() throws CameraAccessException {
          imageReader = ImageReader.newInstance(200,200, ImageFormat.JPEG,2);
         imageReaderSurface = imageReader.getSurface();
         imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
             @Override
             public void onImageAvailable(ImageReader reader) {
                 Log.i("image", "onImageAvailable: got a image");
                 Image image = reader.acquireNextImage();
                 ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                 final byte[] bytes = new byte[buffer.remaining()];
                 buffer.get(bytes);
                 image.close();
                 Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                 imageView.setImageBitmap(bitmap);
                 try {
                     galleryAddPicture(MainActivity.this,bitmap);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         },null);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
         cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[0]);
        StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
        if (checkSelfPermissions()) {
            cameraManager.openCamera(cameraManager.getCameraIdList()[0], new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    try {
                        cameraDevice.createCaptureSession(Arrays.asList(textureViewSurface, imageReaderSurface), sessionStateCallback, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {

                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {

                }
            }, null);
        }
    }
    private void takePhoto(){
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.set(CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_TORCH);
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            builder.set(CaptureRequest.FLASH_MODE,CaptureRequest.FLASH_MODE_TORCH);
            builder.addTarget(imageReaderSurface);
            builder.set(CaptureRequest.JPEG_ORIENTATION,90);
            //但是，这个方法是否可用，是依赖于底层的，
            // 也就是说，底层没有做相应的处理的话，设置之后才有效果，如果底层没有做相应的处理，是没有作用。（如三星手机是没有做相应的处理的）
            CaptureRequest captureRequest = builder.build();
//            List<CaptureRequest> list = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
//                list.add(captureRequest);
//            }
//            cameraCaptureSession.captureBurst(list,null,null);//一次拍10张
            cameraCaptureSession.capture(captureRequest,null,null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
    public void recorder(View view){
        try {
            prepareRecorder();
            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            previewBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
            previewBuilder.addTarget(recorder.getSurface());
            previewBuilder.addTarget(textureViewSurface);
            cameraDevice.createCaptureSession(Arrays.asList(recorder.getSurface(), textureViewSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    previewSession = session;
                    updatePreview();
                    recorder.start();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, null);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        try {
            previewSession.setRepeatingRequest(previewBuilder.build(),null,null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void prepareRecorder(){
         recorder = new MediaRecorder();
        //设置其它必要参数
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (mNextVideoAbsolutePath == null||mNextVideoAbsolutePath.isEmpty()){
            mNextVideoAbsolutePath = getVideoFilePath(this);
        }
        recorder.setOutputFile(mNextVideoAbsolutePath);
        recorder.setVideoEncodingBitRate(10000000);
        recorder.setVideoFrameRate(30);
    Log.e("TAG", "prepareRecorder: "+getWindowManager().getDefaultDisplay().getRotation() );
//        recorder.setMaxDuration(3000);//设置最大录像时间
        recorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOrientationHint(90);


        //参数设置完毕后，调用prepare函数
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    private String getVideoFilePath(Context context) {//获取路径函数
        final File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);//卸载应用，删除存储的文件
//        final File dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)；
        Log.e("TAG", "getVideoFilePath: "+dir.getAbsolutePath() );
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        File image = null;
        try {
             image = File.createTempFile(
                    "JPEG_"+timeStamp,
                    ".mp4",
                    dir
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image == null ? "":image.getAbsolutePath();
    }
    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        return choices[choices.length - 1];
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1&&checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
    private void galleryAddPicture(Context context,Bitmap bmp) throws IOException {
        File appDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        if (!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis()+".jpg";
        File file = new File(appDir,fileName);
         FileOutputStream fos = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
        fos.flush();
        fos.close();
        //第一种，通过mediaStore的方式保存图片到图库
        //一次性保存两张图片，一张在file.getAbsolutePath()里，一张在Pictures里
//        MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),fileName,null);
        Log.e("TAG", "galleryAddPicture: "+file.getAbsolutePath() );
//        //第二种，通过广播的方式保存图片到系统图库
//        Uri contentUri = Uri.fromFile(file);
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
//        context.sendBroadcast(mediaScanIntent);
//        Log.i("照相功能测试", "图片已被保存到图库");
        //第三种，通过MediaScannerConnection方法保存到系统图库，推荐使用,MediaScannerConnection只能用于应用独立文件
        MediaScannerConnection connection = new MediaScannerConnection(context,null);
        connection.connect();
        connection.scanFile(context,new String[]{file.getAbsolutePath()},new String[]{"image/jpeg"},null);
    }
    private void galleryAddVideo(Context context){
        MediaScannerConnection connection = new MediaScannerConnection(context,null);
        connection.connect();
        connection.scanFile(context,new String[]{mNextVideoAbsolutePath},new String[]{"video/mp4"},null);
    }


}


package com.inscripts.cometchatpulse.demo.Helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Presenters.GroupChatPresenter;
import com.inscripts.cometchatpulse.demo.Presenters.OneToOneActivityPresenter;
import com.inscripts.cometchatpulse.demo.Utils.FileUtils;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;

import java.io.File;
import java.net.URISyntaxException;

public class AttachmentHelper {


    public static void selectMedia(Activity activity,
                                   String Type, String[] extraMimeType, int requestCode) {

        final Intent intent = new Intent();
        intent.setType(Type);

//        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeType);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        try {
            activity.startActivityForResult(intent, requestCode);

        } catch (ActivityNotFoundException anfe) {
            Logger.debug("couldn't complete ACTION_OPEN_DOCUMENT, no activity found. falling back.");
        }
    }

    public static <P> void handleGalleryMedia(Context context, P presenter, Intent data, String contactId) {

        String type = null;
        File file = null;
        if (FileUtils.getMimeType(context, data.getData()).toLowerCase().contains("image")) {
            try {
                type = CometChatConstants.MESSAGE_TYPE_IMAGE;
                String filePath = FileUtils.getImageFilePath(data, context);

                if (filePath != null) {
                    file = new File(filePath);
                    Logger.error("handleGalleryMedia", "ACTION_SEND imagefile.exists() ? = " + file.exists() + " filepath : " + filePath);
                    if (file.exists()) {
                        sendMedia(file, contactId, type, presenter);
                    }
                } else {
                    filePath = data.getData().toString()
                            .replace("file://", "").
                                    replace("%20", " ");
                    file = new File(filePath);
                    Logger.error("handleGalleryMedia", "ACTION_SEND imagefile.exists() 2 ? = " + file.exists() + " filepath : " + filePath);
                    if (file.exists()) {
                        sendMedia(file, contactId, type, presenter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            type = CometChatConstants.MESSAGE_TYPE_VIDEO;
            try {
                String videoPath = FileUtils.getVideoPath(data.getData(), context);

                if (videoPath != null) {
                    file = new File(videoPath);
                    if (file.exists()) {
                        sendMedia(file, contactId, type, presenter);
                    }
                } else {
                    videoPath = data.getData().toString().replace("file://", "").
                            replace("%20", " ");
                    file = new File(videoPath);
                    if (file.exists()) {
                        sendMedia(file, contactId, type, presenter);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static <P> void handlefile(Context context, String type, P presenter, Intent data, String contactUid) {


        String filePath[] = new String[2];

        try {
            filePath = getPath(context, data.getData());

             if (filePath[0]!=null) {
                 sendMedia(new File(filePath[0]), contactUid, filePath[1], presenter);
             }
        } catch (Exception e) {
               e.printStackTrace();
        }


    }

    private static <P> void sendMedia(File file, String contactId, String type, P presenter) {
        if (presenter instanceof OneToOneActivityPresenter) {

            ((OneToOneActivityPresenter) presenter).sendMediaMessage(file, contactId, type);
        } else if (presenter instanceof GroupChatPresenter) {
            ((GroupChatPresenter) presenter).sendMediaMessage(file, contactId, type);
        }
    }

    public static <P> void handleCameraImage(Context context, P presenter, Intent data, String contactId) {
        File file = null;
        Logger.error("uri", String.valueOf(data.getData()));
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        Uri fileUri = MediaUtils.getImageUri(context, bitmap);

        Logger.error("", "fileUri: " + fileUri);
        file = new File(MediaUtils.ImagePath(fileUri, context));

        if (file.exists()) {

            if (presenter instanceof OneToOneActivityPresenter) {
                ((OneToOneActivityPresenter) presenter).sendMediaMessage(file, contactId, CometChatConstants.MESSAGE_TYPE_IMAGE);
            } else if (presenter instanceof GroupChatPresenter) {
                ((GroupChatPresenter) presenter).sendMediaMessage(file, contactId, CometChatConstants.MESSAGE_TYPE_IMAGE);
            }
        }

    }

    public static <P> void handleCameraVideo(Context context, P presenter, Intent data, String contactId) {
        String path = MediaUtils.getVideoPath(data.getData(), context);
        Logger.debug("handleCameraVideo", " Video Path" + path);
        File videoFile = new File(path);

        Logger.error("handleCameraVideo", " videoFile exists ? " + videoFile.exists());
        if (videoFile.exists()) {
            if (videoFile.exists()) {

                if (presenter instanceof OneToOneActivityPresenter) {
                    ((OneToOneActivityPresenter) presenter).sendMediaMessage(videoFile, contactId, CometChatConstants.MESSAGE_TYPE_VIDEO);
                } else if (presenter instanceof GroupChatPresenter) {
                    ((GroupChatPresenter) presenter).sendMediaMessage(videoFile, contactId, CometChatConstants.MESSAGE_TYPE_VIDEO);
                }
            }

        }
    }

    public static String[] getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        String[] ar;
        String type = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                ar = new String[]{Environment.getExternalStorageDirectory() + "/" + split[1], CometChatConstants.MESSAGE_TYPE_FILE};
                return ar;
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                type = split[0];
                if ("image".equals(type)) {
                    type = CometChatConstants.MESSAGE_TYPE_IMAGE;
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    type = CometChatConstants.MESSAGE_TYPE_VIDEO;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    type = CometChatConstants.MESSAGE_TYPE_AUDIO;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                type =context.getContentResolver().getType(uri).toLowerCase();
                if (type!=null){
                    if (type.contains("jpeg") || type.contains("jpg") || type.contains("png") || type.contains("image") || type.contains("picture") || type.contains("photo")) {
                        type = CometChatConstants.MESSAGE_TYPE_IMAGE;
                    } else if (type.contains("video") || type.contains("mp4") || type.contains("avi") ||
                            type.contains("flv")) {
                        type = CometChatConstants.MESSAGE_TYPE_VIDEO;
                    }
                    else {
                        type = CometChatConstants.MESSAGE_TYPE_FILE;
                    }
                }
                if (cursor.moveToFirst()) {
                    ar = new String[]{cursor.getString(column_index), type};
                    return ar;
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            ar = new String[]{uri.getPath(), CometChatConstants.MESSAGE_TYPE_FILE};
            return ar;
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void captureImage(Activity activity, int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, code);
    }

    public static void captureVideo(Activity activity, int code) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 15000000L);
        activity.startActivityForResult(intent, code);
    }

}

package com.adida.aka.androidgeneral.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.adida.aka.androidgeneral.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.adida.aka.androidgeneral.R.id.imageView;
import static com.adida.aka.androidgeneral.widget.Constans.REQUEST_CODE_FOLDER;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadAndDownLoadFragment extends android.app.Fragment {

    private Button mBtnUpload;
    private ImageView mImageView;
    String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_and_download, container, false);
        mBtnUpload = view.findViewById(R.id.btn_upload);
        mImageView = view.findViewById(imageView);
        addEvents();
        return view;
    }

    private void addEvents() {
        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new POST_FILE().execute("http://proakashj.esy.es/uploadFile.php");
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getActivity().startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });
    }

    class POST_FILE extends AsyncTask<String, Void, String> {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        @Override
        protected String doInBackground(String... params) {

            if (path != null) {
                File file = new File(path);
                String content_type = getType(file.getPath());
                String file_path = file.getAbsolutePath();

                RequestBody file_body = RequestBody.create(MediaType.parse(content_type), file);
                RequestBody requestBody = new MultipartBody.Builder()
                        .addFormDataPart("upload_file",
                                file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                        .setType(MultipartBody.FORM)
                        .build();
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setImage(Uri uri){
        path = getRealPathFromURI(uri);
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private String getType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}

    package com.thanhcs.com;

    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.os.AsyncTask;
    import android.widget.Toast;
    import com.example.bottombar.sample.R;
    import com.example.bottombar.sample.SignupActivity;
    import com.example.bottombar.sample.api.API;

    import java.io.File;
    import java.io.IOException;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.Map;

    import okhttp3.MediaType;
    import okhttp3.MultipartBody;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.RequestBody;
    import okhttp3.Response;

    /**
     * Created by thanhcs94 on 9/20/2016.
     */
    public class TaskSignUp extends AsyncTask<Void, Void , Void> {
        private final OkHttpClient client = new OkHttpClient();
        Response response = null;
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> hashParams;
        SignUpListener signUpListener;
        public interface SignUpListener {
            public void checkSignUp(Response respond);
        }

        public TaskSignUp(Activity activity , HashMap<String, String> hashParams){
            this.activity = activity;
            this.hashParams = hashParams;
            signUpListener = (SignUpListener ) activity;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle(activity.getResources().getString(R.string.app_name));
            progressDialog.setMessage("Sign up. Wait a moment..");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "";
            MultipartBody.Builder buildernew = new MultipartBody.Builder();
            buildernew.setType(MultipartBody.FORM);
            Map.Entry<String, String> lastEntry = null;
            Iterator<Map.Entry<String,String>> iter = hashParams.entrySet().iterator();
            while(iter.hasNext()) {
                lastEntry = iter.next();
            }
            for (Map.Entry<String,String> entry : hashParams.entrySet()) {
                System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
                if(lastEntry!=entry){
                    buildernew.addFormDataPart(entry.getKey(), entry.getValue());
                }else{
                    final MediaType MEDIA_TYPE = MediaType.parse(entry.getValue().endsWith("png") ? "image/png" : "image/jpeg");
                    buildernew.addFormDataPart(entry.getKey(), "FILE",
                            RequestBody.create(MEDIA_TYPE, new File(entry.getValue())))
                            .build();
                }

            }
            RequestBody requestBody = buildernew.build();
            Request request = new Request.Builder()
                    .url(API.SIG_UP)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response!=null&&!response.isSuccessful()) try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            try {
                if(response!=null)
                    Toast.makeText(activity, ""+response.body().string(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                 Toast.makeText(activity, ""+e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            signUpListener.checkSignUp(response);
            super.onPostExecute(aVoid);
        }
    }
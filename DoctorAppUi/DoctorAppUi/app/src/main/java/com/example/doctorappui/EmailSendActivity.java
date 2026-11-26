package com.example.doctorappui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public
class EmailSendActivity extends AppCompatActivity {

    ProgressDialog progressDialog = null;
    final static String TAG_EMAIL = "email";
    final static String TAG_SUB = "subject";
    final static String TAG_NAME = "name";
    final static String TAG_USERNAME = "username";
    final static String TAG_DATE = "date";
    final static String TAG_TIME = "time";
    final static String INSERT_URL="http://dotech2023.000webhostapp.com/send_email.php";

    SendData send;
    JSONObject jsonObject = null;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_send);

        new Insert().execute("patelchintan8358@gmail.com","strSub","strName","strUserName","strDate","strTime");

    }

    public class Insert extends AsyncTask {

        @Override
        protected
        void onPreExecute() {
            progressDialog=new ProgressDialog(EmailSendActivity.this);
            progressDialog.setTitle("Sending");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected
        Object doInBackground(Object[] objects) {
            send = new SendData();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TAG_EMAIL,objects[0].toString()));
            params.add(new BasicNameValuePair(TAG_SUB,objects[1].toString()));
            params.add(new BasicNameValuePair(TAG_NAME,objects[2].toString()));
            params.add(new BasicNameValuePair(TAG_USERNAME,objects[3].toString()));
            params.add(new BasicNameValuePair(TAG_DATE,objects[4].toString()));
            params.add(new BasicNameValuePair(TAG_TIME,objects[5].toString()));
            jsonObject = send.makeHttpRequest(INSERT_URL,"POST",params);

            return null;
        }

        @Override
        protected
        void onPostExecute(Object o) {
            progressDialog.dismiss();
            //Toast.makeText(MainActivity.this, ""+message+">>>"+status, Toast.LENGTH_SHORT).show();
            super.onPostExecute(o);
        }
    }


}
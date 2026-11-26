package com.example.dotechuser;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MeetingActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private String meetingId = "";
    private String participantName = "";
    private boolean micEnabled = true;
    private boolean webcamEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        webView = findViewById(R.id.webViewMeeting);
        progressBar = findViewById(R.id.meetingProgress);

        SharedPreferences preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        participantName = preferences.getString("name", "Patient");
        if (participantName == null || participantName.isEmpty()) {
            participantName = "Patient";
        }

        meetingId = getIntent().getStringExtra("meetingId");
        if (meetingId == null || meetingId.trim().isEmpty()) {
            meetingId = "Consult_" + System.currentTimeMillis();
        }

        meetingId = meetingId.replaceAll("[^a-zA-Z0-9_]", "_");

        TextView tvMeetingId = findViewById(R.id.tvMeetingId);
        if (tvMeetingId != null) {
            tvMeetingId.setText("Room: " + meetingId);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Video Consultation");
            getSupportActionBar().setSubtitle("Room: " + meetingId);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
        }

        checkPermissionsAndLoadMeeting();
        setupControls();
    }

    private void checkPermissionsAndLoadMeeting() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 101);
        } else {
            loadMeeting();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        loadMeeting();
    }

    private void loadMeeting() {
        if (webView == null) return;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        });

        String roomUrl = "https://meet.jit.si/DoTech_" + meetingId + "#config.prejoinPageEnabled=false&userInfo.displayName=" + Uri.encode(participantName);
        webView.loadUrl(roomUrl);
    }

    private void setupControls() {
        ImageView btnLeave = findViewById(R.id.btnLeave);
        if (btnLeave != null) {
            btnLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (webView != null) {
                        webView.loadUrl("about:blank");
                    }
                    Toast.makeText(MeetingActivity.this, "Consultation call ended", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        ImageView btnMic = findViewById(R.id.btnMic);
        if (btnMic != null) {
            btnMic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    micEnabled = !micEnabled;
                    Toast.makeText(MeetingActivity.this, micEnabled ? "Mic Enabled" : "Mic Muted", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ImageView btnWebcam = findViewById(R.id.btnWebcam);
        if (btnWebcam != null) {
            btnWebcam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webcamEnabled = !webcamEnabled;
                    Toast.makeText(MeetingActivity.this, webcamEnabled ? "Camera Enabled" : "Camera Disabled", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.destroy();
        }
        super.onDestroy();
    }
}

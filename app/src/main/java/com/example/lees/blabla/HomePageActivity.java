package com.example.lees.blabla;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lees on 6/16/2016.
 */
public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_page);

        final TextView displayName = (TextView) findViewById(R.id.display_name);
        final ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);
        final ImageView backgroundPic = (ImageView) findViewById(R.id.background_pic);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);

        Bundle extras = getIntent().getExtras();

        if (!extras.isEmpty()) {
            String name = extras.getString("name");
            String email = extras.getString("email");

            displayName.setText("Hello " + name + "!");

            String emailCode = convertEmailToMD5(email);
            loadImageFromGravatar(emailCode, profilePic);
        }

        final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
                ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(backgroundPic.getWidth(), (backgroundPic.getHeight() + 10));
                        backgroundPic.setLayoutParams(layoutParams);                    }
                };

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewTreeObserver observer = scrollView.getViewTreeObserver();
                observer.addOnScrollChangedListener(onScrollChangedListener);
                return false;
            }
        });

//        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            private ViewTreeObserver observer;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (observer == null) {
//                    observer = scrollView.getViewTreeObserver();
//                    observer.addOnScrollChangedListener(onScrollChangedListener);
//                } else if (!observer.isAlive()) {
//                    observer.removeOnScrollChangedListener(onScrollChangedListener);
//                    observer = scrollView.getViewTreeObserver();
//                    observer.addOnScrollChangedListener(onScrollChangedListener);
//                }
//                System.out.println("*****************************************");
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
//                backgroundPic.setLayoutParams(layoutParams);
//
//                return false;
//            }
//
//        });


//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//
//            @Override
//            public void onScrollChanged() {
//                System.out.println("*****************************************");
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
//                backgroundPic.setLayoutParams(layoutParams);
//            }
//        });
    }

    protected static String convertEmailToMD5(String address) {
        byte[] bytes = null;
        byte[] digested = null;
        String res = null;

        try {
            bytes = address.toLowerCase().trim().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        digested = md.digest(bytes);

        StringBuffer sb = new StringBuffer();
        for (byte b : digested) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    protected void loadImageFromGravatar(String address, ImageView view) {
        Picasso.with(this)
                .load(getString(R.string.gravatar_base_url) + address)
                .into(view);
    }

//    public void resizeImageView(ImageView img) {
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
//        img.setLayoutParams(layoutParams);
//    }
}

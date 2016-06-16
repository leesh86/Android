package com.example.lees.blabla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

        Bundle extras = getIntent().getExtras();
        TextView displayName = (TextView) findViewById(R.id.display_name);
        ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);

        if (!extras.isEmpty()) {
            String name = extras.getString("name");
            String email = extras.getString("email");

            displayName.setText("Hello " + name + "!");

            String emailCode = convertEmailToMD5(email);
            loadImageFromGravatar(emailCode, profilePic);
        }
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
}

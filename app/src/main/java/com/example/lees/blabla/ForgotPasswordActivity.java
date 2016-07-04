package com.example.lees.blabla;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lees on 6/15/2016.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private DBUserAdapter dbUser = new DBUserAdapter(ForgotPasswordActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_forgot_password);

        final Button send = (Button)findViewById(R.id.button_send);
        final String successMsg = getString(R.string.password_success_toast_msg);
        final String failureMsg = getString(R.string.password_failure_toast_msg);
        final CharSequence email = ((EditText)findViewById(R.id.email)).getText();
        final String subject = getString(R.string.email_subject);
        final String emailMsg = getString(R.string.email_msg);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                if (isEmailValid(email) && dbUser.doesEmailExistInDB(email.toString())==true) {
                    Intent i = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                    startActivity(i);

//                    toast = Toast.makeText(getApplicationContext(), successMsg, Toast.LENGTH_SHORT);
//                    sendEmail((String)email, subject, emailMsg);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wrong Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    protected void sendEmail(String email, String subject, String msg) {
//        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//        String[] recipients = new String[]{(String)email, "",};
//        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
//        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
//        emailIntent.setType("text/plain");
//        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//        finish();


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto:"));
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, email);
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

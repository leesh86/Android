package com.example.lees.blabla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.lees.blabla.ForgotPasswordActivity.isEmailValid;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final TextView haveAccount = (TextView) findViewById(R.id.have_account);
        final Button signUp = (Button) findViewById(R.id.button_sign_up);
        final TextView noAccount = (TextView) findViewById(R.id.dont_have_account);
        final TextView forgotPassword = (TextView) findViewById(R.id.forgot_password);
//        final LinearLayout ll = (LinearLayout)findViewById(R.id.linear_layout);
        final CharSequence emailAddress = ((EditText) findViewById(R.id.email)).getText();
        final EditText emailField = ((EditText) findViewById(R.id.email));
        final String failureMsg = getString(R.string.password_failure_toast_msg);
        final CharSequence name = ((EditText) findViewById(R.id.full_name)).getText();
        final EditText nameField = ((EditText) findViewById(R.id.full_name));
        final CharSequence password = ((EditText) findViewById(R.id.password)).getText();
        final EditText passwordField = ((EditText) findViewById(R.id.password));


        haveAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                signUp.setText(getString(R.string.button_sign_in));
                haveAccount.setVisibility(View.GONE);
                noAccount.setVisibility(View.VISIBLE);
                forgotPassword.setVisibility(View.VISIBLE);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                signUp.setText(getString(R.string.button_sign_up));
                noAccount.setVisibility(View.GONE);
                forgotPassword.setVisibility(View.GONE);
                haveAccount.setVisibility(View.VISIBLE);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                boolean isSomething = true;
                if (emailAddress.toString().isEmpty()) {
                    isSomething = false;
                    emailField.setError("Email address is required!");
                }
                if (name.toString().isEmpty()) {
                    isSomething = false;
                    nameField.setError("Name is required!");
                }
                if (password.toString().isEmpty()) {
                    isSomething = false;
                    passwordField.setError("Password is required!");
                }
                if (!isEmailValid(emailAddress)) {
                    isSomething = false;
                    Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
                }
                if (isSomething == true) {
                    Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name.toString());
                    bundle.putString("email", emailAddress.toString());
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

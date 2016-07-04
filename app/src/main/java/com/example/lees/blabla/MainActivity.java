package com.example.lees.blabla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import static com.example.lees.blabla.ForgotPasswordActivity.isEmailValid;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DBUserAdapter dbUser = new DBUserAdapter(MainActivity.this);

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
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
        final Button signIn = (Button) findViewById(R.id.button_sign_In);
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
                signUp.setVisibility(View.GONE);
                signIn.setVisibility(View.VISIBLE);
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
                signIn.setVisibility(View.GONE);
                signUp.setVisibility(View.VISIBLE);
                noAccount.setVisibility(View.GONE);
                forgotPassword.setVisibility(View.GONE);
                haveAccount.setVisibility(View.VISIBLE);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                boolean isFormOK = isLoginFormFilled(emailAddress.toString(), emailField, password.toString(), passwordField, name.toString(), nameField);
                if (isFormOK == true) {
                    addRecordToDBAndStartNextActivity(emailAddress.toString(), password.toString(), name.toString());
                }
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                boolean isFormOK = isLoginFormFilled(emailAddress.toString(), emailField, password.toString(), passwordField, name.toString(), nameField);
                if (isFormOK == true) {
                    loginAndStartNextActivity(emailAddress.toString(), password.toString(), name.toString());
                }
            }
        });

    }

    private boolean isLoginFormFilled(String email, EditText emailField, String password, EditText passwordField, String name, EditText nameField) {
        boolean isSomething = true;
        if (email.isEmpty()) {
            isSomething = false;
            emailField.setError("Email address is required!");
        }
        if (name.isEmpty()) {
            isSomething = false;
            nameField.setError("Name is required!");
        }
        if (password.isEmpty()) {
            isSomething = false;
            passwordField.setError("Password is required!");
        }
        if (!isEmailValid(email) && !email.isEmpty()) {
            isSomething = false;
            Toast.makeText(getApplicationContext(), getString(R.string.password_failure_toast_msg), Toast.LENGTH_SHORT).show();
        }
        return isSomething;
    }

    private void addRecordToDBAndStartNextActivity(String email, String password, String name) {

        Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("email", email);
        i.putExtras(bundle);

        try {
            dbUser.open();
            if (!dbUser.Login(email, password)) {
                dbUser.addUser(email, password);
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "User is already registered", Toast.LENGTH_LONG).show();
            }

            dbUser.close();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loginAndStartNextActivity(String email, String password, String name) {
        Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("email", email);
        i.putExtras(bundle);

        try {
            dbUser.open();

            if (dbUser.Login(email, password)) {
                Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "Invalid Email/Password", Toast.LENGTH_LONG).show();
            }
            dbUser.close();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }

//    public void fireBaseCreateUser(String email, String password) {
//        final Firebase myFirebaseRef = new Firebase(getString(R.string.firebase_url));
//        myFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
//            @Override
//            public void onSuccess(Map<String, Object> result) {
//                System.out.println("Successfully created user account with uid: " + result.get("uid"));
//            }
//
//            @Override
//            public void onError(FirebaseError firebaseError) {
//                System.out.println("Failed to created user account: " + firebaseError);
//            }
//        });
//    }

}

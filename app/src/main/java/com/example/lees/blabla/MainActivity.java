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
                if (!isEmailValid(emailAddress) && !emailAddress.toString().isEmpty()) {
                    isSomething = false;
                    Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
                }
                if (isSomething == true) {
                    Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name.toString());
                    bundle.putString("email", emailAddress.toString());
                    i.putExtras(bundle);
                    mAuth = FirebaseAuth.getInstance();
                    mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                // User is signed in
                                System.out.println("onAuthStateChanged:signed_in:" + user.getUid());
//                                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                            } else {
                                // User is signed out
                                System.out.println("onAuthStateChanged:signed_out");
//                                Log.d(TAG, "onAuthStateChanged:signed_out");
                            }
                            // ...
                        }
                    };
                    mAuth.createUserWithEmailAndPassword(emailAddress.toString(), password.toString());
//                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//                                    // If sign in fails, display a message to the user. If sign in succeeds
//                                    // the auth state listener will be notified and logic to handle the
//                                    // signed in user can be handled in the listener.
//                                    if (!task.isSuccessful()) {
//                                        Toast.makeText(MainActivity.this, "Authentication failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    // ...
//                                }
//                            });
                    // ...
//                    fireBaseCreateUser(emailAddress.toString(), password.toString());
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

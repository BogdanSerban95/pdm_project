package project.passwordproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.passwordproject.R;

/**
 * Created by Wolf on 30.11.2016.
 */

public class SignActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText regis_email;
    private EditText regis_password;
    private EditText confirm_password;
    private String email, password;
    private DatabaseReference myFirebaseRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean passwordsMatching = false;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity);
        mAuth = FirebaseAuth.getInstance();
        //Creates a reference for  your Firebase database
        myFirebaseRef = FirebaseDatabase.getInstance().getReference();

        regis_email = (EditText) findViewById(R.id.email);
        regis_password = (EditText) findViewById(R.id.pass);
        confirm_password = (EditText) findViewById(R.id.confirm_pass);
        cancelButton = (Button) findViewById(R.id.btn_cancel);

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass_text = regis_password.getText().toString();
                if (!pass_text.isEmpty() && pass_text.compareTo(s.toString()) != 0) {
                    confirm_password.setError("Passwords must be the same!");
                    passwordsMatching = false;
                } else {
                    passwordsMatching = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(getApplicationContext(), "User signed in", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Toast.makeText(getApplicationContext(), "User signed out", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };

        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = regis_email.getText().toString();
                password = regis_password.getText().toString();
                String conf_pass = confirm_password.getText().toString();
                if (email.isEmpty()) {
                    regis_email.setError("You must enter email!");
                } else {
                    if (password.isEmpty()) {
                        regis_password.setError("You must enter password!");
                    } else {
                        if (passwordsMatching) {
                            createAccount();
                        }
                    }
                }


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

    }

    public void createAccount() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sorry!Your registration failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Success! Please login with your credentials",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            SignActivity.this.finish();
                        }
                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

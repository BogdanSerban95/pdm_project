package project.passwordproject.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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


public class LoginActivity extends AppCompatActivity {

    private Button login,sign,reset;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText email_ET,password_ET;
    private String email,password;
    private DatabaseReference myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email_ET=(EditText)findViewById(R.id.emailtv);
        password_ET=(EditText)findViewById(R.id.passtv);

        myFirebaseRef = FirebaseDatabase.getInstance().getReference();

        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        login=(Button)findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email_ET.getText()) || TextUtils.isEmpty(password_ET.getText()))
                    Toast.makeText(LoginActivity.this, "Please enter email and password",
                            Toast.LENGTH_SHORT).show();
                else{
                    email=email_ET.getText().toString();
                    password=password_ET.getText().toString();
                    new ConnectionWaiter().execute();
                }
            }
        });

        sign=(Button)findViewById(R.id.signBtn);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),SignActivity.class);
                startActivity(i);
            }
        });

        reset=(Button)findViewById(R.id.resetPassBtn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RPActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
           firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public class ConnectionWaiter extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog dialog=null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Intent i=new Intent(getApplicationContext(),MenuActivity.class);
                                startActivity(i);
                                Toast.makeText(LoginActivity.this, "Authentication succeeded.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //dialog.dismiss();
        }
    }
}


package com.example.itemorganizer;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountSignup extends AppCompatActivity {

    private final String TAG = "AccountSignup";

    private FirebaseAuth mAuth;

    private EditText eName;
    private EditText ePass;
    private EditText eConfPass;
    private EditText eEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signup);
        mAuth = FirebaseAuth.getInstance();

        //get buttons
        eName = findViewById(R.id.name_edit);
        eEmail = findViewById(R.id.email_edit);
        ePass = findViewById(R.id.password_edit);
        eConfPass = findViewById(R.id.confirm_pass);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert (currentUser == null);
    }

    public void createUser(View view) {
        String password = ePass.getText().toString();
        String conf_pass = eConfPass.getText().toString();

        //name is not handled yet

        //if passwords do not match
        if (!password.equals(conf_pass)) {
            Toast.makeText(AccountSignup.this, "Passwords do not match",
                    Toast.LENGTH_SHORT).show();

            MakeClear.clearView(eConfPass, ePass);
            return;
        }

        String email = eEmail.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //register name here
                            openFamilyPage();

                        } else {
                            // If sign in fails, display a message to the user.
                            // firebase has 6 letter passwords so add that in the hint
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AccountSignup.this, "Connection Failed",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private void openFamilyPage(){

        Intent intent = new Intent(this, FamilyLogIn.class);
        startActivity(intent);
    }

}

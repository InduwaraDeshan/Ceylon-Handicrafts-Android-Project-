package lk.zeamac.app.handicraftadmin.Activity;


import android.os.Bundle;

import lk.zeamac.app.handicraftadmin.R;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class SignInActivity extends AppCompatActivity {

    TextView forgotPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CeylonHandicraft_FullScreen);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        EditText emailEditText = findViewById(R.id.editTextSignInPersonName);
        EditText passwordEditText = findViewById(R.id.editTextSignInPassword);
        forgotPassword = findViewById(R.id.textViewRemember);


        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please Add Your Email", Toast.LENGTH_LONG).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignInActivity.this, "Enter valid email address", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please Add Your Password", Toast.LENGTH_LONG).show();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        DocumentReference documentReference1 = fireStore.collection("Users")
                                                .document(firebaseAuth.getCurrentUser().getUid());

                                        documentReference1.addSnapshotListener(SignInActivity.this, new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                    // Handle errors
                                                    return;
                                                }

                                                if (value.exists()) {
                                                    userType = value.getString("type");
                                                    System.out.println("User Type: " + userType);

                                                    if (userType.equals("admin")) {
                                                        updateUI(firebaseAuth.getCurrentUser());
                                                    } else {
                                                        Toast.makeText(SignInActivity.this, "You Aren't Admin..", Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            }
                                        });


                                    } else {
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            Toast.makeText(SignInActivity.this, "Invalid Email Or Password", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });


        EditText password = findViewById(R.id.editTextSignInPassword);
        CheckBox showPassword = findViewById(R.id.checkBoxEnterPassword);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                password.setSelection(password.getText().length());
            }
        });
// ForgotPassword

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.view_holder_forgot_password, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userEmail = emailBox.getText().toString();

                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            Toast.makeText(SignInActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });


// ForgotPassword


    }


    //Update Ui Part
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (!user.isEmailVerified()) {
                Toast.makeText(SignInActivity.this, "Please Verify Your Email", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_LONG).show();


            finish();
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);


        }
    }
//Update Ui Part


}

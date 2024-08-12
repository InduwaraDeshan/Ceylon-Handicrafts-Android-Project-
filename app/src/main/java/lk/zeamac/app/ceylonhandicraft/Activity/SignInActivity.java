package lk.zeamac.app.ceylonhandicraft.Activity;

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
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Pattern;

import lk.zeamac.app.ceylonhandicraft.R;

public class SignInActivity extends AppCompatActivity {
    private SignInClient signInClient;

    private FirebaseAuth firebaseAuth;
    public static String PREFS_NAME = "MyPrefsFile";
    TextView forgotPassword;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CeylonHandicraft_FullScreen);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        signInClient = Identity.getSignInClient(getApplicationContext());


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

                                        SharedPreferences sharedPreferences = getSharedPreferences(SignInActivity.PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putBoolean("hasLoggedIn", true);
                                        editor.commit();

                                        updateUI(firebaseAuth.getCurrentUser());
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
//            Google SignIn
        findViewById(R.id.imageViewGoogleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetSignInIntentRequest getSignInIntentRequest = GetSignInIntentRequest.builder()
                        .setServerClientId(getString(R.string.web_client_id))
                        .build();

                Task<PendingIntent> signInIntent = signInClient.getSignInIntent(getSignInIntentRequest);
                signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest
                                .Builder(pendingIntent).build();
                        signInLauncher.launch(intentSenderRequest);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
//            Google SignIn

        findViewById(R.id.textViewDontHaveAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
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

    //Google SignIn part
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);

        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void handleSignInResult(Intent intent) {
        try {
            SignInCredential signInCredential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = signInCredential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);
        } catch (ApiException e) {


        }
    }

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            handleSignInResult(o.getData());
                        }
                    });


    //Google SignIn part

    //Update Ui Part
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (!user.isEmailVerified()) {
                Toast.makeText(SignInActivity.this, "Please Verify Your Email", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(SignInActivity.this, "Login Success. Shop NOW \uD83D\uDC49\uD83C\uDFFC", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();


        }
    }
//Update Ui Part


}

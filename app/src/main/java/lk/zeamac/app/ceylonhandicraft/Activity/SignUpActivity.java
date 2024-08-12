package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

import lk.zeamac.app.ceylonhandicraft.Entity.UserEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CeylonHandicraft_FullScreen);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        EditText nameEditText = findViewById(R.id.editTextPersonName);
        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        EditText mobileEditText = findViewById(R.id.editTextMobile);

//
//        if (firebaseAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
//            finish();
//        }



        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();


                if (name.isEmpty()) {

                    Toast.makeText(SignUpActivity.this, "Please Add Your Name", Toast.LENGTH_LONG).show();
                }
                if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Add Your Email", Toast.LENGTH_LONG).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Enter valid email address", Toast.LENGTH_LONG).show();
                }  else if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Add Your Password", Toast.LENGTH_LONG).show();
                } else if (mobile.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Add Your Mobile", Toast.LENGTH_LONG).show();
                } else if (mobile.length() != 10) {
                    Toast.makeText(SignUpActivity.this, "Invalid Mobile", Toast.LENGTH_LONG).show();
                } else {


                    String id = UUID.randomUUID().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.sendEmailVerification();
                                userId = firebaseAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = fireStore.collection("Users").document(userId);
                                UserEntity userEntity = new UserEntity(id,name,null,email,"user",null,null,mobile);
                                documentReference.set(userEntity).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(SignUpActivity.this, "Registration Successful , Please Verify Your Email", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                                    }
                                });

                            } else {
                                Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });


        findViewById(R.id.textViewAlreadyRegistered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        EditText password1 = findViewById(R.id.editTextPassword);
        CheckBox showPassword = findViewById(R.id.checkBoxEnterPassword);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                password1.setSelection(password1.getText().length());
            }
        });
    }
}
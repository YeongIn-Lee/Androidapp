package com.example.mysns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.String;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void signUp() {
        String email = ((EditText) findViewById(R.id.EmailAddress_text)).getText().toString();
        String password = ((EditText) findViewById(R.id.Password_text)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.ConfirmPassword_text)).getText().toString();

        if(email.length()>0 && password.length()>0 && passwordCheck.length() >0) {
            if(password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    ToastStart("회원가입에 성공했습니다.");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startMyActivity(MainActivity.class);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthWeakPasswordException e) {
                                        ToastStart("비밀번호는 최소 6자리 이상입니다.");
                                    } catch(FirebaseAuthInvalidCredentialsException e) {
                                        ToastStart("이메일형식이 아닙니다.");
                                    } catch(FirebaseAuthUserCollisionException e) {
                                        ToastStart("이미 존재하는 이메일입니다.");
                                    } catch(Exception e) {
                                        ToastStart("다시 확인해주세요.");
                                    }
                                }
                            }
                        });
            }else {
                ToastStart("비밀번호가 일치하지 않습니다.");
            }
        }else{
            ToastStart("이메일 또는 비밀번호를 입력해 주세요.");
        }
    }

     private void ToastStart(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void startMyActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
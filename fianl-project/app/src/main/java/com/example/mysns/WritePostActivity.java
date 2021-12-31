package com.example.mysns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class WritePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        findViewById(R.id.send_post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writePost();
            }
        });
    }
    private void writePost() {

        String myTitle,myContext;
        try{
            myTitle = ((EditText)findViewById(R.id.title_edit)).getText().toString();
            myContext = ((EditText) findViewById(R.id.context_edit)).getText().toString();

            if(myTitle.length()==0) {ToastStart("제목을 확인하세요."); return;}
            if(myContext.length()==0) {ToastStart("길이를 확인하세요."); return;}


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Post post = new Post(myTitle,myContext,user.getUid(),new Timestamp(new Date()));

            if(user!=null){
                db.collection("posts").add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                ToastStart("게시글 등록을 완료했습니다.");
                                startMainActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                ToastStart("게시글 등록에 실패하였습니다.");
                            }
                        });
            }
        }catch(Exception e){
            ToastStart(e.toString());
        }
    }
    private void ToastStart(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void startMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
package com.example.mysns;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ReadPostActivity extends AppCompatActivity {
    private String docId;
    private String name;
    private String PID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemovePost();
            }
        });

        findViewById(R.id.subPost_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    String myContext;
                    myContext = ((EditText)findViewById(R.id.subPost_edit)).getText().toString();
                    if(myContext.length()==0) {ToastStart("????????? ???????????????."); return;}

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    SubPost subpost = new SubPost(myContext,user.getUid(),new Timestamp(new Date()));

                    if(user!=null){
                        db.collection("posts").document(docId).collection("subpost").add(subpost)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        ToastStart("?????? ????????? ??????????????????.");
                                        setSubPostList();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ToastStart("?????? ????????? ?????????????????????.");
                                    }
                                });
                    }
                }catch(Exception e){
                    ToastStart(e.toString());
                }
            }
        });

        setPostList();
        setSubPostList();
    }

        private void ToastStart (String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }

        private void setPostList () {
            Intent intent = getIntent();
            String title = intent.getExtras().getString("title");
            String context = intent.getExtras().getString("context");
            docId = intent.getExtras().getString("docId");
            String pId = intent.getExtras().getString("pId");
            PID = new String(pId);
            getNickname(pId);

            TextView textTitle = (TextView) findViewById(R.id.title_textView);
            TextView textContext = (TextView)findViewById(R.id.context_textView);

            textTitle.setText(title);
            textContext.setText(context);
        }

        private void setSubPostList(){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ((LinearLayout)findViewById(R.id.subPost_layout)).removeAllViews();
            db.collection("posts").document(docId).collection("subpost")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int i=0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String temp = document.getString("context");
                                    String temp2 = document.getString("publisher");

                                    getNickname(temp,temp2);
                                }
                                ToastStart("???????????????.");
                            } else {
                                ToastStart("??????????????? ??????????????????.");
                            }
                        }
                    });
        }

        private void getNickname(String context,String str){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(str);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                           name = new String(document.getString("name"));
                           addView(context,name);
                        } else {
                           name=  new String("nameless");
                           addView(context,name);
                        }
                    } else {
                        ToastStart(task.getException().toString());
                    }
                }
            });
        }

        private void getNickname(String pid){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(pid);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            ((TextView)findViewById(R.id.publisher_text)).setText(document.getString("name"));

                        } else {
                            ((TextView)findViewById(R.id.publisher_text)).setText("??????");
                        }
                    } else {
                        ToastStart(task.getException().toString());
                    }
                }
            });
        }
        private void addView(String context, String publisher){
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(5,5,5,5);
            ll.setLayoutParams(param);
            /*
               ?????? ????????? or ????????? ?????????
             */
            TextView nickname = new TextView(ReadPostActivity.this);
            nickname.setTextSize(25);
            nickname.setText(publisher);
            ll.addView(nickname);

            TextView textview = new TextView(ReadPostActivity.this);
            textview.setTextSize(20);
            textview.setText(context);

            ll.addView(textview);
            View view = new View(ReadPostActivity.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2));
            view.setBackgroundColor(Color.parseColor("#FFFF0000"));
            ll.addView(view);
            ((LinearLayout)findViewById(R.id.subPost_layout)).addView(ll);
        }

        private void RemovePost(){

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if(!(user.getUid()).equals(PID)){
                ToastStart("????????? ????????? ????????????.");
                return;
            }
            db.collection("posts").document(docId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           ToastStart("????????????");
                           finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ToastStart("????????????");
                        }
                    });
      }
    }


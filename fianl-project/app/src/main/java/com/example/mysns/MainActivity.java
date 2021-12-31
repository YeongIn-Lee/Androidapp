package com.example.mysns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.w3c.dom.Text;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Post> postlist = new ArrayList<>();  // post 를 담고있는 리스트                          //파이어베이스를 통해 받은 커스텀 객체들의 집합
    private ArrayList<String> idList = new ArrayList<>();
    @Override
    public void onClick(View v) {

        int position = (Integer)v.getTag();
        Post m_data = postlist.get(position);

        Intent intent = new Intent(this, ReadPostActivity.class);
        intent.putExtra("title",m_data.getTitle());
        intent.putExtra("context",m_data.getContext());
        intent.putExtra("docId",idList.get(position));
        intent.putExtra("pId",m_data.getPublisher());
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        postlist.clear();
        idList.clear();
        ((LinearLayout)findViewById(R.id.list_post)).removeAllViews();
        setPostList();
    }

    //커스텀 객체의 title 을 받은 TextView 객체들의 집합
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startLoginActivity();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
            }
        });

        findViewById(R.id.goto_memInfoConfirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(MemInfoConfirmActivity.class);
            }
        });

        findViewById(R.id.goto_writePost_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(WritePostActivity.class);
            }
        });
    }
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void startMyActivity (Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    private void ToastStart(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void setPostList(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idList.add(document.getId());
                                postlist.add(document.toObject(Post.class));
                                String temp = document.getString("title");
                                String context = document.getString("context");
                                addView(i++,temp,context);
                            }
                            ToastStart("성공");
                        } else {
                            ToastStart("불러오기에 실패했습니다.");
                        }
                    }
                });
    }

    private void addView(int i,String title,String context){
        //linear layout 토대
        LinearLayout ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.parseColor("#8EDD91"));
        ll.setTag(i);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(15,15,15,15);
        ll.setLayoutParams(param);
        //제목 추가

        TextView textview = new TextView(MainActivity.this);
        textview.setTextSize(30);
        if(title.length()>10){
            title = (title.substring(0,10)).concat("...");
        }
        textview.setText(title);
        ll.addView(textview);
        //  내용 추가 작은 글씨로
        TextView textview2 = new TextView(MainActivity.this);
        textview2.setTextSize(20);
        if(context.length()>35){
            context = (context.substring(0,35)).concat("...");
        }
        textview2.setText(context);
        ll.addView(textview2);

        ll.setOnClickListener(MainActivity.this);
        ((LinearLayout)findViewById(R.id.list_post)).addView(ll);
    }


}
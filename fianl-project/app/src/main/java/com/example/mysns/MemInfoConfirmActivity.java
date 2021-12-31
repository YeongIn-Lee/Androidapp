package com.example.mysns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class MemInfoConfirmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startLoginActivity();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meminfo_confirm);

        setTable();


        findViewById(R.id.goto_memInfo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(MemInfoActivity.class);
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

    public void setTable(){
        final String[] date = new String[1];
        final String[] phone = new String[1];
        final String[] address = new String[1];
        final String[] name = new String[1];

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name[0] = document.getString("name");
                        date[0] = document.getString("date");
                        phone[0] = document.getString("phone");
                        address[0] = document.getString("address");

                        ((TextView)findViewById(R.id.textAddress)).setText(address[0]);
                        ((TextView)findViewById(R.id.textBorn)).setText(date[0]);
                        ((TextView)findViewById(R.id.textName)).setText(name[0]);
                        ((TextView)findViewById(R.id.textPhone)).setText(phone[0]);
                    } else {
                        ToastStart("자료가 없습니다.");
                    }
                } else {
                    ToastStart(task.getException().toString());
                }
            }
        });
    }
}

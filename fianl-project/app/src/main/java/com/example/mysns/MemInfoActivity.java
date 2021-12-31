package com.example.mysns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meminfo);

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memInfoUpdate();
            }
        });

    }

    private void memInfoUpdate() {

        String name,date,phone,address;

        try{
            name = ((EditText)findViewById(R.id.editName)).getText().toString();
            date = ((EditText) findViewById(R.id.editDate)).getText().toString();
            phone = ((EditText) findViewById(R.id.editPhone)).getText().toString();
            address = ((EditText) findViewById(R.id.editAddress)).getText().toString();

            if(name.length()==0) {ToastStart("이름을 확인하세요."); return;}
            if(date.length()<6) {ToastStart("날짜를 확인하세요."); return;}
            if(phone.length()<10) {ToastStart("전화번호를 확인하세요."); return;}
            if(address.length()==0) {ToastStart("주소를 확인하세요."); return;}

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            memInfo member = new memInfo(name, date, phone,address);

            if(user!=null){
                db.collection("users").document(user.getUid())
                        .set(member)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ToastStart("회원정보 등록에 성공하였습니다.");
                                startMainActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                ToastStart("회원정보 등록에 실패하였습니다.");
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

    private void startMyActivity(Class c,int num){ //두번째 파라매터가 0이 아닌 정수일 경우 액티비티 스택을 지우고 이동한다.
        Intent intent = new Intent(this,c);
        if(num!=0){intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);}
        startActivity(intent);
    }
}



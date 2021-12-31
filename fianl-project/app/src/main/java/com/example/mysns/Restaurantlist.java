package com.example.mysns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class Restaurantlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantlist);

        setTitle("맛집랭크");

        final String[] mid = {"신육간",
                "리멘시타",
                "지앤토비코",
                "나오리쇼쿠",
                "카페 이레",
                "11월29일",
                "톤쇼우",
                "소사베이커리",
                "포맨티코",
                "설어정 부산대점",
                "띠리오",
                "크라운닭발 부산대본점",
                "카츠안",
                "서빙고",
                "오공복이",
                "텐동 코하루",
                "이솝 페이블",
                "보노베리",
                "우쭈쭈",
                "카페안집",
                "첫눈에찜한닭",
                "대길고추불고기",
                "델라고",
                "꿈을살다",
                "Nujabes414",
                "뭄바이",
                "야마벤또",
                "17테이블",
                "텐동롯코",
                "구르메식당",
                "하늘동831",
                "벤스하버",
                "보이후드",
                "개성상인",
                "동전집 부산대점",
                "우마이도",
                "카카오두",
                "칼집삼겹",
                "껍사랑목사랑",
                "비윤",
                "에슈에뜨",
                "맥가이버 손칼국수",
                "가야공원돼지국밥",
                "엉클밥",
                "야마즈미우동",
                "카메",
                "인디저트",
                "안다미로",
                "황금칼국수",
                "봄까스",
                "리틀프랑",
                "타코스패밀리",
                "더만족",
                "참나무숯불갈비",
                "싸움의고수",
                "순곱이네",
                "정진영펍",
                "테라스원",
                "키햐아",
                "가네쉬",
                "닭갈비제작소 부산대점",
                "칼맛나는 푸짐한횟집",
                "디델리 부산대점",
                "아웃닭",
                "정희옥 스시 장전점",
                "호우양꼬치",
                "인디키친",
                "유림부대밀면",
                "꼬모도",
                "대독장",
                "이흥용과자점 부산대점",
                "맥도날드 부산대2호점",
                "진주 비봉식당",
                "빨봉분식",
                "닭발공장",
                "무한리필 뙈지야",
                "반디나무",
                "투썸플레이스 부산대정문점",
                "스타벅스 부산대정문점",
                "수라국밥",
                "일월육일",
                "그집짬뽕0927 부산대본점",
                "히로",
                "마리웨일237 부산대점"
        };

        ListView list = (ListView) findViewById(R.id.listView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mid);
        list.setAdapter(adapter);

        Button restaurant_list = (Button) findViewById(R.id.restaurant_list);
        restaurant_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Restaurantlist.this, BlogActivity.class);
                intent.putExtra("find", mid[position]);
                startActivity(intent);
            }
        });

    }
}
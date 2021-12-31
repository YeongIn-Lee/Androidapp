package com.example.mysns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RequestQueue queue;
    private String clientId = "vtOYrv8OWZUoEGbC3Unk";
    private String clientSecret = "lGpE7aUCew";
    Intent intent;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        intent = getIntent();
        text = intent.getStringExtra("find");
        text = "부산대 " + text;
        Log.d("text=>", text);
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        Button button = (Button)findViewById(R.id.see_map);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        queue = Volley.newRequestQueue(this);

        getblogs();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), Storelocation.class);
                intent2.putExtra("find2", text);
                startActivity(intent2);
            }
        });

    }

    public void getblogs(){

        String url = "https://openapi.naver.com/v1/search/blog?query=" + text;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        //Log.d("Response =>", response);

                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            JSONArray arrayblog = jsonObj.getJSONArray("items");

                            List<BlogData> blog = new ArrayList<>();

                            for(int i = 0, j = arrayblog.length(); i<j; i++){
                                JSONObject obj = arrayblog.getJSONObject(i);

                                Log.d("Blog =>", obj.toString());

                                BlogData blogData = new BlogData();
                                blogData.setTitle(obj.getString("title"));
                                blogData.setContent(obj.getString("description"));
                                blogData.setUrl(obj.getString("link"));
                                blog.add(blogData);

                            }

                            mAdapter = new MyAdapter(blog, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(v.getTag() != null){
                                        int position = (int)v.getTag();
                                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(((MyAdapter)mAdapter).getBlog(position).getUrl()));
                                        startActivity(intent2);
                                    }
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error=>", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);
                Log.d("getHeader =>",""+params);
                get(url,params);
                return params;
            }

            public String get(String apiUrl, Map<String, String> requestHeaders){
                HttpURLConnection con = connect(apiUrl);
                try {
                    con.setRequestMethod("GET");
                    for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                        con.setRequestProperty(header.getKey(), header.getValue());
                    }

                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                        return readBody(con.getInputStream());
                    } else { // 에러 발생
                        return readBody(con.getErrorStream());
                    }
                } catch (IOException e) {
                    throw new RuntimeException("API 요청과 응답 실패", e);
                } finally {
                    con.disconnect();
                }
            }

            public HttpURLConnection connect(String apiUrl){
                try {
                    URL url = new URL(apiUrl);
                    return (HttpURLConnection)url.openConnection();
                } catch (MalformedURLException e) {
                    throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
                } catch (IOException e) {
                    throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
                }
            }

            public String readBody(InputStream body){
                InputStreamReader streamReader = new InputStreamReader(body);

                try (BufferedReader lineReader = new BufferedReader(streamReader)) {
                    StringBuilder responseBody = new StringBuilder();

                    String line;
                    while ((line = lineReader.readLine()) != null) {
                        responseBody.append(line);
                    }

                    return responseBody.toString();
                } catch (IOException e) {
                    throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
                }
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

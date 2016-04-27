package com.example.phama.youtube;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by phama on 10/4/2016.
 */
public class SearchActivity extends Activity {

    EditText editText;
    Button search;
    ListView listView;
    AsyncHttpClient client;
    RequestParams params;
    ArrayList<Video> listVideo;
    VideoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        client = new AsyncHttpClient();
        initImageLoader(this);

        editText = (EditText) findViewById(R.id.text_edit);
        search = (Button) findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.list_video);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() > 0) {
                    query(editText.getText().toString());
                    editText.setText("");

                }
            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.putExtra("videoid", listVideo.get(i).getVideoId());
                intent.putExtra("title", listVideo.get(i).getTitle());

                startActivity(intent);
            }
        });

    }


    public void query(String query){

        params = new RequestParams();

        params.put("part","snippet");
        params.put("order", "viewCount");
        params.put("key", Config.SEARCH_API_KEY);
        params.put("q", query);
        params.put("type","video");
        params.put("maxResults",50);
        listVideo = new ArrayList<>();


        String url = "https://www.googleapis.com/youtube/v3/search";

        client.get(url, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject data = new JSONObject(responseString);
                    JSONArray array = data.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        JSONObject id = obj.getJSONObject("id");
                        String videoId = id.getString("videoId");
                        JSONObject snippet = obj.getJSONObject("snippet");
                        String title = snippet.getString("title");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject default1 = thumbnails.getJSONObject("default");
                        String url = default1.getString("url");

                        Video video = new Video(videoId, title, url);

                        listVideo.add(video);

                    }

                    adapter = new VideoAdapter(SearchActivity.this, listVideo);

                    listView.setAdapter(adapter);

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


        });


    }


    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();


        ImageLoader.getInstance().init(config.build());
    }
}









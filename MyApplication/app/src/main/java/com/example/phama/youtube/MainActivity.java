package com.example.phama.youtube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
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

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    Businessitem businessitem;

    String videoId;
    String title;
    String url;
    AsyncHttpClient client2;
    RequestParams params;
    VideoAdapter videoAdapter;
    ArrayList<Video> listVideo;
    HisAdapter hisAdapter;


    ListView listView;
    ListView listView2;
    Button btn1,btn2;
    Video itemvideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client2 = new AsyncHttpClient();
        initImageLoader2(this);

        businessitem = new Businessitem(MainActivity.this);




        videoId = getIntent().getExtras().getString("videoid");
        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");





        btn1= (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);


            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.INVISIBLE);
                listView2.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);

            }
        });

        query(videoId.getClass().toString());
        listView =(ListView)findViewById(R.id.listvideo2) ;
        listView2 =(ListView)findViewById(R.id.listvideo3);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        hisAdapter = new HisAdapter(MainActivity.this, businessitem);

        listView2.setAdapter(hisAdapter);

    }
    @Override
    public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored) {
        final int id=0;
        if (!wasRestored) {


                    player.cueVideo(videoId);

            final TextView textView = (TextView) findViewById(R.id.videoinfo);
                    textView.setText(title);

            businessitem.insertItem(new Video(id,videoId,title,url));
            hisAdapter.reset();
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    itemvideo = (Video)parent.getAdapter().getItem(position);
                    videoId  =itemvideo.getVideoId();
                    title= itemvideo.getTitle();
                    textView.setText(title);
                   player.cueVideo(videoId);
                }
            });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long ad) {
                           videoId = listVideo.get(position).getVideoId();
                            title = listVideo.get(position).getTitle();
                            url = listVideo.get(position).getUrl();
                            player.cueVideo(videoId);
                            businessitem.insertItem(new Video(id,videoId,title,url));
                            hisAdapter.reset();
                            textView.setText(title);
                        }
                    });

                }
        }
    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = new String(getString(R.string.player_error));
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void query(String query){

        params = new RequestParams();

        params.put("part","snippet");
        params.put("key", Config.SEARCH_API_KEY);
        params.put("relatedToVideoId", videoId);
        params.put("type","video");
        params.put("maxResults",50);
        listVideo = new ArrayList<>();


        String url = "https://www.googleapis.com/youtube/v3/search";

        client2.get(url, params, new TextHttpResponseHandler() {

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

                        Video video = new Video(0,videoId, title, url);

                        listVideo.add(video);

                    }

                    videoAdapter = new VideoAdapter(MainActivity.this, listVideo);

                    listView.setAdapter(videoAdapter);

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


        });


    }
    public static void initImageLoader2(Context context) {

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


package com.example.phama.youtube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    String videoId;
    String title;
    AsyncHttpClient client2;
    RequestParams params;
    VideoAdapter videoAdapter;
    ArrayList<Video> listVideo;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client2 = new AsyncHttpClient();
        initImageLoader2(this);


        videoId = getIntent().getExtras().getString("videoid");
        title = getIntent().getExtras().getString("title");

        query(videoId.getClass().toString());




        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

    }


    @Override
    public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

                    player.cueVideo(videoId);
                    final TextView textView = (TextView) findViewById(R.id.videoinfo);
                    textView.setText(title);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           videoId = listVideo.get(position).getVideoId();
                            title = listVideo.get(position).getTitle();
                            player.cueVideo(videoId);

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

                        Video video = new Video(videoId, title, url);

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


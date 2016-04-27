package com.example.phama.youtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by phama on 10/4/2016.
 */
public class VideoAdapter extends BaseAdapter {

    class ViewHolder {
        ImageView imageView;
        TextView title;
    }

    public ArrayList<Video> listVideo;
    LayoutInflater infater = null;


    public VideoAdapter(Context context, ArrayList<Video> apps) {
        infater = LayoutInflater.from(context);
        this.listVideo = apps;

    }

    @Override
    public int getCount() {
        return listVideo.size();
    }

    @Override
    public Object getItem(int position) {
        return listVideo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = infater.inflate(R.layout.item_video,
                    null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.image);
            holder.title = (TextView) convertView
                    .findViewById(R.id.name);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Video item = (Video) getItem(position);
        if (item != null) {

            holder.title.setText(item.getTitle());
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(item.getUrl(), holder.imageView);


        }

        return convertView;
    }}

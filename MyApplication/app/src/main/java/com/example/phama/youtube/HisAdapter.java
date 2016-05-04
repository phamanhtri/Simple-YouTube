package com.example.phama.youtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class HisAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater infater;
    private List<Video> mListItem;
    private Businessitem mBusinessItem;
    class ViewHolder {
        ImageView imageView;
        TextView title;
        ImageView imageselet;
    }


    public HisAdapter(Context context, Businessitem businessItem) {
        mContext = context;
        mBusinessItem = businessItem;
        mListItem = mBusinessItem.getItems();
        infater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = infater.inflate(R.layout.item_video2,
                    null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.image2);
            holder.title = (TextView) convertView
                    .findViewById(R.id.name2);




            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Video item = (Video) getItem(position);
        if (item != null) {

            holder.title.setText(item.getTitle());
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(item.getUrl(), holder.imageView);
           holder.imageselet =(ImageView) convertView.findViewById(R.id.btndelete);



            holder.imageselet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mBusinessItem.deleteItem(item)){
                        reset();
                    }
                }
            });
        }


        return convertView;
    }

    public void reset() {
        mListItem=mBusinessItem.getItems();
        super.notifyDataSetChanged();
    }



}

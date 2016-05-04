package com.example.phama.youtube;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

/**
 * Created by phama on 2/5/2016.
 */
public class Businessitem {
    private Database Database;
    private Context mContext;

    public Businessitem(Context context) {
        mContext = context;
        Database = new Database(context);
    }

    public Video getItem(int id) {
        return Database.getItem(id);
    }

    public List<Video> getItems() {
        return Database.getItems();
    }

    public boolean insertItem(Video Video) {
        return Database.insertItem(Video);
    }



    public boolean deleteItem(Video Video) {
        boolean result = Database.deleteItem(Video.getId());
        if (result) {
            Toast.makeText(mContext, "Đã xóa  " + Video.getTitle(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Xóa không thành công", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}
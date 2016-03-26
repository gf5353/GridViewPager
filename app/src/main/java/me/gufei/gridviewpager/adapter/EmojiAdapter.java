package me.gufei.gridviewpager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import me.gufei.gridviewpager.R;
import me.gufei.gridviewpager.emoji.ChatEmoji;

/**
 * Created by jishubu1 on 2016/3/26.
 */
public class EmojiAdapter extends BaseAdapter {
    private Context mContext;
    private List<ChatEmoji> mLists;
    LayoutInflater mInflater;

    public EmojiAdapter(Context pContext, List<ChatEmoji> mLists) {
        this.mContext = pContext;
        this.mLists = mLists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (null == convertView) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_emoji, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.img.setFocusable(false);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        try {
            ChatEmoji chatEmoji = mLists.get(position);
            holder.img.setImageResource(chatEmoji.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class Holder {
        ImageView img;
    }
}

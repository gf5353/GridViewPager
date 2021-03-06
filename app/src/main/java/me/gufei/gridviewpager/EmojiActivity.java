package me.gufei.gridviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import me.gufei.gridviewpager.adapter.EmojiAdapter;
import me.gufei.gridviewpager.emoji.ChatEmoji;
import me.gufei.gridviewpager.emoji.EmojiUtil;
import me.gufei.gridviewpager.emoji.EmotionInputDetector;
import me.gufei.gridviewpager.view.GridViewPager;
import me.gufei.gridviewpager.view.GridViewPagerDataAdapter;
import me.gufei.gridviewpager.view.PageIndicatorView;

/**
 * Created by jishubu1 on 2016/3/25.
 */
public class EmojiActivity extends AppCompatActivity {
    LinearLayout emotion_layout;
    LinearLayout list;
    EditText edit_text;
    ImageView editText;
    private EmotionInputDetector mDetector;
    private String TAG = "EmojiActivity";

    private GridViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);
        emotion_layout = (LinearLayout) findViewById(R.id.emotion_layout);
        list = (LinearLayout) findViewById(R.id.list);
        edit_text = (EditText) findViewById(R.id.edit_text);
        editText = (ImageView) findViewById(R.id.emotion_button);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(findViewById(R.id.emotion_layout))
                .bindToContent(findViewById(R.id.list))
                .bindToEditText((EditText) findViewById(R.id.edit_text))
                .bindToEmotionButton(findViewById(R.id.emotion_button))
                .build();
        long time_start = System.currentTimeMillis();
        Log.d(TAG, "chatEmojis:" + time_start);


        mViewPager = (GridViewPager) findViewById(R.id.myviewpager);
        mViewPager.setIndicator((PageIndicatorView) findViewById(R.id.indicator));
        mViewPager.setColumns(4, 7);
        List<ChatEmoji> chatEmojis = EmojiUtil.getInstance().getListByConfig(getApplicationContext(), mViewPager.getPageSize(), R.mipmap.ic_launcher);
        Log.d(TAG, "chatEmojis:" + chatEmojis.size());
        Log.d(TAG, "chatEmojis:" + (System.currentTimeMillis() - time_start));
        mViewPager.setGridViewPagerDataAdapter(new GridViewPagerDataAdapter<ChatEmoji>(chatEmojis) {
            @Override
            public BaseAdapter getGridViewAdapter(List<ChatEmoji> currentList, int pageIndex) {
                return new EmojiAdapter(getApplicationContext(), currentList);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, int pageIndex) {
                ChatEmoji emoji = (ChatEmoji) parent.getAdapter().getItem(position);
                String character = emoji.getCharacter();
                if (TextUtils.isEmpty(character)) {
                    return;
                }
                Editable editable = edit_text.getText();
                String text = editable.toString();
                int index = edit_text.getSelectionStart();
                if (emoji.getId() == R.mipmap.ic_launcher) {
                    int delLength = EmojiUtil.getInstance().delEmoji(text);
                    if (index >= delLength) {
                        editable.delete(index - delLength, index);
                    }
                    return;
                }
                SpannableString spannableString = EmojiUtil.getInstance().addFace(getApplicationContext(), emoji.getId(), character);
                editable.insert(index, spannableString);
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(getBaseContext());
                String text = edit_text.getText().toString().trim();
                textView.setText(EmojiUtil.getInstance().getExpressionString(getBaseContext(), text));
                list.addView(textView);
                edit_text.getText().clear();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }
}

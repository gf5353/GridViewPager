package me.gufei.gridviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import me.gufei.gridviewpager.emoji.EmotionInputDetector;

/**
 * Created by jishubu1 on 2016/3/25.
 */
public class EmojiActivity extends AppCompatActivity {
    RelativeLayout emotion_layout;
    ListView list;
    EditText edit_text;
    ImageView editText;
    private EmotionInputDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);
        emotion_layout = (RelativeLayout) findViewById(R.id.emotion_layout);
        list = (ListView) findViewById(R.id.list);
        edit_text = (EditText) findViewById(R.id.edit_text);
        editText = (ImageView) findViewById(R.id.emotion_button);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(findViewById(R.id.emotion_layout))
                .bindToContent(findViewById(R.id.list))
                .bindToEditText((EditText) findViewById(R.id.edit_text))
                .bindToEmotionButton(findViewById(R.id.emotion_button))
                .build();

//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {//隐藏
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                } else {
//                    editText.setFocusable(true);
//                    editText.setFocusableInTouchMode(true);
//                    editText.requestFocus();
//                    InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputManager.showSoftInput(editText, 0);
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }
}

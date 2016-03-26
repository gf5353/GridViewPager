package me.gufei.gridviewpager.emoji;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jishubu1 on 2016/3/25.
 */
public class EmojiUtil {


    /**
     * 读取表情配置文件
     *
     * @param context
     * @return
     */
    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<>();
            InputStream in = context.getResources().getAssets().open("emoji");
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析字符
     *
     * @param data
     */
    public static List<ChatEmoji> parseData(Context context) {
        List<ChatEmoji> chatEmojis = new ArrayList<>();
        List<String> data = getEmojiFile(context);
        if (data == null) {
            return null;
        }
        ChatEmoji emojEentry;
        try {
            for (String str : data) {
                String[] text = str.split(",");
                String fileName = text[0]
                        .substring(0, text[0].lastIndexOf("."));
                int resID = context.getResources().getIdentifier(fileName,
                        "mipmap", context.getPackageName());
                if (resID != 0) {
                    emojEentry = new ChatEmoji();
                    emojEentry.setId(resID);
                    emojEentry.setCharacter(text[1]);
                    emojEentry.setFaceName(fileName);
                    chatEmojis.add(emojEentry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatEmojis;
    }
}

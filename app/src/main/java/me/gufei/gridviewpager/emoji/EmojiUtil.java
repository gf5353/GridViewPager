package me.gufei.gridviewpager.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gufei.gridviewpager.R;

/**
 * Created by jishubu1 on 2016/3/25.
 */
public class EmojiUtil {
    private String TAG = "EmojiUtil";
    private static EmojiUtil emojiUtil;
    /**
     * 保存于内存中的表情集合
     */
    private List<ChatEmoji> chatEmojis = new ArrayList<>();
    /**
     * 保存于内存中的表情HashMap
     */
    private HashMap<String, String> emojiMap = new HashMap<>();

    private EmojiUtil() {
        chatEmojis.clear();
    }

    public static EmojiUtil getInstance() {
        if (emojiUtil == null) {
            emojiUtil = new EmojiUtil();
        }
        return emojiUtil;
    }

    /**
     * 读取表情配置文件
     *
     * @param context
     * @return
     */
    public List<String> getEmojiFile(Context context) {
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
    public List<ChatEmoji> getList(Context context) {
        if (chatEmojis.size() > 0) {
            return chatEmojis;
        }
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
                emojiMap.put(text[1], fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "drawable", context.getPackageName());
                if (resID == 0) {
                    resID = context.getResources().getIdentifier(fileName,
                            "mipmap", context.getPackageName());
                }
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

    public List<ChatEmoji> getListByConfig(Context context, int page, int delRes) {
        List<ChatEmoji> tempList = new ArrayList<>();
        List<ChatEmoji> list = getListByConfig(context);
        Log.d(TAG, "总数量：" + list.size() + "一页：" + page);
        for (int i = 0, allSize = list.size(); i < allSize; i++) {
            ChatEmoji chatEmoji1 = list.get(i);
            if (i != 0 && i % (page - 1) == 0) {
                ChatEmoji temp = new ChatEmoji();
                temp.setId(delRes);
                temp.setCharacter("删除");
                tempList.add(temp);
            }
            tempList.add(chatEmoji1);
        }

        if (tempList.size() % (page - 1) != 0) {
            int allPage = tempList.size() / page + 1;//总页数
            int allSize = page * allPage;//总表情数量
            int fillSize = allSize - tempList.size();//需要填充的数量
            Log.d(TAG, "总页数：" + allPage + "总表情数：" + allSize + "需要填充的数量：" + fillSize);
            for (int i = 0; i < fillSize; i++) {
                ChatEmoji temp = new ChatEmoji();
                if (i == fillSize - 1) {
                    temp.setId(delRes);
                    temp.setCharacter("删除");
                } else {
                    temp.setId(android.R.color.transparent);
                    temp.setCharacter("");
                }
                tempList.add(temp);
            }
        }

        Log.d(TAG, "添加完的总数量：" + tempList.size());
        return tempList;
    }

    public List<ChatEmoji> getListByConfig(Context context) {
        String[] array = context.getResources().getStringArray(R.array.emoji_array);
        List<String> tempList = Arrays.asList(array);
        if (chatEmojis.size() > 0) {
            return filter(tempList, chatEmojis);
        }
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
                emojiMap.put(text[1], fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "drawable", context.getPackageName());
                if (resID == 0) {
                    resID = context.getResources().getIdentifier(fileName,
                            "mipmap", context.getPackageName());
                }
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
        return filter(tempList, chatEmojis);
    }

    public int delEmoji(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int length = str.length();
        if (length < 6) {
            return 1;
        }
        String tempStr = str.substring(length - 6, length);
        Log.d(TAG, "截取的字符串是：" + tempStr);
        if (isEmoji(tempStr)) {
            return 6;
        }
        if (length >= 7) {
            tempStr = str.substring(length - 7, length);
            Log.d(TAG, "截取的字符串是：" + tempStr);
            if (isEmoji(tempStr)) {
                return 7;
            }
        }
        return 1;
    }

    private boolean isEmoji(String emoji) {
        SpannableString spannableString = new SpannableString(emoji);
        String zhengze = "\\[em_(\\d+)\\]";
        Pattern patten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patten.matcher(spannableString);
        boolean isEmoji = false;
        while (matcher.find()) {
            isEmoji = true;
        }
        return isEmoji;
    }


    private List<ChatEmoji> filter(List<String> tempList, List<ChatEmoji> list) {
        List<ChatEmoji> tempEmojis = new ArrayList<>();
        int i = 0;
        for (ChatEmoji emoji : list) {
            if (tempList.contains(emoji.getFaceName())) {
                tempEmojis.add(emoji);
            }
        }
        return tempEmojis;
    }

    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, int imgId,
                                   String spannableString) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                imgId);
        int dimension = dip2px(context, 25);
        bitmap = Bitmap.createScaledBitmap(bitmap, dimension, dimension, true);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public SpannableString getExpressionString(Context context, String str) {
        if (str == null)
            return new SpannableString("");
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 微笑[em_1]
        String zhengze = "\\[em_(\\d+)\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannableString;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            if (resId == 0) {
                resId = context.getResources().getIdentifier(value,
                        "mipmap", context.getPackageName());
            }
            // 通过上面匹配得到的字符串来生成图片资源id
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                // TODO 调整表情大小，第二个参数
                int px = dip2px(context, 32);
                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resId);
                bitmap = Bitmap.createScaledBitmap(bitmap, px, px, true);

                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                ImageSpan imageSpan = new ImageSpan(bitmap);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    public static int dip2px(Context context, float dipValue) {
        /*final float scale = mContext.getResources().getDisplayMetrics().density;

		return (int) (dipValue * scale + 0.5f);*/

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());

    }
}

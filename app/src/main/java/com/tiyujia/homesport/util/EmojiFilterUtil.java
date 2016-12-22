package com.tiyujia.homesport.util;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zzqybyb19860112 on 2016/12/22.
 */

public class EmojiFilterUtil {
    static Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE ) ;
    /**
     * 检测是否有emoji字符
     * @param source
     * [url=home.php?mod=space&uid=7300]@return[/url] 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if ("".equals(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(Context context,String source) {

        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        Toast.makeText(context,"暂不支持颜文字与表情",Toast.LENGTH_SHORT).show();
//        //到这里铁定包含
        StringBuilder buf = new StringBuilder(source.length());;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            } else {
                String result= (String) filter(source);
                buf.append(result);
            }
        }
        return buf.toString();
    }
    public static CharSequence filter (CharSequence source ) {
        Matcher emojiMatcher = emoji . matcher ( source ) ;
        if ( emojiMatcher . find ( ) ) {
           return "" ;
            }
        return null ;
    }
}
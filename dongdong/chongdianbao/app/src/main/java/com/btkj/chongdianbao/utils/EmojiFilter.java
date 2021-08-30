package com.btkj.chongdianbao.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

public class EmojiFilter implements InputFilter {
    public EmojiFilter() {
        super();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return filterEmoji(source);
    }

    /**
     1F30D - 1F567
     1F600 - 1F636
     24C2 - 1F251
     1F680 - 1F6C0
     2702 - 27B0
     1F601 - 1F64F
     */
    private boolean isEmojiCharacter(int first) {
        return !
                ((first == 0x0) ||
                        (first == 0x9) ||
                        (first == 0xA) ||
                        (first == 0xD) ||
                        ((first >= 0x20) && (first <= 0xD7FF)) ||
                        ((first >= 0xE000) && (first <= 0xFFFD)) ||
                        ((first >= 0x10000))) ||
                (first == 0xa9 || first == 0xae || first == 0x2122 ||
                        first == 0x3030 || (first >= 0x25b6 && first <= 0x27bf) ||
                        first == 0x2328 || (first >= 0x23e9 && first <= 0x23fa))
                || ((first >= 0x1F000 && first <= 0x1FFFF))
                || ((first >= 0x2702) && (first <= 0x27B0))
                || ((first >= 0x1F601) && (first <= 0x1F64F));
    }

    private boolean containsEmoji(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            int cp = str.codePointAt(i);
            if (isEmojiCharacter(cp)) {
                return true;
            }
        }
        return false;
    }

    private CharSequence filterEmoji(CharSequence c) {
        if (!containsEmoji(c.toString())) {
            return c;
        }
        StringBuilder buf = null;
        int len = c.length();
        for (int i = 0; i < len; i++) {
            char codePoint = c.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(c.length());
                }
                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return "";
        }
        return buf;
    }
}

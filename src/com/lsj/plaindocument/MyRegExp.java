package com.lsj.plaindocument;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hdmi on 16-12-2.
 */
//edittext的过滤器
public class MyRegExp extends PlainDocument {
    private Pattern pattern;
    private Matcher m;
    public MyRegExp(String pat)
    {
        super();
        this.pattern= Pattern.compile(pat);
    }
    @Override
    public void insertString
            (int offset, String str, AttributeSet attr)
            throws BadLocationException {
        if (str == null){
            return;
        }
        String tmp=getText(0, offset).concat(str);
        m=pattern.matcher(tmp);
        if(m.matches())
            super.insertString(offset, str, attr);
    }
}

package com.lsj.hotalclient;

import com.qt.datapicker.DatePicker;

import javax.swing.*;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hdmi on 16-11-26.
 */
public class ObservingTextField extends JTextField implements Observer {

    public ObservingTextField(int columns) {
        super(columns);
    }

    @Override
    public void update(Observable o, Object arg) {
        Calendar calendar=(Calendar)arg;
        DatePicker dp=(DatePicker)o;
        setText(dp.formatDate(calendar));
    }
}

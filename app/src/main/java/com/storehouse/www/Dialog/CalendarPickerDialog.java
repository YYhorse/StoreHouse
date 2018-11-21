package com.storehouse.www.Dialog;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.storehouse.www.Activity.BarcodeActivity;
import com.storehouse.www.Utils.PopMessage.PopMessageUtil;

import java.util.Calendar;

public class CalendarPickerDialog {
    /******************************************************************************************
     * * 功能说明：日历控件获取UI的ID
     ******************************************************************************************/
    private static boolean todayorderState = false;                                                 //日历控件BUG 点击后运行两次 解决标志位

    public static void TurnoverChooseCalendar(final TextView show_txt, final BarcodeActivity activity) {
        todayorderState = false;
        Calendar c = Calendar.getInstance();
        //日历控件
        android.app.DatePickerDialog dp = new android.app.DatePickerDialog(activity, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
                int now_year = datePicker.getYear();                                                //年
                int now_month = datePicker.getMonth() + 1;                                          //月-1
                int now_day = datePicker.getDayOfMonth();
                if (todayorderState == false) {
                    todayorderState = true;
                    show_txt.setText("" + now_year + "/" + now_month + "/" + now_day);
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }
}
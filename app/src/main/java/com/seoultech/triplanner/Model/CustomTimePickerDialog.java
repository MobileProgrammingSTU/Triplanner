package com.seoultech.triplanner.Model;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.seoultech.triplanner.R;

public class CustomTimePickerDialog extends Dialog {
    private Context context;
    private TimePickerDialogClickListener TimePickerDialogClickListener;
    private TimePicker timePicker;
    private TextView tvTitle;
    private Button btnOk, btnCancel;

    private String text;
    private String title;
    private int setHourVal;
    private int setMinuteVal;

    public CustomTimePickerDialog(@NonNull Context context, TimePickerDialogClickListener TimePickerDialogClickListener) {
        super(context);
        this.context = context;
        this.TimePickerDialogClickListener = TimePickerDialogClickListener;

    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHour(int setHourVal) {
        this.setHourVal = setHourVal;
    }

    public void setMinute(int setMinuteVal) {
        this.setMinuteVal = setMinuteVal;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_timepicker_dialog);

        timePicker = (TimePicker) findViewById(R.id.dialog_timePicker);
        btnOk = (Button) findViewById(R.id.dialog_btnOk);
        btnCancel = (Button) findViewById(R.id.dialog_btnCancel);

        timePicker.setHour(setHourVal);
        timePicker.setMinute(setMinuteVal);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                setHourVal = hourOfDay;
                setMinuteVal = minute;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogClickListener.onPositiveClick(setHourVal, setMinuteVal);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogClickListener.onNegativeClick();
                dismiss();
            }
        });
    }
}

package com.seoultech.triplanner.Model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.seoultech.triplanner.R;

public class CustomNormalDialog extends Dialog implements DialogInterface {
    private View.OnClickListener mConfirmClickListener;
    private View.OnClickListener mCancelClickListener;
    private String mContent;
    private String mTitle;
    private Context context;

    TextView title, content;
    Button btnConfirm, btnCancel;

    public CustomNormalDialog(@NonNull Context context, View.OnClickListener cancel, View.OnClickListener confirm) {
        super(context);

        this.context = context;
        this.mCancelClickListener = cancel;
        this.mConfirmClickListener = confirm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_box);

        title = (TextView) findViewById(R.id.dlgTitle);
        content = (TextView) findViewById(R.id.dlgContent);
        btnConfirm = (Button) findViewById(R.id.dlgBtnConfirm);
        btnCancel = (Button) findViewById(R.id.dlgBtnCancel);

        setTitle(mTitle);
        setMessage(mContent);
        setClickListener(mCancelClickListener , mConfirmClickListener);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setClickListener(View.OnClickListener left , View.OnClickListener right){
        if(left!=null && right!=null){
            btnCancel.setOnClickListener(left);
            btnConfirm.setOnClickListener(right);
        }else if(left==null && right!=null){
            btnCancel.setOnClickListener(right);
        }else {

        }
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public void setMessage(String content){
        this.mContent = content;
    }
}

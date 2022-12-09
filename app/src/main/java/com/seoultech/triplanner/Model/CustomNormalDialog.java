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
    private CustomNormalDialogClickListener dlgClickListener;
    private String mContent;
    private String mTitle;
    private Context context;

    TextView title, content;
    Button btnConfirm, btnCancel;

    public CustomNormalDialog(@NonNull Context context, String title, String message, CustomNormalDialogClickListener dlgClickListener) {
        super(context);

        this.context = context;
        this.dlgClickListener = dlgClickListener;

        this.mTitle = title;
        this.mContent = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_box);

        title = (TextView) findViewById(R.id.dlgTitle);
        content = (TextView) findViewById(R.id.dlgContent);
        btnConfirm = (Button) findViewById(R.id.dlgBtnConfirm);
        btnCancel = (Button) findViewById(R.id.dlgBtnCancel);

        if (!this.mTitle.equals("")) {
            title.setText(this.mTitle);
            title.setVisibility(View.VISIBLE);
        }
        content.setText(mContent);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgClickListener.onPositiveClick();
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgClickListener.onNegativeClick();
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
}

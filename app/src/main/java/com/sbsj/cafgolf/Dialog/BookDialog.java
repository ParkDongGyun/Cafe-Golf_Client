package com.sbsj.cafgolf.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sbsj.cafgolf.R;

public class BookDialog extends Dialog {
    private int position = -1;

    EditText etName;
    EditText etPhone;
    Button btnOK;
    Button btnCancel;
    TextView tverror;

    Context context;

    public BookDialog(final Context context) {
        super(context);

        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));  //다이얼로그의 배경을 투명으로 만듭니다.
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_book);     //다이얼로그에서 사용할 레이아웃입니다.

        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        btnOK = (Button) findViewById(R.id.btn_dialog_book);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().equals("")) {
                    tverror.setText(context.getString(R.string.em_name));
                } else if (etPhone.getText().equals("")) {
                    tverror.setText(context.getString(R.string.em_phone));
                } else {

                }
                initET();
                dismiss();
            }
        });
        btnCancel = findViewById(R.id.btn_dialog_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initET();

                dismiss();
            }
        });
        tverror = findViewById(R.id.tv_errorMessage);
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }

    private void initET() {
        if(etName == null || etPhone == null)
            return;

        etName.setText("");
        etPhone.setText("");
    }
}

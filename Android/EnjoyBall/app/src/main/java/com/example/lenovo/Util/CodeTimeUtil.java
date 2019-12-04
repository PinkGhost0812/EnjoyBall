package com.example.lenovo.Util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

public class CodeTimeUtil extends CountDownTimer {
    private Button tvCode;

    public CodeTimeUtil(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.tvCode = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {

        tvCode.setClickable(false); //设置不可点击
        tvCode.setText("重新获取("+millisUntilFinished / 1000 +")" );  //设置倒计时时间
        tvCode.setBackgroundColor(Color.TRANSPARENT);
        tvCode.setTextColor(Color.parseColor("#0bad61"));


        //富文本显示效果
        SpannableString spannableString = new SpannableString(tvCode.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#0bad61"));

        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvCode.setText(spannableString);
    }

    @Override
    public void onFinish() {
        tvCode.setText("重新获取");
        tvCode.setClickable(true);//重新获得点击
        tvCode.setTextColor(Color.parseColor("#0bad61"));
    }
}

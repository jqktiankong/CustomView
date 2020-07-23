package com.jqk.customview.auxiliaryline;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jqk.customview.R;

public class AuxiliaryLineActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auxiliaryline);

        ScreenUtil.hideSystemUI(getWindow().getDecorView());
    }
}

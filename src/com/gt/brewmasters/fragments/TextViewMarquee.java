package com.gt.brewmasters.fragments;

import com.gt.brewmasters.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TextViewMarquee extends Activity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticker);
        tv = (TextView) this.findViewById(R.id.tv);  
        tv.setSelected(true);  // Set focus to the textview
    }
}

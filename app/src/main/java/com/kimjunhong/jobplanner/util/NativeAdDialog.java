package com.kimjunhong.jobplanner.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.kimjunhong.jobplanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 8. 1..
 */

public class NativeAdDialog extends Dialog {
    @BindView(R.id.dialog_button_app_quit) TextView quit;
    @BindView(R.id.dialog_button_app_quit_cancel) TextView cancel;
    @BindView(R.id.dialog_native_ad) NativeExpressAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_native_ad);
        ButterKnife.bind(this);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public NativeAdDialog(Context context) {
        super(context);
    }
}

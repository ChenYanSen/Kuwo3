package com.designers.kuwo.activitys;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.designers.kuwo.R;

/**
 * Created by 跃 on 2017/1/9.
 */
public class ProvinceCitySelectDialog extends Dialog implements View.OnClickListener {

    private static int default_width = 160; // 默认宽度
    private static int default_height = 120; // 默认高度
    private ProvinceCitySeleCtDialogListening listening;
    private Dialog dialog;
    private TextView txt_cancel, mProvinceTextView, mCityTextView, mDistinguishTextView;

    public interface ProvinceCitySeleCtDialogListening {
        public void onClick(View v);
    }

    protected ProvinceCitySelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ProvinceCitySelectDialog(Context context) {
        super(context);
    }

    public ProvinceCitySelectDialog(Context context, int theme) {
        super(context, theme);
    }

    public ProvinceCitySelectDialog(Context context, View layout, int style, ProvinceCitySeleCtDialogListening listening) {
        this(context, default_width, default_height, layout, style, listening);
    }

    public ProvinceCitySelectDialog(Context context, double widthScale, double heightScale, View layout, int style, ProvinceCitySeleCtDialogListening listening) {
        super(context, style);
        setContentView(layout);
       this.listening=listening;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int windowWidth = outMetrics.widthPixels;
        int windowHeight = outMetrics.heightPixels;
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (windowWidth * widthScale); // 宽度设置为屏幕的一定比例大小

        params.gravity = Gravity.BOTTOM;
        params.y = (int) (windowHeight * heightScale); // 距离顶端高度设置为屏幕的一定比例大小
        getWindow().setAttributes(params);
        txt_cancel= (TextView) findViewById(R.id.txt_cancel);
        mProvinceTextView= (TextView) findViewById(R.id.mProvinceTextView);
        mCityTextView= (TextView) findViewById(R.id.mCityTextView);
        mDistinguishTextView= (TextView) findViewById(R.id.mDistinguishTextView);
        txt_cancel.setOnClickListener(this);
        mProvinceTextView.setOnClickListener(this);
        mCityTextView.setOnClickListener(this);
        mDistinguishTextView.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        listening.onClick(v);
    }


   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_view_actionsheet);

        initViews();
    }*/

    private void initViews() {
       /* quitBtn = (Button)findViewById(R.id.quit_btn);
        stopBtn = (Button)findViewById(R.id.stop_btn);
        cancelBtn = (Button)findViewById(R.id.cancel_btn);

        quitBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);*/
    }
}

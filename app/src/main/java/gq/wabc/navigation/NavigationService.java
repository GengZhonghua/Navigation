package gq.wabc.navigation;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

public class NavigationService extends AccessibilityService implements View.OnClickListener {

    private WindowManager windowManager;
    private int height, width;
    TextView back, home, recents;
    boolean backPosition, isVibration;
    Vibrator vibrator;
    SharedPreferences preferences;
    WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();


        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        backPosition = preferences.getBoolean(getString(R.string.string_position_back), false);
        isVibration = preferences.getBoolean(getString(R.string.string_isVibration), true);

        height = getStatusBarHeight() * 4 / 5;
        width = (int) (height * 1.6);

        createFloatView();

    }

    /*
            获取状态栏高度
             */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 在状态栏创建悬浮窗口
     */
    private void createFloatView() {

        back = new TextView(this);
        home = new TextView(this);
        recents = new TextView(this);

        back.setBackgroundColor(Color.RED);
        home.setBackgroundColor(Color.BLUE);
        recents.setBackgroundColor(Color.GREEN);

        back.setWidth(width);
        back.setHeight(height);
        home.setWidth(width);
        home.setHeight(height);
        recents.setWidth(width);
        recents.setHeight(height);

        back.setOnClickListener(this);
        home.setOnClickListener(this);
        recents.setOnClickListener(this);

        back.setId(R.id.back);
        home.setId(R.id.home);
        recents.setId(R.id.recents);

        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        params.gravity = Gravity.TOP | Gravity.START;

        windowManager.addView(back, params);
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        windowManager.addView(home, params);
        params.gravity = Gravity.TOP | Gravity.END;
        windowManager.addView(recents, params);


    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    //    go back. go home. open recent apps.
    @Override
    public void onClick(View v) {
        if (isVibration) {
            vibrator.vibrate(50);
        }
        switch (v.getId()) {
            case R.id.back:
                if (backPosition) {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                } else {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }
                break;
            case R.id.home:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                break;
            case R.id.recents:
                if (backPosition) {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                } else {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                }
                break;
            default:
                break;
        }
    }


}

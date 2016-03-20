package gq.wabc.navigation;

import android.accessibilityservice.AccessibilityService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
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
    static boolean backPosition;
    SharedPreferences preferences;
    WindowManager.LayoutParams params;
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        backPosition = preferences.getBoolean(getString(R.string.string_position_back), false);

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

//        back.setBackgroundColor(Color.RED);
//        home.setBackgroundColor(Color.BLUE);
//        recents.setBackgroundColor(Color.GREEN);

        back.setWidth(width);
        back.setHeight(height);
        home.setWidth(width);
        home.setHeight(height);
        recents.setWidth(width);
        recents.setHeight(height);

        back.setOnClickListener(this);
        home.setOnClickListener(this);
        recents.setOnClickListener(this);

//        back.setOnLongClickListener(this);
//        home.setOnLongClickListener(this);
//        recents.setOnLongClickListener(this);

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (back.getVisibility()==View.GONE){
            back.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            recents.setVisibility(View.VISIBLE);
        }
        return super.onStartCommand(intent, flags, startId);
    }

//    @Override
//    public boolean onLongClick(View v) {
//        back.setVisibility(View.GONE);
//        home.setVisibility(View.GONE);
//        recents.setVisibility(View.GONE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(android.R.drawable.stat_notify_error)
//                .setContentTitle("已隐藏到通知栏")
//                .setContentText("点击返回").setOngoing(true)
//                .setTicker("状态栏导航按钮已隐藏")
//                .setAutoCancel(true);
//        Notification notification = builder.build();
//        notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
//        PendingIntent intent = PendingIntent.getService(this,0,new Intent(this,NavigationService.class),0);
//        builder.setContentIntent(intent);
//        notificationManager.notify(0,notification);
//        return true;
//    }
}

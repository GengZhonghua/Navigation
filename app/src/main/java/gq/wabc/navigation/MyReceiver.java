package gq.wabc.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //开机启动辅助服务，只有一个广播此处不做判断
        context.startService(new Intent(context, NavigationService.class));
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

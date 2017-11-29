package cn.edu.stu.max.cocovendor.javaClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.edu.stu.max.cocovendor.activities.StartActivity;

/**
 * Created by 0 on 2017/10/12.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent intent1 = new Intent(context, StartActivity.class);
            intent1.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}

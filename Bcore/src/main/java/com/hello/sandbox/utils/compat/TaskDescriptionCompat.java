package com.hello.sandbox.utils.compat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.app.BActivityThread;
import com.hello.sandbox.utils.DrawableUtils;
import java.util.Locale;

public class TaskDescriptionCompat {
  public static ActivityManager.TaskDescription fix(ActivityManager.TaskDescription td) {
    String label = td.getLabel();
    Bitmap icon = td.getIcon();

    if (label != null && icon != null) return td;

    label = getTaskDescriptionLabel(BActivityThread.getUserId(), getApplicationLabel());
    Drawable drawable = getApplicationIcon();
    if (drawable == null) return td;

    ActivityManager am =
        (ActivityManager) SandBoxCore.getContext().getSystemService(Context.ACTIVITY_SERVICE);
    int iconSize = am.getLauncherLargeIconSize();
    icon = DrawableUtils.drawableToBitmap(drawable, iconSize, iconSize);
    td = new ActivityManager.TaskDescription(label, icon, td.getPrimaryColor());
    return td;
  }

  public static String getTaskDescriptionLabel(int userId, CharSequence label) {
    return String.format(Locale.CHINA, "[B%d]%s", userId, label);
  }

  private static CharSequence getApplicationLabel() {
    try {
      PackageManager pm = SandBoxCore.getPackageManager();
      return pm.getApplicationLabel(pm.getApplicationInfo(BActivityThread.getAppPackageName(), 0));
    } catch (PackageManager.NameNotFoundException e) {
      return null;
    }
  }

  private static Drawable getApplicationIcon() {
    try {
      return SandBoxCore.getPackageManager()
          .getApplicationIcon(BActivityThread.getAppPackageName());
    } catch (PackageManager.NameNotFoundException ignore) {
      return null;
    }
  }
}

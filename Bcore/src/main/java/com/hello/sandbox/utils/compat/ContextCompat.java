package com.hello.sandbox.utils.compat;

import android.content.Context;
import android.content.ContextWrapper;
import black.android.app.BRContextImpl;
import black.android.app.BRContextImplKitkat;
import black.android.content.AttributionSourceStateContext;
import black.android.content.BRAttributionSource;
import black.android.content.BRAttributionSourceState;
import black.android.content.BRContentResolver;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.app.BActivityThread;

/** Created by Milk on 3/31/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class ContextCompat {
  public static final String TAG = "ContextCompat";

  public static void fixAttributionSourceState(Object obj, int uid) {
    Object mAttributionSourceState;
    if (obj != null && BRAttributionSource.get(obj)._check_mAttributionSourceState() != null) {
      mAttributionSourceState = BRAttributionSource.get(obj).mAttributionSourceState();

      AttributionSourceStateContext attributionSourceStateContext =
          BRAttributionSourceState.get(mAttributionSourceState);
      attributionSourceStateContext._set_packageName(SandBoxCore.getHostPkg());
      attributionSourceStateContext._set_uid(uid);
      fixAttributionSourceState(BRAttributionSource.get(obj).getNext(), uid);
    }
  }

  public static void fix(Context context) {
    try {
      int deep = 0;
      while (context instanceof ContextWrapper) {
        context = ((ContextWrapper) context).getBaseContext();
        deep++;
        if (deep >= 10) {
          return;
        }
      }
      BRContextImpl.get(context)._set_mPackageManager(null);
      try {
        context.getPackageManager();
      } catch (Throwable e) {
        e.printStackTrace();
      }

      BRContextImpl.get(context)._set_mBasePackageName(SandBoxCore.getHostPkg());
      BRContextImplKitkat.get(context)._set_mOpPackageName(SandBoxCore.getHostPkg());
      BRContentResolver.get(context.getContentResolver())
          ._set_mPackageName(SandBoxCore.getHostPkg());

      if (BuildCompat.isS()) {
        fixAttributionSourceState(
            BRContextImpl.get(context).getAttributionSource(), BActivityThread.getBUid());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

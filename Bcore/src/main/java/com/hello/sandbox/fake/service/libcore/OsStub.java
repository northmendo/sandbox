package com.hello.sandbox.fake.service.libcore;

import android.os.Process;
import black.libcore.io.BRLibcore;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.app.BActivityThread;
import com.hello.sandbox.core.IOCore;
import com.hello.sandbox.fake.hook.ClassInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import com.hello.sandbox.utils.Reflector;
import java.lang.reflect.Method;

/** Created by Milk on 4/9/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class OsStub extends ClassInvocationStub {
  public static final String TAG = "OsStub";
  private Object mBase;

  public OsStub() {
    mBase = BRLibcore.get().os();
  }

  @Override
  protected Object getWho() {
    return mBase;
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    BRLibcore.get()._set_os(proxyInvocation);
  }

  @Override
  protected void onBindMethod() {}

  @Override
  public boolean isBadEnv() {
    return BRLibcore.get().os() != getProxyInvocation();
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        if (args[i] == null) continue;
        if (args[i] instanceof String && ((String) args[i]).startsWith("/")) {
          String orig = (String) args[i];
          args[i] = IOCore.get().redirectPath(orig);
          //                    if (!ObjectsCompat.equals(orig, args[i])) {
          //                        Log.d(TAG, "redirectPath: " + orig + "  => " + args[i]);
          //                    }
        }
      }
    }
    return super.invoke(proxy, method, args);
  }

  @ProxyMethod("getuid")
  public static class getuid extends MethodHook {

    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      int callUid = (int) method.invoke(who, args);
      return getFakeUid(callUid);
    }
  }

  @ProxyMethod("stat")
  public static class stat extends MethodHook {

    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      Object invoke = null;
      try {
        invoke = method.invoke(who, args);
      } catch (Throwable e) {
        throw e.getCause();
      }
      Reflector.with(invoke).field("st_uid").set(getFakeUid(-1));
      return invoke;
    }
  }

  private static int getFakeUid(int callUid) {
    if (callUid > 0 && callUid <= Process.FIRST_APPLICATION_UID) return callUid;
    //            Log.d(TAG, "getuid: " + BActivityThread.getAppPackageName() + ", " +
    // BActivityThread.getAppUid());
    if (BActivityThread.isThreadInit() && BActivityThread.currentActivityThread().isInit()) {
      return BActivityThread.getBAppId();
    } else {
      return SandBoxCore.getHostUid();
    }
  }
}

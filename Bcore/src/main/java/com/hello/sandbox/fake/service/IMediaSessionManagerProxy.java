package com.hello.sandbox.fake.service;

import android.content.Context;
import black.android.media.session.BRISessionManagerStub;
import black.android.os.BRServiceManager;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import java.lang.reflect.Method;

/** Created by Milk on 4/8/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class IMediaSessionManagerProxy extends BinderInvocationStub {

  public IMediaSessionManagerProxy() {
    super(BRServiceManager.get().getService(Context.MEDIA_SESSION_SERVICE));
  }

  @Override
  protected Object getWho() {
    return BRISessionManagerStub.get()
        .asInterface(BRServiceManager.get().getService(Context.MEDIA_SESSION_SERVICE));
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService(Context.MEDIA_SESSION_SERVICE);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @ProxyMethod("createSession")
  public static class CreateSession extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      if (args != null && args.length > 0 && args[0] instanceof String) {
        args[0] = SandBoxCore.getHostPkg();
      }
      return method.invoke(who, args);
    }
  }
}

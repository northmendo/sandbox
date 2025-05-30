package com.hello.sandbox.fake.service.context;

import android.content.Context;
import black.android.content.BRIRestrictionsManagerStub;
import black.android.os.BRServiceManager;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import java.lang.reflect.Method;

/** Created by Milk on 4/8/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class RestrictionsManagerStub extends BinderInvocationStub {

  public RestrictionsManagerStub() {
    super(BRServiceManager.get().getService(Context.RESTRICTIONS_SERVICE));
  }

  @Override
  protected Object getWho() {
    return BRIRestrictionsManagerStub.get()
        .asInterface(BRServiceManager.get().getService(Context.RESTRICTIONS_SERVICE));
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService(Context.RESTRICTIONS_SERVICE);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @ProxyMethod("getApplicationRestrictions")
  public static class GetApplicationRestrictions extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      args[0] = SandBoxCore.getHostPkg();
      return method.invoke(who, args);
    }
  }
}

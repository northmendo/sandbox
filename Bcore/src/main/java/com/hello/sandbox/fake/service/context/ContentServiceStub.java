package com.hello.sandbox.fake.service.context;

import black.android.content.BRIContentServiceStub;
import black.android.os.BRServiceManager;
import java.lang.reflect.Method;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;

/** Created by Milk on 4/6/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class ContentServiceStub extends BinderInvocationStub {

  public ContentServiceStub() {
    super(BRServiceManager.get().getService("content"));
  }

  @Override
  protected Object getWho() {
    return BRIContentServiceStub.get().asInterface(BRServiceManager.get().getService("content"));
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService("content");
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @ProxyMethod("registerContentObserver")
  public static class RegisterContentObserver extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      return 0;
    }
  }

  @ProxyMethod("notifyChange")
  public static class NotifyChange extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      return 0;
    }
  }
}

package com.hello.sandbox.utils;

import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.app.BActivityThread;
import java.util.Arrays;
import java.util.HashSet;

public class MethodParameterUtils {

  public static <T> T getFirstParam(Object[] args, Class<T> tClass) {
    if (args == null) {
      return null;
    }
    int index = ArrayUtils.indexOfFirst(args, tClass);
    if (index != -1) {
      return (T) args[index];
    }
    return null;
  }

  public static String replaceFirstAppPkg(Object[] args) {
    if (args == null) {
      return null;
    }
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof String) {
        String value = (String) args[i];
        if (SandBoxCore.get().isInstalled(value, BActivityThread.getUserId())) {
          args[i] = SandBoxCore.getHostPkg();
          return value;
        }
      }
    }
    return null;
  }

  public static void replaceAllAppPkg(Object[] args) {
    if (args == null) {
      return;
    }
    for (int i = 0; i < args.length; i++) {
      if (args[i] == null) continue;
      if (args[i] instanceof String) {
        String value = (String) args[i];
        if (SandBoxCore.get().isInstalled(value, BActivityThread.getUserId())) {
          args[i] = SandBoxCore.getHostPkg();
        }
      }
    }
  }

  public static void replaceFirstUid(Object[] args) {
    if (args == null) return;
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof Integer) {
        int uid = (int) args[i];
        if (uid == BActivityThread.getBUid()) {
          args[i] = SandBoxCore.getHostUid();
        }
      }
    }
  }

  public static void replaceLastUid(Object[] args) {
    int index = ArrayUtils.indexOfLast(args, Integer.class);
    if (index != -1) {
      int uid = (int) args[index];
      if (uid == BActivityThread.getBUid()) {
        args[index] = SandBoxCore.getHostUid();
      }
    }
  }

  public static String replaceLastAppPkg(Object[] args) {
    int index = ArrayUtils.indexOfLast(args, String.class);
    if (index != -1) {
      String pkg = (String) args[index];
      if (SandBoxCore.get().isInstalled(pkg, BActivityThread.getUserId())) {
        args[index] = SandBoxCore.getHostPkg();
      }
      return pkg;
    }
    return null;
  }

  public static String replaceSequenceAppPkg(Object[] args, int sequence) {
    int index = ArrayUtils.indexOf(args, String.class, sequence);
    if (index != -1) {
      String pkg = (String) args[index];
      if (SandBoxCore.get().isInstalled(pkg, BActivityThread.getUserId())) {
        args[index] = SandBoxCore.getHostPkg();
      }
      return pkg;
    }
    return null;
  }

  public static int getParamsIndex(Class[] args, Class<?> type) {
    for (int i = 0; i < args.length; i++) {
      Class obj = args[i];
      if (obj.equals(type)) {
        return i;
      }
    }
    return -1;
  }

  public static int getIndex(Object[] args, Class<?> type) {
    return getIndex(args, type, 0);
  }

  public static int getIndex(Object[] args, Class<?> type, int start) {
    for (int i = start; i < args.length; i++) {
      Object obj = args[i];
      if (obj != null && obj.getClass() == type) {
        return i;
      }
      if (type.isInstance(obj)) {
        return i;
      }
    }
    return -1;
  }

  public static Class<?>[] getAllInterface(Class clazz) {
    HashSet<Class<?>> classes = new HashSet<>();
    getAllInterfaces(clazz, classes);
    Class<?>[] result = new Class[classes.size()];
    classes.toArray(result);
    return result;
  }

  public static void getAllInterfaces(Class clazz, HashSet<Class<?>> interfaceCollection) {
    Class<?>[] classes = clazz.getInterfaces();
    if (classes.length != 0) {
      interfaceCollection.addAll(Arrays.asList(classes));
    }
    if (clazz.getSuperclass() != Object.class) {
      getAllInterfaces(clazz.getSuperclass(), interfaceCollection);
    }
  }
}

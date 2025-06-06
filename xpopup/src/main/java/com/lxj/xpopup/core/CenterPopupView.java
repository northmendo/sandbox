package com.lxj.xpopup.core;

import static com.lxj.xpopup.enums.PopupAnimation.ScaleAlphaFromCenter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.util.XPopupUtils;

/** Description: 在中间显示的Popup */
public class CenterPopupView extends BasePopupView {
  protected FrameLayout centerPopupContainer;
  protected int bindLayoutId;
  protected int bindItemLayoutId;
  protected View contentView;

  public CenterPopupView(@NonNull Context context) {
    super(context);
    centerPopupContainer = findViewById(R.id.centerPopupContainer);
  }

  protected void addInnerContent() {
    contentView =
        LayoutInflater.from(getContext()).inflate(getImplLayoutId(), centerPopupContainer, false);
    LayoutParams params = (LayoutParams) contentView.getLayoutParams();
    params.gravity = Gravity.CENTER;
    centerPopupContainer.addView(contentView, params);
  }

  @Override
  protected final int getInnerLayoutId() {
    return R.layout._xpopup_center_popup_view;
  }

  @Override
  protected void initPopupContent() {
    super.initPopupContent();
    if (centerPopupContainer.getChildCount() == 0) addInnerContent();
    getPopupContentView().setTranslationX(popupInfo.offsetX);
    getPopupContentView().setTranslationY(popupInfo.offsetY);
    XPopupUtils.applyPopupSize(
        (ViewGroup) getPopupContentView(),
        getMaxWidth(),
        getMaxHeight(),
        getPopupWidth(),
        getPopupHeight(),
        null);
  }

  protected void applyTheme() {
    if (bindLayoutId == 0) {
      if (popupInfo.isDarkTheme) {
        applyDarkTheme();
      } else {
        applyLightTheme();
      }
    }
  }

  @Override
  protected void applyDarkTheme() {
    super.applyDarkTheme();
    centerPopupContainer.setBackground(
        XPopupUtils.createDrawable(
            getResources().getColor(R.color._xpopup_dark_color), popupInfo.borderRadius));
  }

  @Override
  protected void applyLightTheme() {
    super.applyLightTheme();
    centerPopupContainer.setBackground(
        XPopupUtils.createDrawable(
            getResources().getColor(R.color._xpopup_light_color), popupInfo.borderRadius));
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    //        setTranslationY(0);
  }

  /** 具体实现的类的布局 */
  protected int getImplLayoutId() {
    return 0;
  }

  protected int getMaxWidth() {
    return popupInfo.maxWidth == 0
        ? (int) (XPopupUtils.getAppWidth(getContext()) * 0.72f)
        : popupInfo.maxWidth;
  }

  @Override
  protected PopupAnimator getPopupAnimator() {
    return new ScaleAlphaAnimator(
        getPopupContentView(), getAnimationDuration(), ScaleAlphaFromCenter);
  }
}

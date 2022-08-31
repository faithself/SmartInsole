// Generated by data binding compiler. Do not edit!
package de.kai_morich.simple_usb_terminal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import de.kai_morich.simple_usb_terminal.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentAppBinding extends ViewDataBinding {
  @NonNull
  public final TextView bothFeetFloat;

  @NonNull
  public final Guideline guideline2;

  @NonNull
  public final Guideline guideline4;

  @NonNull
  public final TextView heelImpactL;

  @NonNull
  public final TextView heelImpactR;

  @NonNull
  public final TextView leftFootFloat;

  @NonNull
  public final Button resetBiggest;

  @NonNull
  public final TextView rightFootFloat;

  @NonNull
  public final TextView stepFreq;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView1;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView4;

  @NonNull
  public final TextView textView5;

  @NonNull
  public final TextView textView6;

  @NonNull
  public final TextView textView7;

  @NonNull
  public final TextView totalStep;

  protected FragmentAppBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView bothFeetFloat, Guideline guideline2, Guideline guideline4, TextView heelImpactL,
      TextView heelImpactR, TextView leftFootFloat, Button resetBiggest, TextView rightFootFloat,
      TextView stepFreq, TextView textView, TextView textView1, TextView textView2,
      TextView textView3, TextView textView4, TextView textView5, TextView textView6,
      TextView textView7, TextView totalStep) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bothFeetFloat = bothFeetFloat;
    this.guideline2 = guideline2;
    this.guideline4 = guideline4;
    this.heelImpactL = heelImpactL;
    this.heelImpactR = heelImpactR;
    this.leftFootFloat = leftFootFloat;
    this.resetBiggest = resetBiggest;
    this.rightFootFloat = rightFootFloat;
    this.stepFreq = stepFreq;
    this.textView = textView;
    this.textView1 = textView1;
    this.textView2 = textView2;
    this.textView3 = textView3;
    this.textView4 = textView4;
    this.textView5 = textView5;
    this.textView6 = textView6;
    this.textView7 = textView7;
    this.totalStep = totalStep;
  }

  @NonNull
  public static FragmentAppBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_app, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentAppBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentAppBinding>inflateInternal(inflater, R.layout.fragment_app, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentAppBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_app, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentAppBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentAppBinding>inflateInternal(inflater, R.layout.fragment_app, null, false, component);
  }

  public static FragmentAppBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentAppBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentAppBinding)bind(component, view, R.layout.fragment_app);
  }
}
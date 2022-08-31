// Generated by data binding compiler. Do not edit!
package de.kai_morich.simple_usb_terminal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentContainerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import de.kai_morich.simple_usb_terminal.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentFeetFsrValueBinding extends ViewDataBinding {
  @NonNull
  public final TextView bodyWeight;

  @NonNull
  public final BottomNavigationView bottomNavigationView;

  @NonNull
  public final ImageView feetBackground2;

  @NonNull
  public final FragmentContainerView fragmentContainerView;

  @NonNull
  public final ImageView gCenter;

  @NonNull
  public final TextView lowPowerIndL;

  @NonNull
  public final TextView lowPowerIndR;

  @NonNull
  public final ImageView sensorL00;

  @NonNull
  public final ImageView sensorL01;

  @NonNull
  public final ImageView sensorL02;

  @NonNull
  public final ImageView sensorL03;

  @NonNull
  public final ImageView sensorR00;

  @NonNull
  public final ImageView sensorR01;

  @NonNull
  public final ImageView sensorR02;

  @NonNull
  public final ImageView sensorR03;

  @NonNull
  public final TextView textView8;

  @NonNull
  public final TextView textViewL00;

  @NonNull
  public final TextView textViewL01;

  @NonNull
  public final TextView textViewL02;

  @NonNull
  public final TextView textViewL03;

  @NonNull
  public final TextView textViewR00;

  @NonNull
  public final TextView textViewR01;

  @NonNull
  public final TextView textViewR02;

  @NonNull
  public final TextView textViewR03;

  protected FragmentFeetFsrValueBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView bodyWeight, BottomNavigationView bottomNavigationView, ImageView feetBackground2,
      FragmentContainerView fragmentContainerView, ImageView gCenter, TextView lowPowerIndL,
      TextView lowPowerIndR, ImageView sensorL00, ImageView sensorL01, ImageView sensorL02,
      ImageView sensorL03, ImageView sensorR00, ImageView sensorR01, ImageView sensorR02,
      ImageView sensorR03, TextView textView8, TextView textViewL00, TextView textViewL01,
      TextView textViewL02, TextView textViewL03, TextView textViewR00, TextView textViewR01,
      TextView textViewR02, TextView textViewR03) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bodyWeight = bodyWeight;
    this.bottomNavigationView = bottomNavigationView;
    this.feetBackground2 = feetBackground2;
    this.fragmentContainerView = fragmentContainerView;
    this.gCenter = gCenter;
    this.lowPowerIndL = lowPowerIndL;
    this.lowPowerIndR = lowPowerIndR;
    this.sensorL00 = sensorL00;
    this.sensorL01 = sensorL01;
    this.sensorL02 = sensorL02;
    this.sensorL03 = sensorL03;
    this.sensorR00 = sensorR00;
    this.sensorR01 = sensorR01;
    this.sensorR02 = sensorR02;
    this.sensorR03 = sensorR03;
    this.textView8 = textView8;
    this.textViewL00 = textViewL00;
    this.textViewL01 = textViewL01;
    this.textViewL02 = textViewL02;
    this.textViewL03 = textViewL03;
    this.textViewR00 = textViewR00;
    this.textViewR01 = textViewR01;
    this.textViewR02 = textViewR02;
    this.textViewR03 = textViewR03;
  }

  @NonNull
  public static FragmentFeetFsrValueBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_feet_fsr_value, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentFeetFsrValueBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentFeetFsrValueBinding>inflateInternal(inflater, R.layout.fragment_feet_fsr_value, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentFeetFsrValueBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_feet_fsr_value, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentFeetFsrValueBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentFeetFsrValueBinding>inflateInternal(inflater, R.layout.fragment_feet_fsr_value, null, false, component);
  }

  public static FragmentFeetFsrValueBinding bind(@NonNull View view) {
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
  public static FragmentFeetFsrValueBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentFeetFsrValueBinding)bind(component, view, R.layout.fragment_feet_fsr_value);
  }
}
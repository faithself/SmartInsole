package de.kai_morich.simple_usb_terminal;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import de.kai_morich.simple_usb_terminal.databinding.FragmentAppBindingImpl;
import de.kai_morich.simple_usb_terminal.databinding.FragmentFeetFsrValueBindingImpl;
import de.kai_morich.simple_usb_terminal.databinding.FragmentMapBindingImpl;
import de.kai_morich.simple_usb_terminal.databinding.FragmentMedicalBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_FRAGMENTAPP = 1;

  private static final int LAYOUT_FRAGMENTFEETFSRVALUE = 2;

  private static final int LAYOUT_FRAGMENTMAP = 3;

  private static final int LAYOUT_FRAGMENTMEDICAL = 4;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(4);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(de.kai_morich.simple_usb_terminal.R.layout.fragment_app, LAYOUT_FRAGMENTAPP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(de.kai_morich.simple_usb_terminal.R.layout.fragment_feet_fsr_value, LAYOUT_FRAGMENTFEETFSRVALUE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(de.kai_morich.simple_usb_terminal.R.layout.fragment_map, LAYOUT_FRAGMENTMAP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(de.kai_morich.simple_usb_terminal.R.layout.fragment_medical, LAYOUT_FRAGMENTMEDICAL);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_FRAGMENTAPP: {
          if ("layout/fragment_app_0".equals(tag)) {
            return new FragmentAppBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_app is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTFEETFSRVALUE: {
          if ("layout/fragment_feet_fsr_value_0".equals(tag)) {
            return new FragmentFeetFsrValueBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_feet_fsr_value is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMAP: {
          if ("layout/fragment_map_0".equals(tag)) {
            return new FragmentMapBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_map is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMEDICAL: {
          if ("layout/fragment_medical_0".equals(tag)) {
            return new FragmentMedicalBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_medical is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(4);

    static {
      sKeys.put("layout/fragment_app_0", de.kai_morich.simple_usb_terminal.R.layout.fragment_app);
      sKeys.put("layout/fragment_feet_fsr_value_0", de.kai_morich.simple_usb_terminal.R.layout.fragment_feet_fsr_value);
      sKeys.put("layout/fragment_map_0", de.kai_morich.simple_usb_terminal.R.layout.fragment_map);
      sKeys.put("layout/fragment_medical_0", de.kai_morich.simple_usb_terminal.R.layout.fragment_medical);
    }
  }
}

package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new de.kai_morich.simple_usb_terminal.DataBinderMapperImpl());
  }
}

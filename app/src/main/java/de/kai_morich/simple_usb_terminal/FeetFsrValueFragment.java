package de.kai_morich.simple_usb_terminal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.kai_morich.simple_usb_terminal.databinding.FragmentFeetFsrValueBinding;

public class FeetFsrValueFragment extends Fragment  implements ServiceConnection, SerialListener{

    private enum Connected { False, Pending, True }
    private final BroadcastReceiver broadcastReceiver;
    private int deviceId, portNum, baudRate;
    private UsbSerialPort usbSerialPort;
    private SerialService service;
    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private Intent intent;
    private FragmentFeetFsrValueBinding fsrValueBinding;
    private GradientDrawable fsrBackgroundL0, fsrBackgroundL1, fsrBackgroundL2, fsrBackgroundL3;
    private GradientDrawable fsrBackgroundR0, fsrBackgroundR1, fsrBackgroundR2, fsrBackgroundR3;

    public FeetFsrValueFragment() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(Constants.INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    Boolean granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                    connect(granted);
                }
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        deviceId = getArguments().getInt("device");
        portNum = getArguments().getInt("port");
        baudRate = getArguments().getInt("baud");
        intent = new Intent(getActivity(), SerialService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Constants.INTENT_ACTION_GRANT_USB));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fsrValueBinding = FragmentFeetFsrValueBinding.inflate(inflater, container, false);

        BottomNavigationView bottomNavigationView = fsrValueBinding.bottomNavigationView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.fsr, R.id.map, R.id.medical).build();
        NavController navController;
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        return fsrValueBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        //Toast.makeText(getActivity(), "FeetFsrValueFragment is started.", Toast.LENGTH_SHORT).show();
        if(service != null)
            service.attach(this);
        else
            getActivity().startService(intent); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        if(service != null && !getActivity().isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        getActivity().stopService(intent);
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        try { getActivity().unbindService(this); } catch(Exception ignored) {}
        super.onDetach();
    }

    public int fsrMappingColor(short fsrValue)
    {
        byte factor = (byte) (fsrValue >> 4);
        byte cValue = (byte) (0xFF - factor);
        String s = "#" + String.format("%02x", cValue) + String.format("%02x", cValue) + String.format("%02x", cValue);
        return Color.parseColor(s);
    }

    @SuppressLint("SetTextI18n")
    private  void showFsrValueUpdateL() {
        if (fsrBackgroundL0 == null)
        {
            fsrBackgroundL0 = new GradientDrawable();
            fsrBackgroundL0.setShape(GradientDrawable.OVAL);
            fsrBackgroundL0.setSize(40, 40);
        }
        fsrBackgroundL0.setColor(fsrMappingColor(service.getFsr0Value()));
        fsrValueBinding.sensorL00.setBackground(fsrBackgroundL0);
        fsrValueBinding.textViewL00.setText(Short.toString(service.getFsr0Value()));
        if (fsrBackgroundL1 == null)
        {
            fsrBackgroundL1 = new GradientDrawable();
            fsrBackgroundL1.setShape(GradientDrawable.OVAL);
            fsrBackgroundL1.setSize(40, 40);
        }
        fsrBackgroundL1.setColor(fsrMappingColor(service.getFsr1Value()));
        fsrValueBinding.sensorL01.setBackground(fsrBackgroundL1);
        fsrValueBinding.textViewL01.setText(Short.toString(service.getFsr1Value()));
        if (fsrBackgroundL2 == null)
        {
            fsrBackgroundL2 = new GradientDrawable();
            fsrBackgroundL2.setShape(GradientDrawable.OVAL);
            fsrBackgroundL2.setSize(40, 40);
        }
        fsrBackgroundL2.setColor(fsrMappingColor(service.getFsr2Value()));
        fsrValueBinding.sensorL02.setBackground(fsrBackgroundL2);
        fsrValueBinding.textViewL02.setText(Short.toString(service.getFsr2Value()));
        if (fsrBackgroundL3 == null)
        {
            fsrBackgroundL3 = new GradientDrawable();
            fsrBackgroundL3.setShape(GradientDrawable.OVAL);
            fsrBackgroundL3.setSize(40, 40);
        }
        fsrBackgroundL3.setColor(fsrMappingColor(service.getFsr3Value()));
        fsrValueBinding.sensorL03.setBackground(fsrBackgroundL3);
        fsrValueBinding.textViewL03.setText(Short.toString(service.getFsr3Value()));
    }

    @SuppressLint("SetTextI18n")
    private void showFsrValueUpdateR() {
        if (fsrBackgroundR0 == null)
        {
            fsrBackgroundR0 = new GradientDrawable();
            fsrBackgroundR0.setShape(GradientDrawable.OVAL);
            fsrBackgroundR0.setSize(40, 40);
        }
        fsrBackgroundR0.setColor(fsrMappingColor(service.getFsr0Value()));
        fsrValueBinding.sensorR00.setBackground(fsrBackgroundR0);
        fsrValueBinding.textViewR00.setText(Short.toString(service.getFsr0Value()));
        if (fsrBackgroundR1 == null)
        {
            fsrBackgroundR1 = new GradientDrawable();
            fsrBackgroundR1.setShape(GradientDrawable.OVAL);
            fsrBackgroundR1.setSize(40, 40);
        }
        fsrBackgroundR1.setColor(fsrMappingColor(service.getFsr1Value()));
        fsrValueBinding.sensorR01.setBackground(fsrBackgroundR1);
        fsrValueBinding.textViewR01.setText(Short.toString(service.getFsr1Value()));
        if (fsrBackgroundR2 == null)
        {
            fsrBackgroundR2 = new GradientDrawable();
            fsrBackgroundR2.setShape(GradientDrawable.OVAL);
            fsrBackgroundR2.setSize(40, 40);
        }
        fsrBackgroundR2.setColor(fsrMappingColor(service.getFsr2Value()));
        fsrValueBinding.sensorR02.setBackground(fsrBackgroundR2);
        fsrValueBinding.textViewR02.setText(Short.toString(service.getFsr2Value()));
        if (fsrBackgroundR3 == null)
        {
            fsrBackgroundR3 = new GradientDrawable();
            fsrBackgroundR3.setShape(GradientDrawable.OVAL);
            fsrBackgroundR3.setSize(40, 40);
        }
        fsrBackgroundR3.setColor(fsrMappingColor(service.getFsr3Value()));
        fsrValueBinding.sensorR03.setBackground(fsrBackgroundR3);
        fsrValueBinding.textViewR03.setText(Short.toString(service.getFsr3Value()));
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if(initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }

        float centerOffset = (fsrValueBinding.sensorL02.getWidth() - fsrValueBinding.gCenter.getWidth()) / 2;

        service.getFsrValueCountL().observe(getViewLifecycleOwner(), integer -> {
            showFsrValueUpdateL();
        });

        service.getFsrValueCountR().observe(getViewLifecycleOwner(), integer -> {
            showFsrValueUpdateR();
        });

        service.getHorizontalMarkPer().observe(getViewLifecycleOwner(), aFloat -> {
            float gravityZoneW = (fsrValueBinding.sensorR01.getLeft() + fsrValueBinding.sensorR00.getLeft() - fsrValueBinding.sensorL03.getLeft()
                    - fsrValueBinding.sensorL02.getLeft()) / 2;
            fsrValueBinding.gCenter.setX(service.getHorizontalMarkPer().getValue() * gravityZoneW + (fsrValueBinding.sensorL03.getLeft()
                    + fsrValueBinding.sensorL02.getLeft()) / 2 + centerOffset);
        });

        service.getVerticalMarkPer().observe(getViewLifecycleOwner(), aFloat -> {
            float gravityZoneH = fsrValueBinding.sensorL00.getTop() - fsrValueBinding.sensorL03.getTop();
            fsrValueBinding.gCenter.setY(service.getVerticalMarkPer().getValue() * gravityZoneH + fsrValueBinding.sensorL03.getY()
                    + centerOffset);
        });

        service.getWeight().observe(getViewLifecycleOwner(), aFloat -> {
            fsrValueBinding.bodyWeight.setText(Float.toString(service.getWeight().getValue()) + "KG");
        });

        service.getLowPowerL().observe(getViewLifecycleOwner(), aBoolean -> {
            fsrValueBinding.lowPowerIndL.setText(Boolean.toString(service.getLowPowerL().getValue()));
        });

        service.getLowPowerR().observe(getViewLifecycleOwner(), aBoolean -> {
            fsrValueBinding.lowPowerIndR.setText(Boolean.toString(service.getLowPowerR().getValue()));
        });

    }

    public SerialService getService() {
        return service;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }

    /*
     * Serial + UI
     */
    private void connect() {
        connect(null);
    }

    private void connect(Boolean permissionGranted) {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        for(UsbDevice v : usbManager.getDeviceList().values())
            if(v.getDeviceId() == deviceId)
                device = v;
        if(device == null) {
            //status("connection failed: device not found");
            Toast.makeText(getActivity(),"connection failed: device not found",Toast.LENGTH_SHORT).show();
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if(driver == null) {
            //status("connection failed: no driver for device");
            Toast.makeText(getActivity(),"connection failed: no driver for device",Toast.LENGTH_SHORT).show();
            return;
        }
        if(driver.getPorts().size() < portNum) {
            //status("connection failed: not enough ports at device");
            Toast.makeText(getActivity(),"connection failed: not enough ports at device",Toast.LENGTH_SHORT).show();
            return;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if(usbConnection == null && permissionGranted == null && !usbManager.hasPermission(driver.getDevice())) {
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(Constants.INTENT_ACTION_GRANT_USB), 0);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if(usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
//                status("connection failed: permission denied");
                Toast.makeText(getActivity(),"connection failed: permission denied",Toast.LENGTH_SHORT).show();
            else
//                status("connection failed: open failed");
                Toast.makeText(getActivity(),"connection failed: open failed",Toast.LENGTH_SHORT).show();
            return;
        }

        connected = FeetFsrValueFragment.Connected.Pending;
        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), usbConnection, usbSerialPort);
            service.connect(socket);
            // usb connect is not asynchronous. connect-success and connect-error are returned immediately from socket.connect
            // for consistency to bluetooth/bluetooth-LE app use same SerialListener and SerialService classes
            onSerialConnect();
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = FeetFsrValueFragment.Connected.False;
        service.disconnect();
        usbSerialPort = null;
    }


    @Override
    public void onSerialConnect() {
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        //status("connection failed: " + e.getMessage());
        Toast.makeText(getActivity(), "connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
    }

    @Override
    public void onSerialIoError(Exception e) {
        //status("connection lost: " + e.getMessage());
        Toast.makeText(getActivity(), "connection lost: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        disconnect();
    }

}
package de.ultitech.matchingshape;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Set;

import de.ultitech.matchingshape.DynamicListPreference.DynamicListPreferenceOnClickListener;

/**
 * Created by davidgreiner on 3/2/15.
 */
public class SettingsActivity extends Activity {

    protected static final String BT_SELECT_DEVICE_KEY = "bluetooth_select_device";
    protected static final String NO_DEVICE_SELECTED = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = new SettingsFragment();
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();

        settingsFragment.getFragmentManager().executePendingTransactions();
        final DynamicListPreference btDevices = (DynamicListPreference) settingsFragment.findPreference(BT_SELECT_DEVICE_KEY);
        setAvailableBTDevicesOnList(btDevices);
        btDevices.setOnClickListener(new DynamicListPreferenceOnClickListener() {
            @Override
            public void onClick(DynamicListPreference preference) {
                setAvailableBTDevicesOnList(btDevices);
            }
        });
    }

    private enum BluetoothError {
        NO_ADAPTER,
        NOT_ENABLED,
        NO_DEVICE
    }

    private static void setAvailableBTDevicesOnList(DynamicListPreference listPreference) {
        BluetoothError error = null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String[] names = null;
        String[] values = null;
        if(bluetoothAdapter == null) {
            error = BluetoothError.NO_ADAPTER;
        } else if(!bluetoothAdapter.isEnabled()) {
            error = BluetoothError.NOT_ENABLED;
        } else {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() == 0) {
                error = BluetoothError.NO_DEVICE;
            } else {
                BluetoothDevice[] devices = new BluetoothDevice[pairedDevices.size()];
                devices = pairedDevices.toArray(devices);
                names = new String[devices.length];
                values = new String[devices.length];
                for (int i = 0; i < devices.length; i++) {
                    names[i] = devices[i].getName();
                    values[i] = devices[i].getName();
                }
            }
        }
        if(error != null) {
            switch(error) {
                case NO_ADAPTER:
                    listPreference.setDialogMessage(R.string.bluetooth_no_adapter_available);
                    break;
                case NOT_ENABLED:
                    listPreference.setDialogMessage(R.string.bluetooth_not_enabled);
                    break;
                case NO_DEVICE:
                    listPreference.setDialogMessage(R.string.bluetooth_no_devices_available);
                    break;
            }
            names = new String[]{};
            values = new String[]{};
        }
        listPreference.setEntries(names);
        listPreference.setEntryValues(values);
        listPreference.setDefaultValue(NO_DEVICE_SELECTED);
    }
}
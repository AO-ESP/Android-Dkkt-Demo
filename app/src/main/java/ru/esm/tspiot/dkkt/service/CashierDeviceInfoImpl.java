package ru.esm.tspiot.dkkt.service;

import android.os.Build;
import android.os.RemoteException;

public class CashierDeviceInfoImpl extends ICashierDeviceInfo.Stub {
    @Override
    public String getSerialNumber() throws RemoteException {
        return Build.SERIAL;
    }

    @Override
    public String getFirmwareVersion() throws RemoteException {
        return Build.VERSION.RELEASE;
    }

    @Override
    public String getConfigurationVersion() throws RemoteException {
        return Build.MANUFACTURER;
    }

    @Override
    public String getModelName() throws RemoteException {
        return Build.MODEL;
    }
}
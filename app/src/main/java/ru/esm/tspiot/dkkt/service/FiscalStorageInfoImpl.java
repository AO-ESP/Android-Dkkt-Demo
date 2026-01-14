package ru.esm.tspiot.dkkt.service;

import android.os.RemoteException;

public class FiscalStorageInfoImpl extends IFiscalStorageInfo.Stub {
    @Override
    public String getSerialNumber() throws RemoteException {
        return "9999078902018939";
    }

    @Override
    public String getFirmwareVersion() throws RemoteException {
        return "1.0";
    }
}
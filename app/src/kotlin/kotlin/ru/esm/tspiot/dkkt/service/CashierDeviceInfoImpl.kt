package ru.esm.tspiot.dkkt.service

import android.os.Build
import android.os.RemoteException

class CashierDeviceInfoImpl : ICashierDeviceInfo.Stub() {

    override fun getSerialNumber(): String = Build.SERIAL

    override fun getFirmwareVersion(): String = Build.VERSION.RELEASE
    override fun getConfigurationVersion(): String? = Build.VERSION.CODENAME

    override fun getModelName(): String? = Build.MODEL
}
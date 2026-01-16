package ru.esm.tspiot.dkkt.service

class FiscalStorageInfoImpl : IFiscalStorageInfo.Stub() {
    override fun getSerialNumber(): String? = "9999078900001234"
    override fun getFirmwareVersion(): String? = "1.0"
}
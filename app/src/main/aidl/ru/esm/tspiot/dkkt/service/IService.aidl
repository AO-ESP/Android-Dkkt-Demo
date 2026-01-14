package ru.esm.tspiot.dkkt.service;

import ru.esm.tspiot.dkkt.service.ServiceVersion;
import ru.esm.tspiot.dkkt.service.TrustedEncryptionResult;
import ru.esm.tspiot.dkkt.service.ControlledEncryptionResult;
import ru.esm.tspiot.dkkt.service.ICashierDeviceInfo;
import ru.esm.tspiot.dkkt.service.IFiscalStorageInfo;

interface IService {

    boolean isReady();

    ServiceVersion getVersion();

    String getTin();

    String getRegistrationNumber();

    String getSerialNumber();

    boolean isShiftOpened();

    void openShift();

    void closeShift();

    IFiscalStorageInfo getFiscalStorageInfo();

    ICashierDeviceInfo getCashierHardwareInfo();

    TrustedEncryptionResult encryptTrusted(in byte[] data);

    ControlledEncryptionResult encryptControlled(in byte[] data);

    byte[] decryptTrusted(in byte[] control, in byte[] data);

    byte[] decryptControlled(in byte[] data);

    void resetEncryptionSequence();
}
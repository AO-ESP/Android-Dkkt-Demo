package ru.esm.tspiot.dkkt.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.atomic.AtomicInteger;

public class DKService extends Service {
    private static final String TAG = "DKService";
    private static final String ACTION_BIND_SERVICE = "ru.esm.tspiot.dkkt.BIND_SERVICE";

    private boolean shiftOpened = false;
    private final AtomicInteger encryptionSequence = new AtomicInteger(0);
    private final byte[] mockEncryptionKey = "0123456789ABCDEF".getBytes();

    private final IService.Stub serviceImpl = new IService.Stub() {

        @Override
        public boolean isReady() throws RemoteException {
            Log.d(TAG, "DKService called isReady");
            return true;
        }

        @Override
        public ServiceVersion getVersion() throws RemoteException {
            Log.d(TAG, "DKService called getVersion");
            return new ServiceVersion("ESP Demo DKKT", 0);
        }

        @Override
        public String getInn() throws RemoteException {
            Log.d(TAG, "DKService called getTin");
            return "123456789012";
        }

        @Override
        public String getRegistrationNumber() throws RemoteException {
            Log.d(TAG, "DKService called getRegistrationNumber");
            return "КТ-000001";
        }

        @Override
        public String getSerialNumber() throws RemoteException {
            Log.d(TAG, "DKService called getSerialNumber");
            return "SN-987654321";
        }

        @Override
        public boolean isShiftOpened() throws RemoteException {
            Log.d(TAG, "DKService called isShiftOpened");
            return shiftOpened;
        }

        @Override
        public void openShift() throws RemoteException {
            Log.i(TAG, "Opening shift");
            shiftOpened = true;
        }

        @Override
        public void closeShift() throws RemoteException {
            Log.i(TAG, "Closing shift");
            shiftOpened = false;
        }

        @Override
        public IFiscalStorageInfo getFiscalStorageInfo() throws RemoteException {
            Log.d(TAG, "DKService called getFiscalStorageInfo");
            return new FiscalStorageInfoImpl();
        }

        @Override
        public ICashierDeviceInfo getCashierHardwareInfo() throws RemoteException {
            Log.d(TAG, "DKService called getCashierHardwareInfo");
            return new CashierDeviceInfoImpl();
        }

        @Override
        public TrustedEncryptionResult encryptTrusted(byte[] data) throws RemoteException {
            Log.d(TAG, "DKService called encryptTrusted");
            try {
                byte[] dataToEncrypt = data != null ? data : new byte[0];
                byte[] encrypted = performEncryption(dataToEncrypt);
                int sequence = encryptionSequence.incrementAndGet();
                byte[] controlCode = ("CTRL_" + sequence).getBytes();

                return new TrustedEncryptionResult(
                        encrypted,
                        controlCode
                );
            } catch (Exception e) {
                Log.e(TAG, "Encrypt trusted error", e);
                return new TrustedEncryptionResult(
                        new byte[0],
                        new byte[0]
                );
            }
        }

        @Override
        public ControlledEncryptionResult encryptControlled(byte[] data) throws RemoteException {
            Log.d(TAG, "DKService called encryptControlled");
            try {
                byte[] dataToEncrypt = data != null ? data : new byte[0];
                byte[] encrypted = performEncryption(dataToEncrypt);

                return new ControlledEncryptionResult(
                        encrypted
                );
            } catch (Exception e) {
                Log.e(TAG, "Encrypt controlled error", e);
                return new ControlledEncryptionResult(
                        new byte[0]
                );
            }
        }

        @Override
        public byte[] decryptTrusted(byte[] control, byte[] data) throws RemoteException {
            Log.d(TAG, "DKService called decryptTrusted");
            try {
                byte[] dataToDecrypt = data != null ? data : new byte[0];
                return performDecryption(dataToDecrypt);
            } catch (Exception e) {
                Log.e(TAG, "Decrypt trusted error", e);
                return new byte[0];
            }
        }

        @Override
        public byte[] decryptControlled(byte[] data) throws RemoteException {
            Log.d(TAG, "DKService called decryptControlled");
            try {
                byte[] dataToDecrypt = data != null ? data : new byte[0];
                return performDecryption(dataToDecrypt);
            } catch (Exception e) {
                Log.e(TAG, "Decrypt controlled error", e);
                return new byte[0];
            }
        }

        @Override
        public void resetEncryptionSequence() throws RemoteException {
            Log.d(TAG, "DKService called resetEncryptionSequence");
            Log.i(TAG, "Resetting encryption sequence");
            encryptionSequence.set(0);
        }
    };

    private byte[] performEncryption(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(mockEncryptionKey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    private byte[] performDecryption(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(mockEncryptionKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null && ACTION_BIND_SERVICE.equals(intent.getAction())) {
            Log.d(TAG, "Service bound with action: " + ACTION_BIND_SERVICE);
            return serviceImpl;
        } else {
            Log.w(TAG, "Invalid bind action: " + (intent != null ? intent.getAction() : "null"));
            return null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
        super.onDestroy();
    }
}
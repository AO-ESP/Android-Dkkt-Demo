package ru.esm.tspiot.dkkt.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import java.util.concurrent.atomic.AtomicInteger
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DKService : Service() {
    companion object {
        private const val TAG = "DKService"
        private const val ACTION_BIND_SERVICE = "ru.esm.tspiot.dkkt.BIND_SERVICE"
    }

    private var shiftOpened = false
    private val encryptionSequence = AtomicInteger(0)
    private val mockEncryptionKey = "0123456789ABCDEF".toByteArray()

    private val serviceImpl = object : IService.Stub() {

        override fun isReady(): Boolean = true

        override fun getVersion(): ServiceVersion =
            ServiceVersion(1, 0, 0, "build-001")

        override fun getInn(): String = "123456789012"

        override fun getRegistrationNumber(): String = "КТ-000001"

        override fun getSerialNumber(): String = "SN-987654321"

        override fun isShiftOpened(): Boolean = shiftOpened

        @Throws(RemoteException::class)
        override fun openShift() {
            Log.i(TAG, "Opening shift")
            shiftOpened = true
        }

        @Throws(RemoteException::class)
        override fun closeShift() {
            Log.i(TAG, "Closing shift")
            shiftOpened = false
        }

        override fun getFiscalStorageInfo(): IFiscalStorageInfo =
            FiscalStorageInfoImpl()

        override fun getCashierHardwareInfo(): ICashierDeviceInfo =
            CashierDeviceInfoImpl()

        @Throws(RemoteException::class)
        override fun encryptTrusted(data: ByteArray?): TrustedEncryptionResult {
            return try {
                val dataToEncrypt = data ?: byteArrayOf()
                val encrypted = performEncryption(dataToEncrypt)
                val sequence = encryptionSequence.incrementAndGet()
                val controlCode = "CTRL_$sequence".toByteArray()

                TrustedEncryptionResult(
                    encryptedData = encrypted,
                    controlCode = controlCode,
                    statusCode = 0,
                    statusMessage = "SUCCESS"
                )
            } catch (e: Exception) {
                Log.e(TAG, "Encrypt trusted error", e)
                TrustedEncryptionResult(
                    encryptedData = byteArrayOf(),
                    controlCode = byteArrayOf(),
                    statusCode = 1,
                    statusMessage = "ERROR: ${e.message}"
                )
            }
        }

        @Throws(RemoteException::class)
        override fun encryptControlled(data: ByteArray?): ControlledEncryptionResult {
            return try {
                val dataToEncrypt = data ?: byteArrayOf()
                val encrypted = performEncryption(dataToEncrypt)

                ControlledEncryptionResult(
                    encryptedData = encrypted,
                    statusCode = 0,
                    statusMessage = "SUCCESS"
                )
            } catch (e: Exception) {
                Log.e(TAG, "Encrypt controlled error", e)
                ControlledEncryptionResult(
                    encryptedData = byteArrayOf(),
                    statusCode = 1,
                    statusMessage = "ERROR: ${e.message}"
                )
            }
        }

        @Throws(RemoteException::class)
        override fun decryptTrusted(control: ByteArray?, data: ByteArray?): ByteArray {
            return try {
                val dataToDecrypt = data ?: byteArrayOf()
                performDecryption(dataToDecrypt)
            } catch (e: Exception) {
                Log.e(TAG, "Decrypt trusted error", e)
                byteArrayOf()
            }
        }

        @Throws(RemoteException::class)
        override fun decryptControlled(data: ByteArray?): ByteArray {
            return try {
                val dataToDecrypt = data ?: byteArrayOf()
                performDecryption(dataToDecrypt)
            } catch (e: Exception) {
                Log.e(TAG, "Decrypt controlled error", e)
                byteArrayOf()
            }
        }

        @Throws(RemoteException::class)
        override fun resetEncryptionSequence() {
            Log.i(TAG, "Resetting encryption sequence")
            encryptionSequence.set(0)
        }
    }

    private fun performEncryption(data: ByteArray): ByteArray {
        // В реальном приложении здесь должна быть настоящая криптография
        // Это примерная реализация для демонстрации
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val keySpec = SecretKeySpec(mockEncryptionKey, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        return cipher.doFinal(data)
    }

    private fun performDecryption(data: ByteArray): ByteArray {
        // В реальном приложении здесь должна быть настоящая криптография
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val keySpec = SecretKeySpec(mockEncryptionKey, "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        return cipher.doFinal(data)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return if (intent?.action == ACTION_BIND_SERVICE) {
            Log.d(TAG, "Service bound with action: $ACTION_BIND_SERVICE")
            serviceImpl
        } else {
            Log.w(TAG, "Invalid bind action: ${intent?.action}")
            null
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    override fun onDestroy() {
        Log.d(TAG, "Service destroyed")
        super.onDestroy()
    }
}
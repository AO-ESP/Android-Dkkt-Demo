package ru.esm.tspiot.dkkt.service;

import ru.esm.tspiot.dkkt.service.ServiceVersion;
import ru.esm.tspiot.dkkt.service.TrustedEncryptionResult;
import ru.esm.tspiot.dkkt.service.ControlledEncryptionResult;
import ru.esm.tspiot.dkkt.service.ICashierDeviceInfo;
import ru.esm.tspiot.dkkt.service.IFiscalStorageInfo;

/**
 * Основной интерфейс для взаимодействия с фискальным сервисом.
 * Предоставляет методы для управления сменами, получения информации
 * об устройстве и выполнения криптографических операций.
 *
 * Все реализации должны гарантировать потокобезопасность методов
 * и корректное сохранение состояния между вызовами.
 *
 * @version 1.0
 */
interface IService {

    /**
     * Проверяет готовность сервиса к выполнению операций.
     * Сервис считается готовым, если он инициализирован, подключен
     * к фискальному накопителю и аппаратному обеспечению.
     *
     * @return {@code true} - сервис готов к работе,
     *         {@code false} - сервис не готов
     * @throws ServiceConnectionException если произошла ошибка
     *         при проверке состояния сервиса
     */
    boolean isReady();

    /**
     * Возвращает версию сервиса.
     *
     * @return объект {@link ServiceVersion}, содержащий информацию
     *         о версии сервиса. Не может быть {@code null} для
     *         готового сервиса.
     */
    ServiceVersion getVersion();

    /**
     * Возвращает ИНН (идентификационный номер налогоплательщика),
     * связанный с фискальным устройством.
     *
     * @return строка с ИНН в формате 10 или 12 цифр. Формат зависит от типа налогоплательщика.
     * @throws ServiceNotReadyException если сервис не готов
     */
    String getInn();

    /**
     * Возвращает регистрационный номер фискального накопителя (ФН).
     * Регистрационный номер присваивается при постановке на учет
     * в налоговой службе.
     *
     * @return строковое представление регистрационного номера ФН
     *         (16 цифр)
     * @throws ServiceNotReadyException если сервис не готов
     */
    String getRegistrationNumber();

    /**
     * Возвращает серийный номер фискального накопителя.
     *
     * @return заводской серийный номер устройства
     * @throws ServiceNotReadyException если сервис не готов
     */
    String getSerialNumber();

    /**
     * Проверяет состояние текущей смены.
     * Фискальные операции могут выполняться только при открытой смене.
     *
     * @return {@code true} - смена открыта,
     *         {@code false} - смена закрыта
     * @throws ServiceNotReadyException если сервис не готов
     */
    boolean isShiftOpened();

    /**
     * Открывает новую фискальную смену.
     * Перед открытием смены проверяется отсутствие активной смены
     * и готовность сервиса.
     *
     * @throws ShiftAlreadyOpenedException если смена уже открыта
     * @throws ServiceNotReadyException если сервис не готов
     * @throws FiscalStorageException при ошибках фискального накопителя
     */
    void openShift();

    /**
     * Закрывает текущую фискальную смену.
     * При закрытии формируется Z-отчет и сохраняются данные смены.
     *
     * @throws ShiftNotOpenedException если смена не открыта
     * @throws PendingOperationsException если есть незавершенные операции
     * @throws FiscalStorageException при ошибках фискального накопителя
     */
    void closeShift();

    /**
     * Возвращает детальную информацию о фискальном накопителе.
     *
     * @return объект {@link IFiscalStorageInfo}, содержащий информацию
     *         о состоянии, сроке действия и памяти ФН
     * @throws ServiceNotReadyException если сервис не готов
     */
    IFiscalStorageInfo getFiscalStorageInfo();

    /**
     * Возвращает информацию об аппаратном обеспечении кассового места.
     *
     * @return объект {@link ICashierDeviceInfo} с данными о подключенном
     *         оборудовании (фискальный регистратор, принтер и т.д.)
     * @throws ServiceNotReadyException если сервис не готов
     */
    ICashierDeviceInfo getCashierHardwareInfo();

    /**
     * Выполняет доверенное шифрование данных.
     * Используется для защиты фискальных данных, требующих
     * гарантированной целостности и аутентичности.
     *
     * @param data исходные данные для шифрования. Не может быть {@code null}
     * @return результат шифрования, содержащий зашифрованные данные
     *         и контрольную информацию для верификации
     * @throws IllegalArgumentException если {@code data} равен {@code null}
     * @throws EncryptionException при ошибках криптографических операций
     * @throws ServiceNotReadyException если сервис не готов
     */
    TrustedEncryptionResult encryptTrusted(in byte[] data);

    /**
     * Выполняет управляемое шифрование данных.
     * Используется для шифрования данных с возможностью
     * последующего контроля целостности на стороне сервиса.
     *
     * @param data исходные данные для шифрования. Не может быть {@code null}
     * @return результат управляемого шифрования
     * @throws IllegalArgumentException если {@code data} равен {@code null}
     * @throws EncryptionException при ошибках криптографических операций
     * @throws ServiceNotReadyException если сервис не готов
     */
    ControlledEncryptionResult encryptControlled(in byte[] data);

    /**
     * Расшифровывает данные, зашифрованные методом доверенного шифрования.
     * Выполняется проверка целостности данных с использованием
     * контрольной информации.
     *
     * @param control контрольные данные, полученные при шифровании.
     *                Не может быть {@code null}
     * @param data зашифрованные данные. Не может быть {@code null}
     * @return расшифрованные данные
     * @throws IllegalArgumentException если {@code control} или
     *         {@code data} равны {@code null}
     * @throws IntegrityCheckException если проверка целостности не пройдена
     * @throws EncryptionException при ошибках дешифрования
     * @throws ServiceNotReadyException если сервис не готов
     */
    byte[] decryptTrusted(in byte[] control, in byte[] data);

    /**
     * Расшифровывает данные, зашифрованные методом управляемого шифрования.
     * Контроль целостности должен выполняться отдельно.
     *
     * @param data зашифрованные данные. Не может быть {@code null}
     * @return расшифрованные данные
     * @throws IllegalArgumentException если {@code data} равен {@code null}
     * @throws EncryptionException при ошибках дешифрования
     * @throws ServiceNotReadyException если сервис не готов
     */
    byte[] decryptControlled(in byte[] data);

    /**
     * Сбрасывает внутренний счетчик последовательности шифрования.
     * Используется для синхронизации состояний шифрования между
     * клиентом и сервисом после сбоев или перезапусков.
     *
     * @throws EncryptionException при ошибках сброса состояния
     * @throws ServiceNotReadyException если сервис не готов
     */
    void resetEncryptionSequence();
}
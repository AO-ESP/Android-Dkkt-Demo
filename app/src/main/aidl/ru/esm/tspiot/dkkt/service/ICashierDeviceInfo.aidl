package ru.esm.tspiot.dkkt.service;

interface ICashierDeviceInfo {
    String getSerialNumber();
    String getFirmwareVersion();
    String getConfigurationVersion();
    String getModelName();
}
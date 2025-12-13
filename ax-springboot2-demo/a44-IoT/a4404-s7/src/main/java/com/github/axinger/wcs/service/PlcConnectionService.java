package com.github.axinger.wcs.service;

public interface PlcConnectionService {

    int getPlcConnectionStatus();

    boolean open();

    boolean close();
}

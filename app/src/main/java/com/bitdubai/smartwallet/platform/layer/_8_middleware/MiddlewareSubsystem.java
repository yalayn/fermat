package com.bitdubai.smartwallet.platform.layer._8_middleware;


/**
 * Created by ciencias on 20.01.15.
 */
public interface MiddlewareSubsystem {
    public void start () throws CantStartSubsystemException;
    public MiddlewareEngine getMiddlewareEngine();
}

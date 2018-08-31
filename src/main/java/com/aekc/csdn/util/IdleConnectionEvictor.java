package com.aekc.csdn.util;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionEvictor extends Thread {

    private final HttpClientConnectionManager connectionManager;

    private volatile boolean shutdown;

    private Integer waitTime;

    public IdleConnectionEvictor(HttpClientConnectionManager connectionManager, Integer waitTime) {
        super();
        this.connectionManager = connectionManager;
        this.waitTime = waitTime;
        this.start();
    }

    @Override
    public void run() {
        try {
            while(!shutdown) {
                synchronized (this) {
                    wait(waitTime);
                    //关闭无效连接
                    connectionManager.closeExpiredConnections();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}

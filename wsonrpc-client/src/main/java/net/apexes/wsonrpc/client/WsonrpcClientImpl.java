/*
 * Copyright (C) 2016, apexes.net. All rights reserved.
 * 
 *        http://www.apexes.net
 * 
 */
package net.apexes.wsonrpc.client;

import net.apexes.wsonrpc.core.ServiceRegistry;
import net.apexes.wsonrpc.core.WsonrpcEndpoint;
import net.apexes.wsonrpc.core.WebSocketSession;

import java.io.IOException;

/**
 * 
 * @author <a href="mailto:hedyn@foxmail.com">HeDYn</a>
 *
 */
public class WsonrpcClientImpl extends WsonrpcEndpoint implements WsonrpcClient, WsonrpcClientEndpoint {
    
    private final WsonrpcClientConfig config;
    private WsonrpcClientListener clientListener;

    protected WsonrpcClientImpl(WsonrpcClientConfig config) {
        super(config);
        this.config = config;
    }

    private synchronized void fireOpen() {
        if (clientListener != null) {
            clientListener.onOpen(this);
        }
    }

    private synchronized void fireClose(int code, String reason) {
        if (clientListener != null) {
            clientListener.onClose(this, code, reason);
        }
    }
    
    private synchronized void fireSentMessage(byte[] bytes) {
        if (clientListener != null) {
            clientListener.onSentMessage(bytes);
        }
    }
    
    private synchronized void fireSentPing() {
        if (clientListener != null) {
            clientListener.onSentPing();
        }
    }

    @Override
    public void connect() throws Exception {
        if (!isConnected()) {
            config.getWebsocketConnector().connectToServer(this, config.getURI());
        }
    }

    @Override
    public void onOpen(WebSocketSession session) {
        online(new WsonrpcSessionProxy(session));
        fireOpen();
    }

    @Override
    public void onMessage(byte[] bytes) {
        wsonrpcControl.handle(getSession(), bytes);
    }

    @Override
    public void onError(Throwable error) {
        if (config.getErrorProcessor() != null) {
            config.getErrorProcessor().onError(getSessionId(), error);
        }
    }

    @Override
    public void onClose(int code, String reason) {
        offline();
        fireClose(code, reason);
    }

    @Override
    public ServiceRegistry getServiceRegistry() {
        return wsonrpcControl.getServiceRegistry();
    }
    
    @Override
    public int getConnectTimeout() {
        return config.getConnectTimeout();
    }

    @Override
    public void setClientListener(WsonrpcClientListener listener) {
        this.clientListener = listener;
    }

    /**
     * 
     * @author <a href="mailto:hedyn@foxmail.com">HeDYn</a>
     *
     */
    private class WsonrpcSessionProxy implements WebSocketSession {
        
        private final WebSocketSession session;
        
        private WsonrpcSessionProxy(WebSocketSession session) {
            this.session = session;
        }
        
        @Override
        public String getId() {
            return session.getId();
        }

        @Override
        public boolean isOpen() {
            return session.isOpen();
        }

        @Override
        public void sendBinary(byte[] bytes) throws IOException {
            session.sendBinary(bytes);
            fireSentMessage(bytes);
        }

        @Override
        public void ping() throws IOException {
            session.ping();
            fireSentPing();
        }

        @Override
        public void close() throws IOException {
            session.close();
        }
        
    }

}

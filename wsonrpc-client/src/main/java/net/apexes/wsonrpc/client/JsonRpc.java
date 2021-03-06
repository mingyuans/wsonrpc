/*
 * Copyright (C) 2016, apexes.net. All rights reserved.
 * 
 *        http://www.apexes.net
 * 
 */
package net.apexes.wsonrpc.client;

import java.net.MalformedURLException;
import java.net.URL;

import net.apexes.wsonrpc.core.RemoteInvoker;
import net.apexes.wsonrpc.json.JsonImplementor;

/**
 * 
 * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
 *
 */
public final class JsonRpc {
    
    public static JsonRpc url(String url) {
        return new JsonRpc(url);
    }
    
    private final String url;
    private JsonImplementor jsonImpl;
    private int connectTimeout;
    private boolean acceptCompress;
    
    private JsonRpc(String url) {
        this.url = url;
    }
    
    public JsonRpc json(JsonImplementor jsonImpl) {
        this.jsonImpl = jsonImpl;
        return this;
    }
    
    /**
     * 
     * @param connectTimeout
     * @return
     */
    public JsonRpc connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
    
    /**
     * 
     * @param acceptCompress
     * @return
     */
    public JsonRpc acceptCompress(boolean acceptCompress) {
        this.acceptCompress = acceptCompress;
        return this;
    }

    /**
     * 
     * @return
     * @throws MalformedURLException
     */
    public RemoteInvoker invoker() throws MalformedURLException {
        HttpJsonRpcRemote remote = new HttpJsonRpcRemote(new URL(url), jsonImpl);
        remote.setConnectTimeout(connectTimeout);
        remote.setAcceptCompress(acceptCompress);
        return RemoteInvoker.create(remote);
    }

}

/*
 * Copyright (C) 2015, apexes.net. All rights reserved.
 * 
 *        http://www.apexes.net
 * 
 */
package net.apexes.wsonrpc.internal;

import java.lang.reflect.Type;

import net.apexes.wsonrpc.internal.IKey.StringKey;
import net.apexes.wsonrpc.util.AbstractFuture;

/**
 * 
 * @author <a href=mailto:hedyn@foxmail.com>HeDYn</a>
 *
 */
class WsonrpcFuture<V> extends AbstractFuture<V> {

    final IKey key;
    final Type returnType;

    WsonrpcFuture(String id, Type returnType) {
        if (id == null) {
            throw new IllegalArgumentException("The id must be not null.");
        }
        this.key = new StringKey(id);
        this.returnType = returnType;
    }

    @Override
    public boolean set(V value) {
        return super.set(value);
    }

    @Override
    public boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }

    @Override
    public int hashCode() {
        return key.id().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof IKey) {
            IKey ok = (IKey) obj;
            return ok.id().equals(key.id());
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WsonrpcFuture<?> other = (WsonrpcFuture<?>) obj;
        if (other.key.id() == null) {
            return false;
        }
        return key.id().equals(other.key.id());
    }

    @Override
    public String toString() {
        return "WosonrpcFuture [id=" + key.id() + "]";
    }

}
/*
 * Copyright 2014 Sabit SAS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package co.sabit.persistence.usertype;

import co.sabit.persistence.entity.PersistentEnum;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * UserType para persistir <code>Enum</code>
 * @author Andrés Mise Olivera
 * @param <T> tipo de <code>Enum</code>
 */
public abstract class PersistentEnumUserType<T extends PersistentEnum> implements UserType {
 
    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }
 
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }
 
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }
 
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }
 
    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }
 
    @Override
    public boolean isMutable() {
        return false;
    }
 
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor si,
            Object owner)
            throws HibernateException, SQLException {
        Byte id = rs.getByte(names[0]);
        if(rs.wasNull()) {
            return null;
        }
        for(PersistentEnum value : returnedClass().getEnumConstants()) {
            if(value.getId().equals(id)) {
                return value;
            }
        }
        throw new IllegalStateException("Unknown " + returnedClass().getSimpleName() + " id");
    }
 
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, 
            SessionImplementor si)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.TINYINT);
        } else {
            st.setInt(index, (int) ((PersistentEnum)value).getId());
        }
    }
 
    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }
 
    @Override
    public abstract Class<T> returnedClass();
 
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.TINYINT};
    }
 
}

package co.org.conte.usertype;

import co.org.conte.enumerator.EstadoInspector;
import co.sabit.persistence.usertype.PersistentEnumUserType;

/**
 * UserType para EstadoInspector
 * @author Andrés Mise Olivera.
 */
public class EstadoInspectorUserType extends PersistentEnumUserType<EstadoInspector>{

    @Override
    public Class<EstadoInspector> returnedClass() {
        return EstadoInspector.class;
    }
}

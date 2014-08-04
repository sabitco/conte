package co.org.conte.enumerator;

import co.sabit.persistence.entity.PersistentEnum;

/**
 * Contiene los estados de un inspector.
 * @author Andrés Mise Olivera.
 */
public enum EstadoInspector implements PersistentEnum {
 
    ACTIVO((byte) 1, "Activo"), INACTIVO((byte) 2, "Inactivo");
    
    /**
     * Identificador.
     */
    private Byte id;
    
    /**
     * Descripcion del estado.
     */
    private String descripcion;

    private EstadoInspector(Byte id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    @Override
    public Byte getId() {
        return this.id;
    }

    @Override
    public void setId(Byte id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }
}

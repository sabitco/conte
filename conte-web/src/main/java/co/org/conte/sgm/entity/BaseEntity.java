/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.entity;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Transient;


/**
 *
 * @author jam
 */

public class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2720354764385235661L;

	private static Map<Class<? extends BaseEntity>, Method> mapIdMethods = new HashMap<Class<? extends BaseEntity>, Method>();
	
	@Id
	private Long test;		
	
	public Long getTest() {
		return test;
	}

	public void setTest(Long test) {
		this.test = test;
	}

	protected Serializable findEntityId(){
		if( !BaseEntity.mapIdMethods.containsKey( this.getClass() ) ){
			Method method = null;
			for( Field field : this.getClass().getDeclaredFields() ){			
				Id id = field.getAnnotation( Id.class );
				EmbeddedId embeddedId = field.getAnnotation( EmbeddedId.class );
				if( id != null || embeddedId != null ){					
					try {						
						method = this.getClass().getDeclaredMethod( "get"+field.getName().substring( 0, 1 ).toUpperCase()+field.getName().substring( 1 ), null );
					} catch (Exception e) {
						method = null;
					}
					break;
				}				
			}	
			BaseEntity.mapIdMethods.put( this.getClass(), method );
		}		
		Method method = BaseEntity.mapIdMethods.get( this.getClass() );
		if( method != null ){
			try {
				return (Serializable)method.invoke(this, null);				
			} catch (Exception e) {
				return null;
			} 
		}
		else{
			return null;
		}					
	}
	
	@Override
	public int hashCode() {
		Serializable id = this.findEntityId(); 
		if( id != null ){
			return id.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return ( obj != null &&  this.getClass().isInstance( obj ) && this.hashCode() == ((BaseEntity)obj).hashCode() );		
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+": "+this.findEntityId();
	}
                
        @Transient
        public Serializable findId(){
            return findEntityId();
        }
	
}

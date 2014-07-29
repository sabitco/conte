/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conte.exception;

/**
 * Excepci&oacute;n t&eacute;cnica del sistema Infovalmer. Esta excepci&oacute;n es propagada cuando existe un problema asociado
 * al flujo de ejecuci&oacute;n del sistema Infovalmer.
 */
public class SistemaException extends RuntimeException {

  private static final long serialVersionUID = -39218462767344609L;

  public SistemaException(String message) {
    //super(message);
  }

  public SistemaException(String message, Throwable cause) {
    //super(message, cause);
  }
}
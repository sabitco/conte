package co.org.conte.sgm.dao.exception;

/**
 * 
 * @author Andrés Mise Olivera
 */
public class DaoException extends Exception {

    private static final long serialVersionUID = -4075715518678558864L;
    
    public DaoException() {
    }
    
    public DaoException(String message) {
        super(message);
    }
    
    public DaoException(Throwable cause) {
        super(cause);
    }
    
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
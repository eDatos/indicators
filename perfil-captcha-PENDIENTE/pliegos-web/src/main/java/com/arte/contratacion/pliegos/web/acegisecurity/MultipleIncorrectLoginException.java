package com.arte.contratacion.pliegos.web.acegisecurity;

import org.acegisecurity.AuthenticationException;

/**
 * Lanzada cuando se han realizado elevados intentos erróneos de login.
 * Se utiliza para mostrar mensaje de advertencia y el captcha
 */
public class MultipleIncorrectLoginException extends AuthenticationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MultipleIncorrectLoginException(String msg) {
        super(msg);
    }
    
    public MultipleIncorrectLoginException(String msg, Throwable t) {
        super(msg, t);
    }
}

package com.arte.contratacion.pliegos.web.acegisecurity;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.servlet.Constants;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.LockedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.BadSqlGrammarException;

import com.arte.contratacion.ServiceLocator;
import com.arte.contratacion.pliegos.conf.AppConfiguration;
import com.arte.contratacion.pliegos.vo.UsuarioVO;


public class AuthenticationProcessingFilterToLocked extends org.acegisecurity.ui.webapp.AuthenticationProcessingFilter {
	
	private static Log LOG = LogFactory.getLog(AuthenticationProcessingFilterToLocked.class);
	
	public static final String CAPTCHA_KEY = "j_captcha";
	
	/**
	 * Si se debía mostrar el captcha, comprobar que es correcto
	 **/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {

		String nombreUsuario = obtainUsername(request);
		if (!StringUtils.isBlank(nombreUsuario)) {
			UsuarioVO usuarioVO = ServiceLocator.instance().getAdministracionServicio().obtenerUsuario(nombreUsuario);
			Integer intentosAumentarNivelSeguridad = AppConfiguration.instance().getConfiguration().getInteger("login.numeroIntentosAumentarSeguridad", Integer.valueOf(3));  // TODO constante en PropertiesConstants
			if (usuarioVO.getNumIntentosFallidos() != null && usuarioVO.getNumIntentosFallidos() > intentosAumentarNivelSeguridad) {
				String captchaIntroducido = request.getParameter(CAPTCHA_KEY);
				String captchaCorrecto = (String) request.getSession().getAttribute(Constants.SIMPLE_CAPCHA_SESSION_KEY);
				if (captchaIntroducido == null || captchaCorrecto == null || !captchaIntroducido.equals(captchaCorrecto)) {
					throw new MultipleIncorrectLoginException("captcha authentication error");
				}
			}
		}
		return super.attemptAuthentication(request);
	}

	
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {

		UsuarioVO usuarioVO = ServiceLocator.instance().getAdministracionServicio().obtenerUsuario(authResult.getName());
		if (usuarioVO.getBloqueado() != null && usuarioVO.getBloqueado()) {
			LOG.info("Login correcto pero cuenta bloqueada del usuario: " + authResult.getName());
			// Si la cuenta está bloqueada, impedir el acceso
			sendRedirect(request, response, "/usuarioBloqueado.iface");
		} else {
			// Anotar acceso correcto
			ServiceLocator.instance().getAdministracionServicio().anotarLoginCorrecto(authResult.getName());
			super.successfulAuthentication(request, response, authResult);
		}
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
		
		boolean loginIncorrecto = false;
		if (failed instanceof BadCredentialsException) {
			loginIncorrecto = true;
		} else if (failed.getCause() != null && (failed.getCause() instanceof BadSqlGrammarException) && failed.getMessage() != null && failed.getMessage().contains("SQLException")) {
			// Esta excepción puede ocurrir en Preproducción o Explotación, cuando se intenta loguear con LDAP, falla y pasa 
			// a intentar autenticarse por JDBC con la tabla 'users', que no existe en esos entornos (sólo es para local)
			loginIncorrecto = true;
		}
		
		if (loginIncorrecto) {
			String nombreUsuario = failed.getAuthentication().getName();
			if (!StringUtils.isBlank(nombreUsuario)) {
				
		    	// Si la cuenta está bloqueada, mostrar página de cuenta bloqueada. Si no, se volverá a página de login
				UsuarioVO usuarioVO = ServiceLocator.instance().getAdministracionServicio().obtenerUsuario(nombreUsuario);
				if (usuarioVO != null) {
					if (usuarioVO.getBloqueado() != null && usuarioVO.getBloqueado()) {
						failed = new LockedException("Cuenta bloqueada para el usuario " + nombreUsuario, failed); // hará que se muestre página de usuario bloqueado
					} else {
						// Actualizar usuario añadiendo un intento fallido
				        ServiceLocator.instance().getAdministracionServicio().anotarLoginIncorrecto(nombreUsuario);
	
						// Mostrar advertencia si se han producido ya varios intentos de login incorrectos (mostrará también el captch)
				        Integer intentosAumentarNivelSeguridad = AppConfiguration.instance().getConfiguration().getInteger("login.numeroIntentosAumentarSeguridad", Integer.valueOf(3));  // TODO constante en PropertiesConstants
						if (usuarioVO.getNumIntentosFallidos().intValue() >= intentosAumentarNivelSeguridad) {
							failed = new MultipleIncorrectLoginException("Elevados intentos fallidos de login del usuario " + nombreUsuario, failed);
						}
					}
				}
			}
		} else {
			LOG.info("Login incorrecto con otra excepción: " + failed);
		}
		super.unsuccessfulAuthentication(request, response, failed);
	}
}

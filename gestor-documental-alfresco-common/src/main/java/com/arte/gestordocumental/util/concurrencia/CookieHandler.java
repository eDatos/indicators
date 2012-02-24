package com.arte.gestordocumental.util.concurrencia;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * @author Arte Consultores
 *
 */
public class CookieHandler extends BasicHandler 
{
    private static final long serialVersionUID = 5355053439499560511L;

    public void invoke(MessageContext context) 
        throws AxisFault 
    {
        String sessionId = AuthenticationUtils.getAuthenticationDetails().getSessionId();
        if (sessionId != null)
        {
            context.setProperty(HTTPConstants.HEADER_COOKIE, "JSESSIONID=" + sessionId);
        }
    }
 }
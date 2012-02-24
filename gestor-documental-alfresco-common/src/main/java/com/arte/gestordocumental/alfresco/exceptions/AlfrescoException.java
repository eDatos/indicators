package com.arte.gestordocumental.alfresco.exceptions;

import org.apache.commons.beanutils.PropertyUtils;


public class AlfrescoException extends Exception {

    /**
	 * 
	 */
	private static final long	serialVersionUID	= 1833206603187341077L;

	/**
     * The default constructor.
     */
    public AlfrescoException()
    {}

    /**
     * Constructs a new instance of AlfrescoArteException
     *
     * @param throwable the parent Throwable
     */
    public AlfrescoException(Throwable throwable)
    {
        super(findRootCause(throwable));
    }

    /**
     * Constructs a new instance of AlfrescoArteException
     *
     * @param message the throwable message.
     */
    public AlfrescoException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new instance of AlfrescoArteException
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public AlfrescoException(String message, Throwable throwable)
    {
        super(message, findRootCause(throwable));
    }

    /**
     * Finds the root cause of the parent exception
     * by traveling up the exception tree
     */
    private static Throwable findRootCause(Throwable th)
    {
        if (th != null)
        {
            // Lets reflectively get any JMX or EJB exception causes.
            try
            {
                Throwable targetException = null;
                // java.lang.reflect.InvocationTargetException
                // or javax.management.ReflectionException
                String exceptionProperty = "targetException";
                if (PropertyUtils.isReadable(th, exceptionProperty))
                {
                    targetException = (Throwable)PropertyUtils.getProperty(th, exceptionProperty);
                }
                else
                {
                    exceptionProperty = "causedByException";
                    //javax.ejb.EJBException
                    if (PropertyUtils.isReadable(th, exceptionProperty))
                    {
                        targetException = (Throwable)PropertyUtils.getProperty(th, exceptionProperty);
                    }
                }
                if (targetException != null)
                {
                    th = targetException;
                }
            }
            catch (Exception ex)
            {
                // just print the exception and continue
                ex.printStackTrace();
            }

            if (th.getCause() != null)
            {
                th = th.getCause();
                th = findRootCause(th);
            }
        }
        return th;
    }
}

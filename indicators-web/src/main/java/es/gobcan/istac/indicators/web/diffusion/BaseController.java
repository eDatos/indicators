package es.gobcan.istac.indicators.web.diffusion;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

    @Autowired
    private MessageSource messageSource;

    private static String REDIRECT_PREFIX = "redirect:";

    protected Logger      logger          = LoggerFactory.getLogger(getClass());

    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        ModelAndView mv = new ModelAndView(viewName);
        if (logger.isDebugEnabled()) {
            logger.debug("Exposing Exception as model attribute 'exception'");
        }
        mv.addObject("exception", ex);
        return mv;
    }

    protected String redirectToView(String viewName) {
        return new StringBuilder().append(REDIRECT_PREFIX).append(viewName).toString();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ModelAndView handleIOException(EmptyResultDataAccessException ex, HttpServletRequest request) {
        logger.error(ex.getMessage(), ex);
        return getModelAndView(WebConstants.VIEW_NAME_ERROR_404, ex);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleIOException(Exception ex, HttpServletRequest request) {
        logger.error(ex.getMessage(), ex);
        return getModelAndView(WebConstants.VIEW_NAME_ERROR_500, ex);
    }

    protected String translate(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}

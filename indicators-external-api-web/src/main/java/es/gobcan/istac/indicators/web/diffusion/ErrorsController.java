package es.gobcan.istac.indicators.web.diffusion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorsController {

    @RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
    public String error(@PathVariable("code") String code) {
        if("404".equals(code)){
            return "errors/404";
        }
        return "errors/500";
    }

}

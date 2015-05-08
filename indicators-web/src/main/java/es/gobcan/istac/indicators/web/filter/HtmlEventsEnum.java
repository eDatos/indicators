package es.gobcan.istac.indicators.web.filter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum HtmlEventsEnum {
    // @formatter:off
    ONAFTERPRINT("ONAFTERPRINT"),
    ONBEFOREPRINT("ONBEFOREPRINT"),
    ONBEFOREUNLOAD("ONBEFOREUNLOAD"),
    ONERROR("ONERROR"),
    ONHASHCHANGE("ONHASHCHANGE"),
    ONLOAD("ONLOAD"),
    ONMESSAGE("ONMESSAGE"),
    ONOFFLINE("ONOFFLINE"),
    ONONLINE("ONONLINE"),
    ONPAGEHIDE("ONPAGEHIDE"),
    ONPAGESHOW("ONPAGESHOW"),
    ONPOPSTATE("ONPOPSTATE"),
    ONRESIZE("ONRESIZE"),
    ONSTORAGE("ONSTORAGE"),
    ONUNLOAD("ONUNLOAD"),
    ONBLUR("ONBLUR"),
    ONCHANGE("ONCHANGE"),
    ONCONTEXTMENU("ONCONTEXTMENU"),
    ONFOCUS("ONFOCUS"),
    ONINPUT("ONINPUT"),
    ONINVALID("ONINVALID"),
    ONRESET("ONRESET"),
    ONSEARCH("ONSEARCH"),
    ONSELECT("ONSELECT"),
    ONSUBMIT("ONSUBMIT"),
    ONKEYDOWN("ONKEYDOWN"),
    ONKEYPRESS("ONKEYPRESS"),
    ONKEYUP("ONKEYUP"),
    ONCLICK("ONCLICK"),
    ONDBLCLICK("ONDBLCLICK"),
    ONDRAG("ONDRAG"),
    ONDRAGEND("ONDRAGEND"),
    ONDRAGENTER("ONDRAGENTER"),
    ONDRAGLEAVE("ONDRAGLEAVE"),
    ONDRAGOVER("ONDRAGOVER"),
    ONDRAGSTART("ONDRAGSTART"),
    ONDROP("ONDROP"),
    ONMOUSEDOWN("ONMOUSEDOWN"),
    ONMOUSEMOVE("ONMOUSEMOVE"),
    ONMOUSEOUT("ONMOUSEOUT"),
    ONMOUSEOVER("ONMOUSEOVER"),
    ONMOUSEUP("ONMOUSEUP"),
    ONMOUSEWHEEL("ONMOUSEWHEEL"),
    ONSCROLL("ONSCROLL"),
    ONWHEEL("ONWHEEL"),
    ONCOPY("ONCOPY"),
    ONCUT("ONCUT"),
    ONPASTE("ONPASTE"),
    ONABORT("ONABORT"),
    ONCANPLAY("ONCANPLAY"),
    ONCANPLAYTHROUGH("ONCANPLAYTHROUGH"),
    ONCUECHANGE("ONCUECHANGE"),
    ONDURATIONCHANGE("ONDURATIONCHANGE"),
    ONEMPTIED("ONEMPTIED"),
    ONENDED("ONENDED"),
    ONLOADEDDATA("ONLOADEDDATA"),
    ONLOADEDMETADATA("ONLOADEDMETADATA"),
    ONLOADSTART("ONLOADSTART"),
    ONPAUSE("ONPAUSE"),
    ONPLAY("ONPLAY"),
    ONPLAYING("ONPLAYING"),
    ONPROGRESS("ONPROGRESS"),
    ONRATECHANGE("ONRATECHANGE"),
    ONSEEKED("ONSEEKED"),
    ONSEEKING("ONSEEKING"),
    ONSTALLED("ONSTALLED"),
    ONSUSPEND("ONSUSPEND"),
    ONTIMEUPDATE("ONTIMEUPDATE"),
    ONVOLUMECHANGE("ONVOLUMECHANGE"),
    ONWAITING("ONWAITING"),
    ONSHOW("ONSHOW"),
    ONTOGGLE("ONTOGGLE");

    // @formatter:on

    private String                                   event;

    private static final Map<String, HtmlEventsEnum> LOOKUP_MAP = new HashMap<String, HtmlEventsEnum>();
    private static final Set<String>                 CONTAINS   = new HashSet<String>();
    private static String                            REGEXP     = null;

    static {
        for (HtmlEventsEnum s : EnumSet.allOf(HtmlEventsEnum.class)) {
            String key = s.event.toLowerCase();
            LOOKUP_MAP.put(key, s);
            CONTAINS.add(key);
            if (REGEXP == null) {
                REGEXP = key;
            } else {
                REGEXP += "|" + key;
            }

        }
    }

    public static HtmlEventsEnum fromEventString(String campo) {
        return LOOKUP_MAP.get(campo.toLowerCase());
    }

    public static boolean containsEvent(String campo) {
        return CONTAINS.contains(campo.toLowerCase());
    }

    public static String getRegexp() {
        return REGEXP;
    }

    private HtmlEventsEnum(String event) {
        this.event = event;
    }

}

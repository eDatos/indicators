package es.gobcan.istac.indicators.web.diffusion.view;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbList {

    private List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();

    public BreadcrumbList(String... labels) {
        for (String label : labels) {
            this.addBreadcrumb(label);
        }
    }

    private void addBreadcrumb(String label) {
        addBreadcrumb(label, "");
    }

    public void addBreadcrumb(String label, String url) {
        breadcrumbs.add(new Breadcrumb(label, url));
    }

    public List<Breadcrumb> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(List<Breadcrumb> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public class Breadcrumb {

        private String label;
        private String url;

        public Breadcrumb(String label, String url) {
            super();
            this.label = label;
            this.url = url;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

}

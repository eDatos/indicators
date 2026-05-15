# Frontend Specialist — INDICATORS-INTERNAL-WEB

Expert module for GWT-based UI: indicator management, indicator systems, datasource configuration, and data visualization.

## Tech Stack

- **GWT**: 2.3.0
- **SmartGWT**: 3.0 (UI components)
- **GWTP**: 0.7 (MVP framework)
- **GIN**: 1.5.0 (dependency injection)
- **Spring Web**: 3.1.1.RELEASE
- **CAS Client**: 3.2.1
- **ICU4J**: 4.2.1

## Module Structure

```
indicators-internal-web/
├── src/main/java/.../web/
│   ├── client/
│   │   ├── presenter/    # MVP Presenters (GWTP)
│   │   ├── view/         # MVP Views
│   │   └── widgets/      # Reusable SmartGWT widgets
│   ├── server/
│   │   └── handlers/     # Server-side GWTP handlers
│   └── shared/
│       └── criteria/     # Shared search criteria
└── src/main/resources/
    └── IndicatorsWeb.gwt.xml
```

## GWT Restrictions (CRITICAL)

### Diamond operator NOT allowed

GWT 2.3.0 compiles with Java 1.6 compatibility. Diamond operator `<>` is **not supported** here (unlike indicators-core):

```java
// CORRECT
Map<String, String> map = new LinkedHashMap<String, String>();
List<IndicatorDto> list = new ArrayList<IndicatorDto>();

// INVALID — GWT compilation error
Map<String, String> map = new LinkedHashMap<>();
```

### No lambdas — use anonymous classes

```java
// CORRECT
saveButton.addClickHandler(new ClickHandler() {
    @Override
    public void onClick(ClickEvent event) {
        uiHandlers.saveIndicator(getFormValue());
    }
});

// INVALID
saveButton.addClickHandler(event -> uiHandlers.saveIndicator(getFormValue()));
```

### Async callbacks

```java
service.retrieveIndicator(code, new AsyncCallback<IndicatorDto>() {
    @Override
    public void onSuccess(IndicatorDto result) {
        getView().setIndicator(result);
    }

    @Override
    public void onFailure(Throwable caught) {
        ShowMessageEvent.fireErrorMessage(IndicatorPresenter.this, caught.getMessage());
    }
});
```

## GWT-specific i18n note

Single quotes must be escaped as `''` in GWT message files. This is especially relevant in Catalan:

```properties
# IndicatorsWebMessages_ca.properties
error.access=Error d''acc\u00e9s a la p\u00e0gina
```

## MVP Pattern (GWTP)

```java
public class IndicatorPresenter
        extends Presenter<IndicatorPresenter.IndicatorView, IndicatorPresenter.IndicatorProxy>
        implements IndicatorUiHandlers {

    public interface IndicatorView extends View, HasUiHandlers<IndicatorUiHandlers> {
        void setIndicator(IndicatorDto indicator);
        void showError(String message);
    }

    @ProxyStandard
    @NameToken(NameTokens.indicator)
    public interface IndicatorProxy extends ProxyPlace<IndicatorPresenter> {}

    @Inject
    private IndicatorServiceAsync indicatorService;

    @Override
    protected void onReveal() {
        super.onReveal();
        loadData();
    }

    @Override
    public void saveIndicator(IndicatorDto indicator) {
        indicatorService.saveIndicator(indicator, new AsyncCallback<IndicatorDto>() {
            @Override
            public void onSuccess(IndicatorDto result) {
                getView().setIndicator(result);
                ShowMessageEvent.fireSuccessMessage(IndicatorPresenter.this, "Saved");
            }
            @Override
            public void onFailure(Throwable caught) {
                getView().showError(caught.getMessage());
            }
        });
    }
}
```

## Goal

Intuitive and efficient interfaces for indicator management. Priorities:
1. **MVP** — clean Presenter/View separation
2. **Validation** — thorough client-side validation before server calls
3. **Feedback** — clear success/error messages for statistical operations

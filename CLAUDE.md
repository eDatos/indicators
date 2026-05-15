# Indicators — Shared Module Rules

These rules apply to all `indicators-*` modules.

## Code language

All code, functions, variables, constants, documentation and comments must be in **English**.
Variable names in camelCase.

## Constants and configuration values

Before defining a constant in code, check whether it already exists in the project configuration services. Search order:

1. **Common library** — `org.siemac.metamac.core.common.conf.ConfigurationService`
   Shared constants across all METAMAC projects: default language, internal API URLs, i18n config, etc.

2. **Project configuration class** — `es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService`
   Indicators-specific constants: default geographic codelists, own URLs, etc.

### Strict rule (non-test code)

If a constant is needed for a business or configuration concept (languages, locales, URLs, codelists, roles, etc.) and it is **not found in either class above**:

- **Do not hardcode it directly.**
- Add a standard unresolved-issue marker comment just above (keyword: `FIX` + `ME` concatenated, followed by a colon): `hardcoded constant — verify if it should come from ConfigurationService`.
- Explicitly warn the user before continuing.

## Java restrictions

- **NO** Java 8+ features: no lambdas, streams, method references, Optional, LocalDate/LocalDateTime
- Use **Joda Time** instead of java.time
- Use **Lombok** for DTOs and entities (@Data, @Getter, @Setter, @Builder)
- Use **Dozer** for object mapping (entity ↔ DTO)
- **JUnit 4** only (no JUnit 5)

## Do / Avoid

**Do:**
- Respect legacy library versions — do not update dependencies without consulting
- Follow existing package structure
- Use SLF4J for logging
- Configure via properties or XML as the module requires

**Avoid:**
- Breaking backward compatibility with existing REST APIs
- Ignoring Sculptor conventions in modules that use it

## SLF4J — 3+ parameters

This SLF4J version only supports up to 2 inline parameters. For 3+, wrap in `new Object[]{}`:

```java
LOG.info("msg {}", param);
LOG.info("msg {} {}", param1, param2);
LOG.info("msg {} {} {}", new Object[]{param1, param2, param3});
```

## i18n — .properties files

Always populate **three languages**: Catalan (`_ca`), Spanish (`_es`), English (`_en`). Default (`_default`) = same as English.

Always warn the user to review Catalan translations at https://www.softcatala.org/traductor/

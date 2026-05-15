# REST API Specialist — INDICATORS-REST

Expert module for the public REST API exposing published indicators, indicator systems, and their values.

## Tech Stack

- **Apache CXF**: 2.6.0 (JAX-RS server)
- **JAX-RS**: 1.1.1 — annotations: `@Path`, `@GET`, `@PathParam`, `@QueryParam`, `@Produces`
- **Jackson**: 1.9.4 (JSON serialization)
- **Enunciate**: 1.26 (API documentation)
- **Spring Test MVC**: 1.0.0.RELEASE (testing)

## Module Structure

```
indicators-rest-impl/
├── src/main/java/.../rest/
│   ├── v1_0/
│   │   ├── facadeimpl/   # JAX-RS facade implementations
│   │   ├── mapper/       # Domain -> REST type mappers (Do2TypeMapper, Do2JsonStatMapper)
│   │   └── util/         # REST utilities (criteria, exception)
│   └── exception/        # REST exception handling
└── src/test/java/        # REST tests
```

## JAX-RS Patterns (CRITICAL)

**Use JAX-RS annotations — NOT Spring MVC** (`@RestController`, `@GetMapping`, `@RequestParam` are wrong here).

```java
@Path("/v1.0/indicators")
public class IndicatorRestFacadeImpl implements IndicatorRestFacade {

    private static final Logger LOG = LoggerFactory.getLogger(IndicatorRestFacadeImpl.class);

    @Autowired
    private IndicatorsService indicatorsService;

    @Autowired
    private Do2TypeMapper do2TypeMapper;

    @GET
    @Path("/{indicatorCode}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public IndicatorType retrieveIndicator(
            @PathParam("indicatorCode") String indicatorCode,
            @QueryParam("lang") String lang) throws Exception {

        LOG.debug("Retrieving indicator: {}", indicatorCode);
        try {
            IndicatorVersion version = indicatorsService.retrieveIndicatorPublished(
                    ServiceContextHolder.getCurrentServiceContext(), indicatorCode);
            return do2TypeMapper.indicatorDoToType(version, lang);
        } catch (MetamacException e) {
            throw RestExceptionUtils.toRestException(e);
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public IndicatorsType findIndicators(
            @QueryParam("q") String query,
            @QueryParam("limit") Integer limit,
            @QueryParam("offset") Integer offset,
            @QueryParam("lang") String lang,
            @QueryParam("fields") String fields) throws Exception {

        try {
            MetamacCriteria criteria = RestCriteriaUtils.indicatorCriteriaFromQuery(query, limit, offset);
            MetamacCriteriaResult<IndicatorVersion> result =
                    indicatorsService.findIndicatorsPublished(
                            ServiceContextHolder.getCurrentServiceContext(), criteria);
            return do2TypeMapper.indicatorsDoToType(result, limit, offset, lang, fields);
        } catch (MetamacException e) {
            throw RestExceptionUtils.toRestException(e);
        }
    }
}
```

## Data endpoint (JSON / XML / JSON-stat)

```java
@GET
@Path("/{indicatorCode}/data")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
           IndicatorsRestConstants.MEDIA_TYPE_APPLICATION_JSON_STAT})
public DataType retrieveIndicatorData(
        @PathParam("indicatorCode") String indicatorCode,
        @QueryParam("representation") String representation,
        @QueryParam("granularity") String granularity,
        @QueryParam("lang") String lang,
        @QueryParam("fields") String fields) throws Exception {
    ...
}
```

## Mapper pattern

`Do2TypeMapperImpl` converts domain objects to REST types. `Do2JsonStatMapperUtil` handles JSON-stat format.

```java
public IndicatorType indicatorDoToType(IndicatorVersion indicatorVersion, String lang) {
    IndicatorType target = new IndicatorType();
    target.setId(indicatorVersion.getIndicator().getCode());
    target.setKind(IndicatorsRestConstants.KIND_INDICATOR);
    // ... map remaining fields
    return target;
}
```

## Exception handling

Translate `MetamacException` to HTTP responses:

```java
} catch (MetamacException e) {
    if (ServiceExceptionType.INDICATOR_NOT_FOUND.equals(
            e.getExceptionItems().get(0).getCode())) {
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    throw RestExceptionUtils.toRestException(e);
}
```

## Goal

Efficient and well-documented REST API for statistical indicators. Priorities:
1. **Correctness** — JAX-RS/CXF patterns only
2. **Performance** — optimized queries with dimension filters
3. **Compatibility** — maintain API backward compatibility (v1.0)
4. **Formats** — JSON, XML, JSON-stat support

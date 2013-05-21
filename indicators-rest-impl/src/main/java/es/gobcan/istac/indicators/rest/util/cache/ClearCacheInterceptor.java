package es.gobcan.istac.indicators.rest.util.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;

public class ClearCacheInterceptor implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(ClearCacheInterceptor.class);

    private CacheManager cacheManager;

    private List<String> cacheNames;

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCacheNames(List<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    private Collection<String> getCacheNames() {
        return cacheNames == null ? cacheManager.getCacheNames() : cacheNames;
    }

    private void clearCaches() {
        for(String cacheName : getCacheNames()) {
            logger.debug("clearing cache " + cacheName);
            Cache cache = cacheManager.getCache(cacheName);
            cache.clear();
        }
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        clearCaches();
        return methodInvocation.proceed();
    }

}

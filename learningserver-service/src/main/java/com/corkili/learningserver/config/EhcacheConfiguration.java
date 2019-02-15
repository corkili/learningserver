package com.corkili.learningserver.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

@org.springframework.context.annotation.Configuration
@EnableCaching
public class EhcacheConfiguration implements CachingConfigurer {

    private static final String[] CACHE_NAMES = ("course,courseCategory,courseComment,courseSubscription,courseWork," +
            "exam,examQuestion,forumTopic,message,question,scorm,submittedCourseWork,submittedExam,topicComment," +
            "topicReply,user,workQuestion").split(",");

    @Bean
    @Override
    public CacheManager cacheManager() {
        Configuration config = new Configuration();
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("memoryCache");
        cacheConfiguration.setMaxEntriesLocalHeap(500);
        cacheConfiguration.setMaxEntriesLocalDisk(0);
        cacheConfiguration.setMemoryStoreEvictionPolicyFromObject(MemoryStoreEvictionPolicy.LRU);
        cacheConfiguration.setEternal(false);
        cacheConfiguration.setOverflowToOffHeap(false);
        cacheConfiguration.addPersistence(new PersistenceConfiguration().strategy(Strategy.NONE));
        cacheConfiguration.setTimeToIdleSeconds(300);
        cacheConfiguration.setTimeToLiveSeconds(600);
        config.addCache(cacheConfiguration);
        return new EhCacheCacheManager(net.sf.ehcache.CacheManager.newInstance(config));
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }
}

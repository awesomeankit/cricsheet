package com.cricket.cricsheet.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.cricket.cricsheet.common.CricContants;
import com.google.common.cache.CacheBuilder;

import io.micrometer.core.instrument.binder.cache.GuavaCacheMetrics;

@Configuration
@EnableCaching
public class SpringConfig implements CachingConfigurer {
	
	@Value("${CACHE_NAME_GET_MATCHES_BY_TEAM_SIZE:5}")
	private int cache_name_get_matches_by_team_size;
	
	@Value("${CACHE_NAME_GET_MATCHES_BY_TEAM_TIME:5}")
	private int cache_name_get_matches_by_team_time;
	
	@Value("${CACHE_NAME_GET_MATCHES_BY_TEAM_OPPONENT_SIZE:5}")
	private int cache_name_get_matches_by_team_opponent_size;
	
	@Value("${CACHE_NAME_GET_MATCHES_BY_TEAM_OPPONENT_TIME:5}")
	private int cache_name_get_matches_by_team_opponent_time;
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	     return new MethodValidationPostProcessor();
	}
	
	@Bean
	@Override
	public CacheManager cacheManager() {
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
		GuavaCache cache1 = new GuavaCache(CricContants.CACHE_NAME_GET_MATCHES_BY_TEAM,
				CacheBuilder.newBuilder().maximumSize(cache_name_get_matches_by_team_size)
						.expireAfterWrite(cache_name_get_matches_by_team_time, TimeUnit.MINUTES).build());
		GuavaCache cache2 = new GuavaCache(CricContants.CACHE_NAME_GET_MATCHES_BY_TEAM_OPPONENT,
				CacheBuilder.newBuilder().maximumSize(cache_name_get_matches_by_team_opponent_size)
						.expireAfterWrite(cache_name_get_matches_by_team_opponent_time, TimeUnit.MINUTES).build());

		simpleCacheManager.setCaches(Arrays.asList(cache1, cache2));

		return simpleCacheManager;
	}

	@Override
	public CacheResolver cacheResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyGenerator keyGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheErrorHandler errorHandler() {
		// TODO Auto-generated method stub
		return null;
	}
}

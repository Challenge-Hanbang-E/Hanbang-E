package com.hanbang.e.common.config;

import static com.hanbang.e.common.config.JpaConfig.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {MEMBER_DOMAIN_PACKAGE, ORDER_DOMAIN_PACKAGE, PRODUCT_DOMAIN_PACKAGE})
public class JpaConfig {

	static final String MEMBER_DOMAIN_PACKAGE = "com.hanbang.e.member";
	static final String ORDER_DOMAIN_PACKAGE = "com.hanbang.e.order";
	static final String PRODUCT_DOMAIN_PACKAGE = "com.hanbang.e.product";

}

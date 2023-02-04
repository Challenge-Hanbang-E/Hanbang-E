package com.hanbang.e.productes.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.hanbang.e.productes.repository.ProductDocRepository;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = ProductDocRepository.class)
public class ElasticSearchConfig extends ElasticsearchConfigurationSupport {

	@Value("${elasticsearch.host}")
	private String elasticsearchUrl;

	@Bean
	public RestHighLevelClient elasticsearchClient() {
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
			.connectedTo(elasticsearchUrl)
			.build();
		return RestClients.create(clientConfiguration).rest();
	}

}

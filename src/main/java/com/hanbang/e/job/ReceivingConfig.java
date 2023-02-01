package com.hanbang.e.job;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hanbang.e.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ReceivingConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job ReceivingJob() {
		return jobBuilderFactory.get("ReceivingJob")
			.incrementer(new RunIdIncrementer())
			.start(ReceivingStep())
			.build();
	}

	@JobScope
	@Bean
	public Step ReceivingStep() {
		return stepBuilderFactory.get("ReceivingStep")
			.<Product, Product>chunk(100)		//Product데이터를 읽어와서 product데이터 사용 100개 단위로 데이터 처
			.reader(ProductReader())
			.processor(ProductProcessor())
			.writer(Productwriter())
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Product> ProductReader() {		//재고량이 0인 데이터 읽기

		JpaPagingItemReader<Product> reader = new JpaPagingItemReader<Product>() {
			@Override
			public int getPage() {
				return 0;
			}
		};

		reader.setQueryString("SELECT p FROM Product p WHERE p.stock = 0");
		reader.setPageSize(100);
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setName("ProductReader");

		return reader;
	}

	@Bean
	@StepScope
	public ItemProcessor<Product, Product> ProductProcessor() {		//restock메소드 사용하여 재입고 처리
		return item -> {
			item.restock();
			return item;
		};
	}

	@Bean
	@StepScope
	public JpaItemWriter<Product> Productwriter() {
		JpaItemWriter<Product> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}


}

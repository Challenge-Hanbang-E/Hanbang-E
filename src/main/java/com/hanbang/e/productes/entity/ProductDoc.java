package com.hanbang.e.productes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(indexName = "product")
public class ProductDoc {

	@Id
	private String id;

	@Field(name = "product_id")
	private Long productId;

	@Field(name = "name")
	private String productName;

	@Field(name = "price")
	private Long price;

	@Field(name = "img")
	private String img;

	@Field(name = "stock")
	private int stock;

	@Field(name = "sales")
	private int sales;

	@Field(name = "on_sale")
	private Boolean onSale;

}

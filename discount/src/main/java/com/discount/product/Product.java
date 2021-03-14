package com.discount.product;

import java.math.BigDecimal;
import java.math.MathContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Product {

	private BigDecimal price;

	private BigDecimal productDiscount;

	/**
	 * Constructor that takes in the {@link BigDecimal price} of the product.
	 *
	 * @param {@link BigDecimal} price of the product.
	 */
	public Product(String price) {
		validateInput(price);
		this.price = new BigDecimal(price, MathContext.DECIMAL32);
	}

	/**
	 * Not used.
	 */
	@SuppressWarnings("unused")
	private Product() {
	};

	public BigDecimal getPrice() {
		return this.price;
	}

	public BigDecimal getProductDiscount() {
		return this.productDiscount;
	}

	public void setProductDiscount(BigDecimal productDiscount) {
		this.productDiscount = productDiscount;
	}

	private void validateInput(String price) {
		LOGGER.debug("Validating the input");

		if (price == null || price.trim().length() == 0)
			throw new IllegalArgumentException("The price value is invalid");

		BigDecimal tempPrice;
		try {
			tempPrice = new BigDecimal(price, MathContext.DECIMAL32);
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException("The literal price value must be parsable to a number");
		}

		if (tempPrice.compareTo(BigDecimal.ZERO) != 1)
			throw new IllegalArgumentException("The price value must be a positive value");

		LOGGER.debug("Validation passed");
	}

	private static final Logger LOGGER = LogManager.getLogger(Product.class);
}

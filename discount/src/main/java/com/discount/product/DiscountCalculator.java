package com.discount.product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiscountCalculator {

	private final BigDecimal totalDiscount;

	private BigDecimal totalPrice;

	private final List<Product> products;

	/**
	 * Constructor which takes in the {@link List} of {@link Product products} and
	 * the {@code String} totalDiscount of the products.
	 *
	 * @param products      {@link List} of {@link Product products}.
	 * @param totalDiscount The value of the discount represented as a
	 *                      {@code String}.
	 */
	public DiscountCalculator(List<Product> products, String totalDiscount) {
		validateInput(products, totalDiscount);

		this.products = products;
		this.totalDiscount = new BigDecimal(totalDiscount, MathContext.DECIMAL32);
	}

	/**
	 * Calculates the discount of the each of the {@link Product Products}.
	 * Initially calculates the total price of all products, which is later used to
	 * calculate the discount for each of them.
	 *
	 * Individual products discount is dependent on the given product price and the
	 * total price of all products.
	 *
	 */
	public void calculateProductsDiscounts() {
		this.totalPrice = getTotalPrice();
		LOGGER.debug("Set the total price to: {}", this.totalPrice);

		calculateDiscountForEachProduct();
	}

	private BigDecimal getTotalPrice() {
		return this.products.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private void calculateDiscountForEachProduct() {
		calculateDiscount();
		validateCalculatedDiscount();
	}

	private void calculateDiscount() {
		final BigDecimal factor = this.totalDiscount.divide(this.totalPrice, MathContext.DECIMAL32);
		LOGGER.info("Factor for given total discount: {} and total price: {} is {}", this.totalDiscount,
				this.totalPrice, factor);

		this.products.forEach(product -> product.setProductDiscount(product.getPrice().multiply(factor)));
	}

	private void validateCalculatedDiscount() {
		BigDecimal calculatedSum = this.products.stream().map(Product::getProductDiscount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		if (!calculatedSum.equals(this.totalDiscount)) {
			LOGGER.info("Applied discount ({}) does not match the total discount ({}), correcting for the last product",
					calculatedSum, this.totalDiscount);
			correctLastProductDiscountValue(calculatedSum);
		}
	}

	private void correctLastProductDiscountValue(BigDecimal calculatedSum) {
		BigDecimal difference = this.totalDiscount.subtract(calculatedSum);
		LOGGER.info("Applying the difference ({}) to the last product", difference);

		Product lastProduct = this.products.get(this.products.size() - 1);
		lastProduct.setProductDiscount(lastProduct.getProductDiscount().add(difference));
	}

	private void validateInput(List<Product> products, String totalDiscount) {
		LOGGER.debug("Validating the input");

		if (null == products || products.isEmpty())
			throw new IllegalArgumentException("The list of products must contain at least one element");

		if (products.size() > MAX_NUMBER_OF_PRODUCTS)
			throw new IllegalArgumentException("The list of products must contain at most five elements");

		if (totalDiscount == null || totalDiscount.trim().length() == 0)
			throw new IllegalArgumentException("The total discount value is invalid");

		BigDecimal tempDiscount;
		try {
			tempDiscount = new BigDecimal(totalDiscount, MathContext.DECIMAL32);
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException("The literal discount value must be parsable to a number");
		}

		if (tempDiscount.compareTo(BigDecimal.ZERO) != 1)
			throw new IllegalArgumentException("The total discount value must be a positive value");

		LOGGER.debug("Validation passed");
	}

	private static final int MAX_NUMBER_OF_PRODUCTS = 5;

	private static final Logger LOGGER = LogManager.getLogger(DiscountCalculator.class);

}

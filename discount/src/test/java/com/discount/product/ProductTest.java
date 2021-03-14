package com.discount.product;

import org.junit.Test;

public class ProductTest {

	@Test(expected = Test.None.class)
	public void validateInputShouldNotThrowIfThePricesPositive() throws Exception {
		new Product("10");
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateInputShouldThrowIllegalArgumentExceptionIfPriceIsZero2() throws Exception {
		new Product("0.0000");
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateInputShouldThrowIllegalArgumentExceptionIfPriceIsNotANumber() throws Exception {
		new Product("not a number");
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateInputShouldThrowIllegalArgumentExceptionIfPriceIsWhitespaceOnly() throws Exception {
		new Product("   ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateInputShouldThrowIllegalArgumentExceptionIfPriceIsNegative() throws Exception {
		new Product("-123");
	}

}

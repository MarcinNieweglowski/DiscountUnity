package com.discount.product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

public class DiscountCalculatorTest {

	private DiscountCalculator calculator;

	@Test
	public void getTotalPriceShouldReturnTheTotalPriceOfAllProducts() throws Exception {
		Product productMock = Mockito.mock(Product.class);
		Product productMock2 = Mockito.mock(Product.class);
		List<Product> products = Arrays.asList(productMock, productMock2);

		Mockito.when(productMock.getPrice()).thenReturn(new BigDecimal(500));
		Mockito.when(productMock2.getPrice()).thenReturn(new BigDecimal(1500));

		this.calculator = new DiscountCalculator(products, ONE);

		BigDecimal result = (BigDecimal) Whitebox.invokeMethod(this.calculator, "getTotalPrice");
		Assert.assertEquals("Expected the total price to be equal", new BigDecimal(2000), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void discountCalculatorConstructorShouldNotAcceptNullAsTheListOfProducts() throws Exception {
		this.calculator = new DiscountCalculator(null, ONE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void discountCalculatorConstructorShouldNotAcceptAnEmptyListOfProducts() throws Exception {
		this.calculator = new DiscountCalculator(Collections.emptyList(), ONE);
	}

	@Test(expected = IllegalArgumentException.class)
	@SuppressWarnings("unchecked")
	public void discountCalculatorConstructorShouldNotAcceptAListWithMoreThan5Products() throws Exception {
		List<Product> products = Mockito.mock(List.class);
		Mockito.when(products.size()).thenReturn(999);
		this.calculator = new DiscountCalculator(products, ONE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void discountCalculatorConstructorShouldNotAcceptAWhitespaceStringRepresentationOfDiscount()
			throws Exception {
		this.calculator = new DiscountCalculator(Arrays.asList(Mockito.mock(Product.class)), "  ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void discountCalculatorConstructorShouldNotAcceptAStringRepresentationOfDiscountThatIsNotANumber()
			throws Exception {
		this.calculator = new DiscountCalculator(Arrays.asList(Mockito.mock(Product.class)), "something");
	}

	@Test(expected = IllegalArgumentException.class)
	public void discountCalculatorConstructorShouldNotAcceptADiscountWithNegativeValue() throws Exception {
		this.calculator = new DiscountCalculator(Arrays.asList(Mockito.mock(Product.class)), "-123");
	}

	@Test(expected = IllegalArgumentException.class)
	public void discountCalculatorConstructorShouldNotAcceptADiscountWithZeroValue() throws Exception {
		this.calculator = new DiscountCalculator(Arrays.asList(Mockito.mock(Product.class)), "-0.0");
	}

	@Test
	public void calculateDiscountShouldSetTheDiscountPriceForAllProducts() throws Exception {
		Product product = new Product("500");
		Product product2 = new Product("1500");
		List<Product> products = Arrays.asList(product, product2);

		this.calculator = new DiscountCalculator(products, TEN);
		Whitebox.setInternalState(this.calculator, "totalPrice", new BigDecimal(2000));

		Whitebox.invokeMethod(this.calculator, "calculateDiscount");

		Assert.assertEquals("Expected the discount to be equal", 0,
				products.get(0).getProductDiscount().compareTo(new BigDecimal(2.5)));
		Assert.assertEquals("Expected the discount to be equal", 0,
				products.get(1).getProductDiscount().compareTo(new BigDecimal(7.5)));
	}

	@Test
	public void validateCalculatedDiscountShouldCorrectTheDiscountIfTheSumDoesNotEqualTheTotalDiscount()
			throws Exception {
		Product productMock = Mockito.mock(Product.class);
		Product productMock2 = Mockito.mock(Product.class);
		List<Product> products = Arrays.asList(productMock, productMock2);

		Mockito.when(productMock.getProductDiscount()).thenReturn(BigDecimal.ONE);
		Mockito.when(productMock2.getProductDiscount()).thenReturn(new BigDecimal(8));

		this.calculator = new DiscountCalculator(products, TEN);

		Whitebox.invokeMethod(this.calculator, "validateCalculatedDiscount");

		Mockito.verify(productMock, Mockito.never()).setProductDiscount(ArgumentMatchers.any(BigDecimal.class));
		Mockito.verify(productMock2, Mockito.times(1)).setProductDiscount(ArgumentMatchers.any(BigDecimal.class));
	}

	@Test
	public void validateCalculatedDiscountShouldNotCorrectTheDiscountIfTheSumEqualsTheTotalDiscount() throws Exception {
		Product productMock = Mockito.mock(Product.class);
		Product productMock2 = Mockito.mock(Product.class);
		List<Product> products = Arrays.asList(productMock, productMock2);

		Mockito.when(productMock.getProductDiscount()).thenReturn(BigDecimal.ONE);
		Mockito.when(productMock2.getProductDiscount()).thenReturn(new BigDecimal(9));

		this.calculator = new DiscountCalculator(products, TEN);

		Whitebox.invokeMethod(this.calculator, "validateCalculatedDiscount");

		Mockito.verify(productMock, Mockito.never()).setProductDiscount(ArgumentMatchers.any(BigDecimal.class));
		Mockito.verify(productMock2, Mockito.never()).setProductDiscount(ArgumentMatchers.any(BigDecimal.class));
	}

	@Test
	public void correctLastProductDiscountValueShouldUpdateTheLastProductsDiscountValue() throws Exception {
		Product productMock = Mockito.mock(Product.class);
		Mockito.when(productMock.getProductDiscount()).thenReturn(BigDecimal.TEN);

		this.calculator = new DiscountCalculator(Arrays.asList(productMock), TEN);

		Whitebox.invokeMethod(this.calculator, "correctLastProductDiscountValue", BigDecimal.ONE);

		Mockito.verify(productMock, Mockito.times(1)).setProductDiscount(ArgumentMatchers.eq(new BigDecimal(19)));
	}

	private static final String ONE = "1";

	private static final String TEN = "10";

}

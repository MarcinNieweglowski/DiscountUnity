package com.discount.App;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.discount.product.Product;

public class MainAppTest {

	@Test
	public void constructingAMainAppObjectShouldSetTheDiscountOnEachOfTheProductsAccordingToProductsAndTotalDiscount()
			throws Exception {
		Product product = new Product("200");
		Product product2 = new Product("600");
		String totalDiscount = "200";

		Whitebox.invokeConstructor(MainApp.class, totalDiscount, Arrays.asList(product, product2));

		Assert.assertEquals("Expected the discount on first product to be equal", 0,
				product.getProductDiscount().compareTo(new BigDecimal(50)));
		Assert.assertEquals("Expected the discount on second product to be equal", 0,
				product2.getProductDiscount().compareTo(new BigDecimal(150)));

		Product product3 = new Product("200");
		Whitebox.invokeConstructor(MainApp.class, totalDiscount, Arrays.asList(product, product2, product3));

		Assert.assertEquals("Expected the discount on first product to be equal", 0,
				product.getProductDiscount().compareTo(new BigDecimal(40)));
		Assert.assertEquals("Expected the discount on second product to be equal", 0,
				product2.getProductDiscount().compareTo(new BigDecimal(120)));
		Assert.assertEquals("Expected the discount on third product to be equal", 0,
				product3.getProductDiscount().compareTo(new BigDecimal(40)));
	}

}

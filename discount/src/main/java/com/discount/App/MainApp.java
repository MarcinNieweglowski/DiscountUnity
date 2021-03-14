package com.discount.App;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.discount.product.DiscountCalculator;
import com.discount.product.Product;

public class MainApp {

	public MainApp(String totalDiscount, List<Product> products) {
		LOGGER.info("Set the total discount to: {} for {} products", totalDiscount, products.size());
		calculate(products, totalDiscount);

		products.forEach(product -> LOGGER.info("Set the discount to {} for product initial price: {} ",
				product.getProductDiscount(), product.getPrice()));
	}

	public static void main(String[] args) {
		String totalDiscount = "100";
		List<Product> products = generateProductList();

		new MainApp(totalDiscount, products);
	}

	private void calculate(List<Product> products, String totalDiscount) {
		DiscountCalculator calculator = new DiscountCalculator(products, totalDiscount);

		calculator.calculateProductsDiscounts();
	}

	private static List<Product> generateProductList() {
		Product product1 = new Product("500");
		Product product2 = new Product("600");
		Product product3 = new Product("1000");

		return Arrays.asList(product1, product2, product3);
	}

	private static final Logger LOGGER = LogManager.getLogger(MainApp.class);

}

package com.farukgenc.boilerplate.springboot.utils;

import java.util.Locale;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
public final class ProjectConstants {

	// FIXME : Customize project constants for your application.

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final Locale SPANISH_LOCALE = new Locale.Builder().setLanguage("es").setRegion("CO").build();

	private ProjectConstants() {

		throw new UnsupportedOperationException();
	}

}

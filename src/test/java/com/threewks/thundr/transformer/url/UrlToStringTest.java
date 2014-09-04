/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.transformer.url;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class UrlToStringTest {
	private UrlToString transformer = new UrlToString();

	@Test
	public void shouldTransformNullToNull() {
		assertThat(transformer.from(null), is(nullValue()));
	}

	@Test
	public void shouldTransform() throws MalformedURLException {
		assertThat(transformer.from(new URL("http://www.google.com/")), is("http://www.google.com/"));
		assertThat(transformer.from(new URL("http://www.google.com")), is("http://www.google.com"));
		assertThat(transformer.from(new URL("https://www.google.com")), is("https://www.google.com"));
		assertThat(transformer.from(new URL("ftp://www.google.com/ftp/")), is("ftp://www.google.com/ftp/"));
		assertThat(transformer.from(new URL("http://www.google.com/?query=parameter")), is("http://www.google.com/?query=parameter"));
	}
}
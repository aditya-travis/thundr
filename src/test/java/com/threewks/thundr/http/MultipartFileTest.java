/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://3wks.github.io/thundr/
 * Copyright (C) 2014 3wks, <thundr@3wks.com.au>
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
package com.threewks.thundr.http;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MultipartFileTest {
	@Test
	public void shouldRetainValuesOnConstruction() {
		MultipartFile file = new MultipartFile("name", new byte[] { 1, 2, 3 }, "content-type");
		assertThat(file.getName(), is("name"));
		assertThat(file.getData(), is(new byte[] { 1, 2, 3 }));
		assertThat(file.getContentType(), is("content-type"));
	}
}

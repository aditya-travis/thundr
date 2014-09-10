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
package com.threewks.thundr.view;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.http.RequestThreadLocal;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.string.StringView;
import com.threewks.thundr.view.string.StringViewResolver;

public class BasicViewRendererTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	private ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
	private BasicViewRenderer renderer = new BasicViewRenderer(viewResolverRegistry);

	@Before
	public void before() {
		viewResolverRegistry.addResolver(StringView.class, new StringViewResolver());
		RequestThreadLocal.clear();
	}

	@After
	public void after() {
		RequestThreadLocal.clear();
	}

	@Test
	public void shouldRenderAViewMakingOutputAndHeadersAvailable() throws UnsupportedEncodingException {
		renderer.render(new StringView("contents").withHeader("header", "value"));
		assertThat(renderer.getOutputAsString(), is("contents"));
		assertThat(renderer.getOutputAsBytes(), is("contents".getBytes("UTF-8")));
		assertThat(renderer.getContentType(), is("text/plain"));
		assertThat(renderer.getCharacterEncoding(), is("UTF-8"));
		assertThat(renderer.getHeader("header"), is("value"));
		assertThat(renderer.getHeader("HEADER"), is("value"));
	}

	@Test
	public void shouldThrowViewResolutionExceptionIfNoViewResolver() {
		thrown.expect(ViewResolverNotFoundException.class);
		thrown.expectMessage("No ViewResolver is registered for the view result String - ");

		renderer.render("no");
	}

	@Test
	public void shouldThrowViewResolutionExceptionWhenTryingToReuseABasicViewRenderer() {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("This BasicViewRenderer has already been used to render a view, it cannot be reused. Create a new one");

		renderer.render(new StringView("contents"));
		renderer.render(new StringView("contents"));
	}

	@Test
	public void shouldUseRequestInThreadLocalIfPresentRestoringInitialState() {
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse resp = new MockHttpServletResponse();

		req.setAttribute("existing", "value");
		RequestThreadLocal.set(req, resp);

		viewResolverRegistry.addResolver(String.class, new ViewResolver<String>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, String viewResult) {
				assertThat(req.getAttribute("existing"), is((Object) "value"));
				req.setAttribute("existing", "overwritten");
				assertThat(req.getAttribute("existing"), is((Object) "overwritten"));
				try {
					resp.getWriter().write(viewResult);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		renderer.render("Body");
		assertThat(renderer.getOutputAsString(), is("Body"));
		assertThat(req.getAttribute("existing"), is((Object) "value"));
	}

	@Test
	public void shouldUseRequestInThreadLocalIfPresentRestoringInitialStateEvenOnException() {
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse resp = new MockHttpServletResponse();

		req.setAttribute("existing", "value");
		RequestThreadLocal.set(req, resp);

		viewResolverRegistry.addResolver(String.class, new ViewResolver<String>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, String viewResult) {
				assertThat(req.getAttribute("existing"), is((Object) "value"));
				req.setAttribute("existing", "overwritten");
				assertThat(req.getAttribute("existing"), is((Object) "overwritten"));
				throw new RuntimeException("intentional");
			}
		});
		try {
			renderer.render("Body");
			fail("Expected an exception");
		} catch (RuntimeException e) {
			assertThat(req.getAttribute("existing"), is((Object) "value"));
		}
	}
}

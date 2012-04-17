package com.atomicleopard.webFramework.view;

import javax.servlet.ServletContext;

import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.route.RouteException;
import com.atomicleopard.webFramework.view.exception.ExceptionViewResolver;
import com.atomicleopard.webFramework.view.exception.RouteNotFoundViewResolver;
import com.atomicleopard.webFramework.view.json.JsonView;
import com.atomicleopard.webFramework.view.json.JsonViewResolver;
import com.atomicleopard.webFramework.view.jsp.JspView;
import com.atomicleopard.webFramework.view.jsp.JspViewResolver;
import com.atomicleopard.webFramework.view.redirect.RedirectView;
import com.atomicleopard.webFramework.view.redirect.RedirectViewResolver;

public class ViewResolverInjectionConfiguration implements InjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
		injectionContext.inject(ViewResolverRegistry.class).as(viewResolverRegistry);
		addViewResolvers(viewResolverRegistry, injectionContext);
	}

	protected void addViewResolvers(ViewResolverRegistry viewResolverRegistry, UpdatableInjectionContext injectionContext) {
		ServletContext servletContext = injectionContext.get(ServletContext.class);
		viewResolverRegistry.addResolver(Throwable.class, new ExceptionViewResolver());
		viewResolverRegistry.addResolver(RouteException.class, new RouteNotFoundViewResolver());
		viewResolverRegistry.addResolver(RedirectView.class, new RedirectViewResolver());
		viewResolverRegistry.addResolver(JsonView.class, new JsonViewResolver());
		viewResolverRegistry.addResolver(JspView.class, new JspViewResolver());
	}
}
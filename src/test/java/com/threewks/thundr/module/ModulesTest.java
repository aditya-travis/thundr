package com.threewks.thundr.module;

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.InjectionContextImpl;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class ModulesTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Modules modules = new Modules();
	private Module testModule1 = Module.from(new TestModule1());
	private Module testModule2 = Module.from(new TestModule2());
	private Module testModule3 = Module.from(new TestModule3());
	private UpdatableInjectionContext injectionContext = new InjectionContextImpl();

	@Before
	public void before() {
		injectionContext.inject(modules).as(Modules.class);
	}

	@Test
	public void shouldRetainAddedModules() {
		modules.addModule(testModule1);
		modules.addModule(testModule3);
		List<Module> expectedModules = list(testModule1, testModule3);
		assertThat(modules.listModules(), is(expectedModules));
	}

	@Test
	public void shouldLoadAddedModules() {
		modules.addModule(testModule1);
		modules.loadModules(injectionContext);
		assertThat(injectionContext.get(String.class, "TestModule1"), is("Invoked"));
	}

	@Test
	public void shouldPermitAddingOfModulesWhileModulesLoading() {
		modules.addModule(testModule1);
		modules.addModule(testModule2);
		modules.loadModules(injectionContext);
		assertThat(injectionContext.get(String.class, "TestModule1"), is("Invoked"));
		assertThat(injectionContext.get(String.class, "TestModule2"), is("Invoked"));
		assertThat(injectionContext.get(String.class, "TestModule3"), is("Invoked"));
	}

	@Test
	public void shouldDetectCircularModuleLoading() {
		thrown.expect(ModuleLoadingException.class);
		thrown.expectMessage("exceeded the maximum number of allowed modules");
		modules.addModule(Module.from(new TestModule4()));
		modules.loadModules(injectionContext);
	}

	private class TestModule1 implements InjectionConfiguration {
		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			injectionContext.inject("Invoked").named("TestModule1").as(String.class);
		}
	}

	private class TestModule2 implements InjectionConfiguration {
		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			injectionContext.get(Modules.class).addModule(testModule3);
			injectionContext.inject("Invoked").named("TestModule2").as(String.class);
		}
	}

	private class TestModule3 implements InjectionConfiguration {
		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			injectionContext.inject("Invoked").named("TestModule3").as(String.class);
		}
	}

	private class TestModule4 implements InjectionConfiguration {
		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			Modules modules = injectionContext.get(Modules.class);
			modules.addModule(Module.from(new TestModule4()));
		}
	}
}

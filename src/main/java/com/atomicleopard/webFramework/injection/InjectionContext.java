package com.atomicleopard.webFramework.injection;

public interface InjectionContext {
	public <T> T get(Class<T> type, String name) throws NullPointerException;

	public <T> T get(Class<T> type) throws NullPointerException;

	public <T> boolean contains(Class<T> type);

	public <T> boolean contains(Class<T> type, String name);
}
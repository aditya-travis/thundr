package com.atomicleopard.webFramework.configuration;

import com.atomicleopard.webFramework.exception.BaseException;

public class ConfigurationException extends BaseException {
	private static final long serialVersionUID = -4468807352551274648L;

	public ConfigurationException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public ConfigurationException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}

}

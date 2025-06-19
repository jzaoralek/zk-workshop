package com.ness.zkworkshop.web.util;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import jakarta.servlet.http.HttpServletRequest;

public final class WebUtils {

	private WebUtils() {
	};

	/**
	 * Metoda pro ziskani lokalizovane reprezentace polozky enumu z resource bundle
	 * 
	 * @param e
	 * @return
	 */
	public static String getMessageItemFromEnum(Enum<?> e) {
		return Labels.getLabel("enum." + e.getDeclaringClass().getSimpleName() + "." + e.name());
	}

	public static String getRequestParameter(String atr) {
		HttpServletRequest req = getHttpServletRequest();
		if (req == null) {
			return null;
		}
		return req.getParameter(atr);
	}

	public static HttpServletRequest getHttpServletRequest() {
		Execution exec = Executions.getCurrent();
		if (exec == null) {
			return null;
		}
		return (HttpServletRequest) exec.getNativeRequest();
	}

	/**
	 * Vraci uri na aktualni stranku vcetne parametru requestu.
	 * 
	 * @return
	 */
	public static String getRequestPathWithParams() {
		StringBuilder sb = new StringBuilder(getRequestPath());
		String params = getHttpServletRequest().getQueryString();
		if (params != null && !"".equals(params)) {
			sb.append("?");
			sb.append(params);
		}

		return sb.toString();
	}

	/**
	 * Vraci uri na aktualni stranku.
	 * 
	 * @return
	 */
	public static String getRequestPath() {
		return Executions.getCurrent().getDesktop().getRequestPath();
	}

	public static void setRequestAtribute(String atr, Object obj) {
		HttpServletRequest req = getHttpServletRequest();
		if (req == null) {
			return;
		}
		req.setAttribute(atr, obj);
	}

	public static <T> T getRequestAtribute(String atr) {
		HttpServletRequest req = getHttpServletRequest();
		if (req == null) {
			return null;
		}
		return (T) req.getAttribute(atr);
	}
}

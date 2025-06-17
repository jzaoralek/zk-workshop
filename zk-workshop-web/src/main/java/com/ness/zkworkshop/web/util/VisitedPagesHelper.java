package com.ness.zkworkshop.web.util;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

/**
 * Akce pouzite pro obecne tlacitko Zpet a breadcrumbs.
 * pozn. neni jako spring bean, protoze potreba pouzit v Initiator tride, kde je problem s nactenim bean z aplikacniho contextu.
 *
 */
public final class VisitedPagesHelper {

	public static final String PAGE_VISITED_ATTR = "pageVisited";

	private VisitedPagesHelper(){}

	/**
	 * Zaregistrovani navstivene stranky.
	 */
	public static void registerVisitedPage() {
		// registrovat jen GET requests
		if (WebUtils.getHttpServletRequest() == null || !"GET".equals(WebUtils.getHttpServletRequest().getMethod())) {
			return;
		}
    	String requestPath = WebUtils.getRequestPathWithParams();
		if (!requestPath.equals(getLastVisitedPage())) {
			storeHolderToSession(requestPath);
		}
	}

	public static String getLastVisitedPage() {
		Object lastVisitedObj = Executions.getCurrent().getSession().getAttribute(PAGE_VISITED_ATTR);
		if (lastVisitedObj != null) {
			return (String)lastVisitedObj;
		}
		return null;
	}

	private static void storeHolderToSession(String lasVisited) {
		Executions.getCurrent().getSession().setAttribute(PAGE_VISITED_ATTR, lasVisited);
	}

	private static void storeHolderToDesktop(String lasVisited) {
		if (Executions.getCurrent() == null) {
			return;
		}
		Executions.getCurrent().getDesktop().setAttribute(PAGE_VISITED_ATTR, lasVisited);
	}
}
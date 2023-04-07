package com.ness.zkworkshop.web.interceptor;

import com.ness.zkworkshop.web.util.VisitedPagesHelper;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.util.DesktopInit;

/**
 * Interceptor volany v ramci v ramci vytvoreni desktop scope.
 *
 */
public class SdatDesktopInit implements DesktopInit {

	@Override
	public void init(Desktop arg0, Object arg1) throws Exception {
		// zaregistrovani navstivene stranky, preneseno z SdatInitiator.doAfterCompose()
    	VisitedPagesHelper.registerVisitedPage();
	}
}

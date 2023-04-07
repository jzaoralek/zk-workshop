package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.util.VisitedPagesHelper;
import com.ness.zkworkshop.web.util.WebUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LastVisitedPageVM extends BaseVM {

    public String getPageLastVisited() {
        return VisitedPagesHelper.getLastVisitedPage();
    }
}

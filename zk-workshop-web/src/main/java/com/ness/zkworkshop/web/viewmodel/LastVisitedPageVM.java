package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.util.VisitedPagesHelper;

public class LastVisitedPageVM extends BaseVM {

    public String getPageLastVisited() {
        return VisitedPagesHelper.getLastVisitedPage();
    }
}

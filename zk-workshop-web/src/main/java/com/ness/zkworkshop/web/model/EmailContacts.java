package com.ness.zkworkshop.web.model;

import java.util.Arrays;
import java.util.Collection;

public class EmailContacts {

    public static Collection<? extends String> getContacts() {
        return Arrays.asList("Adam (adam@company.org)",
                "Chris (chris@company.org)", "Daniel (daniel@company.org)",
                "Eve(eve@company.org)", "Fritz (fritz@company.org)",
                "Mary (mary@company.org)", "Max (max@company.org)",
                "John (john@company.org)", "Peter (peter@company.org)");
    }

}
package com.ness.zkworkshop.web.model;

import java.util.Arrays;
import java.util.Collection;

public class EmailLabels {
    public static Collection<? extends String> getLabels() {
        return Arrays.asList("accounts", "friends", "information", "personal",
                "products", "projects", "support", "work");
    }

}
package com.github.axinger.config;

import java.util.HashSet;
import java.util.Set;

public class WhitePath {

    static Set<String> whitePaths = new HashSet<>();

    static {
        whitePaths.add("/account/user/doLogin");
        whitePaths.add("/account/user/logout");
        whitePaths.add("/account/user/register");
    }
}

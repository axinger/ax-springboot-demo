package com.github.axinger.model;

//权限分级：
public class SecurityViews {
    public static class Anonymous {
    }

    public static class User extends Anonymous {
    }

    public static class Moderator extends User {
    }
}

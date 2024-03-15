package com.example.springsecurityturtle.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    USER_READ("user::read"),

    ADMIN_READ("admin::read"),
    ADMIN_CREATE("admin::create"),
    ADMIN_UPDATE("admin::update")

    ;

    private final String permission;

}

package com.devhp.SpringRestDemoWithGradle.util.constants;

public enum Authority {
    READ,
    WRITE,
    UPDATE,
    USER, // Can update delete selft object, read anything
    ADMIN // Can read update delete any object

}

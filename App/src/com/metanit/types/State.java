package com.metanit.types;

public class State {

    String defaultVar = "default";  // доступно в любом месте текущего пакета

    private String privateVar = "private";  // доступно только из текущего класса

    protected String protectedVar = "protected";  // доступно из текущего пакета и производных классов

    public String publicVar = "public";  // доступно в любом месте программы


    // доступен в любом месте текущего пакета
    void printDefault() {
        System.out.println(defaultVar);
    }

    // доступен только из текущего класса
    private void printPrivate() {
        System.out.println(privateVar);
    }

    // доступен из текущего пакета и производных классов
    protected void printProtected() {
        System.out.println(protectedVar);
    }

    // доступен в любом месте программы
    public void printPublic() {
        System.out.println(publicVar);
    }
}
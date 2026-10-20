package com.example;

public class App {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello from Jenkins Java Build! App started.");
        // Бесконечный цикл, чтобы контейнер не завершался
        while (true) {
            Thread.sleep(10000); 
        }
    }

    public static String getGreeting() {
        return "Hello Jenkins!";
    }
}

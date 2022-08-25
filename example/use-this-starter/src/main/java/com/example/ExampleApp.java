package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!");
        SpringApplication.run(ExampleApp.class,args);
    }
}

package com.github.bobbobbob15;

import com.google.gson.Gson;
import org.simplejavamail.api.mailer.config.TransportStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    }
 public static Person getUserFromFile(File file) throws FileNotFoundException {
        var gson = new Gson();
        Scanner sc = new Scanner(file);
        var builder = new StringBuilder();
        while(sc.hasNextLine()){
            builder.append(sc.nextLine());
        }
        return gson.fromJson(builder.toString(), Person.class);
    }
}


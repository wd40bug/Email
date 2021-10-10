package com.github.bobbobbob15;

import com.google.gson.Gson;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class Person implements Serializable {
    Person(int i) throws IOException {
        constructor();
    }
    Person(){

    }

    private void constructor() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Hello! enter your email");
        username = in.nextLine();
        if (!EmailAddressValidator.isValid(username)) {
            System.out.println("invalid input");
            constructor();
        }
        var console = System.console();
        password = readPwd();

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    private static String readPwd() throws IOException {
        Console c=System.console();
        if (c==null) { //IN IDE
            System.out.print("Password: ");
            InputStream in=System.in;
            int max=50;
            byte[] b=new byte[max];

            int l= in.read(b);
            l--;//last character is \n
            if (l>0) {
                byte[] e=new byte[l];
                System.arraycopy(b,0, e, 0, l);
                return new String(e);
            } else {
                return null;
            }
        } else { //Outside Eclipse IDE
            return new String(c.readPassword("Password: "));
        }
    }

    private String username;
    private String password;
}

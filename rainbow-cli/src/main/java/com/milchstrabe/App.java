package com.milchstrabe;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * rainbow client test program
 *
 */
public class App{

    public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        Scanner inp = new Scanner(System.in);

        login(inp);

        while (true){
            System.out.print("$ ");
            String str = inp.nextLine();

            if(str == null || "".equals(str.trim())){
                str = "command is error";
            }
            if ("exit".equals(str.trim())){
                System.exit(1);
            }
            System.out.println(str);
        }

    }

    private static void login(Scanner inp){
        System.out.print("username: ");
        String username = inp.nextLine();
        System.out.print("password: ");
        String password = inp.nextLine();
        if("admin".equals(username) && "123".equals(password)){
            return;
        }
        login(inp);
    }
}

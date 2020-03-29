package com.gmail.kapcode.node.coms;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {
    //
    static String homeDir = System.getProperty("user.home");
    String applicationName = "";

    public static void main(String[] args) {
        //args = -home and somePathToHomeDir
        if (args.length > 1 && args[0].equals("-home")) {
            homeDir = args[1];
        }
        try {
            new Main();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Main() throws IOException {
        //added _ to name to ensure working dir is never user.home. is always a sub dir.
        File workingDir = new File(homeDir, "_" + applicationName);
        System.out.println(workingDir.toString());
        if (!workingDir.exists()) {
            //copy index.js/html and package.json to workingDir
            workingDir.mkdirs();

            Files.copy(new File(getClass().getResource("index.html").getFile()).toPath(),
                    new File(workingDir, "index.html").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy(new File(getClass().getResource("index.js").getFile()).toPath(),
                    new File(workingDir, "index.js").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy(new File(getClass().getResource("package.json").getFile()).toPath(),
                    new File(workingDir, "package.json").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);


        }

        //setup node
        //cd to working directory
        //run: npm install express@4.15.2
        //then run: npm install socket.io
        if (File.separator.equals("/")) {
            runCommandLinux(workingDir.getAbsolutePath(), "npm install express@4.15.2");
            runCommandLinux(workingDir.getAbsolutePath(), "npm install socket.io");
        } else {
            //run command using windows method
        }


        // finally: node index.js
        if (File.separator.equals("/")) {
            runCommandLinux(workingDir.getAbsolutePath(), "node index.js");
        }
        /*TODO be able to receive messages on client side from server.
        https://socket.io/get-started/chat/#Introduction
         */


    }

    public void handleMessage(String message){
        switch (message){
            case "":

                break;



        }
    }

    //todo test windows impl.
    public void runCommandWindows(String workingDirectory, String command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd " + workingDirectory + " && " + command);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
    }


    //todo go through the copy/paste code!
    public void runCommandLinux(String workingDir, String command) throws IOException {
        Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "cd " + workingDir + "&& " + command});
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));
        String s = null;
        try {
            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                if(command.equals("node index.js"))handleMessage(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (
                IOException e) {
            System.out.println("exception..");
            e.printStackTrace();
            System.exit(-1);
        }


    }



}

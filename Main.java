package com.company;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    static boolean login = false;
    static String myURL = "UNKNOWN";
    static Bot bot = new Bot();
    static boolean needCode = true;
    static boolean useScript = true;
    static Queue<String> replies = new LinkedList<>();
    static String script = "";
    static MainWindow mainWindow;

    public static void main(String[] args) {
        initFrame();
        bot.initialise();
        mainWindow.initSettingPanel();
        mainWindow.updateReplyPanel();
    }

    static void readScript() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("script.txt"), StandardCharsets.UTF_8));
            reader.readLine();
            String temp;
            while (reader.ready()) {
                temp = reader.readLine();
                script += temp;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainWindow.appendInfoText("자바스크립트: ");
        mainWindow.appendInfoText(script);
    }

    private static void initFrame() {
        JFrame frame = new JFrame("MainWindow");
        mainWindow = new MainWindow();
        frame.setContentPane(mainWindow.panelMain);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
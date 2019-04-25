package com.company;

import org.jsoup.Jsoup;

import static com.company.Main.mainWindow;
import static com.company.Main.myURL;


class ViewAttack {

    private static int cores = 5;
    private static int viewcount = 1;
    private static int prev_c = 0;

    private static boolean[] thread_online= new boolean[cores];
    static void startThreads() {
        for (int i = 0; i < cores; i++) {
            thread_online[i]=true;
            int finalI = i;
            Thread go = new Thread(() -> attack(finalI));
            go.start();
            mainWindow.appendViewText("[Core " + i + "] is online.");
        }
        countdown();
    }
    static void stopThreads(){
        for (int i = 0 ; i< cores;i++) {
            thread_online[i] = false;
        }
    }
    private static void countdown() {
        while (thread_online[0]) {
            try {
                int interval = 5;
                Thread.sleep(interval * 1000);
                mainWindow.updateViewcount(viewcount,prev_c);
                prev_c = viewcount;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void attack(int number) {
        while (thread_online[number]) {
            try {
                Jsoup.connect(myURL).get();
                viewcount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Core "+number+" stops");
    }

}

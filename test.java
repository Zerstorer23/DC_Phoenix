package com.company;

import static com.company.Main.*;
/*
Maven Dependencies

    <dependencies>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-server</artifactId>
            <version>3.13.0</version>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>3.13.0</version>
        </dependency>
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.7.2</version>
        </dependency>
    </dependencies>
* */
public class test {

    public static void main(String[] args) {
        System.out.println("코드 테스트용 입니다.");
        myURL="https://m.dcinside.com/board/comic_new1/9232955";
        Thread parent = new Thread(ViewAttack::startThreads);
        parent.start();
    }


}

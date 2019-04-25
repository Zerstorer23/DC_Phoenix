package com.company;

import javax.swing.*;

import static com.company.Main.*;

public class MainWindow {
    private JButton recommButton;
    private JButton F5recomm;
    private JTextPane a1011011TextPane;
    private JButton QuitButton;
    private JButton CommentButton;
    private JButton VIewButton;
    private JTextArea repliesTextArea;
    public JPanel panelMain;
    public JTextField codeField;
    public JPanel codePanel;
    private JTextPane viewtextpane;
    private JPanel recom_panel;
    private JPanel extra_panel;


    private boolean viewToggle=false;
    MainWindow() {
        recommButton.addActionListener(e -> {
            System.out.println("추천 확인");
            if(!bot.initDone) return;
            bot.recommend();
        });
        F5recomm.addActionListener(e -> {
            System.out.println("새로고침확인");
            if(!bot.initDone) return;
            bot.clear();
            bot.connectTo(myURL);
        });
        CommentButton.addActionListener(e -> {
            if(!bot.initDone) return;
            bot.writeComment();
            System.out.println(" 댓글 확인");
        });
        QuitButton.addActionListener(e -> {
            bot.driver.quit();
            System.exit(1);
        });
        VIewButton.addActionListener(e -> {
            if(!viewToggle){
                System.out.println("시작");
                Thread parent = new Thread(ViewAttack::startThreads);
                parent.start();
                VIewButton.setText("중단");
            }else{
                System.out.println("종료");
                Thread parent = new Thread(ViewAttack::stopThreads);
                parent.start();
                VIewButton.setText("조회수 조작");
            }
            viewToggle=!viewToggle;
        });
        codeField.addActionListener(e -> {
            if(!bot.initDone) return;
            bot.recommend();
        });
    }
    void delaySettingPanel(){
        extra_panel.setVisible(false);
        recom_panel.setVisible(false);
    }
    void initSettingPanel(){
        String setting;
        if(useScript){
            setting="자바스크립트로 추천합니다 \n";
        }else{
            setting="태그로 추천버튼을 클릭합니다 \n";
        }
        if(needCode){
            setting+="코드가 필요한 갤러리.\n";
            codePanel.setVisible(true);
            if(useScript){
                setting+="자바스크립트 사용 해제\n";
            }
        }
        setting+="연결 페이지:\n"+myURL;
        if(needCode){
            setting+="코드를 입력해주세요";
        }
        a1011011TextPane.setText(setting);
        extra_panel.setVisible(true);
        recom_panel.setVisible(true);
    }
    void setText(String text){
        a1011011TextPane.setText(text);
    }
    void appendInfoText(String text){
        String prev = a1011011TextPane.getText();
        prev+="\n"+text;
        a1011011TextPane.setText(prev);
    }
    void updateReplyPanel(){
        StringBuilder reps= new StringBuilder();
        for(String reply:replies){
            reps.append(reply).append("\n");
        }
        repliesTextArea.setText(reps.toString());
    }
    void updateViewcount(int now, int prev){
        viewtextpane.setText("조회:"+now+ " (+" + (now - prev) + ")");
    }
    void appendViewText(String text){
        String prev = viewtextpane.getText();
        prev+="\n"+text;
        viewtextpane.setText(prev);

    }
}

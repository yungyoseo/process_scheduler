package scheduler_gui;

import process.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class ButtonsPanel extends JPanel {
    private InputPanel inputPanel;
    private OutputPanel outputPanel;
    private GanttPanel ganttPanel;
    private ProcessPoll pp = new ProcessPoll();
    private String[] comboIndex = {"FCFS", "SJF", "SRTF", "RR", "Custom"};
    JComboBox<String> policyBox;

    public ButtonsPanel(InputPanel inputPanel, OutputPanel outputPanel, GanttPanel ganttPanel) {
        this.outputPanel = outputPanel;
        this.inputPanel = inputPanel;
        this.ganttPanel = ganttPanel;
        setLayout(new FlowLayout());

        JButton openBtn = new JButton("file open");
        JButton runBtn = new JButton("run");
        policyBox = new JComboBox<>();
        for (String s : comboIndex) policyBox.addItem(s);

        openBtn.setBorder(new LineBorder(Color.green));
        openBtn.setPreferredSize(new Dimension(100, 40));
        runBtn.setBorder(new LineBorder(Color.green));
        runBtn.setPreferredSize(new Dimension(100, 40));
        policyBox.setBorder(new LineBorder(Color.green));
        policyBox.setPreferredSize(new Dimension(100, 40));

        openBtn.addActionListener(new SelectFile());
        runBtn.addActionListener(new runFile());
        add(openBtn);
        add(runBtn);
        add(policyBox);
    }

    class SelectFile implements ActionListener{
        public void actionPerformed(ActionEvent e){
            new FileSelector(pp);

            // 버튼 눌렀을 때 input패널창 변경
            inputPanel.reTable(pp);
        }
    }
    class runFile implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(pp.isEmpty()) return;
            List<List<String>> output = new ArrayList<>();
            List<List<String>> gantt = new ArrayList<>();

            FCFS fcfs = new FCFS(pp);
            SJF sjf = new SJF(pp);
            SRTF srtf = new SRTF(pp);
            RoundRobin rr = new RoundRobin(pp, 4);
            Custom custom = new Custom(pp);

            // 현재 선택된 정책에 따라 다른 정책을 실행
            String selectedPolicy = (String) policyBox.getSelectedItem();
            switch (selectedPolicy) {
                case "FCFS":
                    fcfs.run();
                    output = fcfs.getOutput();
                    gantt = fcfs.getGantt();
                    break;
                case "SJF":
                    sjf.run();
                    output = sjf.getOutput();
                    gantt = sjf.getGantt();
                    break;
                case "SRTF" :
                    srtf.run();
                    output = srtf.getOutput();
                    gantt = srtf.getGantt();
                    break;
                case "RR" :
                    rr.run();
                    output = rr.getOutput();
                    gantt = rr.getGantt();
                    break;
                case "Custom":
                    custom.run();
                    output = custom.getGanttOutput();
                    break;
                default:
                    // 기본으로 FCFS 실행
                    fcfs.run();
                    break;
            }
            outputPanel.reTable(output);
            ganttPanel.paintChart(gantt);
            ganttPanel.setAverageWaitingTime(output);
        }
    }
}

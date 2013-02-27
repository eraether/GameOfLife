package com.eugene.gol;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class ConwayFrame extends JFrame{
      private ConwayPanel conwayPanel;
      public ConwayFrame(){
         initGUI();
      }
   	
      private void initGUI(){
         setLayout(new GridLayout(1,1));
         setTitle("Conways GoL");
         ConwayPanel conwayPanel = new ConwayPanel();
         setConwayPanel(conwayPanel);
         setSize(500,500);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         add(conwayPanel);
      }
      private void setConwayPanel(ConwayPanel panel){
         this.conwayPanel = panel;
      }
   	
      public void setConwayGame(ConwayGame game){
         conwayPanel.setConwayGame(game);
      }
   }
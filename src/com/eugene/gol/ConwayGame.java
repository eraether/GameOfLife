package com.eugene.gol;

public class ConwayGame{
      private Board board;
      private boolean[] livingRules;
      private boolean[] reanimateRules;
   	
      public ConwayGame(Board board){
         this(board, new boolean[]{false,false,true,true,false,false,false,false,false}, new boolean[]{false,false,false,true,false,false,false,false,false});
      }
      public ConwayGame(Board board, boolean[] rules, boolean[] reanimateRules){
         setBoard(board);
         setLivingRules(rules);
         setReanimateRules(reanimateRules);
      } 
   	
      private void setLivingRules(boolean[] rules){
         this.livingRules = rules;
      }
      public boolean[] getLivingRules(){
         return livingRules;
      }
   	
      private void setReanimateRules(boolean[] rules){
         this.reanimateRules = rules;
      }
      public boolean[] getReanimateRules(){
         return reanimateRules;
      }
   	
      public void activateTile(int tile){
      
      }
   	
   	//returns an int[] array of how many neighbors a given slot has
      public void simulateTick(){
         Board newTickBoard = new Board(board.getHeight(), board.getWidth());      
         boolean[] livingRules = this.getLivingRules();
         boolean[] reanimateRules = this.getReanimateRules();
      	  
         boolean  isCurrentTileActive = false;
         for(int x=0;x<board.getHeight();x++){
            for(int y=0;y<board.getWidth();y++){
               isCurrentTileActive = board.getTile(x,y);
               int size = board.getNeighbors(x,y);
               if(isCurrentTileActive == true)
               {          
                  isCurrentTileActive = livingRules[size];
               }
               else
               { 
                  isCurrentTileActive = reanimateRules[size];
               }
               newTickBoard.setTile(isCurrentTileActive, x,y);
            }
         }
         this.board = newTickBoard;
      }
   
      private void setBoard(Board board){
         this.board = board;
      }
   
      public Board getBoard(){
         return board;
      }
   }
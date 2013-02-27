package com.eugene.gol;

public class Board{
      private boolean[][] board;
      public Board(int width, int height){
         board = new boolean[width][height];
         resetBoard();
      }
   	
      public Board duplicate(){
         Board dupBoard = new Board(getWidth(), getHeight());
         for(int x=0;x<getHeight();x++){
            for(int y=0;y<getWidth();y++){
               dupBoard.setTile(getTile(x,y),x,y);
            }
         }
         return dupBoard;
      }
   	
      private void resetBoard(){
         for(int x=0;x<getHeight();x++){
            for(int y=0;y<getWidth();y++){
               setTile(false,x,y);
            }
         }
      }
   	
      public int getWidth(){
         return board[0].length;
      }
   	
      public int getHeight(){
         return board.length;
      }
   	
      public void setTile(boolean t, int x, int y)
      {
         board[x][y] = t;
      }
    
      public int getNeighbors(int centerX, int centerY)
      {
         int total = 0;
      
         for(int x=-1;x<=1;x++){
            for(int y=-1;y<=1;y++){
               if(x==0 && y==0)
                  continue;
               int dX = x+centerX;
               int dY = y+centerY;
               if(isValid(dX,dY) && getTile(dX,dY))
                  total++;
            }
         }
         return total;
      }
   	  
      public boolean getTile(int x, int y){
         return board[x][y];
      }
   	
      public boolean isValid(int x, int y){
         if(x < 0 || y < 0 || x >= getHeight() || y >= getWidth())
            return false;
         return true;
      }
   }
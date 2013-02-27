package com.eugene.gol;
   import java.awt.*;
   import javax.swing.*;
   import java.util.*;
   import java.awt.geom.*;
   import java.awt.event.*;
   import javax.swing.event.*;
   import java.awt.image.*;
	import javax.imageio.*;
	import java.io.*;
	
   public class ConwaysGameOfLife{
      public static void main(String [] args){
         System.out.println("Conway's Game of Life v1.00 by Eugene Raether\n");
         ConwayFrame frame = new ConwayFrame();
      	
         int defaultWidth = 400;
         int defaultHeight = 400;
         boolean[] defaultLivingRules= new boolean[]{false,false,true,true,false,false,false,false,false};
         boolean[] defaultReanimateRules= new boolean[]{false,false,false,true,false,false,false,false,false};
         String defaultBoardType = "checkerboard";
      	
         TreeMap<String, String> parameterMap = generateParameterMap(args);
      
         int width = iParam(parameterMap.get("width"), defaultWidth);
         int height = iParam(parameterMap.get("height"), defaultHeight);
         boolean[] livingRules = baParam(parameterMap.get("livingrules"), defaultLivingRules);  
         boolean[] reanimateRules = baParam(parameterMap.get("reanimaterules"), defaultReanimateRules);
         if(livingRules.length != defaultLivingRules.length || reanimateRules.length != defaultReanimateRules.length)
         {
            System.out.println("ERROR: Rulesets must have a length of "+defaultLivingRules.length+"!  Using default values.");
            livingRules = defaultLivingRules;
            reanimateRules = defaultReanimateRules;
         }
      
         ArrayList<String> availableBoardTypes = new ArrayList<String>();
         availableBoardTypes.add(defaultBoardType);
         availableBoardTypes.add("random");
      	
         String boardType = sParam(parameterMap.get("type"), defaultBoardType);
         if(availableBoardTypes.contains(boardType) == false)
         {
            System.out.println("ERROR: Provided board type does not exist.  Available board types are "+availableBoardTypes+".  Using default value.");
            boardType = availableBoardTypes.get(0);
         }
      	
         System.out.println("\nSETTINGS");
         System.out.println("Board size set to "+width+"x"+height+" (set with width=, height=)");
         System.out.println("Living Rules:"+printArray(livingRules)+" (set with livingrules=f,f,t,t,f...)");
         System.out.println("Reanimate Rules:"+printArray(reanimateRules)+" (set with reanimaterules=f,f,f,t,f,...)");
         System.out.println("Board type is "+boardType+" (set with type="+availableBoardTypes+")");
         System.out.println("\nCONTROLS");
         System.out.println("Click to edit board, space to start/stop simulation, right arrow to simulate 1 frame, 'p' to regenerate color palette, 's' to save current display buffer, hold alt while hovering over the display to zoom in");
         ConwayGame game;
         if(boardType.equals(defaultBoardType))
            game = generateCheckerboard(width, height, livingRules, reanimateRules);
         else
            game = generateRandomBoard(width, height, livingRules, reanimateRules);
      		
         frame.setConwayGame(game);    
         frame.setVisible(true);
      }
   	
      private static int iParam(String val, int defaultValue){
         try{
            return Integer.parseInt(val);
         }
            catch(Exception e){
               return defaultValue;
            }
      }
   	
      private static String sParam(String val, String defaultVal){
         if(val == null)
            return defaultVal;
         return val;
      }
   	
      private static boolean[] baParam(String val, boolean[] defaultValue)
      {
         try{
            String[] arr = val.split(",");
            boolean[] bArr = new boolean[arr.length];
            for(int x=0;x<arr.length;x++){
               bArr[x] = true;
               if(arr[x].toLowerCase().startsWith("f"))
                  bArr[x] = false;
            }
            return bArr;
         }
            catch(Exception e){
               return defaultValue;
            }
      }
   
      private static String printArray(boolean[] array){
         if(array == null)
            return "null";
      
         String out = "[";
         for(int x=0;x<array.length;x++){
            out += array[x];
            if(x != array.length-1)
               out += ",";
         }
         out += "]";
         return out;
      }
   
      private static TreeMap<String, String> generateParameterMap(String[] args){
         String delimiter = "=";
         TreeMap<String, String> map = new TreeMap<String, String>();
         for(String s : args)
         {
         
            if(s.contains(delimiter))
            {
               int index = s.indexOf(delimiter);
               String variable = s.substring(0, index);
               String value = s.substring(index+1);
               map.put(variable, value);
            }
         }
         return map;
      }
   	
   	
      private static ConwayGame generateCoolGraphic(){
         Board board = new Board(70,70);
         for(int x=0;x<board.getHeight();x++){
            for(int y=0;y<board.getWidth();y++){
               board.setTile((x+y)%2==0,x,y);
            }
         }
         ConwayGame game = new ConwayGame(board);
         return game;
      }
   	
      private static ConwayGame generateCheckerboard(int width, int height, boolean[] livingRules, boolean[] reanimateRules)
      {
         Board board = new Board(width, height);
         for(int x=0;x<board.getHeight();x++){
            for(int y=0;y<board.getWidth();y++){
               board.setTile((x+y)%2==0,x,y);
            }
         }
      
      
         ConwayGame game = new ConwayGame(board, livingRules, reanimateRules);
         return game;
      }
   	
      private static ConwayGame generateRandomBoard(int width, int height, boolean[] livingRules, boolean[]reanimateRules){
         Board board = new Board(width, height);
         for(int x=0;x<board.getHeight();x++){
            for(int y=0;y<board.getWidth();y++){
               board.setTile(Math.random()>0.5,x,y);
            }
         }
         ConwayGame game = new ConwayGame(board, livingRules, reanimateRules);
         return game;
      }
   	
   
      private static ConwayGame generateNormalGame(){
         Board board = new Board(1000,1000);
         for(int x=0;x<board.getHeight();x++){
            for(int y=0;y<board.getWidth();y++){
               board.setTile(Math.random()>0.5,x,y);
            }
         }
         ConwayGame game = new ConwayGame(board);
         return game;
      }
   }

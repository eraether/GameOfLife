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
	
   class ConwayFrame extends JFrame{
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
   class ConwayPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
      private ConwayGame conwayGame;
      private boolean paused = true;    
      private long lastTickTime;  
      private int tileSize = 20;
      private boolean isTileDraggedActive = false;  
      private boolean simulateOnce = false;
      private int[] colorPalette;
      private ArrayList<Long> fps = new ArrayList<Long>();
      private BufferedImage renderContext;
   	
      private int lastMX = 0;
      private int lastMY = 0;
      private boolean zoomingIn = false;
      private int totalFrames = 0;
   	
      public ConwayPanel(){
         addListeners();
      	
         setFocusable(true);  
         requestFocus();
         setPalette(generatePalette());	
      }
   	
      private void setPalette(int[] palette){
         colorPalette = palette;
      }
   	
   	
      private void addListeners(){
         this.addMouseListener(this);
         this.addMouseMotionListener(this);
         this.addKeyListener(this);
      }
   	
      public void setConwayGame(ConwayGame conwayGame){
         this.conwayGame = conwayGame;
         this.renderContext = new BufferedImage(conwayGame.getBoard().getWidth(), conwayGame.getBoard().getHeight(),BufferedImage.TYPE_INT_RGB);
      }
   	
      private void renderNeighborMap(int[] res)
      {
         int[] paletteMap = this.colorPalette;
         for(int x=0;x<res.length;x++)
         {
            res[x] = paletteMap[res[x]];
         }
         renderContext.setRGB(0,0,getGame().getBoard().getWidth(),getGame().getBoard().getHeight(),res,0,1);
      
      }
      private void runUpdate(){
         long diff = System.currentTimeMillis()-lastTickTime;	
         if(simulateOnce)
            System.out.println("Simulating once (frame "+totalFrames+")");
      	
         if((!paused && diff > 40) || simulateOnce)
         {
            simulateOnce = false;
         	//this.colorPalette = generatePalette();
            lastTickTime = System.currentTimeMillis();
            conwayGame.simulateTick();
           // renderNeighborMap(res);
            totalFrames++;
         }
         fps.add(System.currentTimeMillis());
         if(fps.size()>100)
            fps.remove(0);  
      }
   	
      private Color generateColor(boolean maxSat){
         Color c = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
         if(!maxSat)
            return c;
         int min = Math.min(Math.min(255-c.getRed(),255-c.getGreen()),255-c.getBlue());
         return new Color(c.getRed()+min, c.getGreen()+min, c.getBlue()+min);
      }
   	
      private int calculateSaturation(Color start, Color end){
         return (start.getRed()+start.getGreen()+start.getBlue())/3-(end.getRed()+end.getGreen()+end.getBlue())/3;
      }
   
      private Color[] generateColors(int differentColors, int interpolationsPerColor){
         Color[] randomColors = new Color[differentColors+1];
         for(int x=0;x<randomColors.length;x++)
            randomColors[x] = generateColor(x%2==1);
      		
         int current = 0;
         Color[] finalColors = new Color[differentColors*interpolationsPerColor];
         for(int x=0;x<randomColors.length-1;x++){
            Color a = randomColors[x];
            Color b = randomColors[x+1];
            for(int i=0;i<interpolationsPerColor;i++)
            {
               int dR = (b.getRed()-a.getRed())/(interpolationsPerColor);
               int dG = (b.getGreen() - a.getGreen())/(interpolationsPerColor);
               int dB = (b.getBlue() - a.getBlue())/(interpolationsPerColor);
               finalColors[current++] = new Color(a.getRed()+i*dR,a.getGreen()+i*dG,a.getBlue()+i*dB);
            }
         }
         return finalColors;      
      }
   
      private int[] generatePalette(){
         Color[] colors = generateColors(5,2);
         
         int[] palette = new int[colors.length];
         for(int x=0;x<colors.length;x++)
         {
            palette[x] = colors[x].getRGB();
         }  
         return palette;
      }
   
      private void recalculateTileSize(int width, int height){
         int dX = width / getGame().getBoard().getWidth();
         int dY = height / getGame().getBoard().getHeight();
         setTileSize(Math.max(Math.min(dX,dY),1));
      }
   
     
      private Rectangle calculateSourceRectangle(int startX, int startY)
      {
         int coverageArea = 50 / getTileSize();
         return new Rectangle(startX-coverageArea,startY-coverageArea,startX+coverageArea,startY+coverageArea);
      }
      
      public void paintComponent(Graphics g){
         runUpdate();      
         Rectangle clipBounds = g.getClipBounds();
         g.setColor(Color.black);
         g.fillRect(0,0,clipBounds.width,clipBounds.height);
         recalculateTileSize(clipBounds.width, clipBounds.height);
         int tileSize = getTileSize();
      	
      	
        /* for(int x=0;x<getGame().getBoard().getHeight();x++){
            for(int y=0;y<getGame().getBoard().getWidth();y++){
               Tile t = getGame().getBoard().getTile(x,y);
               g.setColor(Color.black);
               g.drawRect(y*tileSize, x*tileSize, tileSize, tileSize);
            }
         }*/
      	
         renderGameToBuffer(getGame().getBoard(), renderContext, colorPalette);
      	
         g.drawImage(renderContext,0,0,getGame().getBoard().getWidth()*tileSize, getGame().getBoard().getHeight()*tileSize,null);
      	
         if(this.zoomingIn)
         {
            Point snapped = snapMouseToGrid(lastMX, lastMY);
         	//y, x
            Rectangle source = calculateSourceRectangle(snapped.y, snapped.x);
            int magSize = 100;
            //int startX = Math.max(lastMX-magSize,0);
            //int startY = Math.max(lastMY-magSize,0);
            //int endX = Math.min(lastMX+magSize,getGame().getBoard().getWidth()*tileSize);
            //int endY = Math.min(lastMY+magSize,getGame().getBoard().getHeight()*tileSize);
            int startX = snapped.y*tileSize-magSize;
            int startY = snapped.x*tileSize-magSize;
            int endX = startX+2*magSize;
            int endY = startY+2*magSize;
         	
            if(startX < 0)
            {
               int diff = endX - startX;
               endX = diff;
               startX = 0;
            }     	
         	
            if(startY < 0)
            {
               int diff = endY - startY;
               endY = diff;
               startY = 0;
            }
         	
            g.setColor(Color.white);
         //   g.drawRect(source.x*tileSize, source.y*tileSize, (source.width-source.x)*2*tileSize, (source.height-source.y)*2*tileSize);
         //   g.drawString(source.toString(), 20, 20);
            g.drawImage(renderContext,startX,startY,endX,endY,source.x,source.y,source.width,source.height,null);
         }
      	
      		/*
               boolean t = getGame().getBoard().getTile(x,y);
               g.setColor(Color.red);
               int size = getGame().getBoard().getNeighbors(x,y);
               if(t)
               {
                  g.setColor(new Color(colorPalette*size));
                  g.fillRect(y*tileSize, x*tileSize, tileSize, tileSize);
               }
            }
         }*/
      	
      	
         g.setColor(Color.yellow);
         long diff = Math.max(fps.get(fps.size()-1)-fps.get(0),1);
         g.drawString((int)((1000/(diff/(double)fps.size())))+"",(int)(clipBounds.getWidth()-15),10);
         String tickText = ""+totalFrames;
      	//g.drawString(tickText,(int)(clipBounds.getWidth()-7*(tickText.length())),20);
         try{
            Thread.sleep(20);
         }
            catch(Exception e){
            }
         repaint();
      }
   	
      private static void renderGameToBuffer(Board board, BufferedImage renderContext, int[] colorPalette){
      
         int[] color = colorPalette;  
         for(int x=0;x<board.getHeight();x++){
            for(int y=0;y<board.getWidth();y++){
               if(board.getTile(x,y))
                  renderContext.setRGB(y,x,color[board.getNeighbors(x,y)]);
               else
                  renderContext.setRGB(y,x,0);
            }
         }
      }
   	
      public ConwayGame getGame(){
         return this.conwayGame;
      }
   	
      public void mouseClicked(MouseEvent e){}
      public void mouseEntered(MouseEvent e){}
      public void mouseExited(MouseEvent e){}
      public void mousePressed(MouseEvent e){
         Point p = snapMouseToGrid(e.getX(), e.getY());
         
         if(getGame().getBoard().isValid(p.x,p.y))
         {
            boolean t = getGame().getBoard().getTile(p.x,p.y);
            isTileDraggedActive = t;
            getGame().getBoard().setTile(!t, p.x,p.y);
         }      }
      public void keyPressed(KeyEvent e){
         if(e.getKeyCode() == 39)//right arrow
         {
            simulateOnce = true;
         }
         else if(e.getKeyCode() == 18)//alt
         {
            zoomingIn = true;
         }
      	
      	
      }
      public void keyReleased(KeyEvent e){
         if(e.getKeyCode() == 18)
            zoomingIn = false;
      
      }
      public void keyTyped(KeyEvent e){
         if(e.getKeyChar() == ' ')
         {
            paused = !paused;
            System.out.println(paused?"Paused":"Unpaused");
         }
         else if(e.getKeyChar() == 'p') //p
         {
            setPalette(generatePalette());	
         }
         else if(e.getKeyChar() == 's')
         {
            saveImageBuffer();
         }
      
      
      }
   	
      private void saveImageBuffer(){
         Board board = this.getGame().getBoard().duplicate();
         BufferedImage context = new BufferedImage(this.renderContext.getWidth(), this.renderContext.getHeight(), this.renderContext.getType());
      //Graphics g = context.createGraphics();
         renderGameToBuffer(board, context, this.colorPalette);
         try{
            File f = new File(System.currentTimeMillis()+".png");
            ImageIO.write(context, "PNG", f);
            System.out.println("Saved screenshot to "+f.getCanonicalPath());
         }
            catch(Exception e){
               System.out.println("Could not save screenshot:"+e);
            }
      }
   	
   	
      public void mouseReleased(MouseEvent e){}
      public void mouseMoved(MouseEvent e){
         lastMX = e.getX();
         lastMY = e.getY();
      }
      public void mouseDragged(MouseEvent e){
         Point p = snapMouseToGrid(e.getX(), e.getY());
         if(getGame().getBoard().isValid(p.x,p.y))
         {
          //  Tile t = getGame().getBoard().getTile(p.x,p.y);
            getGame().getBoard().setTile(!isTileDraggedActive, p.x,p.y);
         }
      }
   
      private Point snapMouseToGrid(int x, int y){
         return new Point(y/getTileSize(), x/getTileSize());
      }
   	
      private int getTileSize(){
         return tileSize;
      }
   
      private void setTileSize(int tileSize){
         this.tileSize = tileSize;
      }
   }
	
   class ConwayGame{
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
	
   class Board{
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

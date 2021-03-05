package com.tarena.tetris;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * 俄罗斯方块游戏面板 
 *
 */
public class Tetris extends JPanel {
//	
//	/** 正在下落方块 */
	private Tetromino tetromino;
//	
//	/** 下一个下落方块 */

	private Tetromino nextOne;
	private Tetromino nextNextOne;
//	
//	
//	/** 行数 */
	public static final int ROWS = 20;
//	/** 列数 */
	public static final int COLS = 10;
//	
//	
//	/** 墙 */
	private Cell[][] wall = new Cell[ROWS][COLS]; 
//	/** 消掉的行数 */
	private int lines;
	/** 分数 */
	private int score;
	private int grade = 3;
	
	private Music music = new Music();
//	
	public static final int CELL_SIZE = 26;

	
	private static Image background;//背景图片
	public static Image I;
	public static Image J;
	public static Image L;
	public static Image S;
	public static Image Z;
	public static Image O;
	public static Image T;
	public static Image X;
	public static Image K;
	static{
		
		try{
			background = ImageIO.read(
				Tetris.class.getResource("tetris.png"));
			T=ImageIO.read(Tetris.class.getResource("T.png"));
			I=ImageIO.read(Tetris.class.getResource("I.png"));
			S=ImageIO.read(Tetris.class.getResource("S.png"));
			Z=ImageIO.read(Tetris.class.getResource("Z.png"));
			L=ImageIO.read(Tetris.class.getResource("L.png"));
			J=ImageIO.read(Tetris.class.getResource("J.png"));
			O=ImageIO.read(Tetris.class.getResource("O.png"));
			X=ImageIO.read(Tetris.class.getResource("X.png"));
			K=ImageIO.read(Tetris.class.getResource("K.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	




	
	public void action(){
		//tetromino = Tetromino.randomTetromino();
		//nextOne = Tetromino.randomTetromino();
		//wall[19][2] = new Cell(19,2,Tetris.T);

		startAction();
		repaint();

		
		music.init();
		music.playBj1();
		

		KeyAdapter l = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_Q){
					music.playExit();
					System.out.println("退出清零正常");
					
					System.exit(0);         //退出当前的Java进程
				}
				if(gameOver){               //结束后重新开始
					if(key==KeyEvent.VK_S){
						music.stopBj2();
						music.playBj1();
						startAction();
					}
					return;
				}
				//如果暂停并且按键是[C]就继续动作
				if(pause){//pause = false
					if(key==KeyEvent.VK_C){	
						music.stopBj2();
						music.playStart();
						music.playBj1();
						
						continueAction();	
					}
					return;
				}
				
				//否则处理其它按键
				switch(key){
					case KeyEvent.VK_RIGHT: {
						moveRightAction();
						music.stopMove();
						music.playMove();
						}; 
						break;
					case KeyEvent.VK_LEFT: {
						moveLeftAction();
						music.stopMove();
						music.playMove();
					}
						break;
					case KeyEvent.VK_DOWN: {
						moveAction() ;
						music.stopMove();
						music.playMove();
					} 
						break;
					case KeyEvent.VK_UP: {
						rotateRightAction() ;
						music.stopMove();
						music.playMove();
					}
					 	break;
					case KeyEvent.VK_Z: {
						rotateLeftAction() ;
						music.stopMove();
						music.playMove();
					}
					 	break;
					case KeyEvent.VK_SPACE: {
						hardDropAction() ;
					}
					 	break;
					case KeyEvent.VK_P: {
						music.playSuspend();
						music.stopBj1();
						music.playBj2();
						pauseAction() ;
					}
					 	break;
					case KeyEvent.VK_0: {
						softDropAction();
					}
					 	break;
					case KeyEvent.VK_1: {
						oneDropAction();
					}
					 	break;
					case KeyEvent.VK_2: {
						twoDropAction();
					}
					 	break;
					case KeyEvent.VK_3: {
						threeDropAction();
					}
					 	break;
					case KeyEvent.VK_4: {
						fourDropAction();
					}
					 	break;
					case KeyEvent.VK_5: {
						fiveDropAction();
					}
					 	break;
					case KeyEvent.VK_6: {
						sixDropAction();
					}
					 	break;
					case KeyEvent.VK_X: {
						clear();
					}
					 	break;
				}
				
				repaint();
			}
		};
		
		this.requestFocus();
		this.addKeyListener(l);
		
	}

	public void startAction(){
		
		clearWall();
		tetromino = Tetromino.randomTetromino();
		nextOne = Tetromino.randomTetromino();
		nextNextOne = Tetromino.randomTetromino();
		System.out.println("预窗格加载");
		
		lines = 0; 
		score = 0;	
		pause=false; 
		gameOver=false;
		

		softDropAction();
		System.out.println("图形启动速度正常");
	}
	
	
	
	public void isXBomb(){
		if(tetrominoCanDrop()){
			tetromino.softDrop();
		}else{
			Xbum();
			tetromino = nextOne;
			nextOne = nextNextOne;
			nextNextOne = Tetromino.randomTetromino();
		}
		
	}
	public void isKBomb(){
		if(tetrominoCanDrop()){
			tetromino.softDrop();
		}else{
			Ybum();
			tetromino = nextOne;
			nextOne = nextNextOne;
			nextNextOne = Tetromino.randomTetromino();
		}
		
	}
	public void Ybum(){
		music.playLand();
		Cell[] cells = tetromino.getCells();
		
			Cell cell1 = cells[0];
			Cell cell2 = cells[1];
			
			int row1 = cell1.getRow();
			int col1 = cell1.getCol();
			
			int row2 = cell2.getRow();
			int col2 = cell2.getCol();
			
			if(row1 == row2){
				deleteRow(row1);
			}else {				
				for(int i=0;i<=ROWS-1;i++){
					wall[i][col1]=null;	
				}
			}		
			this.lines += 1;//0 1 2 3 4
	}
	
	public void Xbum(){
		music.playLand();
		Cell[] cells = tetromino.getCells();
		
			Cell cell = cells[0];
			int row = cell.getRow();
			int col = cell.getCol();

			
			if(row >= ROWS-2 && col == 0){
			
				wall[ROWS-3][2]=null;
				wall[ROWS-2][2]=null;
				wall[ROWS-1][2]=null;
			}else if (row >= ROWS-2 && col >= COLS-2) {
				
				wall[ROWS-3][COLS-3]=null;
				wall[ROWS-2][COLS-3]=null;
				wall[ROWS-1][COLS-3]=null;
			}else if (row >= ROWS-2) {
				
				wall[row-1][col-1]=null;
				wall[row-1][col+2]=null;
				
				wall[row][col-1]=null;
				wall[row][col+2]=null;
				
				wall[row+1][col-1]=null;
				wall[row+1][col+2]=null;
				
			}else if (col<=0) {
				wall[row-1][col+2]=null;
				wall[row][col+2]=null;
				wall[row+1][col+2]=null;
				wall[row+2][col+2]=null;
				
				wall[row+2][col]=null;
				wall[row+2][col+1]=null;
			}else if (col>=COLS-2) {
				wall[row-1][col-1]=null;
				wall[row][col-1]=null;
				wall[row+1][col-1]=null;
				wall[row+2][col-1]=null;
				
				wall[row+2][col]=null;
				wall[row+2][col+1]=null;
				
			}else {
				
				wall[row-1][col-1]=null;
				wall[row][col-1]=null;
				wall[row+1][col-1]=null;
				wall[row+2][col-1]=null;
				
				wall[row-1][col+2]=null;
				wall[row][col+2]=null;
				wall[row+1][col+2]=null;
				wall[row+2][col+2]=null;
				
				wall[row+2][col]=null;
				wall[row+2][col+1]=null;
			}
			this.lines += 2;//0 1 2 3 4
	}
	
	public void moveAction(){
		if(tetrominoCanDrop()){
			tetromino.softDrop();
		}else{
			if (tetromino.getFlagCell() == "X") {
				isXBomb();
				
			}else if(tetromino.getFlagCell() == "K"){
				isKBomb();
			}else {
				tetrominoLandToWall();
				destroyLines();//破坏满的行
				checkGameOver();
				tetromino = nextOne;
				nextOne = nextNextOne;
				nextNextOne = Tetromino.randomTetromino();	
				softDropAction();
			}
			repaint();
		}
	}
	public void paint(Graphics g){
		g.drawImage(background, 0, 0, null);//使用this 作为观察者
		g.translate(15, 15);//平移绘图坐标系
		paintTetromino(g);//绘制正在下落的方块
		paintWall(g);//画墙
		paintNextOne(g);
		paintNextNextOne(g);
		paintScore(g);
		
	}
	
	public static final int FONT_COLOR = 0x667799;
	public static final int FONT_SIZE = 0x20;
	private void paintScore(Graphics g) {
		Font f = getFont();//获取当前的 面板默认字体
		Font font = new Font(
				f.getName(), Font.BOLD, FONT_SIZE);
		int x = 290;
		int y = 162;
		g.setColor(new Color(FONT_COLOR));
		g.setFont(font);
		
		String str = "S:"+this.score;
		g.drawString(str, x, y);
		
		x+=70;
		str = "L:"+this.lines;
		g.drawString(str, x, y);
		
		x+=70;
		str = "G:"+this.grade;
		g.drawString(str, x, y);
		
		y+=56;
		x-=140;
		
		int c = lines/2 - used;
		str = "[X]Clear: "+c;
		g.drawString(str, x, y);
		
		y+=56;
		str = "[P]Pause";
		if(pause){str = "[C]Continue";}
		if(gameOver){	str = "[S]Start!";}
		g.drawString(str, x, y);

		
		
	}



	private void paintNextOne(Graphics g) {
		Cell[] cells = nextOne.getCells();
		for(int i=0; i<cells.length; i++){
			Cell c = cells[i];
			int x = (c.getCol()+8) * CELL_SIZE-1;
			int y = (c.getRow()+1) * CELL_SIZE-1;
			g.drawImage(c.getImage(), x, y, null);
		}	
	}
	
	private void paintNextNextOne(Graphics g) {
		Cell[] cells = nextNextOne.getCells();
		for(int i=0; i<cells.length; i++){
			Cell c = cells[i];
			int x = (c.getCol()+12) * CELL_SIZE-1;
			int y = (c.getRow()+1) * CELL_SIZE-1;
			g.drawImage(c.getImage(), x, y, null);
		}	
	}

	private void paintTetromino(Graphics g) {
		Cell[] cells = tetromino.getCells();
		for(int i=0; i<cells.length; i++){
			Cell c = cells[i];
			int x = c.getCol() * CELL_SIZE-1;
			int y = c.getRow() * CELL_SIZE-1;
			//g.setColor(new Color(c.getColor()));
			//g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
			g.drawImage(c.getImage(), x, y, null);
		}		
	}
//
//	//在 Tetris 类 中添加 方法 paintWall
	private void paintWall(Graphics g){
		for(int row=0; row<wall.length; row++){
			//迭代每一行, i = 0 1 2 ... 19
			Cell[] line = wall[row];
			//line.length = 10
			for(int col=0; col<line.length; col++){
				Cell cell = line[col]; 
				int x = col*CELL_SIZE; 
				int y = row*CELL_SIZE;
				if(cell==null){
					g.setColor(new Color(0));
//					画方形 
					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
				}else{
					g.drawImage(cell.getImage(), x-1, y-1, null);
				}
			}
		}
	}
//	/**
//	 * 在 Tetris(俄罗斯方块) 类中增加方法
//	 * 这个方法的功能是：软下落的动作 控制流程
//	 * 完成功能：如果能够下落就下落，否则就着陆到墙上，
//	 *   而新的方块出现并开始落下。
//	 */

	
	
	
//	/** 销毁已经满的行，并且计分
//	 * 1）迭代每一行 
//	 * 2）如果（检查）某行满是格子了 就销毁这行 
//	 **/
	public void destroyLines(){
		music.stopLand();
		music.playSorce();
		
		int lines = 0;
		for(int row = 0; row<wall.length; row++){
			if(fullCells(row)){
				deleteRow(row);
				lines++;
			}
		}
		// lines = ?
		this.lines += lines;//0 1 2 3 4
		this.score += SCORE_TABLE[lines];
	}
	private static final int[] SCORE_TABLE={0,1,4,9,16};
//	//                                      0 1  2  3  4
//	
		
	public boolean fullCells(int row){
		Cell[] line = wall[row];
		for(int i=0; i<line.length; i++){
			if(line[i]==null){//如果有空格式就不是满行
				return false;
			}
		}
		return true;
	}
	
	public void deleteRow(int row){
		for(int i=row; i>=1; i--){
			//复制 [i-1] -> [i] 
			System.arraycopy(wall[i-1], 0, wall[i], 0, COLS);//将i-1行移到i行
		}
		Arrays.fill(wall[0], null);    //填充起始行
	}
//	
//	/** 检查当前的4格方块能否继续下落 */
	public boolean tetrominoCanDrop(){
		Cell[] cells = tetromino.getCells();
		for(int i = 0; i<cells.length; i++){
			Cell cell = cells[i];
			int row = cell.getRow(); 
			int col = cell.getCol();
			if(row == ROWS-1){
				return false;
			}//到底就不能下降了
		}
		for(int i = 0; i<cells.length; i++){
			Cell cell = cells[i];
			int row = cell.getRow(); 
			int col = cell.getCol();
			if(wall[row+1][col] != null){
				return false;//下方墙上有方块就不能下降了
			}
		}
		return true;
	}
//	/** 4格方块着陆到墙上 */
	public void tetrominoLandToWall(){
		music.playLand();
		
		
		Cell[] cells = tetromino.getCells();
		for(int i=0; i<cells.length; i++){
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			wall[row][col] = cell;
		}
	}
//	
	public void moveRightAction(){
		tetromino.moveRight();
		if(outOfBound() || coincide()){  //判断越墙 、出界
			tetromino.moveLeft();
		}
	}
	public void moveLeftAction(){
		tetromino.moveLeft();
		if(outOfBound() || coincide()){
			tetromino.moveRight();
		}
	}
//	/** ... */
	private boolean outOfBound(){
		Cell[] cells = tetromino.getCells();
		for(int i=0; i<cells.length; i++){
			Cell cell = cells[i];
			int col = cell.getCol();
			if(col<0 || col>=COLS){
				return true;//出界了
			}
		}
		return false;
	}
	private boolean coincide(){
		Cell[] cells = tetromino.getCells();
		//for each 循环、迭代，简化了"数组迭代书写"
		for(Cell cell: cells){//Java 5 以后提供增强版for循环
			int row = cell.getRow();
			int col = cell.getCol();
			if(row<0 || row>=ROWS || col<0 || col>=COLS || 
					wall[row][col]!=null){
				return true; //墙上有格子对象，发生重合
			}
		}
		return false;
	} 
//	/** 向右旋转动作 */
	public void rotateRightAction(){
		//旋转之前
		//System.out.println(tetromino);
		tetromino.rotateRight();
		//System.out.println(tetromino);
		//旋转之后
		if(outOfBound() || coincide()){
			tetromino.rotateLeft();
		}
	}
//	
//	/** Tetris 类中添加的方法 */
	public void rotateLeftAction(){
		tetromino.rotateLeft();
		if(outOfBound() || coincide()){
			tetromino.rotateRight();
		}
	}
	public void hardDropAction(){
		while(tetrominoCanDrop()){
			tetromino.softDrop();
		}
		moveAction();
	}
	
	private boolean pause;
	private boolean gameOver;
	private Timer timer;
//	/** Tetris 类中添加的方法, 用于启动游戏 */

	

	
	private void clearWall(){
		//将墙的每一行的每个格子清理为null
		for(int row=0; row<ROWS; row++){
			Arrays.fill(wall[row], null);
		}
	}
//	
//	/** 在Tetris 类中添加方法  */
	public void pauseAction(){
		timer.cancel(); //停止定时器
		pause = true;
		repaint();
	}
	public void continueAction(){
		
		softDropAction();
		pause = false;
		
		repaint();
	}
	public void softDropAction(){
		
		if(score<=5){
			threeDropAction();
			
		}else if (score<=30) {
			fourDropAction();
		}else if (score<=60) {
			fiveDropAction();
		}else{
			sixDropAction();
		}
		
	}
	public void oneDropAction(){
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				moveAction();
				repaint();
			}
		}, 2000, 2000);
		grade =1;
	}
	public void twoDropAction(){
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				moveAction();
				repaint();
			}
		}, 1300, 1300);
		grade =2;
	}
	public void threeDropAction(){
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				moveAction();
				repaint();
			}
		}, 700, 700);
		grade =3;
	}
	public void fourDropAction(){
		
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				moveAction();
				repaint();
			}
		}, 500, 500);
		grade =4;
	}
	public void fiveDropAction(){
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				moveAction();
				repaint();
			}
		}, 300, 300);
		grade =5;
	}
	public void sixDropAction(){
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				moveAction();
				repaint();
			}
		}, 100, 100);
		grade =6;
	}



//	/** 在 Tetris 类中添加 方法 */
	public void checkGameOver(){
		if(wall[0][4]==null){          //第四个格子开始，所以直接检查第四个格子
			return;
		}
		gameOver = true;
		music.stopBj1();
		music.playOver();
		music.playBj2();
		timer.cancel();
		repaint();
	}


	private int used = 0;
	private int num = 0;
	public void clear(){
		
		int a = lines/2;
		if( lines>=2 && a - used >0){
			
			for(int i=ROWS-1; i>=1; i--){
				//复制 [i-1] -> [i] 
				System.arraycopy(wall[i-1], 0, wall[i], 0, COLS);//将i-1行移到i行
			}
			Arrays.fill(wall[0], null);    //填充起始行
			used++;
			num = a - used;
		}
	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Tetris tetris = new Tetris();

        

		frame.add(tetris);

		frame.setSize(525, 550);
		frame.setUndecorated(true);//去掉窗口框！
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Location 位置 RelativeTo相对于 
		frame.setLocationRelativeTo(null);//使当前窗口居中
		frame.setVisible(true);
		tetris.action();
		
	}
	
}




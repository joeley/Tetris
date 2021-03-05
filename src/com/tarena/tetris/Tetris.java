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
 * ����˹������Ϸ��� 
 *
 */
public class Tetris extends JPanel {
//	
//	/** �������䷽�� */
	private Tetromino tetromino;
//	
//	/** ��һ�����䷽�� */

	private Tetromino nextOne;
	private Tetromino nextNextOne;
//	
//	
//	/** ���� */
	public static final int ROWS = 20;
//	/** ���� */
	public static final int COLS = 10;
//	
//	
//	/** ǽ */
	private Cell[][] wall = new Cell[ROWS][COLS]; 
//	/** ���������� */
	private int lines;
	/** ���� */
	private int score;
	private int grade = 3;
	
	private Music music = new Music();
//	
	public static final int CELL_SIZE = 26;

	
	private static Image background;//����ͼƬ
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
					System.out.println("�˳���������");
					
					System.exit(0);         //�˳���ǰ��Java����
				}
				if(gameOver){               //���������¿�ʼ
					if(key==KeyEvent.VK_S){
						music.stopBj2();
						music.playBj1();
						startAction();
					}
					return;
				}
				//�����ͣ���Ұ�����[C]�ͼ�������
				if(pause){//pause = false
					if(key==KeyEvent.VK_C){	
						music.stopBj2();
						music.playStart();
						music.playBj1();
						
						continueAction();	
					}
					return;
				}
				
				//��������������
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
		System.out.println("Ԥ�������");
		
		lines = 0; 
		score = 0;	
		pause=false; 
		gameOver=false;
		

		softDropAction();
		System.out.println("ͼ�������ٶ�����");
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
				destroyLines();//�ƻ�������
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
		g.drawImage(background, 0, 0, null);//ʹ��this ��Ϊ�۲���
		g.translate(15, 15);//ƽ�ƻ�ͼ����ϵ
		paintTetromino(g);//������������ķ���
		paintWall(g);//��ǽ
		paintNextOne(g);
		paintNextNextOne(g);
		paintScore(g);
		
	}
	
	public static final int FONT_COLOR = 0x667799;
	public static final int FONT_SIZE = 0x20;
	private void paintScore(Graphics g) {
		Font f = getFont();//��ȡ��ǰ�� ���Ĭ������
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
//	//�� Tetris �� ����� ���� paintWall
	private void paintWall(Graphics g){
		for(int row=0; row<wall.length; row++){
			//����ÿһ��, i = 0 1 2 ... 19
			Cell[] line = wall[row];
			//line.length = 10
			for(int col=0; col<line.length; col++){
				Cell cell = line[col]; 
				int x = col*CELL_SIZE; 
				int y = row*CELL_SIZE;
				if(cell==null){
					g.setColor(new Color(0));
//					������ 
					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
				}else{
					g.drawImage(cell.getImage(), x-1, y-1, null);
				}
			}
		}
	}
//	/**
//	 * �� Tetris(����˹����) �������ӷ���
//	 * ��������Ĺ����ǣ�������Ķ��� ��������
//	 * ��ɹ��ܣ�����ܹ���������䣬�������½��ǽ�ϣ�
//	 *   ���µķ�����ֲ���ʼ���¡�
//	 */

	
	
	
//	/** �����Ѿ������У����ҼƷ�
//	 * 1������ÿһ�� 
//	 * 2���������飩ĳ�����Ǹ����� ���������� 
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
			if(line[i]==null){//����пո�ʽ�Ͳ�������
				return false;
			}
		}
		return true;
	}
	
	public void deleteRow(int row){
		for(int i=row; i>=1; i--){
			//���� [i-1] -> [i] 
			System.arraycopy(wall[i-1], 0, wall[i], 0, COLS);//��i-1���Ƶ�i��
		}
		Arrays.fill(wall[0], null);    //�����ʼ��
	}
//	
//	/** ��鵱ǰ��4�񷽿��ܷ�������� */
	public boolean tetrominoCanDrop(){
		Cell[] cells = tetromino.getCells();
		for(int i = 0; i<cells.length; i++){
			Cell cell = cells[i];
			int row = cell.getRow(); 
			int col = cell.getCol();
			if(row == ROWS-1){
				return false;
			}//���׾Ͳ����½���
		}
		for(int i = 0; i<cells.length; i++){
			Cell cell = cells[i];
			int row = cell.getRow(); 
			int col = cell.getCol();
			if(wall[row+1][col] != null){
				return false;//�·�ǽ���з���Ͳ����½���
			}
		}
		return true;
	}
//	/** 4�񷽿���½��ǽ�� */
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
		if(outOfBound() || coincide()){  //�ж�Խǽ ������
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
				return true;//������
			}
		}
		return false;
	}
	private boolean coincide(){
		Cell[] cells = tetromino.getCells();
		//for each ѭ��������������"���������д"
		for(Cell cell: cells){//Java 5 �Ժ��ṩ��ǿ��forѭ��
			int row = cell.getRow();
			int col = cell.getCol();
			if(row<0 || row>=ROWS || col<0 || col>=COLS || 
					wall[row][col]!=null){
				return true; //ǽ���и��Ӷ��󣬷����غ�
			}
		}
		return false;
	} 
//	/** ������ת���� */
	public void rotateRightAction(){
		//��ת֮ǰ
		//System.out.println(tetromino);
		tetromino.rotateRight();
		//System.out.println(tetromino);
		//��ת֮��
		if(outOfBound() || coincide()){
			tetromino.rotateLeft();
		}
	}
//	
//	/** Tetris ������ӵķ��� */
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
//	/** Tetris ������ӵķ���, ����������Ϸ */

	

	
	private void clearWall(){
		//��ǽ��ÿһ�е�ÿ����������Ϊnull
		for(int row=0; row<ROWS; row++){
			Arrays.fill(wall[row], null);
		}
	}
//	
//	/** ��Tetris ������ӷ���  */
	public void pauseAction(){
		timer.cancel(); //ֹͣ��ʱ��
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



//	/** �� Tetris ������� ���� */
	public void checkGameOver(){
		if(wall[0][4]==null){          //���ĸ����ӿ�ʼ������ֱ�Ӽ����ĸ�����
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
				//���� [i-1] -> [i] 
				System.arraycopy(wall[i-1], 0, wall[i], 0, COLS);//��i-1���Ƶ�i��
			}
			Arrays.fill(wall[0], null);    //�����ʼ��
			used++;
			num = a - used;
		}
	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Tetris tetris = new Tetris();

        

		frame.add(tetris);

		frame.setSize(525, 550);
		frame.setUndecorated(true);//ȥ�����ڿ�
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Location λ�� RelativeTo����� 
		frame.setLocationRelativeTo(null);//ʹ��ǰ���ھ���
		frame.setVisible(true);
		tetris.action();
		
	}
	
}




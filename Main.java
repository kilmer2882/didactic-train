package project;

import java.applet.Applet;
import java.awt.*;
import java.io.*;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;

public class Main extends Applet implements Runnable{
	private static final long serialVersionUID = 1L; //serial verison ID
	
	//boolean for the game-loop
	public static boolean isRunning = false;
	
	//dimensions
	public static int renderSize = 5; //size of pixels
	public static int width = 550; //width of screen
	public static int height = 550; //height of screen
	public static Dimension size = new Dimension(width, height); //setting screen size dimension
	
	//window and screen
	private static String name = "Testing GITHUB"; //name of window
	public static Image screen; //image for the screen
	
	//main-constructor
	public Main() {
		setPreferredSize(size);
		setFocusable(true);
	}
	
	//starting the loop
	public void start() {
		isRunning = true;
		new Thread(this).start();
	}
	
	public static void main(String args[]) {
		JFrame frame = new JFrame(); //adding JFrame
		Main component = new Main(); //creating component
		frame.add(component); //adding component
		frame.pack(); //packing screen
		//other settings
		frame.setResizable(false);
		frame.setTitle(name);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		component.start(); //starting the loop
	}
	
	//polygon class
	class polygon {
		ArrayList<Point> vertices = new ArrayList<Point>(); //ArrayList of all the vertices
		//vertices in list must be in order
	
		//constructor takes an array of Points for vertices, must be in order
		public polygon(Point[] v) {
			int size = v.length;
			for(int i = 0; i < size; i++) {
				vertices.add(v[i]); //adds the vertices to the class ArrayList
			}
		}
		
		//method for adding more vertices
		public void addVertex(Point a) {
			vertices.add(a);
		}
		
		//method for calculating the area based on the formula
		public double getArea() {
			vertices.add(vertices.get(0)); //adding the first vertex onto the end of the list of vertices
			int size = vertices.size();
			double sum = 0;
			for(int i = 1; i < size; i++) { //for loop for all the vertices
				//implementing the formula
				Point u = vertices.get(i); Point v = vertices.get(i-1);
				sum += (u.x*v.y) - (v.x*u.y);
			}
			sum = Math.abs(sum); //taking the absolute value
			sum /= 2; //dividing by 2
			vertices.remove(vertices.size()-1); //removing the added vertex
			return sum;
		}
		
		//method for drawing the polygon
		public void draw(Graphics g, Color color) {
			g.setColor(color); 
			int size = vertices.size();
			vertices.add(vertices.get(0)); //adding the first vertex onto the end of the list of vertices
			for(int i = 0; i < size; i++) { //for loop for all the vertices
				try {
					//drawing the sides of the polygon
					g.drawLine(vertices.get(i).x, vertices.get(i).y, vertices.get(i+1).x, vertices.get(i+1).y);
				} catch(Exception e) { }
			}
			vertices.remove(vertices.size()-1); //removing the added vertex
			
			//drawing the area of the polygon onto the screen
			g.drawString("" + getArea(), vertices.get(0).x, vertices.get(0).y);
		}
	}

	public void render() {
		Graphics g = screen.getGraphics(); //establishing the graphics
		
		//three-sided polygon (n=3)
		Point[] three_points = {new Point (20, 20), new Point (30, 30), new Point (10, 30)};
		polygon three_sided_polygon = new polygon(three_points);
		three_sided_polygon.draw(g, Color.green);
		
		//four-sided polygon (n=4)
		Point[] four_points = {new Point (0, 50), new Point (10, 50), new Point (10, 60), new Point (0, 60)};
		polygon four_sided_polygon = new polygon(four_points);
		four_sided_polygon.draw(g, Color.red);
		
		//five-sided polygon (n=5)
		Point[] five_points = {new Point (20, 70), new Point (40, 70), new Point (40, 90), new Point (30, 95), new Point(20, 90)};
		polygon five_sided_polygon = new polygon(five_points);
		five_sided_polygon.draw(g, Color.blue);
		
		Point[] six_points = {new Point (70, 15), new Point (80, 45), new Point (90, 55), new Point (80, 65), new Point (60, 65), new Point (60, 45)};
		polygon six_sided_polygon = new polygon(six_points);
		six_sided_polygon.draw(g, Color.black);
		
		g = getGraphics(); //retaking the graphics
		//drawing the screen
		g.drawImage(screen, 0, 0, size.width, size.height, 0, 0, size.width / renderSize, size.height / renderSize, null);
		g.dispose(); //disposing of the graphics
	}
	
	//game-loop method
	public void run() {
		//Creating the volatile image
		screen = createVolatileImage(size.width / renderSize, size.height / renderSize);
		
		while(isRunning) { //while loop for when the game is running
			render(); //rendering the polygons
			try {
				Thread.sleep(5); //letting the thread sleep
			} catch(Exception e) { System.out.println(e); }
		}
	}
}

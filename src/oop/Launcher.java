package oop;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	//Constructor
	public Launcher(){
		super();
	}
	
	public void launch(String document1, String document2, int shingleSize) throws InterruptedException {
		//Blocking Queue of Shingles
		BlockingQueue<Shingle> queue = new LinkedBlockingQueue<>();

		//Threads
		Thread t1 = new Thread(new FileParser(queue, document1, shingleSize, 1), "T1");	// file reading
		Thread t2 = new Thread(new FileParser(queue, document2, shingleSize, 2), "T2");	// file reading
		Thread t3 = new Thread(new Consumer(queue, shingleSize), "T3");	// Consumer thread
		
		//Start threads
		t1.start();
		t2.start();
		t3.start();
		
		//Join on threads
		t1.join();
		t2.join();
		t3.join();
		
	}//Launch
}//Launcher

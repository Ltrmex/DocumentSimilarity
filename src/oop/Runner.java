package oop;

import java.util.Scanner;

public class Runner {
	public static void main(String[] args) throws InterruptedException {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);	// import scanner

		//Menu UI
		System.out.println("*** Document Comparison Service ***");
		System.out.print("\tEnter File Name / URL 1: ");
		String document1 = in.next();	// first file / URL
				
		System.out.print("\tEnter File Name / URL 2: ");
		String document2 = in.next();	// second file / URL
		
		System.out.println("\n...processing...please wait...\n");	// message while threads work
		
		Launcher runThreads = new Launcher();
		runThreads.launch(document1, document2, 4);	// launch threads
		
	}//main()
}//Runner

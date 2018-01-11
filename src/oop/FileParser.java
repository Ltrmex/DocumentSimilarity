package oop;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable {	// implement Runnable
	//Variables
	private BlockingQueue<Shingle> queue;	//queue
	private Deque<String> buffer = new LinkedList<>();	//buffer
	private String fileName;	//name of the file
	private int shingleSize;	//shingles size
	private int docId;	//document ID
	BufferedReader br = null;	// reader

	//Constructors
	public FileParser() {}
	
	public FileParser(BlockingQueue<Shingle> queue, String fileName, int shingleSize, int docId) {
		super();
		this.queue = queue;
		this.fileName = fileName;
		this.shingleSize = shingleSize;
		this.docId = docId;
	}//FileParser

	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));	//file name
			String line = null;	// to temporarily store line when read
			
			while ((line = br.readLine()) != null) {	// read line by line
					line = line.toLowerCase();	// everything to lower case
					String[] words = line.split(" ");	// split by spaces
					addToBuffer(words);	// add array of words to buffer
			}//while

			//Loop through until empty
			do {
				Shingle shingle = getNextShingle();
				
				if (!(shingle == null))	// if shingle not null add to the queue
					queue.put(shingle);	// shingle being added
				
			}while (buffer.size() != 0);	// while buffer not empty


			flushBuffer();	// flush buffer
			br.close();	// close reader
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}//run()

	//Function which adds words to the buffer
	private void addToBuffer(String[] words) {
		for (String s : words)
			buffer.add(s);
	}//addToBuffer()

	private Shingle getNextShingle() {
		//Variables
		StringBuffer sb = new StringBuffer();	// string buffer
		int counter = 0;	// counter

		do {
			if (buffer.peek() != null) {	// if buffer not null
				sb.append(buffer.poll());	// append
				counter++;	// increment counter
			}
			else
				shingleSize = counter;
		}while (counter < shingleSize);	// do while counter smaller than shingleSize

		if (sb.length() > 0) {	// if string buffer bigger than 0
			return (new Shingle(docId, sb.toString().hashCode()));	// new shingle object
		} else {
			return (null);	// else return null
		}
	}//getNextShingle()
	
	// Flush Buffer - left overs added to last shingle
	private void flushBuffer() throws InterruptedException {
		do {
			Shingle s = getNextShingle();
			
			if (s != null)
				queue.put(s);
		}while (buffer.size() > 0);
		
		queue.put(new Poison(0, 0));
	}//flushBuffer();

}//FileParser

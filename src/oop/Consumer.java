package oop;

import java.util.*;
import java.util.concurrent.*;

public class Consumer implements Runnable {	// implement Runnable
	//Variables
	private BlockingQueue<Shingle> queue;
	private Map<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService threadPool;
	private Set<Integer> minHash;
	private int k;

	//Constructor
	public Consumer(BlockingQueue<Shingle> queue, int k) {
		this.queue = queue;
		this.k = k;
		threadPool = Executors.newFixedThreadPool(k);
		
	}//Consumer()
	
	//MinHash function
	public synchronized int minHash(Shingle s) {
		int minValue = Integer.MAX_VALUE;
		
		for (Integer hash : minHash) {
			//XOR the hashCode
			int minHashed = s.getHashCode() ^ hash;
			
			if (minHashed < minValue)
				minValue = minHashed;
		}//for
		return minValue;
	}

	public void run() {
		//Variables
		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		List<Integer> voidList = new ArrayList<>();
		int count = 2;
		
		minHash = new TreeSet<Integer>();

		//Random number for MinHash
		Random randomNum = new Random();

		for (int i = 0; i < k; i++)
			minHash.add(randomNum.nextInt());

		do {
				Shingle shingle;
				
				try {
					shingle = queue.take();
				
				if (shingle instanceof Poison == false) {
					threadPool.execute(new Runnable() {
						public void run() {
							if (shingle.getDocId() == 1)
								list1.add(minHash(shingle));
							else if (shingle.getDocId() == 2)
								list2.add(minHash(shingle));
							else
								voidList.add(minHash(shingle));
						}
					});// Runnable
				} 
				else
					--count;
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

		}while (count > 0);

		terminate(threadPool);	// terminate
		
		//Two lists put into a map
		map.put(1, list1);
		map.put(2, list2);
		
		//Getting the intersection or similarities between two lists
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));

		double jaccard = calculateSimilarities(intersection.size(), (list1.size() + list2.size()));

		System.out.println("*************************************");
		System.out.printf("Similarity: %.2f%s\n", jaccard, "%");
		System.out.println("*************************************");
	}//run()

	// To calculate Jaccard Index percentage:
	// (amount of similarities / size of both lists) * 100
	public double calculateSimilarities(int amountOfSimilarities, int sizeOfLists) {
		return (((double) amountOfSimilarities) / ((double) sizeOfLists)) * 100;
	}//calculateSimilarities()
	
	//Shut down executor 
	public void terminate(ExecutorService threadPool) {
		threadPool.shutdown(); //disable tasks
		
		try {
			//Wait for tasks to terminate
			if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
				threadPool.shutdownNow(); // cancel currently executing tasks
				
				//Wait for response
				if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}//if
		} catch (InterruptedException e) {
			threadPool.shutdownNow();	//recancel if current thread interrupted
			Thread.currentThread().interrupt();	//interrupt status
		}//catch
	}//terminate()

}//Consumer

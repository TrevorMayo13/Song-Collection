package student;
import java.io.*;
import java.util.*;

import student.Song.CmpArtist;

/*
 * SearchByArtistPrefix.java
 * starting code
 * Boothe 2016
 */
public class SearchByTitlePrefix {

	private Song[] songs;// The constructor fetches and saves a reference to the song array here
	static int addenCounter;
	static int mergeCounter;
	static int searchCounter;
	static int counter = 0;
	private Comparator<Song> comp = new Song.CmpTitle();
	AddendumList<Song> adden = new AddendumList(comp);
	
	public SearchByTitlePrefix(SongCollection sc) {
		songs = sc.getAllSongs();
		
		CmpCnt.cmpCnt = 0;
		
		for (int i = 0; i < songs.length; i++)
		{
			adden.add(songs[i]);
		}
		
		addenCounter = CmpCnt.cmpCnt;
		
		CmpCnt.cmpCnt = 0;
		
		//adden.mergeAllLevels();
		
		mergeCounter = CmpCnt.cmpCnt;
	}

	/**
	 * find all songs matching artist prefix
	 * uses binary search
	 * should operate in time log n + k (# matches)
	 */
	public Song[] search(String titlePrefix) {
		
		
		AddendumList temp = new AddendumList(comp);
		
		char lastChar = titlePrefix.charAt(titlePrefix.length()-1);
		lastChar+=1;
		String endPre = titlePrefix.substring(0, titlePrefix.length()-1) + lastChar;
		
		Song s1 = new Song("", titlePrefix, "", 0);
		Song s2 = new Song("", endPre, "", 0);
		
		CmpCnt.cmpCnt = 0;		
		
		temp = adden.subList(s1, s2);
		
		//get the counter from CmpCnt
		searchCounter = CmpCnt.cmpCnt;
		
		Song[] sub = new Song[temp.size];
		
		temp.toArray(sub);
		
		System.out.println("Number of comparisons done to build AddendumList: " + addenCounter);
		System.out.println("Binary search comparisons (including mergeAll comparisons): " + searchCounter);
		
		
		return sub;
	}
	
	public static void printFirst10Songs(Song[] arr)
	{
		//if the there is less than 10 songs print out all of them
		int total = arr.length;
		int len = 0;
		if(total <= 10)
		{
			len = total;
		}
		else
		{
			len = 10;
		}
		
		if (total == 0)
		{
			System.out.println("No songs found");
		}
		else
		{
			System.out.println("Total songs = " + total + " found, first " + len + " songs:");
			
			for (int i = 0; i < len; i++)
			{
				System.out.println(arr[i].toString());
			}
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByTitlePrefix sbap = new SearchByTitlePrefix(sc);

		if (args.length > 1){
			System.out.println("Searching for: "+args[1]);
			Song[] byTitleResult = sbap.search(args[1]);

			// to do: show first 10 matches
			printFirst10Songs(byTitleResult);
			
			//print out the comparisons
			System.out.println();
			System.out.println("Number of comparisons done to build AddendumList: " + addenCounter);
			System.out.println("Binary search comparisons (including mergeAll comparisons): " + searchCounter);
		}
	}
}

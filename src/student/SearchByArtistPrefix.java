package student;
import java.io.*;
import java.util.*;

import student.Song.CmpArtist;

/*
 * SearchByArtistPrefix.java
 * starting code
 * Boothe 2016
 */
public class SearchByArtistPrefix {

	private Song[] songs;  // The constructor fetches and saves a reference to the song array here
	static int CmpCounter;
	static int counter = 0;

	public SearchByArtistPrefix(SongCollection sc) {
		songs = sc.getAllSongs();
	}

	/**
	 * find all songs matching artist prefix
	 * uses binary search
	 * should operate in time log n + k (# matches)
	 */
	public Song[] search(String artistPrefix) {
		// write this method
		
		//create temp array and arraylist
		Song[] arr;
		ArrayList<Song> search = new ArrayList<Song>();
		
		//use binary search to find a match
		int index = Arrays.binarySearch(songs, new Song(artistPrefix, "", "", 0));
		index = index * -1;
		
		//get the counter from CmpCnt
		CmpCounter = CmpCnt.cmpCnt;
		
		String artPre = artistPrefix.toLowerCase();
		
		//Set up loop to find the first match
		int first = 0;
		for (int i = index; i >= 0; i--)
		{
			Song temp = songs[i];
			String tempStr = temp.getArtist().toLowerCase();
			if (tempStr.indexOf(artPre) != 0)
			{
				first = i + 1;
				break;
			}
			counter++;
		}
		
		//after the first match is found loop through the list to make an array of all matches
		for (int i = first; i < songs.length; i++)
		{
			Song temp = songs[i];
			String temp2 = temp.getArtist().toLowerCase();
			
			if (temp2.indexOf(artPre) == 0)
			{
				search.add(temp);
			}
			else
			{
				break;
			}
			counter++;
		}
		
		//convert the arraylist into an array
		arr = search.toArray(new Song[search.size()]);
		return arr;
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
		SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

		if (args.length > 1){
			System.out.println("searching for: "+args[1]);
			Song[] byArtistResult = sbap.search(args[1]);

			// to do: show first 10 matches
			printFirst10Songs(byArtistResult);
			
			//print out the comparisons
			System.out.println();
			System.out.println("Binary search comparisons: " + CmpCounter);
			System.out.println("Number of comparisons done to match all songs: " + counter);
		}
	}
}

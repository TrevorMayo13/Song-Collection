package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/*
 * SongCollection.java
 * Read the specified data file and build an array of songs.
 * 
 * Starting code by Prof. Boothe 2016
 */
public class SongCollection {

	//setup variables needed for the script
	private static Song[] songs;
	static int total;

	public SongCollection(String filename) throws FileNotFoundException {

		// read in the song file and build the songs array
		Scanner scanner = new Scanner(new File(filename));
		
		//create arraylist for holding Songs
		ArrayList<Song> arr = new ArrayList<Song>();
		
		//setup variables needed for while loop
		String artist = "";
		String title = "";
		String lyrics = "";
		String temp = "";
		int index;
		
		while (scanner.hasNext())
		{
			//break out the song author
			temp = scanner.nextLine();
			index = temp.indexOf("\"") + 1;
			artist = temp.substring(index, temp.length()-1);
			
			//System.out.println(artist);
			
			//break out the song title
			temp = scanner.nextLine();
			index = temp.indexOf("\"") + 1;
			title = temp.substring(index, temp.length()-1);
			
			//System.out.println(title);
			
			//break out the songs first lyric
			temp = scanner.nextLine();
			index = temp.indexOf("\"") + 1;
			lyrics = temp.substring(index) + "\n";
			
			//set up while loop to find the rest of the lyrics for the song
			while (true)
			{
				temp = scanner.nextLine();
				index = temp.indexOf("\"");
				
				//if the line contains a " then exit the loop
				if (index > -1)
				{
					break;
				}
				else
				{
					lyrics = lyrics + temp + "\n";
					
				}
				
			}
			
			//create the song and add it to the arraylist
			arr.add(new Song(artist, title, lyrics, 0));
			
			//reset the variables to blank
			artist = "";
			title = "";
			lyrics = "";
		}
		
		//turn arraylist into a array for sorting
		songs = arr.toArray(new Song[arr.size()]);
		
		// sort the songs array
		Arrays.sort(songs);
		
		//set the total songs variable
		total = songs.length;
		
		CmpCnt.resetCmpCnt();
		
	}
	
	public static void printFirst10Songs()
	{
		//if the there is less than 10 songs print out all of them
		int len = 0;
		if(total <= 10)
		{
			len = total;
		}
		else
		{
			len = 10;
		}
		
		System.out.println("Total songs = " + total + ", first " + len + " songs:");
		
		for (int i = 0; i < len; i++)
		{
			System.out.println(songs[i].toString());
		}
	}

	// returns the array of all Songs
	// this is used as the data source for building other data structures
	public Song[] getAllSongs() {
		return songs;
	}



	// testing method
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 0) {
			System.err.println("usage: prog songfile");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);

		// todo: show song count and first 10 songs (name & title only, 1 per line)
		printFirst10Songs();
	}
}

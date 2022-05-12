package student;
import java.io.*;
import java.util.*;

import student.Song.CmpArtist;

/*
 * SearchByArtistPrefix.java
 * starting code
 * Boothe 2016
 */
public class SearchByLyricsWords 
{

	private Song[] songs; // The constructor fetches and saves a reference to the song array here
	static TreeMap<String,TreeSet<Song>> map = new TreeMap<String,TreeSet<Song>>();
	String commonWords = "the of and a to in is you that it he for was on are as with his they at be this from i have or by one had not but what all were when we there can an your which their if do will each how them then she many some so these would into has more her two him see could no make than been its now my made did get our me too";
	
	public SearchByLyricsWords(SongCollection sc) 
	{
		//Get all of the songs from song collections
		songs = sc.getAllSongs();
		
		//Created an array to search because commonWords.contains would fail if a string like "thi" is used.
		String[] common = commonWords.split("[^a-zA-Z]+");
		Arrays.sort(common);
		
		//Start looping through each song
		for (int i = 0; i < songs.length; i++)
		{
			//Get the lyrics and split them into an array
			String lyrics = songs[i].getLyrics();
			String[] arr = lyrics.split("[^a-zA-Z]+");
			
			//Loop through the lyrics of the song
			for (int j = 0; j < arr.length; j++)
			{
				//Grab the word from the lyrics
				String word = arr[j].toLowerCase();
				
				//Test to see if the word is blank, a common word or if it already exists in the treeMap
				if (word != "" && word.length() > 1)
				{
					//Searched the array to find out if the word is a common word
					int binary = Arrays.binarySearch(common, word);
					if (binary < -1)
					{
						if (map.containsKey(word) == false)
						{
							TreeSet<Song> set = new TreeSet<Song>();
							set.add(songs[i]);
							map.put(word, set);
						}
						else
						{
							//get all of the songs related to the word already and update it with the new one
							TreeSet<Song> set = new TreeSet<Song>();
							set = map.get(word);
							set.add(songs[i]);
							map.put(word, set);
						}
					}
				}
			}
		}
	}
	
	public static void statistics()
	{
		//Get the size of TreeSet
		int mapSize = map.size();
		
		//Iterate through the map and collect all of the songs
		int valueSize = 0;
		
		for (Map.Entry<String,TreeSet<Song>> entry : map.entrySet()) 
		{
			TreeSet<Song> value = entry.getValue();
			valueSize = valueSize + value.size();
			
		}
		
		//print out the comparisons
		//System.out.println();
		System.out.println("Number of keys in TreeMap: " + mapSize);
		System.out.println("Size of TreeMap (5N): " + (5*mapSize));
		System.out.println("Songs Stored in TreeMap: " + valueSize);
		System.out.println("Size of all TreeSets (4N): " + (4*valueSize));
		System.out.println("Total space used (keys + values): " + (valueSize + mapSize));
		System.out.println("Total size of structure (5N + 4S): " + (4*valueSize + 5*mapSize));
		
	}
	
	public Song[] search(String searchStr) 
	{
		//Turn search string into array of words
		String[] searchWords = searchStr.split("[^a-zA-Z]+");
		
		//Create a treeSet to hold the songs after searching the word
		TreeSet<Song> search = new TreeSet<Song>();
		int first = 0;
		
		//loop through the input lyrics
		for (int i = 0; i < searchWords.length; i++)
		{
			//Get the word and make it lower case
			String word = searchWords[i].toLowerCase();
			
			//If that word is not blank and is more than 1 letter
			if (word != "" && word.length() > 1)
			{
				//See if that word is in the TreeMap
				if (map.containsKey(word))
				{
					//Get the Songs for that word
					TreeSet<Song> values = map.get(word);
					
					//If the set is empty add all songs from the first word
					if (search.size() == 0 && first == 0)
					{
						search.addAll(values);
						first = 1;
					}
					else if (search.size() == 0 && first == 1)
					{
						return null;
					}
					//else use retainAll to get only the common songs
					else
					{
						search.retainAll(values);
					}
				}
			}
		}
		
		//Create a array of songs to hold the TreeSet
		Song[] arr = new Song[search.size()];
		search.toArray(arr);
		
		//return array
		return arr;
	}
	
	public static void printFirst10Songs(Song[] arr)
	{
		if (arr == null)
		{
			System.out.println("No songs found");
		}
		else
		{
			//if the there is less than 10 songs print out all of them
			int total = arr.length;
			int len;
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
	}

	public static void main(String[] args) throws IOException
	{
		if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByLyricsWords sblw = new SearchByLyricsWords(sc);
		
		//statistics();

		if (args.length > 1){
			System.out.println("Searching for: "+args[1]);
			Song[] byLyricResult = sblw.search(args[1]);
			
			// to do: show first 10 matches
			printFirst10Songs(byLyricResult);
			
		}
	}
}

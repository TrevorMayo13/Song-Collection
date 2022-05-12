package student;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhraseRanking {
	
	private static Song[] songs;  // The constructor fetches and saves a reference to the song array here
	static String[] rankedSongs;
	
	
	public PhraseRanking(SongCollection sc) {
		songs = sc.getAllSongs();
	}


	static int rankPhrase(String lyrics, String lyricsPhrase)
	{
		//turn everything into lower case
		lyrics = lyrics.toLowerCase();
		lyricsPhrase = lyricsPhrase.toLowerCase();
		
		//check to see if the phrase is already in the lyrics
		if (lyrics.contains(lyricsPhrase) != false)
		{
			return lyricsPhrase.length();
		}
		//if the phrase is not directly in the lyrics find the best ranking string
		else
		{
			//Split the lyricPhrase into an array and the lyrics into a list
			String[] arr = lyricsPhrase.split("[^a-zA-Z]+");
	
			//Create a new List for holding the end result
			String phrase = "";
			
			//Booleans
			boolean fail = false;
			
			//Variables
			int pos = 0;
			int last = lyrics.length();
			
			//Loop forever until exited
			for (;;)
			{	
				int start = 0;
				int end = 0;
				
				//Loop through all of the other words in the phrase
				for (int i = 0; i < arr.length; i++)
				{
					String word = arr[i];
					int index = searchWord(word, lyrics, pos);
					
					if (index == -1)
					{
						fail = true;
						break;
					}
					
					else
					{
						if (i == 0)
							start = index;
						if (i == arr.length - 1)
							end = index + arr[i].length();
						
						pos = index;
					}
				}
				
				if (fail == true)
					break;
				
				else
				{
					int temp = start;
					for (;;)
					{
						int ind = searchWord(arr[0], lyrics, start + arr[0].length());
						int ind2 = searchWord(arr[1], lyrics, temp);
						if (ind == -1 || ind2 == -1)
							break;
						else if (ind < ind2)
							start = ind;
						else
							break;
					}
					
					int min = end - start;
					if(min < last)
						last = min;
				}
			}
			
			if (last == lyrics.length())
				return -1;
			
			return last;
		}
	}
	
	private static int searchWord(String word, String lyrics, int pos)
	{
		for(;;)
		{
			//Check to see if the next word in the phrase is in the lyrics
			int index = lyrics.indexOf(word, pos);
			
			//If the word is not in the lyrics then exit
			if (index == -1)
				return -1;
			
			//Check to see if the word is not part of another word
			if (index > 0 && Character.isLetter(lyrics.charAt(index - 1)))
			{
				pos = index + 1;
				continue;
				
			}
			
			int end = index + word.length();
			if(end < lyrics.length() && Character.isLetter(lyrics.charAt(end)))
			{
				pos = end;
				continue;
			}
			
			return index;
		}
	}
	
	public static void printFirst10Songs(String[] arr)
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
					System.out.println(arr[i]);
				}
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		if (args.length == 0)
		{
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		PhraseRanking pr = new PhraseRanking(sc);

		if (args.length > 1)
		{
			String phrase = args[1];
			
			System.out.println("searching for: "+ phrase);
			
			ArrayList<String> arr = new ArrayList<String>();
			
			for (int i = 0; i < songs.length; i++)
			{
				String lyrics = songs[i].getLyrics();

				int rank = rankPhrase(lyrics, phrase);
				
				if (rank != -1)
				{
					String song = songs[i].toString();
					arr.add(rank + " " + song);
				}
			}
			
			rankedSongs = new String[arr.size()];
			arr.toArray(rankedSongs);
			

			// to do: show first 10 matches
			printFirst10Songs(rankedSongs);
		}

	}

}

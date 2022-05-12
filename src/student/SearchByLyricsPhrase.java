package student;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SearchByLyricsPhrase {
	
	private static Song[] songs;  // The constructor fetches and saves a reference to the song array here
	private static Comparator<Song> comp = new Song.CmpRanked(); //Comparator to compare rank in songs
	
	
	public SearchByLyricsPhrase(SongCollection sc) {
		songs = sc.getAllSongs();
	}

	public static Song[] search(String searchStr) 
	{
		
		ArrayList<Song> arr = new ArrayList<Song>();
		
		for (int i = 0; i < songs.length; i++)
		{
			String lyrics = songs[i].getLyrics();

			int rank = rankPhrase(lyrics, searchStr);
			
			if (rank != -1)
			{
				songs[i].Rank = rank;
				arr.add(songs[i]);
			}
		}
		
		Song[] ranked = new Song[arr.size()];
		arr.toArray(ranked);
		Arrays.sort(ranked, comp);
		
		return ranked;
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
					//Get the word
					String word = arr[i];
					
					//Search the lyrics to find the word using a position
					int index = searchWord(word, lyrics, pos);
					
					//If the world is not found break out of the loop
					if (index == -1)
					{
						fail = true;
						break;
					}
					
					//Else update the pos, start and end
					else
					{
						if (i == 0)
							start = index;
						if (i == arr.length - 1)
							end = index + arr[i].length();
						
						pos = index + 1;
					}
				}
				
				//If a word was not found break out to return statments
				if (fail == true)
					break;
				
				//Find the smallest rank
				else
				{
					//Set up a temp to hold start variable for looping and start a forever loop
					int temp = start;
					for (;;)
					{
						//Search for the first word after the first occurnce and test to make sure its before the second word, to get smallest rank
						int ind = searchWord(arr[0], lyrics, start + arr[0].length());
						int ind2 = searchWord(arr[1], lyrics, temp);
						
						//If the word is not found break out
						if (ind == -1 || ind2 == -1)
							break;
						
						//Else make sure the first word is before the second
						else if (ind < ind2)
							start = ind;
						
						//Else break out
						else
							break;
					}
					
					//Test to see if this occurnce is smaller than the last occurence.
					int min = end - start;
					if(min < last)
						last = min;
				}
			}
			
			//If last never changed then return -1
			if (last == lyrics.length())
				return -1;
			
			//Return smallest rank
			return last;
		}
	}
	
	//Search for the word in the lyrics with a posistion
	private static int searchWord(String word, String lyrics, int pos)
	{
		for(;;)
		{
			//Check to see if the next word in the phrase is in the lyrics
			int index = lyrics.indexOf(word, pos);
			
			//If the word is not in the lyrics then exit
			if (index == -1)
				return -1;
			
			//Check to see if the spot before the word is a letter or not
			if (index > 0 && Character.isLetter(lyrics.charAt(index - 1)))
			{
				//Update pos for next start
				pos = index + 1;
				continue;
			}
			
			//Check to see if the spot after the word is a letter or not
			int end = index + word.length();
			if(end < lyrics.length() && Character.isLetter(lyrics.charAt(end)))
			{
				//Update pos for next start
				pos = end;
				continue;
			}
			
			//Return start of the word
			return index;
		}
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
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		if (args.length == 0)
		{
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByLyricsPhrase sblp = new SearchByLyricsPhrase(sc);

		if (args.length > 1)
		{
			String phrase = args[1];
			
			System.out.println("searching for: "+ phrase);
			Song[] search = search(phrase);

			// to do: show first 10 matches
			printFirst10Songs(search);
		}

	}

}
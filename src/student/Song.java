package student;

import java.util.*;

/*
 * Song objects contain Strings for a song's artist, title, and lyrics
 * 
 * Starting code by Prof. Boothe 2016
 */

public class Song implements Comparable<Song> {
	// fields
	String Artist;
	String Title;
	String Lyrics;
	int Rank;
   
   // constructor
   public Song(String artist, String title, String lyrics, int rank) {
	   Artist = artist;
	   Title = title;
	   Lyrics = lyrics;
	   Rank = rank;
   }

   //get the songs artist
   public String getArtist() {
	   return Artist;
   }

   //get the song lyrics
   public String getLyrics() {
	   return Lyrics;
   }

   //get the song title
   public String getTitle() {
	   return Title;
   }
   
   //get the song rank
   public int getRank() {
	   return Rank;
   }

   //get the artist and song title
   public String toStr() {
	   String str = Artist + ", \"" + Title + "\"";
	   return str;
   }
   
 //get the artist and song title
   public String toString() {
	   String str = Rank + " " + Artist + ", \"" + Title + "\"";
	   return str;
   }
   
   
   public static class CmpArtist extends CmpCnt implements Comparator<Song> {
	   public int compare(Song s1, Song s2) {
		   
		   //Update the counter
		   cmpCnt++;
		   
		   //Get the two artists
		   String s1Art = s1.getArtist();
		   String s2Art = s2.getArtist();
		   
		   //compare
		   int compareArt = s1Art.compareToIgnoreCase(s2Art);
		   
		   //return the compare
		   return compareArt;
	   }
	   
   }
   
   public static class CmpTitle extends CmpCnt implements Comparator<Song> {
	   
	   public int compare(Song s1, Song s2)
	   {
		   //Update the counter
		   cmpCnt++;
		   
		   //Get the two artists
		   String s1T = s1.getTitle();
		   String s2T = s2.getTitle();
		   
		   //compare
		   int compareTitle = s1T.compareToIgnoreCase(s2T);
		   
		 //return the compare
		   return compareTitle;
	   }
	   
   }

   /* 
    * the default comparison of songs
    * primary key: artist, secondary key: title
    * used for sorting and searching the song array
    * if two songs have the same artist and title they are considered the same
    */
   public int compareTo(Song song2) {
	   
	   CmpCnt.cmpCnt++;
	   
	   //get the artists and the title of song2
	   String art = song2.getArtist();
	   String title = song2.getTitle();
	   
	   //compare the artists
	   int i = this.Artist.compareToIgnoreCase(art);
	   
	   //if the artists are the same compare the titles
	   if (i == 0)
	   {
		   return this.Title.compareToIgnoreCase(title);
	   }
	   else
	   {
		   return i;
	   }
   }
   
   public static class CmpRanked extends CmpCnt implements Comparator<Song> {
	   //Compare the ranks
	   public int compare(Song s1, Song s2) {
		   
		   //get the rank of the song
		   int r1 = s1.getRank();
		   int r2 = s2.getRank();
		   
		   //compare the ranks
		   return r1 - r2;
	   }
   }

   // testing method to test this class
   public static void main(String[] args) {
      Song s1 = new Song("Professor B",
            "Small Steps",
            "Write your programs in small steps\n"+
            "small steps, small steps\n"+
            "Write your programs in small steps\n"+
            "Test and debug every step of the way.\n", 0);

      Song s2 = new Song("Brian Dill",
            "Ode to Bobby B",
            "Professor Bobby B., can't you see,\n"+
            "sometimes your data structures mystify me,\n"+
            "the biggest algorithm pro since Donald Knuth,\n"+
            "here he is, he's Robert Boothe!\n", 0);

      Song s3 = new Song("Professor B",
            "Debugger Love",
            "I didn't used to like her\n"+
            "I stuck with what I knew\n"+
            "She was waiting there to help me,\n"+
            "but I always thought print would do\n\n"+
            "Debugger love .........\n"+
            "Now I'm so in love with you\n", 0);

      System.out.println("testing getArtist: " + s1.getArtist());
      System.out.println("testing getTitle: " + s1.getTitle());
      System.out.println("testing getLyrics:\n" + s1.getLyrics());
    
      System.out.println("testing toString:\n");
      System.out.println("Song 1: " + s1);
      System.out.println("Song 2: " + s2);
      System.out.println("Song 3: " + s3);

      System.out.println("testing compareTo:");
      System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
      System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
      System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
      System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
      System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
      
      /*int i = CmpArtist.compare(s1, s2);
      
      System.out.println("testing compare:");
      System.out.println("Song1 vs Song2 = " + CmpArtist.compare(s1, s2));
      System.out.println("Song2 vs Song1 = " + CmpArtist.compare(s2, s1));
      System.out.println("Song1 vs Song3 = " + CmpArtist.compare(s1, s3));
      System.out.println("Song3 vs Song1 = " + CmpArtist.compare(s3, s1));
      System.out.println("Song1 vs Song1 = " + CmpArtist.compare(s1, s1));*/
   }
}

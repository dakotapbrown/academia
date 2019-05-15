import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * This class provides functionality to the {@code SongReader} class by 
 * providing a way to complete tasks with the information that can be 
 * extracted by the {@code SongReader} class.
 * 
 * @author Dakota Brown
 * @version CMSC 256-001, Spring 2018
 * @since April 21, 2018
 */
public final class MusicManager {
	private static SongReader reader;

	private MusicManager() {}
	
	public static void main(String... args) {
		
		printHeading();
		
		// Initialize the SongReader object first; worry about 
		// other args later 
		reader = new SongReader();
		boolean readerInitialized = false;
		do {
			try {
				if (args.length > 0) {
					reader.setFileReader(args[0]);
				} else {
					usage();
					args = new String[1];
					args[0] = SongReader.promptForFileName();
					reader.setFileReader(args[0]);
				}
				readerInitialized = true;
			} catch (FileNotFoundException fnfe) {
				// keep prompting for a proper filename
				System.out.println("File not found: please choose a"
						+ " valid file\n");
				if (args.length == 0) {
					args = new String[1];
				}
				args[0] = SongReader.promptForFileName();
			}
		} while (!readerInitialized);
		
		// definitely needs second option to continue
		if (args.length == 1) {
			args = Arrays.copyOf(args, 2);
			usage();
			args[1] = promptForSecondOption(Double.NaN);
		} // else args[1] already exists
		
		// determine if option is valid
		int option = 0;
		boolean optionValid = false;
		do {
			try {
				option = Integer.parseInt(args[1]);
				optionValid = true;
				
				switch (option) {
				case 1: optionOne();
					break;
				case 2: optionTwo(args[2]);
					break;
				case 3: optionThree(args[2]);
					break;
				default: 
					optionValid = false;
					usage();
					args[1] = promptForSecondOption(option);
					break; 
				}				
			} catch (NumberFormatException nfe) {
				usage();
				args[1] = promptForSecondOption(option);
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				// ask for third argument because one wasn't found
				// copy args into new args array with length three
				
				usage();
				optionValid = false;
				args = Arrays.copyOf(args, 3);
				args[2] = promptForThirdOption();
			}
		} while (!optionValid);
	}
	
	/**
	 * Prints a message for the user that describes the command line usage
	 * of this class.
	 */
	private static void usage() {

		System.out.println("Usage: java MusicManager <filename> <option>"
				+ " <artist name>");
		System.out.println("\nwhere <filename> contains readable songs in XML "
				+ "format,\n<option> is an integer value between 1-3,"
				+ "\nand <artist name> is an artist name\n");
	}

	/**
	 * Executes the function related to the user choosing option one. Displays
	 * a Top 10 playlist of songs via the console.
	 */
	private static void optionOne() {
		List<MySong> playlist = reader.run();
		
		Collections.sort(playlist,
						 (c1, c2) -> { 
							 int i = c1.getPlaycount();
							 int j = c2.getPlaycount();
							 return (i > j) ? -1 : ((i == j) ? 0 : 1);
						 });
		
		Iterator<MySong> itr = playlist.iterator();
		
		System.out.println("\nTop 10 Playlist:\n");
		if (playlist.size() < 10) {
			for (int i = 0; i < playlist.size(); i++) {
				System.out.println(itr.next().toString());
			}
		} else {
			for (int i = 0; i < 10; i++) {
				System.out.println(itr.next().toString());
			}
		}
	}

	/**
	 * Executes the function related to the user choosing option two. Informs 
	 * the user of whether or not a particular artist is present in the current
	 * playlist
	 * @param artistName The artist to check for
	 */
	private static void optionTwo(String artistName) {
		List<MySong> playlist = reader.run();
		
		Iterator<MySong> itr = playlist.iterator();
		while (itr.hasNext()) {
			boolean found = itr.next().getArtist().equalsIgnoreCase(artistName);
			if (found) {
				System.out.println("\n" + artistName + " is included in the playlist.");
				return;
			}
		}
		
		System.out.println("\n" + artistName + " is not included in the playlist.");
	}

	/**
	 * Executes the function related to the user choosing option three. 
	 * Displays all songs by specified artist, grouped by album and sorted by
	 * title
	 * @param artistName The artist whose songs to display
	 */
	private static void optionThree(String artistName) {
		List<MySong> playlist = reader.run();
		List<MySong> artistPlaylist = new ArrayList<>();
		
		for (MySong song : playlist) {
			if (song.getArtist().equalsIgnoreCase(artistName)) {
				artistPlaylist.add(song);
			}
		}
		
		if (artistPlaylist.isEmpty()) {
			System.out.println("Could not find any songs by \"" + artistName 
							   + "\" in current playlist.");
			return;
		}
		
		Collections.sort(artistPlaylist,
						 (c1, c2) -> {
							 if (c1.getAlbum().compareTo(c2.getAlbum()) == 0) {
								 return c1.getTitle().compareTo(c2.getTitle());
							 } else {
								 return c1.getAlbum().compareTo(c2.getAlbum());
							 }
						 });
		
		
		Iterator<MySong> itr = artistPlaylist.iterator();
		MySong previousSong = itr.next();
		System.out.println("\"" + previousSong.getAlbum() + "\"");
		System.out.println("---------------------------------------------"
						   + "-----------");
		System.out.println(previousSong.toString());
		while (itr.hasNext()) {
			// if next has different album than previous, use line separator
			MySong nextSong = itr.next();
			if (nextSong.getAlbum().equals(previousSong.getAlbum())) {
				System.out.println(nextSong.toString());
			} else {
				System.out.println("\"" + nextSong.getAlbum() + "\"");
				System.out.println("-------------------------------------------"
								   + "-------------");
				System.out.println(nextSong.toString());
			}
			
			previousSong = nextSong;
		}
	}

	/**
	 * Prompts the user for the second required argument for the execution of
	 * the main method of this class
	 * @param i The previous option chosen if this method is invoked more than 
	 * 			once; NaN otherwise
	 * @return The string value of what the user typed
	 */
	private static String promptForSecondOption(double i) {
		if (!Double.isNaN(i)) {
			System.out.println("\nThe option you supplied is not a valid "
					+ "option; please try again. (Use an integer value)");
		}
		
		System.out.println("\nWhat do you want to do?");
		System.out.println("1\t:\tDisplay Top 10 playlist");
		System.out.println("2\t:\tFind out if the playlist has any songs "
				+ "by a specified artist");
		System.out.println("3\t:\tDisplay all songs by specified artist, "
				+ "grouped by album");
		System.out.print("\nType choice here: ");
	
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String result = in.nextLine();
		System.out.println();
		
		return result;
	}

	/**
	 * Prompts the user for the third argument necessary to execute options 
	 * two and three of this class.
	 * @return The string value of what the user typed
	 */
	private static String promptForThirdOption() {
		System.out.println("\nWhat artsit are you interested in?");
		System.out.print("Artist: ");
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String result = in.nextLine();
		
		return result;
	}

	/**
	 * Prints a heading with project specific details.
	 */
	private static void printHeading() {
		System.out.println("Dakota Brown");
	    System.out.println("Project 5 - Playlist Manager");
	    System.out.println("CMSC 256 - Section 001");
	    System.out.println("Spring 2018\n\n\n");		
	}

}

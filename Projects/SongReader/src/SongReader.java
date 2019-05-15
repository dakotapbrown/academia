import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code SongReader} class serves to handle I/O for reading {@code Song} 
 * attributes from a file and parsing the data. It can also handle errors in 
 * the data format, printing out an error log of all encountered errors to a 
 * file titled "ErrorsLog.txt".
 * 
 * The class is final to prevent subclasses.
 * 
 * @author Dakota Brown
 * @version CMSC 256-001, Spring 2018
 * @since March 31, 2018
 */
public final class SongReader {
	private static List<Song> songBook;
	private static Scanner in;
	private static String errors = "";
	
	/**
	 * Private constructor prevents instances of the class.
	 */
	private SongReader() {
		// prevent instances
	}
	
	/**
	 * Accepts a file name, represented as a string, to parse containing 
	 * {@code Song} object attributes. If one is not provided, the user 
	 * will be prompted for one via the console.
	 * 
	 * @param args The name of the file to parse
	 */
	public static void main(String[] args) {
		
		printHeading();

		if (args.length > 0) {
			setScanner(args[0]);
		} else {
			setScanner(promptForFileName());
		}
		
		songBook = new ArrayList<>();		
		while (in.hasNext()) {
			String potentialSong = in.next();
			if (potentialSong.matches("^\\s*?$") &&
					in.hasNext()) {
				// empty space between song tags, skip it
				potentialSong = in.next().trim();
			} else {
				if (!in.hasNext()) {
					potentialSong = null;
				}
			} 
		
			String errors;
			if (isTagMatched(potentialSong)) {
				String album 
					= extractTagContents(potentialSong, "album");
				String title
					= extractTagContents(potentialSong, "title");
				String artist 
					= extractTagContents(potentialSong, "artist");
				if (album == null 
						|| artist == null
						|| title == null) {
					// a field cannot be null, therefore the song is invalid
					errors = potentialSong; 
				} else {
					album = album.trim();
					artist = artist.trim();
					title = title.trim();
					// all tags are there, simply remove them & their contents
					errors = extractErrors(potentialSong).trim();
					
					// add the new song
					songBook.add(new Song(title, artist, album));
				}
				
			} else {
				// if all present, valid tags don't match, song is invalid
				errors = potentialSong;
			}
			
			if (errors != null && !errors.matches("^\\s*?$")) {
				logErrors(errors);
			} // else it's just whitespace
			
			
		}
		in.close();
		
		writeErrors();
		
		for (Song song : songBook) {
			System.out.println(song.toString());
		}
	}

	/**
	 * Prints a heading with project specific details.
	 */
	private static void printHeading() {
		System.out.println("Dakota Brown");
	    System.out.println("Project 4 - SongReader");
	    System.out.println("CMSC 256 - Section 001");
	    System.out.println("Spring 2018\n\n\n");		
	}
	
	/**
	 * Writes all logged errors to "ErrorsLog.txt" file.
	 */
	private static void writeErrors() {
		try (PrintWriter out 
				= new PrintWriter(new File("ErrorsLog.txt"))) {
			
			for (char ch : errors.toCharArray()) {
				if (ch == '\n') {
					out.println();
				} else if (ch != '\t') {
					out.print(ch);
				}
			}
		} catch (FileNotFoundException fnfe) {
			System.err.println("Could not open or write to file.");
			fnfe.printStackTrace();
		}
	}

	/**
	 * Logs the errors encountered in each potential {@code Song} object,
	 * with all errors of a particular song separated by a header of 
	 * "*********Error********". Each song will have it's own header and 
	 * listed errors, if any. 
	 * 
	 * @param errors The errors contained in a potential {@code Song} object
	 */
	private static void logErrors(String errors) {
		String result = "*********Error********";
		result += "\n" + errors + "\n\n";
		
		SongReader.errors += result;
	}

	/**
	 * Sets the {@code Scanner} instance variable <i>in</i> to the provided
	 * file name. If the file cannot be found, the user will be prompted for 
	 * another file name until an acceptable file is provided.
	 * 
	 * Sets a delimiter on the {@code Scanner} to tokenize anything between 
	 * HTML-style {@code <song>} tags (opening an closing).
	 * 
	 * @param fileName The name of the {@code File} to pass to {@code Scanner}
	 * 			constructor
	 * @throws FileNotFoundException
	 * 			If the file cannot be found, or cannot be opened/isn't writable
	 */
	@SuppressWarnings("resource") // closed in main() method
	private static void setScanner(String fileName) {
		do {
			try {
				in = new Scanner(new File(fileName)).useDelimiter("</?song>");
				fileName = null;
			} catch (FileNotFoundException fnfe) {
				System.out.println("File not found. Please identify a new file.");
				fileName = promptForFileName();
			}		
		} while (fileName != null);
	}
	
	/**
	 * Prompts the user for a file name via the console.
	 * 
	 * @return The file name specified
	 */
	private static String promptForFileName() {
		@SuppressWarnings("resource") // might reuse
		Scanner systemIn = new Scanner(System.in);
		System.out.print("Enter file name to parse: ");
		String result = systemIn.nextLine();
		
		return result;
	}

	/**
	 * Extracts the contents of the specified HTML-style {@code 
	 * tag} from the specified {@code line}.
	 * 
	 * @param line The line to parse
	 * @param tag The tag contents to extract
	 * @return The extracted tag contents
	 */
	private static String extractTagContents(String line, String tag) {
		int i =	line.indexOf("<" + tag + ">");
		if (i < 0) {
			return null;
		}
		
		i += tag.length() + 2;			
		int j = line.indexOf("</" + tag + ">");
		
		return line.substring(i, j);
	}
	
	/**
	 * Extracts any errors from a potential {@code Song}. The errors are 
	 * anything that appear outside the three acceptable tags. All three tags
	 * present in the enumeration {@code Tag} must be accounted for before 
	 * invoking this method.
	 * 
	 * @param line The line to parse
	 * @return The errors extracted
	 */
	private static String extractErrors(String line) {
	
		String result;
		int i = line.indexOf("<title>");
		int j = line.indexOf("</title>");
		String before = line.substring(0, i);
		String after = line.substring(j + "</title>".length());
		line = before + after;
		
		i = line.indexOf("<album>");
		j = line.indexOf("</album>");
		before = line.substring(0, i);
		after = line.substring(j + "</album>".length());
		line = before + after;
		
		i = line.indexOf("<artist>");
		j = line.indexOf("</artist>");
		before = line.substring(0, i);
		after = line.substring(j + "</artist>".length());
		result = before + after;
		
		return result;
	}

	/**
	 * Tests the specified {@code line} to see if all tags present in the
	 * enumeration {@code Tag} are matched properly.
	 * 
	 * @param line The line to parse
	 * @return {@code true} if any present proper opening tags have matching 
	 * 			closing tags. There must not be more than one opening tag 
	 * 			before finding a matching closing tag. {@code false} otherwise.
	 */
	private static boolean isTagMatched(String line) {
		// FIXME: two errors. nested opening tags & opening tag after
		// three properly matched opening/closing tags
		if (line == null) {
			return false;
		}
		
		line.trim();
		
		StackInterface<String> tagStack = new ArrayStack<>();
		int j = line.indexOf("<"); // find first '<' character
		int properTags = 0;

		while (j != -1) {
			// find next '>' character
			int k = line.indexOf(">", j + 1);
			if (k == -1) {
				
				/*
				 * check if proper tags have been found yet. if so, and no 
				 * closing bracket is found, it's fine. no extra proper tags 
				 * are present. if all proper tags haven't been matched, then
				 * it's definitely not going to match if there are no more
				 * tags present.
				 */
				
				// check if opening bracket is last char && NOT first char
				if (j == line.length() - 1 && j != 0) {
					
					/* *********************************************************
					 * edge case: opening bracket as last char of line is 
					 * not grounds for tags being mismatched. if there are 
					 * proper tags present, its still possible that all proper
					 * tags are matched. all three proper tags **must** have 
					 * matched for this condition to hold
					 **********************************************************/
					if (properTags == Tag.values().length) {
						return true;
					} // otherwise, all three proper tags weren't present
				}
				return false; // invalid tag
			}
			
			// remove < >
			String potentialTag = line.substring(j + 1, k);
			if (!potentialTag.startsWith("/")) { // this is an opening tag
				for (Tag mTag : Tag.values()) { 
					// test if proper tag
					if (potentialTag
							.toUpperCase()
							.equals(mTag.toString())) {
						// add to stack
						tagStack.push(potentialTag.toUpperCase()); 
					} // else ignore it; it's not a tag
				}
			} else { // closing tag
				for (Tag mTag : Tag.values()) { 
					// test if proper tag
					if (potentialTag
							.substring(1)
							.toUpperCase()
							.equals(mTag.toString())) {
						if (tagStack.isEmpty()) {
							return false; // nothing to match
						} else {
							if (!tagStack
									.pop()
									.equals(potentialTag
											.substring(1)
											.toUpperCase())) {
								return false; // mismatched tags
							} else {
								// closing tag matches prior opening tag
								properTags++;
							}
						}
					}
				}
			}
			j = line.indexOf('<', j + 1); // find next '<' character
		}
		 
		return tagStack.isEmpty(); //
	}
	
	/**
	 * Provides a succinct definition of which tags are acceptable for parsing
	 * a file for {@code Song} attributes.
	 * 
	 * @author Dakota Brown
	 * @version CMSC 256-001, Spring 2018
	 * @since March 31, 2018
	 */
	protected enum Tag {
		ALBUM, ARTIST, TITLE
	}
	
}

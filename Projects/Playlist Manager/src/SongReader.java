import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
 * @since April 21, 2018
 */
public final class SongReader {
	private static List<MySong> songBook;
	private static Scanner fileReader;
	private static String errors = "";
	
	public SongReader() {
		// construct object; do not initialize variables
	}
	
	/**
	 * TODO
	 */
	public SongReader(String fileName) throws FileNotFoundException {
		setFileReader(fileName);
	}
	
	/**
	 * Accepts a file name, represented as a string, to parse containing 
	 * {@code Song} object attributes. If one is not provided, the user 
	 * will be prompted for one via the console.
	 * 
	 * @param args The name of the file to parse
	 */
	public List<MySong> run() {
		
		songBook = new ArrayList<>();		
		while (fileReader.hasNext()) {
			String potentialSong = fileReader.next();
			if (potentialSong.matches("^\\s*?$") &&
					fileReader.hasNext()) {
				// empty space between song tags, skip it
				potentialSong = fileReader.next().trim();
			} else {
				if (!fileReader.hasNext()) {
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
//				String playcountStr
//					= extractTagContents(potentialSong, "playcount");
				
				if (album == null 
						|| artist == null
						|| title == null) {
					// a field cannot be null, therefore the song is invalid
					errors = potentialSong; 
				} else {
					album = album.trim();
					artist = artist.trim();
					title = title.trim();
					int playcount = 0;
					do {
						String playcountStr
							= extractTagContents(potentialSong, "playcount");
						try {
							if (playcountStr != null) {
								playcountStr = playcountStr.trim();
							}
							playcount += Integer.parseInt(playcountStr);
						} catch (NumberFormatException nfe) {
							playcount = 0;
						}
						// all tags are there, simply remove them & their contents
						potentialSong = extractErrors(potentialSong).trim();
					} while (potentialSong.contains("<playcount>"));
					// add the new song
					
					errors = potentialSong;
					songBook.add(new MySong(title, artist, album, playcount));
				}
				
			} else {
				// if all present, valid tags don't match, song is invalid
				errors = potentialSong;
			}
			
			if (errors != null && !errors.matches("^\\s*?$")) {
				logErrors(errors);
			} // else it's just whitespace
			
			
		}
		fileReader.close();
		
		writeErrors();
		
		return songBook;
	}
	
	/**
	 * Writes all logged errors to "ErrorsLog.txt" file.
	 */
	public static void writeErrors() {
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
	public static void logErrors(String errors) {
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
	public void setFileReader(String fileName)
			throws FileNotFoundException {
		
		fileReader = new Scanner(new File(fileName));
		fileReader.useDelimiter("</?song>"); // parse items between song tags
		
	}
	
	/**
	 * Prompts the user for a file name via the console.
	 * 
	 * @return The file name specified
	 */
	public static String promptForFileName() {
		@SuppressWarnings("resource") // might reuse
		Scanner systemIn = new Scanner(System.in);
		System.out.print("Enter file name to parse: ");
		String result = systemIn.nextLine();
		System.out.println();
		
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
	public static String extractTagContents(String line, String tag) {
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
	public static String extractErrors(String line) {
	
		String result;
		
		int i = line.indexOf("<title>");
		int j = line.indexOf("</title>");
		String before, after;
		if (i >= 0) {
			before = line.substring(0, i);
			after = line.substring(j + "</title>".length());
			line = before + after;
		} // else title is not present
		
		i = line.indexOf("<playcount>");
		if (i >= 0) {
			j = line.indexOf("</playcount>");
			before = line.substring(0, i);
			after = line.substring(j + "</playcount>".length());
			line = before + after;		
		} // else playcount is not present
		
		i = line.indexOf("<album>");
		if (i >= 0) {
			j = line.indexOf("</album>");
			before = line.substring(0, i);
			after = line.substring(j + "</album>".length());
			line = before + after;
		}
		
		i = line.indexOf("<artist>");
		if (i >= 0) {
			j = line.indexOf("</artist>");
			before = line.substring(0, i);
			after = line.substring(j + "</artist>".length());
			line = before + after;
		}
		
		result = line;
		
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
	public static boolean isTagMatched(String line) {
		// FIXME: two errors. nested opening tags & opening tag after
		// three properly matched opening/closing tags
		if (line == null) {
			return false;
		}
		
		line.trim();
		
		Set<String> presentTags = new HashSet<>();
		StackInterface<String> tagStack = new ArrayStack<>();
		
		List<String> allTags = new ArrayList<>();
		for (EssentialTags tag : EssentialTags.values()) {
			allTags.add(tag.toString().toLowerCase());
		}
		for (OptionalTags tag : OptionalTags.values()) {
			allTags.add(tag.toString().toLowerCase());
		}
		
		int j = line.indexOf("<"); // find first '<' character

		while (j != -1) {
			// find next '>' character
			int k = line.indexOf(">", j + 1);
			if (k == -1) {				
				/* ************************************************************
				 * check if allessential tags have been found yet. if so, and 
				 * no closing bracket is found, it's fine. no extra essential 
				 * tags are present. if all proper tags haven't been matched, 
				 * then it's definitely not going to match if there are no more
				 * tags present.
				 **************************************************************/
				for (EssentialTags tag : EssentialTags.values()) {
					if (!presentTags.contains(tag.toString().toLowerCase())) {
						return false;
					} else {
						return true;
					}
				}
				
//				} else if (j == line.length() - 1 && j != 0) { 
//					// check if opening bracket is last char && NOT first char
//					
//					/* *********************************************************
//					 * edge case: opening bracket as last char of line is 
//					 * not grounds for tags being mismatched. if there are 
//					 * proper tags present, its still possible that all proper
//					 * tags are matched. all three proper tags **must** have 
//					 * matched for this condition to hold
//					 **********************************************************/
//				}
				
				
				
			}		
			
			// remove < >
			String potentialTag = line.substring(j + 1, k);
			if (!potentialTag.startsWith("/")) { // this is an opening tag
				for (String tag : allTags) { 
					// test if proper tag
					if (potentialTag.equals(tag)) {
						if (!tagStack.isEmpty()) {
							return false;
						} else { // add to stack
							tagStack.push(potentialTag); 
						}
					} // else ignore it; it's not a tag
				}
			} else { // closing tag
				for (String tag : allTags) { 
					// test if proper tag
					if (potentialTag.substring(1).equals(tag)) {
						if (tagStack.isEmpty()) {
							return false; // nothing to match
						} else {
							String openTag = tagStack.pop();
							if (!openTag.equals(potentialTag.substring(1))) {
								return false; // mismatched tags
							} else {
								// closing tag matches prior opening tag
								presentTags.add(openTag);
							}
						}
					}
				}
			}
			j = line.indexOf('<', j + 1); // find next '<' character
		}
		 
		return tagStack.isEmpty(); 
	}
	
	/**
	 * Provides a succinct definition of which tags are acceptable for parsing
	 * a file for {@code Song} attributes.
	 * 
	 * @author Dakota Brown
	 * @version CMSC 256-001, Spring 2018
	 * @since March 31, 2018
	 */
	private enum EssentialTags {
		ALBUM, ARTIST, TITLE
	}
	
	private enum OptionalTags {
		PLAYLIST
	}
	
}

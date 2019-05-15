/**
 * This class serves as an extension of the {@code Song} class to provide
 * additional attributes to {@code Song} objects.
 * 
 * @author Dakota Brown
 * @version CMSC 256-001, Spring 2018
 * @since April 21, 2018
 */
public class MySong extends Song {
	private int playcount;
	
	public MySong(String title, String artist, String album, int playcount) {
		super(title, artist, album);
		setPlaycount(playcount);
	}

	/**
	 * TODO
	 * @return the playcount
	 */
	public int getPlaycount() {
		return playcount;
	}

	/**
	 * TODO
	 * @param playcount the playcount to set
	 */
	public void setPlaycount(int playcount) {
		if (playcount < 0) {
			throw new IllegalArgumentException("Playcount cannot be less"
					+ " than zero");
		}
		this.playcount = playcount;
	}
	
	/**
	 * @see Song#toString()
	 */
	@Override
	public String toString() {
		String result = super.toString();
		result += String.format("Playcount: %d%n", playcount);
		
		return result;
	}

}

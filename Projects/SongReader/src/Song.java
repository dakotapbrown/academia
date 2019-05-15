/**
 * The {@code Song} class represents a song, with a title, artist, and album. 
 * 
 * @author Dakota Brown
 * @version CMSC 256-001, Spring 2018
 * @since March 31, 2018
 */
public class Song implements Comparable<Song> {
	private String title;
	private String artist;
	private String album;
	
	/**
	 * Constructs a default {@code Song} object with all instances
	 * set to {@code null}, as an empty string is an acceptable value
	 * for any of the instances. 
	 */
	public Song() {
		this(null, null, null);
	}
	
	/**
	 * Constructs a {@code Song} object with the specified labels.
	 * 
	 * @param title The song name
	 * @param artist The artist of the song
	 * @param album	Title of album that contains the song
	 */
	public Song(String title, String artist, String album) {
		setTitle(title);
		setArtist(artist);
		setAlbum(album);
	}

	/**
	 * Retrieves the title of this {@code Song}.
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets this {@code Song} title.
	 * 
	 * @param title Tthe title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Retrieves the artist of this {@code Song}.
	 * 
	 * @return The artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * Sets the artist of this {@code Song}.
	 * 
	 * @param artist The artist to set
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * Retrieves the album title of this {@code Song}.
	 * 
	 * @return The album
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * Sets the album name of this {@code Song}.
	 * 
	 * @param album The album to set
	 */
	public void setAlbum(String album) {
		this.album = album;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof Song) {
			Song temp = (Song) obj;
			if (getArtist() != null &&
					getArtist().equals(temp.getArtist())) {
				if (getAlbum() != null &&
						getAlbum().equals(temp.getAlbum())) {
					if (getTitle() != null &&
							getTitle().equals(temp.getTitle())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Title: %s%nArtist: %s%nAlbum: %s%n",
							 getTitle(),
							 getArtist(),
							 getAlbum());
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Song song) {
		if (this == song) {
			return 0;
		}
		
		int artistComparison = getArtist().compareTo(song.getArtist());
		int albumComparison = getAlbum().compareTo(song.getAlbum());
		int titleComparison = getTitle().compareTo(song.getTitle());
		
		if (artistComparison == 0) {
			if (albumComparison == 0) {
				return titleComparison;
			}
			return albumComparison;
		}
		
		return artistComparison;
	}
}

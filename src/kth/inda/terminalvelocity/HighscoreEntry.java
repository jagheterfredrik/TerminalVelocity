package kth.inda.terminalvelocity;

import java.io.Serializable;

/**
 * A simple class that describes one highscore.
 * @author Fredrik Gustafsson
 */

public class HighscoreEntry implements Comparable<HighscoreEntry>, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer score;
	
	/**
	 * Creates an highscore entry with the name and score
	 * provided.
	 * @param name the name of the contestant.
	 * @param score the score of the contestant.
	 */
	public HighscoreEntry(String name, Integer score) {
		this.name = name;
		this.score = score;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(HighscoreEntry o) {
		return this.score.compareTo(o.getScore());
	}
	
	/**
	 * Returns the score in this highscore-entry.
	 * @return the score.
	 */
	public Integer getScore() {
		return this.score;
	}
	
	
	/**
	 * Returns the name of the contestant in this
	 * highscore-entry.
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}
}

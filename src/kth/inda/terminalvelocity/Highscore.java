package kth.inda.terminalvelocity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class handles the highscore. It uses DES-encryption to
 * prevent tempering to the highscore-file.
 * @author Fredrik Gustafsson
 * @version 2009-04-22
 */

public class Highscore {
	static private Highscore _instance = null;
	private ArrayList<HighscoreEntry> entries = null;
	private Cipher ecipher;
	private Cipher dcipher;

	public static Highscore instance() {
		if(null == _instance) {
			_instance = new Highscore();
		}
		return _instance;
	}

	/**
	 * Constructor, initializes the crypto-system and reads the highscore
	 * from the file.
	 */
	public Highscore() {
		//Setup DES-en/de-cryption to be used for the highscore.
		try {
			ecipher = Cipher.getInstance("AES");
			dcipher = Cipher.getInstance("AES");
			SecretKey key = null;
			key = new SecretKeySpec(GameConstants.
					HIGHSCORE_CRYPTO_KEY.getBytes(), "AES");
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);

		} catch (javax.crypto.NoSuchPaddingException e) {
		} catch (java.security.NoSuchAlgorithmException e) {
		} catch (java.security.InvalidKeyException e) {
			e.printStackTrace();
		}
		
		readHighscore();

		return;
	}

	/**
	 * Read crypted data from the highscore-file.
	 * @return the decrypted data in the file.
	 * @see ObjectInputStream#readObject
	 */
	@SuppressWarnings("unchecked")
	private void readHighscore() {
		FileInputStream fis = null;
		CipherInputStream cis = null;
		ObjectInputStream ois = null;
		entries = new ArrayList<HighscoreEntry>();
		try {
			fis = new FileInputStream(GameConstants.HIGHSCORE_FILE_NAME);
			cis = new CipherInputStream(fis, dcipher);
			ois = new ObjectInputStream(cis);
			
			this.entries = (ArrayList<HighscoreEntry>) ois.readObject();
			
			ois.close();
			cis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			entries = new ArrayList<HighscoreEntry>();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Write crypted data to the highscore-file.
	 * @param the string to write.
	 */
	private void writeHighscore() {
		FileOutputStream fos = null;
		CipherOutputStream cos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(GameConstants.HIGHSCORE_FILE_NAME);
			cos = new CipherOutputStream(fos, ecipher);
			oos = new ObjectOutputStream(cos);
			
			oos.writeObject(entries);
			oos.flush();
			
			oos.close();
			cos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new highscore.
	 * @param name the name of the contestant.
	 * @param score the score of the contestant.
	 */
	@SuppressWarnings("unchecked")
	public void submitHighscore(String name, int score) {
		HighscoreEntry he = new HighscoreEntry(name, score);
		
		entries.add(he);
		Comparator c = Collections.reverseOrder();
		
		Collections.sort(entries,c);
		
		while(entries.size()>GameConstants.HIGHSCORE_SCORE_COUNT)
			entries.remove(entries.size()-1);
		
		writeHighscore();
	}
	
	/**
	 * Returns an sorted iterator over the highscores.
	 * @return
	 */
	public Iterator<HighscoreEntry> elementIterator() {
		return entries.iterator();
	}
	
	/**
	 * Check if a score is enough to place the contestant
	 * in the highscore.
	 * @param score the score to be qualified.
	 * @return the rank on the highscore-list given
	 * the score.
	 */
	public int qualify(int score) {
		for(int i = 0; i < entries.size(); i++)
			if(entries.get(i).getScore()<score)
				return i+1;
		if(entries.size()<GameConstants.HIGHSCORE_SCORE_COUNT)
			return entries.size()+1;
		return 0;
	}

}

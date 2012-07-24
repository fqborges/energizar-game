package util;

/**
 * 
 * 
 * @author fonte:
 *         http://v4ks1n.wordpress.com/2011/01/25/blackberry-javame-string
 *         -manipulation-split-replace-replaceall/
 * 
 */
public class StringUtil {

	/**
	 * Quebra uma string em substrings delimitadas pelo separador.
	 * 
	 * No caso da string terminar com o delimitador o array de substrings terá
	 * exatamente um substring para cada ocorrência do separador.
	 * 
	 * No caso da string não terminar com o delimitador o array terá um
	 * substring a mais que a quantidade de ocorrências do separador.
	 * 
	 * @param strString
	 *            string original
	 * @param strDelimiter
	 *            delimitador
	 * @return substrings da string original delimitadas pelo separador
	 */
	public static String[] split(String strString, String strDelimiter) {
		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0;

		// Check for null input strings.
		if (strString == null) {
			throw new NullPointerException("Input string cannot be null.");
		}
		// Check for null or empty delimiter
		// strings.
		if (strDelimiter.length() <= 0 || strDelimiter == null) {
			throw new NullPointerException("Delimeter cannot be null or empty.");
		}

		// If strString begins with delimiter
		// then remove it in
		// order
		// to comply with the desired format.

		if (strString.startsWith(strDelimiter)) {
			strString = strString.substring(strDelimiter.length());
		}

		// If strString does not end with the
		// delimiter then add it
		// to the string in order to comply with
		// the desired format.
		if (!strString.endsWith(strDelimiter)) {
			strString += strDelimiter;
		}

		// Count occurrences of the delimiter in
		// the string.
		// Occurrences should be the same amount
		// of inner strings.
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
				iIndexOfInnerString)) != -1) {
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		}

		// Declare the array with the correct
		// size.
		String[] strArray = new String[iOccurrences];

		// Reset the indices.
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0;

		// Walk across the string again and this
		// time add the
		// strings to the array.
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
				iIndexOfInnerString)) != -1) {

			// Add string to
			// array.
			strArray[iCounter] = strString.substring(iIndexOfInnerString,
					iIndexOfDelimiter);

			// Increment the
			// index to the next
			// character after
			// the next
			// delimiter.
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();

			// Inc the counter.
			iCounter += 1;
		}
		return strArray;
	}

	/**
	 * Retorna uma string cujo conteúdo é o conteúdo da string original com
	 * todas ocorrencias de <code>pattern</code> substituidas por
	 * <code>replacement</code>.
	 * 
	 * @param source
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(String source, String pattern,
			String replacement) {

		// If source is null then Stop
		// and retutn empty String.
		if (source == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		// Intialize Index to -1
		// to check agaist it later
		int idx = -1;
		// Search source from 0 to first occurrence of pattern
		// Set Idx equal to index at which pattern is found.

		String workingSource = source;

		// Iterate for the Pattern till idx is not be -1.
		while ((idx = workingSource.indexOf(pattern)) != -1) {
			// append all the string in source till the pattern starts.
			sb.append(workingSource.substring(0, idx));
			// append replacement of the pattern.
			sb.append(replacement);
			// Append remaining string to the String Buffer.
			sb.append(workingSource.substring(idx + pattern.length()));

			// Store the updated String and check again.
			workingSource = sb.toString();

			// Reset the StringBuffer.
			sb.delete(0, sb.length());
		}

		return workingSource;
	}
}

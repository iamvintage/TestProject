import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class MyDistinctWords {

	final static String originalFileError = "originalFileError";
	final static String originalFileErrorMsg = "Original file error. Try to input an existing original file name.";
	final static String outputFileError = "outputFileError";
	final static String outputFileErrorMsg = "Output file error. Try to input an output file name with a valid directory.";
	final static String otherErrorMsg = "Error encountered. Please provide the original file to developer to fix bug.";
	final static String strTokens = " ,.;:\\\"+—-=@!#$%^&*()_{}[]|/<>~`?”“";

	/*
	 * This is to read the original file and store the unique words (without check in casing) in a Set
	 * @param fileName File name of original file
	 * @throws IOException
	 */
	private Set<String> getDistinctWords(String fileName) throws IOException {
		// Use TreeSet to avoid duplicates and sorted in natural order
		Set<String> wordsSet = new TreeSet<String>();

		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null; //buffered reader will read data in chunk

		try {
			fis = new FileInputStream(fileName);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));

			String line = null;
			while ((line = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, strTokens);
				while (token.hasMoreTokens()) {
					String word = token.nextToken().toLowerCase().trim();
					
					while(word != null && word.startsWith("\'")|| word.endsWith("\'")) {
						word = word.replaceAll("^\'|\'$", "");
					}

					if (!word.isEmpty()) {
							wordsSet.add(word);
					}
				}
			}

		}catch (IOException ex) {
			originalFileError();
		} finally {
			if (br != null) {
				try {
					br.close();
					dis.close();
					fis.close();
				} catch (Exception ex) {
				}
			}
		}

		return wordsSet;
	}

	/*
	 * This is to write the contents of Set to output file.
	 * @param wordsSet Set of unique words
	 * @param outputFile File name of output file
	 * @throws IOException
	 */
	private void writeFile(Set<String> wordsSet, String outputFile) throws IOException {

		BufferedWriter br = null;

		try {
			br = new BufferedWriter(new FileWriter(outputFile));
			for (String word : wordsSet) {
				br.write(word);
				br.newLine();
			}
		} catch (IOException ex) {
			outputFileError();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception ex) {
				}
			}
		}

	}
	
	/*
	 * This is a method to throw exception on user-entered output file
	 * @throws IOException
	 */
	private static void outputFileError() throws IOException {
		throw new IOException(outputFileError);
	}

	/*
	 * This is a method to throw exception on user-entered original file
	 * @throws IOException
	 */
	private static void originalFileError() throws IOException {
		throw new IOException(originalFileError);
	}

	public static void main(String args[]) {

		Scanner sc = null;

		while (true) {
			try {
				sc = new Scanner(System.in);
				System.out.println("Enter full path of original file: ");
				String origFile = sc.nextLine();
				System.out.println("Enter full path of output file: ");
				String outputFile = sc.nextLine();

				// throw error if orig file is empty
				if (origFile == null || origFile.isEmpty())
					originalFileError();

				// throw error if output file is empty
				if (outputFile.isEmpty() || outputFile == null)
					outputFileError();

				// throw error if origFile is not an existing file
				if (!(new File(origFile).exists()))
					originalFileError();

				// throw error if output file is not a valid directory
				if (!new File(new File(outputFile).getParent()).exists()) {
					outputFileError();
				}

				MyDistinctWords distWords = new MyDistinctWords();
				Set<String> wordsSet = distWords.getDistinctWords(origFile);
				distWords.writeFile(wordsSet, outputFile);
				sc.close();
				System.out.println("Processing completed. Generated " + outputFile);
				break;

			} catch (IOException ex) {
				if (ex.getMessage().equals(originalFileError)) {
					System.out.println(originalFileErrorMsg);
				} else if (ex.getMessage().equals(outputFileError)) {
					System.out.println(outputFileErrorMsg);
				}
			} catch (Exception ex) {
				System.out.println(otherErrorMsg);
			}
		}
	}
}

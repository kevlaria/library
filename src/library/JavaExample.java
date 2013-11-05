package library;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import library.Book;

public class JavaExample {

	public static void main(String[] args) {
		System.out.println("What is the name of the file containing book information?");
		Scanner scanner = new Scanner(System.in);
		String filename = scanner.nextLine();
		scanner.close();
		try {
			scanner = new Scanner(new File(filename));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(" :: ");
				String title = tokens[0];
				String author = tokens[1];
				Book newBook = new Book(title, author);
				books.add(newBook);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Your library data file does not exist. No library created");
			System.out.println("Your text file should be placed at the same level as the src folder");
			System.out.println("The text file will be right below .project and .classpath");
		}
	}
}
//Taorui Cui, Kevin Lee

package library;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OverdueNoticeTest {

	OverdueNotice notice1, notice2;
	Patron patron1, patron2;
	Book book1, book2, book3;
	Library library;


	@Before
	public void setUp() throws Exception {
		book1 = new Book("Contact", "Carl Sagan");
		book2 = new Book("Equal Rites", "Terry Pratchett");
		book3 = new Book("Weird Sisters", "Terry Pratchett");
		book1.checkOut(8);
		book2.checkOut(4);
		book3.checkOut(3);
		patron1 = new Patron("Kevin", library);
		patron2 = new Patron("Amy", library);
		patron1.take(book1);
		patron1.take(book2);
		patron2.take(book3);
		notice1 = new OverdueNotice(patron1, 4);
		notice2 = new OverdueNotice(patron2, 0);
	}

	/**
	 * Test method for constructor
	 */
	@Test
	public void testOverdueNotice() {        
		assertTrue(notice1 instanceof OverdueNotice);
		assertTrue(notice2 instanceof OverdueNotice);
	}

	/**
	 * Test method for toStirng()
	 */
	@Test
	public void test() {
		System.out.println(notice1);

		//test the warnings of overdue notice.
		assertTrue(notice1.toString().contains("This book is overdue")); 
		assertFalse(notice2.toString().contains("This book is overdue"));
		//test today's date is printed out if overdue.
		assertTrue(notice1.toString().contains("Today")); 
		assertFalse(notice2.toString().contains("Today"));
		//test the overdue book.
		assertTrue(notice1.toString().contains(book1.toString()) && 
				notice1.toString().contains("This book is overdue")); 
		assertFalse(notice2.toString().contains(book3.toString()) && 
				notice2.toString().contains("This book is overdue")); 

	}

}

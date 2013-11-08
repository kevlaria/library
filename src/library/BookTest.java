//Taorui Cui, Kevin Lee

package library;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author David Matuszek
 */
public class BookTest {
    private Book contact;
    private Book equalRites;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        contact = new Book("Contact", "Carl Sagan");
        equalRites = new Book("Equal Rites", "Terry Pratchett");
    }

    /**
     * Test method for constructor.
     */
    @Test
    public void testBook() {        
        assertTrue(contact instanceof Book);
        assertTrue(equalRites instanceof Book);
    }
    
    /**
     * Test method for {@link library.Book#getTitle()}.
     */
    @Test
    public void testGetTitle() {
        assertEquals("Contact", contact.getTitle());
        assertEquals("Equal Rites", equalRites.getTitle());
    }

    /**
     * Test method for {@link library.Book#getAuthor()}.
     */
    @Test
    public void testGetAuthor() {
        assertEquals("Carl Sagan", contact.getAuthor());
        assertEquals("Terry Pratchett", equalRites.getAuthor());
    }

    /**
     * Test method for {@link library.Book#getDueDate()}.
     */
    @Test
    public void testGetDueDate() {
        assertEquals(-1, contact.getDueDate());
    }

    /**
     * Test method for {@link library.Book#checkOut(int)}.
     */
    @Test
    public void testCheckOut() {
        contact.checkOut(12);
        assertEquals(12, contact.getDueDate());
    }

    /**
     * Test method for {@link library.Book#checkIn()}.
     */
    @Test
    public void testCheckIn() {
        contact.checkOut(12);
        assertEquals(12, contact.getDueDate());
        contact.checkIn();
        assertEquals(-1, contact.getDueDate());
    }

    /**
     * Test method for {@link library.Book#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("Contact by Carl Sagan", contact.toString());
    }

}

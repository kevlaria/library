package library;

public class Calendar {

	private int date;
	
	/**
	 * Constructor for Calendar object. Sets date as 0.
	 */
	public Calendar(){
		this.date = 0;
	}
	
	/**
	 * Returns the current date
	 * @return current date (int)
	 */
	public int getDate(){
		return this.date;
	}
	
	/**
	 * Increment the date by 1
	 */
	public void advance(){
		this.date = this.date + 1;
	}

	/**
	 * For unit tests
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Calendar other = (Calendar) obj;
		if (date != other.date)
			return false;
		return true;
	}
	
	
}

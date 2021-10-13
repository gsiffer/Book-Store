/**
 * Project: a01179721_Assignment2
 * File: Book.java
 * Date: Feb. 22, 2020
 * Time: 1:56:28 p.m.
 */
package a01179721.data;

/**
 * Class Book to store book data.
 * 
 * @author Gabor Siffer, A01179721
 *
 */
public class Book {

	public static final int ATTRIBUTE_COUNT = 8;

	private long id;
	private String isbn;
	private String authors;
	private int publicationYear;
	private String title;
	private double averageRating;
	private long ratingsCount;
	private String imageUrl;

	public static class Builder {
		// Required parameters
		private long id;
		private String isbn;

		// Optional parameters
		private String authors;
		private int publicationYear;
		private String title;
		private double averageRating;
		private long ratingsCount;
		private String imageUrl;

		public Builder(long id, String isbn) {
			this.id = id;
			this.isbn = isbn;
		}

		/**
		 * @param authors
		 *            the authors to set
		 */
		public Builder setAuthors(String authors) {
			this.authors = authors;
			return this;
		}

		/**
		 * @param publicationYear
		 *            the publicationYear to set
		 */
		public Builder setPublicationYear(int publicationYear) {
			this.publicationYear = publicationYear;
			return this;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * @param averageRating
		 *            the averageRating to set
		 */
		public Builder setAverageRating(double averageRating) {
			this.averageRating = averageRating;
			return this;
		}

		/**
		 * @param ratingsCount
		 *            the ratingsCount to set
		 */
		public Builder setRatingsCount(long ratingsCount) {
			this.ratingsCount = ratingsCount;
			return this;
		}

		/**
		 * @param imageUrl
		 *            the imageUrl to set
		 */
		public Builder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		public Book build() {
			return new Book(this);
		}

	}

	/**
	 * Default Constructor
	 */
	private Book(Builder builder) {
		setId(builder.id);
		setIsbn(builder.isbn);
		authors = builder.authors;
		publicationYear = builder.publicationYear;
		title = builder.title;
		averageRating = builder.averageRating;
		ratingsCount = builder.ratingsCount;
		imageUrl = builder.imageUrl;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		if (id > 0) {
			this.id = id;
		} else {
			System.err.format("Invalid id number: %d. It has to be greater than zero", id);
			System.exit(-1);
		}
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn
	 *            the isbn to set
	 */
	public void setIsbn(String isbn) {
		if (isbn != null && !isbn.trim().isEmpty()) {
			this.isbn = isbn.trim();
		} else {
			System.err.println("Invalid isbn number. It can't be empty or null.");
			System.exit(-1);
		}
	}

	/**
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors
	 *            the authors to set
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * @return the publicationYear
	 */
	public int getPublicationYear() {
		return publicationYear;
	}

	/**
	 * @param publicationYear
	 *            the publicationYear to set
	 */
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the averageRating
	 */
	public double getAverageRating() {
		return averageRating;
	}

	/**
	 * @param averageRating
	 *            the averageRating to set
	 */
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	/**
	 * @return the ratingsCount
	 */
	public long getRatingsCount() {
		return ratingsCount;
	}

	/**
	 * @param ratingsCount
	 *            the ratingsCount to set
	 */
	public void setRatingsCount(long ratingsCount) {
		this.ratingsCount = ratingsCount;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", isbn=" + isbn + ", authors=" + authors + ", publicationYear=" + publicationYear + ", title=" + title
				+ ", averageRating=" + averageRating + ", ratingsCount=" + ratingsCount + ", imageUrl=" + imageUrl + "]";
	}

}

package com.ac.cst8276.xmlparser.entity;

import com.ac.cst8276.xmlparser.controller.CourseService;

public class NodeObj {
	
	private String bookId;
	private String author;
	private String title;
	private String genre;
	private String price; 
	private String publishDate; 
	private String description;
	
	
	// Default constructor
	public NodeObj() {
		
	}
	
	
	/**
	 * @param bookId
	 * @param author
	 * @param title
	 * @param genre
	 * @param price
	 * @param description
	 */
	public NodeObj(String bookId, String author, String title, String genre, String price, String publishDate, String description) {
		super();
		this.bookId = bookId;
		this.author = author;
		this.title = title;
		this.genre = genre;
		this.price = price;
		this.publishDate = publishDate;
		this.description = description;
	}
	
	
	/**
	 * @param author
	 * @param title
	 * @param genre
	 * @param price
	 * @param description
	 */
	public NodeObj(String author, String title, String genre, String price, String description) {
		super();
		this.author = author;
		this.title = title;
		this.genre = genre;
		this.price = price;
		this.description = description;
	}

	
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public NodeObj fromId(String id) {
//		CourseService xserv = new CourseService();
//	    this.bookId = id;
//	    this.author = xserv.getElementById(bookId, "author");
//	    this.title = xserv.getElementById(bookId, "title");
//	    this.genre = xserv.getElementById(bookId, "genre");
//	    this.price = xserv.getElementById(bookId, "price");
//	    this.publishDate = xserv.getElementById(bookId, "publish_date");
//	    this.description = xserv.getElementById(bookId, "description");
	    
	    return this;
	}
	
}

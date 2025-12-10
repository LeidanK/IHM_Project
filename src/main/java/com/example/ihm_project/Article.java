package application;

public class Article {
    String title;
    String authors;
    String category; 
    String summary;
    String link;
    int year;
    int month;

    public Article(String title, String authors, String category, String summary, String link, int year, int month) {
        this.title = title;
        this.authors = authors;
        this.category = category;
        this.summary = summary;
        this.link = link;
        this.year = year;
        this.month = month;
    }

    @Override 
    public String toString() { 
        return title; 
    }
}
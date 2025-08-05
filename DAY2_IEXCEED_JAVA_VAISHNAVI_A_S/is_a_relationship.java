class LibraryItem {
    String title;

    LibraryItem(String title) {
        this.title = title;
    }

    void displayTitle() {
        System.out.println("Title: " + title);
    }
}

class Book extends LibraryItem {
    String author;

    Book(String title, String author) {
        super(title);
        this.author = author;
    }

    void displayInfo() {
        displayTitle();
        System.out.println("Author: " + author);
    }
}

public class is_a_relationship{
    public static void main(String[] args) {
        Book book = new Book("money", "James");
        book.displayInfo();
    }
}

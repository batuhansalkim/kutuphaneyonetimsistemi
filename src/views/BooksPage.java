package views;
import javax.swing.table.DefaultTableModel;

import models.Book;
import javax.swing.*;
import java.awt.*;
import controller.BookController;
import java.util.List;

public class BooksPage extends JFrame {
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private BookController bookController;

    public BooksPage() {
        // Frame ayarları
        setTitle("Kitaplar Sayfası - Library Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Controller'ı başlat
        bookController = new BookController();

        // Başlık Paneli
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(100, 149, 237));
        JLabel headerLabel = new JLabel("Kitaplar");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Kitaplar Tablosu
        String[] columnNames = {"Sayı", "Kitap Adı", "Yazar", "Kategori", "Durum"};
        tableModel = new DefaultTableModel(columnNames, 0);
        booksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buton Paneli
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Kitap Ekle Butonu
        JButton addBookButton = new JButton("Kitap Ekle");
        addBookButton.addActionListener(e -> showAddBookDialog());
        buttonPanel.add(addBookButton);

        // Geri Dön Butonu
        JButton backButton = new JButton("Geri Dön");
        backButton.addActionListener(e -> {
            dispose();
            HomePage homePage = new HomePage("staff"); // HomePage geri dön
            homePage.setVisible(true);
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Veritabanından kitapları çek ve tabloyu güncelle
        loadBooksFromDatabase();

        setLocationRelativeTo(null); // Ortada açılmasını sağlar
    }

    // Veritabanından kitapları yükle
    private void loadBooksFromDatabase() {
        List<Book> books = bookController.getBooks();

        // Mevcut tabloyu temizle
        tableModel.setRowCount(0);

        // Kitapları tabloya ekle
        for (Book book : books) {
            Object[] row = {book.getBookId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getStatus()};
            tableModel.addRow(row);
        }
    }

    // Yeni kitap ekleme formunu göster
    private void showAddBookDialog() {
        // Yeni bir panel oluşturuyoruz
        JPanel addBookPanel = new JPanel();
        addBookPanel.setLayout(new GridLayout(5, 2)); // 5 satır, 2 sütun

        // Kitap adı
        JLabel titleLabel = new JLabel("Kitap Adı:");
        JTextField titleField = new JTextField(20);
        addBookPanel.add(titleLabel);
        addBookPanel.add(titleField);

        // Yazar adı
        JLabel authorLabel = new JLabel("Yazar Adı:");
        JTextField authorField = new JTextField(20);
        addBookPanel.add(authorLabel);
        addBookPanel.add(authorField);

        // Kategori
        JLabel categoryLabel = new JLabel("Kategori:");
        JTextField categoryField = new JTextField(20);
        addBookPanel.add(categoryLabel);
        addBookPanel.add(categoryField);

        // Durum
        JLabel statusLabel = new JLabel("Durum:");
        JTextField statusField = new JTextField(20);
        addBookPanel.add(statusLabel);
        addBookPanel.add(statusField);

        // Kitap ekle butonu
        JButton addButton = new JButton("Kitap Ekle");
        addButton.addActionListener(e -> {
            // Veriler boş değilse veritabanına ekleyelim
            String bookName = titleField.getText();
            String authorName = authorField.getText();
            String category = categoryField.getText();
            String status = statusField.getText();

            if (bookName != null && !bookName.isEmpty() &&
                authorName != null && !authorName.isEmpty() &&
                category != null && !category.isEmpty() &&
                status != null && !status.isEmpty()) {

                Book newBook = new Book(0, bookName, authorName, category, status); // ID veritabanı tarafından otomatik atanacak
                boolean success = bookController.addBook(newBook);

                if (success) {
                    loadBooksFromDatabase(); // Kitapları tekrar yükle
                    JOptionPane.showMessageDialog(this, "Kitap başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Kitap eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        addBookPanel.add(new JLabel()); // Placeholder boş bir etiket
        addBookPanel.add(addButton);

        // Paneli bir dialog içinde göster
        int option = JOptionPane.showConfirmDialog(this, addBookPanel, "Yeni Kitap Ekle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.CANCEL_OPTION) {
            // Kullanıcı iptal ettiğinde herhangi bir işlem yapma
            return;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BooksPage booksPage = new BooksPage();
            booksPage.setVisible(true);
        });
    }
}
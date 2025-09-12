import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

// Helper class to store anime info
class Anime {
    String title;
    String genre;
    String imagePath; // File name

    Anime(String title, String genre, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.imagePath = imagePath;
    }
}

public class Main {
    public static void main(String[] args) {
        // Create window
        JFrame frame = new JFrame("Anime Recommendation");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout
        frame.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Anime Recommendation", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(title, BorderLayout.NORTH);

        // Dropdown menu 🎨
        String[] moods = {"😊 Happy", "😢 Sad", "⚡ Excited", "🌸 Chill"};
        String[] genres = {
                "⚔️ Action",
                "💕 Romance",
                "😂 Comedy",
                "🧚 Fantasy",
                "🕵 Mystery",
                "👻 Horror",
                "🌍 Adventure",
                "🏆 Sports"
        };

        JComboBox<String> moodBox = new JComboBox<>(moods);
        JComboBox<String> genreBox = new JComboBox<>(genres);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(255, 192, 203)); // light pink
        inputPanel.add(new JLabel("Mood:"));
        inputPanel.add(moodBox);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreBox);
        frame.add(inputPanel, BorderLayout.CENTER);

        // Result area (text + image)
        JPanel resultPanel = new JPanel(new BorderLayout());
        JLabel result = new JLabel("Pick mood + genre and click Recommend!", JLabel.CENTER);
        result.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel imageLabel = new JLabel("", JLabel.CENTER); // where the cover image will appear
        resultPanel.add(result, BorderLayout.NORTH);
        resultPanel.add(imageLabel, BorderLayout.CENTER);

        // Button
        JButton recommendBtn = new JButton("Recommend");

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 182, 193)); // sakura pink
        bottomPanel.add(resultPanel, BorderLayout.CENTER);
        bottomPanel.add(recommendBtn, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Anime recommendations by mood + genre
        Anime[][] recommendations = {
                // Happy 😊
                {
                        new Anime("Haikyuu!!", "Action", "haikyuu.jpg"), // sports & uplifting
                        new Anime("Toradora!", "Romance", "toradora.jpg"), // rom-com
                        new Anime("K-On!", "Comedy", "k_on.jpg"), // slice-of-life comedy
                        new Anime("One Piece", "Adventure", "one_piece.jpg"), // fun adventure
                        new Anime("Kimi ni Todoke", "Romance", "kimi_ni_todoke.jpg"), // sweet romance
                        new Anime("Ouran High School Host Club", "Comedy", "ouran.jpg"), // rom-com, lighthearted
                        new Anime("Spy x Family", "Comedy", "spy_x_family.jpg"), // fun family comedy
                        new Anime("Pokémon", "Adventure", "pokemon.jpg") // cheerful adventure
                },
                // Sad 😢
                {
                        new Anime("Your Lie in April", "Romance", "your_lie_in_april.jpg"), // emotional romance
                        new Anime("Clannad", "Romance", "clannad.jpg"), // drama/romance
                        new Anime("A Silent Voice", "Romance", "a_silent_voice.jpg"), // emotional drama
                        new Anime("Nana", "Romance", "nana.jpg"), // tragic romance
                        new Anime("5 Centimeters Per Second", "Romance", "five_cm_per_sec.jpeg"), // bittersweet romance
                        new Anime("The Summer Hikaru Died", "Horror", "hikaru_died.jpg") // dark, tragic
                },
                // Excited ⚡
                {
                        new Anime("Attack on Titan", "Action", "attack_on_titan.jpg"), // intense action
                        new Anime("Jujutsu Kaisen", "Action", "jujutsu_kaisen.jpg"), // hype action
                        new Anime("Blue Lock", "Sports", "blue_lock.jpg"), // adrenaline sports
                        new Anime("Hunter X Hunter", "Adventure", "hunter_x_hunter.jpg"), // exciting adventure
                        new Anime("My Hero Academia", "Action", "my_hero_academia.jpg"), // action-packed
                        new Anime("Demon Slayer", "Action", "demon_slayer.jpg"), // thrilling battles
                        new Anime("Romantic Killer", "Comedy", "romantic_killer.jpg"), // chaotic rom-com excitement
                        new Anime("The Disastrous Life of Saiki K", "Comedy", "saiki_k.jpg"), // over-the-top comedy
                        new Anime("Higurashi", "Horror", "higurashi.png") // psychological horror, intense
                },
                // Chill 🌸
                {
                        new Anime("Fruits Basket", "Romance", "fruits_basket.jpg"), // gentle romance
                        new Anime("Spirited Away", "Fantasy", "spirited_away.jpg"), // magical, calming
                        new Anime("A Whisker Away", "Fantasy", "a_whisker_away.jpg"), // whimsical
                        new Anime("The Apothecary Diaries", "Mystery", "apothecary_diaries.jpg"), // slow-burn mystery
                        new Anime("Jojo’s Bizarre Adventure", "Adventure", "jojo.jpg"), // quirky, stylish adventure
                        new Anime("Bleach", "Action", "bleach.jpg"), // more adventure vibe
                        new Anime("My Happy Marriage", "Romance", "my_happy_marriage.jpg"), // calm romance drama
                        new Anime("Sailor Moon", "Fantasy", "sailor_moon.jpg") // magical girl classic
                }
        };

        Random random = new Random();

        // Button logic
        recommendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int moodIndex = moodBox.getSelectedIndex();

                // Strip emojis from dropdown values to match Anime genres
                String selectedGenre = genreBox.getSelectedItem().toString().replaceAll("^[^a-zA-Z]+", "").trim();

                // Filter by genre
                List<Anime> filtered = new ArrayList<>();
                for (Anime rec : recommendations[moodIndex]) {
                    if (rec.genre.equals(selectedGenre)) {
                        filtered.add(rec);
                    }
                }

                if (!filtered.isEmpty()) {
                    Anime chosen = filtered.get(random.nextInt(filtered.size()));
                    result.setText("Recommendation: " + chosen.title);

                    // Load image from src/resources/images
                    java.net.URL imgURL = Main.class.getResource("/resources/images/" + chosen.imagePath);
                    if (imgURL != null) {
                        ImageIcon icon = new ImageIcon(imgURL);
                        Image scaledImage = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                    } else {
                        imageLabel.setIcon(null);
                        System.out.println("❌ Image not found: " + chosen.imagePath);
                    }
                } else {
                    result.setText("No anime found for " + moods[moodIndex] + " + " + selectedGenre);
                    imageLabel.setIcon(null);
                }
            }
        });

        // Show window
        frame.setVisible(true);
    }
}

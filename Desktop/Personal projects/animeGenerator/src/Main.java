import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create window
        JFrame frame = new JFrame("Anime Recommendation App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Layout
        frame.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Anime Recommendation Generator", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(title, BorderLayout.NORTH);

        // Dropdowns
        String[] moods = {"Happy", "Sad", "Excited", "Chill"};
        String[] genres = {"Action", "Romance", "Comedy", "Fantasy", "Mystery", "Horror", "Adventure", "Sports"};

        JComboBox<String> moodBox = new JComboBox<>(moods);
        JComboBox<String> genreBox = new JComboBox<>(genres);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(255, 192, 203)); // light pink
        inputPanel.add(new JLabel("Mood:"));
        inputPanel.add(moodBox);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreBox);
        frame.add(inputPanel, BorderLayout.CENTER);

        // Recommendation label
        JLabel result = new JLabel("Pick mood + genre and click Recommend!", JLabel.CENTER);
        result.setFont(new Font("Arial", Font.PLAIN, 14));

        // Button
        JButton recommendBtn = new JButton("Recommend");

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 182, 193)); // sakura pink
        bottomPanel.add(result, BorderLayout.CENTER);
        bottomPanel.add(recommendBtn, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Anime recommendations by mood + genre
        String[][] recommendations = {
                // Happy
                {
                        "Happy + Action → Haikyuu!!",
                        "Happy + Romance → Toradora!",
                        "Happy + Comedy → K-On!",
                        "Happy + Fantasy → One Piece",
                        "Happy + Romance → Kimi ni Todoke",
                        "Happy + Action → Jujutsu Kaisen",
                        "Happy + Mystery → Death Note",
                        "Happy + Romance → Fruits Basket"
                },
                // Sad
                {
                        "Sad + Action → Demon Slayer",
                        "Sad + Romance → Your Lie in April",
                        "Sad + Comedy → Clannad",
                        "Sad + Fantasy → A Silent Voice",
                        "Sad + Romance → Ouran High School Host Club",
                        "Sad + Action → Bleach",
                        "Sad + Romance → My Happy Marriage"
                },
                // Excited
                {
                        "Excited + Action → Attack on Titan",
                        "Excited + Romance → Romantic Killer",
                        "Excited + Comedy → The Disastrous Life of Saiki K",
                        "Excited + Fantasy → Jujutsu Kaisen",
                        "Excited + Horror → Higurashi",
                        "Excited + Sports → Blue Lock",
                        "Excited + Action → Hunter X Hunter",
                        "Excited + Action → My Hero Academia"
                },
                // Chill
                {
                        "Chill + Action → Naruto",
                        "Chill + Romance → Fruits Basket",
                        "Chill + Comedy → Spy x Family",
                        "Chill + Fantasy → Spirited Away",
                        "Chill + Adventure → Jojo’s Bizarre Adventure",
                        "Chill + Comedy → Pokémon",
                        "Chill + Mystery → The Apothecary Diaries",
                        "Chill + Romance → Nana",
                        "Chill + Fantasy → A Whisker Away",
                        "Chill + Romance → 5 Centimeters Per Second",
                        "Chill + Horror → The Summer Hikaru Died"
                }
        };


        Random random = new Random();

        // Button logic
        recommendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int moodIndex = moodBox.getSelectedIndex();
                String selectedGenre = genreBox.getSelectedItem().toString();

                // Filter recommendations by genre
                List<String> filtered = new ArrayList<>();
                for (String rec : recommendations[moodIndex]) {
                    if (rec.contains(selectedGenre)) {
                        filtered.add(rec);
                    }
                }

                if (!filtered.isEmpty()) {
                    String chosen = filtered.get(random.nextInt(filtered.size()));
                    result.setText("Recommendation: " + chosen);
                } else {
                    result.setText("No anime found for " + moods[moodIndex] + " + " + selectedGenre);
                }
            }
        });

        // Show window
        frame.setVisible(true);
    }
}

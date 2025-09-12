import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AnimeRecommenderApp {
    public static void main(String[] args) {
        // Create window
        JFrame frame = new JFrame("Anime Recommendation App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout
        frame.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Anime Recommendation Generator", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(title, BorderLayout.NORTH);

        // Dropdowns
        String[] moods = {"Happy", "Sad", "Excited", "Chill"};
        String[] genres = {"Action", "Romance", "Comedy", "Fantasy"};

        JComboBox<String> moodBox = new JComboBox<>(moods);
        JComboBox<String> genreBox = new JComboBox<>(genres);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Mood:"));
        inputPanel.add(moodBox);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreBox);
        frame.add(inputPanel, BorderLayout.CENTER);

        // Recommendation label
        JLabel result = new JLabel("Pick mood + genre and click Recommend!", JLabel.CENTER);
        result.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(result, BorderLayout.SOUTH);

        // Button
        JButton recommendBtn = new JButton("Recommend");
        frame.add(recommendBtn, BorderLayout.PAGE_END);

        // Anime options
        String[][] animeList = {
                {"K-On!", "Your Lie in April", "Attack on Titan", "Spirited Away", "One Piece"},
                {"Haikyuu!!", "Clannad", "Demon Slayer", "Naruto", "Toradora!"}
        };

        Random random = new Random();

        // Button logic
        recommendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String mood = moodBox.getSelectedItem().toString();
                String genre = genreBox.getSelectedItem().toString();

                // Pick a random anime
                String[] recs = {
                        "Happy + Comedy → K-On!",
                        "Sad + Romance → Your Lie in April",
                        "Excited + Action → Attack on Titan",
                        "Chill + Fantasy → Spirited Away",
                        "Any mood → One Piece"
                };

                String chosen = recs[random.nextInt(recs.length)];
                result.setText("Recommendation: " + chosen);
            }
        });

        // Show window
        frame.setVisible(true);
    }
}

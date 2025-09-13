import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.Map;

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
        // Load anime.json using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Anime>>>() {}.getType();

        Map<String, List<Anime>> animeData = null;
        try {
            // ✅ Try to load anime.json from resources
            InputStream inputStream = Main.class.getResourceAsStream("/anime.json");

            // Debug print — will tell us if the file is found
            System.out.println("anime.json resource = " + Main.class.getResource("/anime.json"));

            if (inputStream == null) {
                throw new RuntimeException("❌ anime.json not found in resources folder! " +
                        "Make sure it's inside src/resources/ and that folder is marked as Resources Root.");
            }

            // Use UTF-8 so special characters (like Japanese titles) load correctly
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                animeData = gson.fromJson(reader, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Failed to load anime.json", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Map moods to lists (same order as dropdown menu 🎨)
        List<List<Anime>> recommendations = List.of(
                animeData.getOrDefault("Happy", new ArrayList<>()),   // 😊 Happy
                animeData.getOrDefault("Sad", new ArrayList<>()),     // 😢 Sad
                animeData.getOrDefault("Excited", new ArrayList<>()), // ⚡ Excited
                animeData.getOrDefault("Chill", new ArrayList<>())    // 🌸 Chill
        );

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

        Random random = new Random();

        // Button logic
        recommendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int moodIndex = moodBox.getSelectedIndex();

                // Strip emojis from dropdown values to match Anime genres
                String selectedGenre = genreBox.getSelectedItem().toString().replaceAll("^[^a-zA-Z]+", "").trim();

                // Filter by genre
                List<Anime> filtered = new ArrayList<>();
                for (Anime rec : recommendations.get(moodIndex)) {
                    if (rec.genre.equalsIgnoreCase(selectedGenre)) {
                        filtered.add(rec);
                    }
                }

                if (!filtered.isEmpty()) {
                    Anime chosen = filtered.get(random.nextInt(filtered.size()));
                    result.setText("Recommendation: " + chosen.title);

                    // Load image from src/resources/images
                    java.net.URL imgURL = Main.class.getResource("/images/" + chosen.imagePath);
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

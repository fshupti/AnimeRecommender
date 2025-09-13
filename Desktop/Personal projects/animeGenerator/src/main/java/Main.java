import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.Map;

// Helper class to store anime info (used for local JSON backup)
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

// Custom rounded border class (for cute panels/buttons)
class RoundedBorder implements javax.swing.border.Border {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(new Color(230, 230, 230)); // soft light gray border
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

public class Main {
    public static void main(String[] args) {
        //  Load anime.json using Gson (backup data if AniList fails)
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Anime>>>() {}.getType();

        Map<String, List<Anime>> animeData = null;
        try {
            // Load anime.json from resources
            InputStream inputStream = Main.class.getResourceAsStream("/anime.json");

            System.out.println("anime.json resource = " + Main.class.getResource("/anime.json"));

            if (inputStream == null) {
                throw new RuntimeException("❌ anime.json not found in resources folder! " +
                        "Make sure it's inside src/resources/ and that folder is marked as Resources Root.");
            }

            // Parse JSON with UTF-8 (for Japanese titles, etc.)
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                animeData = gson.fromJson(reader, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Failed to load anime.json", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Map moods to anime lists (same order as dropdown menu 🎨)
        List<List<Anime>> recommendations = List.of(
                animeData.getOrDefault("Happy", new ArrayList<>()),   // 😊 Happy
                animeData.getOrDefault("Sad", new ArrayList<>()),     // 😢 Sad
                animeData.getOrDefault("Excited", new ArrayList<>()), // ⚡ Excited
                animeData.getOrDefault("Chill", new ArrayList<>())    // 🌸 Chill
        );

        // Create main window
        JFrame frame = new JFrame("Anime Recommendation");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title at top
        JLabel title = new JLabel("Anime Recommendation", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dropdown menus 🎨 (Mood + Genre selection)
        String[] moods = {"😊 Happy", "😢 Sad", "⚡ Excited", "🌸 Chill"};
        String[] genres = {
                "⚔️ Action", "💕 Romance", "😂 Comedy", "🧚 Fantasy",
                "🕵 Mystery", "👻 Horror", "🌍 Adventure", "🏆 Sports"
        };

        JComboBox<String> moodBox = new JComboBox<>(moods);
        JComboBox<String> genreBox = new JComboBox<>(genres);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(255, 192, 203)); // light pink background
        inputPanel.add(new JLabel("Mood:"));
        inputPanel.add(moodBox);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreBox);

        // Combine title + inputPanel into one top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(255, 192, 203));
        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(5)); // small spacing
        topPanel.add(inputPanel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Card-style panel for results
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(20), // rounded edges
                BorderFactory.createEmptyBorder(15, 15, 15, 15) // padding inside
        ));
        cardPanel.setLayout(new BorderLayout());

        JLabel result = new JLabel("Pick mood + genre and click Recommend!", JLabel.CENTER);
        result.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel imageLabel = new JLabel("", JLabel.CENTER); // where cover image will show
        cardPanel.add(result, BorderLayout.NORTH);
        cardPanel.add(imageLabel, BorderLayout.CENTER);

        // Center the card with padding
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(255, 182, 193)); // sakura pink
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        centerPanel.add(cardPanel);

        frame.add(centerPanel, BorderLayout.CENTER);

        //  Recommend Button (cute pill-style)
        JButton recommendBtn = new JButton("Recommend");
        recommendBtn.setPreferredSize(new Dimension(120, 35));
        recommendBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        recommendBtn.setBackground(new Color(255, 182, 193)); // pastel pink
        recommendBtn.setForeground(Color.WHITE);
        recommendBtn.setFocusPainted(false);
        recommendBtn.setOpaque(true);
        recommendBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true)); // soft white outline

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 182, 193));
        buttonPanel.add(recommendBtn);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        Random random = new Random();

        // Button logic: Try AniList first, fallback to local JSON
        recommendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedGenre = genreBox.getSelectedItem().toString().replaceAll("^[^a-zA-Z]+", "").trim();

                try {
                    //  Try AniList API
                    String response = AniListAPI.fetchByGenre(selectedGenre);

                    JsonObject data = JsonParser.parseString(response)
                            .getAsJsonObject()
                            .getAsJsonObject("data")
                            .getAsJsonObject("Page");

                    JsonArray mediaArray = data.getAsJsonArray("media");

                    if (mediaArray.size() > 0) {
                        int randomIndex = new Random().nextInt(mediaArray.size());
                        JsonObject anime = mediaArray.get(randomIndex).getAsJsonObject();

                        String title = anime.getAsJsonObject("title").get("romaji").getAsString();
                        String img = anime.getAsJsonObject("coverImage").get("large").getAsString();
                        String url = anime.get("siteUrl").getAsString();

                        // Show AniList result
                        result.setText("<html>" + title + " (<a href='" + url + "'>AniList</a>)</html>");

                        ImageIcon icon = new ImageIcon(new URL(img));
                        Image scaledImage = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                        return; // stop here if AniList works
                    }
                } catch (Exception ex) {
                    System.out.println("⚠️ AniList failed, using backup JSON...");
                }

                // Fallback: Local JSON
                int moodIndex = moodBox.getSelectedIndex();
                List<Anime> filtered = new ArrayList<>();
                for (Anime rec : recommendations.get(moodIndex)) {
                    if (rec.genre.equalsIgnoreCase(selectedGenre)) {
                        filtered.add(rec);
                    }
                }

                if (!filtered.isEmpty()) {
                    Anime chosen = filtered.get(random.nextInt(filtered.size()));
                    result.setText("Backup Recommendation: " + chosen.title);

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
                    result.setText("No anime found for " + selectedGenre);
                    imageLabel.setIcon(null);
                }
            }
        });

        // Show window
        frame.setVisible(true);
    }
}

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class AniListAPI {

    // Fetch anime list from AniList by genre
    public static String fetchByGenre(String genre) throws Exception {
        // GraphQL query asking for anime by genre
        String query = "{ Page(perPage: 10) { media(type: ANIME, genre_in: [\"" + genre + "\"], sort: POPULARITY_DESC) { title { romaji english } coverImage { large } siteUrl genres }}}";

        // Build JSON body
        String jsonInput = "{ \"query\": \"" + query.replace("\"", "\\\"") + "\" }";

        URL url = new URL("https://graphql.anilist.co");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        // Send body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read response or error
        int status = conn.getResponseCode();
        InputStream stream = (status < 400) ? conn.getInputStream() : conn.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }

        // Debug logs
        System.out.println("AniList HTTP Status: " + status);
        System.out.println("AniList Response: " + response);

        return response.toString(); // return AniList JSON
    }
}

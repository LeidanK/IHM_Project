package com.example.ihm_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebContentFetcher {

    public static String fetchHTML(String urlString) throws IOException {
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = null;

        // Création de l'objet URL
        URL url = new URL(urlString);

        // Ouverture de la connexion HTTP
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000); // 5 secondes
        connection.setReadTimeout(5000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        // Vérification du code de réponse HTTP
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Erreur HTTP : " + status);
        }

        // Lecture du flux d'entrée
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    public static void main(String[] args) {
        String url = "https://arxiv.org/";

        try {
            String html = fetchHTML(url);
            System.out.println("Contenu récupéré :\n");
            System.out.println(html);
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
    }

    public static String extractBetween(String text, String start, String end) {
        int s = text.indexOf(start);
        if (s == -1) return "";
        s += start.length();
        int e = text.indexOf(end, s);
        if (e == -1) return "";
        return text.substring(s, e).trim();
    }

    public static Map<String, List<String>> extractSections(String html) {

        Map<String, List<String>> map = new LinkedHashMap<>();

        int pos = 0;

        while (true) {

            // Trouver un titre H2
            int h2Start = html.indexOf("<h2>", pos);
            if (h2Start == -1) break;

            int h2End = html.indexOf("</h2>", h2Start);
            if (h2End == -1) break;

            String sectionTitle = html.substring(h2Start + 4, h2End).trim();

            // Trouver le <ul> qui suit cette section
            int ulStart = html.indexOf("<ul>", h2End);
            if (ulStart == -1) break;

            int ulEnd = html.indexOf("</ul>", ulStart);
            if (ulEnd == -1) break;

            String ulContent = html.substring(ulStart, ulEnd);

            List<String> links = extractLinks(ulContent);

            map.put(sectionTitle, links);

            // continuer plus loin
            pos = ulEnd;
        }

        return map;
    }

    public static List<String> extractLinks(String html) {

        List<String> links = new ArrayList<>();

        int pos = 0;

        while (true) {

            int aStart = html.indexOf("<a ", pos);
            if (aStart == -1) break;

            int hrefStart = html.indexOf("href=\"", aStart);
            if (hrefStart == -1) break;

            hrefStart += 6; // avancer après href="

            int hrefEnd = html.indexOf("\"", hrefStart);
            if (hrefEnd == -1) break;

            String url = html.substring(hrefStart, hrefEnd);

            // texte du lien
            int textStart = html.indexOf(">", hrefEnd) + 1;
            int textEnd = html.indexOf("</a>", textStart);
            if (textEnd == -1) break;

            String text = html.substring(textStart, textEnd).trim();

            links.add(text + " -> " + url);

            pos = textEnd;
        }

        return links;
    }
}

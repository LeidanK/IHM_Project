package com.example.ihm_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.HostServices;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArxivApp {
    private final VBox articleContainer = new VBox(10);
    private final HostServices hostServices;

    public ArxivApp(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    static class ArxivArticle {
        String title;
        String authors;
        String link;

        ArxivArticle(String title, String authors, String link) {
            this.title = title;
            this.authors = authors;
            this.link = link;
        }
    }

    private List<ArxivArticle> scrapeRecent() throws IOException {
        List<ArxivArticle> papers = new ArrayList<>();
        Document doc = Jsoup.connect("https://arxiv.org/list/cs.CL/recent")
                .userAgent("Mozilla/5.0")
                .get();

        Elements entries = doc.select("dl > dt");
        Elements details = doc.select("dl > dd");

        for (int i = 0; i < entries.size(); i++) {
            Element entry = entries.get(i);
            Element detail = details.get(i);

            String title = detail.select("div.list-title").text().replace("Title:", "").trim();
            String authors = detail.select("div.list-authors").text().replace("Authors:", "").trim();
            String link = "https://arxiv.org" + entry.select("a[href]").first().attr("href");

            papers.add(new ArxivArticle(title, authors, link));
        }
        return papers;
    }

    public Scene createScene(Stage stage, Scene helloScene) {
        // Logo
        Image arxivImage = null;
        try {
            arxivImage = new Image(getClass().getResourceAsStream("/com/example/ihm_project/arxiv-logo.png"));
        } catch (Exception e) { System.out.println("Logo not found"); }
        
        ImageView arxivLogo = new ImageView();
        if (arxivImage != null) {
            arxivLogo.setImage(arxivImage);
            arxivLogo.setFitHeight(40);
            arxivLogo.setPreserveRatio(true);
        }

        // Back Button (Styled to fit under logo)
        Button backButton = new Button("← Retour");
        backButton.setStyle("-fx-background-color: white; -fx-text-fill: #b31b1b; -fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 2 5;");
        backButton.setOnAction(e -> stage.setScene(helloScene));

        // --- NEW LAYOUT: VBox for Logo + Button ---
        VBox leftColumn = new VBox(5, arxivLogo, backButton);
        leftColumn.setAlignment(Pos.CENTER_LEFT);
        // ------------------------------------------

        Label cornellLabel = new Label("Cornell University");
        cornellLabel.getStyleClass().add("cornell-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Header: Left Column (Logo+Button) -> Spacer -> Cornell Label
        HBox headerBox = new HBox(15, leftColumn, spacer, cornellLabel);
        headerBox.getStyleClass().add("header-box");
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10, 20, 10, 20));

        TextField searchField = new TextField();
        VBox.setMargin(searchField, new Insets(10, 15, 0, 15));
        searchField.setPromptText("Search");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> loadArticles(newVal));

        ScrollPane scroll = new ScrollPane(articleContainer);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.getStyleClass().add("article-scroll");
        scroll.setPannable(true);
        
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(headerBox);
        root.setCenter(new VBox(10, searchField, scroll));

        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(getClass().getResource("/com/example/ihm_project/style.css").toExternalForm());
        } catch (Exception e) { System.out.println("CSS not found"); }

        loadArticles("");

        return scene;
    }

    private void loadArticles(String filter) {
        articleContainer.getChildren().clear();
        try {
            List<ArxivArticle> articles = scrapeRecent();
            if (!filter.isEmpty()) {
                articles = articles.stream()
                        .filter(p -> p.title.toLowerCase().contains(filter.toLowerCase()) ||
                                     p.authors.toLowerCase().contains(filter.toLowerCase()) ||
                                     p.link.toLowerCase().contains(filter.toLowerCase()))
                        .toList();
            }
            for (ArxivArticle p : articles) {
                articleContainer.getChildren().add(createArticelCard(p));
            }
            articleContainer.setStyle("-fx-padding: 15;");
        } catch (IOException e) {
            articleContainer.getChildren().add(new Label("Erreur: " + e.getMessage()));
        }
    }

    private VBox createArticelCard(ArxivArticle arcticle) {
        Label title = new Label(arcticle.title);
        title.getStyleClass().add("article-title");
        FlowPane authorsPane = new FlowPane();
        authorsPane.setHgap(5); authorsPane.setVgap(4); authorsPane.setPrefWrapLength(500);
        for (String a : arcticle.authors.split(",")) {
            String trimmed = a.trim();
            Hyperlink authorLink = new Hyperlink(trimmed);
            authorLink.getStyleClass().add("author-link");
            authorLink.setOnAction(e -> loadArticlesByAuthor(trimmed));
            authorsPane.getChildren().add(authorLink);
        }
        Hyperlink link = new Hyperlink("Open the article →");
        link.getStyleClass().add("article-link");
        link.setOnAction(e -> openArticleWindow(arcticle));
        VBox card = new VBox(6, title, authorsPane, link);
        card.getStyleClass().add("article-card");
        return card;
    }

    private void loadArticlesByAuthor(String author) {
        articleContainer.getChildren().clear();
        try {
            String query = author.replace(" ", "+").replace(".", "");
            String url = "https://arxiv.org/search/cs?searchtype=author&query=" + query;
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
            List<ArxivArticle> articles = parseSearchResults(doc);
            for (ArxivArticle a : articles) {
                articleContainer.getChildren().add(createArticelCard(a));
            }
        } catch (Exception e) {
            articleContainer.getChildren().add(new Label("Cannot load author: " + author));
        }
    }

    private void openArticleWindow(ArxivArticle article) {
        Stage stage = new Stage();
        stage.setTitle(article.title);
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");
        Label title = new Label(article.title);
        title.getStyleClass().add("article-window-title");
        HBox authorsBox = new HBox(5);
        String abstractText = loadContent(article.link);
        Label abs = new Label(abstractText);
        abs.setWrapText(true);
        abs.getStyleClass().add("article-content");
        Hyperlink pdf = new Hyperlink("Download PDF");
        pdf.setOnAction(e -> hostServices.showDocument(article.link.replace("abs", "pdf")));
        root.getChildren().addAll(title, authorsBox, abs, pdf);
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    private String loadContent(String link) {
        try {
            Document doc = Jsoup.connect(link).userAgent("Mozilla/5.0").get();
            Element abs = doc.selectFirst("blockquote.abstract");
            if (abs != null) {
                abs.select("span.descriptor").remove();
                return abs.text().trim();
            }
            return "Abstract not found";
        } catch (Exception e) { return "Content not found"; }
    }

    private List<ArxivArticle> parseSearchResults(Document doc) {
        List<ArxivArticle> list = new ArrayList<>();
        Elements items = doc.select("li.arxiv-result");
        for (Element el : items) {
            String title = el.select("p.title").text();
            String authors = el.select("p.authors").text().replace("Authors:", "").trim();
            String link = el.select("p.title a").attr("href");
            if (!link.startsWith("http")) link = "https://arxiv.org" + link;
            list.add(new ArxivArticle(title, authors, link));
        }
        return list;
    }
}
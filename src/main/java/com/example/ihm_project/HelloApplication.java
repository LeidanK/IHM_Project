package com.example.ihm_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javafx.scene.control.TextField;

import static com.example.ihm_project.Card.createCategoryCard;
import static com.example.ihm_project.WebContentFetcher.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {

        // Composant principal
        VBox main = new VBox(15);
        main.setStyle("-fx-background-color: white;");
//        main.setPadding(new Insets(15));
        main.setAlignment(Pos.TOP_CENTER);

        // Titre
//        Label title = new Label("arXiv");
//        title.setFont(Font.font("Arial", FontWeight.BOLD, 35));
//        title.setTextFill(Color.WHITE);

        // Image / Logo
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/com/example/ihm_project/arxiv-logo.png")));
        logo.setPreserveRatio(true);
        logo.setFitHeight(60);
//        InputStream is = getClass().getResourceAsStream("/com/example/ihm_project/arxiv-logo.png");
//        System.out.println(is); // doit afficher quelque chose ≠ null

        // Header
        HBox header = new HBox(logo);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #b31b1b;");
        header.setMinHeight(80);
        header.setAlignment(Pos.CENTER_LEFT);

        // Footer
        GridPane footer = new GridPane();
        footer.setPadding(new Insets(18));
        footer.setHgap(120);
        footer.setVgap(15);
        footer.setStyle("-fx-background-color: #e7e7e7;");
        footer.setMinHeight(80);
        footer.setAlignment(Pos.BASELINE_CENTER);

        // Footer elements
        Hyperlink about = new Hyperlink("About");
        about.setTextFill(Color.BLACK);
        Hyperlink help = new Hyperlink("Help");
        help.setTextFill(Color.BLACK);
        Hyperlink contact = new Hyperlink("Contact");
        contact.setTextFill(Color.BLACK);
        Hyperlink subscribe = new Hyperlink("Subscribe");
        subscribe.setTextFill(Color.BLACK);
        Hyperlink copyright = new Hyperlink("Copyright");
        copyright.setTextFill(Color.BLACK);
        Hyperlink privacy = new Hyperlink("Privacy Policy");
        privacy.setTextFill(Color.BLACK);
        Hyperlink assistance = new Hyperlink("Web Accessibility Assistance");
        assistance.setTextFill(Color.BLACK);
        Hyperlink status = new Hyperlink("arXiv Operational Status");
        status.setTextFill(Color.BLACK);

        Hyperlink[] footerList = {about, help, contact, subscribe, copyright, privacy, assistance, status};
        String footerBaseUrl = "https://info.arxiv.org/";
        String[] urls = {"about", "help", "help/contact.html", "help/subscribe", "help/license/index.html", "help/policies/privacy_policy.html", "help/web_accessibility.html", "https://status.arxiv.org/"};

        footer.add(about, 0, 0);
        footer.add(help, 0, 1);
        footer.add(contact, 1, 0);
        footer.add(subscribe, 1, 1);
        footer.add(copyright, 2, 0);
        footer.add(privacy, 2, 1);
        footer.add(assistance, 3, 0);
        footer.add(status, 3, 1);

        for(int i = 0; i < 7; i++) {
            int finalI = i;
            footerList[i].setOnAction(e -> {
                    try {
                        // Ouvre le lien complet dans le navigateur
                        Desktop.getDesktop().browse(
                                new URI(footerBaseUrl + urls[finalI])
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            });
        }

        // Catégories
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        // Données extraites de la page web
        String url = "https://arxiv.org/";
        String html = null;

        try {
            html = fetchHTML(url);
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }

        String content = html.toString();

        // Scrapping données
        String introText = extractBetween(content, "<p class=\"tagline\">", "</p>");
        Map<String, List<String>> data = extractSections(content);

        // Description arXiv
        Label intro = new Label(introText);
        intro.setWrapText(true);
        intro.setFont(Font.font("Arial", 15));
        intro.setPadding(new Insets(10));

        updateGrid(grid, data, "");

        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher...");
        searchField.setMaxWidth(300);
        searchField.setPadding(new Insets(10));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateGrid(grid, data, newValue);
        });

        // Ajout des différentes Node
        main.getChildren().addAll(header, intro, searchField, grid, footer);

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);    // Le contenu occupe toute la largeur
        scroll.setStyle("-fx-background: white;");

        Scene helloScene = new Scene(scroll, 1000, 900);

        // Bouton pour passer au deuxième écran
        Button goToArxiv = new Button("Passer vers Articles");
        goToArxiv.setMinWidth(150);
        footer.add(goToArxiv, 4, 0);

        goToArxiv.setOnAction(e -> {
            ArxivApp app = new ArxivApp(getHostServices());
            Scene secondScene = app.createScene(stage, helloScene);
            stage.setScene(secondScene);
        });

        // --- NEW BUTTON FOR SIMULATION MODE ---
        Button goToSim = new Button("Mode Simulation");
        goToSim.setMinWidth(150);
        // Add it to the footer at column 4, row 1 (below the other button)
        footer.add(goToSim, 4, 1); 

        goToSim.setOnAction(e -> {
            SimulationApp sim = new SimulationApp();
            Scene simScene = sim.createScene(stage, helloScene);
            stage.setScene(simScene);
        });
        // ---------------------------------------

        stage.setTitle("arXiv Styled Page");
        stage.setScene(helloScene);
        stage.show();
    }

    private void updateGrid(GridPane grid, Map<String, List<String>> data, String filter) {

        grid.getChildren().clear(); // Efface les cards

        int col = 0, row = 0;

        for (String category : data.keySet()) {

            // Vérifie si le nom de catégorie correspond au filtre
            boolean matchCategory = category.toLowerCase().contains(filter.toLowerCase());

            // Vérifie si au moins un sous-élément correspond
            boolean matchItem = data.get(category).stream()
                    .anyMatch(item -> item.toLowerCase().contains(filter.toLowerCase()));

            if (filter.isEmpty() || matchCategory || matchItem) {
                VBox card = Card.createCategoryCard(category, data.get(category));

                grid.add(card, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
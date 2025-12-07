package com.example.ihm_project;

import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class Card {

    public static VBox createCategoryCard(String category, List<String> articles) {

        Label title = new Label(category);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextAlignment(TextAlignment.CENTER);

        VBox articleList = new VBox(5);

        for (String article : articles) {

            String[] parts = article.split("->");
            String text = parts[0].trim();
            String url = parts.length > 1 ? parts[1].trim() : "";

            System.out.println(text);

            Hyperlink link = new Hyperlink("• " + text);
            link.setFont(Font.font("Arial", 14));
            link.setWrapText(true);

            link.setOnAction(e -> {
                if (!url.isEmpty()) {
                    try {
                        // Ouvre le lien complet dans le navigateur
                        java.awt.Desktop.getDesktop().browse(
                                new java.net.URI("https://arxiv.org" + url)
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            articleList.getChildren().add(link);
        }

        // Scroll interne
        ScrollPane innerScroll = new ScrollPane(articleList);
        innerScroll.setFitToWidth(true);
        innerScroll.setStyle("-fx-background: #f8f8f8; -fx-background-color: #f8f8f8;");
        innerScroll.setPrefHeight(180);
        innerScroll.setMaxHeight(180);
        innerScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        innerScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox card = new VBox(10, title, innerScroll);
        card.setPadding(new Insets(15));

        card.setPrefSize(400, 300);
        card.setMaxSize(470, 350);
        card.setMinSize(280, 250);

        card.setStyle(
                "-fx-background-color: #f8f8f8;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        return card;
    }

    public static void main(String[] args) {

    }
}

package application;

import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.Consumer;

public class ArticleCard extends VBox {

    // Constructeur : On reçoit l'article ET l'action à effectuer au clic
    public ArticleCard(Article item, Consumer<String> onOpenLink) {
        
        // --- 1. STYLE GLOBAL (Ton style préféré) ---
        this.setPadding(new Insets(15));
        this.setSpacing(8);
        this.setStyle(
            "-fx-background-color: #f8f8f8;" +         // Fond gris clair
            "-fx-border-color: #cccccc;" +             // Bordure grise
            "-fx-border-radius: 8;" +                  // Coins arrondis bordure
            "-fx-background-radius: 8;"                // Coins arrondis fond
        );
        
        // Petite ombre portée pour le relief (Optionnel)
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        shadow.setRadius(5); 
        shadow.setOffsetY(2);
        this.setEffect(shadow);

        // --- 2. CONTENU ---
        
        // Titre
        Label title = new Label(item.title);
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        title.setTextFill(Color.web("#b31b1b")); // Rouge arXiv
        title.setWrapText(true);
        
        // Métadonnées (Date - Catégorie - Auteurs)
        HBox metaBox = new HBox(10);
        
        Label dateLabel = new Label(item.month + "/" + item.year);
        dateLabel.setStyle("-fx-text-fill: #b31b1b; -fx-font-weight: bold;");

        Label tag = new Label(item.category);
        tag.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 2 6 2 6; -fx-background-radius: 4; -fx-font-weight: bold; -fx-text-fill: #555;");
        
        Label authors = new Label("By: " + item.authors);
        authors.setFont(Font.font("Arial", javafx.scene.text.FontPosture.ITALIC, 12));
        authors.setTextFill(Color.web("#666"));
        
        metaBox.getChildren().addAll(dateLabel, tag, authors);
        
        // Résumé
        Label abstractText = new Label(item.summary);
        abstractText.setWrapText(true);
        abstractText.setFont(Font.font("Arial", 13));
        abstractText.setTextFill(Color.web("#333"));
        
        // Lien / Bouton
        Hyperlink link = new Hyperlink("Voir sur arXiv");
        link.setFont(Font.font("Arial", 14));
        link.setStyle("-fx-text-fill: #0055aa;"); // Bleu lien standard
        
        // Action au clic : on appelle la méthode du Main
        link.setOnAction(e -> onOpenLink.accept(item.link));

        // --- 3. ASSEMBLAGE ---
        this.getChildren().addAll(title, metaBox, abstractText, new Separator(), link);
    }
}
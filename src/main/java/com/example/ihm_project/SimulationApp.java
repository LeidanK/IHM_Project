package com.example.ihm_project; // <--- CHANGED PACKAGE

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Random;

public class SimulationApp {

    private static final String ARXIV_RED = "#b31b1b";
    private static final String BACKGROUND_GREY = "#f3f3f3";

    private ObservableList<Article> masterData = FXCollections.observableArrayList();
    private FilteredList<Article> filteredData;
    private SortedList<Article> sortedData;

    private TextField searchField;
    private CheckBox cbGalaxies, cbCosmo, cbPlanets;
    private ComboBox<Integer> yearSelect;
    private ToggleGroup monthGroup;
    private Label statusLabel;

    public Scene createScene(Stage stage, Scene mainScene) {
        // 1. GENERATE DATA
        generateFakeData();

        // 2. CONFIGURE LISTS
        filteredData = new FilteredList<>(masterData, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.setComparator(Comparator.comparingInt((Article a) -> a.year)
                .thenComparingInt(a -> a.month)
                .reversed());

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_GREY + ";");

        // --- TOP ---
        VBox topContainer = new VBox(0);
        
        HBox header = new HBox(15);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setStyle("-fx-background-color: " + ARXIV_RED + ";");
        header.setAlignment(Pos.CENTER_LEFT);
        
        // --- ADDED BACK BUTTON ---
        Button backBtn = new Button("← Retour Menu");
        backBtn.setStyle("-fx-background-color: white; -fx-text-fill: " + ARXIV_RED + "; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> stage.setScene(mainScene));
        // -------------------------

        Label logoText = new Label("arXiv Simulation");
        logoText.setTextFill(Color.WHITE);
        logoText.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        statusLabel = new Label("Mode Simulation");
        statusLabel.setTextFill(Color.web("#ffdddd"));
        
        header.getChildren().addAll(backBtn, logoText, spacer, statusLabel);

        HBox timelineBox = new HBox(15);
        timelineBox.setPadding(new Insets(10, 15, 10, 15));
        timelineBox.setAlignment(Pos.CENTER_LEFT);
        timelineBox.setStyle("-fx-background-color: " + ARXIV_RED + "; -fx-border-color: #9e1717; -fx-border-width: 1 0 0 0;"); 

        VBox yearBox = new VBox(2);
        Label lblYear = new Label("ANNÉE");
        lblYear.setTextFill(Color.web("#ffcccc"));
        lblYear.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        yearSelect = new ComboBox<>();
        yearSelect.getItems().addAll(2020, 2021, 2022, 2023, 2024, 2025);
        yearSelect.setValue(2024);
        yearSelect.setStyle("-fx-background-color: white; -fx-font-weight: bold;");
        yearSelect.setPrefWidth(80);
        yearSelect.valueProperty().addListener((obs, oldVal, newVal) -> applyLocalFilter());
        yearBox.getChildren().addAll(lblYear, yearSelect);

        Separator sep = new Separator(Orientation.VERTICAL);
        
        monthGroup = new ToggleGroup();
        monthGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> applyLocalFilter());

        String[] months = {"JAN", "FEB", "MAR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        HBox monthsBox = new HBox(8);
        for (int i = 0; i < months.length; i++) {
            monthsBox.getChildren().add(createMonthTile(months[i], i + 1, monthGroup));
        }
        timelineBox.getChildren().addAll(yearBox, sep, monthsBox);
        topContainer.getChildren().addAll(header, timelineBox);
        root.setTop(topContainer);

        // --- LEFT FILTERS ---
        VBox filtersBox = new VBox(15);
        filtersBox.setPadding(new Insets(20));
        filtersBox.setPrefWidth(240);
        filtersBox.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");
        
        Label filterTitle = new Label("Filtres");
        filterTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        filterTitle.setTextFill(Color.web(ARXIV_RED));
        
        searchField = new TextField();
        searchField.setPromptText("🔍 Titre, auteur...");
        searchField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #ccc; -fx-padding: 5 10 5 10;");
        searchField.textProperty().addListener((obs, old, val) -> applyLocalFilter());

        cbGalaxies = new CheckBox("Galaxies");
        cbCosmo = new CheckBox("Cosmology");
        cbPlanets = new CheckBox("Planetary");
        cbGalaxies.selectedProperty().addListener((obs, old, val) -> applyLocalFilter());
        cbCosmo.selectedProperty().addListener((obs, old, val) -> applyLocalFilter());
        cbPlanets.selectedProperty().addListener((obs, old, val) -> applyLocalFilter());
        
        Button resetBtn = new Button("Voir toute l'année");
        resetBtn.setStyle("-fx-background-color: #eee; -fx-cursor: hand;");
        resetBtn.setOnAction(e -> {
            if(monthGroup.getSelectedToggle() != null) monthGroup.getSelectedToggle().setSelected(false);
        });

        filtersBox.getChildren().addAll(filterTitle, searchField, new Separator(), cbGalaxies, cbCosmo, cbPlanets, new Separator(), resetBtn);
        root.setLeft(filtersBox);

        // --- CENTER LIST ---
        ListView<Article> paperList = new ListView<>();
        paperList.setItems(sortedData);
        paperList.setStyle("-fx-background-color: transparent; -fx-control-inner-background: " + BACKGROUND_GREY + ";");
        paperList.setPadding(new Insets(10));
        paperList.setPlaceholder(new Label("Aucune donnée trouvée."));

        paperList.setCellFactory(param -> new ListCell<Article>() {
            @Override
            protected void updateItem(Article item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-background-color: transparent; -fx-padding: 5px;"); 
                
                if (empty || item == null) {
                    setText(null); setGraphic(null);
                } else {
                    ArticleCard card = new ArticleCard(item, url -> System.out.println("Opening link: " + url));
                    setGraphic(card);
                }
            }
        });
        root.setCenter(paperList);

        Scene scene = new Scene(root, 1100, 750);
        applyLocalFilter(); // Init
        return scene;
    }

    private void generateFakeData() {
        masterData.clear(); // Ensure clean state
        String[] subjects = {"Dark Matter", "Exoplanet", "Black Hole", "Redshift", "Neutrino", "Star Formation", "Galaxy Cluster", "Cosmic Web", "Gravitational Wave", "Supernova", "Quantum Field", "String Theory"};
        String[] actions = {"Detection of", "Analysis of", "New constraints on", "Simulating", "Observations of", "The origin of", "Mapping the", "Dynamics of"};
        String[] authorsList = {"Smith J.", "Doe A.", "Einstein A.", "Hawking S.", "Sagan C.", "Turing A.", "Curie M.", "Newton I.", "Bohr N."};
        Random rand = new Random();

        for (int year = 2020; year <= 2025; year++) {
            for (int month = 1; month <= 12; month++) {
                int nbArticles = 5 + rand.nextInt(8);
                for (int i = 0; i < nbArticles; i++) {
                    String title = actions[rand.nextInt(actions.length)] + " " + subjects[rand.nextInt(subjects.length)];
                    String author = authorsList[rand.nextInt(authorsList.length)] + ", " + authorsList[rand.nextInt(authorsList.length)];
                    String cat = "Cosmology";
                    int r = rand.nextInt(3);
                    if (r == 1) cat = "Galaxies";
                    if (r == 2) cat = "Planetary";
                    if (rand.nextBoolean()) title += " in Sector " + rand.nextInt(99);
                    String summary = "This is a simulated abstract for the paper titled " + title + ".";
                    masterData.add(new Article(title, author, cat, summary, "https://arxiv.org", year, month));
                }
            }
        }
    }

    private void applyLocalFilter() {
        filteredData.setPredicate(article -> {
            if (article.year != yearSelect.getValue()) return false;
            if (monthGroup.getSelectedToggle() != null) {
                int selectedMonth = (int) monthGroup.getSelectedToggle().getUserData();
                if (article.month != selectedMonth) return false;
            }
            String searchText = searchField.getText();
            if (searchText != null && !searchText.isEmpty()) {
                String lower = searchText.toLowerCase();
                if (!article.title.toLowerCase().contains(lower) && !article.authors.toLowerCase().contains(lower)) return false;
            }
            boolean ga = cbGalaxies.isSelected();
            boolean co = cbCosmo.isSelected();
            boolean pl = cbPlanets.isSelected();
            if (ga || co || pl) {
                boolean match = false;
                if (ga && article.category.equals("Galaxies")) match = true;
                if (pl && article.category.equals("Planetary")) match = true;
                if (co && article.category.equals("Cosmology")) match = true;
                if (!match) return false;
            }
            return true;
        });
        statusLabel.setText(filteredData.size() + " articles affichés.");
    }

    private ToggleButton createMonthTile(String monthName, int monthNumber, ToggleGroup group) {
        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER);
        Label lblName = new Label(monthName);
        lblName.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
        content.getChildren().addAll(lblName);
        ToggleButton btn = new ToggleButton();
        btn.setGraphic(content);
        btn.setToggleGroup(group);
        btn.setUserData(monthNumber); 
        btn.setPrefSize(60, 50);
        String normalStyle = "-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 4; -fx-cursor: hand;";
        String selectedStyle = "-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 4; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 0, 1);";
        btn.setStyle(normalStyle);
        btn.selectedProperty().addListener((obs, oldVal, newVal) -> btn.setStyle(newVal ? selectedStyle : normalStyle));
        return btn;
    }
}
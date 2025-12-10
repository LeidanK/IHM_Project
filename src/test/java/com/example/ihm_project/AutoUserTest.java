package com.example.ihm_project;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutoUserTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        new HelloApplication().start(stage);

        // 1. Force Window to Top-Left (Critical for Robot Accuracy)
        stage.setX(0);
        stage.setY(0);

        // 2. Size
        stage.setWidth(1050);
        stage.setHeight(650);

        // 3. Focus
        stage.toFront();
        stage.requestFocus();
    }

    // --- HELPER METHODS ---

    private void scrollToBottom() {
        interact(() -> {
            lookup(".scroll-pane").queryAll().forEach(node -> {
                if (node instanceof ScrollPane)
                    ((ScrollPane) node).setVvalue(1.0);
            });
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    private void scrollToTop() {
        interact(() -> {
            lookup(".scroll-pane").queryAll().forEach(node -> {
                if (node instanceof ScrollPane)
                    ((ScrollPane) node).setVvalue(0.0);
            });
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }

    private void forceClick(String partialText) {
        Node node = lookup(".button").match(n -> n instanceof Button && ((Button) n).getText().contains(partialText))
                .query();

        moveTo(node);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        clickOn(node);
    }

    private void navigateToArticlesPage() {
        scrollToBottom();
        try {
            WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> lookup("Passer vers Articles").tryQuery().isPresent());
        } catch (TimeoutException e) {
        }

        clickOn((Node) lookup("Passer vers Articles").query());

        try {
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> lookup(".cornell-label").tryQuery().isPresent());
        } catch (TimeoutException e) {
            System.out.println("Timeout: ArxivApp did not load.");
        }
    }

    private void navigateToSimulationPage() {
        scrollToBottom();
        try {
            WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> lookup("Mode Simulation").tryQuery().isPresent());
        } catch (TimeoutException e) {
        }

        clickOn((Node) lookup("Mode Simulation").query());

        try {
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> lookup("arXiv Simulation").tryQuery().isPresent());
        } catch (TimeoutException e) {
            System.out.println("Timeout: SimulationApp did not load.");
        }
    }

    // --- TEST CASES ---

    @Test
    @Order(1)
    public void testLandingPageElements() {
        verifyThat(".root", isVisible());
        scrollToBottom();
        verifyThat("About", isVisible());
    }

    @Test
    @Order(2)
    public void testHomePageSearch() {
        scrollToTop();
        clickOn(".text-field").write("Computer");
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat(".text-field", (TextField tf) -> tf.getText().equals("Computer"));
    }

    @Test
    @Order(3)
    public void testNavigationToArticles() {
        navigateToArticlesPage();
        verifyThat(".cornell-label", isVisible());
        scrollToTop();
        forceClick("Retour");
    }

    @Test
    @Order(4)
    public void testArticleSearchFunction() {
        navigateToArticlesPage();
        scrollToTop();

        clickOn(".text-field").write("Language");
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat(".article-scroll", isVisible());

        scrollToTop();
        forceClick("Retour");
    }

    @Test
    @Order(5)
    public void testReturnNavigation() {
        navigateToArticlesPage();

        scrollToTop();
        forceClick("Retour");

        try {
            WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> lookup(".text-field").tryQuery().isPresent());
        } catch (TimeoutException e) {
        }

        scrollToBottom();
        verifyThat("Passer vers Articles", isVisible());
    }

    @Test
    @Order(6)
    public void testNavigationToSimulation() {
        navigateToSimulationPage();
        verifyThat("arXiv Simulation", isVisible());
    }

    @Test
    @Order(7)
    public void testSimulationFilters() {
        navigateToSimulationPage();

        Node searchField = lookup(".text-field").query();
        clickOn(searchField).write("Black Hole");
        clickOn("Galaxies");

        verifyThat("arXiv Simulation", isVisible());
    }

    @Test
    @Order(8)
    public void testDateFilterInteraction() {
        navigateToSimulationPage();

        Node comboBox = lookup(".combo-box").query();
        clickOn(comboBox);
        clickOn("2023");
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("JAN");
        WaitForAsyncUtils.waitForFxEvents();

        verifyThat(lookup(".label").match(n -> n instanceof Label && ((Label) n).getText().contains("articles")),
                isVisible());
    }

    @Test
    @Order(9)
    public void testSimulationReturn() {
        navigateToSimulationPage();
        forceClick("Retour Menu");

        try {
            WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> lookup("Passer vers Articles").tryQuery().isPresent());
        } catch (TimeoutException e) {
        }

        scrollToBottom();
        verifyThat("Mode Simulation", isVisible());
    }

    @Test
    @Order(10)
    public void testInvalidHomePageSearchInput() {
        scrollToTop();
        TextField searchField = lookup(".text-field").query();
        clickOn(searchField).eraseText(searchField.getText().length());
        WaitForAsyncUtils.waitForFxEvents();

        clickOn(searchField).write("@@@###");
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    @Order(11)
    public void testSimulationFilterReset() {
        navigateToSimulationPage();

        Node searchField = lookup(".text-field").query();
        clickOn(searchField).write("Quantum");
        clickOn("Galaxies");
        WaitForAsyncUtils.waitForFxEvents();

        if (lookup("Reset").tryQuery().isPresent()) {
            clickOn("Reset");
            WaitForAsyncUtils.waitForFxEvents();
        }
    }
}
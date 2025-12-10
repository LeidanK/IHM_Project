package com.example.ihm_project;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class TestReportRunner {

    // Helper class to store result data
    static class TestResultData {
        String name;
        String status; // PASS or FAIL
        long duration; // in ms
        String errorMessage;

        public TestResultData(String name, String status, long duration, String errorMessage) {
            this.name = name;
            this.status = status;
            this.duration = duration;
            this.errorMessage = errorMessage;
        }
    }

    public static void main(String[] args) {
        // 1. Setup Launcher
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(AutoUserTest.class))
                .build();

        Launcher launcher = LauncherFactory.create();
        List<TestResultData> results = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        // 2. Register Listener
        launcher.registerTestExecutionListeners(new TestExecutionListener() {
            long testStart;

            @Override
            public void executionStarted(TestIdentifier testIdentifier) {
                if (testIdentifier.isTest()) {
                    testStart = System.currentTimeMillis();
                    System.out.println("Running: " + testIdentifier.getDisplayName());
                }
            }

            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                if (testIdentifier.isTest()) {
                    long duration = System.currentTimeMillis() - testStart;
                    String status = testExecutionResult.getStatus() == TestExecutionResult.Status.SUCCESSFUL ? "PASS" : "FAIL";
                    String error = testExecutionResult.getThrowable().map(Throwable::getMessage).orElse("");
                    results.add(new TestResultData(testIdentifier.getDisplayName(), status, duration, error));
                }
            }
        });

        // 3. Run Tests
        System.out.println("Starting Test Suite...");
        launcher.execute(request);
        long totalTime = System.currentTimeMillis() - startTime;

        // 4. Generate Reports
        String htmlContent = generateHtmlString(results, totalTime);
        
        // Save HTML
        try (PrintWriter out = new PrintWriter(new FileWriter("TestReport.html"))) {
            out.println(htmlContent);
            System.out.println("HTML Report generated: TestReport.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save PDF
        try (OutputStream os = new FileOutputStream("TestReport.pdf")) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);
            builder.run();
            System.out.println("PDF Report generated: TestReport.pdf");
        } catch (Exception e) {
            System.err.println("Error creating PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String generateHtmlString(List<TestResultData> results, long totalDuration) {
        long passed = results.stream().filter(r -> r.status.equals("PASS")).count();
        long failed = results.stream().filter(r -> r.status.equals("FAIL")).count();

        StringBuilder html = new StringBuilder();
        // PDF Generator requires strict XHTML (Closing all tags, using style tags correctly)
        html.append("<!DOCTYPE html>");
        html.append("<html><head><title>Full Project Test Report</title>");
        html.append("<style>");
        html.append("body { font-family: sans-serif; margin: 40px; background-color: #ffffff; }");
        html.append("h1 { color: #333; border-bottom: 2px solid #b31b1b; padding-bottom: 10px; }");
        html.append(".summary-box { width: 100%; overflow: hidden; margin-bottom: 30px; }");
        html.append(".card { float: left; width: 30%; padding: 15px; margin-right: 3%; border-radius: 5px; text-align: center; color: white; font-weight: bold; }");
        html.append(".bg-blue { background-color: #2196F3; }");
        html.append(".bg-green { background-color: #4CAF50; }");
        html.append(".bg-red { background-color: #F44336; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; border: 1px solid #ddd; }");
        html.append("th, td { padding: 10px; border-bottom: 1px solid #ddd; text-align: left; font-size: 12px; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".badge-pass { color: #2e7d32; font-weight: bold; }");
        html.append(".badge-fail { color: #c62828; font-weight: bold; }");
        html.append("</style></head><body>");

        html.append("<h1>Software Quality Test Report</h1>");
        html.append("<p><strong>Project:</strong> IHM Project (Main + Simulation)</p>");
        html.append("<p><strong>Date:</strong> ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</p>");

        html.append("<div class='summary-box'>");
        html.append("<div class='card bg-blue'>Total Tests<br/><span style='font-size:24px'>").append(results.size()).append("</span></div>");
        html.append("<div class='card bg-green'>Passed<br/><span style='font-size:24px'>").append(passed).append("</span></div>");
        // Only show failed card if there are failures
        if (failed > 0) {
            html.append("<div class='card bg-red'>Failed<br/><span style='font-size:24px'>").append(failed).append("</span></div>");
        } else {
             html.append("<div class='card bg-green'>Failed<br/><span style='font-size:24px'>0</span></div>");
        }
        html.append("</div>");
        
        // Clear float
        html.append("<div style='clear:both;'></div>");

        html.append("<h3>Test Scope</h3>");
        html.append("<ul>");
        html.append("<li><strong>HelloApplication:</strong> Navigation, Footer, Main Search.</li>");
        html.append("<li><strong>ArxivApp:</strong> Live API Fetching, Article List.</li>");
        html.append("<li><strong>SimulationApp:</strong> Date Filters, Category Checkboxes, Mock Data.</li>");
        html.append("</ul>");

        html.append("<table>");
        html.append("<thead><tr><th>Test Case</th><th>Status</th><th>Duration</th><th>Details</th></tr></thead>");
        html.append("<tbody>");

        for (TestResultData res : results) {
            String badge = res.status.equals("PASS") ? "badge-pass" : "badge-fail";
            html.append("<tr>");
            html.append("<td>").append(res.name.replace("()", "")).append("</td>");
            html.append("<td><span class='").append(badge).append("'>").append(res.status).append("</span></td>");
            html.append("<td>").append(res.duration).append(" ms</td>");
            // Escape null error messages
            String safeError = res.errorMessage.replace("<", "&lt;").replace(">", "&gt;");
            html.append("<td style='color:red; font-size: 0.9em;'>").append(safeError).append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody></table>");
        html.append("</body></html>");
        
        return html.toString();
    }
}
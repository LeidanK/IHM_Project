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
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javafx.scene.control.TextField;

import static com.example.ihm_project.Card.createCategoryCard;
import static com.example.ihm_project.WebContentFetcher.extractBetween;
import static com.example.ihm_project.WebContentFetcher.extractSections;

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
        Hyperlink help = new Hyperlink("Help");
        Hyperlink contact = new Hyperlink("Contact");
        Hyperlink subscribe = new Hyperlink("Subscribe");
        Hyperlink copyright = new Hyperlink("Copyright");
        Hyperlink privacy = new Hyperlink("Privacy Policy");
        Hyperlink assistance = new Hyperlink("Web Accessibility Assistance");
        Hyperlink status = new Hyperlink("arXiv Operational Status");

        Hyperlink[] footerList = {about, help, contact, subscribe, copyright, privacy, assistance, status};
        String url = "https://info.arxiv.org/";
        String[] urls = {"about", "help", "help/contact.html", "help/subscribe", "help/license/index.html", "help/policies/privacy_policy.html", "help/web_accessibility.html", "https://status.arxiv.org/"};

        for(int i = 0; i < 7; i++) {
            int finalI = i;
            footerList[i].setOnAction(e -> {
                    try {
                        // Ouvre le lien complet dans le navigateur
                        Desktop.getDesktop().browse(
                                new URI(url + urls[finalI])
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            });
        }

        footer.add(about, 0, 0);
        footer.add(help, 0, 1);
        footer.add(contact, 1, 0);
        footer.add(subscribe, 1, 1);
        footer.add(copyright, 2, 0);
        footer.add(privacy, 2, 1);
        footer.add(assistance, 3, 0);
        footer.add(status, 3, 1);


        // Catégories
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        // Données extraites de la page web
        StringBuilder content = new StringBuilder("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>  <title>arXiv.org e-Print archive</title>\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <link rel=\"apple-touch-icon\" sizes=\"180x180\" href=\"/static/browse/0.3.4/images/icons/apple-touch-icon.png\">\n" +
                "  <link rel=\"icon\" type=\"image/png\" sizes=\"32x32\" href=\"/static/browse/0.3.4/images/icons/favicon-32x32.png\">\n" +
                "  <link rel=\"icon\" type=\"image/png\" sizes=\"16x16\" href=\"/static/browse/0.3.4/images/icons/favicon-16x16.png\">\n" +
                "  <link rel=\"manifest\" href=\"/static/browse/0.3.4/images/icons/site.webmanifest\">\n" +
                "  <link rel=\"mask-icon\" href=\"/static/browse/0.3.4/images/icons/safari-pinned-tab.svg\" color=\"#5bbad5\">\n" +
                "  <meta name=\"msapplication-TileColor\" content=\"#da532c\">\n" +
                "  <meta name=\"theme-color\" content=\"#ffffff\">\n" +
                "  <link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"/static/browse/0.3.4/css/arXiv.css?v=20241206\" />\n" +
                "  <link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"/static/browse/0.3.4/css/arXiv-print.css?v=20200611\" />\n" +
                "  <link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"/static/browse/0.3.4/css/browse_search.css\" />\n" +
                "  <script language=\"javascript\" src=\"/static/browse/0.3.4/js/accordion.js\" ></script>\n" +
                "  <script language=\"javascript\" src=\"/static/browse/0.3.4/js/optin-modal.js?v=20250819\"></script>\n" +
                "  \n" +
                "  </head>\n" +
                "\n" +
                "<body id=\"front\" class=\"with-cu-identity\">\n" +
                "  \n" +
                "  \n" +
                "  <div class=\"flex-wrap-footer\">\n" +
                "    <header>\n" +
                "      <a href=\"#content\" class=\"is-sr-only\">Skip to main content</a>\n" +
                "      <!-- start desktop header -->\n" +
                "      <div class=\"columns is-vcentered is-hidden-mobile\" id=\"cu-identity\">\n" +
                "        <div class=\"column\" id=\"cu-logo\">\n" +
                "          <a href=\"https://www.cornell.edu/\"><img src=\"/static/browse/0.3.4/images/icons/cu/cornell-reduced-white-SMALL.svg\" alt=\"Cornell University\" /></a>\n" +
                "        </div><!-- /from April 7 at 1:00 AM to May 29 at 21:40 --><!-- /from May 2 at 1:00 AM to May 5 at 9:45 AM --><div class=\"column\" id=\"support-ack\">\n" +
                "          <span id=\"support-ack-url\">We gratefully acknowledge support from the Simons Foundation, <a href=\"https://info.arxiv.org/about/ourmembers.html\">member institutions</a>, and all contributors.</span>\n" +
                "          <a href=\"https://info.arxiv.org/about/donate.html\" class=\"btn-header-donate\">Donate</a>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <div id=\"header\" class=\"is-hidden-mobile\">\n" +
                "<a aria-hidden=\"true\" tabindex=\"-1\" href=\"/IgnoreMe\"></a>\n" +
                "<h1><img src=\"/static/browse/0.3.4/images/arxiv-logo-one-color-white.svg\" alt=\"arxiv logo\" style=\"height:60px;\"/></h1>\n" +
                "        <div class=\"columns is-vcentered is-mobile\" style=\"justify-content: flex-end;\">\n" +
                "<div class=\"column is-narrow\" id=\"opt-in-status-button-container\" style=\"display:none;\">\n" +
                "  <button id=\"opt-in-status-button\" class=\"button is-small is-light btn-header-donate\"  title=\"\">Status</button>\n" +
                "</div><div class=\"login\">\n" +
                "  <a href=\"https://arxiv.org/login\">Login</a>\n" +
                "</div>        </div>\n" +
                "\n" +
                "          <div class=\"search-block level-right\">\n" +
                "    <form class=\"level-item mini-search\" method=\"GET\" action=\"https://arxiv.org/search\">\n" +
                "      <div class=\"field has-addons\">\n" +
                "        <div class=\"control\">\n" +
                "          <input class=\"input is-small\" type=\"text\" name=\"query\" placeholder=\"Search...\" aria-label=\"Search term or terms\" />\n" +
                "          <p class=\"help\"><a href=\"https://info.arxiv.org/help\">Help</a> | <a href=\"https://arxiv.org/search/advanced\">Advanced Search</a></p>\n" +
                "        </div>\n" +
                "        <div class=\"control\">\n" +
                "          <div class=\"select is-small\">\n" +
                "            <select name=\"searchtype\" aria-label=\"Field to search\">\n" +
                "              <option value=\"all\" selected=\"selected\">All fields</option>\n" +
                "              <option value=\"title\">Title</option>\n" +
                "              <option value=\"author\">Author</option>\n" +
                "              <option value=\"abstract\">Abstract</option>\n" +
                "              <option value=\"comments\">Comments</option>\n" +
                "              <option value=\"journal_ref\">Journal reference</option>\n" +
                "              <option value=\"acm_class\">ACM classification</option>\n" +
                "              <option value=\"msc_class\">MSC classification</option>\n" +
                "              <option value=\"report_num\">Report number</option>\n" +
                "              <option value=\"paper_id\">arXiv identifier</option>\n" +
                "              <option value=\"doi\">DOI</option>\n" +
                "              <option value=\"orcid\">ORCID</option>\n" +
                "              <option value=\"author_id\">arXiv author ID</option>\n" +
                "              <option value=\"help\">Help pages</option>\n" +
                "              <option value=\"full_text\">Full text</option>\n" +
                "            </select>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <input type=\"hidden\" name=\"source\" value=\"header\">\n" +
                "        <button class=\"button is-small is-cul-darker\">Search</button>\n" +
                "      </div>\n" +
                "    </form>\n" +
                "  </div>\n" +
                "     </div><!-- /end desktop header -->\n" +
                "\n" +
                "      <div class=\"mobile-header\">\n" +
                "        <div class=\"columns is-mobile\">\n" +
                "          <div class=\"column logo-arxiv\"><a href=\"https://arxiv.org/\"><img src=\"/static/browse/0.3.4/images/arxiv-logomark-small-white.svg\" alt=\"arXiv logo\" style=\"height:60px;\" /></a></div>\n" +
                "          <div class=\"column logo-cornell\"><a href=\"https://www.cornell.edu/\">\n" +
                "            <picture>\n" +
                "              <source media=\"(min-width: 501px)\"\n" +
                "                srcset=\"/static/browse/0.3.4/images/icons/cu/cornell-reduced-white-SMALL.svg  400w\"\n" +
                "                sizes=\"400w\" />\n" +
                "              <source srcset=\"/static/browse/0.3.4/images/icons/cu/cornell_seal_simple_black.svg 2x\" />\n" +
                "              <img src=\"/static/browse/0.3.4/images/icons/cu/cornell-reduced-white-SMALL.svg\" alt=\"Cornell University Logo\" />\n" +
                "            </picture>\n" +
                "          </a></div>\n" +
                "          <div class=\"column nav\" id=\"toggle-container\" role=\"menubar\">\n" +
                "            <button class=\"toggle-control\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\" class=\"icon filter-white\"><title>open search</title><path d=\"M505 442.7L405.3 343c-4.5-4.5-10.6-7-17-7H372c27.6-35.3 44-79.7 44-128C416 93.1 322.9 0 208 0S0 93.1 0 208s93.1 208 208 208c48.3 0 92.7-16.4 128-44v16.3c0 6.4 2.5 12.5 7 17l99.7 99.7c9.4 9.4 24.6 9.4 33.9 0l28.3-28.3c9.4-9.4 9.4-24.6.1-34zM208 336c-70.7 0-128-57.2-128-128 0-70.7 57.2-128 128-128 70.7 0 128 57.2 128 128 0 70.7-57.2 128-128 128z\"/></svg></button>\n" +
                "            <div class=\"mobile-toggle-block toggle-target\">\n" +
                "              <form class=\"mobile-search-form\" method=\"GET\" action=\"https://arxiv.org/search\">\n" +
                "                <div class=\"field has-addons\">\n" +
                "                  <input class=\"input\" type=\"text\" name=\"query\" placeholder=\"Search...\" aria-label=\"Search term or terms\" />\n" +
                "                  <input type=\"hidden\" name=\"source\" value=\"header\">\n" +
                "                  <input type=\"hidden\" name=\"searchtype\" value=\"all\">\n" +
                "                  <button class=\"button\">GO</button>\n" +
                "                </div>\n" +
                "              </form>\n" +
                "            </div>\n" +
                "\n" +
                "            <button class=\"toggle-control\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 448 512\" class=\"icon filter-white\" role=\"menu\"><title>open navigation menu</title><path d=\"M16 132h416c8.837 0 16-7.163 16-16V76c0-8.837-7.163-16-16-16H16C7.163 60 0 67.163 0 76v40c0 8.837 7.163 16 16 16zm0 160h416c8.837 0 16-7.163 16-16v-40c0-8.837-7.163-16-16-16H16c-8.837 0-16 7.163-16 16v40c0 8.837 7.163 16 16 16zm0 160h416c8.837 0 16-7.163 16-16v-40c0-8.837-7.163-16-16-16H16c-8.837 0-16 7.163-16 16v40c0 8.837 7.163 16 16 16z\"/ ></svg></button>\n" +
                "            <div class=\"mobile-toggle-block toggle-target\">\n" +
                "              <nav class=\"mobile-menu\" aria-labelledby=\"mobilemenulabel\">\n" +
                "                <h2 id=\"mobilemenulabel\">quick links</h2>\n" +
                "                <ul>\n" +
                "                    <li><a href=\"https://arxiv.org/login\">Login</a></li>\n" +
                "                    <li><a href=\"https://info.arxiv.org/help\">Help Pages</a></li>\n" +
                "                    <li><a href=\"https://info.arxiv.org/about\">About</a></li>\n" +
                "                </ul>\n" +
                "              </nav>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div><!-- /end mobile-header -->\n" +
                "    </header>\n" +
                "\n" +
                "    <main>\n" +
                "      <div id=\"content\">\n" +
                "<div class=\"columns\">\n" +
                "  <div class=\"column intro-and-news is-half-desktop\">\n" +
                "    <p class=\"tagline\">arXiv is a free distribution service and an open-access archive for nearly 2.4 million\n" +
                "      scholarly articles in the fields of physics, mathematics, computer science, quantitative biology, quantitative finance, statistics, electrical engineering and systems science, and economics.\n" +
                "      Materials on this site are not peer-reviewed by arXiv.\n" +
                "     </p>\n" +
                "    <!-- <p class=\"tagline\">arXiv is a free distribution service and an open-access archive for      scholarly articles in the fields of physics, mathematics, computer science, quantitative biology, quantitative finance, statistics, electrical engineering and systems science, and economics.\n" +
                "     Materials on this site are not peer-reviewed by arXiv.\n" +
                "    </p> --><form name=\"home-adv-search\" class=\"home-search\" action=\"/multi\" method=\"get\" role=\"search\">\n" +
                "      <label for=\"search-category\">Subject search and browse:</label><br>\n" +
                "      <select name=\"group\" title=\"Search in\" id=\"search-category\">        <option\n" +
                "            value =\"grp_physics\"\n" +
                "            data-url=\"https://arxiv.org/search/physics\"\n" +
                "selected=\"selected\">\n" +
                "          Physics\n" +
                "        </option>        <option\n" +
                "            value =\"grp_math\"\n" +
                "            data-url=\"https://arxiv.org/search/math\"\n" +
                ">\n" +
                "          Mathematics\n" +
                "        </option>        <option\n" +
                "            value =\"grp_q-bio\"\n" +
                "            data-url=\"https://arxiv.org/search/q-bio\"\n" +
                ">\n" +
                "          Quantitative Biology\n" +
                "        </option>        <option\n" +
                "            value =\"grp_cs\"\n" +
                "            data-url=\"https://arxiv.org/search/cs\"\n" +
                ">\n" +
                "          Computer Science\n" +
                "        </option>        <option\n" +
                "            value =\"grp_q-fin\"\n" +
                "            data-url=\"https://arxiv.org/search/q-fin\"\n" +
                ">\n" +
                "          Quantitative Finance\n" +
                "        </option>        <option\n" +
                "            value =\"grp_stat\"\n" +
                "            data-url=\"https://arxiv.org/search/stat\"\n" +
                ">\n" +
                "          Statistics\n" +
                "        </option>        <option\n" +
                "            value =\"grp_eess\"\n" +
                "            data-url=\"https://arxiv.org/search/eess\"\n" +
                ">\n" +
                "          Electrical Engineering and Systems Science\n" +
                "        </option>        <option\n" +
                "            value =\"grp_econ\"\n" +
                "            data-url=\"https://arxiv.org/search/econ\"\n" +
                ">\n" +
                "          Economics\n" +
                "        </option>      </select>\n" +
                "      <input id=\"adv-search-btn\" type=\"button\" value=\"Search\">\n" +
                "      <input type=\"submit\" name=\"/form\" value=\"Form Interface\">\n" +
                "      <input id=\"catchup-btn\" type=\"submit\" name=\"/catchup\" value=\"Catchup\">\n" +
                "    </form>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      //catchup no long hosted from multi\n" +
                "      document.getElementById('catchup-btn').addEventListener('click', function(event) {\n" +
                "        document.querySelector('form[name=\"home-adv-search\"]').action = \"/catchup\";\n" +
                "      });\n" +
                "\n" +
                "     function doAdvSearchBtn(event) {\n" +
                "         sel = document.querySelector('select[name=\"group\"]')\n" +
                "         if(sel && sel.options && sel.options[sel.selectedIndex].dataset.url ){\n" +
                "             data_url = sel.options[sel.selectedIndex].dataset.url\n" +
                "             if( data_url ){\n" +
                "                 window.location = data_url;\n" +
                "             }else{\n" +
                "                 console.error('home page search button: no data_url found for search');\n" +
                "             }\n" +
                "         }\n" +
                "     }\n" +
                "     document.addEventListener('DOMContentLoaded',function() {\n" +
                "         document.getElementById('adv-search-btn').onclick=doAdvSearchBtn;\n" +
                "     },false);\n" +
                "    </script>\n" +
                "  </div>\n" +
                "  <!-- special message column --></div><!-- /end columns -->  <h2>Physics</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"/archive/astro-ph\" id=\"main-astro-ph\" aria-labelledby=\"main-astro-ph\">Astrophysics</a>\n" +
                "      (<strong id=\"astro-ph\">astro-ph</strong>\n" +
                "      <a id=\"new-astro-ph\" aria-labelledby=\"new-astro-ph astro-ph\" href=\"/list/astro-ph/new\">new</a>,\n" +
                "      <a id=\"recent-astro-ph\" aria-labelledby=\"recent-astro-ph astro-ph\" href=\"/list/astro-ph/recent\">recent</a>,\n" +
                "      <a id=\"search-astro-ph\" aria-labelledby=\"search-astro-ph astro-ph\" href=\"https://arxiv.org/search/astro-ph\">search</a>)\n" +
                "\n" +
                "<a href=\"/list/astro-ph.GA/recent\" id=\"astro-ph.GA\" aria-labelledby=\"main-astro-ph astro-ph.GA\">Astrophysics of Galaxies</a>; <a href=\"/list/astro-ph.CO/recent\" id=\"astro-ph.CO\" aria-labelledby=\"main-astro-ph astro-ph.CO\">Cosmology and Nongalactic Astrophysics</a>; <a href=\"/list/astro-ph.EP/recent\" id=\"astro-ph.EP\" aria-labelledby=\"main-astro-ph astro-ph.EP\">Earth and Planetary Astrophysics</a>; <a href=\"/list/astro-ph.HE/recent\" id=\"astro-ph.HE\" aria-labelledby=\"main-astro-ph astro-ph.HE\">High Energy Astrophysical Phenomena</a>; <a href=\"/list/astro-ph.IM/recent\" id=\"astro-ph.IM\" aria-labelledby=\"main-astro-ph astro-ph.IM\">Instrumentation and Methods for Astrophysics</a>; <a href=\"/list/astro-ph.SR/recent\" id=\"astro-ph.SR\" aria-labelledby=\"main-astro-ph astro-ph.SR\">Solar and Stellar Astrophysics</a>    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/cond-mat\" id=\"main-cond-mat\" aria-labelledby=\"main-cond-mat\">Condensed Matter</a>\n" +
                "      (<strong id=\"cond-mat\">cond-mat</strong>\n" +
                "      <a id=\"new-cond-mat\" aria-labelledby=\"new-cond-mat cond-mat\" href=\"/list/cond-mat/new\">new</a>,\n" +
                "      <a id=\"recent-cond-mat\" aria-labelledby=\"recent-cond-mat cond-mat\" href=\"/list/cond-mat/recent\">recent</a>,\n" +
                "      <a id=\"search-cond-mat\" aria-labelledby=\"search-cond-mat cond-mat\" href=\"https://arxiv.org/search/cond-mat\">search</a>)\n" +
                "\n" +
                "<a href=\"/list/cond-mat.dis-nn/recent\" id=\"cond-mat.dis-nn\" aria-labelledby=\"main-cond-mat cond-mat.dis-nn\">Disordered Systems and Neural Networks</a>; <a href=\"/list/cond-mat.mtrl-sci/recent\" id=\"cond-mat.mtrl-sci\" aria-labelledby=\"main-cond-mat cond-mat.mtrl-sci\">Materials Science</a>; <a href=\"/list/cond-mat.mes-hall/recent\" id=\"cond-mat.mes-hall\" aria-labelledby=\"main-cond-mat cond-mat.mes-hall\">Mesoscale and Nanoscale Physics</a>; <a href=\"/list/cond-mat.other/recent\" id=\"cond-mat.other\" aria-labelledby=\"main-cond-mat cond-mat.other\">Other Condensed Matter</a>; <a href=\"/list/cond-mat.quant-gas/recent\" id=\"cond-mat.quant-gas\" aria-labelledby=\"main-cond-mat cond-mat.quant-gas\">Quantum Gases</a>; <a href=\"/list/cond-mat.soft/recent\" id=\"cond-mat.soft\" aria-labelledby=\"main-cond-mat cond-mat.soft\">Soft Condensed Matter</a>; <a href=\"/list/cond-mat.stat-mech/recent\" id=\"cond-mat.stat-mech\" aria-labelledby=\"main-cond-mat cond-mat.stat-mech\">Statistical Mechanics</a>; <a href=\"/list/cond-mat.str-el/recent\" id=\"cond-mat.str-el\" aria-labelledby=\"main-cond-mat cond-mat.str-el\">Strongly Correlated Electrons</a>; <a href=\"/list/cond-mat.supr-con/recent\" id=\"cond-mat.supr-con\" aria-labelledby=\"main-cond-mat cond-mat.supr-con\">Superconductivity</a>    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/gr-qc\" id=\"main-gr-qc\" aria-labelledby=\"main-gr-qc\">General Relativity and Quantum Cosmology</a>\n" +
                "      (<strong id=\"gr-qc\">gr-qc</strong>\n" +
                "      <a id=\"new-gr-qc\" aria-labelledby=\"new-gr-qc gr-qc\" href=\"/list/gr-qc/new\">new</a>,\n" +
                "      <a id=\"recent-gr-qc\" aria-labelledby=\"recent-gr-qc gr-qc\" href=\"/list/gr-qc/recent\">recent</a>,\n" +
                "      <a id=\"search-gr-qc\" aria-labelledby=\"search-gr-qc gr-qc\" href=\"https://arxiv.org/search/gr-qc\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/hep-ex\" id=\"main-hep-ex\" aria-labelledby=\"main-hep-ex\">High Energy Physics - Experiment</a>\n" +
                "      (<strong id=\"hep-ex\">hep-ex</strong>\n" +
                "      <a id=\"new-hep-ex\" aria-labelledby=\"new-hep-ex hep-ex\" href=\"/list/hep-ex/new\">new</a>,\n" +
                "      <a id=\"recent-hep-ex\" aria-labelledby=\"recent-hep-ex hep-ex\" href=\"/list/hep-ex/recent\">recent</a>,\n" +
                "      <a id=\"search-hep-ex\" aria-labelledby=\"search-hep-ex hep-ex\" href=\"https://arxiv.org/search/hep-ex\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/hep-lat\" id=\"main-hep-lat\" aria-labelledby=\"main-hep-lat\">High Energy Physics - Lattice</a>\n" +
                "      (<strong id=\"hep-lat\">hep-lat</strong>\n" +
                "      <a id=\"new-hep-lat\" aria-labelledby=\"new-hep-lat hep-lat\" href=\"/list/hep-lat/new\">new</a>,\n" +
                "      <a id=\"recent-hep-lat\" aria-labelledby=\"recent-hep-lat hep-lat\" href=\"/list/hep-lat/recent\">recent</a>,\n" +
                "      <a id=\"search-hep-lat\" aria-labelledby=\"search-hep-lat hep-lat\" href=\"https://arxiv.org/search/hep-lat\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/hep-ph\" id=\"main-hep-ph\" aria-labelledby=\"main-hep-ph\">High Energy Physics - Phenomenology</a>\n" +
                "      (<strong id=\"hep-ph\">hep-ph</strong>\n" +
                "      <a id=\"new-hep-ph\" aria-labelledby=\"new-hep-ph hep-ph\" href=\"/list/hep-ph/new\">new</a>,\n" +
                "      <a id=\"recent-hep-ph\" aria-labelledby=\"recent-hep-ph hep-ph\" href=\"/list/hep-ph/recent\">recent</a>,\n" +
                "      <a id=\"search-hep-ph\" aria-labelledby=\"search-hep-ph hep-ph\" href=\"https://arxiv.org/search/hep-ph\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/hep-th\" id=\"main-hep-th\" aria-labelledby=\"main-hep-th\">High Energy Physics - Theory</a>\n" +
                "      (<strong id=\"hep-th\">hep-th</strong>\n" +
                "      <a id=\"new-hep-th\" aria-labelledby=\"new-hep-th hep-th\" href=\"/list/hep-th/new\">new</a>,\n" +
                "      <a id=\"recent-hep-th\" aria-labelledby=\"recent-hep-th hep-th\" href=\"/list/hep-th/recent\">recent</a>,\n" +
                "      <a id=\"search-hep-th\" aria-labelledby=\"search-hep-th hep-th\" href=\"https://arxiv.org/search/hep-th\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/math-ph\" id=\"main-math-ph\" aria-labelledby=\"main-math-ph\">Mathematical Physics</a>\n" +
                "      (<strong id=\"math-ph\">math-ph</strong>\n" +
                "      <a id=\"new-math-ph\" aria-labelledby=\"new-math-ph math-ph\" href=\"/list/math-ph/new\">new</a>,\n" +
                "      <a id=\"recent-math-ph\" aria-labelledby=\"recent-math-ph math-ph\" href=\"/list/math-ph/recent\">recent</a>,\n" +
                "      <a id=\"search-math-ph\" aria-labelledby=\"search-math-ph math-ph\" href=\"https://arxiv.org/search/math-ph\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/nlin\" id=\"main-nlin\" aria-labelledby=\"main-nlin\">Nonlinear Sciences</a>\n" +
                "      (<strong id=\"nlin\">nlin</strong>\n" +
                "      <a id=\"new-nlin\" aria-labelledby=\"new-nlin nlin\" href=\"/list/nlin/new\">new</a>,\n" +
                "      <a id=\"recent-nlin\" aria-labelledby=\"recent-nlin nlin\" href=\"/list/nlin/recent\">recent</a>,\n" +
                "      <a id=\"search-nlin\" aria-labelledby=\"search-nlin nlin\" href=\"https://arxiv.org/search/nlin\">search</a>)\n" +
                "      <br/> includes:\n" +
                "\n" +
                "<a href=\"/list/nlin.AO/recent\" id=\"nlin.AO\" aria-labelledby=\"main-nlin nlin.AO\">Adaptation and Self-Organizing Systems</a>; <a href=\"/list/nlin.CG/recent\" id=\"nlin.CG\" aria-labelledby=\"main-nlin nlin.CG\">Cellular Automata and Lattice Gases</a>; <a href=\"/list/nlin.CD/recent\" id=\"nlin.CD\" aria-labelledby=\"main-nlin nlin.CD\">Chaotic Dynamics</a>; <a href=\"/list/nlin.SI/recent\" id=\"nlin.SI\" aria-labelledby=\"main-nlin nlin.SI\">Exactly Solvable and Integrable Systems</a>; <a href=\"/list/nlin.PS/recent\" id=\"nlin.PS\" aria-labelledby=\"main-nlin nlin.PS\">Pattern Formation and Solitons</a>    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/nucl-ex\" id=\"main-nucl-ex\" aria-labelledby=\"main-nucl-ex\">Nuclear Experiment</a>\n" +
                "      (<strong id=\"nucl-ex\">nucl-ex</strong>\n" +
                "      <a id=\"new-nucl-ex\" aria-labelledby=\"new-nucl-ex nucl-ex\" href=\"/list/nucl-ex/new\">new</a>,\n" +
                "      <a id=\"recent-nucl-ex\" aria-labelledby=\"recent-nucl-ex nucl-ex\" href=\"/list/nucl-ex/recent\">recent</a>,\n" +
                "      <a id=\"search-nucl-ex\" aria-labelledby=\"search-nucl-ex nucl-ex\" href=\"https://arxiv.org/search/nucl-ex\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/nucl-th\" id=\"main-nucl-th\" aria-labelledby=\"main-nucl-th\">Nuclear Theory</a>\n" +
                "      (<strong id=\"nucl-th\">nucl-th</strong>\n" +
                "      <a id=\"new-nucl-th\" aria-labelledby=\"new-nucl-th nucl-th\" href=\"/list/nucl-th/new\">new</a>,\n" +
                "      <a id=\"recent-nucl-th\" aria-labelledby=\"recent-nucl-th nucl-th\" href=\"/list/nucl-th/recent\">recent</a>,\n" +
                "      <a id=\"search-nucl-th\" aria-labelledby=\"search-nucl-th nucl-th\" href=\"https://arxiv.org/search/nucl-th\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/physics\" id=\"main-physics\" aria-labelledby=\"main-physics\">Physics</a>\n" +
                "      (<strong id=\"physics\">physics</strong>\n" +
                "      <a id=\"new-physics\" aria-labelledby=\"new-physics physics\" href=\"/list/physics/new\">new</a>,\n" +
                "      <a id=\"recent-physics\" aria-labelledby=\"recent-physics physics\" href=\"/list/physics/recent\">recent</a>,\n" +
                "      <a id=\"search-physics\" aria-labelledby=\"search-physics physics\" href=\"https://arxiv.org/search/physics\">search</a>)\n" +
                "      <br/> includes:\n" +
                "\n" +
                "<a href=\"/list/physics.acc-ph/recent\" id=\"physics.acc-ph\" aria-labelledby=\"main-physics physics.acc-ph\">Accelerator Physics</a>; <a href=\"/list/physics.app-ph/recent\" id=\"physics.app-ph\" aria-labelledby=\"main-physics physics.app-ph\">Applied Physics</a>; <a href=\"/list/physics.ao-ph/recent\" id=\"physics.ao-ph\" aria-labelledby=\"main-physics physics.ao-ph\">Atmospheric and Oceanic Physics</a>; <a href=\"/list/physics.atm-clus/recent\" id=\"physics.atm-clus\" aria-labelledby=\"main-physics physics.atm-clus\">Atomic and Molecular Clusters</a>; <a href=\"/list/physics.atom-ph/recent\" id=\"physics.atom-ph\" aria-labelledby=\"main-physics physics.atom-ph\">Atomic Physics</a>; <a href=\"/list/physics.bio-ph/recent\" id=\"physics.bio-ph\" aria-labelledby=\"main-physics physics.bio-ph\">Biological Physics</a>; <a href=\"/list/physics.chem-ph/recent\" id=\"physics.chem-ph\" aria-labelledby=\"main-physics physics.chem-ph\">Chemical Physics</a>; <a href=\"/list/physics.class-ph/recent\" id=\"physics.class-ph\" aria-labelledby=\"main-physics physics.class-ph\">Classical Physics</a>; <a href=\"/list/physics.comp-ph/recent\" id=\"physics.comp-ph\" aria-labelledby=\"main-physics physics.comp-ph\">Computational Physics</a>; <a href=\"/list/physics.data-an/recent\" id=\"physics.data-an\" aria-labelledby=\"main-physics physics.data-an\">Data Analysis, Statistics and Probability</a>; <a href=\"/list/physics.flu-dyn/recent\" id=\"physics.flu-dyn\" aria-labelledby=\"main-physics physics.flu-dyn\">Fluid Dynamics</a>; <a href=\"/list/physics.gen-ph/recent\" id=\"physics.gen-ph\" aria-labelledby=\"main-physics physics.gen-ph\">General Physics</a>; <a href=\"/list/physics.geo-ph/recent\" id=\"physics.geo-ph\" aria-labelledby=\"main-physics physics.geo-ph\">Geophysics</a>; <a href=\"/list/physics.hist-ph/recent\" id=\"physics.hist-ph\" aria-labelledby=\"main-physics physics.hist-ph\">History and Philosophy of Physics</a>; <a href=\"/list/physics.ins-det/recent\" id=\"physics.ins-det\" aria-labelledby=\"main-physics physics.ins-det\">Instrumentation and Detectors</a>; <a href=\"/list/physics.med-ph/recent\" id=\"physics.med-ph\" aria-labelledby=\"main-physics physics.med-ph\">Medical Physics</a>; <a href=\"/list/physics.optics/recent\" id=\"physics.optics\" aria-labelledby=\"main-physics physics.optics\">Optics</a>; <a href=\"/list/physics.soc-ph/recent\" id=\"physics.soc-ph\" aria-labelledby=\"main-physics physics.soc-ph\">Physics and Society</a>; <a href=\"/list/physics.ed-ph/recent\" id=\"physics.ed-ph\" aria-labelledby=\"main-physics physics.ed-ph\">Physics Education</a>; <a href=\"/list/physics.plasm-ph/recent\" id=\"physics.plasm-ph\" aria-labelledby=\"main-physics physics.plasm-ph\">Plasma Physics</a>; <a href=\"/list/physics.pop-ph/recent\" id=\"physics.pop-ph\" aria-labelledby=\"main-physics physics.pop-ph\">Popular Physics</a>; <a href=\"/list/physics.space-ph/recent\" id=\"physics.space-ph\" aria-labelledby=\"main-physics physics.space-ph\">Space Physics</a>    </li>\n" +
                "<li>\n" +
                "      <a href=\"/archive/quant-ph\" id=\"main-quant-ph\" aria-labelledby=\"main-quant-ph\">Quantum Physics</a>\n" +
                "      (<strong id=\"quant-ph\">quant-ph</strong>\n" +
                "      <a id=\"new-quant-ph\" aria-labelledby=\"new-quant-ph quant-ph\" href=\"/list/quant-ph/new\">new</a>,\n" +
                "      <a id=\"recent-quant-ph\" aria-labelledby=\"recent-quant-ph quant-ph\" href=\"/list/quant-ph/recent\">recent</a>,\n" +
                "      <a id=\"search-quant-ph\" aria-labelledby=\"search-quant-ph quant-ph\" href=\"https://arxiv.org/search/quant-ph\">search</a>)\n" +
                "\n" +
                "    </li>\n" +
                "  </ul>\n" +
                "  <h2>Mathematics</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"/archive/math\" id=\"main-math\" aria-labelledby=\"main-math\">Mathematics</a>\n" +
                "      (<strong id=\"math\">math</strong>\n" +
                "      <a id=\"new-math\" aria-labelledby=\"new-math math\" href=\"/list/math/new\">new</a>,\n" +
                "      <a id=\"recent-math\" aria-labelledby=\"recent-math math\" href=\"/list/math/recent\">recent</a>,\n" +
                "      <a id=\"search-math\" aria-labelledby=\"search-math math\" href=\"https://arxiv.org/search/math\">search</a>)\n" +
                "      <br/>includes: (see <a href=\"https://info.arxiv.org/help/math/index.html\" id=\"details-math\" aria-labelledby=\"details-math main-math\">detailed description</a>):\n" +
                "\n" +
                "<a href=\"/list/math.AG/recent\" id=\"math.AG\" aria-labelledby=\"main-math math.AG\">Algebraic Geometry</a>; <a href=\"/list/math.AT/recent\" id=\"math.AT\" aria-labelledby=\"main-math math.AT\">Algebraic Topology</a>; <a href=\"/list/math.AP/recent\" id=\"math.AP\" aria-labelledby=\"main-math math.AP\">Analysis of PDEs</a>; <a href=\"/list/math.CT/recent\" id=\"math.CT\" aria-labelledby=\"main-math math.CT\">Category Theory</a>; <a href=\"/list/math.CA/recent\" id=\"math.CA\" aria-labelledby=\"main-math math.CA\">Classical Analysis and ODEs</a>; <a href=\"/list/math.CO/recent\" id=\"math.CO\" aria-labelledby=\"main-math math.CO\">Combinatorics</a>; <a href=\"/list/math.AC/recent\" id=\"math.AC\" aria-labelledby=\"main-math math.AC\">Commutative Algebra</a>; <a href=\"/list/math.CV/recent\" id=\"math.CV\" aria-labelledby=\"main-math math.CV\">Complex Variables</a>; <a href=\"/list/math.DG/recent\" id=\"math.DG\" aria-labelledby=\"main-math math.DG\">Differential Geometry</a>; <a href=\"/list/math.DS/recent\" id=\"math.DS\" aria-labelledby=\"main-math math.DS\">Dynamical Systems</a>; <a href=\"/list/math.FA/recent\" id=\"math.FA\" aria-labelledby=\"main-math math.FA\">Functional Analysis</a>; <a href=\"/list/math.GM/recent\" id=\"math.GM\" aria-labelledby=\"main-math math.GM\">General Mathematics</a>; <a href=\"/list/math.GN/recent\" id=\"math.GN\" aria-labelledby=\"main-math math.GN\">General Topology</a>; <a href=\"/list/math.GT/recent\" id=\"math.GT\" aria-labelledby=\"main-math math.GT\">Geometric Topology</a>; <a href=\"/list/math.GR/recent\" id=\"math.GR\" aria-labelledby=\"main-math math.GR\">Group Theory</a>; <a href=\"/list/math.HO/recent\" id=\"math.HO\" aria-labelledby=\"main-math math.HO\">History and Overview</a>; <a href=\"/list/math.IT/recent\" id=\"math.IT\" aria-labelledby=\"main-math math.IT\">Information Theory</a>; <a href=\"/list/math.KT/recent\" id=\"math.KT\" aria-labelledby=\"main-math math.KT\">K-Theory and Homology</a>; <a href=\"/list/math.LO/recent\" id=\"math.LO\" aria-labelledby=\"main-math math.LO\">Logic</a>; <a href=\"/list/math.MP/recent\" id=\"math.MP\" aria-labelledby=\"main-math math.MP\">Mathematical Physics</a>; <a href=\"/list/math.MG/recent\" id=\"math.MG\" aria-labelledby=\"main-math math.MG\">Metric Geometry</a>; <a href=\"/list/math.NT/recent\" id=\"math.NT\" aria-labelledby=\"main-math math.NT\">Number Theory</a>; <a href=\"/list/math.NA/recent\" id=\"math.NA\" aria-labelledby=\"main-math math.NA\">Numerical Analysis</a>; <a href=\"/list/math.OA/recent\" id=\"math.OA\" aria-labelledby=\"main-math math.OA\">Operator Algebras</a>; <a href=\"/list/math.OC/recent\" id=\"math.OC\" aria-labelledby=\"main-math math.OC\">Optimization and Control</a>; <a href=\"/list/math.PR/recent\" id=\"math.PR\" aria-labelledby=\"main-math math.PR\">Probability</a>; <a href=\"/list/math.QA/recent\" id=\"math.QA\" aria-labelledby=\"main-math math.QA\">Quantum Algebra</a>; <a href=\"/list/math.RT/recent\" id=\"math.RT\" aria-labelledby=\"main-math math.RT\">Representation Theory</a>; <a href=\"/list/math.RA/recent\" id=\"math.RA\" aria-labelledby=\"main-math math.RA\">Rings and Algebras</a>; <a href=\"/list/math.SP/recent\" id=\"math.SP\" aria-labelledby=\"main-math math.SP\">Spectral Theory</a>; <a href=\"/list/math.ST/recent\" id=\"math.ST\" aria-labelledby=\"main-math math.ST\">Statistics Theory</a>; <a href=\"/list/math.SG/recent\" id=\"math.SG\" aria-labelledby=\"main-math math.SG\">Symplectic Geometry</a>    </li>\n" +
                "  </ul>\n" +
                "  <h2>Computer Science</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"https://info.arxiv.org/help/cs/index.html\" id=\"main-cs\" aria-labelledby=\"main-cs\">Computing Research Repository</a>\n" +
                "      (<strong id=\"cs\">CoRR</strong>\n" +
                "      <a id=\"new-cs\" aria-labelledby=\"new-cs cs\" href=\"/list/cs/new\">new</a>,\n" +
                "      <a id=\"recent-cs\" aria-labelledby=\"recent-cs cs\" href=\"/list/cs/recent\">recent</a>,\n" +
                "      <a id=\"search-cs\" aria-labelledby=\"search-cs cs\" href=\"https://arxiv.org/search/cs\">search</a>)\n" +
                "      <br/>includes: (see <a href=\"https://info.arxiv.org/help/cs/index.html\" id=\"details-cs\" aria-labelledby=\"details-cs main-cs\">detailed description</a>):\n" +
                "\n" +
                "<a href=\"/list/cs.AI/recent\" id=\"cs.AI\" aria-labelledby=\"main-cs cs.AI\">Artificial Intelligence</a>; <a href=\"/list/cs.CL/recent\" id=\"cs.CL\" aria-labelledby=\"main-cs cs.CL\">Computation and Language</a>; <a href=\"/list/cs.CC/recent\" id=\"cs.CC\" aria-labelledby=\"main-cs cs.CC\">Computational Complexity</a>; <a href=\"/list/cs.CE/recent\" id=\"cs.CE\" aria-labelledby=\"main-cs cs.CE\">Computational Engineering, Finance, and Science</a>; <a href=\"/list/cs.CG/recent\" id=\"cs.CG\" aria-labelledby=\"main-cs cs.CG\">Computational Geometry</a>; <a href=\"/list/cs.GT/recent\" id=\"cs.GT\" aria-labelledby=\"main-cs cs.GT\">Computer Science and Game Theory</a>; <a href=\"/list/cs.CV/recent\" id=\"cs.CV\" aria-labelledby=\"main-cs cs.CV\">Computer Vision and Pattern Recognition</a>; <a href=\"/list/cs.CY/recent\" id=\"cs.CY\" aria-labelledby=\"main-cs cs.CY\">Computers and Society</a>; <a href=\"/list/cs.CR/recent\" id=\"cs.CR\" aria-labelledby=\"main-cs cs.CR\">Cryptography and Security</a>; <a href=\"/list/cs.DS/recent\" id=\"cs.DS\" aria-labelledby=\"main-cs cs.DS\">Data Structures and Algorithms</a>; <a href=\"/list/cs.DB/recent\" id=\"cs.DB\" aria-labelledby=\"main-cs cs.DB\">Databases</a>; <a href=\"/list/cs.DL/recent\" id=\"cs.DL\" aria-labelledby=\"main-cs cs.DL\">Digital Libraries</a>; <a href=\"/list/cs.DM/recent\" id=\"cs.DM\" aria-labelledby=\"main-cs cs.DM\">Discrete Mathematics</a>; <a href=\"/list/cs.DC/recent\" id=\"cs.DC\" aria-labelledby=\"main-cs cs.DC\">Distributed, Parallel, and Cluster Computing</a>; <a href=\"/list/cs.ET/recent\" id=\"cs.ET\" aria-labelledby=\"main-cs cs.ET\">Emerging Technologies</a>; <a href=\"/list/cs.FL/recent\" id=\"cs.FL\" aria-labelledby=\"main-cs cs.FL\">Formal Languages and Automata Theory</a>; <a href=\"/list/cs.GL/recent\" id=\"cs.GL\" aria-labelledby=\"main-cs cs.GL\">General Literature</a>; <a href=\"/list/cs.GR/recent\" id=\"cs.GR\" aria-labelledby=\"main-cs cs.GR\">Graphics</a>; <a href=\"/list/cs.AR/recent\" id=\"cs.AR\" aria-labelledby=\"main-cs cs.AR\">Hardware Architecture</a>; <a href=\"/list/cs.HC/recent\" id=\"cs.HC\" aria-labelledby=\"main-cs cs.HC\">Human-Computer Interaction</a>; <a href=\"/list/cs.IR/recent\" id=\"cs.IR\" aria-labelledby=\"main-cs cs.IR\">Information Retrieval</a>; <a href=\"/list/cs.IT/recent\" id=\"cs.IT\" aria-labelledby=\"main-cs cs.IT\">Information Theory</a>; <a href=\"/list/cs.LO/recent\" id=\"cs.LO\" aria-labelledby=\"main-cs cs.LO\">Logic in Computer Science</a>; <a href=\"/list/cs.LG/recent\" id=\"cs.LG\" aria-labelledby=\"main-cs cs.LG\">Machine Learning</a>; <a href=\"/list/cs.MS/recent\" id=\"cs.MS\" aria-labelledby=\"main-cs cs.MS\">Mathematical Software</a>; <a href=\"/list/cs.MA/recent\" id=\"cs.MA\" aria-labelledby=\"main-cs cs.MA\">Multiagent Systems</a>; <a href=\"/list/cs.MM/recent\" id=\"cs.MM\" aria-labelledby=\"main-cs cs.MM\">Multimedia</a>; <a href=\"/list/cs.NI/recent\" id=\"cs.NI\" aria-labelledby=\"main-cs cs.NI\">Networking and Internet Architecture</a>; <a href=\"/list/cs.NE/recent\" id=\"cs.NE\" aria-labelledby=\"main-cs cs.NE\">Neural and Evolutionary Computing</a>; <a href=\"/list/cs.NA/recent\" id=\"cs.NA\" aria-labelledby=\"main-cs cs.NA\">Numerical Analysis</a>; <a href=\"/list/cs.OS/recent\" id=\"cs.OS\" aria-labelledby=\"main-cs cs.OS\">Operating Systems</a>; <a href=\"/list/cs.OH/recent\" id=\"cs.OH\" aria-labelledby=\"main-cs cs.OH\">Other Computer Science</a>; <a href=\"/list/cs.PF/recent\" id=\"cs.PF\" aria-labelledby=\"main-cs cs.PF\">Performance</a>; <a href=\"/list/cs.PL/recent\" id=\"cs.PL\" aria-labelledby=\"main-cs cs.PL\">Programming Languages</a>; <a href=\"/list/cs.RO/recent\" id=\"cs.RO\" aria-labelledby=\"main-cs cs.RO\">Robotics</a>; <a href=\"/list/cs.SI/recent\" id=\"cs.SI\" aria-labelledby=\"main-cs cs.SI\">Social and Information Networks</a>; <a href=\"/list/cs.SE/recent\" id=\"cs.SE\" aria-labelledby=\"main-cs cs.SE\">Software Engineering</a>; <a href=\"/list/cs.SD/recent\" id=\"cs.SD\" aria-labelledby=\"main-cs cs.SD\">Sound</a>; <a href=\"/list/cs.SC/recent\" id=\"cs.SC\" aria-labelledby=\"main-cs cs.SC\">Symbolic Computation</a>; <a href=\"/list/cs.SY/recent\" id=\"cs.SY\" aria-labelledby=\"main-cs cs.SY\">Systems and Control</a>    </li>\n" +
                "  </ul>\n" +
                "  <h2>Quantitative Biology</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"/archive/q-bio\" id=\"main-q-bio\" aria-labelledby=\"main-q-bio\">Quantitative Biology</a>\n" +
                "      (<strong id=\"q-bio\">q-bio</strong>\n" +
                "      <a id=\"new-q-bio\" aria-labelledby=\"new-q-bio q-bio\" href=\"/list/q-bio/new\">new</a>,\n" +
                "      <a id=\"recent-q-bio\" aria-labelledby=\"recent-q-bio q-bio\" href=\"/list/q-bio/recent\">recent</a>,\n" +
                "      <a id=\"search-q-bio\" aria-labelledby=\"search-q-bio q-bio\" href=\"https://arxiv.org/search/q-bio\">search</a>)\n" +
                "      <br/>includes: (see <a href=\"https://info.arxiv.org/help/q-bio/index.html\" id=\"details-q-bio\" aria-labelledby=\"details-q-bio main-q-bio\">detailed description</a>):\n" +
                "\n" +
                "<a href=\"/list/q-bio.BM/recent\" id=\"q-bio.BM\" aria-labelledby=\"main-q-bio q-bio.BM\">Biomolecules</a>; <a href=\"/list/q-bio.CB/recent\" id=\"q-bio.CB\" aria-labelledby=\"main-q-bio q-bio.CB\">Cell Behavior</a>; <a href=\"/list/q-bio.GN/recent\" id=\"q-bio.GN\" aria-labelledby=\"main-q-bio q-bio.GN\">Genomics</a>; <a href=\"/list/q-bio.MN/recent\" id=\"q-bio.MN\" aria-labelledby=\"main-q-bio q-bio.MN\">Molecular Networks</a>; <a href=\"/list/q-bio.NC/recent\" id=\"q-bio.NC\" aria-labelledby=\"main-q-bio q-bio.NC\">Neurons and Cognition</a>; <a href=\"/list/q-bio.OT/recent\" id=\"q-bio.OT\" aria-labelledby=\"main-q-bio q-bio.OT\">Other Quantitative Biology</a>; <a href=\"/list/q-bio.PE/recent\" id=\"q-bio.PE\" aria-labelledby=\"main-q-bio q-bio.PE\">Populations and Evolution</a>; <a href=\"/list/q-bio.QM/recent\" id=\"q-bio.QM\" aria-labelledby=\"main-q-bio q-bio.QM\">Quantitative Methods</a>; <a href=\"/list/q-bio.SC/recent\" id=\"q-bio.SC\" aria-labelledby=\"main-q-bio q-bio.SC\">Subcellular Processes</a>; <a href=\"/list/q-bio.TO/recent\" id=\"q-bio.TO\" aria-labelledby=\"main-q-bio q-bio.TO\">Tissues and Organs</a>    </li>\n" +
                "  </ul>\n" +
                "  <h2>Quantitative Finance</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"/archive/q-fin\" id=\"main-q-fin\" aria-labelledby=\"main-q-fin\">Quantitative Finance</a>\n" +
                "      (<strong id=\"q-fin\">q-fin</strong>\n" +
                "      <a id=\"new-q-fin\" aria-labelledby=\"new-q-fin q-fin\" href=\"/list/q-fin/new\">new</a>,\n" +
                "      <a id=\"recent-q-fin\" aria-labelledby=\"recent-q-fin q-fin\" href=\"/list/q-fin/recent\">recent</a>,\n" +
                "      <a id=\"search-q-fin\" aria-labelledby=\"search-q-fin q-fin\" href=\"https://arxiv.org/search/q-fin\">search</a>)\n" +
                "      <br/>includes: (see <a href=\"https://info.arxiv.org/help/q-fin/index.html\" id=\"details-q-fin\" aria-labelledby=\"details-q-fin main-q-fin\">detailed description</a>):\n" +
                "\n" +
                "<a href=\"/list/q-fin.CP/recent\" id=\"q-fin.CP\" aria-labelledby=\"main-q-fin q-fin.CP\">Computational Finance</a>; <a href=\"/list/q-fin.EC/recent\" id=\"q-fin.EC\" aria-labelledby=\"main-q-fin q-fin.EC\">Economics</a>; <a href=\"/list/q-fin.GN/recent\" id=\"q-fin.GN\" aria-labelledby=\"main-q-fin q-fin.GN\">General Finance</a>; <a href=\"/list/q-fin.MF/recent\" id=\"q-fin.MF\" aria-labelledby=\"main-q-fin q-fin.MF\">Mathematical Finance</a>; <a href=\"/list/q-fin.PM/recent\" id=\"q-fin.PM\" aria-labelledby=\"main-q-fin q-fin.PM\">Portfolio Management</a>; <a href=\"/list/q-fin.PR/recent\" id=\"q-fin.PR\" aria-labelledby=\"main-q-fin q-fin.PR\">Pricing of Securities</a>; <a href=\"/list/q-fin.RM/recent\" id=\"q-fin.RM\" aria-labelledby=\"main-q-fin q-fin.RM\">Risk Management</a>; <a href=\"/list/q-fin.ST/recent\" id=\"q-fin.ST\" aria-labelledby=\"main-q-fin q-fin.ST\">Statistical Finance</a>; <a href=\"/list/q-fin.TR/recent\" id=\"q-fin.TR\" aria-labelledby=\"main-q-fin q-fin.TR\">Trading and Market Microstructure</a>    </li>\n" +
                "  </ul>\n" +
                "  <h2>Statistics</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"/archive/stat\" id=\"main-stat\" aria-labelledby=\"main-stat\">Statistics</a>\n" +
                "      (<strong id=\"stat\">stat</strong>\n" +
                "      <a id=\"new-stat\" aria-labelledby=\"new-stat stat\" href=\"/list/stat/new\">new</a>,\n" +
                "      <a id=\"recent-stat\" aria-labelledby=\"recent-stat stat\" href=\"/list/stat/recent\">recent</a>,\n" +
                "      <a id=\"search-stat\" aria-labelledby=\"search-stat stat\" href=\"https://arxiv.org/search/stat\">search</a>)\n" +
                "      <br/>includes: (see <a href=\"https://info.arxiv.org/help/stat/index.html\" id=\"details-stat\" aria-labelledby=\"details-stat main-stat\">detailed description</a>):\n" +
                "\n" +
                "<a href=\"/list/stat.AP/recent\" id=\"stat.AP\" aria-labelledby=\"main-stat stat.AP\">Applications</a>; <a href=\"/list/stat.CO/recent\" id=\"stat.CO\" aria-labelledby=\"main-stat stat.CO\">Computation</a>; <a href=\"/list/stat.ML/recent\" id=\"stat.ML\" aria-labelledby=\"main-stat stat.ML\">Machine Learning</a>; <a href=\"/list/stat.ME/recent\" id=\"stat.ME\" aria-labelledby=\"main-stat stat.ME\">Methodology</a>; <a href=\"/list/stat.OT/recent\" id=\"stat.OT\" aria-labelledby=\"main-stat stat.OT\">Other Statistics</a>; <a href=\"/list/stat.TH/recent\" id=\"stat.TH\" aria-labelledby=\"main-stat stat.TH\">Statistics Theory</a>    </li>\n" +
                "  </ul>\n" +
                "  <h2>Electrical Engineering and Systems Science</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"/archive/eess\" id=\"main-eess\" aria-labelledby=\"main-eess\">Electrical Engineering and Systems Science</a>\n" +
                "      (<strong id=\"eess\">eess</strong>\n" +
                "      <a id=\"new-eess\" aria-labelledby=\"new-eess eess\" href=\"/list/eess/new\">new</a>,\n" +
                "      <a id=\"recent-eess\" aria-labelledby=\"recent-eess eess\" href=\"/list/eess/recent\">recent</a>,\n" +
                "      <a id=\"search-eess\" aria-labelledby=\"search-eess eess\" href=\"https://arxiv.org/search/eess\">search</a>)\n" +
                "      <br/>includes: (see <a href=\"https://info.arxiv.org/help/eess/index.html\" id=\"details-eess\" aria-labelledby=\"details-eess main-eess\">detailed description</a>):\n" +
                "\n" +
                "<a href=\"/list/eess.AS/recent\" id=\"eess.AS\" aria-labelledby=\"main-eess eess.AS\">Audio and Speech Processing</a>; <a href=\"/list/eess.IV/recent\" id=\"eess.IV\" aria-labelledby=\"main-eess eess.IV\">Image and Video Processing</a>; <a href=\"/list/eess.SP/recent\" id=\"eess.SP\" aria-labelledby=\"main-eess eess.SP\">Signal Processing</a>; <a href=\"/list/eess.SY/recent\" id=\"eess.SY\" aria-labelledby=\"main-eess eess.SY\">Systems and Control</a>    </li>\n" +
                "  </ul>\n" +
                "  <h2>Economics</h2>\n" +
                "  <ul><li>\n" +
                "      <a href=\"/archive/econ\" id=\"main-econ\" aria-labelledby=\"main-econ\">Economics</a>\n" +
                "      (<strong id=\"econ\">econ</strong>\n" +
                "      <a id=\"new-econ\" aria-labelledby=\"new-econ econ\" href=\"/list/econ/new\">new</a>,\n" +
                "      <a id=\"recent-econ\" aria-labelledby=\"recent-econ econ\" href=\"/list/econ/recent\">recent</a>,\n" +
                "      <a id=\"search-econ\" aria-labelledby=\"search-econ econ\" href=\"https://arxiv.org/search/econ\">search</a>)\n" +
                "      <br/>includes: (see <a href=\"https://info.arxiv.org/help/econ/index.html\" id=\"details-econ\" aria-labelledby=\"details-econ main-econ\">detailed description</a>):\n" +
                "\n" +
                "<a href=\"/list/econ.EM/recent\" id=\"econ.EM\" aria-labelledby=\"main-econ econ.EM\">Econometrics</a>; <a href=\"/list/econ.GN/recent\" id=\"econ.GN\" aria-labelledby=\"main-econ econ.GN\">General Economics</a>; <a href=\"/list/econ.TH/recent\" id=\"econ.TH\" aria-labelledby=\"main-econ econ.TH\">Theoretical Economics</a>    </li>\n" +
                "  </ul>\n" +
                "\n" +
                "\n" +
                "<hr />\n" +
                "<h2>About arXiv</h2>\n" +
                "<ul>\n" +
                "  <li><a href=\"https://info.arxiv.org/about\">General information</a></li>\n" +
                "  <li><a href=\"https://info.arxiv.org/help/submit/index.html\">How to Submit to arXiv</a></li>\n" +
                "  <li><a href=\"https://info.arxiv.org/about/donate.html\">Membership &amp; Giving</a></li>\n" +
                "  <li><a href=\"https://info.arxiv.org/about/people/index.html\">Who We Are</a></li>\n" +
                "</ul>\n" +
                "\n" +
                "      </div>\n" +
                "    </main>\n" +
                "\n" +
                "    <footer style=\"clear: both;\">\n" +
                "      <div class=\"columns is-desktop\" role=\"navigation\" aria-label=\"Secondary\" style=\"margin: -0.75em -0.75em 0.75em -0.75em\">\n" +
                "        <!-- Macro-Column 1 -->\n" +
                "        <div class=\"column\" style=\"padding: 0;\">\n" +
                "          <div class=\"columns\">\n" +
                "            <div class=\"column\">\n" +
                "              <ul style=\"list-style: none; line-height: 2;\">\n" +
                "                <li><a href=\"https://info.arxiv.org/about\">About</a></li>\n" +
                "                <li><a href=\"https://info.arxiv.org/help\">Help</a></li>\n" +
                "              </ul>\n" +
                "            </div>\n" +
                "            <div class=\"column\">\n" +
                "              <ul style=\"list-style: none; line-height: 2;\">\n" +
                "                <li>\n" +
                "                  <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\" class=\"icon filter-black\" role=\"presentation\"><title>contact arXiv</title><desc>Click here to contact arXiv</desc><path d=\"M502.3 190.8c3.9-3.1 9.7-.2 9.7 4.7V400c0 26.5-21.5 48-48 48H48c-26.5 0-48-21.5-48-48V195.6c0-5 5.7-7.8 9.7-4.7 22.4 17.4 52.1 39.5 154.1 113.6 21.1 15.4 56.7 47.8 92.2 47.6 35.7.3 72-32.8 92.3-47.6 102-74.1 131.6-96.3 154-113.7zM256 320c23.2.4 56.6-29.2 73.4-41.4 132.7-96.3 142.8-104.7 173.4-128.7 5.8-4.5 9.2-11.5 9.2-18.9v-19c0-26.5-21.5-48-48-48H48C21.5 64 0 85.5 0 112v19c0 7.4 3.4 14.3 9.2 18.9 30.6 23.9 40.7 32.4 173.4 128.7 16.8 12.2 50.2 41.8 73.4 41.4z\"/></svg>\n" +
                "                  <a href=\"https://info.arxiv.org/help/contact.html\"> Contact</a>\n" +
                "                </li>\n" +
                "                <li>\n" +
                "                  <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\" class=\"icon filter-black\" role=\"presentation\"><title>subscribe to arXiv mailings</title><desc>Click here to subscribe</desc><path d=\"M476 3.2L12.5 270.6c-18.1 10.4-15.8 35.6 2.2 43.2L121 358.4l287.3-253.2c5.5-4.9 13.3 2.6 8.6 8.3L176 407v80.5c0 23.6 28.5 32.9 42.5 15.8L282 426l124.6 52.2c14.2 6 30.4-2.9 33-18.2l72-432C515 7.8 493.3-6.8 476 3.2z\"/></svg>\n" +
                "                  <a href=\"https://info.arxiv.org/help/subscribe\"> Subscribe</a>\n" +
                "                </li>\n" +
                "              </ul>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <!-- End Macro-Column 1 -->\n" +
                "        <!-- Macro-Column 2 -->\n" +
                "        <div class=\"column\" style=\"padding: 0;\">\n" +
                "          <div class=\"columns\">\n" +
                "            <div class=\"column\">\n" +
                "              <ul style=\"list-style: none; line-height: 2;\">\n" +
                "                <li><a href=\"https://info.arxiv.org/help/license/index.html\">Copyright</a></li>\n" +
                "                <li><a href=\"https://info.arxiv.org/help/policies/privacy_policy.html\">Privacy Policy</a></li>\n" +
                "              </ul>\n" +
                "            </div>\n" +
                "            <div class=\"column sorry-app-links\">\n" +
                "              <ul style=\"list-style: none; line-height: 2;\">\n" +
                "                <li><a href=\"https://info.arxiv.org/help/web_accessibility.html\">Web Accessibility Assistance</a></li>\n" +
                "                <li>\n" +
                "                  <p class=\"help\">\n" +
                "                    <a class=\"a11y-main-link\" href=\"https://status.arxiv.org\" target=\"_blank\">arXiv Operational Status <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 256 512\" class=\"icon filter-dark_grey\" role=\"presentation\"><path d=\"M224.3 273l-136 136c-9.4 9.4-24.6 9.4-33.9 0l-22.6-22.6c-9.4-9.4-9.4-24.6 0-33.9l96.4-96.4-96.4-96.4c-9.4-9.4-9.4-24.6 0-33.9L54.3 103c9.4-9.4 24.6-9.4 33.9 0l136 136c9.5 9.4 9.5 24.6.1 34z\"/></svg></a><br>\n" +
                "                  </p>\n" +
                "                </li>\n" +
                "              </ul>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div> <!-- end MetaColumn 2 -->\n" +
                "        <!-- End Macro-Column 2 -->\n" +
                "      </div>\n" +
                "    </footer>\n" +
                "  </div>\n" +
                "\n" +
                "  <script src=\"/static/base/1.0.1/js/member_acknowledgement.js\"></script>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>");

        String html = content.toString();


        // Scrapping données
        String introText = extractBetween(html, "<p class=\"tagline\">", "</p>");
        Map<String, List<String>> data = extractSections(html);

        // Description arXiv
        Label intro = new Label(introText);
        intro.setWrapText(true);
        intro.setFont(Font.font("Arial", 15));
        intro.setPadding(new Insets(10));

        int col = 0, row = 0;

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

        Scene scene = new Scene(scroll, 1000, 900);
        stage.setScene(scene);

        stage.setTitle("arXiv Styled Page");
        stage.setScene(scene);
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
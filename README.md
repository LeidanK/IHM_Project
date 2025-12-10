# IHM Project

## Introduction
Voici le repo GitHub qui sera utilisé pour la construction du projet IHM.

Et voici également les liens des trois pages qui devront être recréées :
- https://arxiv.org/
- https://arxiv.org/list/cs.CL/recent
- https://arxiv.org/year/astro-ph/2021

Pour coder sur ce repo, il suffit de cloner le repo sur votre machine de manière classique.

Etant donné que c'est un projet JavaFX, il faudra bien veiller à respecter la mise en place suivante pour le bon lancement de l'application.

## Configuration

Par la suite, la procédure est décrite pour les utilisateurs de **IntelliJ**, mais l'équivalent existe sur tout autre IDE. Si doute, demandez à ChatGPT de traduire le paragraphe suivant pour votre IDE ;)

### Java Version
La version de Java utilisée est **Java 21**. C'est cette version qu'il faudra utilisée tout du long du projet pour éviter tout conflit.

Pour configurer la version de Java utilisée, vous devez avoir un onglet, quelque chose comme Project Structure, où vous pouvez gérer les différentes informations et configurations relatives à votre projet.
De là, il faut bien spécifier **SDK > Java 21** et **Language level > SDK Default**.

Une fois cela fait, déjà bravo, mais ce n'est pas fini. Il reste deux petites choses à faire.

### Dépendances JavaFX

Lors de notre cours, nous avons dû télécharger depuis internet le **SDK JavaFX**. Il est nécessaire d'intégrer dans les dépendances du projet.

    Projet Structure > Modules > Dependancies
et d'ajouter tous les fichiers .jar situés dans le dossier ``/lib`` du SDK JavaFX installé.
_Il est conseillé d'utiliser le JavaFX SDK **version 21** également, par soucis de conflits entre versions._

Pour mener à bien la partie scrapping de la page initiale et extraction des données, il est nécessaire de télécharger le fichier ``jsoup-1.21.2.jar`` et de le placer dans le dossier ``/lib`` du projet.
De cette manière, aucune dépendance ne manquera au projet.

### VM Options

On approche de la fin !

Si vous n'avez pas de configuration, créez en une, ``Add new Configuration``, et sélectionner ``Application`` dans les choix proposés (pour lancer notre application).

Spécifiez la classe principale du projet : ``com.example.ihm_project.HelloApplication``

Ajouter les options VM (``Modify options > Add VM Options``).

``--module-path "chemin/absolu/vers/SDK_installé/lib" --add-modules javafx.controls,javafx.fxml``

Cette ligne est à copier / coller dans le options VM de la configuration que vous utilisez pour lancer l'application, **en remplaçant bien évidemment le chemin absolu par votre réel chemin vers le sous-dossier ``/lib`` du SDK téléchargé.**

## Conclusion

Ce devrait être tout bon maintenant ! Vous pouvez lancer l'application depuis la configuration créée.

Maintenant, on code :)

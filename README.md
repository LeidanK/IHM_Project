# IHM Project

Uniquement si vous prenez la branhe maven.

## Introduction

Ce projet est une application JavaFX inspirée de plusieurs pages arXiv :
- https://arxiv.org/
- https://arxiv.org/list/cs.CL/recent
- https://arxiv.org/year/astro-ph/2021

## Prérequis

- **Java 21** (SDK installé sur votre machine)
- **Maven** (outil de build)

## Installation

Clonez le dépôt sur votre machine :
```sh
git clone <url_du_repo>
cd ihm_project
```

## Lancement de l'application

Pour lancer l'application JavaFX via Maven :

```sh
mvn javafx:run
```
> Si vous utilisez un IDE, vous pouvez aussi lancer la classe principale `com.example.ihm_project.HelloApplication`.

## Lancement des tests

Pour compiler et exécuter les tests (JUnit + TestFX) :

```sh
mvn test
```

Pour compiler les tests et lancer le runner de rapport (si configuré dans le pom.xml) :

```sh
mvn test-compile exec:java
```
> Cela exécutera la classe `com.example.ihm_project.TestReportRunner` (modifiez si besoin).

## Nettoyage du projet

Pour nettoyer les fichiers compilés et les dossiers générés par Maven :

```sh
mvn clean
```
> Cette commande supprime le dossier `target` et permet de repartir sur une base propre avant une nouvelle compilation ou exécution.

## Configuration JavaFX

Si vous rencontrez des problèmes de dépendances JavaFX, vérifiez que le SDK JavaFX version 21 est bien installé et que le chemin vers `/lib` est correctement configuré dans votre IDE ou vos variables d'environnement.

## Structure du projet

- `src/main/java` : Code source de l'application
- `src/test/java` : Tests automatisés (JUnit, TestFX)
- `pom.xml` : Configuration Maven et dépendances

## Remarques

- Toutes les dépendances nécessaires sont gérées par Maven.
- Pour toute question sur la configuration ou l'exécution, consultez la documentation Maven ou demandez à ChatGPT.

Bon développement !

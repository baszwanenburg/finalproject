# Proposal
## Type Racer for Android
Bas Zwanenburg
11980370

## Problem Statement
De smartphone is een nog relatief nieuwe technologie. De laatste paar jaar is het apparaat ook steeds populairder geworden bij de ouderen. Deze mensen, die niet zijn opgegroeid met computers en er nooit mee hebben gewerkt, hebben vaak veel moeite met typen. Ze hebben echter niet de mogelijkheid om hier wat aan te doen. De bedoeling is dat gebruikers met deze app op een leuke manier kunnen oefenen en zo hun typvaardigheid vanzelf verbeteren. Uiteraard is dit niet alleen bedoeld voor ouderen, maar voor iedereen die verlangt om sneller te typen.

## Solution
De app is in feite een spelletje, waarbij de gebruiker een willekeurig stuk tekst zo snel mogelijk foutloos dient over te typen om een zo hoog mogelijke score te behalen. Met behulp van een autootje als visualisatie wordt de progressie van de gebruiker weergegeven. De stukjes tekst worden opgehaald met behulp van JSON API van een film database, waarbij de beschrijving van een willekeurige film als prompt wordt gebruikt.

## Visual Sketch
![Sketches](/docs/Sketches.png)
Schetsen ter concept. De Minimum Viable Project bestaat uit alle blauwe componenten. De rode componenten zijn optioneel en worden alleen geïmplementeerd als dat daadwerkelijk haalbaar is. 

### Main Features
- Gebruikers kunnen registeren en inloggen met Firebase, of kunnen gebruikmaken van de app met behulp van een gastaccount
- Tijdens het overtypen wordt in groen aangegeven welke woorden juist zijn overgetypt. Elke spelfout en alles wat erna komt is fout, en wordt in rood weergegeven
- Na de eindstreep te hebben behaald, wordt de score getoond, evenals informatie over de film waarvan de beschrijving afkomstig is
- In het geval dat de gebruiker is ingelogd, wordt de score voor het desbetreffende stuk tekst opgeslagen en op een apart scherm in de bijbehorende leaderboard weergegeven.
- De gebruiker kan selecteren of het toetsenbord 'ligt' of 'staat'. Beide opties hebben aparte leaderboards (optioneel)
- Gebruikers kunnen naast individueel spelen, ook onderling tegen elkaar racen (optioneel)
- De voortgang van de speler wordt gevisualiseerd met een auto over een weg (optioneel)

## Prerequisites
- Data sources: [TheMovieDataBase](https://www.themoviedb.org/documentation/api) returnt een JSON met de informatie van alle films in de database, inclusief de beschrijving van de film.
- External components: Firebase wordt gebruikt voor het registeren en inloggen van gebruikers en het opslaan van hun high score. Verder worden nog wat libraries als dependencies toegevoegd om de implementatie makkelijker te maken, voornamelijk ButterKnife en EventBus.
- Review similar project: Het idee van de app is gebaseerd op de [TypeRacer website](http://play.typeracer.com/). Hier zijn ook meerdere apps in de Google Play Store te vinden. Deze apps maken echter exclusief gebruik van korte prompts, en naar mijn mening kunnen gebruikers met kortere stukjes tekst minder goed oefenen. Ook geven ze niet de optie om het scherm te roteren, waardoor het toetsenbord alleen staand of liggend kan worden gebruikt. Beide problemen wil ik met deze app verhelpen.
- Hardest part: Het berekenen van de score. Deze score is afhankelijk van het gemiddeld aantal juist overgetypte woorden per minuut.

Verder zijn er nog drie onderdelen van de app die lastig worden om te implementeren. Zo weet ik nog niet hoe ik er voor zorg dat typfouten niet worden meegerekend en de gebruiker pas verder komt als hij of zij deze fouten heeft rechtgezet. Ook moet ik een manier vinden om de progressie in de app te visualiseren, waarbij de progressie gebaseerd is op het percentage woorden van de filmbeschrijving dat juist is overgetypt. Tot slot wil ik de mogelijkheid implementeren dat gebruikers lokaal peer to peer tegen elkaar kunnen racen, maar dit is misschien niet realistisch en beschouw ik dus als optioneel.

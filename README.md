# Programmeerproject: Type Racer
Bas Zwanenburg
11980370

## About
Met deze app wordt het typspelletje Type Racer geïmplementeerd, dat gebaseerd is op het gelijknamige browserspelletje. Hierbij wordt een willekeurig stukje tekst naar de gebruiker verzonden en dit dient zo snel mogelijk correct te worden overgetypt. De tekst beschrijft de plot van een willekeurig recente of nog aan te komen film, en wordt met behulp van The Movie DataBase (TMDB) API via het internet verkregen. De API returnt de gegevens van 20 films, en de JSON die de app ophaalt wordt regelmatig aangepast. 

Tijdens het overtypen van de tekst loopt een timer. Hiermee wordt het aantal woorden per minuut dat de speler correct heeft overgetypt bijgehouden. De progressie wordt gevisualiseerd met behulp van een laadbalkje. De prestatie van de speler wordt beoordeeld op de snelheid, waarbij een hogere snelheid tot een lagere score leidt (een lage score is in dit spel juist gewenst). Indien de gebruiker zich heeft geregistreerd en is ingelogd, wordt de beste prestatie van de gebruiker vastgelegd in een leaderboard met behulp van Firebase, en dit gebeurt voor elk aparte film dat de gebruiker kan ontvangen (er zijn dus minstens 20 leaderboards die door Firebase worden bijgehouden). Daarnaast wordt ook nog twee laadbalkjes weergegeven die het persoonlijk record (indien aanwezig) en de absolute high score van het desbetreffend stuk tekst visualiseert. Deze laadbalkjes zijn bedoeld om een competitief gevoel te geven. 

Het nut van de app is dat de gebruiker door de competitie op een leuke en uitdagende manier leert om te gaan met het on-screen toetsenbord van de smartphone. Veel mensen hebben moeite met het typen en zijn vaak veel langzamer dan wanneer ze een traditionele toetsenbord gebruiken. Het idee is dus dat dit spel de gebruiker hier enigszins mee helpt. Na afloop wordt ook nog aangetoond van welke film de beschrijving afkomstig is; als bonus krijgt de gebruiker dus ook mogelijke filmsuggesties.


## Screenshots
![Screenshots](/docs/appScreenshots.png)

## Product Video
https://vimeo.com/253942479

## Better Code Hub
[![BCH compliance](https://bettercodehub.com/edge/badge/baszwanenburg/finalproject?branch=master)](https://bettercodehub.com/)

## Licenses
Copyright 2018 baszwanenburg

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

# Problem Statement
De smartphone is een nog relatief nieuwe technologie. De laatste paar jaar is het apparaat ook steeds populairder geworden bij de ouderen. Deze mensen, die niet zijn opgegroeid met computers en er nooit mee hebben gewerkt, hebben vaak veel moeite met typen. Ze hebben echter niet de mogelijkheid om hier wat aan te doen. De bedoeling is dat gebruikers met deze app op een leuke manier kunnen oefenen en zo hun typvaardigheid vanzelf verbeteren. Uiteraard is dit niet alleen bedoeld voor ouderen, maar voor iedereen die verlangt om sneller te typen.

## Solution
De app is in feite een spelletje, waarbij de gebruiker een willekeurig stuk tekst zo snel mogelijk foutloos dient over te typen om een zo hoog mogelijke score te behalen. Met behulp van een autootje als visualisatie wordt de progressie van de gebruiker weergegeven. De stukjes tekst worden opgehaald met behulp van JSON API van een film database, waarbij de beschrijving van een willekeurige film als prompt wordt gebruikt.

## Visual Sketch
![Sketch](/docs/Sketch.jpg)
Schetsen ter concept. De rode componenten zijn optioneel en worden alleen geïmplementeerd als dat daadwerkelijk haalbaar is. De Minimum Viable Project bestaat uit de volgende componenten:
- LoginRegister om gebruikers gebruik te laten maken van Firebase
- Vergelijken van EditText en TextView
- Inladen van data uit de film database
- Toepassen van een stopwatch timer
- High score van de gebruiker opslaan (en vervangen)

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

# Design Document
Bas Zwanenburg, 11980370

## Advanced sketch
![Sketches](/docs/Sketches.png)
In deze gedetailleerde tekening wordt aangegeven welke schermen en onderdelen met elkaar verbonden zijn. De rode delen geven aan wat optioneel is en afhankelijk van de tijd wel of niet wordt geïmplementeerd. De lay-out van de verschillende activities kan nog enigszins veranderen, maar ik ga er wel van uit dat het eindresultaat er ongeveer zo zal uit zien, omdat deze indeling duidelijk en gebruikersvriendelijk is.

## Utility modules
De volgende modules dienen in ieder geval worden gebruikt:
![Moduli](/docs/Moduli.png)

## API's, frameworks and data sources
- TheMovieDataBase: Uit deze database worden 100 films afgelezen en de titel, cover, regisseur, jaartal en vooral de beschrijving opgehaald.
- Firebase: Regelt het registeren en inloggen van gebruikers. Dit is nodig om de high scores op te slaan en een leaderboard te maken.
- Glide: Deze library vervangt een image url met een afbeelding (makkelijker dan Picasso).
- EventBus: Deze library regelt interacties tussen bijvoorbeeld classes of activities op een efficiënte manier.
- ButterKnife: Deze library maakt het schrijven van veelvoorkomende stukjes code sneller en korter. 

## Datalist table
In de Firebase tree krijgt elke gebruiker een aparte node. Deze node heeft de volgende children:
- UserID (String)
- Username (String)

Voor elke film waarvan de desbetreffende gebruiker de beschrijving heeft overgetypt wordt ook nog een child toegevoegd. Deze child heeft als naam de titel van de film (String) en heeft de volgende nodes:
- Score (int)
- Rank (int)
- Datum (datetime/String)

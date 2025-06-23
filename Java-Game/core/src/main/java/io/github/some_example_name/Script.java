package io.github.some_example_name;

public class Script {
    static String[/*npc*/][/*statement*/][/*line*/] npcscript =
        { //Dialogauswahl wir mit der Variable scriptIndex im NPC festgelegt
            {//testNPC
                {// erstes Statement (Ansammlung von Zeilen die auf einmal gesagt werden)
                    "Lorem Ipsum, Sapere Aude, \ndas hier ist die erste Zeile",
                    "zweite Zeile",
                    "   hallo spieler du\n bist unterentwickelt",
                    "test 2 bestanden",
                    "sogar 3 mit bravouraaaaaaaaaa",
                    "ich bin der der Zerstörer",
                    "Deine Werbung könnte hier stehen."
                },
                {// zweites Statement
                    "zweites Statement",
                   "danke für deinen Kauf",
                    "ich gebe dir nächstes mal einen Treue Bonus"
                }
            },
            {//theoretischer 2. NPC
                {//erstes Statement
                    "ich bin der 2. npc (Händler)",
                    "Gustav Klauk ist mein Name",
                    "wie du glaubst mir nicht?",
                        "Sehr wohl. Möchtest du etwas kaufen?",
                },
                    {//zweites Statment
                            "(2. STatement) warum bin ich so naiv?",
                            "meine Waren sind viel zu billig",
                            "wie du glaubst mir nicht?",
                    }

            }

        };

    static String[] startmenuscript =
        {
            "hehehehoha",
            "99% Wahrscheinlichkeit, dass dies ein Spiel ist.",
            "Willkommen! Jetzt gibt es kein Entkommen.",
            "Es ist zu spät zum Umkehren.",
            "Ein Spiel beginnt... oder eine Legende?",
            "Robert ist hier",
            "Eisern Union!!!",
            "Ice and Fire",
            "Für Ruhm und Ehre",
            "Press Alt + F4",
            "This is a temporary Screen\n or is it?",
            "This Game is being developed since 2024",
            "Theo-retisch...",
            "Warum bist du hier?",
            "Jetzt gibt es kein Zurück mehr...",
            "Enter the Void",
            "Reality not included",
            "Now with 10% more bugs!!",///+++++
            "Für Bedeutungslosigkeit  und Wahnsinn!",
            "Möge der Irrsinn uns leiten!", //++++++
            "Are ya ready son?",
            "Grass is green",
            "Dieser Bildschirm ist nur geliehen",
            "Entwickelt mit IntelliJ und (Timofeys) Tränen",
            "Vorsicht, epic moments ahead!",
            "Du bist bereit... oder?",
            "Help! I am trapped in a screen-message factory :\\",
            "I was a player once... \nnow I'm just code.",///+++++++
            "Das hättest du nicht sehen sollen...",///+++++
            "They said I could be anything...\n so I became text.",
            "89% Gameplay, 2% Off-by-one-Errors, 10% Bugs",
            "Willkommen im Chaos.",
            "Vertrau mir, hier ist nichts.",
            "Hello there!",
            "HINTER DIR!!     ist dein Rücken :O",
            "Drücke einen Knopf.",
            "                       \n                                                                                                  \nWillst du das Spiel starten?\n oder  weiter auf deinen\n Bildschirm starren?",
            "Nur Mut!",
            "Was echt ist, entscheidest du.",
            "Bereit oder nicht...",
            "Darkness awaits",
            "Wer wagt, gewinnt!",
            "Jetzt wird’s ernst.",
            "I am the voice inside the machine.",//+++++++++++
            "This is not a bug, it is a feature.",///+++++++++++
            "Are you even real?",
            "Lorem Ipsum.",
            "This game is sponsored by Raid: Shadow Legends!\n            (not really)             "

        };
    static String[] loadingscreenscript=
    {
        "Ladebildschirm nicht abschalten!",
        "Is this reality or just a loading screen?",
        "Lädt... irgendwie.",
        "Kaffee holen empfohlen.",
        "Fast da... vielleicht.",
        "Mehr Ladebalken = mehr Spaß!",
        "Physik berechnet deine Fehler.",
        "Bereite dich vor... oder warte einfach.",
        "Speicher wird optimiert... Hoffentlich.",
        "Die Magie des Ladens läuft.",
        "Deine Werbung könnte hier stehen",
        "Nicht der langsamste Ladebildschirm!",
        "Die KI denkt nach…",
        "Loading... please don’t blink.",
        "Preparing your next mistake.",
        "MÆth magic in progress.",
        "Your patience is being tested.",
        "Simulating intense game mechanics.",
        "Loading... still loading...",
        "Life is hard, but loading is harder.",
        "One does not simply skip loading screens.",
        "Optimizing... definitely not making it worse.",
        "Ehrlich gesagt, habe ich das Laden absichtlich lange gemacht."
    };

    static String[] deathscreenscript =
        {
            "Dust yourself off.\n\n Greatness awaits.",
            "Auf ein neues!",
            "Nächstes Mal...",
            "Compiling your skill issue",
            "Du bist gestorben",
            "Naja...",
            "Fähigkeitsproblem",
            "Schwabbel weg davon!",
            "Das andere Rechts!",
            "Der Tod wartet immer.",
            "Das linkere Links vielleicht?",
            "Wieder hier?",
            "Sag zum Abschied\n\n   leise Servus...",
            "GG: Get Good",
            "You found me! Now what?", //+++++++++++++++++++++(you found death)
            "Skill Issue",
            "I blinked once and now I am here.",
            "Intentional\n\nGamedesign",
            "Jetzt ist es zu spät zum Umkehren.",
            "Hoffentlich hast du gespeichert...",
            "Siehst du das? Nein? Genau.",
            "Drücke jeden beliebigen Knopf... \naußer Selbstzerstörung.",
            "Alt + F4 ist deine einzige Hoffnung",
            "Die Steuerung kämpft gegen dich.",
            "Statistik sagt: 100% verloren.",
            "Respawn advised!",
            "Was that part of the plan?",
            "That was... unfortunate.",
            "Speed alone won’t save you!",
            "Wieder gestolpert?",
            "Das war eine mutige Entscheidung.",
            "Respawn or Ragequit?",
            "Just try again.",
            "Und wieder von vorne.",
            "Physik ist auch nicht dein Freund.",
            "War das mit Absicht?",
            "Das war knapp. Nicht!!",
            "Das Tutorial hätte geholfen.",
            "Respawn in 3... 2... 1...",
            "Warum küsst du den Boden?",
            "Das war ein Speedrun, oder?",
            "Glückwunsch, du hast verloren!",
            "Ein tragisches Ende...",
            "Falls du dich fragst: Ja, das zählt als Tod.",
            "One step too far.",
            "Es liegt nicht an uns, es liegt an dir."

        };
}


package io.github.some_example_name;

public class LevelList { //Hier werden alle Level gespeichert
    static String[][] levels =
            {
                {
                    "########################################",
                    "#d                                    ##",
                    "  t  t  n #                           ##",
                    "   @                                  ##",
                    "    n            =                    ##",
                    "#       g                             ##",
                    "# ttt                                 ##",
                    "#                                     ##",
                    "########################################"
                },
                    {//Level muss mindestens 9*9 sein wegen der Camera
                            "#####################",
                            "##@               d##",
                            "##      ttt        ##",
                            "##           ggggg ##",
                            "##            g   g ##",
                            "##           ggggg ##",
                            "##                 ##",
                            "##      m d        ##",
                            "##               n ##",
                            "##                 ##",
                            "#####################",
                    },

                    {
                            "########################################",
                            "#d        ##    #   #  g   d          ##",
                            "  t  t  n #  ## # #   ##              ##",
                            "   @      # ##    ###g##              ##",
                            "    n     # #   #     #               ##",
                            "#       g # # # #### ##               ##",
                            "# ttt       #      #                  ##",
                            "#           ######   #               ##",
                            "########################################"
                    },

                    {
                            "####################",
                            "#ggggg        ggggg#",
                            "#                  ##",
                            "#                  ##",
                            "#                  ##",
                            "#                  ##",
                            "#                  ##",
                            "#                  ##",
                            "#      ######      ##",
                            "#                  ##",
                            "#        @         ##",
                            "#                  ##",
                            "#          d       ##",
                            "#                  ##",
                            "#                  #",
                            "#                  #",
                            "#                  #",
                            "#                  #",
                            "#                  #",
                            "#gggg          gggg#",
                            "####################"
                    },
                    {
                            "####################################################################",
                            "##############################                                     #",
                            "##############################                                     #",
                            "##############################                                     #",
                            "#                                                                  #",
                            "# @                  n             gggggggggggg                    #",
                            "#                                                                  #",
                            "#                                                                  #",
                            "#                                                                  #",
                            "#                                k             d                    #",
                            "#          n                                                       #",
                            "#                                                                  #",
                            "#                                                                  #",
                            "#                                                                  #",
                            "#                    n             gggggggggggg                    #",
                            "#                                                                  #",
                            "#                                                                  #",
                            "#                                                                  #",
                            "##############################                                     #",
                            "####################################################################"
                    }
            };


    static NpcData[][] NPCDATA = {
        {///Level 1
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510)
        },
        {///Level 2
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),

        },
        {///Level 3
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f,0,0,200,510),


        }
    };
}


package io.github.some_example_name;

public class LevelList { //Hier werden alle Level gespeichert
    static String[][] levels =
            {

                    {
                            "#####################",
                            "##@               d##",
                            "##      ttt        ##",
                            "##           ggggg ##",
                            "##           g   g ##",
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
                            "# ttt       ###       #               ##",
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
                            "##############################                                     #",
                            "##############################                                     #",
                            "##############################                                     #",
                            "####################################################################"
                    }
            };


    static NpcData[][] NPCDATA = {
        {///Level 1
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f)
        },
        {///Level 2
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),

        },
        {///Level 3
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),
            new NpcData("Al Assad.png", "own Watertile 2.png", 0, 0.3f),


        }
    };
}


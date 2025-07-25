package io.github.some_example_name;

public class LevelList { //Hier werden alle Level gespeichert
    static String[][] levels =
            {

                    {//Level muss mindestens 9*16 sein wegen der Camera
                            "##################",
                            "##################",
                            "##            d##",
                            "##              #",
                            "#              ##",
                            "#       m      ##",
                            "#              ##",
                            "#              ##",
                            "#       t      ##",
                            "#              ##",
                            "#              ##",
                            "#       g      ##",
                            "#              ##",
                            "#              ##",
                            "#       c      ##",
                            "#              ##",
                            "#              ##",
                            "#  m    =      ##",
                            "#       =      ##",
                            "#  m           ##",
                            "#         h    ##",
                            "#              ##",
                            "#              ##",
                            "#       n      ##",
                            "####        d  ##",
                            "#####   @   #####",
                    },
                    {
                            "###################",
                            "###################",
                            "##               ##",
                            "##               ##",
                            "##               ##",
                            "##               ##",
                            "##               ##",
                            "##      t tttt   ##",
                            "## ttttttt tt    ##",
                            "##    ttt        ##",
                            "##               ##",
                            "##    m       d  ##",
                            "##  #####        ##",
                            "##  h            ##",
                            "                   ",
                            "#########@#########",
                            "###################"

                    },
                {
                    "########################################",
                    "#      c              c               ##",
                    "                              c       ##",
                    "   @             ####                 ##",
                    "                 ####             c   ##",
                    "#                        c            ##",
                    "#                                     ##",
                    "#           c                      d  ##",
                    "########################################"
                },
                    {

                                            "#########################",
                                            "@ #       #       #     #" ,
                                            "# ### ### ####### # ### #" ,
                                            "#       #       #   #   #" ,
                                            "####### ####### ##### ###" ,
                                            "#   #   t   #   #   #   #" ,
                                            "# ### ##### # ### # # # #" ,
                                            "#     #     #     # # # #" ,
                                            "# ##### ### ####### ### #" ,
                                            "# #   t #     #   # #   #" ,
                                            "### ## ###### # # # # # #" ,
                                            "#   #     #   # # #   # #" ,
                                            "# ### ### ### ### ##### #" ,
                                            "# #   # #   #     #     #",
                                            "# # ### ### ##### ### ###" ,
                                            "# # #     #   #         #" ,
                                            "# # ### # ### # ####### #" ,
                                            "# #  t  # #   # #     # #" ,
                                            "# ##### ### ##### ### # #" ,
                                            "#   #   #   #t    # # ###" ,
                                            "# ### ### ### ##### # # #" ,
                                            "# #   #t  #   #     #  d#" ,
                                            "# # ### ### ####### #####" ,
                                            "#     #                  " ,
                                            "#########################",

                    },


                    {
                            "########################################",
                            "#         ##    #   #  g   d          ##",
                            "# t  t  n #  ## # #   ## t            ##",
                            "#  @      # ## t  ###g##              ##",
                            "#   n     # #   #     #        g      ##",
                            "#       g # # # #### ##               ##",
                            "# ttt       #      #    g      t      ##",
                            "#           ######   #                ##",
                            "########################################"
                    },


                    {
                            "#####################################################",
                            "##     t      ################                   g  #",
                            "##############################           g          #",
                            "##############################   g                  #",
                            "#       c                                           #",
                            "#                    n                 m    ggg     #",
                            "#                                                   #",
                            "#                  ##                               #",
                            "#                  ##                               #",
                            "#@                 ##                        d      #",
                            "#                  ##            c                  #",
                            "#                  ##                               #",
                            "#                  ##                               #",
                            "#                  ##                               #",
                            "#                                     gmm   ggg     #",
                            "#   c                                               #",
                            "#    m                                              #",
                            "#            c                                      #",
                            "##############################                      #",
                            "#####################################################"
                    },
                    {
                            "##########################                  #",
                            "###################m   ###                  #",
                            "################### c g###                  #",
                            "###################    ###                d #",
                            "#######c                 #               ####",
                            "#######    c             #               ############",
                            "#######     #######      #                          #",
                            "#######     #######   g  #                          #",
                            "#######     #######      #           c   c ##       #",
                            "#######  @  #######      #                 ##       #",
                            "###################      #          ##           c m#",
                            "###################      #          ##              #",
                            "######## cmc             #          ##              #",
                            "###################      #          ##              #",
                            "###################                            c   ##",
                            "###################                                ##",

                    },
                    {
                            "#####################",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#         d         #",
                            "#         k         #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#                   #",
                            "#       @           #",
                            "#                   #",
                            "#####################"
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


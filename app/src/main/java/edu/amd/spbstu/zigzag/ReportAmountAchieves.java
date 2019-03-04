package edu.amd.spbstu.zigzag;

public  class ReportAmountAchieves {
    public static int countOfScore;
    public static int countOfLives;
    static{
        countOfScore = 0;
        countOfLives = 0;
    }
    public static void addCountOfScore(){
        countOfScore++;
    }

    public static void addCountOfLives(){
        countOfLives++;
    }

    public static void takeCountOfLives(){
        countOfLives--;
    }

    public static void setStartValueOfBonuses(){
        countOfLives = 0;
        countOfScore = 0;
    }

}

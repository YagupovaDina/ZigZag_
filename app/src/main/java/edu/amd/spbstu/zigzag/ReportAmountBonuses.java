package edu.amd.spbstu.zigzag;

/**
 * в статическом классе хранится количество бонусов и жизней
 */
public  class ReportAmountBonuses {
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

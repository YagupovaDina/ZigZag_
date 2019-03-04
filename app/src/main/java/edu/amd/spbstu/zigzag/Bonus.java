package edu.amd.spbstu.zigzag;
import android.graphics.Canvas;


public class Bonus {
    private KINDOFBONUSES kindOfBonus;
    private int radiusBall;
    private boolean isAlive;
    private boolean isDraw;
    private Point2D.Double coordinate;


    public Bonus(int radius) {
        radiusBall = radius;
    }

    public KINDOFBONUSES getKindOfBonus() {
        return kindOfBonus;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public Point2D.Double getCoordinate() {
        return coordinate;
    }

    public void setCoordinateY(double coordinate) {
        this.coordinate.y = coordinate;
    }

    public void setCoordinate(Point2D.Double coordinate) {
        this.coordinate = coordinate;
    }

    public void drawBonus(int m_screenH, Canvas canvas, Characters characters) {
            if (isAlive) {

                if (kindOfBonus == KINDOFBONUSES.LIVE)
                    canvas.drawBitmap(characters.mouse.getPictureBeforeEating(), (float) coordinate.x, (float) coordinate.y, null);
                else
                    canvas.drawBitmap(characters.fish.getPictureBeforeEating(), (float) coordinate.x, (float) coordinate.y, null);

            } else {
                if (kindOfBonus == KINDOFBONUSES.LIVE)
                    canvas.drawBitmap(characters.mouse.getPictureAfterEating(), (float) coordinate.x, (float) coordinate.y, null);
                else
                    canvas.drawBitmap(characters.fish.getPictureAfterEating(), (float) coordinate.x, (float) coordinate.y, null);
            }

    }
    public void setKindOfBonus(int numberOfIteration){
        double tmp = Math.random() * ( 100 - 13 );
        isAlive = true;
        isDraw = false;
        if (Math.round(tmp) % 17  == 0)
            kindOfBonus = KINDOFBONUSES.LIVE;
        else if (Math.round(tmp) % 2 == 0 && Math.round(tmp) % 10  != 0) {
            kindOfBonus = KINDOFBONUSES.SCORE;
        }
        if ((numberOfIteration + 1) % 2 == 0)
            isDraw = false;
        else
            isDraw = true;
    }

    /**
     * Проверка на захват бонуса главным персонажем
     * @param xMainCharacter
     * @param yMainCharacter
     * @param radiusMainCharacter
     * @return true если персонаж захватил бонус
     */
    public  boolean isCatchBonus(double xMainCharacter, double yMainCharacter, float radiusMainCharacter){
        if (coordinate.y - radiusBall<= yMainCharacter + radiusMainCharacter && coordinate.y - 0.5f*radiusBall>= yMainCharacter - radiusMainCharacter) {
            if (coordinate.x - radiusBall > (xMainCharacter + radiusMainCharacter)
                    || coordinate.x + radiusBall < (xMainCharacter - radiusMainCharacter)){
            return false;}
                if (isAlive == true){
                    if (kindOfBonus == KINDOFBONUSES.SCORE)
                        ReportAmountAchieves.addCountOfScore();
                    if (kindOfBonus == KINDOFBONUSES.LIVE)
                        ReportAmountAchieves.addCountOfLives();
                }
                if (isAlive == true) {
                    isAlive = false;
                    return true;
                }
                isAlive = false;
                return false;
        }
        return false;
    }

}


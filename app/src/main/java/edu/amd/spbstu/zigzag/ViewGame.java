package edu.amd.spbstu.zigzag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Shader;
import android.os.Message;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class RefreshHandler extends Handler {
    ViewGame m_viewGame;

    public RefreshHandler(ViewGame v){m_viewGame = v;}

    public void handleMessage(Message msg){
        m_viewGame.update();
        m_viewGame.invalidate();
    }

    public void sleep(long delayMillis){
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0),delayMillis);
    }
}
public class ViewGame extends View{
    private Characters characters;
    private final int UPDATE_TIME_MS = 30;
    private RefreshHandler m_refresh;
    private boolean m_active = false;
    private Paint paintText,backgraundText,paintForRoad, backgroundForGame;
    private byte language;
    private List<Bonus> bonuses;
    private float widthRoad = 0, deltaForRoad, deltaForCat;
    boolean gameFinish;
    private int directForCat;
    private MainActivity	m_app;
    private final int AMOUNT_OF_STAGE = 1000;
    private int m_screenW, m_screenH;
    private List<Point2D.Double> road;


    public ViewGame(Context context) {
        super(context);
    }

    public ViewGame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setPaintForBackground(){
        backgroundForGame = new Paint();
        backgroundForGame.setShader(new LinearGradient(0, 0, m_screenW, m_screenH,
                Color.BLACK, Color.CYAN, Shader.TileMode.MIRROR));
        backgroundForGame.setStyle(Paint.Style.FILL);

    }

    private void setPaintForRoad(){
        paintForRoad = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        paintForRoad.setStyle(Paint.Style.STROKE);
        paintForRoad.setStrokeWidth(widthRoad);//было m_screenH
        paintForRoad.setAntiAlias(true);
        paintForRoad.setShader(new LinearGradient(1000, 0, 10, 100,
                Color.GREEN, Color.WHITE, Shader.TileMode.MIRROR));


    }

    private void setPaintTextForBonus(){
        backgraundText = new Paint();
        backgraundText.setColor(Color.BLACK);
        backgraundText.setStyle(Paint.Style.FILL);
        paintText = new Paint();

    }

    public ViewGame(MainActivity app, int m_screenW, int m_screenH) {
        super(app);
        this.m_screenW = m_screenW;
        this.m_screenH = m_screenH;
        m_refresh = new RefreshHandler(this);
        m_app = app;
        setOnTouchListener(m_app);
        widthRoad = m_screenW/(float)2.5;
        characters = new Characters(getContext(),m_screenH,m_screenW);
        setPaintTextForBonus();
        setPaintForBackground();
        setPaintForRoad();
        setLanguage();
    }




    public void update(){
        if (!m_active)
            return;
        if (m_active)
            m_refresh.sleep(UPDATE_TIME_MS);

    }

    private void setLanguage(){
        String strLang;
        strLang = Locale.getDefault().getDisplayLanguage();
        if (strLang.equalsIgnoreCase("english"))
        {
            Log.d("THREE", "LOCALE: English");
            language = AppIntro.LANGUAGE_ENG;
        }
        else if (strLang.equalsIgnoreCase("русский"))
        {
            Log.d("THREE", "LOCALE: Russian");
            language = AppIntro.LANGUAGE_RUS;
        }
        else
        {
            Log.d("THREE", "LOCALE unknown: " + strLang);
            language = AppIntro.LANGUAGE_UNKNOWN;
        }
    }



    private void setCoordinateOfRoadAndBonus(){
        int koefForDirectionOfRoad = 1;
        Point2D.Double coordinateForPointOfRoad,coordinateForPointOfBonus;
        double deltaForY = m_screenH/4;
        Random rnd = new Random(System.currentTimeMillis());
        Bonus bonus;
        for (int i = 0;i<AMOUNT_OF_STAGE;i++){
            coordinateForPointOfRoad = new Point2D.Double();
            coordinateForPointOfBonus = new Point2D.Double();
            coordinateForPointOfRoad.x = (m_screenW / 3) + koefForDirectionOfRoad*(m_screenW/5);
            coordinateForPointOfRoad.y = m_screenH - (i)*deltaForY;
            coordinateForPointOfBonus.x = coordinateForPointOfRoad.x;
            coordinateForPointOfBonus.y = coordinateForPointOfRoad.y ;
            if (i < 5)
                koefForDirectionOfRoad = 1;
            if (i >= 5 && i <= 10) {
                koefForDirectionOfRoad = 2;
            }
            if (i > 11){
                koefForDirectionOfRoad = rnd.nextInt((2 - 0) + 1) + 0;
            }
            bonus = new Bonus(characters.fish.getPictureBeforeEating().getWidth()/2);
            road.add(coordinateForPointOfRoad);
            bonus.setCoordinate(coordinateForPointOfBonus);
            bonus.setKindOfBonus(i);
            bonuses.add(bonus);
        }

        if (fl_stopTimer)
            fl_stopTimer = false;
        else
        if (x == null)
            execTimer();
    }

    public void start() {
        m_active = true;
        m_refresh.sleep(UPDATE_TIME_MS);
        active = 0;
        characters.cat.init();
        ReportAmountAchieves.setStartValueOfBonuses();
        deltaForRoad = 0;
        deltaForCat = 0;
        gameFinish = false;
        directForCat = 0;
        road = new ArrayList<>();
        bonuses = new ArrayList<>();
        setCoordinateOfRoadAndBonus();
    }

    public boolean	onTouch(int x, int y, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (characters.cat.isDraw()) {
                    if (directForCat == 1)
                        directForCat = -1;
                    else directForCat = 1;
                    if (directForCat == 0)
                        directForCat = -1;
                }
                break;
        }
        return true;
    }

    Timer x;
    boolean fl_stopTimer = false;
    private void execTimer(){
        x = new Timer("timerName");
        x.schedule(_timerTask, 0, 30);
        x.schedule(_timerTaskForStep, 1000, 60);
    }

    private final TimerTask _timerTaskForStep = new TimerTask() {
        @Override
        public void run() {
            synchronized ((Float) deltaForRoad) {
                if (directForCat != 0) {
                    if (2.f+ deltaForCat <= 9)
                    deltaForCat += 0.001;
                }
                if (deltaForRoad + 0.0004 < 5) {
                    deltaForRoad += 0.0004;
                }
            }
        }
    };
private int active = 0;
    private final TimerTask _timerTask = new TimerTask() {
        @Override
        public void run() {
            synchronized (bonuses){
                synchronized (road) {
                    float step = ((1.f+ deltaForRoad) * ((float) m_screenH / 1280.f));
                    if (!characters.cat.isDraw())
                        characters.cat.setY(characters.cat.getY() - step);
                    for (int i = 0; i < road.size() && !gameFinish; i++) {
                        //bonuses.get(i).getCoordinate().y+=step;
                        bonuses.get(i).setCoordinateY(bonuses.get(i).getCoordinate().y+step);
                        road.get(i).y += step;
                        if (i + 1 < road.size()) {
                            road.get(i + 1).y += step;
                            bonuses.get(i).setCoordinateY(bonuses.get(i).getCoordinate().y+step);
                        }
                        if (i + 2 < road.size()) {
                            road.get(i + 2).y += step;
                            bonuses.get(i).setCoordinateY(bonuses.get(i).getCoordinate().y+step);
                        }
                    }
                }
            }
        }
    };

    void drawSignForStart(){
        for (int i = 0; i < road.size()-2 && !gameFinish && !characters.cat.isDraw(); i++) {
            if (road.get(i+2).y >= m_screenH / 2 + characters.cat.getSize()*2) {
                Toast toast;
                if (active == 0) {
                    if (language == AppIntro.LANGUAGE_RUS) {
                        toast = Toast.makeText(getContext(), "Старт!", Toast.LENGTH_SHORT);
                    } else
                        toast = Toast.makeText(getContext(), "START!", Toast.LENGTH_SHORT);
                    toast.show();
                    active = 1;
                }
                characters.cat.setDraw(true);
                break;
            }
        }
    }

    void drawRoad(Path path, Canvas canvas){
        path.moveTo((float) road.get(2).x, (float) road.get(2).y);
        for (int i = 2; i < road.size() - 2; i += 2) {
            if (road.get(i).y >= -m_screenH / 4 && road.get(i + 2).y <= m_screenH + m_screenH / 4) {
                path.cubicTo((float) road.get(i).x, (float) road.get(i).y-(float)2.4 ,
                        (float) road.get(i + 1).x, (float) road.get(i + 1).y,
                        (float) road.get(i + 2).x, (float) road.get(i + 2).y) ;
            }
            else if (road.get(i + 2).y > m_screenH) {
                path.reset();
                path.moveTo((float) road.get(i + 2).x, (float) road.get(i + 2).y);
            }
        }
        canvas.drawPath(path, paintForRoad);
    }

    private void drawKindCat(int i){
        if (ReportAmountAchieves.countOfScore != 0 && ReportAmountAchieves.countOfScore % 10 == 0
                && bonuses.get(i).getKindOfBonus() == KINDOFBONUSES.SCORE){
            Toast toast3;
            if (language == AppIntro.LANGUAGE_RUS) {
                toast3 = Toast.makeText(getContext(),
                        "уже " + ReportAmountAchieves.countOfScore + " молодец!", Toast.LENGTH_SHORT);
            }else{
                toast3 = Toast.makeText(getContext(),
                        ReportAmountAchieves.countOfScore + " good for you!", Toast.LENGTH_SHORT);
            }
            toast3.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK, 0, m_screenH/2);
            LinearLayout toastContainer = (LinearLayout) toast3.getView();
            ImageView catImageView = new ImageView(getContext());
            catImageView.setImageResource(R.drawable.cat_with_fish);
            toastContainer.addView(catImageView, 0);
            toast3.show();
        }
    }

    private void drawBonuses(Canvas canvas){
        boolean activeL = false;
        for (int i = 6; i < bonuses.size() && characters.cat.isDraw(); i ++) {
            if (bonuses.get(i).isDraw()){
                if (bonuses.get(i).getCoordinate().y >= -m_screenH / 4 && bonuses.get(i).getCoordinate().y <= m_screenH + m_screenH / 4) {
                    activeL = true;
                    bonuses.get(i).drawBonus(m_screenH,canvas,characters);
                    if (bonuses.get(i).isCatchBonus(characters.cat.getX(),characters.cat.getY(),characters.cat.getSize())){
                        drawKindCat(i);
                    }
                }
            }
            if (bonuses.get(i).getCoordinate().y > m_screenH + m_screenH / 4 && activeL){
                break;
            }
        }
    }

    private void printDialog(String string1,String string2, String string3){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_app);
            alertDialog.setTitle(string1).setMessage(string2).setCancelable(true).
                    setNegativeButton(string3,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    start();
                                }
                            });
        final AlertDialog dialog = alertDialog.create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    gameFinish = true;
                    m_app.setContentView(m_app.m_viewIntro);
                    m_app.setView(m_app.VIEW_INTRO);
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        dialog.show();
    }

    private void drawToastWithAttention(){
        Toast toast;
        if (language == AppIntro.LANGUAGE_RUS) {
            toast = Toast.makeText(getContext(),
                    "Осторожно! Использована дополнительная жизнь, осталось: " + ReportAmountAchieves.countOfLives + " жизней!", Toast.LENGTH_LONG);
        }else{
            toast = Toast.makeText(getContext(),
                    "Be careful! You used extra live, amount of rest life: " + ReportAmountAchieves.countOfLives, Toast.LENGTH_LONG);
        }
        toast.show();
    }

    private boolean isWin(){
        if (road.get(road.size()-1).y >= characters.cat.getY()-characters.cat.getSize() && road.get(road.size()-1).y <= characters.cat.getY()){
            fl_stopTimer = true;
            gameFinish = true;
            if (language == AppIntro.LANGUAGE_RUS)
                printDialog("Поздравляем!","Вы выиграли!","начать заново");
            if (language == AppIntro.LANGUAGE_ENG)
                printDialog("Congratulations!","you are the champion!","start again");
            return true;
        }
        return false;
    }

    private boolean isLose(float[] pos){
        if (pos[1] <= characters.cat.getY()){
            if (pos[0] - widthRoad/2 > (characters.cat.getX()  + characters.cat.getSize())  ||
                    pos[0] + widthRoad/2 < ((characters.cat.getX() + characters.cat.getSize()))) {
                ReportAmountAchieves.takeCountOfLives();
                if (ReportAmountAchieves.countOfLives != -1){
                    characters.cat.setX(pos[0] - characters.cat.getSize() - widthRoad/10);
                    directForCat = 0;
                    drawToastWithAttention();
                    return true;
                }
                if (ReportAmountAchieves.countOfLives == -1){
                    if (!gameFinish){
                        fl_stopTimer = true;
                        gameFinish = true;
                        if (language == AppIntro.LANGUAGE_RUS)
                            printDialog("Важное сообщение!","Вы проиграли! Сумма ваших очков: ","начать заново");
                        if (language == AppIntro.LANGUAGE_ENG)
                            printDialog("attention!","you lose! Amount of score: ","start again");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void drawTextForBonuses(Canvas canvas){
        float sizeText = 100 * (m_screenW / 1920.f );
        canvas.drawRect(0,m_screenH - 3*sizeText,m_screenW,m_screenH,backgraundText);
        paintText.setTextSize(sizeText);
        paintText.setARGB(150, 200, 0, 500);

        if (language == AppIntro.LANGUAGE_RUS){
            canvas.drawText("Количество очков (рыб): " + ReportAmountAchieves.countOfScore,
                    (float) (0), (float) (m_screenH - sizeText), paintText);
            if (ReportAmountAchieves.countOfLives < 0)
                ReportAmountAchieves.countOfLives = 0;
            canvas.drawText("Количество жизней (мышей): " + ReportAmountAchieves.countOfLives,
                    (float) (0), (float) (m_screenH - 2*sizeText), paintText);
        }
        else{
            canvas.drawText("Amount of score (fish): " + ReportAmountAchieves.countOfScore,
                    (float) (0), (float) (m_screenH - sizeText), paintText);
            if (ReportAmountAchieves.countOfLives < 0)
                ReportAmountAchieves.countOfLives = 0;
            canvas.drawText("Amount of life (mouse): " + ReportAmountAchieves.countOfLives,
                    (float) (0), (float) (m_screenH - 2*sizeText), paintText);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(backgroundForGame);
        PathMeasure measure;
        synchronized (road) {
            drawSignForStart();
            Path path = new Path();
            drawRoad(path,canvas);
            if (characters.cat.isDraw()){
                characters.cat.setX(characters.cat.getX() + directForCat * ((2.f+ deltaForCat) * ((float)m_screenH / 1280.f)));
            }
            drawBonuses(canvas);
            measure = new PathMeasure(path, false);
        }
        float[] pos = new float[2], tan=new float[Math.round(2)];
        pos[1] = m_screenH;
        for (int i = 1; pos[1] >= characters.cat.getY() && !gameFinish  && characters.cat.isDraw(); i++){
            measure.getPosTan(i, pos, tan);
            if (isWin())
                break;
            if (isLose(pos))
                break;
        }
        if (!gameFinish)
            canvas.drawBitmap(characters.cat.getPicture(),(float)characters.cat.getX(),(float)characters.cat.getY(),null);
        drawTextForBonuses(canvas);
    }
}

package edu.amd.spbstu.zigzag;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

public class MainActivity extends Activity implements View.OnTouchListener{
    AppIntro m_app;
    public static final int	VIEW_INTRO		= 0;
    public static final int	VIEW_GAME       = 1;
    int						m_viewCur = -1;

    ViewIntro			    m_viewIntro;
    ViewGame				m_viewGame;


    // screen dim
    int						m_screenW;
    int						m_screenH;

    Display display;
    Point point;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        display = getWindowManager().getDefaultDisplay();
        point = new Point();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        String strLang = Locale.getDefault().getDisplayLanguage();
        byte language;

        display.getSize(point);
        m_screenW = point.x;
        m_screenH = point.y;
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
        m_app = new AppIntro(this, language);
        setView(VIEW_INTRO);
    }
    public void setView(int viewID)
    {
        if (m_viewCur == viewID)
        {
            Log.d("THREE", "setView: already set");
            return;
        }

        m_viewCur = viewID;
        if (m_viewCur == VIEW_INTRO)
        {
            m_viewIntro = new ViewIntro(this);
            setContentView(m_viewIntro);
        }
        if (m_viewCur == VIEW_GAME)
        {
            m_viewGame = new ViewGame(this,m_screenW,m_screenH);//здесь твоя игра
            //Log.d("THREE", "Switch to m_viewGame" );
            setContentView(m_viewGame);
            m_viewGame.start();
        }
    }
    public AppIntro getApp()
    {
        return m_app;
    }
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

        // delayedHide(100);
    }
    public void onCompletion(MediaPlayer mp)
    {
        Log.d("THREE", "onCompletion: Video play is completed");
        //switchToGame();
    }
    @Override
    public boolean onTouch(View v, MotionEvent evt) {

        int x = (int)evt.getX();
        int y = (int)evt.getY();
        int touchType = AppIntro.TOUCH_DOWN;
        //if (evt.getAction() == MotionEvent.ACTION_DOWN)
        //  Log.d("THREE", "Touch pressed (ACTION_DOWN) at (" + String.valueOf(x) + "," + String.valueOf(y) +  ")"  );

        if (evt.getAction() == MotionEvent.ACTION_MOVE)
            touchType = AppIntro.TOUCH_MOVE;
        if (evt.getAction() == MotionEvent.ACTION_UP)
            touchType = AppIntro.TOUCH_UP;
        if (m_viewCur == VIEW_INTRO)
            return m_viewIntro.onTouch( x, y, touchType);
        if (m_viewCur == VIEW_GAME)
            return m_viewGame.onTouch(x, y, evt);
       // {
        //}
        return true;
    }
    public boolean onKeyDown(int keyCode, KeyEvent evt)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (m_viewCur == VIEW_GAME) {/////////////////////////////
                //setContentView(m_viewIntro);///////////////////////
                //boolean ret = super.onKeyDown(keyCode, evt);
                //return ret;
            }
            //Log.d("THREE", "Back key pressed");
            //boolean wantKill = m_app.onKey(Application.KEY_BACK);
            //if (wantKill)
            //		finish();
            //return true;
        }
        boolean ret = super.onKeyDown(keyCode, evt);
        return ret;
    }

    protected void onResume()
    {
        super.onResume();
        if (m_viewCur == VIEW_INTRO)
            m_viewIntro.start();
        //if (m_viewCur == VIEW_GAME)
        //    m_viewGame.start();
        //Log.d("THREE", "App onResume");
    }
    protected void onPause()
    {
        // stop anims
        if (m_viewCur == VIEW_INTRO)
            m_viewIntro.stop();
        //if (m_viewCur == VIEW_GAME)
           // m_viewGame.onPause();

        // complete system
        super.onPause();
        //Log.d("THREE", "App onPause");
    }
    protected void onDestroy()
    {
     //   if (m_viewCur == VIEW_GAME)
      //      m_viewGame.onDestroy();
        super.onDestroy();
        //Log.d("THREE", "App onDestroy");
    }
    public void onConfigurationChanged(Configuration confNew)
    {
        super.onConfigurationChanged(confNew);
        m_viewIntro.onConfigurationChanged(confNew);
    }
}

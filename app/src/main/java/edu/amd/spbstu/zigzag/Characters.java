package edu.amd.spbstu.zigzag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class Characters extends View {
    public MainCharacter cat ;
    public BonusView fish;
    public BonusView mouse;
    private int m_screenH,m_screenW;

    public Characters(Context context, int m_screenH, int m_screenW) {
        super(context);
        this.m_screenH = m_screenH;
        this.m_screenW = m_screenW;
        cat = new MainCharacter();
        fish = new BonusView(BitmapFactory.decodeResource(getResources(),R.drawable.fish_on),
                BitmapFactory.decodeResource(getResources(),R.drawable.fish_off));
        mouse = new BonusView(BitmapFactory.decodeResource(getResources(),R.drawable.mouse_on),
                BitmapFactory.decodeResource(getResources(),R.drawable.mouse_off));
    }

    class MainCharacter {
        private boolean isDraw;
        private float size;
        private Point2D.Double coordinate;
        private Bitmap picture;

        protected void init(){
            size = picture.getWidth()/2;
            coordinate = new Point2D.Double();
            coordinate.x = m_screenW/2 - size;
            coordinate.y = m_screenH - m_screenH/4 - size*2;
            isDraw = false;
        }

        public MainCharacter(){
            picture = BitmapFactory.decodeResource(getResources(),R.drawable.cat);
            init();
        }

        protected boolean isDraw() {
            return isDraw;
        }

        protected void setDraw(boolean draw) {
            isDraw = draw;
        }

        protected float getSize() {
            return size;
        }

        protected void setSize(float size) {
            this.size = size;
        }

        protected double getX() {
            return coordinate.x;
        }

        protected void setX(double x) {
            this.coordinate.x = x;
        }

        protected double getY() {
            return coordinate.y;
        }

        protected void setY(double y) {
            this.coordinate.y = y;
        }

        protected Bitmap getPicture() {
            return picture;
        }

    }

    class BonusView {
        private Bitmap pictureBeforeEating;
        private Bitmap pictureAfterEating;
        public BonusView(Bitmap before, Bitmap after){
            pictureBeforeEating = before;
            pictureAfterEating = after;

        }
        protected Bitmap getPictureBeforeEating() {
            return pictureBeforeEating;
        }
        protected Bitmap getPictureAfterEating() {
            return pictureAfterEating;
        }
    }
}

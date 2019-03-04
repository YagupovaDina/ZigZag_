package edu.amd.spbstu.zigzag;

import android.os.Handler;
import android.os.Message;

//****************************************************************
//class RefreshHandler
//****************************************************************
class RedrawHandler extends Handler
{
    ViewIntro m_viewIntro;

    public RedrawHandler(ViewIntro v)
    {
        m_viewIntro = v;
    }

    public void handleMessage(Message msg)
    {
        m_viewIntro.update();
        m_viewIntro.invalidate();
    }

    public void sleep(long delayMillis)
    {
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }
}
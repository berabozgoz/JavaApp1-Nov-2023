package org.csystem.scheduler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public abstract class CountDownTimer {
    private final Timer m_timer;
    private final long m_millisInFuture;
    private final long m_countDownInterval;

    private TimerTask createTimeTask()
    {
        return new TimerTask() {
            long duration;
            public void run()
            {
                if (duration >= m_millisInFuture) {
                    m_timer.cancel();
                    onFinish();
                    return;
                }

                duration += m_countDownInterval;
                onTick(m_millisInFuture - duration);
            }
        };
    }

    protected CountDownTimer(long durationInFuture, long countDownInterval, TimeUnit timeUnit)
    {
        this(timeUnit.toMillis(durationInFuture), timeUnit.toMillis(countDownInterval));
    }

    protected CountDownTimer(long millisInFuture, long countDownInterval)
    {
        m_timer = new Timer();
        m_millisInFuture = millisInFuture;
        m_countDownInterval = countDownInterval;
    }

    public abstract void onTick(long remainingMilliseconds);

    public abstract void onFinish();

    public final void start()
    {
        m_timer.scheduleAtFixedRate(createTimeTask(), 0, m_countDownInterval);
    }

    public final void cancel()
    {
        m_timer.cancel();
    }
}


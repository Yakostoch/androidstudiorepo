package com.mirea.bkt.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private final Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String profession = msg.getData().getString("profession");
                int age = msg.getData().getInt("age");

                Log.d("MyLooper", "get message: age=" + age + ", profession=" + profession);

                try {
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(
                        "result",
                        String.format(
                                "Пользователю %d лет, он работает: %s. Задержка составила %d секунд.",
                                age,
                                profession,
                                age
                        )
                );
                message.setData(bundle);
                mainHandler.sendMessage(message);
            }
        };

        Looper.loop();
    }
}

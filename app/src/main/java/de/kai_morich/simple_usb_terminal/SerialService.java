package de.kai_morich.simple_usb_terminal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.SmsManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * create notification and queue serial data while activity is not in the foreground
 * use listener chain: SerialSocket -> SerialService -> UI fragment
 */
public class SerialService extends Service implements SerialListener {

    final private int fsrValueSize = 256, fallDownTimeDiffThreshold = 200, fallDownKeepTimeThreshold = 800, adPer = 5, Rmin = 300;
    public long footFloatTimeL, footFloatTimeR, bothFeetFloatTime, biggestBothFloatTime, oneStepTime = 120000,
            heelImpactTimeL, heelImpactTimeR;
    public short totalFsrL, totalFsrR;
    private boolean findTwoFeets = false, isLeftFoot = false, calL = false, calR = false, calDirectL = false, calDirectR = false;
    private int currentIndex_L, currentIndex_R;
    private short foreFootL, foreFootR, F0L, F1L, F2L, F3L, F0R, F1R, F2R, F3R, avgFsrSumL, avgFsrSumR;
    private long footFloatStartL, footFloatTailL, footFloatTailR, bothFeetFloatStart, tempFloatL, diffFloatFeet,
            oneStepStartL, oneStepStartR, heelImpactStartL, heelImpactStartR, footFloatStartR, footFloatTimeDiff,
            bothFloatDuration;
    private short[] fsr0_Value_R = new short[fsrValueSize], fsr0_Value_L = new short[fsrValueSize];
    private short[] fsr1_Value_R = new short[fsrValueSize], fsr1_Value_L = new short[fsrValueSize];
    private short[] fsr2_Value_R = new short[fsrValueSize], fsr2_Value_L = new short[fsrValueSize];
    private short[] fsr3_Value_R = new short[fsrValueSize], fsr3_Value_L = new short[fsrValueSize];
    private byte lameLCnt, lameRCnt;
    private byte[] macL = new byte[6], macR = new byte[6], macArray = new byte[6];
    private MutableLiveData<Integer> fsrValueCount_R, fsrValueCount_L, stepCount;
    private MutableLiveData<Boolean> touchGround, heelImpactL, heelImpactR, isFloatL, isFloatR, fallDownOccur,
            lowPowerL, lowPowerR, lameL, lameR, mainSuportL, mainSuportR;
    private MutableLiveData<Float> horizontalMarkPer, verticalMarkPer, weight;

    public MutableLiveData<Float> getWeight() {
        if (weight == null) {
            weight = new MutableLiveData<>();
            weight.setValue(0.0f);
        }
        return weight;
    }

    public MutableLiveData<Float> getHorizontalMarkPer() {
        if (horizontalMarkPer == null) {
            horizontalMarkPer = new MutableLiveData<>();
            horizontalMarkPer.setValue(0.5f);
        }
        return horizontalMarkPer;
    }

    public MutableLiveData<Float> getVerticalMarkPer() {
        if (verticalMarkPer == null) {
            verticalMarkPer = new MutableLiveData<>();
            verticalMarkPer.setValue(0.7f);
        }
        return verticalMarkPer;
    }

    public MutableLiveData<Boolean> getMainSuportL() {
        if (mainSuportL == null) {
            mainSuportL = new MutableLiveData<>();
            mainSuportL.setValue(false);
        }
        return mainSuportL;
    }

    public MutableLiveData<Boolean> getMainSuportR() {
        if (mainSuportR == null) {
            mainSuportR = new MutableLiveData<>();
            mainSuportR.setValue(false);
        }
        return mainSuportR;
    }

    public MutableLiveData<Boolean> getLameL() {
        if (lameL == null) {
            lameL = new MutableLiveData<>();
            lameL.setValue(false);
        }
        return lameL;
    }

    public MutableLiveData<Boolean> getLameR() {
        if (lameR == null) {
            lameR = new MutableLiveData<>();
            lameR.setValue(false);
        }
        return lameR;
    }

    public MutableLiveData<Boolean> getLowPowerL() {
        if (lowPowerL == null) {
            lowPowerL = new MutableLiveData<>();
            lowPowerL.setValue(false);
        }
        return lowPowerL;
    }

    public MutableLiveData<Boolean> getLowPowerR() {
        if (lowPowerR == null) {
            lowPowerR = new MutableLiveData<>();
            lowPowerR.setValue(false);
        }
        return lowPowerR;
    }

    public MutableLiveData<Boolean> getFallDownOccur() {
        if (fallDownOccur == null) {
            fallDownOccur = new MutableLiveData<>();
            fallDownOccur.setValue(false);
        }
        return fallDownOccur;
    }

    public MutableLiveData<Boolean> getIsFloatL() {
        if (isFloatL == null) {
            isFloatL = new MutableLiveData<>();
            isFloatL.setValue(false);
        }
        return isFloatL;
    }

    public MutableLiveData<Boolean> getIsFloatR() {
        if (isFloatR == null) {
            isFloatR = new MutableLiveData<>();
            isFloatR.setValue(false);
        }
        return isFloatR;
    }

    public MutableLiveData<Boolean> getHeelImpactL() {
        if (heelImpactL == null) {
            heelImpactL = new MutableLiveData<>();
            heelImpactL.setValue(false);
        }
        return heelImpactL;
    }

    public MutableLiveData<Boolean> getHeelImpactR() {
        if (heelImpactR == null) {
            heelImpactR = new MutableLiveData<>();
            heelImpactR.setValue(false);
        }
        return heelImpactR;
    }

    public MutableLiveData<Integer> getStepCount() {
        if (stepCount == null) {
            stepCount = new MutableLiveData<>();
            stepCount.setValue(0);
        }
        return stepCount;
    }

    public MutableLiveData<Boolean> getTouchGround() {
        if (touchGround == null) {
            touchGround = new MutableLiveData<>();
            touchGround.setValue(true);
        }
        return touchGround;
    }

    public MutableLiveData<Integer> getFsrValueCountL() {
        if (fsrValueCount_L == null) {
            fsrValueCount_L = new MutableLiveData<>();
            fsrValueCount_L.setValue(0);
        }
        return fsrValueCount_L;
    }

    public MutableLiveData<Integer> getFsrValueCountR() {
        if (fsrValueCount_R == null) {
            fsrValueCount_R = new MutableLiveData<>();
            fsrValueCount_R.setValue(0);
        }
        return fsrValueCount_R;
    }

    public void updateFsrValueAction(byte[] packet, int index) {
        if (!findTwoFeets) {
            System.arraycopy(packet, index + 11, macR, 0, 6);

            if (Arrays.equals(macL, macArray)) {
                System.arraycopy(packet, index + 11, macL, 0, 6);
            } else {
                if (!Arrays.equals(macL, macR)) {
                    findTwoFeets = true;
                    for (int j = 0; j < 6; j++) {//小mac为左，大为右
                        if (Short.compare(macL[j], macR[j]) > 0) {
                            System.arraycopy(macL, 0, macArray, 0, 6);
                            System.arraycopy(macR, 0, macL, 0, 6);
                            System.arraycopy(macArray, 0, macR, 0, 6);
                            break;
                        } else if (Short.compare(macL[j], macR[j]) < 0) {
                            break;
                        }
                    }
                }
            }
        } else {
            System.arraycopy(packet, index + 11, macArray, 0, 6);
            if (Arrays.equals(macR, macArray)) {
                isLeftFoot = false;
                currentIndex_R = fsrValueCount_R.getValue();

                fsr0_Value_R[currentIndex_R] = (short) ((packet[index + 3] << 8) | (packet[index + 4] & 0xff));
                fsr1_Value_R[currentIndex_R] = (short) ((packet[index + 5] << 8) | (packet[index + 6] & 0xff));
                fsr2_Value_R[currentIndex_R] = (short) ((packet[index + 7] << 8) | (packet[index + 8] & 0xff));
                fsr3_Value_R[currentIndex_R] = (short) ((packet[index + 9] << 8) | (packet[index + 10] & 0xff));

                if (currentIndex_R == fsrValueSize - 1) {
                    fsrValueCount_R.setValue(currentIndex_R);//trigger observe
                    for (int i = 0; i < currentIndex_R; i++) {
                        fsr0_Value_R[i] = fsr0_Value_R[i + 1];
                        fsr1_Value_R[i] = fsr1_Value_R[i + 1];
                        fsr2_Value_R[i] = fsr2_Value_R[i + 1];
                        fsr3_Value_R[i] = fsr3_Value_R[i + 1];
                    }
                } else {
                    fsrValueCount_R.setValue(currentIndex_R + 1);
                }

                calFootTimeR();
            }
            if (Arrays.equals(macL, macArray)) {
                isLeftFoot = true;
                currentIndex_L = fsrValueCount_L.getValue();

                fsr0_Value_L[currentIndex_L] = (short) ((packet[index + 3] << 8) | (packet[index + 4] & 0xff));
                fsr1_Value_L[currentIndex_L] = (short) ((packet[index + 5] << 8) | (packet[index + 6] & 0xff));
                fsr2_Value_L[currentIndex_L] = (short) ((packet[index + 7] << 8) | (packet[index + 8] & 0xff));
                fsr3_Value_L[currentIndex_L] = (short) ((packet[index + 9] << 8) | (packet[index + 10] & 0xff));

                if (currentIndex_L == fsrValueSize - 1) {
                    fsrValueCount_L.setValue(currentIndex_L);//trigger observe
                    for (int i = 0; i < currentIndex_L; i++) {
                        fsr0_Value_L[i] = fsr0_Value_L[i + 1];
                        fsr1_Value_L[i] = fsr1_Value_L[i + 1];
                        fsr2_Value_L[i] = fsr2_Value_L[i + 1];
                        fsr3_Value_L[i] = fsr3_Value_L[i + 1];
                    }
                } else {
                    fsrValueCount_L.setValue(currentIndex_L + 1);
                }

                calFootTimeL();
            }
            reportFallDown();
            batteryCheck(packet, index);
            gravityMark();
            gestureDetect();
        }
    }

    private void batteryCheck(byte[] pkt, int idx) {
        if (Byte.compare(pkt[idx + 30], (byte) 0) != 0) {
            if (isLeftFoot) {
                getLowPowerL().setValue(true);
            } else {
                getLowPowerR().setValue(true);
            }
        } else {
            if (isLeftFoot) {
                getLowPowerL().setValue(false);
            } else {
                getLowPowerR().setValue(false);
            }
        }
    }

    private short calResistance(short adc) {
        short cal = adc;
        if (Short.compare(cal, (short) 181) < 0) {
            cal = (short) 181;
        }
        return (short) (Rmin * (20480f / cal - 4) - 100);
    }

    private void calWeight(short f0l, short f1l, short f2l, short f3l, short f0r, short f1r, short f2r, short f3r) {
        float factor = (float) Math.log(153.18);
        float result = (float) (0.003 * (Math.exp((factor - Math.log(calResistance(f0l) / 1000f)) / 0.699) +
                                Math.exp((factor - Math.log(calResistance(f1l) / 1000f)) / 0.699) +
                                Math.exp((factor - Math.log(calResistance(f2l) / 1000f)) / 0.699) +
                                Math.exp((factor - Math.log(calResistance(f3l) / 1000f)) / 0.699) +
                                Math.exp((factor - Math.log(calResistance(f0r) / 1000f)) / 0.699) +
                                Math.exp((factor - Math.log(calResistance(f1r) / 1000f)) / 0.699) +
                                Math.exp((factor - Math.log(calResistance(f2r) / 1000f)) / 0.699) +
                                Math.exp((factor - Math.log(calResistance(f3r) / 1000f)) / 0.699)));
        BigDecimal b = new BigDecimal(result);
        getWeight().setValue(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
    }

    public short getFsr0Value() {
        int index;
        if (isLeftFoot) {
            index = currentIndex_L;
            return fsr0_Value_L[index];
        } else {
            index = currentIndex_R;
            return fsr0_Value_R[index];
        }
    }

    public short getFsr1Value() {
        int index;
        if (isLeftFoot) {
            index = currentIndex_L;
            return fsr1_Value_L[index];
        } else {
            index = currentIndex_R;
            return fsr1_Value_R[index];
        }
    }

    public short getFsr2Value() {
        int index;
        if (isLeftFoot) {
            index = currentIndex_L;
            return fsr2_Value_L[index];
        } else {
            index = currentIndex_R;
            return fsr2_Value_R[index];
        }
    }

    public short getFsr3Value() {
        int index;
        if (isLeftFoot) {
            index = currentIndex_L;
            return fsr3_Value_L[index];
        } else {
            index = currentIndex_R;
            return fsr3_Value_R[index];
        }
    }

    public void calFootTimeL() {
        if (Short.compare((short) (getFsr0Value() + getFsr1Value() + getFsr2Value() + getFsr3Value() >> 2), (short) 700) < 0
                //鞋多有裹脚设计，因此取平均值判断
                && currentIndex_L > 50) {//1s后数值生效
            if (!getIsFloatL().getValue()) {
                footFloatStartL = System.currentTimeMillis();
                if (footFloatTimeL != 0) {
                    oneStepTime = System.currentTimeMillis() - oneStepStartL;
                }
                footFloatTimeL = 0;
                oneStepStartL = System.currentTimeMillis();
                getIsFloatL().setValue(true);
                if (getTouchGround().getValue() && getIsFloatR().getValue()) {
                    bothFeetFloatTime = 0;
                    footFloatTimeDiff = System.currentTimeMillis() - footFloatStartR;
                    bothFeetFloatStart = System.currentTimeMillis();
                    getTouchGround().setValue(false);
                }
                heelImpactTimeL = 0;
                getHeelImpactL().setValue(false);
            }

            if (!getTouchGround().getValue()) {
                bothFloatDuration = System.currentTimeMillis() - bothFeetFloatStart;
                if (!getFallDownOccur().getValue() && footFloatTimeDiff < fallDownTimeDiffThreshold
                        && bothFloatDuration > fallDownKeepTimeThreshold) {
                    getFallDownOccur().setValue(true);
                }
            }
        } else {
            if (getIsFloatL().getValue()) {
                heelImpactStartL = System.currentTimeMillis();
                getStepCount().setValue(getStepCount().getValue() + 1);
                if (Short.compare(getFsr1Value(), (short) 300) >= 0) {
                    footFloatTailL = 1;
                } else if (Short.compare(getFsr2Value(), (short) 300) >= 0) {
                    footFloatTailL = 2;
                } else if (Short.compare(getFsr3Value(), (short) 300) >= 0) {
                    footFloatTailL = 3;
                }
                footFloatTimeL = System.currentTimeMillis() - footFloatStartL + adPer * footFloatTailL;

                if (!getTouchGround().getValue()) {
                    footFloatTimeDiff = 0;
                    bothFeetFloatTime = System.currentTimeMillis() - bothFeetFloatStart + (adPer * footFloatTailL >> 1);
                    getTouchGround().setValue(true);

                    if (Long.compare(footFloatTimeL, bothFeetFloatTime) < 0) {
                        footFloatTimeL = bothFeetFloatTime;
                    }
                }
                getIsFloatL().setValue(false);
            }
            if (Short.compare(getFsr0Value(), (short) 3000) > 0 && heelImpactTimeL == 0 && footFloatTimeL != 0) {
                heelImpactTimeL = System.currentTimeMillis() - heelImpactStartL;
                getHeelImpactL().setValue(true);
            }
        }
    }

    public void calFootTimeR() {
        if (Short.compare((short) (getFsr0Value() + getFsr1Value() + getFsr2Value() + getFsr3Value() >> 2), (short) 700) < 0
                && currentIndex_R > 50) {//1s后数值生效
            if (!getIsFloatR().getValue()) {
                footFloatStartR = System.currentTimeMillis();
                if (footFloatTimeR != 0) {
                    oneStepTime = System.currentTimeMillis() - oneStepStartR;
                }
                footFloatTimeR = 0;
                oneStepStartR = System.currentTimeMillis();
                getIsFloatR().setValue(true);
                if (getTouchGround().getValue() && getIsFloatL().getValue()) {
                    bothFeetFloatTime = 0;
                    footFloatTimeDiff = System.currentTimeMillis() - footFloatStartL;
                    bothFeetFloatStart = System.currentTimeMillis();
                    getTouchGround().setValue(false);
                }
                heelImpactTimeR = 0;
                getHeelImpactR().setValue(false);
            }

            if (!getTouchGround().getValue()) {
                bothFloatDuration = System.currentTimeMillis() - bothFeetFloatStart;
                if (!getFallDownOccur().getValue() && footFloatTimeDiff < fallDownTimeDiffThreshold
                        && bothFloatDuration > fallDownKeepTimeThreshold) {
                    getFallDownOccur().setValue(true);
                }
            }
        } else {
            if (getIsFloatR().getValue()) {
                heelImpactStartR = System.currentTimeMillis();
                getStepCount().setValue(getStepCount().getValue() + 1);
                if (Short.compare(getFsr1Value(), (short) 300) >= 0) {
                    footFloatTailR = 1;
                } else if (Short.compare(getFsr2Value(), (short) 300) >= 0) {
                    footFloatTailR = 2;
                } else if (Short.compare(getFsr3Value(), (short) 300) >= 0) {
                    footFloatTailR = 3;
                }
                footFloatTimeR = System.currentTimeMillis() - footFloatStartR + adPer * footFloatTailR;

                if (!getTouchGround().getValue()) {
                    footFloatTimeDiff = 0;
                    bothFeetFloatTime = System.currentTimeMillis() - bothFeetFloatStart + (adPer * footFloatTailR >> 1);
                    getTouchGround().setValue(true);

                    if (Long.compare(footFloatTimeR, bothFeetFloatTime) < 0) {
                        footFloatTimeR = bothFeetFloatTime;
                    }
                }
                getIsFloatR().setValue(false);
            }
            if (Short.compare(getFsr3Value(), (short) 3000) > 0 && heelImpactTimeR == 0 && footFloatTimeR != 0) {
                heelImpactTimeR = System.currentTimeMillis() - heelImpactStartR;
                getHeelImpactR().setValue(true);
            }
        }
    }

    private void gestureDetect() {
        if (!getIsFloatL().getValue() && getIsFloatR().getValue() && footFloatTimeL != (long) 0 && tempFloatL == (long) 0) {
            tempFloatL = footFloatTimeL;
        } else if (getIsFloatL().getValue() && !getIsFloatR().getValue() && footFloatTimeR != (long) 0 && tempFloatL != (long) 0) {
            diffFloatFeet = tempFloatL - footFloatTimeR;
            if (Long.compare(diffFloatFeet, (long) 100) > 0) {
                lameLCnt++;
                if (lameLCnt == (byte) 5) {
                    lameLCnt -= (byte) 1;
                    getLameL().setValue(true);
                }
                lameRCnt = (byte) 0;
                getLameR().setValue(false);
            } else if (Long.compare(diffFloatFeet, (long) -100) < 0) {
                lameRCnt++;
                if (lameRCnt == (byte) 5) {
                    lameRCnt -= (byte) 1;
                    getLameR().setValue(true);
                }
                lameLCnt = (byte) 0;
                getLameL().setValue(false);
            } else {
                lameLCnt = (byte) 0;
                lameRCnt = (byte) 0;
                getLameL().setValue(false);
                getLameR().setValue(false);
            }
            tempFloatL = (long) 0;
        }
    }

    public long getBiggestBothFloatTime() {
        if (biggestBothFloatTime == 0) {
            biggestBothFloatTime = bothFeetFloatTime;
        } else if (Long.compare(biggestBothFloatTime, bothFeetFloatTime) < 0) {
            biggestBothFloatTime = bothFeetFloatTime;
        }
        return biggestBothFloatTime;
    }

    public long resetBiggestBothFloatTime() {
        biggestBothFloatTime = 0;
        return biggestBothFloatTime;
    }

    private void reportFallDown() {
        if (getFallDownOccur().getValue()) {
            String content = "发生跌倒，请及时去电问询。";//短信内容
            String phone = "13151680531";//电话号码
            SmsManager sm = SmsManager.getDefault();
            List<String> sms = sm.divideMessage(content);
            for (String smslist : sms) {
                sm.sendTextMessage(phone, null, smslist, null, null);
            }
            getFallDownOccur().setValue(false);
            footFloatTimeDiff = fallDownTimeDiffThreshold;
        }
    }

    private void gravityMark() {
        if (isLeftFoot) {
            F0L = getFsr0Value(); F1L = getFsr1Value(); F2L = getFsr2Value(); F3L = getFsr3Value();
            totalFsrL = (short) (F0L + F1L + F2L + F3L);
            foreFootL = (short) (F2L + F3L);
            if (currentIndex_L != fsrValueSize - 1) {
                avgFsrSumL += (short) (totalFsrL >> 8);
            } else {
                avgFsrSumL = (short) (avgFsrSumL * currentIndex_L + totalFsrL >> 8);
            }
            calL = true;
        } else {
            F0R = getFsr0Value(); F1R = getFsr1Value(); F2R = getFsr2Value(); F3R = getFsr3Value();
            totalFsrR = (short) (F0R + F1R + F2R + F3R);
            foreFootR = (short) (F0R + F1R);
            if (currentIndex_R != fsrValueSize - 1) {
                avgFsrSumR += (short) (totalFsrR >> 8);
            } else {
                avgFsrSumR = (short) (avgFsrSumR * currentIndex_R + totalFsrR >> 8);
            }
            calR = true;
        }
        if (calL && calR) {
            getHorizontalMarkPer().setValue((float) totalFsrR / (totalFsrL + totalFsrR + 1));//In case /0
            getVerticalMarkPer().setValue((float) 2 * (F0L + F3R) / (2 * (F0L + F3R) + foreFootL + foreFootR + 1));//In case /0
            if (Float.compare(getHorizontalMarkPer().getValue(), (float) 0.49) > 0 &&
                    Float.compare(getHorizontalMarkPer().getValue(), (float) 0.51) < 0) {
                calWeight(F0L, F1L, F2L, F3L, F0R, F1R, F2R, F3R);
            }
            if (currentIndex_L == fsrValueSize - 1 && currentIndex_R == fsrValueSize - 1) {
                if (Short.compare((short) (avgFsrSumL - avgFsrSumR), (short) 0) > 0) {
                    getMainSuportL().setValue(true);
                    getMainSuportR().setValue(false);
                } else if (Short.compare((short) (avgFsrSumL - avgFsrSumR), (short) 0) < 0) {
                    getMainSuportL().setValue(false);
                    getMainSuportR().setValue(true);
                }
            }
            calL = false;
            calR = false;
        }
    }

    class SerialBinder extends Binder {
        SerialService getService() {
            return SerialService.this;
        }
    }

    private enum QueueType {Connect, ConnectError, Read, IoError}

    private static class QueueItem {
        QueueType type;
        byte[] data;
        Exception e;

        QueueItem(QueueType type, byte[] data, Exception e) {
            this.type = type;
            this.data = data;
            this.e = e;
        }
    }

    private final Handler mainLooper;
    private final IBinder binder;
    private final Queue<QueueItem> queue1, queue2;

    private SerialSocket socket;
    private SerialListener listener;
    private boolean connected;

    /**
     * Lifecylce
     */
    public SerialService() {
        mainLooper = new Handler(Looper.getMainLooper());
        binder = new SerialBinder();
        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
    }

    @Override
    public void onDestroy() {
        cancelNotification();
        disconnect();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * Api
     */
    public void connect(SerialSocket socket) throws IOException {
        socket.connect(this);
        this.socket = socket;
        connected = true;
    }

    public void disconnect() {
        connected = false; // ignore data,errors while disconnecting
        cancelNotification();
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public void write(byte[] data) throws IOException {
        if (!connected)
            throw new IOException("not connected");
        socket.write(data);
    }

    public void attach(SerialListener listener) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread())
            throw new IllegalArgumentException("not in main thread");
        cancelNotification();
        // use synchronized() to prevent new items in queue2
        // new items will not be added to queue1 because mainLooper.post and attach() run in main thread
        synchronized (this) {
            this.listener = listener;
        }
        for (QueueItem item : queue1) {
            switch (item.type) {
                case Connect:
                    listener.onSerialConnect();
                    break;
                case ConnectError:
                    listener.onSerialConnectError(item.e);
                    break;
                case Read:
                    listener.onSerialRead(item.data);
                    break;
                case IoError:
                    listener.onSerialIoError(item.e);
                    break;
            }
        }
        for (QueueItem item : queue2) {
            switch (item.type) {
                case Connect:
                    listener.onSerialConnect();
                    break;
                case ConnectError:
                    listener.onSerialConnectError(item.e);
                    break;
                case Read:
                    listener.onSerialRead(item.data);
                    break;
                case IoError:
                    listener.onSerialIoError(item.e);
                    break;
            }
        }
        queue1.clear();
        queue2.clear();
    }

    public void detach() {
        if (connected)
            createNotification();
        // items already in event queue (posted before detach() to mainLooper) will end up in queue1
        // items occurring later, will be moved directly to queue2
        // detach() and mainLooper.post run in the main thread, so all items are caught
//        listener = null;
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(Constants.NOTIFICATION_CHANNEL, "Background service", NotificationManager.IMPORTANCE_LOW);
            nc.setShowBadge(false);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(nc);
        }
        Intent disconnectIntent = new Intent()
                .setAction(Constants.INTENT_ACTION_DISCONNECT);
        Intent restartIntent = new Intent()
                .setClassName(this, Constants.INTENT_CLASS_MAIN_ACTIVITY)
                .setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent disconnectPendingIntent = PendingIntent.getBroadcast(this, 1, disconnectIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent restartPendingIntent = PendingIntent.getActivity(this, 1, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(socket != null ? "Connected to " + socket.getName() : "Background Service")
                .setContentIntent(restartPendingIntent)
                .setOngoing(true)
                .addAction(new NotificationCompat.Action(R.drawable.ic_clear_white_24dp, "Disconnect", disconnectPendingIntent));
        // @drawable/ic_notification created with Android Studio -> New -> Image Asset using @color/colorPrimaryDark as background color
        // Android < API 21 does not support vectorDrawables in notifications, so both drawables used here, are created as .png instead of .xml
        Notification notification = builder.build();
        startForeground(Constants.NOTIFY_MANAGER_START_FOREGROUND_SERVICE, notification);
    }

    private void cancelNotification() {
        stopForeground(true);
    }

    /**
     * SerialListener
     */
    public void onSerialConnect() {
        if (connected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialConnect();
                        } else {
                            queue1.add(new QueueItem(QueueType.Connect, null, null));
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.Connect, null, null));
                }
            }
        }
    }

    public void onSerialConnectError(Exception e) {
        if (connected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialConnectError(e);
                        } else {
                            queue1.add(new QueueItem(QueueType.ConnectError, null, e));
                            cancelNotification();
                            disconnect();
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.ConnectError, null, e));
                    cancelNotification();
                    disconnect();
                }
            }
        }
    }

    public void onSerialRead(byte[] data) {
        if (connected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialRead(data);
                        } else {
                            queue1.add(new QueueItem(QueueType.Read, data, null));
                        }
                        for (int i = 0; i < data.length - 31; i++) {
                            if (data[i] == (byte) 0x66 && data[i + 1] == (byte) 0xbe && data[i + 2] == (byte) 0x2d) {
                                updateFsrValueAction(data, i);
                            }
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.Read, data, null));
                }
            }
        }
    }

    public void onSerialIoError(Exception e) {
        if (connected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialIoError(e);
                        } else {
                            queue1.add(new QueueItem(QueueType.IoError, null, e));
                            cancelNotification();
                            disconnect();
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.IoError, null, e));
                    cancelNotification();
                    disconnect();
                }
            }
        }
    }

}

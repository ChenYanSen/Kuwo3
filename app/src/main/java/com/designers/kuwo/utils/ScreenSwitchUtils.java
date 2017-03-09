package com.designers.kuwo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ScreenSwitchUtils {
	
	private static final String TAG = ScreenSwitchUtils.class.getSimpleName();
	
	private volatile static ScreenSwitchUtils mInstance;
	
	private Activity mActivity;
	
	// �Ƿ�������
	private boolean isPortrait = true;
	
	private SensorManager sm;
	private OrientationSensorListener listener;
	private Sensor sensor;

	private SensorManager sm1;
	private Sensor sensor1;
	private OrientationSensorListener1 listener1;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 888:
				int orientation = msg.arg1;
				if (orientation > 65 && orientation < 135) {
					if (isPortrait) {
						Log.e("test", "切换成横屏");
						mActivity.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
						isPortrait = false;
					}

				} else if (orientation > 135 && orientation < 225) {
					if (!isPortrait) {
						Log.e("test","切换成竖屏");
						mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						isPortrait = true;
					}
				} else if (orientation > 225 && orientation < 315) {
					if (isPortrait) {
						Log.e("test", "切换成横屏");
						mActivity.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
						isPortrait = false;
					}
				} else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 65)) {
					if (!isPortrait) {
						Log.e("test","切换成竖屏");
						mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						isPortrait = true;
					}
				}

				break;
			default:
				break;
			}

		};
	};
	
	/** ����ScreenSwitchUtils���� **/
    public static ScreenSwitchUtils init(Context context) {
        if (mInstance == null) {
            synchronized (ScreenSwitchUtils.class) {
                if (mInstance == null) {
                    mInstance = new ScreenSwitchUtils(context);
                }
            }
        }
        return mInstance;
    }

    private ScreenSwitchUtils(Context context) {
        Log.i(TAG, "init orientation listener.");
        // ע��������Ӧ��,������Ļ��ת
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(mHandler);

        // ���� ��ת֮��/���ȫ��֮�� ���߷���һ��,����sm.
        sm1 = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener1 = new OrientationSensorListener1();
    }
    
    /** ��ʼ���� */
    public void start(Activity activity) {
    	Log.i(TAG, "start orientation listener.");
        mActivity = activity;
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /** ֹͣ���� */
    public void stop() {
    	Log.i(TAG, "stop orientation listener.");
    	sm.unregisterListener(listener);
		sm1.unregisterListener(listener1);
    }
    
    /**
     * �ֶ��������л�����
     */
    public void toggleScreen() {
		sm.unregisterListener(listener);
		sm1.registerListener(listener1, sensor1,SensorManager.SENSOR_DELAY_UI);
		if (isPortrait) {
			isPortrait = false;
			// �л��ɺ���
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			isPortrait = true;
			// �л�������
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
    
    public boolean isPortrait(){
    	return this.isPortrait;
    }
    
    /**
	 * ������Ӧ������
	 */
	public class OrientationSensorListener implements SensorEventListener {
		private static final int _DATA_X = 0;
		private static final int _DATA_Y = 1;
		private static final int _DATA_Z = 2;

		public static final int ORIENTATION_UNKNOWN = -1;

		private Handler rotateHandler;

		public OrientationSensorListener(Handler handler) {
			rotateHandler = handler;
		}

		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			int orientation = ORIENTATION_UNKNOWN;
			float X = -values[_DATA_X];
			float Y = -values[_DATA_Y];
			float Z = -values[_DATA_Z];
			float magnitude = X * X + Y * Y;
			// Don't trust the angle if the magnitude is small compared to the y
			// value
			if (magnitude * 4 >= Z * Z) {
				// ��Ļ��תʱ
				float OneEightyOverPi = 57.29577957855f;
				float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
				orientation = 90 - (int) Math.round(angle);

				while (orientation >= 360) {
					orientation -= 360;
				}
				while (orientation < 0) {
					orientation += 360;
				}


			}

//只检测是否有四个角度的改变
		/*	if( orientation > 350 || orientation< 10 ) { //0度
				orientation = 0;
			}
			else if( orientation > 80 &&orientation < 100 ) { //90度
				orientation= 90;
			}
			else if( orientation > 170 &&orientation < 190 ) { //180度
				orientation= 180;
			}
			else if( orientation > 260 &&orientation < 280  ) { //270度
				orientation= 270;
			}
			else {
				return;
			}*/



			if (rotateHandler != null) {
				rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
			}
		}
	}

	public class OrientationSensorListener1 implements SensorEventListener {
		private static final int _DATA_X = 0;
		private static final int _DATA_Y = 1;
		private static final int _DATA_Z = 2;

		public static final int ORIENTATION_UNKNOWN = -1;

		public OrientationSensorListener1() {
		}

		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			int orientation = ORIENTATION_UNKNOWN;
			float X = -values[_DATA_X];
			float Y = -values[_DATA_Y];
			float Z = -values[_DATA_Z];
			float magnitude = X * X + Y * Y;
			// Don't trust the angle if the magnitude is small compared to the y
			// value
			if (magnitude * 4 >= Z * Z) {
				// ��Ļ��תʱ
				float OneEightyOverPi = 57.29577957855f;
				float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
				orientation = 90 - (int) Math.round(angle);
				// normalize to 0 - 359 range
				while (orientation >= 360) {
					orientation -= 360;
				}
				while (orientation < 0) {
					orientation += 360;
				}
			}
			if (orientation > 225 && orientation < 315) {// ��⵽��ǰʵ���Ǻ���
				if (!isPortrait) {
					sm.registerListener(listener, sensor,SensorManager.SENSOR_DELAY_UI);
					sm1.unregisterListener(listener1);
				}
			} else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {// ��⵽��ǰʵ��������
				if (isPortrait) {
					sm.registerListener(listener, sensor,SensorManager.SENSOR_DELAY_UI);
					sm1.unregisterListener(listener1);
				}
			}
		}
	}
}

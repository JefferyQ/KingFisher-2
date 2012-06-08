package org.MAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

public class Shaker extends Activity implements SensorEventListener, SurfaceHolder.Callback {

	private static final String TAG = "Shaker";
	
	private MySurfaceView foreground;
	private SurfaceHolder holder;
	
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Vibrator vibrotron;
	private float m_totalForcePrev;
	private int shiverTimbers; //a counter for how many times the user has shaken the king
	
	private int catchID; //TODO: used to determine which king we will draw. add logic for more kings later.
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.shaker);
		
		Bundle extras = getIntent().getExtras(); 
        if(extras !=null) {
            catchID = extras.getInt("CatchID");
            Log.d(TAG, "Catch ID: " + catchID);
        }
        
        foreground = (MySurfaceView)findViewById(R.id.shaker_foreground);
        
        //TODO: fill sprites with the correct stuff based on our catch.
        foreground.addSprite(new Sprite("Napoleon", BitmapFactory.decodeResource(getResources(), R.drawable.napoleon_sprite1), 0, 0, 0));
        holder = foreground.getHolder();
        
        holder.addCallback(this);
        
		Log.e("KingFisher", "made the Shaker");
		
		SoundManager.loadSounds(SoundManager.SHAKABLE);
		
		vibrotron = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		sensorManager = (SensorManager)getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	@Override
	public void onPause() {
		sensorManager.unregisterListener(this);
		holder.removeCallback(this);
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		
		//TODO: wait, then "shake him down for the plunder!"
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	
	public void onSensorChanged(SensorEvent event) {
		double forceThreshHold = 2.5f;
        double totalForce = 0.0f;
        totalForce += Math.pow(event.values[SensorManager.DATA_X]/SensorManager.GRAVITY_EARTH, 2.0);
        totalForce += Math.pow(event.values[SensorManager.DATA_Y]/SensorManager.GRAVITY_EARTH, 2.0);
        totalForce += Math.pow(event.values[SensorManager.DATA_Z]/SensorManager.GRAVITY_EARTH, 2.0);
        totalForce = Math.sqrt(totalForce);
       
        if ((totalForce < forceThreshHold) && (m_totalForcePrev > forceThreshHold)) {
        	Log.e("KingFisher", "SHAKE!");
        	shiverTimbers++;
        	
        	vibrotron.vibrate(300);
        	SoundManager.playSound(1, 1);
        	
        	
        	
        	if (shiverTimbers > 15) {
        		sensorManager.unregisterListener(this);
        		//TODO: jump to the next activity now. bundle up which king was caught and send it along! we also need the levelID still.
        		
        		try {
                	Intent ourIntent = new Intent(Shaker.this, Class.forName("org.MAG.Rejecterator"));
                	ourIntent.putExtra("CatchID", catchID);
                	ourIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(ourIntent);
        			finish();
        		} catch (ClassNotFoundException ex) {
        			Log.e(TAG, "Failed to jump to another activity");
        		}
        		
        		
    		}
        	
        	drawSprites();
        }
       
        m_totalForcePrev = (float) totalForce;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) { }
	

	public void surfaceCreated(SurfaceHolder holder) {
		drawSprites();
	}

	public void surfaceDestroyed(SurfaceHolder holder) { }

	private void drawSprites() {
		if (holder.getSurface().isValid()) {
        	Log.d(TAG, "valid");
	        Canvas canvas = holder.lockCanvas();
	        foreground.draw(canvas);
	        holder.unlockCanvasAndPost(canvas);
        }
	}
	
}

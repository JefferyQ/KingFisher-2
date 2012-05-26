package org.MAG;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	
	static private SoundManager _instance;
	private static SoundPool mSoundPool; 
	private static HashMap<Integer, Integer> mSoundPoolMap; 
	private static HashMap<Integer, Integer> mSoundPoolClick; 
	private static HashMap<Integer, Integer> mSoundPoolCoins; 
	private static HashMap<Integer, Integer> mKingSayings; 
	private static HashMap<Integer, Integer> mJesterSayings; 
	private static HashMap<Integer, Integer> mSoundSplash;
	private static AudioManager  mAudioManager;
	private static Context mContext;
	
	public static final int INTRO = 0; //intro screen
	public static final int TITLE = 1; //title screen
	public static final int TUTORIAL = 2; //tutorial
	public static final int LEVEL = 3; //level selection screen
	public static final int TRAVEL = 4; //en route to level
	public static final int CASTABLE = 5; //casting screen
	public static final int REELABLE = 6; //reeling screen
	public static final int FAIL = 7; //king escaped splash
	public static final int SUCCESS = 8; //caught the king
	public static final int POUT = 9; //king looks stupid
	public static final int SHAKABLE = 10; //take his lunch money
	public static final int FLINGABLE = 11; //throw him in the river!
	public static final int ACHIEVEMENT = 12; //the loot
	public static final int VICTORY = 13; //YAY, YOU WIN!
	
	private SoundManager() { }
	
	/**
	 * Requests the instance of the Sound Manager and creates it
	 * if it does not exist.
	 * 
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance() 
	{
	    if (_instance == null) 
	      _instance = new SoundManager();
	    return _instance;
	 }
	
	/** TODO: do we need all of these different pools? we could just have one big hashmap for everything
	 * Initializes the storage for the sounds
	 * 
	 * @param theContext The Application context
	 */
	public static void initSounds(Context theContext) 
	{ 
		 mContext = theContext;
	     mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	     mSoundPoolMap = new HashMap<Integer, Integer>(); 
	     mSoundPoolClick = new HashMap<Integer, Integer>();
	     mSoundPoolCoins = new HashMap<Integer, Integer>(); 
	     mKingSayings = new HashMap<Integer, Integer>();
	     mJesterSayings = new HashMap<Integer, Integer>();
	     mSoundSplash = new HashMap<Integer, Integer>();
	     mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	} 
	
	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param Index - The Sound Index for Retrieval
	 * @param SoundID - The Android ID for the Sound asset.
	 */
	public static void addSound(int Index,int SoundID)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	/** TODO: run through each state, make sure we're loading the right sounds, get more audio assets from Nick et al
	 * Loads the various sound assets
	 * @param: state - game state
	 */
	public static void loadSounds(int state)
	{
		mAudioManager.unloadSoundEffects();
		
		switch (state) {
		case INTRO: //intro sounds
			break;
		case TITLE: //title sounds
			break;
		case TUTORIAL: //tutorial sounds
			break;
		case LEVEL: //level selection sounds
			mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.fishing_for_napoleon, 1));
			mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.shake_to_confirm, 1));
			break;
		case TRAVEL: //traveling sounds
			break;
		case CASTABLE: //reel casting
			mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.reelcast, 1));
			mJesterSayings.put(1, mSoundPool.load(mContext, R.raw.cast_away, 1));
			break;
		case REELABLE: //reeling sounds
			mSoundPoolClick.put(4, mSoundPool.load(mContext, R.raw.clickdouble, 1));
			mSoundPoolMap.put(5, mSoundPool.load(mContext, R.raw.snapped_the_line, 1));
			mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.hooked_something2, 1));
			mSoundPoolMap.put(3, mSoundPool.load(mContext, R.raw.got_away, 1));
			mSoundPoolMap.put(4, mSoundPool.load(mContext, R.raw.kings_not_garbage, 1));
			mJesterSayings.put(1, mSoundPool.load(mContext, R.raw.napoleon_plunder, 1));
			break;
		case FAIL: //fail sounds
			break;
		case SUCCESS: //success sounds
			break;
		case POUT: //king pouting sounds
			break;
		case SHAKABLE: //coins, loot, king whining
			mSoundPoolCoins.put(1, mSoundPool.load(mContext, R.raw.coinsmall, 1));
			break;
		case FLINGABLE: //king yelling, splash
			mSoundPoolMap.put(6, mSoundPool.load(mContext, R.raw.throw_it_back ,1));
			mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.splash ,1));
			//load up a splash sound to play after ah.
			mKingSayings.put(1, mSoundPool.load(mContext, R.raw.ah, 1));
			break;
		case ACHIEVEMENT: //why can't I hold all these golds?
			break;
		case VICTORY: //yay, you win.
			break;
		}
	}
	
	/** TODO: we will only need playSound after the above TODOs are completed. remove the others.
	 * Plays a Sound
	 * 
	 * @param index - The Index of the Sound to be played
	 * @param speed - The Speed to play not, not currently used but included for compatibility
	 */
	public static void playSound(int index,float speed) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed); 
	}
	
	public static void playClickSound(int index,float speed) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mSoundPoolClick.get(index), streamVolume, streamVolume, 1, 0, speed); 
	}
	
	public static void playKingSound(int index,float speed) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mKingSayings.get(index), streamVolume, streamVolume, 1, 0, speed); 
	}
	public static void playCoinSound(int index,float speed) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mSoundPoolCoins.get(index), streamVolume, streamVolume, 1, 0, speed); 
	}
	public static void playJesterSound(int index,float speed) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mJesterSayings.get(index), streamVolume, streamVolume, 1, 0, speed); 
	}
	public static void playSplashSound(int index,float speed) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mSoundSplash.get(index), streamVolume, streamVolume, 1, 0, speed); 
	}
	
	/**
	 * Stop a Sound
	 * @param index - index of the sound to be stopped
	 */
	public static void stopSound(int index)
	{
		mSoundPool.stop(mSoundPoolMap.get(index));
	}
	
	/**
	 * Release sound assets, clear the hashmap, unload the audio manager, destroy the static instance
	 */
	public static void cleanup()
	{
		if (!(mSoundPool == null))
			mSoundPool.release();
		mSoundPool = null;
	    mSoundPoolMap.clear();
	    mAudioManager.unloadSoundEffects();
	    _instance = null;
	}
}
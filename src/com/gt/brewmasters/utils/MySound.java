package com.gt.brewmasters.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;


public class MySound
{


	private final int duration = 1;
	private final int sampleRate = 8000;
	private final int numSamples = this.duration * this.sampleRate;
	private final double sample[] = new double[this.numSamples];
	private final double freqOfTone = 880;

	private final byte generatedSnd[] = new byte[2 * this.numSamples];

	public MySound() 
	{
		for (int i = 0; i < this.numSamples; ++i) 
		{
			this.sample[i] = Math.sin(2 * Math.PI * i / (this.sampleRate / this.freqOfTone));
		}


		int idx = 0;
		for (final double dVal : this.sample) 
		{
			final short val = (short) ((dVal * 32767));
			this.generatedSnd[idx++] = (byte) (val & 0x00ff);
			this.generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

		}
	}

	public void playSound() 
	{
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,this.sampleRate, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT, this.numSamples,AudioTrack.MODE_STATIC);
		audioTrack.write(this.generatedSnd, 0, this.generatedSnd.length);
		audioTrack.play();
	}
}
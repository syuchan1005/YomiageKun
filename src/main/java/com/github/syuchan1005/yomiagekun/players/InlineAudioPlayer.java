package com.github.syuchan1005.yomiagekun.players;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by syuchan on 2016/11/17.
 */
public class InlineAudioPlayer extends Thread {
	private static final AudioFormat linearPCM = new AudioFormat(16000f, 16, 1, true, true);

	private static DataLine.Info info = new DataLine.Info(Clip.class, linearPCM);
	private Queue<byte[]> queue = new ArrayDeque<>();
	private Clip clip;

	public InlineAudioPlayer() {
		try {
			this.clip = (Clip) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		this.start();
	}

	public void addQueue(byte[] bytes) {
		queue.add(bytes);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
				byte[] bytes = queue.poll();
				if (bytes == null) continue;
				clip.open(linearPCM, bytes, 0, bytes.length);
				clip.start();
				Thread.sleep(clip.getMicrosecondLength() / 1000);
				clip.close();
			} catch (InterruptedException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
}

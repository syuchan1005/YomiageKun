package com.github.syuchan1005.yomiagekun.players;

import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Created by syuchan on 2016/12/30.
 */
public class DiscordAudioPlayer extends Thread {
	private static final AudioFormat linearPCM = new AudioFormat(16000f, 16, 1, true, true);

	private static DataLine.Info info = new DataLine.Info(Clip.class, linearPCM);
	private Queue<byte[]> queue = new ArrayDeque<>();
	private Clip clip;

	private Set<IVoiceChannel> voiceChannels = new HashSet<>();

	public DiscordAudioPlayer() {
		try {
			this.clip = (Clip) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		this.start();
	}

	public void addVoiceChannel(IVoiceChannel voiceChannel) {
		voiceChannels.add(voiceChannel);
	}

	public void deleteVoiceChannel(IVoiceChannel voiceChannel) {
		voiceChannels.remove(voiceChannel);
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
				AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(bytes)
						, linearPCM, bytes.length);
				for (IVoiceChannel voiceChannel : voiceChannels) {
					AudioPlayer audioPlayer = AudioPlayer.getAudioPlayerForGuild(voiceChannel.getGuild());
					audioPlayer.queue(audioInputStream);
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}

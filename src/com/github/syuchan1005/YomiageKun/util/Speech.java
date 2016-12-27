package com.github.syuchan1005.YomiageKun.util;

import jp.ne.docomo.smt.dev.aitalk.AiTalkTextToSpeech;
import jp.ne.docomo.smt.dev.aitalk.data.AiTalkSsml;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;
import jp.ne.docomo.smt.dev.common.http.AuthApiKey;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by syuchan on 2016/03/26.
 */
public class Speech extends Thread {
	private static Queue<byte[]> queue = new ArrayDeque<>();
	private static AiTalkTextToSpeech aiTalkTextToSpeech = new AiTalkTextToSpeech();
	private static AudioFormat af = new AudioFormat(16000f, 16, 1, true, true);
	private static DataLine.Info info = new DataLine.Info(Clip.class, af);
	private static Clip clip;
	private static Speech speech;

	public static void init(String apiKey) {
		if (speech == null) {
			AuthApiKey.initializeAuth(apiKey);
			speech = new Speech();
			speech.start();
		}
		if (clip == null) {
			try {
				clip = (Clip) AudioSystem.getLine(info);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] speakFemale(String text) throws RestApiException {
		return Speech.speak(Speaker.MAKI, text);
	}

	public static byte[] speak(Speaker speaker, String text) throws RestApiException {
		return speak(speaker, text, false);
	}

	public static byte[] speak(Speaker speaker, String text, boolean isSSML) throws RestApiException {
		if (!isSSML) {
			AiTalkSsml aiTalkSsml = new AiTalkSsml();
			aiTalkSsml.startVoice(speaker.getName());
			aiTalkSsml.addText(text.replace('<', ' ').replace('>', ' '));
			aiTalkSsml.endVoice();
			text = aiTalkSsml.makeSsml();
		}
		byte[] e = aiTalkTextToSpeech.requestAiTalkSsmlToSound(text);
		queue.add(e);
		return e;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
				byte[] bytes = queue.poll();
				if (bytes == null) continue;
				clip.open(af, bytes, 0, bytes.length);
				clip.start();
				Thread.sleep(clip.getMicrosecondLength() / 1000);
				clip.close();
			} catch (InterruptedException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	public enum Speaker {
		NOZOMI,
		SEIJI,
		AKARI,
		ANZU,
		HIROSHI,
		KAHO,
		KOUTAROU,
		MAKI,
		NANAKO,
		OSAMU,
		SUMIRE;

		public String getName() {
			return this.name().toLowerCase();
		}
	}

}

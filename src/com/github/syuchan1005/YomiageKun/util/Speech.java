package com.github.syuchan1005.YomiageKun.util;

import jp.ne.docomo.smt.dev.aitalk.AiTalkTextToSpeech;
import jp.ne.docomo.smt.dev.aitalk.data.AiTalkSsml;
import jp.ne.docomo.smt.dev.common.http.AuthApiKey;

import javax.sound.sampled.*;
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

	static  {
		AuthApiKey.initializeAuth("376a47742f574c6f685144476c6d4552786b424656644833312e325573394f562f58636272304945644131");
		speech = new Speech();
		speech.start();
	}

	public static void speckMale(String text) {
		Speech.speak(Speaker.OSAMU, text);
	}

	public static void speakFemale(String text) {
		Speech.speak(Speaker.MAKI, text);
	}

	public static void speak(Speaker speaker, String text) {
		AiTalkSsml aiTalkSsml = new AiTalkSsml();
		aiTalkSsml.startVoice(speaker.getName());
		aiTalkSsml.addText(text);
		aiTalkSsml.endVoice();
		aiTalkSsml.addBreak(100);
		try {
			byte[] bytes = aiTalkTextToSpeech.requestAiTalkSsmlToSound(aiTalkSsml.makeSsml());
			if (clip == null) clip = (Clip) AudioSystem.getLine(info);
			queue.add(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isSelectSpeaker(String text, String sender) {
		for (Speaker speaker : Speaker.values()) {
			if (text.startsWith(speaker.getName())) {
				text = text.substring(speaker.getName().length() + 1);
				speak(speaker, sender + text);
				return true;
			}
		}
		return false;
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

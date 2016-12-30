package com.github.syuchan1005.yomiagekun;

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
public class DocomoSpeech {
	private static AiTalkTextToSpeech aiTalkTextToSpeech = new AiTalkTextToSpeech();
	private static DocomoSpeech speech;

	private DocomoSpeech() {}

	public static void init(String apiKey) {
		AuthApiKey.initializeAuth(apiKey);
	}

	public static byte[] speakFemale(String text) throws RestApiException {
		return speak(Speaker.MAKI, text);
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
		return aiTalkTextToSpeech.requestAiTalkSsmlToSound(text);
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
package com.github.syuchan1005.yomiagekun;

import com.github.syuchan1005.yomiagekun.contents.ReadContent;
import com.github.syuchan1005.yomiagekun.controllers.DiscordController;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;

import java.util.Optional;

/**
 * Created by syuchan on 2016/12/31.
 */
public class DiscordWrapper {
	private static DiscordController discordController;
	private IDiscordClient discordClient;
	private IListener<MessageReceivedEvent> listener;
	private static String lastName = "";

	public static void setDiscordController(DiscordController discordController) {
		DiscordWrapper.discordController = discordController;
	}

	@Deprecated
	public DiscordWrapper(String email, String password) throws DiscordException {
		discordClient = new ClientBuilder().withLogin(email, password).login();
	}

	public DiscordWrapper(String token) throws DiscordException {
		discordClient = new ClientBuilder().withToken(token).login();
	}

	public IDiscordClient getDiscordClient() {
		return discordClient;
	}

	public void readStart() {
		if (listener == null) {
			listener = new IListener<MessageReceivedEvent>() {
				@Override
				public void handle(MessageReceivedEvent event) {
					IMessage message = event.getMessage();
					String topic = message.getChannel().getTopic();
					if (topic != null && topic.contains("!read") || event.getMessage().getAuthor().isBot()) return;
					try {
						String content = message.getContent();
						if (content.equalsIgnoreCase("!join")) {
							YomiageKun.getDiscordAudioPlayer().addVoiceChannel(message.getAuthor().getConnectedVoiceChannels().get(0));
						}
						if (content.equalsIgnoreCase("!leave")) {
							YomiageKun.getDiscordAudioPlayer().deleteVoiceChannel(message.getAuthor().getConnectedVoiceChannels().get(0));
						}
						Optional<String> nicknameForGuild = message.getAuthor().getNicknameForGuild(message.getGuild());
						String nick = nicknameForGuild.isPresent() ? nicknameForGuild.get() : message.getAuthor().getName();
						discordSpeech(nick, content);
					} catch (RestApiException e1) {
					}
				}
			};
		}
		discordClient.getDispatcher().registerListener(listener);
	}

	public void readStop() {
		discordClient.getDispatcher().unregisterListener(listener);
	}

	private void discordSpeech(String nick, String text) throws RestApiException {
		if (text.startsWith("\\") || text.startsWith("!") || text.startsWith("`")) return;
		text = text.replaceAll("<:(\\w+):\\d+>", ":$1:"); // stamp
		String rep = YomiageKun.getInstance().studyText(nick, text, !lastName.equals(nick));
		discordController.addReadingList(nick, text, rep);
		byte[] bytes = DocomoSpeech.speakFemale(rep);
		if (discordController.isDiscordSpeak()) {
			YomiageKun.getDiscordAudioPlayer().addQueue(bytes);
		}
		YomiageKun.getInlineAudioPlayer().addQueue(bytes);
		lastName = nick;
	}
}

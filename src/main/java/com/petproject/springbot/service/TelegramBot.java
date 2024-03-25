package com.petproject.springbot.service;

import com.petproject.springbot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
  final BotConfig config;

  public TelegramBot(BotConfig config) {
    this.config = config;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if(update.hasMessage() && update.getMessage().hasText()) {
      var messageText = update.getMessage().getText();
      var chatId = update.getMessage().getChatId();

      switch (messageText) {
        case "/start":
          startCommandReceived(chatId, update.getMessage().getChat().getUserName());
        default:
          sendMessage(chatId, "Not supported Command");
      }
    }
  }

  @Override
  public String getBotUsername() {
    return config.getBotName();
  }

  @Override
  public String getBotToken() {
    return config.getToken();
  }

  private void startCommandReceived(long chatId, String userName) {
    var answer = "Hi, " + userName + "!";
    sendMessage(chatId, answer);
  }

  private void sendMessage(long chatId, String textToSend) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(textToSend);

    try {
      execute(message);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}

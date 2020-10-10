package client

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


class TxantxangorriBot: TelegramLongPollingBot() {
    val TOKEN = "1309064279:AAFLFm6TrWeWUk_A510WSDj9pOiRiAghD28"
    val NAME = "Txantxangorri_bot"

    /**
     * Returns the token of the bot to be able to perform Telegram Api Requests
     * @return Token of the bot
     */
    override fun getBotToken(): String {
        return TOKEN
    }

    /**
     * This method is called when receiving updates via GetUpdates method
     * @param update Update received
     */
    override fun onUpdateReceived(update: Update?) {
        val messageTextReceived = update!!.message.text
        val chatId = update.message.chatId
        val message = SendMessage().setChatId(chatId).setText(messageTextReceived)
        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    /**
     * Return bot username of this bot
     */
    override fun getBotUsername(): String {
        return  NAME
    }
}

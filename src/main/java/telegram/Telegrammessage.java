package telegram;


import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author my name is nobody
 */
public class Telegrammessage extends TelegramLongPollingBot {
    
    private String robot_name;
    private String token_robot;
    
    private List<String> message = new ArrayList();
    
    public Telegrammessage(String robot_name, String token_robot)
    {
        this.robot_name  = robot_name;
        this.token_robot = token_robot;
    }
    
    
    public String message()
    {
        if(message.isEmpty())
            return null;       
        else
            return message.remove(0);
    }
    
    
    public void onUpdateReceived(Update update) {
        // TODO
        
        if (update.hasMessage() && update.getMessage().hasText())
        {
            message.add(update.getMessage().getText()+":"+update.getMessage().getChatId());   
        }
        /*
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
         */  
    }

    @Override
    public String getBotUsername() {
        // TODO
        return robot_name;
    }

    @Override
    public String getBotToken() {
        // TODO
        return token_robot;
    }    
}

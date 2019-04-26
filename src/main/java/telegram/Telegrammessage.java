package telegram;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.TextArea;
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
    
    private TextArea watch_bot;
    
    private String text;
    private boolean first_time = true;
    
    public Telegrammessage(String robot_name, String token_robot,TextArea value)
    {
        this.robot_name  = robot_name;
        this.token_robot = token_robot;
        
        this.watch_bot = value;
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
            
            if(first_time)
            {
                text = "ID : " + update.getMessage().getChatId()+"\n"+
                       "Author : " + update.getMessage().getChat().getUserName()+"\n"+
                       "Date : " + new Date(TimeUnit.SECONDS.toMillis(update.getMessage().getDate())).toString()+"\n"+
                       "Message : " + update.getMessage().getText()+"\n"; 
                
                first_time = false;
            }else
            {
                text = text + "\n"+ 
                       "ID : " + update.getMessage().getChatId()+"\n"+
                       "Author : " + update.getMessage().getChat().getUserName()+"\n"+
                       "Date : " + new Date(TimeUnit.SECONDS.toMillis(update.getMessage().getDate())).toString()+"\n"+
                       "Message : " + update.getMessage().getText()+"\n"; 
            }
            
            watch_bot.setText(text);            
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

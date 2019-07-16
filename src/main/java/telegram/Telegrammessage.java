package telegram;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
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
    private String log;
    private boolean first_time = true;
    
    private Logger logger;
    
    public Telegrammessage(String robot_name, String token_robot,TextArea value)
    {
        this.robot_name  = robot_name;
        this.token_robot = token_robot;
        
        this.watch_bot = value;
        
        logger = log_generic_creator("useraction","generic_log_"+new Date().toString());
    }
    
    
    public String message()
    {
        if(message.isEmpty())
            return null;       
        else
            return message.remove(0);
    }
       
    private Logger log_generic_creator(String namelog,String pathlog)
    {
             
        Logger logger = Logger.getLogger(namelog);
        FileHandler fh;
            
        try {  
            fh = new FileHandler(pathlog);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);
        } catch (IOException ex) {
            Logger.getLogger(telegramwalletmessagehandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(telegramwalletmessagehandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return logger;
    }
    
    
    public void onUpdateReceived(Update update) {
        // TODO
        
        if (update.hasMessage() && update.getMessage().hasText())
        {
            message.add(update.getMessage().getText().replace(":", "").replace("|", "").trim()+":"+update.getMessage().getChatId());   
            
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
            
                            
            log = "ID : " + update.getMessage().getChatId()+"\n"+
                  "Author : " + update.getMessage().getChat().getUserName()+"\n"+
                  "Date : " + new Date(TimeUnit.SECONDS.toMillis(update.getMessage().getDate())).toString()+"\n"+
                  "Message : " + update.getMessage().getText()+"\n"; 

            logger.info(log);
            
            watch_bot.setText(text);            
        }
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

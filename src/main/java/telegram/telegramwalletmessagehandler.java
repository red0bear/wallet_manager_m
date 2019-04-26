/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram;

import generic_command_pack.generic_cli;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;
import javax.imageio.ImageIO;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author my name is nobody
 */
public class telegramwalletmessagehandler {
    
    private Telegrammessage messager;
    private TelegramBotsApi telegramBotsApi;
    
    private WorkerSendMessageTelegram telegraminfo = null;
    private Thread t_telegraminfo;
      
    private generic_cli command;
    
    private String current_id = "";
    
    private boolean runable_auto  = true; 
    
    private List<String> messages;
    
    private String message;
    
    
    private List<userinfocontrol> user_control;

    /**/
    private List<String> wallet_name;
    private List<URL> wallet_conn;
    
    
    private int global_counter = 0;
    
     
    public telegramwalletmessagehandler(String id, String user,TextArea text)
    {        
        
        ApiContextInitializer.init();
        messager = new Telegrammessage(id,user,text);
        telegramBotsApi = new TelegramBotsApi();
        
        try {
            telegramBotsApi.registerBot(messager);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } 
        
        messages     = new ArrayList();
        
        user_control = new ArrayList();
    } 
    
    
    private String help()
    {
        String help =" XXX !help                          --> see all options \n"+
                     " XXX !balance                       --> see your balance in wallet \n"+
                     " XXX !listaddrstr                   --> generate new addr hash string \n"+
                     " XXX !getnewaddrstr                 --> generate new addr hash string \n"+
                     " XXX !generateaddrqrcode addrstring --> generate new addr hash image \n"+
                     " XXX !send addr value               -->   Send to address a amount of coin";
        return help;
    }

    public List<String> get_list()
    {
        return messages;
    }
    
    public void StartIpMachine()
    {
        telegraminfo   = new WorkerSendMessageTelegram();
        t_telegraminfo = new Thread(telegraminfo);
        t_telegraminfo.setDaemon(true);
        t_telegraminfo.start();             
    }
    
    public void StopIpMachine()
    {
        runable_auto = false;
    }
    
    /**/
    public List<String> get_list_permissions(int index)
    {
        return user_control.get(index).list_perm();
    }
    
    /*RPC WORK*/
    public void set_url_wallet(List<String> wallet_name, List<URL> wallet_conn)
    {
        this.wallet_name = wallet_name;
        this.wallet_conn = wallet_conn;
    }
      
    /*USER*/
    public void add_user(String id,String name)
    {
        boolean found_id = false;
        if(user_control.isEmpty())
        {
        }else
        {
            for(userinfocontrol aux :user_control)
            {
                if(aux.get_user_id().compareTo(id) == 0)
                {
                    found_id = true;
                }
            }
        }
        
        if(found_id)
        {}
        else
        {
            if(id.compareTo("") == 0 || name.compareTo("") == 0)
            {}
            else
            {
                userinfocontrol a = new userinfocontrol(id,name);
                user_control.add(a);
            }
        }
    }

    public void rem_user(int index)
    {
        user_control.remove(index);
    }
    
    /*UPDATE*/
    public void update_permissions(int index_user,int index_wallet, String wallet)
    {
        boolean found_id = false;   
        
        if(user_control.isEmpty())
        {}
        else
        {
            for(String aux : user_control.get(index_user).list_perm())
            {
                if(aux.compareTo(wallet.toUpperCase()) == 0)
                {
                    found_id = true;
                }
            }
        }
        
        if(found_id)
        {}
        else
        {
            if(wallet.compareTo("") == 0)
            {}
            else
            {
                user_control.get(index_user).add_permission(wallet);
                
                if(new generic_cli(wallet_conn.get(index_wallet)).generic_get_address_by_account(user_control.get(index_user).get_user_id()).size() > 0)
                {}
                else
                {
                    new generic_cli(wallet_conn.get(index_wallet)).generic_new_account(user_control.get(index_user).get_user_id());
                }
            }
        }
    }
    
    public List<String> rem_permissions(int index_user,int index_wallet)
    {
        if(user_control.isEmpty())
        {
        }
        else if(user_control.get(index_user).list_perm().isEmpty())
        {
        }
        else
        {
            user_control.get(index_user).rem_permission(index_wallet);
            
            return user_control.get(index_user).list_perm();
        }
        
        return null;
    }
    
    private boolean check_permission(String value)
    {
        int user_found = 0;
        
        for(userinfocontrol aux : user_control)
        {
            if(aux.get_user_id().compareTo(current_id) == 0)
            {
                break;
            }
            else
            {
                user_found++;
            }
        }
        
        if(user_control.get(user_found).list_perm().isEmpty())
        {
            return false;
        }
                
        for(String aux : user_control.get(user_found).list_perm())
        {
            if(aux.compareTo(value) == 0)
            {
                return true;
            }
        }
        
        return false;
    }
 
    private void handle_commands_wallet(String value)
    {
       int state_walker=0;
       int type_wallet=0;
       int id_select=0;
    
       String values[] = value.split(" ");
       
       if(!check_permission(values[0]))
       {
           return;
       }
       
       
       for(String aux:values)
       {
           switch(state_walker)
           {
               /*SEE WALLET TO ISSUE COMMAND*/
               case 0:
                   for(String aux_name:wallet_name)
                   {
                       if(aux_name.compareTo(aux) == 0)
                       {
                           state_walker =1;
                           break;
                       }else
                       {
                           type_wallet++;
                       }
                   }
                   
               break;
               /*WALLET COMMAND*/
               case 1:
                   
                   command  = new generic_cli(wallet_conn.get(type_wallet));
                   
                   if(aux.compareTo("!help") == 0)
                   {
                       message_t_sent(help());
                       return;
                   }
                   else if(aux.compareTo("!balance") == 0)
                   {
                     
                     for(String account : command.generic_list_accounts())
                     {
                        if(account.compareTo(current_id) == 0)
                        {
                             message_t_sent(wallet_name.get(type_wallet));
                             message_t_sent("Balance is : " + command.generic_get_address_by_account().get(id_select));
                             return;
                        }else
                        {
                           id_select++;
                        }
                     }
                   }
                   else if(aux.compareTo("!listaddrstr") == 0)
                   {
                       message_t_sent(wallet_name.get(type_wallet));
                                             
                       for(String aux_list : command.generic_get_address_by_account(current_id))
                       {
                           message_t_sent(aux_list);
                       }
                       
                       return;
                   }
                   else if(aux.compareTo("!getnewaddrstr") == 0)
                   {
                       message_t_sent(wallet_name.get(type_wallet));
                       message_t_sent(command.generic_new_account_g(current_id));
                       return;
                   }
                   else if(aux.compareTo("!generateaddrqrcode") == 0)
                   {
                       if(values.length < 3)
                       {
                           message_t_sent(wallet_name.get(type_wallet) + " something is missing .... ");
                           return;
                       }
                        
                       /**/
                           
                       boolean found_addr = false;
                       for(String aux_list : command.generic_get_address_by_account(current_id))
                       {
                           if(aux_list.compareTo(values[2]) == 0)
                           {
                               found_addr = true;
                           }
                       }
                       
                       if(found_addr)
                       {    
                            message_t_sent(wallet_name.get(type_wallet));
                            message_t_sent_image(values[2], command.get_qr_code(values[2]));
                            return;
                       }else
                       {
                           message_t_sent(wallet_name.get(type_wallet) + " address not found .... ");
                           return;
                       }
                   }
                   else if(aux.compareTo("!send") == 0)
                   {                        
                       String result = command.generic_send_from_to_address(current_id, values[2], new BigDecimal(values[3]));
                       message_t_sent(wallet_name.get(type_wallet));
                       message_t_sent("txid result");
                       message_t_sent(result);
                       return;
                    }
               break;          
           }
       
       }
       
    }
    
    /*TELEGRAM*/
    private void message_t_sent(String message_s)
    {           
        SendMessage message;
        message = new SendMessage()
                .setChatId(Long.parseLong(current_id))
                .setText(message_s);
        try {
         messager.execute(message); 
        } catch (TelegramApiException e) {
         // e.printStackTrace();
        }
    
    } 

    private void message_t_sent_image(String hash, BufferedImage image)
    {           
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException ex) {
            Logger.getLogger(telegramwalletmessagehandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setCaption(hash);
        sendPhoto.setPhoto(hash,new ByteArrayInputStream(os.toByteArray()));
        sendPhoto.setChatId(current_id);
       
        SendMessage message;
        message = new SendMessage()
                .setChatId(Long.parseLong(current_id));
        try {
         messager.execute(sendPhoto); 
        } catch (TelegramApiException e) {
         // e.printStackTrace();
        }
        
            
    }     
        
    class WorkerSendMessageTelegram implements Runnable
    {
    
        @Override
        public void run() 
        {
  
            while(runable_auto)
            {   

                String received = messager.message();
                    
                if(received == null)
                {}
                else if(user_control.isEmpty())
                {}
                else
                {
                   //System.out.println(received);

                    for(userinfocontrol aux : user_control)
                    {
                        if(aux.get_user_id().compareTo(received.split(":")[1]) == 0)
                        {
                            current_id = received.split(":")[1];
                            handle_commands_wallet(received.split(":")[0]);
                        }
                    }
                }
                
                try { 
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ex) {
                  //  Logger.getLogger(RSTRUNNING.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }   
        
    }
}

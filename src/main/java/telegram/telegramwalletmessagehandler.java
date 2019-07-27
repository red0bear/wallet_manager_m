/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram;

import generic_command_pack.generic_command_cli_jna;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;
import javax.imageio.ImageIO;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
      
    private generic_command_cli_jna command;
    
    private String current_id = "";
    
    private boolean runable_auto  = true; 
    
    private List<String> messages;
    
    private List<userinfocontrol> user_control;
    private userinfocontrol selected ;
    
    /**/
    private List<String> wallet_name;
    private List<String> wallet_conn;    
    
    private List<generic_command_cli_jna> client_manager;
    
    private ResourceBundle bundle_manager ;

    
    public telegramwalletmessagehandler(String id, String user,TextArea text, List<generic_command_cli_jna> client_manager)
    {        
        
        ApiContextInitializer.init();
        messager = new Telegrammessage(id,user,text);
        telegramBotsApi = new TelegramBotsApi();
        this.client_manager = client_manager;
        
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
    public void set_url_wallet(List<String> wallet_name, List<String> wallet_conn)
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
                
                generic_command_cli_jna cmd = new generic_command_cli_jna(wallet_conn.get(index_wallet),client_manager.get(index_wallet).is_deprecated());
                
                cmd.wallet_secondary_command(true);
                
                if(client_manager.get(index_wallet).is_deprecated())
                {}    
                else
                    cmd.direct_set_wallet(user_control.get(index_user).get_user_id());
                                
                if(cmd.generic_get_address_by_account(user_control.get(index_user).get_user_id()).size() > 0)
                {}
                else
                {
                    cmd.generic_new_account(user_control.get(index_user).get_user_id());
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
    
    private void select_wallet()
    {
        String[][] keyboardWallet = selected.get_wallet_allowed();
        sendReplyKeyboard(Long.valueOf(current_id), bundle_manager.getString("CHOOSE"), keyboardWallet, true);
    }
    
    private void command_wallet_telegram()
    {
        String[][] commands = {{bundle_manager.getString("balance")},
                              {bundle_manager.getString("listaddrstr")},
                              {bundle_manager.getString("getnewaddrstr")},
                             /// {"generateaddrqrcode"},
                              {bundle_manager.getString("send")},
                              {bundle_manager.getString("back")}};
               
        sendReplyKeyboard(Long.valueOf(current_id), bundle_manager.getString("COMMAND"), commands, true);    
    }
    
    
    private void set_lenguage(String value)
    {
        if(value.toUpperCase().compareTo("ENGLISH - US") == 0)
        {
            bundle_manager = ResourceBundle.getBundle("bundles.bundle_lenguage", new Locale("en", "EN"));
        }else if(value.toUpperCase().compareTo("PORTUGUES - PT") == 0)
        {
            bundle_manager = ResourceBundle.getBundle("bundles.bundle_lenguage", new Locale("pt", "PT"));
        }else if(value.toUpperCase().compareTo("РОССИЯ - RUS") == 0)
        {
            bundle_manager = ResourceBundle.getBundle("bundles.bundle_lenguage", new Locale("rus", "RUS"));
        }
        else
        {
            bundle_manager = ResourceBundle.getBundle("bundles.bundle_lenguage", new Locale("en", "EN"));
        }
    }
    
    
    private String balance_label_find()
    {
        int id_select=0;
                
        for(String account : command.generic_list_accounts())
        {
            if(account.compareTo(current_id) == 0)
            {
                break;
            }else
            {
                id_select++;
            }
        } 
        
        return command.generic_get_address_by_account().get(id_select);
    }
       
    private void message_handle(String value)
    {        
       int type_wallet=0; 
          
        switch(selected.get_state())
        {
            case 0:        
                
                String[][] lenguage = {{"ENGLISH - US"},{"PORTUGUES - PT"},{"РОССИЯ - RUS"}};
                sendReplyKeyboard(Long.valueOf(current_id),"SELECT",lenguage, true);   
                selected.update_state(1);
                
            break;
            case 1:
                
                set_lenguage(value);
                        
                String[][] keyboardText = {{bundle_manager.getString("CELL")},{bundle_manager.getString("CMD")}};
                sendReplyKeyboard(Long.valueOf(current_id), bundle_manager.getString("CHOOSE") ,keyboardText, true);   
                selected.update_state(2);
                
            break;
            case 2:
                
                if(value.toUpperCase().compareTo(bundle_manager.getString("CELL")) == 0)
                {
                    select_wallet();
                    selected.update_state(3);                  
                }
                else if(value.toUpperCase().compareTo(bundle_manager.getString("CMD")) == 0)
                {
                   selected.update_state(9);
                }
            break;
            case 3:
                
                selected.set_wallet(value);      
                command_wallet_telegram();  
                selected.update_state(4);
                
            break;
            case 4:

                type_wallet = 0;
                
                for(String aux_name:wallet_name)
                {
                     if(selected.get_wallet().compareTo(aux_name.toUpperCase()) == 0)
                     {
                        // state_walker =1;
                         break;
                     }else
                     {
                        type_wallet++;
                     }
                }

                command  = new generic_command_cli_jna(wallet_conn.get(type_wallet),client_manager.get(type_wallet).is_deprecated());

                command.wallet_secondary_command(true);
                
                if(client_manager.get(type_wallet).is_deprecated())
                {}    
                else
                    command.direct_set_wallet(selected.get_user_id());
                
               if(value.toUpperCase().compareTo(bundle_manager.getString("balance")) == 0)
               {      
                    message_t_sent(wallet_name.get(type_wallet));
                    message_t_sent(bundle_manager.getString("ebalance") + " " + balance_label_find());

                    command_wallet_telegram();  
                    selected.update_state(4);                                    
               }
               else if(value.toUpperCase().compareTo(bundle_manager.getString("listaddrstr")) == 0)
               {                          
                    String[][] addrs = selected.get_List_allowed(command.generic_get_address_by_account(current_id));
                    sendReplyKeyboard(Long.valueOf(current_id), bundle_manager.getString("mlist") + " " + wallet_name.get(type_wallet), addrs, true);
                            
                    ///command_wallet_telegram();  
                    selected.update_state(5); 
                }
                else if(value.toUpperCase().compareTo(bundle_manager.getString("getnewaddrstr")) == 0)
                {
                    String value_r = command.generic_new_account_g(current_id);
                    
                    message_t_sent(wallet_name.get(type_wallet));
                    message_t_sent(value_r);
                    message_t_sent_image(value_r, command.get_qr_code(value_r));
                    
                    command_wallet_telegram();  
                    selected.update_state(4);
                           
                }
                else if(value.toUpperCase().compareTo(bundle_manager.getString("send")) == 0)
                {
                    message_t_sent(bundle_manager.getString("maddr") + " " + wallet_name.get(type_wallet));
                    selected.update_state(6);                                            
                }
                else if(value.toUpperCase().compareTo(bundle_manager.getString("back")) == 0)
                {
                    
                    select_wallet();
                    selected.set_wallet_hash("");
                    selected.set_wallet("");
                    //selected.update_sub_state(0);
                    selected.update_state(3);  
                     
                }          
               

            break;
            case 5:
                                    
                command_wallet_telegram();  
                selected.set_wallet_hash(value);
                message_t_sent(selected.get_wallet_hash());
                message_t_sent_image(selected.get_wallet_hash(), command.get_qr_code(selected.get_wallet_hash()));                
                selected.update_state(4);
                
            break;
            case 6:
               
               message_t_sent(bundle_manager.getString("evalue") + " " + balance_label_find());
               selected.set_hash_to_send(value);
               selected.update_state(7);
               
            break;
            case 7:
               
               //message_t_sent("Please enter the value " + command.generic_get_address_by_account().get(id_select));
               //selected.set_hash_to_send(value);
               
                if(new BigDecimal(value).compareTo(new BigDecimal(balance_label_find())) <= 0 )
                {    
                    String result = command.generic_send_from_to_address(current_id, selected.get_hash_to_send(), new BigDecimal(value));
                    message_t_sent(wallet_name.get(type_wallet));
                    message_t_sent(bundle_manager.getString("etxid"));
                    message_t_sent(result);
                    select_wallet();
                    selected.update_state(2);
                }
                else
                {                       
                    message_t_sent(bundle_manager.getString("evalue") + " " + balance_label_find());
                    selected.update_state(7);
                }
                
            break;
            case 9:

                int id_select=0;
                type_wallet=0;
                
                String values[] = value.split(" ");
       
                if(!check_permission(values[0].toUpperCase()))
                {
                    return;
                }
                
                for(String aux_name:wallet_name)
                {
                     if(values[0].toUpperCase().compareTo(aux_name) == 0)
                     {
                        // state_walker =1;
                         break;
                     }else
                     {
                        type_wallet++;
                     }
                }
                
                 /*wallet command execute*/
                 
                 command  = new generic_command_cli_jna(wallet_conn.get(type_wallet),client_manager.get(type_wallet).is_deprecated());
                 command.wallet_secondary_command(true);
                 
                 if(values[1].toLowerCase().compareTo("!help") == 0)
                 {
                     message_t_sent(help());
                     return;

                 }
                 else if(values[1].toLowerCase().compareTo("!balance") == 0) 
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
                 else if(values[1].toLowerCase().compareTo("!listaddrstr") == 0)
                 {
                     message_t_sent(wallet_name.get(type_wallet));

                     for(String aux_list : command.generic_get_address_by_account(current_id))
                     {
                         message_t_sent(aux_list);
                     }

                     return;
                 }
                 else if(values[1].toLowerCase().compareTo("!getnewaddrstr") == 0)
                 {
                     message_t_sent(wallet_name.get(type_wallet));
                     message_t_sent(command.generic_new_account_g(current_id));
                     return;
                 }
                 else if(values[1].toLowerCase().compareTo("!generateaddrqrcode") == 0)
                 {
                    if(values.length < 3)
                    {
                        message_t_sent(wallet_name.get(type_wallet) + " something is missing .... ");
                        return;
                    }



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
                 else if(values[1].toLowerCase().compareTo("!send") == 0) 
                 {      

                     if(values.length < 4)
                          return;

                     String result = command.generic_send_from_to_address(current_id, values[2], new BigDecimal(values[3]));
                     message_t_sent(wallet_name.get(type_wallet));
                     message_t_sent("txid result");
                     message_t_sent(result);
                     return;            
                 }        
                
            break;
        
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

    public void sendReplyKeyboard(long chatId, String message, String[][] keyboardText, boolean oneTimeKeyboard) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(); // return null;
        keyboard.setOneTimeKeyboard(oneTimeKeyboard);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        
        for (String[] rows : keyboardText) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (String row : rows) {
                keyboardRow.add(row);
            }
            keyboardRows.add(keyboardRow);
        }
        
        keyboard.setKeyboard(keyboardRows);
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.setReplyMarkup(keyboard);
        
        try {
         messager.execute(sendMessage); 
        } catch (TelegramApiException e) {
         // e.printStackTrace();
        }
        //return execute(sendMessage);
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
                   System.out.println(received);

                    for(userinfocontrol aux : user_control)
                    {
                        if(aux.get_user_id().compareTo(received.split(":")[1]) == 0)
                        {
                            current_id = received.split(":")[1];
                            selected = aux;
                            
                            if(bundle_manager != null)
                            {
                                if(received.split(":")[0].toUpperCase().compareTo(bundle_manager.getString("reset")) == 0)
                                {
                                     selected.set_wallet_hash("");
                                     selected.set_wallet("");
                                     selected.update_state(0); 
                                }
                                else if(received.split(":")[0].toLowerCase().compareTo("reset") == 0)
                                {
                                     selected.set_wallet_hash("");
                                     selected.set_wallet("");
                                     selected.update_state(0); 
                                }
                            }
                            else if(received.split(":")[0].toLowerCase().compareTo("reset") == 0)
                            {
                                 selected.set_wallet_hash("");
                                 selected.set_wallet("");
                                 selected.update_state(0); 
                            } 

                            message_handle(received.split(":")[0]);
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

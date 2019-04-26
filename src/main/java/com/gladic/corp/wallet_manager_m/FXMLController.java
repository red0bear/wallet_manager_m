package com.gladic.corp.wallet_manager_m;

import generic_command_pack.generic_cli;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javax.swing.SwingUtilities;
import telegram.telegramwalletmessagehandler;

public class FXMLController implements Initializable {
    
    /* WALLET MANAGER LOCAL CONTROL */
    private List<String> wallet = new ArrayList();
    private List<generic_cli> client_manager = new ArrayList();
    
    /**/
    private List<String> user_list = new ArrayList();
    
    /**/
    private List<String> wallet_name = new ArrayList();
    private List<URL> wallet_conn = new ArrayList();    
    
    /**/
    private generic_cli cli_worker;
      
    telegramwalletmessagehandler handler_msg;
    
    /**/
    private boolean block_h = true;
    private boolean block_a = true;
    private boolean block_s = true;
    private boolean block_c = true;
    private boolean block_label = true;
    
    private boolean block_b_start = false;
    
    /**/
    private boolean block_telegram_a = true;
    
    @FXML
    private Label disponible;
    
    @FXML
    private Label immature;
    
    @FXML
    private Label total;
    
    @FXML
    private Label totalaccount;
    
    @FXML
    private ImageView hashqrcode;
    
    @FXML
    private Label hashstr;
    
    /**/
    
    @FXML
    private TextField walletstart;
    
    @FXML
    private TextField rpcconnect;
    
    @FXML
    private TextField rpcuser;
    
    @FXML
    private TextField rpcpass;
    
    @FXML
    private TextField rpcport;
    
    @FXML
    private TextField valuetosend;
    
    @FXML
    private TextField hashtosend;
    
    @FXML
    private TextField newaccountname;

    @FXML
    private ComboBox<String> walletsel;
    
    @FXML
    private ComboBox<String> accountsel;
      
    @FXML
    private ComboBox<String> hashsel;
    
    
    /*TELEGRAM*/

    @FXML
    private TextField userid;
    
    @FXML
    private TextField nameid;
    
    @FXML
    private TextField botusername;
    
    @FXML
    private TextField bottokenname;
    
    @FXML
    private TextArea logarea;

    /**/
    
    @FXML
    private ComboBox<String> walletseldisallowrem;
    
    @FXML
    private ComboBox<String> userselect;
    
    @FXML
    private ComboBox<String> walletselallowrem;
        
    @FXML
    private Button deleteuser;
    
    @FXML
    private Button allow;
    
    
    @FXML
    private void HandleadduserAction(ActionEvent event)
    {
        handler_msg.add_user(userid.getText(), nameid.getText());
        
        user_list.add(userid.getText()+":"+nameid.getText());

        
        block_telegram_a = true;
        block_s = block_a = block_h = true;
                  
        userselect.setItems(observableArrayList(user_list));
        userselect.getSelectionModel().selectFirst();
        
        client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_new_account(newaccountname.getText());
            
        client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_wallet_info();
        disponible.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.balance);
        immature.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.unconfirmed_balance);
        total.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.immature_balance);            

        accountsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_list_accounts())); 
        accountsel.getSelectionModel().selectFirst();

        hashsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account(accountsel.getSelectionModel().getSelectedItem())));
        hashsel.getSelectionModel().selectFirst();

        totalaccount.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account().get(accountsel.getSelectionModel().getSelectedIndex()));

        hashstr.setText(hashsel.getSelectionModel().getSelectedItem());

        hashqrcode.setImage(SwingFXUtils.toFXImage(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_qr_code(hashsel.getSelectionModel().getSelectedItem()),null));

        block_s = block_a = block_h = false;
        block_telegram_a = false;
    }
    
    @FXML
    private void HandleuserselAction(ActionEvent event)
    {
        if(block_telegram_a)
        {}
        else
            walletseldisallowrem.setItems(observableArrayList(handler_msg.get_list_permissions(userselect.getSelectionModel().getSelectedIndex())));
    }
    
    @FXML
    private void HandledeleteuserAction(ActionEvent event){}

    @FXML
    private void HandleallowAction(ActionEvent event)
    {
        if(client_manager.isEmpty())
        {
        }
        else
        {
            if(block_telegram_a)
            {}
            else
            {
                handler_msg.update_permissions(userselect.getSelectionModel().getSelectedIndex(),walletselallowrem.getSelectionModel().getSelectedIndex(), walletselallowrem.getSelectionModel().getSelectedItem());
                walletseldisallowrem.setItems(observableArrayList(handler_msg.get_list_permissions(userselect.getSelectionModel().getSelectedIndex())));
            }
        }
    }
        
    @FXML
    private void HandledisallowAction(ActionEvent event)
    {    
        if(client_manager.isEmpty())
        {
        }
        else
            walletseldisallowrem.setItems(observableArrayList(handler_msg.rem_permissions(userselect.getSelectionModel().getSelectedIndex(),walletseldisallowrem.getSelectionModel().getSelectedIndex())));
    }
    
    @FXML
    private void HandlestartbotconfigAction(ActionEvent event)
    {
        handler_msg = new telegramwalletmessagehandler(botusername.getText(),bottokenname.getText(),logarea);
        handler_msg.set_url_wallet(wallet_name, wallet_conn);
        handler_msg.StartIpMachine();
    }
    
    /*WALLET MANAGER*/
    @FXML
    private void HandlerefreshvalueAction(ActionEvent event)
    {
        if(client_manager.isEmpty())
        {
        }
        else
            valuetosend.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account ().get(accountsel.getSelectionModel().getSelectedIndex()));
    }
     
    @FXML
    private void HandlenewaddrAction(ActionEvent event)
    {
        if(client_manager.isEmpty())
        {
        }
        else
        {
            block_label = block_s = block_a = block_h = true;
            
            client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_new_account(newaccountname.getText());
            
            client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_wallet_info();
            disponible.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.balance);
            immature.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.unconfirmed_balance);
            total.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.immature_balance);            

            accountsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_list_accounts())); 
            accountsel.getSelectionModel().selectFirst();

            hashsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account(accountsel.getSelectionModel().getSelectedItem())));
            hashsel.getSelectionModel().selectFirst();

            totalaccount.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account().get(accountsel.getSelectionModel().getSelectedIndex()));

            hashstr.setText(hashsel.getSelectionModel().getSelectedItem());

            hashqrcode.setImage(SwingFXUtils.toFXImage(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_qr_code(hashsel.getSelectionModel().getSelectedItem()),null));

            block_label = block_s = block_a = block_h = false;            
        }
    }
    
    @FXML
    private void handlebuttonsendaction(ActionEvent event)
    {
        if(hashtosend.getText().isEmpty())
        {
        }
        else
        {
            client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_send_from_to_address(accountsel.getSelectionModel().getSelectedItem(), hashtosend.getText(), new BigDecimal(valuetosend.getText())); 
        }
    }
    
    @FXML
    private void handleselectionwalletAction(ActionEvent event) 
    {
        System.out.println("You clicked me!");    
        
        if(!block_c)
        {
           block_label = block_s = block_a = block_h = true;

            client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_wallet_info();
            disponible.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.balance);
            immature.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.unconfirmed_balance);
            total.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.immature_balance);            

            accountsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_list_accounts())); 
            accountsel.getSelectionModel().selectFirst();

            hashsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account(accountsel.getSelectionModel().getSelectedItem())));
            hashsel.getSelectionModel().selectFirst();

            totalaccount.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account().get(accountsel.getSelectionModel().getSelectedIndex()));

            hashstr.setText(hashsel.getSelectionModel().getSelectedItem());

            hashqrcode.setImage(SwingFXUtils.toFXImage(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_qr_code(hashsel.getSelectionModel().getSelectedItem()),null));

            block_label = block_s = block_a = block_h = false;
        }
    }
    
    @FXML
    private void handleselectionaccountAction(ActionEvent event)
    {
        System.out.println("selection  event: " + event.getEventType().toString());
        
        if(!block_a) 
        {
            block_b_start = block_h = true;

            accountsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_list_accounts())); 

            hashsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account(accountsel.getSelectionModel().getSelectedItem())));
            hashsel.getSelectionModel().selectFirst();

            totalaccount.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account().get(accountsel.getSelectionModel().getSelectedIndex()));
            
            valuetosend.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account ().get(accountsel.getSelectionModel().getSelectedIndex()));
                       
            hashstr.setText(hashsel.getSelectionModel().getSelectedItem());
            
            BufferedImage aux= client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_qr_code(hashsel.getSelectionModel().getSelectedItem());
            hashqrcode.setImage(SwingFXUtils.toFXImage(aux,null));
            
            block_b_start = block_h = false;
        }
    }
    
    @FXML
    private void handleselectionhashAction(ActionEvent event)
    {           
        System.out.println("selection  event I: " + event.getEventType().toString());
        
        if(!block_h)
        {
                      
            block_a = true;
                      
            hashsel.getSelectionModel().select(hashsel.getSelectionModel().getSelectedItem());
            hashstr.setText(hashsel.getSelectionModel().getSelectedItem());
            hashqrcode.setImage(SwingFXUtils.toFXImage(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_qr_code(hashsel.getSelectionModel().getSelectedItem()),null));
            
            block_a = false;
        }
    }
    
    
    @FXML
    private void HandlestartbuttonAction(ActionEvent event)
    {    
        System.out.println("button event");       
        
      // if(!block_b_start)
      // {
           block_label = block_c = block_s = block_a = block_h = true;
           
           wallet.add(walletstart.getText());

           walletsel.setItems(observableArrayList(wallet));
           walletsel.getSelectionModel().selectFirst();
                      
           cli_worker = new generic_cli(rpcconnect.getText(),rpcuser.getText(),rpcpass.getText(),rpcport.getText());
           client_manager.add(cli_worker);
           
           wallet_name.add(walletstart.getText());
           wallet_conn.add(cli_worker.get_url());
           
           walletselallowrem.setItems(observableArrayList(wallet));
           walletselallowrem.getSelectionModel().selectFirst();
              
           client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_wallet_info();
           disponible.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.balance);
           immature.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.unconfirmed_balance);
           total.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.immature_balance);            
                       
           accountsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_list_accounts())); 
           accountsel.getSelectionModel().selectFirst();

           hashsel.setItems(observableArrayList(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account(accountsel.getSelectionModel().getSelectedItem())));
           hashsel.getSelectionModel().selectFirst();

           totalaccount.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account().get(accountsel.getSelectionModel().getSelectedIndex())); 
           valuetosend.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_address_by_account ().get(accountsel.getSelectionModel().getSelectedIndex()));
           
           hashstr.setText(hashsel.getSelectionModel().getSelectedItem());
            
           hashqrcode.setImage(SwingFXUtils.toFXImage(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_qr_code(hashsel.getSelectionModel().getSelectedItem()),null));

           block_label = block_c = block_s = block_a = block_h = false;  
           block_b_start = true;
      // }
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        disponible.setText("0.00000000");
        immature.setText("0.00000000");
        total.setText("0.00000000");
        
        
        
       // handleselectionwalletAction.getItems().removeAll(handleselectionwalletAction.getItems());
        walletsel.setItems(observableArrayList(wallet));
        walletselallowrem.setItems(observableArrayList(wallet));
        //walletsel.getSelectionModel().select("FLO"); 
        
        
                    Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                    if(block_s)
                                    {}
                                    else if(block_label)
                                    {}
                                    else 
                                    {
                                        client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).generic_get_wallet_info();
                                        disponible.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.balance);
                                        immature.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.unconfirmed_balance);
                                        total.setText(client_manager.get(walletsel.getSelectionModel().getSelectedIndex()).get_walletinfo.immature_balance);
                                    }
                            }
                        });
                    }
                }, 0, 2000);
        
    }    
      
}

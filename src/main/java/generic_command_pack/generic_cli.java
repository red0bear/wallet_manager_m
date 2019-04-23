/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generic_command_pack;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;


/**
 *
 * @author my name is nobody
 */
public class generic_cli {
    
    private String ip;
    private String user;
    private String pass;
    private String port;

    public getwalletinfo get_walletinfo = new getwalletinfo();  
            
    private URL url;
    private BitcoinJSONRPCClient bitcoinClient;
        
    private List<String> hashgot;
    private List<String> accounts;
    private List<String> values;

    private BufferedImage img;


    public generic_cli(URL url)
    {
        bitcoinClient = new BitcoinJSONRPCClient(url);
    }    
    
    public generic_cli(String user , String pass , String port)
    {
        this.ip = "127.0.0.1";
        this.user = user;
        this.pass = pass;
        this.port = port;

        try {
            url = new URL("http://" + user + ':' + pass + "@" + ip + ":" + port + "/");
        } catch (MalformedURLException ex) {
            Logger.getLogger(generic_cli.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        bitcoinClient = new BitcoinJSONRPCClient(url);
    }
    
    public generic_cli(String ip , String user , String pass , String port)
    {
        this.ip = ip;
        this.user = user;
        this.pass = pass;
        this.port = port;

        try {
            url = new URL("http://" + user + ':' + pass + "@" + ip + ":" + port + "/");
        } catch (MalformedURLException ex) {
            Logger.getLogger(generic_cli.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        bitcoinClient = new BitcoinJSONRPCClient(url);
    }
    
    
    public URL get_url()
    {
        return url;
    }
        
    public List<String> generic_list_accounts()
    { 
        if(accounts == null)
        {
            accounts = new ArrayList();
        }
        else
        {
            accounts.clear();
        }
        
        
        if(values == null)
        {
            values =  new ArrayList();
        }
        else
        {
            values.clear();
        }
        
        for (String key : bitcoinClient.listAccounts().keySet())
        {
            if(key.compareTo("") == 0)
            {
            
            }
            else
            {
                accounts.add(key);
                values.add(String.format ("%.8f", bitcoinClient.listAccounts().get(key).doubleValue()));
            }
        }
            
        return accounts;
    }
    
    /**/

    public List<String> generic_get_address_by_account (String myaccount)
    {      
        hashgot = bitcoinClient.getAddressesByAccount(myaccount);
                        
        return hashgot;
    }
    
    
    public String generic_send_from_to_address(String account,String toaddress,BigDecimal value)
    {
        return bitcoinClient.sendFrom(account, toaddress, value);
    }
    
    public String generic_send_to_address(String toaddress,BigDecimal value)
    {
        return bitcoinClient.sendToAddress(toaddress, value);
    }
    
    public void generic_new_account(String myaccount)
    {
        bitcoinClient.getNewAddress(myaccount);
    } 
    
    public String generic_new_account_g(String myaccount)
    {
        return bitcoinClient.getNewAddress(myaccount);
    }     
    
    public List<String> generic_get_address_by_account ()
    {                                             
        return values;
    }    
    
        
    public BufferedImage get_qr_code(String value)
    {     
        return get_qr_code_Image(value, 300 , 300); 
    }

    public void generic_get_wallet_info()
    {
        get_walletinfo.balance              = bitcoinClient.getWalletInfo().balance().toPlainString();
        get_walletinfo.immature_balance     = bitcoinClient.getWalletInfo().balance().add(bitcoinClient.getWalletInfo().unconfirmedBalance()).toPlainString();
        get_walletinfo.unconfirmed_balance  = bitcoinClient.getWalletInfo().unconfirmedBalance().toPlainString();
    }
    
    /*QR CODE*/
    private BufferedImage get_qr_code_Image(String text, int width, int height)  
    {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            // ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            // MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
              
            BufferedImage aux = MatrixToImageWriter.toBufferedImage(bitMatrix);
              
           return aux;
           // byte[] pngData = pngOutputStream.toByteArray();
           // return pngData;

        } catch (WriterException ex) {
            Logger.getLogger(generic_cli.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return null;
    }

}

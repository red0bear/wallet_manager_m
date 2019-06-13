/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generic_command_pack;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.json.simple.parser.JSONParser;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author my name is nobody
 */
public class generic_command_cli_jna {
    
    String command_issue;
    
    String command;

    String ip   ;
    String user ;
    String pass ;
    String port ;
              
    public getwalletinfo get_walletinfo = new getwalletinfo(); 
            
    String path_cli_exec = "bitcoin-cli ";
    String config_rpc = "-rpcconnect=" + ip + " -rpcuser=" + user +" -rpcpassword="+ pass + " -rpcport=" + port;    
    
    private List<String> accounts;
    private List<String> values;   
     
    public generic_command_cli_jna(String url)
    {
        this.command_issue = url;
    } 
     
    public generic_command_cli_jna(String path_cli_exec,
                                   String ip,
                                   String user,
                                   String pass,
                                   String port)
    {
        
        this.path_cli_exec = path_cli_exec;
        this.ip = ip;
        this.user = user;
        this.pass = pass; 
        this.port = port; 
        
        config_rpc = "-rpcconnect=" + ip + " -rpcuser=" + user +" -rpcpassword="+ pass + " -rpcport=" + port;    
        
        command_issue = path_cli_exec + " " + config_rpc + " ";
    }
    

    private String get_data_json(String key, String jsonstring)
    {
        JSONParser parser = new JSONParser();
        JSONObject obj = new JSONObject(jsonstring);
        String aux = String.valueOf(obj.get(key));
        return aux;
    }
    
    private List<Object> get_name_json(String jsonstring)
    {
        JSONParser parser = new JSONParser();
        JSONObject obj = new JSONObject(jsonstring);
        JSONArray names  = obj.names();        
        names.remove(0);
        
        return names.toList();
    }    
    
    
    private data_json_got get_data(String data, Class make)
    {
    
        try {
            JSONParser parser = new JSONParser();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            byte[] jsondata = data.getBytes();
            
            return data_json_got.class.cast(mapper.readValue(jsondata, make));
           
           //return mapper.readValue(jsondata, make);
        } catch (IOException ex) {
            Logger.getLogger(generic_command_cli_jna.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    
    
    /******************************/
    /**                         **/
    /*****************************/
    
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
            Native.load((Platform.isLinux()? "/command_cli/clientcmd.so" : "c"),
                                CLibrary.class);

       public Pointer cmdtst(String val);
    }
    
    public String get_url()
    {
        return path_cli_exec + " " + config_rpc + " ";
    }
        
    public void generic_get_wallet_info()
    {
      
       String info = CLibrary.INSTANCE.cmdtst(command_issue + " getwalletinfo").getString(0);

       BigDecimal balance = new BigDecimal(get_data_json("balance", info)).setScale(8);
       BigDecimal immature_balance = new BigDecimal(get_data_json("immature_balance", info)).setScale(8);
       BigDecimal uncorfirmed_balance = new BigDecimal(get_data_json("unconfirmed_balance", info)).setScale(8);
       
       get_walletinfo.balance              = balance.toPlainString();
       get_walletinfo.immature_balance     = immature_balance.add(uncorfirmed_balance).toPlainString();
       get_walletinfo.unconfirmed_balance  = uncorfirmed_balance.toPlainString();
       
    }
    
    public List<String> generic_list_accounts()
    {
       String info = CLibrary.INSTANCE.cmdtst(command_issue + " listaccounts").getString(0);
       //System.out.println(s);
       
       //  
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
       
       //
       for(Object aux : get_name_json(info))
       {
           accounts.add((String) aux);
           values.add(new BigDecimal(get_data_json((String) aux, info)).setScale(8).toPlainString());
       }
       
       return accounts;
    }
    
    
    public List<String> generic_get_address_by_account (String myaccount)
    {
        String info = CLibrary.INSTANCE.cmdtst(command_issue + " getaddressesbyaccount"  + " \"" +myaccount + "\"").getString(0);
        
        List<String> listdata = new ArrayList();
        
        JSONArray values_addr = new JSONArray(info);
        
        if (values_addr != null) 
        { 
            for (int i=0;i<values_addr.length();i++){ 
             listdata.add(values_addr.getString(i));
            } 
        }else
        {
            return null;
        }
        
        return listdata;
    }
    
    public String generic_send_from_to_address(String account,String toaddress,BigDecimal value)
    {
       String info = CLibrary.INSTANCE.cmdtst(command_issue + " sendfrom " + " \"" + account + "\"" + " \"" + toaddress + "\" " + value.toPlainString()).getString(0);
        
       return info;
    }
    
    public String generic_send_to_address(String toaddress,BigDecimal value)
    {       
        String info = CLibrary.INSTANCE.cmdtst(command_issue + " sendtoaddress " + " " + toaddress + " " + value.toPlainString()).getString(0);
        
        return  info;
    }
    
     
    public void generic_new_account(String myaccount)
    {       
        String info = CLibrary.INSTANCE.cmdtst(command_issue  + " getnewaddress " + myaccount).getString(0);
    }
    
    public String generic_new_account_g(String myaccount)
    {       
        String info = CLibrary.INSTANCE.cmdtst(command_issue+ " getnewaddress " + myaccount).getString(0);
        
        return info;
    } 
    
    public List<String> generic_get_address_by_account ()
    {                                             
        return values;
    }
    
    
    public BufferedImage get_qr_code(String value)
    {     
        return get_qr_code_Image(value, 300 , 300); 
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
            Logger.getLogger(generic_command_cli_jna.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return null;
    }
    
}

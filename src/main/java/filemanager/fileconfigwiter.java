/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author my name is nobody
 */
public class fileconfigwiter {
    
    private List<String> lines_r = new ArrayList();
    private String r_file;
    
    public fileconfigwiter(){}
    
    
    public void write_config(String pathcli,String walletname,String rpcip,String rpcuser,String rpcpass,String rpcport)
    {
        lines_r.clear();
        
        try {

            RandomAccessFile writer = new RandomAccessFile("config_wallet.cfg", "rw");
            String value = pathcli+":"+walletname+":"+rpcip+":"+rpcuser+":"+rpcpass+":"+rpcport+"\n";
            
            
            if(writer.length() > 0)
            {
                while((r_file=writer.readLine()) != null)
                {
                   lines_r.add(r_file);
                }
                
                writer.seek(0);
                
                
                for(String aux : lines_r)
                {
                    writer.write((aux+"\n").getBytes());
                }
                
                writer.write(value.getBytes());
                
                writer.close();
            }else
            {                       
                writer.write(value.getBytes());
                
                writer.close();
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(fileconfigwiter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(fileconfigwiter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int read_config()
    {
        lines_r.clear();
        
        try {
            RandomAccessFile writer = new RandomAccessFile("config_wallet.cfg", "rw");
            
            if(writer.length() > 0)
            {
                while((r_file=writer.readLine()) != null)
                {
                   lines_r.add(r_file);
                }
            }
            else
               return 0;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(fileconfigwiter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(fileconfigwiter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 1;
    }
    
    
    public List<String> get_wallet_config()
    {
        return lines_r;
    }
    
}

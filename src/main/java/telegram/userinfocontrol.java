/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author my name is nobody
 */
public class userinfocontrol {
    
    private String id_user;
    private String name_user;
    
    /**/
    private int state     = 0;
    
    private String wallet_selected;
    private String wallet_hash_select;
    
    private String hash_to_send;
    
    private List<String> wallet_allow;
            
    private String wallet [][];
    
            
    public userinfocontrol(String id_user,String name_user)
    {
        this.id_user = id_user;
        this.name_user = name_user;
        
        wallet_allow = new ArrayList();
    }
    
    /*************/
    
    public void update_state(int value)
    {
        this.state = value;
    }
    
    public int get_state()
    {
        return state;
    }
        
    /***************/
    
    public void set_wallet(String value)
    {
        wallet_selected = value;
    }
    
    public String get_wallet()
    {
        return wallet_selected;
    }
    
    /****************/
    
    public void set_wallet_hash(String value)
    {
        this.wallet_hash_select = value;
    }
    
    public String get_wallet_hash()
    {
        return this.wallet_hash_select;
    }

    /*****************/
    
    public void set_hash_to_send(String value)
    {
        this.wallet_hash_select = value;
    }
    
    public String get_hash_to_send()
    {
        return this.wallet_hash_select;
    }
    
    
    /****************/
    
    public String get_user_id()
    {
        return id_user;
    }
    
    public String get_name_user()
    {
        return name_user;
    }
    
    public List<String> list_perm()
    {
        return this.wallet_allow;
    }
        
    public void add_permission(String wallet)
    {
        this.wallet_allow.add(wallet);
    }
    
    public void rem_permission(int value)
    {
        if(wallet_allow.isEmpty())
        {
        
        }
        else if(value <= wallet_allow.size())
            this.wallet_allow.remove(value);
    }
    
    public String [][] get_wallet_allowed()
    {
        int counter = 0;
        
        wallet = new String [wallet_allow.size()][];   
           
        for(String aux :  wallet_allow)
        {
           wallet[counter] = new String []{ aux };
           counter++;
        }
          
        return wallet;    
    }
    
    public String [][] get_List_allowed(List<String> values)
    {
        int counter = 0;
        
       String [][] wallet_l = new String [values.size()][];   
           
        for(String aux :  values)
        {
           wallet_l[counter] = new String []{ aux };
           counter++;
        }
          
        return wallet_l;    
    }    
    
}

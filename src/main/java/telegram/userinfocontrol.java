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
    
    private List<String> wallet_allow;
            
            
    public userinfocontrol(String id_user,String name_user)
    {
        this.id_user = id_user;
        this.name_user = name_user;
        
        wallet_allow = new ArrayList();
    }
    
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generic_command_pack;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author my name is nobody
 */
public class getwalletinfo {
        
    @SuppressWarnings("unchecked")
    @JsonProperty("walletname")
    public String walletname;
    
    @SuppressWarnings("unchecked")
    @JsonProperty("walletversion")
    public String walletversion;
    
    @SuppressWarnings("unchecked")
    @JsonProperty("balance")
    public String balance;   
    
    @SuppressWarnings("unchecked")
    @JsonProperty("unconfirmed_balance")
    public String unconfirmed_balance;
    
    @SuppressWarnings("unchecked")
    @JsonProperty("immature_balance")
    public String immature_balance; 
    
    @SuppressWarnings("unchecked")
    @JsonProperty("txcount")
    public String txcount;
    
    @SuppressWarnings("unchecked")
    @JsonProperty("keypoololdest")
    public String keypoololdest;

    @SuppressWarnings("unchecked")
    @JsonProperty("keypoolsize")
    public String keypoolsize;   
    
    @SuppressWarnings("unchecked")
    @JsonProperty("keypoolsize_hd_internal")
    public String keypoolsize_hd_internal;
    
    @SuppressWarnings("unchecked")
    @JsonProperty("paytxfee")
    public String paytxfee;

    @SuppressWarnings("unchecked")
    @JsonProperty("hdmasterkeyid")
    public String hdmasterkeyid;    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generic_command_pack;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author my name is nobody
 */
public class data_json_got {

    @SuppressWarnings("unchecked")
    List<String> keyused;
    List<String> valuestored;
    
    public data_json_got()
    {
        keyused = new ArrayList();
        valuestored = new ArrayList();
    }
    
    @JsonAnySetter
    public void handleUnknownProperty(String key, String value) {
        //System.out.printf("JSON property: %s: %s", key, value);
        keyused.add(key);
        valuestored.add(value);
    }
    
}

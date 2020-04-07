/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andromda.cartridges.springws;

import java.util.List;

/**
 *
 * @author junior
 */
public class SpringWSHelper {
    
    public static String getRequestType(String requestType) {
        
        String[] splits = requestType.split("\\.");        
        String rt = splits[splits.length-1];
        
        if(rt == null) {
            rt = "POST";
        }
        
        if(rt.equals("GET")) {
            return "@org.springframework.web.bind.annotation.GetMapping";
        } else if(rt.equals("DELETE")) {
            return "@org.springframework.web.bind.annotation.DeleteMapping";
        } else if(rt.equals("PATCH")) {
            return "@org.springframework.web.bind.annotation.PatchMapping";
        } else if(rt.equals("PUT")) {
            return "@org.springframework.web.bind.annotation.PutMapping";
        } else {
            return "@org.springframework.web.bind.annotation.PostMapping";
        }
        
    }
    
    /*
    public static String getOperationParameters(WebServiceOperationLogic operation) {
        
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for(WebServiceParameterLogic arg : operation.getArguments()) {
            
            if(i > 0) {
                builder.append(", ");
            }
            
            builder.append(arg.getType().getFullyQualiffiedName());
            builder.append(" ");
            builder.append(arg.getName());
            
            i++;
        }
        
        return builder.toString();
    }
	*/
}

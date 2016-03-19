package com.hupu.ad.util;

import java.util.Map;
import java.util.Random;

public class RandomEngineUtils {
	 /**
	  *  概率选择
	  * 
	  * @param keyChanceMap key为唯一标识，value为该标识的概率，是去掉%的数字 
	  * @return
	  */
     public static String chanceSelect(Map<String, Integer> keyChanceMap) {  
          if(keyChanceMap == null || keyChanceMap.size() == 0)  
               return null;  
            
          Integer sum = 0;  
          for (Integer value : keyChanceMap.values()) {  
               sum += value;  
          }  
          // 从1开始  
          Integer rand = new Random().nextInt(sum) + 1;  
            
          for (Map.Entry<String, Integer> entry : keyChanceMap.entrySet()) {  
               rand -= entry.getValue();  
               // 选中  
               if(rand <= 0) {  
                    return entry.getKey();  
               }  
          }  
          return null;  
     }  
     
}


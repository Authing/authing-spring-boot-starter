package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResponseVO<T> {
    
    private Integer code;
    private String message;
    private T data;
    
    /**
     * 成功响应的方法
     * @param <T>
     * @return
     */
    public static <T> ResponseVO success(){
        return new ResponseVO().setCode(200).setMessage("success");
    }
    
    /**
     * 失败：自定义失败 错误码和提示信息
     * @param code
     * @param message
     * @return
     */
    public static ResponseVO fail(int code,String message){
        return new ResponseVO().setCode(code).setMessage(message);
    }
}

package com.lanchong.exception;

import com.lanchong.result.CodeMsg;

/**
 * @program: SeckillProject
 * @description: 异常封装
 **/
public class GlobalException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }
    public CodeMsg getCm() {
        return cm;
    }
}

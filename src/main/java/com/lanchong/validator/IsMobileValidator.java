package com.lanchong.validator;

import com.lanchong.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: SeckillProject
 * @description: 手机格式校验
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;

    public void initialize(IsMobile constraintAnnotation) {

        required = constraintAnnotation.required();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required) {
            return ValidatorUtil.isMobile(value);
        }else {
            if(StringUtils.isEmpty(value)) {
                return true;
            }else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }

}

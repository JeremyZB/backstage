package com.bobo.core.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.bobo.framework.service.BaseService;
import com.bobo.framework.utils.JpException;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author lzw
 * @date 2018-06-22 10:50
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws ServiceException  校验不通过，则报ServiceException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws JpException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage()).append(",");
            }
            String message=msg.toString();
            int length=message.length();
            if(length>1)
            throw new JpException(BaseService.fail, msg.toString().substring(0,msg.toString().length()-1));
        }
    }
    
  
    
    
}

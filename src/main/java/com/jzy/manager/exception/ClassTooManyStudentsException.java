package com.jzy.manager.exception;

/**
 * @author JinZhiyun
 * @version 1.0
 * @ClassName ClassTooManyStudentsException
 * @description 班上学生人数过多的异常
 * @date 2019/11/1 18:20
 **/
public class ClassTooManyStudentsException extends Exception {
    private static final long serialVersionUID = -4216769939804067755L;

    public ClassTooManyStudentsException() {
    }

    public ClassTooManyStudentsException(String message) {
        super(message);
    }
}

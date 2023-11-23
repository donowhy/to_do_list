package todoList.soloProject.global.error.exception;

import lombok.Getter;
import todoList.soloProject.global.error.code.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
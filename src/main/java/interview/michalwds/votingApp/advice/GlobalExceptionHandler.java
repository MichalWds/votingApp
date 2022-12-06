package interview.michalwds.votingApp.advice;

import interview.michalwds.votingApp.exception.RoleException;
import interview.michalwds.votingApp.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(UserException ex, WebRequest request){
        return new ResponseEntity<>("Not found any user. Cause: " + ex.getCause(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<Object> handleRoleException(RoleException ex){
        return new ResponseEntity<>(ex + " Incorrect role.", HttpStatus.NOT_FOUND);
    }
}

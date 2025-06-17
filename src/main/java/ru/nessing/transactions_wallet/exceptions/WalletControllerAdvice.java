package ru.nessing.transactions_wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Objects;

@RestControllerAdvice
/** Обработка исключений **/
public class WalletControllerAdvice {

    /** Обработка исключения корректного JSON **/
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        String message = "Некорректный формат запроса: " + Objects.requireNonNull(ex.getRootCause()).getMessage();
        ErrorResponse response = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), message
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    /** Обработка исключения валидности переданных данных (WalletDto.class) **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();

        ErrorResponse response = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), message
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /** Обработка исключения на одновременное изменение баланса у одного кошелька (Оптимистическая блокировка) **/
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleConcurrentUpdate() {
        String message = "Операция не была выполнена. Попробуйте еще раз";
        ErrorResponse response = createErrorResponse(
                HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(), message
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /** Обработка исключения на наличие кошелька **/
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(WalletNotFoundException ex) {
        ErrorResponse response = createErrorResponse(
                HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /** Обработка исключения на доступное количество средств **/
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException ex) {
        ErrorResponse response = createErrorResponse(
                HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(), ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /** @param statusValue код ошибки
     * @param statusName название ошибки
     * @param message описание ошибки **/
    private ErrorResponse createErrorResponse(int statusValue, String statusName, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Instant.now().toString());
        response.setStatus(statusValue);
        response.setError(statusName);
        response.setMessage(message);
        return response;
    }
}

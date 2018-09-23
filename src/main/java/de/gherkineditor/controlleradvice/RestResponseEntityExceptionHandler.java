package de.gherkineditor.controlleradvice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({RepositoryConstraintViolationException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        RepositoryConstraintViolationException nevEx =
                (RepositoryConstraintViolationException) ex;


        List<ApiGlobalError> globaelErrors = nevEx
                .getErrors()
                .getGlobalErrors()
                .stream()
                .map(error -> new ApiGlobalError(
                        error.getCodes(),
                        messageSource.getMessage(error , LocaleContextHolder.getLocale()))
                ).collect(Collectors.toList());

        List<ApiFieldError> fieldErrors = nevEx
                .getErrors()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiFieldError(
                        error.getField(),
                        error.getCodes(),
                        messageSource.getMessage(error , LocaleContextHolder.getLocale()))
                ).collect(Collectors.toList());

        ApiErrorsView apiErrorsView = new ApiErrorsView(nevEx.getLocalizedMessage(),globaelErrors, fieldErrors);


        return new ResponseEntity<>(apiErrorsView, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    class ApiErrorsView {
        private String message;
        private List<ApiGlobalError> globalErrors;
        private List<ApiFieldError> fieldErrors;


        public ApiErrorsView(String message, List<ApiGlobalError> globalErrors, List<ApiFieldError> fieldErrors) {
            this.message = message;
            this.globalErrors = globalErrors;
            this.fieldErrors = fieldErrors;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ApiGlobalError> getGlobalErrors() {
            return globalErrors;
        }

        public void setGlobalErrors(List<ApiGlobalError> globalErrors) {
            this.globalErrors = globalErrors;
        }

        public List<ApiFieldError> getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(List<ApiFieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }
    }
    class ApiGlobalError {
        private String[] codes;
        private String message;

        public ApiGlobalError(String[] codes, String message) {
            this.codes = codes;
            this.message = message;
        }

        public String[] getCodes() {
            return codes;
        }

        public void setCodes(String[] codes) {
            this.codes = codes;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    class ApiFieldError {
        private String field;
        private String[] codes;
        private String message;

        public ApiFieldError(String field, String[] codes, String message) {
            this.field = field;
            this.codes = codes;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String[] getCodes() {
            return codes;
        }

        public void setCode(String[] codes) {
            this.codes = codes;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
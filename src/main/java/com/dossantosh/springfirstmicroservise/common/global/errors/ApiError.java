package com.dossantosh.springfirstmicroservise.common.global.errors;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int status;               // Código HTTP (404, 400, 500, etc.)
    private String error;             // Tipo de error (Not Found, Bad Request, etc.)
    private String message;           // Mensaje detallado
    private String path;              // URL donde ocurrió el error
    private List<String> details;     // Errores específicos (opcional)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;  // Fecha y hora del error

    public ApiError(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}

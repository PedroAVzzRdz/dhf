package com.example.dulcehorno.utils;

import com.google.gson.Gson;


public class ErrorHandler {

    // Clase interna para mapear JSON de error
    private static class ErrorResponse {
        String message;
    }

    /**
     * Obtiene el mensaje de error desde un Response de OkHttp
     * @return mensaje de error o mensaje por defecto si no se puede extraer
     */
    public static String getErrorMessage(String responseBody) {
        try {
            if (responseBody != null && !responseBody.isEmpty()) {
                ErrorResponse errorResponse = new Gson().fromJson(responseBody, ErrorResponse.class);
                if (errorResponse != null && errorResponse.message != null) {
                    return errorResponse.message;
                }
            }
        } catch (com.google.gson.JsonSyntaxException e) {
            e.printStackTrace();
        }
        return "Ocurrió un error desconocido";
    }

}

//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.exception;

//=================================================================================================
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String message) {
        super(message);
    }
}

package com.alphasystem.docbook.model;

public class NotImplementedException extends IllegalArgumentException {

    public NotImplementedException(Object parent, Object child) {
        super(String.format("Type \"%s\" is not implemented yet in \"%s\".", child.getClass().getName(), parent.getClass().getName()));
    }
}

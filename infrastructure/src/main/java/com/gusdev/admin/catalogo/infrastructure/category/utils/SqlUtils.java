package com.gusdev.admin.catalogo.infrastructure.category.utils;

public final class SqlUtils {

    private SqlUtils() {}

    public static String upper(final String term){
        if (term == null) return  null;
        return term.toUpperCase();
    }

    public static String like(final String term){
        if (term == null)
            return null;
        return "%" + term + "%";
    }
}
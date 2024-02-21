package com.gusdev.admin.catalogo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("all")
public final class SpecificationUtils {

    private SpecificationUtils() {}

    public static <T> Specification<T> like(final String prop, final String term) {
        final var termToUpperCase = SqlUtils.upper(term);

        Specification<T> specification = (root, query, cb) -> cb.like(cb.upper(root.get(prop)), SqlUtils.like(termToUpperCase));
        return specification;
    }
}

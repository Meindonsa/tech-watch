package com.meindonsa.tech_watch.shared;

import com.meindonsa.toolbox.utils.Functions;
import com.meindonsa.toolbox.view.PageInfo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Utils {
    private Utils() {}

    private static final int DEFAULT_PAGE_SIZE = 10;

    public static Pageable getPaging(PageInfo pageInfo) {
        return getPaging(pageInfo.getIndex(), pageInfo.getSize(), false);
    }

    public static Pageable getPaging(int index, int size) {
        return PageRequest.of(
                index, size == 0 ? DEFAULT_PAGE_SIZE : size, Sort.by("id").ascending());
    }

    public static Pageable getPaging(int index, int size, boolean isAscending) {
        if (isAscending) {
            return PageRequest.of(
                    index, size == 0 ? DEFAULT_PAGE_SIZE : size, Sort.by("id").ascending());
        }

        return PageRequest.of(
                index, size == 0 ? DEFAULT_PAGE_SIZE : size, Sort.by("id").descending());
    }

    public static String getString(String value) {
        return Functions.isNullOrEmpty(value) ? null : value.trim();
    }
}

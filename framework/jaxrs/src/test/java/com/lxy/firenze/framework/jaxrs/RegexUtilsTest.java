package com.lxy.firenze.framework.jaxrs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RegexUtilsTest {

    @Test
    public void should_match_empty_path_when_path_value_is_empty() {
        String matched = RegexUtils.match("", "");

        assertEquals(matched, "");
    }

    @Test
    public void should_match_by_prefix_when_path_value_not_contains_variable() {
        String matched = RegexUtils.match("/students/1/scores/1", "/students");

        assertEquals(matched, "/students");
    }

    @Test
    public void should_not_match_by_prefix_when_path_value_not_contains_variable() {
        String matched = RegexUtils.match("/students/1/scores/1", "/students/scores");

        assertNull(matched);
    }

    @Test
    public void should_match_multi_variable_when_path_value_contains_variables() {
        String matched = RegexUtils.match("/students/1/scores/1", "/students/{id}/scores/{scoreId}");

        assertEquals(matched, "/students/1/scores/1");
    }

    @Test
    @Disabled
    public void should_match_variable_when_path_value_contains_variable() {
        String matched = RegexUtils.match("/students/1/scores/1", "/students/{id}");

        assertEquals(matched, "/students/1");
    }
}

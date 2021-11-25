package com.lxy.firenze.framework.jaxrs;


import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    private static String pathVariablePattern = "\\{([^}]*)\\}";

    private static String pathValuePattern = "([^/]*)";

    public static String match(String url, String path) {
        if (Strings.isNullOrEmpty(url) && Strings.isNullOrEmpty(path)) {
            return "";
        }
        if (url.startsWith(path) && !Strings.isNullOrEmpty(path)) {
            return path;
        }
        String[] splitPatterns = path.split("/");
        String[] splitMatches = url.split("/");
        if (splitPatterns.length == splitMatches.length) {
            String regex = path.replaceAll(pathVariablePattern, pathValuePattern);
            Pattern compile = Pattern.compile(regex);
            Matcher matcher = compile.matcher(url);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return null;
    }

    public static Map<String, String> pathVariableMap(String matchedPath, String path) {
        String[] splitPatterns = path.split("/");
        String[] splitMatches = matchedPath.split("/");
        Map<String, String> pathVariableMap = new HashMap<>();
        for (int i = 0; i < splitPatterns.length; i++) {
            if (splitPatterns[i].matches(pathVariablePattern)) {
                String name = splitPatterns[i].substring(1, splitPatterns[i].length() - 1);
                pathVariableMap.put(name, splitMatches[i]);
            }
        }
        return pathVariableMap;
    }

    public static void main(String[] args) {
        System.out.println(match("/students/1/scores/1", "/students/{id}"));
        System.out.println(match("/students/1/scores/1", "/students/{id}/scores/{scoreId}"));
        System.out.println(match("/students/1/scores/1", "/students/scores"));
        System.out.println(match("/students/1/scores/1", "/students"));
        Map<String, String> a = pathVariableMap("/students/1/scores/1", "/students/{id}/scores/{scoreId}");
    }
}

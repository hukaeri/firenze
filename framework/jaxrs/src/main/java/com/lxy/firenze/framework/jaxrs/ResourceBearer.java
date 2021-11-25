package com.lxy.firenze.framework.jaxrs;

import java.lang.annotation.Annotation;
import java.util.List;

public class ResourceBearer<T> {

    protected List<ResourceBearer> subResources;

    protected T data;

    protected String path;

    protected String matchedPath;

    public ResourceBearer(T data, String path) {
        this.data = data;
        this.path = path;
    }

    public String getMatchedPath() {
        return matchedPath;
    }

    public ResourceMethodBearer lookup(String url, Class<? extends Annotation> methodAnnotation) {
        if (subResources == null) {
            return null;
        }
        for (ResourceBearer subResource : subResources) {
            if (subResource.match(url, methodAnnotation)) {
                if (subResource instanceof ResourceMethodBearer) {
                    return (ResourceMethodBearer) subResource;
                } else {
                    return subResource.lookup(subUrl(url, subResource.getMatchedPath()), methodAnnotation);
                }
            }
        }
        return null;
    }

    private String subUrl(String url, String matchedPath) {
        return url.substring(url.indexOf(matchedPath) + matchedPath.length());
    }

    protected boolean match(String url, Class<? extends Annotation> methodAnnotation) {
        String matchedPath = RegexUtils.match(url, this.path);
        if (matchedPath != null) {
            this.matchedPath = matchedPath;
            return true;
        }
        return false;
    }

    public void register() {

    }


    public void clearCache() {
        this.matchedPath = null;
        if (subResources != null) {
            for (ResourceBearer subResource : subResources) {
                subResource.clearCache();
            }
        }
    }
}

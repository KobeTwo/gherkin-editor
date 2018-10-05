package de.gherkineditor.util;

public class Util {

    public static final String SLASH = "/";

    public static String getConcatenatedPath(String path, String fileName) {
        String concatenatedPath = null;
        if (path.endsWith(SLASH)) {
            concatenatedPath = path.concat(fileName);
        } else {
            concatenatedPath = path.concat("/").concat(fileName);
        }
        return concatenatedPath;
    }

    public static String getSplittedPath(String path) {
        if (path.endsWith(SLASH)) {
            path = path.substring(0, path.lastIndexOf(SLASH));
        }
        int indexOfLastSlash = path.lastIndexOf(SLASH);
        return path.substring(0, indexOfLastSlash + 1);

    }

    public static String getSplittedFileName(String path) {
        if (path.endsWith(SLASH)) {
            path = path.substring(0, path.lastIndexOf(SLASH));
        }
        int indexOfLastSlash = path.lastIndexOf(SLASH);
        return path.substring(indexOfLastSlash + 1);

    }
}

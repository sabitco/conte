package com.quasarbi.document;

/**
 *
 * @author J4M0
 */
public enum DocType {

    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "docx"),
    PDF("application/pdf",
            "pdf");
    ;

    private final String mime;
    private final String extension;

    DocType(String mime, String extension) {
        this.mime = mime;
        this.extension = extension;
    }

    public String getMime() {
        return mime;
    }

    public String getExtension() {
        return extension;
    }
}

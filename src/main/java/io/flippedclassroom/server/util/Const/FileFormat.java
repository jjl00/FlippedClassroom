package io.flippedclassroom.server.util.Const;


import lombok.Getter;

public enum FileFormat {
    PICTURE("picture"),
    VIDEO("video"),
    AUDIO("audio"),
    DOCUMENT("document");
    @Getter
    String format;

    FileFormat(String format) {
        this.format = format;
    }
}

package io.flippedclassroom.server.util.Const;




public enum ChoiceQuestionType {
    SINGLE_CHOICE_QUESTION(1,"单选题"),MULTIPLE_CHOICE_QUESTION(2,"多选题");
     int index;
     String name;

    ChoiceQuestionType(int index, String name) {
        this.index = index;
        this.name = name;
    }

}

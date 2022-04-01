package org.example.databench.executor.script;

public interface Constants {
    String AT = "@";
    String COLON = ":";
    String SEMI_COLON = ";";
    String HORIZONTAL_BAR = "-";
    String UNDERLINE = "_";
    String COMMA = ",";
    String EMPTY = "";
    String SPACE = " ";
    String LEFT_SLASH = "/";
    String RIGHT_SLASH = "\\";
    String LINE_FEED = "\n";
    String POINTER = ".";

    interface Command {
        String SH = "sh";
        String SCP = "scp";
        String SSH = "ssh";
        String MKDIR = "mkdir";
        String CD = "cd";
        String SOURCE = "source";
        String SUDO = "sudo";
        String CURL = "curl";
    }
}

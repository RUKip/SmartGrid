package com.rug.energygrid.parser;

import com.rug.energygrid.UI.SettingsParser;

public class Main {

    public static void main(String[] args){
        Initializer init = new Initializer();
        new SettingsParser(init);
    }
}

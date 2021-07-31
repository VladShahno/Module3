package ua.com.alevel;

import ua.com.alevel.controller.ApplicationController;

public class Main {


    public static void main(String[] args) {

        String[] prop = new String[3];
        for (int i = 0; i < 3; i++)
            prop[i] = args[i];

        ApplicationController.runUserInterface(prop);

    }

}

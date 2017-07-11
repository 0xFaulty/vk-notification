package com.defaulty;

import com.defaulty.notivk.MainController;

import java.util.Timer;

/**
 * @author 0xFaulty
 * @version 0.4
 * @created June 2017.
 */

public class Main {

    public static void main(String[] args) throws Exception {

        MainController mainController = new MainController();
        Timer controllerTimer = new Timer(true);
        controllerTimer.scheduleAtFixedRate(mainController, 0, 15 * 1000);

    }

}
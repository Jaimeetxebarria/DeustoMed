package org.deustomed;

import com.formdev.flatlaf.FlatLightLaf;
import org.deustomed.ui.WindowLogin;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        WindowLogin windowLogin = new WindowLogin();
        windowLogin.setSize(300, 300);
        windowLogin.setVisible(true);
    }
}
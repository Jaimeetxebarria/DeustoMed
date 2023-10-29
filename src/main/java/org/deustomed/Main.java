package org.deustomed;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import org.deustomed.ui.WindowLogin;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();
        WindowLogin windowLogin = new WindowLogin();
        windowLogin.setVisible(true);
        windowLogin.setSize(300,350);
    }
}
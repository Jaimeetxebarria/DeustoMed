package org.deustomed.ui;

import org.deustomed.authentication.UserAuthenticationService;

import javax.swing.*;
import java.awt.event.WindowAdapter;

/**
 * Used to log out from the UserAuthenticationService when the window is closed
 */
public class UserAuthenticatedWindow extends JFrame {
    public UserAuthenticatedWindow(UserAuthenticationService userAuthenticationService) {
        if (userAuthenticationService == null) return;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    userAuthenticationService.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

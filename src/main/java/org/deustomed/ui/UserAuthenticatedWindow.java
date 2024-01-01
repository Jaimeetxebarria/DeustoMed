package org.deustomed.ui;

import org.deustomed.authentication.UserAuthenticationService;

import javax.swing.*;
import java.awt.event.WindowAdapter;

/**
 * Used to log out from the UserAuthenticationService when the window is closed
 */
public class UserAuthenticatedWindow extends JFrame {
    UserAuthenticationService userAuthenticationService;
    public UserAuthenticatedWindow(UserAuthenticationService userAuthenticationService) {
        if (userAuthenticationService == null) return;
        this.userAuthenticationService = userAuthenticationService;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                logOut();
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logOut();
            }
        });
    }

    private void logOut() {
        try {
            userAuthenticationService.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

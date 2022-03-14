package SplineGenerator.GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * A class for reading values from the keyboard
 */
public final class KeyBoardListener implements KeyEventDispatcher {

    /**
     * The list of key codes, this corresponds to the keyValues ArrayList
     */
    public static ArrayList<Integer> keyCodes;

    /**
     * The list of key values, this corresponds to the keyCodes ArrayList
     */
    public static ArrayList<Boolean> keyValues;

    /**
     * A private default constructor so that this class may never be instantiated
     */
    private KeyBoardListener() {

    }

    /**
     * A simple method for initializing the listener, this must be called before calling other methods
     */
    public static void initialize() {
        keyCodes = new ArrayList<>();
        keyValues = new ArrayList<>();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyBoardListener());
    }

    /**
     * A method for dispatching the KeyEvent
     *
     * @param e The KeyEvent to be dispatched
     * @return false so that the KeyEvent will be sent to other listeners as well
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (keyCodes.contains(e.getKeyCode())) {
            setValue(e);
        } else {
            keyCodes.add(e.getKeyCode());
            keyValues.add(true);
        }

        return false;
    }

    /**
     * A method for setting the value of a key
     *
     * @param e The KeyEvent containing the info to set the key value
     */
    private static void setValue(KeyEvent e) {
        int index = indexOf(e.getKeyCode());
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            keyValues.set(index, true);
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            keyValues.set(index, false);
        }
    }

    /**
     * A method to get the index of the keyCode in the keyCodes ArrayList
     *
     * @param keyCode The keyCode to be found
     * @return The index of the keyCode value in teh keyCodes ArrayList
     */
    private static int indexOf(int keyCode) {
        for (int i = 0; i < keyCodes.size(); i++) {
            if (keyCodes.get(i) == keyCode) {
                return i;
            }
        }

        return -1;
    }

    /**
     * A method to get the value of a key
     *
     * @param keyCode The keyCode of the key to be read
     * @return The value of the key specified by keyCode
     */
    public static boolean get(int keyCode) {
        if (keyCodes.contains(keyCode)) {
            return keyValues.get(indexOf(keyCode));
        } else {
            return false;
        }
    }

}

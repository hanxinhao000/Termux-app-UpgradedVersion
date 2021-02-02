package main.java.com.termux.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.termux.R;
import com.termux.terminal.TerminalSession;
import com.termux.view.TerminalView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.drawerlayout.widget.DrawerLayout;
import main.java.com.termux.application.TermuxApplication;

/**
 * A view showing extra keys (such as Escape, Ctrl, Alt) not normally available on an Android soft
 * keyboard.
 */
public final class ExtraKeysView extends GridLayout {

    public static int TEXT_COLOR = 0xFFFFFFFF;
    private static final int BUTTON_COLOR = 0x00000000;
    private static final int INTERESTING_COLOR = 0xFF80DEEA;
    private static final int BUTTON_PRESSED_COLOR = 0x7FFFFFFF;

    public ExtraKeysView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * HashMap that implements Python dict.get(key, default) function.
     * Default java.util .get(key) is then the same as .get(key, null);
     */
    static class CleverMap<K, V> extends HashMap<K, V> {
        V get(K key, V defaultValue) {
            if (containsKey(key))
                return get(key);
            else
                return defaultValue;
        }
    }

    static class CharDisplayMap extends CleverMap<String, String> {
    }

    /**
     * Keys are displayed in a natural looking way, like "→" for "RIGHT"
     */
    static final Map<String, Integer> keyCodesForString = new HashMap<String, Integer>() {{
        put("ESC", KeyEvent.KEYCODE_ESCAPE);
        put("TAB", KeyEvent.KEYCODE_TAB);
        put("HOME", KeyEvent.KEYCODE_MOVE_HOME);
        put("END", KeyEvent.KEYCODE_MOVE_END);
        put("PGUP", KeyEvent.KEYCODE_PAGE_UP);
        put("PGDN", KeyEvent.KEYCODE_PAGE_DOWN);
        put("INS", KeyEvent.KEYCODE_INSERT);
        put("DEL", KeyEvent.KEYCODE_FORWARD_DEL);
        put("BKSP", KeyEvent.KEYCODE_DEL);
        put("UP", KeyEvent.KEYCODE_DPAD_UP);
        put("LEFT", KeyEvent.KEYCODE_DPAD_LEFT);
        put("RIGHT", KeyEvent.KEYCODE_DPAD_RIGHT);
        put("DOWN", KeyEvent.KEYCODE_DPAD_DOWN);
        put("ENTER", KeyEvent.KEYCODE_ENTER);
        put("F1", KeyEvent.KEYCODE_F1);
        put("F2", KeyEvent.KEYCODE_F2);
        put("F3", KeyEvent.KEYCODE_F3);
        put("F4", KeyEvent.KEYCODE_F4);
        put("F5", KeyEvent.KEYCODE_F5);
        put("F6", KeyEvent.KEYCODE_F6);
        put("F7", KeyEvent.KEYCODE_F7);
        put("F8", KeyEvent.KEYCODE_F8);
        put("F9", KeyEvent.KEYCODE_F9);
        put("F10", KeyEvent.KEYCODE_F10);
        put("F11", KeyEvent.KEYCODE_F11);
        put("F12", KeyEvent.KEYCODE_F12);
    }};

    private void sendKey(View view, String keyName, boolean forceCtrlDown, boolean forceLeftAltDown) {
        TerminalView terminalView = view.findViewById(R.id.terminal_view);
        if ("KEYBOARD".equals(keyName)) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, 0);
        } else if ("DRAWER".equals(keyName)) {
            DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
            drawer.openDrawer(Gravity.LEFT);
        } else if (keyCodesForString.containsKey(keyName)) {
            int keyCode = keyCodesForString.get(keyName);
            int metaState = 0;
            if (forceCtrlDown) {
                metaState |= KeyEvent.META_CTRL_ON | KeyEvent.META_CTRL_LEFT_ON;
            }
            if (forceLeftAltDown) {
                metaState |= KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON;
            }
            KeyEvent keyEvent = new KeyEvent(0, 0, KeyEvent.ACTION_UP, keyCode, 0, metaState);
            terminalView.onKeyDown(keyCode, keyEvent);
        } else {
            // not a control char
            keyName.codePoints().forEach(codePoint -> {
                terminalView.inputCodePoint(codePoint, forceCtrlDown, forceLeftAltDown);
            });
        }
    }

    private void sendKey(View view, ExtraKeyButton buttonInfo) {
        if (buttonInfo.isMacro()) {
            String[] keys = buttonInfo.getKey().split(" ");
            boolean ctrlDown = false;
            boolean altDown = false;
            for (String key : keys) {
                if ("CTRL".equals(key)) {
                    ctrlDown = true;
                } else if ("ALT".equals(key)) {
                    altDown = true;
                } else {
                    sendKey(view, key, ctrlDown, altDown);
                    ctrlDown = false;
                    altDown = false;
                }
            }
        } else {
            sendKey(view, buttonInfo.getKey(), false, false);
        }
    }

    public static void sendKey2(TerminalView view, String keyName) {
        TerminalView terminalView = view;
        if (keyCodesForString.containsKey(keyName)) {
            int keyCode = keyCodesForString.get(keyName);
            terminalView.onKeyDown(keyCode, new KeyEvent(KeyEvent.ACTION_UP, keyCode));
            // view.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode));
        } else {
            // not a control char
            TerminalSession session = terminalView.getCurrentSession();
            if (session != null && keyName.length() > 0)
                session.write(keyName);
        }
    }


    public enum SpecialButton {
        CTRL, ALT, FN
    }

    private static class SpecialButtonState {
        boolean isOn = false;
        ToggleButton button = null;
    }

    private Map<SpecialButton, SpecialButtonState> specialButtons = new HashMap<SpecialButton, SpecialButtonState>() {{
        put(SpecialButton.CTRL, new SpecialButtonState());
        put(SpecialButton.ALT, new SpecialButtonState());
        put(SpecialButton.FN, new SpecialButtonState());
    }};

    private ScheduledExecutorService scheduledExecutor;
    private PopupWindow popupWindow;
    private int longPressCount;

    public boolean readSpecialButton(SpecialButton name) {
        SpecialButtonState state = specialButtons.get(name);
        if (state == null)
            throw new RuntimeException("Must be a valid special button (see source)");

        if (!state.isOn)
            return false;

        if (state.button == null) {
            return false;
        }

        if (state.button.isPressed())
            return true;

        if (!state.button.isChecked())
            return false;

        state.button.setChecked(false);
        state.button.setTextColor(TEXT_COLOR);
        return true;
    }

    void popup(View view, String text) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Button button = new Button(getContext(), null, android.R.attr.buttonBarButtonStyle);
        button.setText(text);
        button.setTextColor(TEXT_COLOR);
        button.setPadding(0, 0, 0, 0);
        button.setMinHeight(0);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setMinimumHeight(0);
        button.setWidth(width);
        button.setHeight(height);
        button.setBackgroundColor(BUTTON_PRESSED_COLOR);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(button);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.showAsDropDown(view, 0, -2 * height);
    }

    static final CharDisplayMap classicArrowsDisplay = new CharDisplayMap() {{
        // classic arrow keys (for ◀ ▶ ▲ ▼ @see arrowVariationDisplay) 
        put("LEFT", "←"); // U+2190 ← LEFTWARDS ARROW
        put("RIGHT", "→"); // U+2192 → RIGHTWARDS ARROW
        put("UP", "↑"); // U+2191 ↑ UPWARDS ARROW
        put("DOWN", "↓"); // U+2193 ↓ DOWNWARDS ARROW
    }};

    static final CharDisplayMap wellKnownCharactersDisplay = new CharDisplayMap() {{
        // well known characters // https://en.wikipedia.org/wiki/{Enter_key, Tab_key, Delete_key}
        put("ENTER", "↲"); // U+21B2 ↲ DOWNWARDS ARROW WITH TIP LEFTWARDS
        put("TAB", "↹"); // U+21B9 ↹ LEFTWARDS ARROW TO BAR OVER RIGHTWARDS ARROW TO BAR
        put("BKSP", "⌫"); // U+232B ⌫ ERASE TO THE LEFT sometimes seen and easy to understand
        put("DEL", "⌦"); // U+2326 ⌦ ERASE TO THE RIGHT not well known but easy to understand
    }};

    static final CharDisplayMap lessKnownCharactersDisplay = new CharDisplayMap() {{
        // https://en.wikipedia.org/wiki/{Home_key, End_key, Page_Up_and_Page_Down_keys}
        // home key can mean "goto the beginning of line" or "goto first page" depending on context, hence the diagonal
        put("HOME", "⇱"); // from IEC 9995 // U+21F1 ⇱ NORTH WEST ARROW TO CORNER
        put("END", "⇲"); // from IEC 9995 // ⇲ // U+21F2 ⇲ SOUTH EAST ARROW TO CORNER
        put("PGUP", "⇑"); // no ISO character exists, U+21D1 ⇑ UPWARDS DOUBLE ARROW will do the trick
        put("PGDN", "⇓"); // no ISO character exists, U+21D3 ⇓ DOWNWARDS DOUBLE ARROW will do the trick
    }};

    static final CharDisplayMap arrowTriangleVariationDisplay = new CharDisplayMap() {{
        // alternative to classic arrow keys 
        put("LEFT", "◀"); // U+25C0 ◀ BLACK LEFT-POINTING TRIANGLE
        put("RIGHT", "▶"); // U+25B6 ▶ BLACK RIGHT-POINTING TRIANGLE
        put("UP", "▲"); // U+25B2 ▲ BLACK UP-POINTING TRIANGLE
        put("DOWN", "▼"); // U+25BC ▼ BLACK DOWN-POINTING TRIANGLE
    }};

    static final CharDisplayMap notKnownIsoCharacters = new CharDisplayMap() {{
        // Control chars that are more clear as text // https://en.wikipedia.org/wiki/{Function_key, Alt_key, Control_key, Esc_key}
        // put("FN", "FN"); // no ISO character exists
        put("CTRL", "⎈"); // ISO character "U+2388 ⎈ HELM SYMBOL" is unknown to people and never printed on computers, however "U+25C7 ◇ WHITE DIAMOND" is a nice presentation, and "^" for terminal app and mac is often used 
        put("ALT", "⎇"); // ISO character "U+2387 ⎇ ALTERNATIVE KEY SYMBOL'" is unknown to people and only printed as the Option key "⌥" on Mac computer
        put("ESC", "⎋"); // ISO character "U+238B ⎋ BROKEN CIRCLE WITH NORTHWEST ARROW" is unknown to people and not often printed on computers 
    }};

    static final CharDisplayMap nicerLookingDisplay = new CharDisplayMap() {{
        // nicer looking for most cases
        put("-", "―"); // U+2015 ― HORIZONTAL BAR
    }};

    /**
     * Keys are displayed in a natural looking way, like "→" for "RIGHT" or "↲" for ENTER
     */
    public static final CharDisplayMap defaultCharDisplay = new CharDisplayMap() {{
        putAll(classicArrowsDisplay);
        putAll(wellKnownCharactersDisplay);
        putAll(nicerLookingDisplay);
        // all other characters are displayed as themselves
    }};

    public static final CharDisplayMap lotsOfArrowsCharDisplay = new CharDisplayMap() {{
        putAll(classicArrowsDisplay);
        putAll(wellKnownCharactersDisplay);
        putAll(lessKnownCharactersDisplay); // NEW
        putAll(nicerLookingDisplay);
    }};

    public static final CharDisplayMap arrowsOnlyCharDisplay = new CharDisplayMap() {{
        putAll(classicArrowsDisplay);
        // putAll(wellKnownCharactersDisplay); // REMOVED
        // putAll(lessKnownCharactersDisplay); // REMOVED
        putAll(nicerLookingDisplay);
    }};

    public static final CharDisplayMap fullIsoCharDisplay = new CharDisplayMap() {{
        putAll(classicArrowsDisplay);
        putAll(wellKnownCharactersDisplay);
        putAll(lessKnownCharactersDisplay); // NEW
        putAll(nicerLookingDisplay);
        putAll(notKnownIsoCharacters); // NEW
    }};

    /**
     * Some people might call our keys differently
     */
    static final CharDisplayMap controlCharsAliases = new CharDisplayMap() {{
        put("ESCAPE", "ESC");
        put("CONTROL", "CTRL");
        put("RETURN", "ENTER"); // Technically different keys, but most applications won't see the difference
        put("FUNCTION", "FN");
        // no alias for ALT

        // Directions are sometimes written as first and last letter for brevety
        put("LT", "LEFT");
        put("RT", "RIGHT");
        put("DN", "DOWN");
        // put("UP", "UP"); well, "UP" is already two letters

        put("PAGEUP", "PGUP");
        put("PAGE_UP", "PGUP");
        put("PAGE UP", "PGUP");
        put("PAGE-UP", "PGUP");

        // no alias for HOME
        // no alias for END

        put("PAGEDOWN", "PGDN");
        put("PAGE_DOWN", "PGDN");
        put("PAGE-DOWN", "PGDN");

        put("DELETE", "DEL");
        put("BACKSPACE", "BKSP");

        // easier for writing in termux.properties
        put("BACKSLASH", "\\");
        put("QUOTE", "\"");
        put("APOSTROPHE", "'");
    }};

    /**
     * Applies the 'controlCharsAliases' mapping to all the strings in *buttons*
     * Modifies the array, doesn't return a new one.
     */
    void replaceAliases(String[][] buttons) {
        for (int i = 0; i < buttons.length; i++)
            for (int j = 0; j < buttons[i].length; j++)
                buttons[i][j] = controlCharsAliases.get(buttons[i][j], buttons[i][j]);
    }

    /**
     * General util function to compute the longest column length in a matrix.
     */
    static int maximumLength(Object[][] matrix) {
        int m = 0;
        for (Object[] row : matrix)
            m = Math.max(m, row.length);
        return m;
    }
    /**
     * Reload the view given parameters in termux.properties
     *
     * @param buttons matrix of String as defined in termux.properties extrakeys
     *                Can Contain The Strings CTRL ALT TAB FN ENTER LEFT RIGHT UP DOWN or normal strings
     *                Some aliases are possible like RETURN for ENTER, LT for LEFT and more (@see controlCharsAliases for the whole list).
     *                Any string of length > 1 in total Uppercase will print a warning
     *                <p>
     *                Examples:
     *                "ENTER" will trigger the ENTER keycode
     *                "LEFT" will trigger the LEFT keycode and be displayed as "←"
     *                "→" will input a "→" character
     *                "−" will input a "−" character
     *                "-_-" will input the string "-_-"
     */
    @SuppressLint("ClickableViewAccessibility")
    void reload(ExtraKeysInfos infos) {
        if(infos == null)
            return;

        for(SpecialButtonState state : specialButtons.values())
            state.button = null;

        removeAllViews();

        ExtraKeyButton[][] buttons = infos.getMatrix();

        setRowCount(buttons.length);
        setColumnCount(maximumLength(buttons));

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                final ExtraKeyButton buttonInfo = buttons[row][col];

                Button button;
                if(Arrays.asList("CTRL", "ALT", "FN").contains(buttonInfo.getKey())) {
                    SpecialButtonState state = specialButtons.get(SpecialButton.valueOf(buttonInfo.getKey())); // for valueOf: https://stackoverflow.com/a/604426/1980630
                    state.isOn = true;
                    button = state.button = new ToggleButton(getContext(), null, android.R.attr.buttonBarButtonStyle);
                    button.setClickable(true);
                } else {
                    button = new Button(getContext(), null, android.R.attr.buttonBarButtonStyle);
                }

                button.setText(buttonInfo.getDisplay());
                button.setTextColor(TEXT_COLOR);
                button.setPadding(0, 0, 0, 0);

                final Button finalButton = button;
                button.setOnClickListener(v -> {
                    if (Settings.System.getInt(getContext().getContentResolver(),
                        Settings.System.HAPTIC_FEEDBACK_ENABLED, 0) != 0) {

                        if (Build.VERSION.SDK_INT >= 28) {
                            finalButton.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                        } else {
                            // Perform haptic feedback only if no total silence mode enabled.
                            if (Settings.Global.getInt(getContext().getContentResolver(), "zen_mode", 0) != 2) {
                                finalButton.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                            }
                        }
                    }

                    View root = getRootView();
                    if (Arrays.asList("CTRL", "ALT", "FN").contains(buttonInfo.getKey())) {
                        ToggleButton self = (ToggleButton) finalButton;
                        self.setChecked(self.isChecked());
                        self.setTextColor(self.isChecked() ? INTERESTING_COLOR : TEXT_COLOR);
                    } else {
                        sendKey(root, buttonInfo);
                    }
                });

                button.setOnTouchListener((v, event) -> {
                    final View root = getRootView();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            longPressCount = 0;
                            v.setBackgroundColor(BUTTON_PRESSED_COLOR);
                            if (Arrays.asList("UP", "DOWN", "LEFT", "RIGHT", "BKSP", "DEL").contains(buttonInfo.getKey())) {
                                // autorepeat
                                scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
                                scheduledExecutor.scheduleWithFixedDelay(() -> {
                                    longPressCount++;
                                    sendKey(root, buttonInfo);
                                }, 400, 80, TimeUnit.MILLISECONDS);
                            }
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            if (buttonInfo.getPopup() != null) {
                                if (popupWindow == null && event.getY() < 0) {
                                    if (scheduledExecutor != null) {
                                        scheduledExecutor.shutdownNow();
                                        scheduledExecutor = null;
                                    }
                                    v.setBackgroundColor(BUTTON_COLOR);
                                    String extraButtonDisplayedText = buttonInfo.getPopup().getDisplay();
                                    popup(v, extraButtonDisplayedText);
                                }
                                if (popupWindow != null && event.getY() > 0) {
                                    v.setBackgroundColor(BUTTON_PRESSED_COLOR);
                                    popupWindow.dismiss();
                                    popupWindow = null;
                                }
                            }
                            return true;

                        case MotionEvent.ACTION_CANCEL:
                            v.setBackgroundColor(BUTTON_COLOR);
                            if (scheduledExecutor != null) {
                                scheduledExecutor.shutdownNow();
                                scheduledExecutor = null;
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(BUTTON_COLOR);
                            if (scheduledExecutor != null) {
                                scheduledExecutor.shutdownNow();
                                scheduledExecutor = null;
                            }
                            if (longPressCount == 0 || popupWindow != null) {
                                if (popupWindow != null) {
                                    popupWindow.setContentView(null);
                                    popupWindow.dismiss();
                                    popupWindow = null;
                                    if (buttonInfo.getPopup() != null) {
                                        sendKey(root, buttonInfo.getPopup());
                                    }
                                } else {
                                    v.performClick();
                                }
                            }
                            return true;

                        default:
                            return true;
                    }
                });

                LayoutParams param = new GridLayout.LayoutParams();
                param.width = 0;
                param.height = 0;
                param.setMargins(0, 0, 0, 0);
                param.columnSpec = GridLayout.spec(col, GridLayout.FILL, 1.f);
                param.rowSpec = GridLayout.spec(row, GridLayout.FILL, 1.f);
                button.setLayoutParams(param);

                addView(button);
            }
        }
    }



    //初始化颜色
    public void setColorButton() {

        for (int i = 0; i < getChildCount(); i++) {

            Button childAt = (Button) getChildAt(i);
            childAt.setTextColor(TEXT_COLOR);
        }

    }
}

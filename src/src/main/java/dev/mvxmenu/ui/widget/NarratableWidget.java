package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;

public interface NarratableWidget extends MvxmenuWidget {

    /**
     * Get the narration text announced by the in-game narrator.
     *
     * @return spoken description of this widget
     */
    String getNarrationText();

    /**
     * Get the narration priority (affects announcement order).
     *
     * @return narration priority level
     */
    default Priority getNarrationPriority() {
        return Priority.NORMAL;
    }

    enum Priority {
        LOW,
        NORMAL,
        HIGH
    }
}

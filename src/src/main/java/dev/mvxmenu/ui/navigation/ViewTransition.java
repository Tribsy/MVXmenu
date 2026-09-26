package dev.mvxmenu.ui.navigation;

public class ViewTransition {
    private float alpha = 1.0f;
    private float targetAlpha = 1.0f;
    private float transitionSpeed = 0.1f;

    public void startFadeOut() {
        targetAlpha = 0.0f;
    }

    public void startFadeIn() {
        targetAlpha = 1.0f;
    }

    public void update() {
        if (alpha < targetAlpha) {
            alpha = Math.min(targetAlpha, alpha + transitionSpeed);
        } else if (alpha > targetAlpha) {
            alpha = Math.max(targetAlpha, alpha - transitionSpeed);
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean isComplete() {
        return alpha == targetAlpha;
    }
} 

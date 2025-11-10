package model;

public enum FillMode {
    SOLID("Однородная", 1),
    GRADIENT("Градиентная", 3);

    private final String displayName;
    private final int requiredColors;

    FillMode(String displayName, int requiredColors) {
        this.displayName = displayName;
        this.requiredColors = requiredColors;
    }

    public String getDisplayName() {
        return displayName;
    }
}
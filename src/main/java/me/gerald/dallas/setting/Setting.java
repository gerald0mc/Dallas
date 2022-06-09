package me.gerald.dallas.setting;

public class Setting {
    private final Visibility visibility;
    private String name;
    private final String description;

    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
        visibility = null;
    }

    public Setting(String name, String description, Visibility visibility) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVisible() {
        if (visibility == null)
            return true;
        return visibility.visible();
    }
}

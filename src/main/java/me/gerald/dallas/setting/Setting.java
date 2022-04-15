package me.gerald.dallas.setting;

public class Setting {
    private final Visibility visibility;
    private String name;

    public Setting(String name) {
        this.name = name;
        visibility = null;
    }

    public Setting(String name, Visibility visibility) {
        this.name = name;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        if (visibility == null)
            return true;
        return visibility.visible();
    }
}

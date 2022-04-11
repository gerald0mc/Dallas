package me.gerald.dallas.setting;

public class Setting {
    private String name;
    private Visibility visibility;

    public Setting(String name) {
        this.name = name;
        this.visibility = null;
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
        if (this.visibility == null)
            return true;
        return this.visibility.visible();
    }
}

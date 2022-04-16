package me.gerald.dallas.features.gui.api;

public abstract class DragComponent extends AbstractContainer {
    protected boolean dragging = false;
    private int dragX, dragY;

    public DragComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void beginDragging(int mouseX, int mouseY) {
        dragging = true;
        dragX = mouseX - x;
        dragY = mouseY - y;
    }

    public void stopDragging() {
        dragging = false;
    }

    public void updateDragPosition(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    public boolean isDragging() {
        return dragging;
    }
}

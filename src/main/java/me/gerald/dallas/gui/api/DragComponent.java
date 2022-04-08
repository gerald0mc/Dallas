package me.gerald.dallas.gui.api;

public abstract class DragComponent extends AbstractContainer {
    public DragComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    protected boolean dragging = false;
    private int dragX, dragY;

    protected void beginDragging(int mouseX, int mouseY) {
        dragging = true;
        dragX = mouseX - x;
        dragY = mouseY - y;
    }

    protected void stopDragging() {
        dragging = false;
    }

    protected void updateDragPosition(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }
}

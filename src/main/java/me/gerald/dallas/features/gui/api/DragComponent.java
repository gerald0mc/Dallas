package me.gerald.dallas.features.gui.api;

public abstract class DragComponent extends AbstractContainer {
    protected boolean dragging = false;
    private int dragX, dragY;

    public DragComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    protected void beginDragging(int mouseX, int mouseY) {
        this.dragging = true;
        this.dragX = mouseX - this.x;
        this.dragY = mouseY - this.y;
    }

    protected void stopDragging() {
        this.dragging = false;
    }

    protected void updateDragPosition(int mouseX, int mouseY) {
        if (this.dragging) {
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
        }
    }
}

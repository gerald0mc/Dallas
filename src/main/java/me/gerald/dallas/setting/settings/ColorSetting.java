package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;

import java.awt.*;

public class ColorSetting extends Setting {
    public ColorSetting(String name, int r, int g, int b) {
        super(name);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    public ColorSetting(String name, int r, int g, int b, int a) {
        super(name);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    private int r, g, b, a;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public Color getColor() {
        return new Color(r, g, b, a);
    }

    public void setColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
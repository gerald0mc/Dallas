package me.gerald.dallas.mixin.mixins;

public interface ICPacketPlayer {
    void setOnGround(boolean onGround);

    void setX(double x);

    void setY(double y);

    void setZ(double z);

    void setYaw(float yaw);

    void setPitch(float pitch);
}

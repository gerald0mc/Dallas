package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.mixin.mixins.MixinItemRenderer;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author bush
 * @see MixinItemRenderer
 * @since 5/6/2022
 */
public class ViewModel extends Module {
    public BooleanSetting noSway = new BooleanSetting("NoSway", true);
    public BooleanSetting viewModel = new BooleanSetting("ViewModel", true);
    public BooleanSetting bothSame = new BooleanSetting("BothSame", true, () -> viewModel.getValue());
    public ModeSetting handPage = new ModeSetting("HandPage", "Left", () -> viewModel.getValue() && !bothSame.getValue(), "Left", "Right");
    //translate
    public NumberSetting translateX = new NumberSetting("TranslateX", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    public NumberSetting translateY = new NumberSetting("TranslateY", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    public NumberSetting translateZ = new NumberSetting("TranslateZ", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    //rotate
    public NumberSetting rotateX = new NumberSetting("RotateX", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    public NumberSetting rotateY = new NumberSetting("RotateY", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    public NumberSetting rotateZ = new NumberSetting("RotateZ", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    //scale
    public NumberSetting scaleX = new NumberSetting("ScaleX", 100, 0, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    public NumberSetting scaleY = new NumberSetting("ScaleY", 100, 0, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    public NumberSetting scaleZ = new NumberSetting("ScaleZ", 100, 0, 200, () -> viewModel.getValue() && !bothSame.getValue() ? handPage.getMode().equalsIgnoreCase("left") : bothSame.getValue());
    //translate
    public NumberSetting rightTranslateX = new NumberSetting("RightTranslateX", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    public NumberSetting rightTranslateY = new NumberSetting("RightTranslateY", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    public NumberSetting rightTranslateZ = new NumberSetting("RightTranslateZ", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    //rotate
    public NumberSetting rightRotateX = new NumberSetting("RightRotateX", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    public NumberSetting rightRotateY = new NumberSetting("RightRotateY", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    public NumberSetting rightRotateZ = new NumberSetting("RightRotateZ", 0, -200, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    //scale
    public NumberSetting rightScaleX = new NumberSetting("RightScaleX", 100, 0, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    public NumberSetting rightScaleY = new NumberSetting("RightScaleY", 100, 0, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));
    public NumberSetting rightScaleZ = new NumberSetting("RightScaleZ", 100, 0, 200, () -> viewModel.getValue() && !bothSame.getValue() && handPage.getMode().equalsIgnoreCase("right"));

    public ViewModel() {
        super("ViewModel", Category.RENDER, "Alters the appearance of held items");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(!bothSame.getValue()) {
            if(handPage.getMode().equalsIgnoreCase("Left")) {
                if (handPage.getMode().equalsIgnoreCase("Left")) {
                    translateX.setName("LeftTranslateX");
                    translateY.setName("LeftTranslateY");
                    translateZ.setName("LeftTranslateZ");
                    rotateX.setName("LeftRotateX");
                    rotateY.setName("LeftRotateY");
                    rotateZ.setName("LeftRotateZ");
                    scaleX.setName("LeftScaleX");
                    scaleY.setName("LeftScaleY");
                    scaleZ.setName("LeftScaleZ");
                }
            }
        } else {
            translateX.setName("TranslateX");
            translateY.setName("TranslateY");
            translateZ.setName("TranslateZ");
            rotateX.setName("RotateX");
            rotateY.setName("RotateY");
            rotateZ.setName("RotateZ");
            scaleX.setName("ScaleX");
            scaleY.setName("ScaleY");
            scaleZ.setName("ScaleZ");
        }
    }
}

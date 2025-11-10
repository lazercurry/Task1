package controller.service;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import model.FillMode;
import utils.ConversionUtils;

public class ColorManager {
    public java.awt.Color[] prepareColors(FillMode fillMode, ColorPicker v1, ColorPicker v2, ColorPicker v3) {
        if (fillMode == FillMode.GRADIENT) {
            return new java.awt.Color[]{
                    ConversionUtils.toAwtColor(v1.getValue()),
                    ConversionUtils.toAwtColor(v2.getValue()),
                    ConversionUtils.toAwtColor(v3.getValue())
            };
        } else {
            return new java.awt.Color[]{ ConversionUtils.toAwtColor(v1.getValue()) };
        }
    }
}
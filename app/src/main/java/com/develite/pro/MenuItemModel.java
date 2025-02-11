package com.develite.pro;

public class MenuItemModel {
    private String title;
    private int iconRes;

    public MenuItemModel(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public int getIconRes() {
        return iconRes;
    }
}

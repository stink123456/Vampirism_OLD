/*
 * File: Vampirism.java
 *
 * Copyright (C) Exorath 2014 All rights reserved.
 * Do not duplicate, publish, modify or otherwise distribute.
 */

package com.exorath.vampirism;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Matthew Hogan
 */
public class Vampirism extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = this;
    }

    /**
     * @return the Bukkit Plugin instance
     */
    private static Plugin getInstance() { return instance; }
}
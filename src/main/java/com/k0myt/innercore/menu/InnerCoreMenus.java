package com.k0myt.innercore.menu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

public class InnerCoreMenus {
    public static final DeferredRegister<AbstractContainerMenu> MENUS = 
            DeferredRegister.create(Registries.MENU, "innercore");

    // TODO: Add menu types for container interactions
}

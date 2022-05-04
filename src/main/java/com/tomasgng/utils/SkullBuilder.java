package com.tomasgng.utils;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.tomasgng.TommyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class SkullBuilder {

    private ItemStack itemStack;
    private SkullMeta itemMeta;

    public SkullBuilder() {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        this.itemMeta = (SkullMeta) itemStack.getItemMeta();
    }

    public SkullBuilder setPlayerProfileByURL(String url) {
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        var profile = Bukkit.createProfile(UUID.randomUUID(), null);
        profile.getProperties().add(new ProfileProperty("textures", new String(encodedData)));
        itemMeta.setPlayerProfile(profile);
        return this;
    }

    public SkullBuilder setKey(String key) {
        this.itemMeta.getPersistentDataContainer().set(new NamespacedKey(TommyGenerator.getInstance(), key), PersistentDataType.DOUBLE, Math.PI);
        return this;
    }

    public SkullBuilder setDisplayName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public SkullBuilder setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public SkullBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public SkullBuilder setUnbreakable(boolean bool) {
        this.itemMeta.setUnbreakable(bool);
        return this;
    }

    public SkullBuilder setItemFlag(ItemFlag... itemflag) {
        this.itemMeta.addItemFlags(itemflag);
        return this;
    }

    public SkullBuilder setLore(String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public SkullBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, false);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}

package io.github.linsminecraftstudio.mxlib.i18n;

import io.github.linsminecraftstudio.mxlib.chat.components.WrappedComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.MessageFormat;

class LanguageData {
    private final ConfigurationSection section;

    private LanguageData(ConfigurationSection section) {
        this.section = section;
    }

    public WrappedComponent getMsg(String key, Object... args) {
        return WrappedComponent.of(getMessageFormat(key).format(args));
    }

    public String getRawMsg(String key) {
        return section.getString(key, "Missing translation for key: " + key);
    }

    private MessageFormat getMessageFormat(String key) {
        String pattern = section.getString(key, "Missing translation for key: " + key);
        return new MessageFormat(pattern);
    }

    public static LanguageData fromFile(File file) {
        return new LanguageData(YamlConfiguration.loadConfiguration(file));
    }
}

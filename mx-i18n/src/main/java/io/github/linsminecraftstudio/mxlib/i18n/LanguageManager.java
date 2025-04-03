package io.github.linsminecraftstudio.mxlib.i18n;

import io.github.linsminecraftstudio.mxlib.chat.components.WrappedComponent;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LanguageManager {
    private final Map<Language, LanguageData> languageDataMap = new HashMap<>();

    private final Plugin plugin;
    private final Language defaultLanguage;

    @Setter
    private boolean detectPlayerLocale = false;

    public LanguageManager(Plugin plugin) {
        this(plugin, Language.ENGLISH_US);
    }

    public LanguageManager(Plugin plugin, Language defaultLanguage) {
        this.plugin = plugin;
        this.defaultLanguage = defaultLanguage;
    }

    private void loadLanguages() {
        File pluginFolder = plugin.getDataFolder();

        URL fileURL = Objects.requireNonNull(plugin.getClass().getClassLoader().getResource("languages/"));
        String jarPath = fileURL.toString().substring(0, fileURL.toString().indexOf("!/") + 2);

        try {
            URL jar = URI.create(jarPath).toURL();
            JarURLConnection jarCon = (JarURLConnection) jar.openConnection();
            JarFile jarFile = jarCon.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();

            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                String name = entry.getName();
                if (name.startsWith("languages/") && !entry.isDirectory()) {
                    String realName = name.replaceAll("languages/", "");
                    try (InputStream stream = plugin.getClass().getClassLoader().getResourceAsStream(name)) {
                        File destinationFile = new File(pluginFolder, "languages/" + realName);

                        if (!destinationFile.exists() && stream != null) {
                            plugin.saveResource("languages/" + realName, false);
                        }

                        completeLangFile(plugin, "languages/" + realName);

                        File languageFile = new File(pluginFolder, "languages/" + realName);
                        languageDataMap.put(
                                Language.fromLanguageTag(realName.replace(".yml", "")),
                                LanguageData.fromFile(languageFile)
                        );
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void completeLangFile(Plugin plugin, String resourceFile) {
        InputStream stream = plugin.getResource(resourceFile);
        File file = new File(plugin.getDataFolder(), resourceFile);

        if (!file.exists()) {
            if (stream != null) {
                plugin.saveResource(resourceFile, false);
                return;
            }
            plugin.getLogger().warning("Language file completion of '" + resourceFile + "' is failed.");
            return;
        }

        if (stream == null) {
            plugin.getLogger().warning("Language file completion of '" + resourceFile + "' is failed.");
            return;
        }

        try {
            Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);
            YamlConfiguration configuration2 = YamlConfiguration.loadConfiguration(file);

            Set<String> keys = configuration.getKeys(true);
            for (String key : keys) {
                Object value = configuration.get(key);
                if (value instanceof List<?>) {
                    List<?> list2 = configuration2.getList(key);
                    if (list2 == null) {
                        configuration2.set(key, value);
                        continue;
                    }
                }
                if (!configuration2.contains(key)) {
                    configuration2.set(key, value);
                }
                if (!configuration.getComments(key).equals(configuration2.getComments(key))) {
                    configuration2.setComments(key, configuration.getComments(key));
                }
            }
            for (String key : configuration2.getKeys(true)) {
                if (configuration2.contains(key) & !configuration.contains(key)) {
                    configuration2.set(key, null);
                }
            }
            configuration2.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning("Language file completion of '" + resourceFile + "' is failed.");
        }
    }


    public WrappedComponent getMsg(Player player, String key, Object... args) {
        return getMsg(getPlayerLocale(player), key, args);
    }

    public WrappedComponent getMsg(Language language, String key, Object... args) {
        LanguageData languageData = getLanguageData(language);
        return languageData.getMsg(key, args);
    }

    public void sendMsg(CommandSender sender, String key, Object... args) {
        sender.sendMessage(getMsg(getPlayerLocale(null), key, args).asComponent());
    }

    public void sendActionBar(Player player, String key, Object... args) {
        player.sendActionBar(getMsg(player, key, args).asComponent());
    }

    public void reload() {
        languageDataMap.clear();
        loadLanguages();
    }

    private Language getPlayerLocale(Player player) {
        if (player == null) {
            return defaultLanguage;
        }

        if (detectPlayerLocale) {
            return Language.fromLocale(player.locale());
        }

        return defaultLanguage;
    }

    private LanguageData getLanguageData(Language language) {
        if (languageDataMap.containsKey(language)) {
            return languageDataMap.get(language);
        }

        return languageDataMap.get(defaultLanguage);
    }

    public int getLoadedLanguagesCount() {
        return languageDataMap.size();
    }
}

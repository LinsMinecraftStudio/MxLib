package io.github.linsminecraftstudio.mxlib.i18n;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Locale;
import java.util.Set;
import java.util.HashSet;

/**
 * Feel free to add more languages to this enum.<br>
 * Feel free to copy this class.<br>
 * <br>
 *
 * An enum that contains many languages that most people use.
 * @author lijinhong11
 * @since MX-I18n Module 1.0
 */
@Getter
public enum Language {
    CHINESE_SIMPLIFIED("zh", "CN", "简体中文", "cmn", "Chinese (Simplified)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5YmMwMzVjZGM4MGYxYWI1ZTExOThmMjlmM2FkM2ZkZDJiNDJkOWE2OWFlYjY0ZGU5OTA2ODE4MDBiOThkYyJ9fX0="),
    CHINESE_TRADITIONAL("zh", "TW", "繁體中文", "cmn", "Chinese (Traditional)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5YmMwMzVjZGM4MGYxYWI1ZTExOThmMjlmM2FkM2ZkZDJiNDJkOWE2OWFlYjY0ZGU5OTA2ODE4MDBiOThkYyJ9fX0="),
    ENGLISH_US("en", "US", "English (US)", "eng", "English (United States)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5MTQ1Njg3N2Y1NGJmMWFjZTI1MWU0Y2VlNDBkYmE1OTdkMmNjNDAzNjJjYjhmNGVkNzExZTUwYjBiZTViMyJ9fX0="),
    ENGLISH_UK("en", "GB", "English (UK)", "eng", "English (United Kingdom)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQzOWQ3ZjljNjdmMzJkY2JiODZiNzAxMGIxZTE0YjYwZGU5Njc3NmEzNWY2MWNlZTk4MjY2MGFhY2Y1MjY0YiJ9fX0="),
    SPANISH("es", "ES", "Español", "spa", "Spanish (Spain)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0="),
    FRENCH("fr", "FR", "Français", "fra", "French (France)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjkwMzM0OWZhNDViZGQ4NzEyNmQ5Y2QzYzZjMGFiYmE3ZGJkNmY1NmZiOGQ3ODcwMTg3M2ExZTdjOGVlMzNjZiJ9fX0="),
    GERMAN("de", "DE", "Deutsch", "deu", "German (Germany)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ=="),
    ITALIAN("it", "IT", "Italiano", "ita", "Italian (Italy)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODVjZTg5MjIzZmE0MmZlMDZhZDY1ZDhkNDRjYTQxMmFlODk5YzgzMTMwOWQ2ODkyNGRmZTBkMTQyZmRiZWVhNCJ9fX0="),
    JAPANESE("ja", "JP", "日本語", "jpn", "Japanese (Japan)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZjMmNhNzIzODY2NmFlMWI5ZGQ5ZGFhM2Q0ZmM4MjlkYjIyNjA5ZmI1NjkzMTJkZWMxZmIwYzhkNmRkNmMxZCJ9fX0="),
    KOREAN("ko", "KR", "한국어", "kor", "Korean (Korea)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ExMjkxM2Q3ZGY2NDBkMThiY2M3YTQ1YTgxNzJjNjhmZmEwNDc1NmU4NGM2ZjBhMmVkYTNkYTQ1ZTAwZGFkZCJ9fX0="),
    PORTUGUESE("pt", "PT", "Português", "por", "Portuguese (Portugal)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWJkNTFmNDY5M2FmMTc0ZTZmZTE5NzkyMzNkMjNhNDBiYjk4NzM5OGUzODkxNjY1ZmFmZDJiYTU2N2I1YTUzYSJ9fX0="),
    PORTUGUESE_BRAZIL("pt", "BR", "Português (Brasil)", "por", "Portuguese (Brazil)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0NjQ3NWQ1ZGNjODE1ZjZjNWYyODU5ZWRiYjEwNjExZjNlODYxYzBlYjE0ZjA4ODE2MWIzYzBjY2IyYjBkOSJ9fX0="),
    RUSSIAN("ru", "RU", "Русский", "rus", "Russian (Russia)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZlYWZlZjk4MGQ2MTE3ZGFiZTg5ODJhYzRiNDUwOTg4N2UyYzQ2MjFmNmE4ZmU1YzliNzM1YTgzZDc3NWFkIn19fQ=="),
    ARABIC("ar", "SA", "العربية", "ara", "Arabic (Saudi Arabia)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRiZTc1OWE5Y2Y3ZjBhMTlhN2U4ZTYyZjIzNzg5YWQxZDIxY2ViYWUzOGFmOWQ5NTQxNjc2YTNkYjAwMTU3MiJ9fX0="),
    INDONESIAN("id", "ID", "Bahasa Indonesia", "ind", "Indonesian (Indonesia)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JlNjNmY2FhMDIwM2Y4MDQ2ZmI5NjM3NWQwNzlhNWMwOWI0MTYxZDAxMTlkNzE5NDU2NmU2ODljYWIxOGY2OCJ9fX0="),
    THAI("th", "TH", "ไทย", "tha", "Thai (Thailand)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE1YmUzY2RjMWFmMTRjYTE1MTU1NTE3MTEwYmEzMjZkOTk0NTAwN2E4ODllNjY4MTk5OWMzN2QwN2JkNjVmNSJ9fX0="),
    VIETNAMESE("vi", "VN", "Tiếng Việt", "vie", "Vietnamese (Vietnam)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGE1N2I5ZDdkZDA0MTY5NDc4Y2ZkYjhkMGI2ZmQwYjhjODJiNjU2NmJiMjgzNzFlZTlhN2M3YzE2NzFhZDBiYiJ9fX0="),
    SWEDISH("sv", "SE", "Svenska", "swe", "Swedish (Sweden)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTkxMDkwNGJmZjljODZmNmVkNDc2ODhlOTQyOWMyNmU4ZDljNWQ1NzQzYmQzZWJiOGU2ZjUwNDBiZTE5Mjk5OCJ9fX0="),
    DANISH("da", "DK", "Dansk", "dan", "Danish (Denmark)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjMjMwNTVjMzkyNjA2ZjdlNTMxZGFhMjY3NmViZTJlMzQ4OTg4ODEwYzE1ZjE1ZGM1YjM3MzM5OTgyMzIifX19"),
    NORWEGIAN("no", "NO", "Norsk", "nor", "Norwegian (Norway)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRhMDQ4YmMxNTNiMzg0NjdlNzZhMzM0N2YzODM5Njg2MGE4YmM2ODYwMzkzMWU5MWY3YWY1OGJlYzU3MzgzZCJ9fX0="),
    CZECH("cs", "CZ", "Čeština", "ces", "Czech (Czech Republic)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDgxNTJiNzMzNGQ3ZWNmMzM1ZTQ3YTRmMzVkZWZiZDJlYjY5NTdmYzdiZmU5NDIxMjY0MmQ2MmY0NmU2MWUifX19"),
    GREEK("el", "GR", "Ελληνικά", "ell", "Greek (Greece)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTUxNGRlNmRkMmI3NjgyYjFkM2ViY2QxMDI5MWFlMWYwMjFlMzAxMmI1YzhiZWZmZWI3NWIxODE5ZWI0MjU5ZCJ9fX0="),
    ROMANIAN("ro", "RO", "Română", "ron", "Romanian (Romania)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNlYjE3MDhkNTQwNGVmMzI2MTAzZTdiNjA1NTljOTE3OGYzZGNlNzI5MDA3YWM5YTBiNDk4YmRlYmU0NjEwNyJ9fX0="),
    UKRAINIAN("uk", "UA", "Українська", "ukr", "Ukrainian (Ukraine)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjhiOWY1MmUzNmFhNWM3Y2FhYTFlN2YyNmVhOTdlMjhmNjM1ZThlYWM5YWVmNzRjZWM5N2Y0NjVmNWE2YjUxIn19fQ==");

    private final String languageCode;
    private final String regionCode;
    private final String displayName;
    private final String iso3Code;
    private final String englishName;
    private final String minecraftHeadTexture;

    Language(String languageCode, String regionCode, String displayName,
             String iso3Code, String englishName, String minecraftHeadTexture) {
        this.languageCode = languageCode;
        this.regionCode = regionCode;
        this.displayName = displayName;
        this.iso3Code = iso3Code;
        this.englishName = englishName;
        this.minecraftHeadTexture = minecraftHeadTexture;
    }

    public String toLanguageTag() {
        return languageCode + "_" + regionCode;
    }

    public String toHyphenLanguageTag() {
        return languageCode + "-" + regionCode;
    }

    public Locale toLocale() {
        return new Locale(languageCode, regionCode);
    }

    public ItemStack getHeadItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile("MX_COUNTRY_HEAD");
        ProfileProperty profileProperty = new ProfileProperty("textures", minecraftHeadTexture);
        profile.setProperty(profileProperty);

        meta.setPlayerProfile(profile);
        meta.displayName(
                Component.text(displayName)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.YELLOW));

        item.setItemMeta(meta);
        return item;
    }

    public static Language fromLanguageTag(String languageTag) {
        String[] parts = languageTag.split("[_-]");
        if (parts.length != 2) {
            return null;
        }
        return fromCodes(parts[0], parts[1]);
    }

    public static Language fromCodes(String languageCode, String regionCode) {
        for (Language code : values()) {
            if (code.languageCode.equalsIgnoreCase(languageCode) &&
                    code.regionCode.equalsIgnoreCase(regionCode)) {
                return code;
            }
        }
        return null;
    }

    public static Language fromLocale(Locale locale) {
        return fromCodes(locale.getLanguage(), locale.getCountry());
    }

    /**
     * Get all variants for a given language code.
     * @param languageCode the language code to get variants for
     * @return a set of all variants for the given language code
     */
    public static Set<Language> getVariantsForLanguage(String languageCode) {
        Set<Language> variants = new HashSet<>();
        for (Language code : values()) {
            if (code.languageCode.equalsIgnoreCase(languageCode)) {
                variants.add(code);
            }
        }
        return variants;
    }
}
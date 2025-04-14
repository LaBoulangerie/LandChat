package net.laboulangerie.landschat.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.laboulangerie.landschat.LandsChat;

public class LandsChatRenderer implements ChatRenderer.ViewerUnaware {
    private ComponentRenderer componentRenderer;
    private Map<String, TagResolver> resolverMap = Map.of(
            "townychat.format.color", StandardTags.color(),
            "townychat.format.bold",
            TagResolver.resolver(Set.of("bold", "b"), (argumentQueue, context) -> Tag.styling(TextDecoration.BOLD)),
            "townychat.format.underline",
            TagResolver.resolver(Set.of("underline", "u"),
                    (argumentQueue, context) -> Tag.styling(TextDecoration.UNDERLINED)),
            "townychat.format.italic",
            TagResolver.resolver(Set.of("italic", "i", "em"),
                    (argumentQueue, context) -> Tag.styling(TextDecoration.ITALIC)),
            "townychat.format.strikethrough",
            TagResolver.resolver(Set.of("strikethrough", "st"),
                    (argumentQueue, context) -> Tag.styling(TextDecoration.STRIKETHROUGH)),
            "townychat.format.obfuscated",
            TagResolver.resolver(Set.of("obfuscated", "obf"),
                    (argumentQueue, context) -> Tag.styling(TextDecoration.OBFUSCATED)),
            "townychat.format.rainbow", StandardTags.rainbow());

    public LandsChatRenderer() {
        this.componentRenderer = LandsChat.PLUGIN.getComponentRenderer();
    }

    private Component buildMessage(@NotNull Player source, Component message, String format) {
        String plainText = PlainTextComponentSerializer.plainText().serialize(message);
        censorString(plainText);

        List<TagResolver> resolvers = new ArrayList<>();
        resolverMap.forEach((perm, resolver) -> {
            if (source.hasPermission(perm)) {
                resolvers.add(resolver);
            }
        });

        if (source.hasPermission("landschat.format.all")) {
            message = MiniMessage.miniMessage().deserialize(plainText);
        } else {
            message = MiniMessage.builder()
                    .tags(TagResolver.builder().resolvers(resolvers).build())
                    .build()
                    .deserialize(plainText);
        }

        resolvers.add(Placeholder.component("message", message));
        resolvers.add(Placeholder.component("username", source.name()));

        return componentRenderer.parse(source, format, TagResolver.resolver(resolvers));
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName,
            @NotNull Component message) {
        return buildMessage(source, message, LandsChat.PLUGIN.getConfig().getString("format"));
    }

    private String censorString(String string) {
        List<String> words = LandsChat.PLUGIN.getConfig().getStringList("blacklist");
        String[] censorChars = { "#", "@", "!", "*" };

        for (String word : words) {
            // Not readable to say the least but i like it
            // It generates a random string e.g. insult -> !#@!*#
            // Yes it's overcomplicated but it looks cool :)
            string = string.replaceAll("(?i)" + Pattern.quote(word),
                    new Random().ints(word.length(), 0, censorChars.length).mapToObj(i -> censorChars[i])
                            .collect(Collectors.joining()));
        }

        return string;
    }
}
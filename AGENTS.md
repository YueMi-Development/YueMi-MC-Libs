# YueMi-MC-Libs

Shared library plugin providing core APIs for other YueMi plugins.

## Stack
- PaperMC 1.21+, Java 21, Gradle (Kotlin DSL)
- Modular: `core-api` (compile-only) + `core-plugin` (implementation)

## Modules
- `core-api` - Public interfaces (compile-only dependency)
- `core-plugin` - Implementation with shaded API
- `module-gui` - GUI system API
- `module-economy` - Economy system API
- `module-items` - Items system API
- `module-event` - Event system API

## Key APIs
```java
// Access API
YueMiLibsApi api = YueMiLibsProvider.getApi();

// GUI API
GuiApi gui = api.getGui();
GuiBuilder builder = gui.createBuilder();
GuiItem.Builder itemBuilder = gui.createItemBuilder();

// Economy API
EconomyApi economy = api.getEconomy();
economy.getBalance(player);
economy.withdraw(player, amount);

// Items API
ItemsApi items = api.getItems();
items.giveItem(player, "namespace:id", quantity);
```

## Repositories
- Maven Central
- PaperMC: `https://repo.papermc.io/repository/maven-public/`
- YueMi: `https://repo.yuemi.my.id/repository/maven-public/`
- Momirealms: `https://repo.momirealms.net/releases/`
- PlaceholderAPI: `https://repo.extendedclip.com/content/repositories/placeholderapi/`

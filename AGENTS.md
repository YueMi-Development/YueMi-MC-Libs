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
- `module-general` - Compare and format utility APIs

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

// General API
GeneralApi general = api.getGeneral();
general.getCompare().compare(item1, item2);
general.getFormat().formatNumber(1234567);
general.getFormat().formatCompact(1500000);
general.getFormat().formatDuration(3665);
```

## Repositories
- Maven Central
- PaperMC: `https://repo.papermc.io/repository/maven-public/`
- YueMi: `https://repo.yuemi.my.id/repository/maven-public/`
- Momirealms: `https://repo.momirealms.net/releases/`
- PlaceholderAPI: `https://repo.extendedclip.com/content/repositories/placeholderapi/`

## Linting
SpotBugs (`com.github.spotbugs` 6.5.13, engine 4.10.5) gates the build. It runs at `effort = MAX` / `reportLevel = LOW` over main sources only — test sources are skipped because they mock Bukkit/Paper APIs.

```bash
./gradlew spotbugsMain              # all modules
./gradlew :core-plugin:spotbugsMain # one module
```

- Reports: `build/reports/spotbugs/main.html` and `main.xml` per module.
- `ignoreFailures = false`, so any finding fails the build (and CI).
- False positives go in `config/spotbugs/exclude.xml`, each entry commented with the detector and the reason. Real defects are fixed in the source.
- Per-site suppression uses `@SuppressFBWarnings("DETECTOR")` with a `justification`; `spotbugs-annotations` is `compileOnly` and never reaches a published POM or the shaded jar.

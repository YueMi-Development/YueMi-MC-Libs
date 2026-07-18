# YueMi Libs

A modern, high-performance, and extensible Minecraft server library plugin built for **PaperMC** (1.21+ / Java 21). It provides developer-friendly, unified abstraction layers for common plugin features like economies and items.

<a href="https://modrinth.com/plugin/yuemi-libs" target="_blank" rel="noopener noreferrer"><img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg"></a>

![](https://img.shields.io/bstats/players/32719?color=green&label=Players)
![](https://img.shields.io/bstats/servers/32719?color=green&label=Servers)

---

## Features

### 1. Unified Economy API
* **Extensible Registry**: Register custom economy backends or hook into existing plugins.
* **Vault Hook**: Built-in support for Vault integration.
* **Flexible Actions**: Standardized methods for checking, modifying, and setting player balances.

### 2. Multi-Namespace Item Provider API
* **Concurrent Providers**: Support multiple item registries at the same time using namespaced IDs (`namespace:id`).
* **Vanilla Minecraft Provider (`minecraft:`)**:
  * **Configurable Match Modes**: Match inventory items by `ID` (type), `CUSTOM_MODEL_DATA` (type + model data), or `NBT` (strict similarity).
  * **Advanced Parsing**: Automatically parses bracket format identifiers (e.g. `diamond_sword{CustomModelData:123}`).
* **CraftEngine Provider (`craftengine:`)**:
  * Seamlessly hooks into CraftEngine's custom items registry to retrieve, give, take, and count custom items.

### 3. Smart Configuration & Migration
* **Auto-Migration**: Features an incremental config migrator that upgrades older config configurations (up to v3) automatically on startup without losing user settings.

---

## Project Structure

This project uses a modular design to separate public interfaces from implementation details:

* **[core-api](file:///f:/Github-Repository/YueMi-MC-Libs/core-api)**: Contains public interfaces (e.g., `YueMiLibsApi`, `EconomyProvider`, `ItemProvider`) and static helper providers. Contains no server-side logic and is safe to use as a compile-only dependency.
* **[core-plugin](file:///f:/Github-Repository/YueMi-MC-Libs/core-plugin)**: Contains the main Paper plugin class (`onEnable`, `onDisable`), event listeners, and the concrete API implementations. It shades `core-api` into the final JAR for easy deployment.

---

## Developer API Usage

Add `core-api` to your project dependencies. Then, access the services using `YueMiLibsProvider`:

### Accessing the API
```java
import org.yuemi.libs.api.YueMiLibsApi;
import org.yuemi.libs.api.YueMiLibsProvider;

YueMiLibsApi api = YueMiLibsProvider.getApi();
```

### Economy API Example
```java
import org.yuemi.libs.api.economy.EconomyApi;

EconomyApi economy = YueMiLibsProvider.getApi().getEconomy();

if (economy.isAvailable()) {
    double balance = economy.getBalance(player);
    // Withdraw money
    if (economy.withdraw(player, 100.0)) {
        player.sendMessage("Successfully spent $100.00!");
    }
}
```

### Item Provider API Example
Give, take, or query items across different namespaces concurrently:

```java
import org.yuemi.libs.api.items.ItemsApi;
import org.bukkit.inventory.ItemStack;

ItemsApi items = YueMiLibsProvider.getApi().getItems();

// Get a vanilla diamond sword with custom model data
ItemStack customSword = items.getItem("minecraft:diamond_sword{CustomModelData:456}", 1);

// Give a CraftEngine custom item
boolean success = items.giveItem(player, "craftengine:my_custom_weapon", 1);

// Count how many custom items a player has
int count = items.getItemCount(player, "craftengine:my_custom_weapon");

// Take 5 items
boolean tookItems = items.takeItem(player, "craftengine:my_custom_weapon", 5);
```

---

## Configuration (`config.yml`)

Configure hooks, matching options, and third-party integrations:

```yaml
# YueMi Libs Configuration
config-version: 3

hooks:
  economy:
    enabled: true
    # The economy provider to hook (e.g. "Vault")
    provider: "Vault"
  items:
    # Whether to hook into CraftEngine to support its custom items
    craftengine:
      enabled: true

providers:
  minecraft:
    # Options:
    # - ID: Matches only by Material ID type (e.g. minecraft:stone)
    # - CUSTOM_MODEL_DATA: Matches by Material ID type and Custom Model Data
    # - NBT: Matches strictly (all metadata / NBT must match)
    match-mode: "ID"
```

---

## Compiling and Building

The project uses Gradle with Kotlin DSL and compiles using Java 21.

To build the plugin JAR (which is located at `core-plugin/build/libs/core-plugin-<version>.jar`):
```bash
# Windows
.\gradlew build

# Linux / macOS
./gradlew build
```
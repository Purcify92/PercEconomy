# PercEconomy
  **PercEconomy** is a lightweight and flexible economy plugin designed for Minecraft servers. It offers essential features like player balances, transaction commands, and admin tools, all with a clean and intuitive configuration system. 

---

## Features
- Player commands:
  - `/balance`: Check your current balance.
  - `/pay <player> <amount>`: Send money to another player.
- Admin commands (SOON):
  - `/economy give <player> <amount>`: Add money to a player's account.
  - `/economy take <player> <amount>`: Remove money from a player's account.
  - `/economy set <player> <amount>`: Set a player's balance.
- Supports (SOON):
  - Vault integration for compatibility with other plugins.
  - Configurable messages and currency format.
  - SQLite or MySQL storage options for flexibility.

---

## Installation
1. Download the latest release from the [Releases](https://github.com/Purcify92/PercEconomy/releases) page or build it yourself.
2. Place the `PercEconomy.jar` file into your server's `plugins` folder.
3. Restart the server to generate the configuration files.
4. Edit the `config.yml` in the `PercEconomy` folder to customize the plugin.
5. Restart the server to apply changes.

---

## Commands & Permissions
| Command                     | Permission               | Description                            |
|-----------------------------|--------------------------|----------------------------------------|
| `/balance [player]`         | `perceconomy.balance`    | View the balance of yourself           |
|                             |                          | or another player                      |   
| `/pay <player> <amount>`    | `perceconomy.pay`        | Send money to another player.          |

---

## Configuration
The `config.yml` file lets you customize the plugin to suit your server's needs. Key options include:
- **STARTING BALANCE**: Lets you configure how much money new players should start with.
Example `config.yml`:
```yaml
starting-balance: 1000
```
# PercEconomy
  **PercEconomy** is a lightweight and flexible economy plugin designed for Minecraft servers. It offers essential features like player balances, transaction commands, and admin tools, all with a clean and intuitive configuration system. 

---

## Features
- Player commands:
  - `/balance`: Check your current balance.
  - `/pay <player> <amount>`: Send money to another player.
- Admin commands:
  - `/economy give <player> <amount>`: Add money to a player's account.
  - `/economy take <player> <amount>`: Remove money from a player's account.
  - `/economy set <player> <amount>`: Set a player's balance.
  - `/economy reload`: Reload the configuration without having to restart the server.
- Supports:
  - Vault integration for compatibility with other plugins. **SOON**
  - Configurable messages and currency format. 
  - SQLite or MySQL storage options for flexibility. **SOON**

---

## Installation
1. Download the latest release from the [Releases](https://github.com/Purcify92/PercEconomy/releases) page or build it yourself.
2. Place the `PercEconomy.jar` file into your server's `plugins` folder.
3. Restart the server to generate the configuration files.
4. Edit the `config.yml` in the `PercEconomy` folder to customize the plugin.
5. Restart the server or use `/economy reload` to apply config changes.

---

## Commands & Permissions
| Command                     | Permission               | Description                            |
|-----------------------------|--------------------------|----------------------------------------|
| `/balance [player]`         | `perceconomy.balance`    | View the balance of yourself or another player.         | 
| `/pay <player> <amount>`    | `perceconomy.pay`        | Send money to another player.          |
| `/economy <give/take/set/reload> [player] [amount]`    | `perceconomy.admin`        | Allows you to manage the balance of other players and reload the plugin's config.          |

---
## Placeholders
These placeholders can be used to customize the messages in the plugin's configuration.
| Placeholder   | Represents                           |
|---------------|-------------------------------------|
| `{balance}`   | Player's current balance after transaction |
| `{player}`    | Target player's name                |
| `{sender}`    | Command executor's name             |
| `{receiver}`  | Recipient player's name             |
| `{amount}`    | Amount of currency involved         |
| `{currency}`  | Currency symbol defined in `config.yml` |
| `{target}`    | Name of a player not found          |
---

## Configuration
The `config.yml` file lets you customize the plugin to suit your server's needs. Key options include:
- **STARTING BALANCE**: Lets you configure how much money new players should start with.
- **CURRENCY SYMBOL**: Allows you to change the symbol used by the plugin.
- **MESSAGES**: Allows you to customize every message sent by the plugin.

Example `config.yml`:
```yaml
# The starting balance for new players
starting-balance: 1000.0

# Currency symbol
currency-symbol: "$"

# Messages
messages:
  balance:
    self: "&aYour balance is: &6{currency}{balance}"
    others: "&a{player}'s balance is: &6{currency}{balance}"
    usage: "&cUsage: /balance [player]"
    no-permission: "&cYou do not have permission to check others' balances."
    player-not-found: "&cPlayer {target} not found."
  pay:
    success: "&aYou have paid &6{currency}{amount} &ato &b{receiver}. Your new balance is &6{currency}{balance}"
    received: "&b{sender} &ahas paid you &6{currency}{amount}. Your new balance is &6{currency}{balance}"
    insufficient-funds: "&cYou do not have enough balance. Your balance is &6{currency}{balance}"
    invalid-amount: "&cInvalid amount."
    negative-amount: "&cAmount must be positive."
    pay-self: "&cYou cannot pay yourself."
    player-not-found: "&cPlayer {target} not found or not online."
    usage: "&cUsage: /pay <player> <amount>"
  economy:
    give: "&aYou have given &6{currency}{amount} &ato &b{player}. &aTheir new balance is &6{currency}{balance}"
    take: "&aYou have taken &6{currency}{amount} &afrom &b{player}. &aTheir new balance is &6{currency}{balance}"
    set: "&aYou have set &b{player}'s &abalance to &6{currency}{balance}"
    reload: "&aPlugin configuration reloaded."
    player-not-found: "&cPlayer {target} not found."
    invalid-amount: "&cInvalid amount."
    usage: "&cUsage: /economy <give|take|set|reload> [player] [amount]"
    no-permission: "&cYou do not have permission to use this command."

```

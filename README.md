# ContainerTracker
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/EmiCB/LockBar?label=Download&logo=github)](https://github.com/EmiCB/ContainerTracker/releases/latest)

A Minecraft plugin to track the contents (items and their positions) of container inventories. 
It will also track [physical actions](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/block/Action.html).

## Building
Compile a `.jar` from the commandline by doing an `install` via Maven:
```
$ mvn install
```
It should show up in the `target` directory. Make sure to update the version number.

---

## Commands

| Command            | Description        |
|--------------------|--------------------|
| `/ct-toggle-debug` | toggles debug mode |

Note: planning to refactor the command structure and possibly add more commands as needed 

## Config
### General
| Key            | Type           | Description                                        |
|----------------|----------------|----------------------------------------------------|
| `debug`        | `boolean`      | enable / disable debug mode                        |

####Example
```yaml
debug: true
```

### MySQL

| Key              | Type      | Description                     |
|------------------|-----------|---------------------------------|
| `mysql.host`     | `string`  | The host of the database        |
| `mysql.port`     | `integer` | The port of the database        |
| `mysql.database` | `string`  | The name of the database to use |
| `mysql.username` | `string`  | Username for credentials        |
| `mysql.password` | `string`  | Password for credentials        |

####Example
```yaml
mysql:
    host: localhost
    port: 3306
    database: minecraft
    username: user
    password: pass
```
## Database Tables
### whimc_action_physical
| Column        | Type      | Description                                                                                       |
|---------------|-----------|---------------------------------------------------------------------------------------------------|
| `rowid`       | `int`     | Unique row id for db entry                                                                        |
| `uuid`        | `string`  | UUID of player causing the interaction                                                            |
| `username`    | `string`  | Name of the player causing the interaction                                                        |
| `world`       | `string`  | Name of the world where the interaction occurred                                                  |
| `x`           | `double`  | The x position of the interaction                                                                 |
| `y`           | `double`  | The y position of the interaction (up/down height)                                                |
| `z`           | `double`  | The z position of the interaction                                                                 |
| `time`        | `big int` | The unix time stamp when the interaction occurred                                                 |
| `type`        | `string`  | The type of block interacted with (pressure plate, lever, or button)                              |
| `region_name` | `string`  | The names of the regions where the interaction took place (regions names are separated by spaces) |

### whimc_barrelbot_outcome
| Column             | Type      | Description                                                                 |
|--------------------|-----------|-----------------------------------------------------------------------------|
| `rowid`            | `int`     | Unique row id for db entry                                                  |
| `uuid`             | `string`  | UUID of player who completed the puzzle                                     |
| `username`         | `string`  | Name of the player who completed the puzzle                                 |
| `world`            | `string`  | Name of the world where the puzzle was completed                            |
| `x`                | `double`  | The x position of the player                                                |
| `y`                | `double`  | The y position of the player (up/down height)                               |
| `z`                | `double`  | The z position of the player                                                |
| `time`             | `big int` | The unix time stamp when the puzzle was completed                           |
| `outcome`          | `string`  | The outcome of the puzzle (currently only has Success)                      |
| `inventory_row_id` | `int`     | The last rowid in whimc_containers from the player before puzzle completion |
| `puzzle_name`      | `string`  | The name of puzzle that the player completed                                |

### whimc_containers
| Column           | Type      | Description                                                                                     |
|------------------|-----------|-------------------------------------------------------------------------------------------------|
| `rowid`          | `int`     | Unique row id for db entry                                                                      |
| `uuid`           | `string`  | UUID of player who closed an inventory                                                          |
| `username`       | `string`  | Name of the player who closed an inventory                                                      |
| `world`          | `string`  | Name of the world where the inventory was closed                                                |
| `x`              | `double`  | The x position of the inventory                                                                 |
| `y`              | `double`  | The y position of the inventory (up/down height)                                                |
| `z`              | `double`  | The z position of the inventory                                                                 |
| `time`           | `big int` | The unix time stamp when the inventory was closed                                               |
| `slot1-27`       | `string`  | The name of the block in the inventory slot (1 is the top left and 27 is the bottom right)      |
| `inventory_type` | `string`  | Name of the inventory (barrel or shulker)                                                       |
| `region_name`    | `string`  | The names of the regions where the inventory is located (regions names are separated by spaces) |

## Known Issues
- Weighted pressure plates spam the database
- Citizens can trigger physical interactions (working on an optional toggle for this)
- Double chests ar not supported
- Containers that are under the size of 27 slots throw an error and the contents do not get stored
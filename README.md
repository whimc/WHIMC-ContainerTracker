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

## Known Issues
- Weighted pressure plates spam the database
- Citizens can trigger physical interactions (working on an optional toggle for this)
- Double chests ar not supported
- Containers that are under the size of 27 slots throw an error and the contents do not get stored
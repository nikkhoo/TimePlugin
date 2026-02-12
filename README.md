# TimePlugin

A Paper Minecraft plugin that shows a Time Bar (boss bar) for each player.
## Features

- ✅ Starts automatically when a player joins
- ✅ Timer begins counting from the moment of login (join time)
- ✅ `/time show` and `/time hide` commands per player
- ✅ `/show` and `/hide` commands per player

## Requirements

- Paper 1.20.4 or higher
- Java 17 or higher
- Maven (for building)

## Installation

1. Clone or download this repository
2. Build the plugin: `mvn clean package`
3. Copy the JAR file from `target/` to your server's `plugins/` folder
4. Restart your server

## Configuration

Edit `config.yml` in the `plugins/LoginPlugin/` folder:

```yaml
plugin:
  time-bar:
    enabled: true
    title: "§eSession time: §a%time%s"
    progress-cycle-seconds: 60
    color: YELLOW
    style: SOLID
```

## Commands

### Player Commands

-`/time show` - show your time bar
-`/time hide` - hide your time bar

## Development

### Project Structure

```
TimePlugin/
├── pom.xml
├── plugin.yml
├── src/main/java/com/example/timeplugin/
│   ├── TimePlugin.java          (Main plugin class)
│   │
│   ├── commands/
│   │   ├── HideCommand.java
│   │   └── ShowCommand.java
│   │
│   ├── listeners/
│   │   ├── PlayerJoinListener.java
│   │   └── PlayerQuitListener.java
│   └── util/
│       └── TimeBarManager.java     
└── src/main/resources/
    └── config.yml
```

## Building

```bash
mvn clean package
```

The compiled JAR will be in the `target/` folder.

## License

MIT License

## Support

For issues and feature requests, please open an issue on GitHub.